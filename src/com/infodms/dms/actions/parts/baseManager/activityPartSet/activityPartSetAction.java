package com.infodms.dms.actions.parts.baseManager.activityPartSet;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.activityPartSet.activityPartSetDao;
import com.infodms.dms.dao.parts.baseManager.partSTOSet.partSTOSetDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.dao.parts.salesManager.PartReplacedDlrOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtPartSpecialDefinePO;
import com.infodms.dms.po.TtPartSpecialDlrRelPO;
import com.infodms.dms.po.TtPartVinPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理活动配件明细设置业务
 * @Description:CHANADMS
 * @Copyright: Copyright (c) 2010
 * @Date: 2013-7-6
 * @remark
 */
public class activityPartSetAction extends BaseImport {
    public Logger logger = Logger.getLogger(activityPartSetAction.class);
    private static final activityPartSetDao dao = activityPartSetDao.getInstance();

    private static final String PART_STO_SET_MAIN = "/jsp/parts/baseManager/activityPartSet/activityPartSetMain.jsp";//活动配件明细设置首页
    private static final String PART_STO_SET_MOD = "/jsp/parts/baseManager/activityPartSet/activityPartSetMod.jsp";//活动配件明细设置页面
    private static final String PART_SELECT_PAGE = "/jsp/parts/baseManager/activityPartSet/queryPartsForMod.jsp";//配件选择页面
    private static final String PART_SELECT_PAGE_FOR_REPLACED = "/jsp/parts/baseManager/activityPartSet/queryPartsForModReplaced.jsp";//配件选择页面
    private static final String CLOSE_PAGE = "/jsp/parts/baseManager/activityPartSet/activityPartSetClose.jsp";//活动关闭页面

    private static final String PART_STO_SET_ADD = "/jsp/parts/baseManager/activityPartSet/activityPartSetAdd.jsp";//活动配件明细设置ADD页面
    private static final String PART_ADD_PAGE = "/jsp/parts/baseManager/activityPartSet/queryPartsForAdd.jsp";//配件选择ADD页面
    private static final String REPART_ADD_PAGE = "/jsp/parts/baseManager/activityPartSet/queryReplacedPartsForAdd.jsp";//配件选择ADD页面
    private static final String VIN_UPLOAD_FILE = "/jsp/parts/salesManager/carFactorySalesManager/VINUploadFile.jsp";//配件选择ADD页面
    private static final String VENDER_SELECT_PAGE = "/jsp/parts/baseManager/activityPartSet/venderForAdd.jsp";//供应商选择页面
    private static List<Long> defIdList = new ArrayList<Long>();

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 跳转至活动配件明细设置页面
     */
    public void activityPartSetInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            Map<String, String> stateMapForPart = new LinkedHashMap<String, String>();
            stateMapForPart.put(Constant.ACTIVITY_PART_TYPE_01 + "", "装车配件");
            stateMapForPart.put(Constant.ACTIVITY_PART_TYPE_02 + "", "库存配件");

            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.PART_ACTIVITY_TYPE_REPLACED_01 + "", "替换件活动");

            act.setOutData("stateMapForPart", stateMapForPart);
            act.setOutData("stateMap", stateMap);
            act.setForword(PART_STO_SET_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "活动配件明细设置初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    public void activityPartSetCloseInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String descId = CommonUtils.checkNull(request.getParamValue("descId")); // 配件名称
            act.setOutData("descId", descId);
            act.setForword(CLOSE_PAGE);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "活动关闭初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 按条件查询活动配件明细信息
     */
    public void activityPartSetSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String actDescription = CommonUtils.checkNull(request.getParamValue("actDescription")); // 活动描述
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("partName")); // 配件名称
            String describe = CommonUtils.checkNull(request.getParamValue("describe")); // 活动描述 （MOD）
            String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 活动开始日期
            String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 活动结束日期
            String actType = CommonUtils.checkNull(request.getParamValue("state")); // 活动类型
            String partType = CommonUtils.checkNull(request.getParamValue("partTpye")); // 配件类型
            String actPartType = CommonUtils.checkNull(request.getParamValue("actPartType")); // 查询条件选择的配件类型
            String act_Code = CommonUtils.checkNull(request.getParamValue("act_Code")); // 活动编码
//			String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 活动结束日期

            StringBuffer sql = new StringBuffer();

            if (null != actDescription && !"".equals(actDescription)) {
                sql.append(" AND UPPER(SD.DESCRIBE) LIKE '%" + actDescription.toUpperCase() + "%' ");
            }

            if (null != describe && !"".equals(describe)) {
                sql.append(" AND UPPER(SD.DESCRIBE) = '" + describe.toUpperCase() + "' ");
            }

            if (null != actType && !"".equals(actType)) {
                sql.append(" AND UPPER(SD.ACTIVITY_TYPE) = '" + actType.toUpperCase() + "' ");
            }

            if (null != partType && !"".equals(partType)) {
                sql.append(" AND UPPER(SD.PART_TYPE) = '" + partType.toUpperCase() + "' ");
            } else {
                if (null != actPartType && !"".equals(actPartType)) {
                    sql.append(" AND UPPER(SD.PART_TYPE) = '" + actPartType.toUpperCase() + "' ");
                }
            }

            if (null != act_Code && !"".equals(act_Code)) {
                sql.append(" AND UPPER(SD.ACTIVITY_CODE) LIKE '%" + act_Code.toUpperCase() + "%' ");
            }

            if (null != partOldcode && !"".equals(partOldcode)) {
                sql.append(" AND UPPER(SD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
            }
            if (null != partName && !"".equals(partName)) {
                sql.append(" AND SD.PART_NAME LIKE '%" + partName + "%' ");
            }

            if (null != startDate && !"".equals(startDate)) {
                sql.append(" AND TO_CHAR(SD.START_DATE,'yyyy-MM-dd') >= '" + startDate + "' ");
            }
            if (null != endDate && !"".equals(endDate)) {
                sql.append(" AND TO_CHAR(SD.END_DATE,'yyyy-MM-dd') <= '" + endDate + "' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = dao.queryActivityPartSet(sql.toString(), Constant.PAGE_SIZE, curPage);
            act.setOutData("partType", partType);
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询人员类型信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void activityPartSetCloseSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            StringBuffer sql = new StringBuffer();
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryActivityPartSetClose(request, Constant.PAGE_SIZE, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询人员类型信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void queryActivityPartSetDtl() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String actDescription = CommonUtils.checkNull(request.getParamValue("actDescription")); // 活动描述
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("partName")); // 配件名称
            String describe = CommonUtils.checkNull(request.getParamValue("describe")); // 活动描述 （MOD）
            String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 活动开始日期
            String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 活动结束日期
            String actType = CommonUtils.checkNull(request.getParamValue("state")); // 活动类型
            String partType = CommonUtils.checkNull(request.getParamValue("partTpye")); // 配件类型
            String actPartType = CommonUtils.checkNull(request.getParamValue("actPartType")); // 查询条件选择的配件类型
            String act_Code = CommonUtils.checkNull(request.getParamValue("act_Code")); // 活动编码
//			String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 活动结束日期

            StringBuffer sql = new StringBuffer();

            if (null != actDescription && !"".equals(actDescription)) {
                sql.append(" AND UPPER(SD.DESCRIBE) LIKE '%" + actDescription.toUpperCase() + "%' ");
            }

            if (null != describe && !"".equals(describe)) {
                sql.append(" AND UPPER(SD.DESCRIBE) = '" + describe.toUpperCase() + "' ");
            }

            if (null != actType && !"".equals(actType)) {
                sql.append(" AND UPPER(SD.ACTIVITY_TYPE) = '" + actType.toUpperCase() + "' ");
            }

            if (null != partType && !"".equals(partType)) {
                sql.append(" AND UPPER(SD.PART_TYPE) = '" + partType.toUpperCase() + "' ");
            } else {
                if (null != actPartType && !"".equals(actPartType)) {
                    sql.append(" AND UPPER(SD.PART_TYPE) = '" + actPartType.toUpperCase() + "' ");
                }
            }

            if (null != act_Code && !"".equals(act_Code)) {
                sql.append(" AND UPPER(SD.ACTIVITY_CODE) LIKE '%" + act_Code.toUpperCase() + "%' ");
            }

            if (null != partOldcode && !"".equals(partOldcode)) {
                sql.append(" AND UPPER(SD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
            }
            if (null != partName && !"".equals(partName)) {
                sql.append(" AND SD.PART_NAME LIKE '%" + partName + "%' ");
            }

            if (null != startDate && !"".equals(startDate)) {
                sql.append(" AND SD.START_DATE>= to_date('" + startDate + "','yyyy-MM-dd HH24:mi:ss')");
            }
            if (null != endDate && !"".equals(endDate)) {
                sql.append(" AND SD.END_DATE <= to_date('" + endDate + "','yyyy-MM-dd HH24:mi:ss')");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = dao.queryActivityPartSetDtl(sql.toString(), Constant.PAGE_SIZE, curPage);
            act.setOutData("partType", partType);
            act.setOutData("status", CommonUtils.checkNull(request.getParamValue("status")));
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询人员类型信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-8
     * @Title : 删掉活动配件明细设置
     */
    public void delActivityPartSet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String defineId = CommonUtils.checkNull(request.getParamValue("defineId"));// 关系ID

            //TtPartSpecialDefinePO
            TtPartSpecialDefinePO delPO = new TtPartSpecialDefinePO();

            delPO.setDefId(Long.parseLong(defineId));

            dao.delete(delPO);

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "删掉活动配件明细设置失败 ");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-6
     * @Title : 更新活动配件明细设置
     */
    public void updateActivityPartSet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            Long userId = logonUser.getUserId();// 操作用户ID
            Date date = new Date();
            String defineIds[] = request.getParamValues("defineIds");// 关系ID
            String desId = CommonUtils.checkNull(request.getParamValue("desId"));// 描述ID
            String prvDescribe = CommonUtils.checkNull(request.getParamValue("prvDescribe")); // Old活动描述
            String describe = CommonUtils.checkNull(request.getParamValue("describe")); // New 活动描述
            String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 活动开始日期
            String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 活动结束日期
            String actCode = CommonUtils.checkNull(request.getParamValue("actCode")); // 活动结束日期
            String partType = CommonUtils.checkNull(request.getParamValue("actpartType")); // 活动结束日期
            String actType = CommonUtils.checkNull(request.getParamValue("activityType")); // 活动结束日期
            String BAND_ACT_CODE = CommonUtils.checkNull(request.getParamValue("BAND_ACT_CODE"));
            String dlrSelectVal = CommonUtils.checkNull(request.getParamValue("dlrSelectVal")); // 活动配件类型
            String[] dealerIds = request.getParamValues("dealerIds"); // 活动配件类型

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            //TtPartSpecialDefinePO
            TtPartSpecialDefinePO selPO = null;
            TtPartSpecialDefinePO updatePO = null;

            selPO = new TtPartSpecialDefinePO();
            updatePO = new TtPartSpecialDefinePO();

            selPO.setDescribe(prvDescribe);
            selPO.setDescribeId(Integer.parseInt(desId));

            updatePO.setDescribe(describe);
            updatePO.setActivityType(Integer.valueOf(actType));
            updatePO.setPartType(Integer.valueOf(partType));
            updatePO.setActivityCode(actCode);
            updatePO.setStartDate(sdf.parse(startDate));
            updatePO.setEndDate(sdf.parse(endDate));
            updatePO.setUpdateBy(userId);
            updatePO.setUpdateDate(date);
            updatePO.setBandActicode(BAND_ACT_CODE);
            updatePO.setAllDlr(Integer.valueOf(dlrSelectVal));

            dao.update(selPO, updatePO);

            if (null != defineIds && defineIds.length > 0) {
                for (int i = 0; i < defineIds.length; i++) {
                    String deftId = defineIds[i];// 序列ID
                    String sugNum = request.getParamValue("sugNum_" + deftId);//建议数量
                    String isneedFlag = CommonUtils.checkNull(request.getParamValue("isneedFlag_" + deftId)); // 是否需要回运
                    String isNormal = CommonUtils.checkNull(request.getParamValue("isNormal_" + deftId)); // 是否有效

                    if ("".equals(isNormal)) {
                        isNormal = Constant.IF_TYPE_NO + "";
                    }
                    selPO = new TtPartSpecialDefinePO();
                    updatePO = new TtPartSpecialDefinePO();

                    selPO.setDefId(Long.parseLong(deftId));

                    updatePO.setSpecQty(Long.parseLong(sugNum));
                    updatePO.setIsneedFlag(Integer.valueOf(isneedFlag));
                    updatePO.setIsNormal(Integer.valueOf(isNormal));

                    dao.update(selPO, updatePO);

                }
            }

            if ("0".equals(dlrSelectVal) && dealerIds.length > 0) {
                //插入活动和服务商关系表
                List<TtPartSpecialDlrRelPO> dlrRelPOs = new ArrayList<TtPartSpecialDlrRelPO>();
                for (int j = 0; j < dealerIds.length; j++) {
                    TtPartSpecialDlrRelPO dlrRelPO = new TtPartSpecialDlrRelPO();
                    dlrRelPO.setRelId(Long.valueOf(SequenceManager.getSequence("")));
                    dlrRelPO.setDescribeId(Long.valueOf(desId));
                    dlrRelPO.setDealerId(Long.valueOf(dealerIds[j]));
                    dlrRelPOs.add(dlrRelPO);
                }
                dao.insert(dlrRelPOs);
            } else {
                dao.insertRel(desId);
            }
            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "更新配件人员类型关系");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 跳转至活动配件明细设置修改页面
     */
    public void activityPartSetFormodInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String desId = CommonUtils.checkNull(request.getParamValue("desId"));// 描述ID
            TtPartSpecialDefinePO partSpecialDefinePO = new TtPartSpecialDefinePO();
            partSpecialDefinePO.setDescribeId(Integer.valueOf(desId));
            partSpecialDefinePO = (TtPartSpecialDefinePO) dao.select(partSpecialDefinePO).get(0);
            String describe = CommonUtils.checkNull(request.getParamValue("describe"));// 活动描述
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate")); // 活动开始日期
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); // 活动结束日期
            String actType = CommonUtils.checkNull(request.getParamValue("actType")); // 活动类型
            String partType = CommonUtils.checkNull(request.getParamValue("partType")); // 配件类型
            String actPartType = CommonUtils.checkNull(request.getParamValue("actPartType")); //查询条件选择的配件类型
            String actCode = CommonUtils.checkNull(request.getParamValue("actCode")); // 活动编码
            String status = CommonUtils.checkNull(request.getParamValue("status")); // 状态
            String bandAct = CommonUtils.checkNull(request.getParamValue("bandAct")); // 绑定编码
            StringBuffer sb = new StringBuffer();

            sb.append("SELECT DISTINCT ACTIVITY_CODE FROM TT_PART_SPECIAL_DEFINE WHERE PART_TYPE = 95621002");
            act.setOutData("stateMaps", dao.pageQuery(sb.toString(), null, getFunName()));
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.PART_ACTIVITY_TYPE_REPLACED_01.toString(), "替换件活动");
            act.setOutData("stateMap", stateMap);

            Map<String, String> stateMapForPart = new LinkedHashMap<String, String>();
            stateMapForPart.put(Constant.ACTIVITY_PART_TYPE_01.toString(), "装车配件");
            stateMapForPart.put(Constant.ACTIVITY_PART_TYPE_02.toString(), "库存配件");
            act.setOutData("stateMapForPart", stateMapForPart);

            act.setOutData("desId", desId);
            act.setOutData("describe", describe);
            act.setOutData("startDate", startDate.substring(0, 10));
            act.setOutData("endDate", endDate.substring(0, 10));
            act.setOutData("actTpye", actType);
            act.setOutData("partType", partType);
            act.setOutData("actPartType", actPartType);
            act.setOutData("actCode", actCode);
            act.setOutData("status", status);
            act.setOutData("aList", dao.getSpeRel(desId));
            act.setOutData("bandAct", CommonUtils.checkNull(partSpecialDefinePO.getBandActicode()));
            act.setOutData("allDlr", CommonUtils.checkNull(partSpecialDefinePO.getAllDlr()));

            act.setForword(PART_STO_SET_MOD);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "活动配件明细设置修改初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void closeActivity() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String descId = CommonUtils.checkNull(request.getParamValue("descId"));// 描述ID
            String relId = CommonUtils.checkNull(request.getParamValue("relId"));// 明细ID
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));// 全部和单个标记
            String command = CommonUtils.checkNull(request.getParamValue("command"));// 打开和关闭指令
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));// 服务商代码
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
            //全部关闭
            if ("2".equals(flag) && null != descId) {
                TtPartSpecialDefinePO definePO1 = new TtPartSpecialDefinePO();
                TtPartSpecialDefinePO definePO2 = new TtPartSpecialDefinePO();
                definePO1.setDescribeId(Integer.parseInt(descId));
                definePO2.setState(10011002);
                dao.update(definePO1, definePO2);

                TtPartSpecialDlrRelPO dlrRelPO1 = new TtPartSpecialDlrRelPO();
                TtPartSpecialDlrRelPO dlrRelPO2 = new TtPartSpecialDlrRelPO();
                dlrRelPO1.setDescribeId(Long.valueOf(descId));
                dlrRelPO2.setState(Constant.STATUS_DISABLE);
                dlrRelPO2.setStutus(0);

                dao.update(dlrRelPO1, dlrRelPO2);

                act.setOutData("curPage", curPage);
                act.setOutData("success", "成功");
                act.setForword(CLOSE_PAGE);
            }
            //单个部分关闭或打开
            if ("1".equals(flag) && null != relId && null != command) {
                TtPartSpecialDlrRelPO definePO1 = new TtPartSpecialDlrRelPO();
                TtPartSpecialDlrRelPO definePO2 = new TtPartSpecialDlrRelPO();
                definePO1.setRelId(Long.valueOf(relId));
                if ("0".equals(command)) {
                    definePO2.setState(10011002);
                    definePO2.setStutus(0);
                } else {
                    definePO2.setState(10011001);
                    definePO2.setStutus(1);
                }
                dao.update(definePO1, definePO2);
                act.setOutData("curPage", curPage);
                act.setOutData("success", "成功");
                act.setForword(CLOSE_PAGE);
            }
            //关闭制定代码服务商所有活动
            if ("3".equals(flag)) {
                TmDealerPO dealerPO = new TmDealerPO();
                dealerPO.setDealerCode(dealerCode);
                if (dao.select(dealerPO).size() > 0) {
                    TtPartSpecialDlrRelPO srcPo = new TtPartSpecialDlrRelPO();
                    TtPartSpecialDlrRelPO updatePo = new TtPartSpecialDlrRelPO();

                    srcPo.setDealerId(((TmDealerPO) dao.select(dealerPO).get(0)).getDealerId());
                    updatePo.setState(10011002);
                    updatePo.setStutus(0);
                    dao.update(srcPo, updatePo);
                    act.setOutData("success", "操作成功！");
                    act.setOutData("error", "");
                } else {
                    act.setOutData("success", "");
                    act.setOutData("error", "服务商代码不正确！");
                }
                act.setOutData("curPage", curPage);
            }
            //全部打开
            if ("4".equals(flag) && null != descId) {
                TtPartSpecialDefinePO definePO1 = new TtPartSpecialDefinePO();
                TtPartSpecialDefinePO definePO2 = new TtPartSpecialDefinePO();
                definePO1.setDescribeId(Integer.parseInt(descId));
                definePO1.setState(10011002);
                definePO2.setState(10011001);
                dao.update(definePO1, definePO2);

                TtPartSpecialDlrRelPO dlrRelPO1 = new TtPartSpecialDlrRelPO();
                TtPartSpecialDlrRelPO dlrRelPO2 = new TtPartSpecialDlrRelPO();
                dlrRelPO1.setDescribeId(Long.valueOf(descId));
                dlrRelPO1.setState(Constant.STATUS_DISABLE);
                dlrRelPO2.setState(Constant.STATUS_ENABLE);
                dlrRelPO2.setStutus(1);

                dao.update(dlrRelPO1, dlrRelPO2);

                act.setOutData("curPage", curPage);
                act.setOutData("success", "成功");
                act.setForword(CLOSE_PAGE);
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "活动配件明细设置修改初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 跳转至活动配件明细设置新增页面
     */
    public void activityPartSetAddInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.PART_ACTIVITY_TYPE_REPLACED_01.toString(), "替换件活动");
            act.setOutData("stateMap", stateMap);
            Map<String, String> stateMapForPart = new LinkedHashMap<String, String>();
            stateMapForPart.put(Constant.ACTIVITY_PART_TYPE_02.toString(), "库存配件");
            stateMapForPart.put(Constant.ACTIVITY_PART_TYPE_01.toString(), "装车配件");

            StringBuffer sb = new StringBuffer();
            sb.append("SELECT DISTINCT ACTIVITY_CODE FROM TT_PART_SPECIAL_DEFINE WHERE PART_TYPE = 95621002");

            act.setOutData("stateMaps", dao.pageQuery(sb.toString(), null, getFunName()));
            act.setOutData("stateMapForPart", stateMapForPart);
            act.setForword(PART_STO_SET_ADD);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "活动配件明细设置新增初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件信息弹出框 for Modify
     */
    public void queryPartsInit() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String desId = CommonUtils.checkNull(request.getParamValue("desId"));
            String activityType = CommonUtils.checkNull(request.getParamValue("activityType"));
            String describe = "";
            String startDate = "";
            String endDate = "";
            String actCode = "";
            String partType = "";

            String sqlStr = " AND SD.DESCRIBE_ID = '" + desId + "'";
            List<Map<String, Object>> sdList = dao.queryActivityPartSetList(sqlStr);

            if (null != sdList && sdList.size() > 0) {
                describe = sdList.get(0).get("DESCRIBE").toString();
                startDate = sdList.get(0).get("F_START_DATE").toString();
                endDate = sdList.get(0).get("F_END_DATE").toString();
                actCode = sdList.get(0).get("ACTIVITY_CODE").toString();
                partType = sdList.get(0).get("PART_TYPE").toString();
            }

            act.setOutData("desId", desId);
            act.setOutData("describe", describe);
            act.setOutData("startDate", startDate);
            act.setOutData("actCode", actCode);
            act.setOutData("partType", partType);
            act.setOutData("endDate", endDate);
            act.setOutData("activityType", activityType);
            if (Constant.PART_ACTIVITY_TYPE_REPLACED_01.toString().equals(activityType)) {
                act.setForword(PART_SELECT_PAGE_FOR_REPLACED);
            } else {
                act.setForword(PART_SELECT_PAGE);
            }
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件信息初始化MOD");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件信息弹出框 for Add
     */
    public void queryPartsForAddInit() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            Integer activ = 0;
            if (CommonUtils.checkNull(request.getParamValue("state")) != "") {
                activ = CommonUtils.parseInteger(CommonUtils.checkNull(request.getParamValue("state")));
            }
            //活动类型判断 替换件活动
            if (Constant.PART_ACTIVITY_TYPE_REPLACED_01.equals(activ)) {
                act.setForword(REPART_ADD_PAGE);
            } else {
                act.setForword(PART_ADD_PAGE);
            }
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件信息初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 返回配件信息
     */
    public void queryPartsDialog() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partolcode = CommonUtils.checkNull(request.getParamValue("partolcode")); // 配件编码
            String partcname = CommonUtils.checkNull(request.getParamValue("partcname")); // 配件名称

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            String oemId = Constant.OEM_ACTIVITIES;//主机厂ID
            PageResult<Map<String, Object>> ps = dao.getParts(partolcode, partcname,
                    13, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询人员信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 新增替换件活动配件（库存配件保存）
     */
    public void insertActivityReplacedPartSet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String errorExist = "";
        String actCodeExist = "";
        try {
            String[] parts = null;
            String desId = CommonUtils.checkNull(request.getParamValue("desId"));//描述ID
            String describe = CommonUtils.checkNull(request.getParamValue("describe"));//活动描述
            if (Constant.ACTIVITY_PART_TYPE_02.toString().equals(CommonUtils.checkNull(request.getParamValue("switch")))) {
                parts = CommonUtils.checkNull(request.getParamValue("parts")).split(","); //新增配件
            } else {
                parts = CommonUtils.checkNull(request.getParamValue("parts")).split(","); //新增配件
            }

            String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 活动开始日期
            String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 活动结束日期
            String actCode = CommonUtils.checkNull(request.getParamValue("actCode")); // 活动编码

            String actPartType = CommonUtils.checkNull(request.getParamValue("actPartType")); // 活动配件类型
            String dlrSelectVal = CommonUtils.checkNull(request.getParamValue("dlrSelectVal")); // 活动配件类型
            String[] dealerIds = request.getParamValues("dealerIds"); // 活动配件类型

            PartReplacedDlrOrderDao partReplacedDlrOrderDao = PartReplacedDlrOrderDao.getInstance();
            List<Map<String, Object>> list = partReplacedDlrOrderDao.getActCode();

            for (int i = 0; i < list.size(); i++) {
                if (actCode.equals(CommonUtils.checkNull(list.get(i).get("ACTIVITY_CODE")))) {
                    actCodeExist = "活动编码" + actCode + "已经存在!";
                }
            }
            if (CommonUtils.isEmpty(actCodeExist)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                Long userId = logonUser.getUserId();// 用户ID
                Date date = new Date();
                String str = " AND SD.DESCRIBE = '" + describe + "'";
                List<Map<String, Object>> spList = dao.getDesId(str);

                if (null != spList && spList.size() > 0) {
                    desId = spList.get(0).get("DESCRIBE_ID").toString();
                }

                if (null == desId || "".equals(desId) || "null".equals(desId)) {
                    String desIdTmp = dao.getMaxDesId().get(0).get("MAX_DESID").toString();
                    desId = (Integer.parseInt(desIdTmp) + 1) + "";
                }

                TtPartSpecialDefinePO insertPO = null;

                for (int i = 0; i < parts.length; i++) {
                    List<?> existlist = dao.getExistPO(describe, parts[i].toString());
                    if (existlist != null && existlist.size() > 0) {
                        TtPartSpecialDefinePO sdPo = (TtPartSpecialDefinePO) existlist.get(0);
                        errorExist += "活动" + sdPo.getDescribe() + "已经存在该" + sdPo.getPartName() + " ";
                    }
                }
                Long defId = 0l;

                if ("".equals(errorExist)) {
                    for (int i = 0; i < parts.length; i++) {
                        String partId = parts[i];
                        List<Map<String, Object>> List = partSTOSetDao.getInstance().getPartException(partId);

                        String partCode = List.get(0).get("PART_CODE").toString();
                        String partOldcode = List.get(0).get("PART_OLDCODE").toString();
                        String partName = List.get(0).get("PART_CNAME").toString();
                        String repartId = List.get(0).get("REPART_ID").toString();
                        String repartCode = List.get(0).get("REPART_CODE").toString();
                        String repartOldcode = List.get(0).get("REPART_OLDCODE").toString();
                        String repartName = List.get(0).get("REPART_CNAME").toString();
                        String isneedFlag = CommonUtils.checkNull(request.getParamValue("isneedFlag_" + partId)); // 是否需要回运
                        String isNormal = CommonUtils.checkNull(request.getParamValue("isNormal_" + partId)); // 是否可用
                        if ("".equals(isNormal)) {
                            isNormal = Constant.IF_TYPE_NO + "";
                        }
                        defId = Long.parseLong(SequenceManager.getSequence(""));
                        defIdList.add(defId);
                        insertPO = new TtPartSpecialDefinePO();
                        insertPO.setDefId(defId);
                        insertPO.setDescribeId(Integer.parseInt(desId));
                        insertPO.setDescribe(describe);
                        insertPO.setPartId(Long.parseLong(partId));
                        insertPO.setPartCode(partCode);
                        insertPO.setPartOldcode(partOldcode);
                        insertPO.setPartName(partName);
                        insertPO.setRepartId(Long.parseLong(repartId));
                        insertPO.setRepartCode(repartCode);
                        insertPO.setRepartOldcode(repartOldcode);
                        insertPO.setRepartName(repartName);
                        insertPO.setIsNormal(Integer.valueOf(isNormal));
                        insertPO.setAllDlr(Integer.valueOf(dlrSelectVal));
                        String sugnumint = CommonUtils.checkNull(request.getParamValue("sugNum_" + partId)); // 建议数量
                        if (CommonUtils.isEmpty(sugnumint)) {
                            insertPO.setSpecQty(1l);//默认 1
                        } else {
                            insertPO.setSpecQty(Long.valueOf(sugnumint));
                        }
                        insertPO.setStartDate(sdf.parse(startDate));
                        insertPO.setEndDate(sdf.parse(endDate));
                        insertPO.setCreateBy(userId);
                        insertPO.setCreateDate(date);
                        insertPO.setIsneedFlag(Integer.valueOf(isneedFlag));
                        insertPO.setPartType(Integer.valueOf(actPartType));
                        String BAND_ACT_CODE = CommonUtils.checkNull(request.getParamValue("BAND_ACT_CODE"));//活动描述
                        if (actPartType.equals("95621001")) {
                            insertPO.setBandActicode(BAND_ACT_CODE);
                        }
                        if (!CommonUtils.checkIsNullStr(actCode)) {
                            insertPO.setActivityCode(actCode);
                        }

                        insertPO.setActivityType(Constant.PART_ACTIVITY_TYPE_REPLACED_01);

                        dao.insert(insertPO);
                    }

                    if ("0".equals(dlrSelectVal) && dealerIds.length > 0) {
                        //插入活动和服务商关系表
                        List<TtPartSpecialDlrRelPO> dlrRelPOs = new ArrayList<TtPartSpecialDlrRelPO>();
                        for (int j = 0; j < dealerIds.length; j++) {
                            TtPartSpecialDlrRelPO dlrRelPO = new TtPartSpecialDlrRelPO();
                            dlrRelPO.setRelId(Long.valueOf(SequenceManager.getSequence("")));
                            dlrRelPO.setDescribeId(Long.valueOf(desId));
                            dlrRelPO.setDealerId(Long.valueOf(dealerIds[j]));
                            dlrRelPOs.add(dlrRelPO);
                        }
                        dao.insert(dlrRelPOs);
                    } else {
                        dao.insertRel(desId);
                    }
                } else {
                    BizException e1 = new BizException(errorExist);
                    throw e1;
                }

                String curPage = CommonUtils.checkNull(request
                        .getParamValue("curPage"));// 当前页
                if ("".equals(curPage)) {
                    curPage = "1";
                }
                act.setOutData("success", "true");
                act.setOutData("curPage", curPage);
                act.setForword(VIN_UPLOAD_FILE);
            } else {
                act.setOutData("actCodeExist", actCodeExist);
            }

        } catch (Exception e) {// 异常方法
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setOutData("actCodeExist", actCodeExist);
                act.setOutData("success", false);
                act.setException(e1);
//                act.setForword(VIN_UPLOAD_FILE);
                act.setOutData("flag", false);
                return;
            }
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "新增活动配件明细");
            logger.error(logonUser, e1);
            act.setForword(VIN_UPLOAD_FILE);
            act.setOutData("success", false);
            act.setOutData("erroe", e1.getMessage());
            act.setOutData("flag", false);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 新增替换件活动配件（导入）
     */
    public void insertActivityReplacedPartSetForImport() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String errorExist = "";
        String actCodeExist = "";
        try {
            String[] parts = null;
            String desId = CommonUtils.checkNull(request.getParamValue("desId"));//描述ID
            String describe = CommonUtils.checkNull(request.getParamValue("describe"));//活动描述
            if (Constant.ACTIVITY_PART_TYPE_02.toString().equals(CommonUtils.checkNull(request.getParamValue("switch")))) {
                parts = CommonUtils.checkNull(request.getParamValue("parts")).split(","); //新增配件
            } else {
                parts = request.getParamValues("parts"); //新增配件
            }

            String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 活动开始日期
            String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 活动结束日期
            String actCode = CommonUtils.checkNull(request.getParamValue("actCode")); // 活动编码

            String actPartType = CommonUtils.checkNull(request.getParamValue("actPartType")); // 活动配件类型

            PartReplacedDlrOrderDao partReplacedDlrOrderDao = PartReplacedDlrOrderDao.getInstance();
            List<Map<String, Object>> list = partReplacedDlrOrderDao.getActCode();

            for (int i = 0; i < list.size(); i++) {
                if (actCode.equals(CommonUtils.checkNull(list.get(i).get("ACTIVITY_CODE")))) {
                    actCodeExist = "活动编码" + actCode + "已经存在!";
                }
            }
            if (CommonUtils.isEmpty(actCodeExist)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                Long userId = logonUser.getUserId();// 用户ID
                Date date = new Date();
                String str = " AND SD.DESCRIBE = '" + describe + "'";
                List<Map<String, Object>> spList = dao.getDesId(str);

                if (null != spList && spList.size() > 0) {
                    desId = spList.get(0).get("DESCRIBE_ID").toString();
                }

                if (null == desId || "".equals(desId) || "null".equals(desId)) {
                    String desIdTmp = dao.getMaxDesId().get(0).get("MAX_DESID").toString();
                    desId = (Integer.parseInt(desIdTmp) + 1) + "";

                }

                TtPartSpecialDefinePO insertPO = null;

                for (int i = 0; i < parts.length; i++) {
                    List<?> existlist = dao.getExistPO(describe, parts[i].toString());
                    if (existlist != null && existlist.size() > 0) {
                        TtPartSpecialDefinePO sdPo = (TtPartSpecialDefinePO) existlist.get(0);
                        errorExist += "活动" + sdPo.getDescribe() + "已经存在该" + sdPo.getPartName() + " ";
                    }
                }
                Long defId = 0l;

                if ("".equals(errorExist)) {
                    for (int i = 0; i < parts.length; i++) {
                        String partId = parts[i];
                        List<Map<String, Object>> List = partSTOSetDao.getInstance().getPartException(partId);

                        String partCode = List.get(0).get("PART_CODE").toString();
                        String partOldcode = List.get(0).get("PART_OLDCODE").toString();
                        String partName = List.get(0).get("PART_CNAME").toString();
                        String repartId = List.get(0).get("REPART_ID").toString();
                        String repartCode = List.get(0).get("REPART_CODE").toString();
                        String repartOldcode = List.get(0).get("REPART_OLDCODE").toString();
                        String repartName = List.get(0).get("REPART_CNAME").toString();
                        String isneedFlag = CommonUtils.checkNull(request.getParamValue("isneedFlag_" + partId)); // 是否需要回运
                        defId = Long.parseLong(SequenceManager.getSequence(""));
                        defIdList.add(Long.valueOf(desId));
                        insertPO = new TtPartSpecialDefinePO();
                        insertPO.setDefId(defId);
                        insertPO.setDescribeId(Integer.parseInt(desId));
                        insertPO.setDescribe(describe);
                        insertPO.setPartId(Long.parseLong(partId));
                        insertPO.setPartCode(partCode);
                        insertPO.setPartOldcode(partOldcode);
                        insertPO.setPartName(partName);
                        insertPO.setRepartId(Long.parseLong(repartId));
                        insertPO.setRepartCode(repartCode);
                        insertPO.setRepartOldcode(repartOldcode);
                        insertPO.setRepartName(repartName);
                        String sugnumint = CommonUtils.checkNull(request.getParamValue("sugNum_" + partId)); // 建议数量
                        if (CommonUtils.isEmpty(sugnumint)) {
                            insertPO.setSpecQty(1l);//默认 1
                        } else {
                            insertPO.setSpecQty(Long.valueOf(sugnumint));
                        }
                        insertPO.setStartDate(sdf.parse(startDate));
                        insertPO.setEndDate(sdf.parse(endDate));
                        insertPO.setCreateBy(userId);
                        insertPO.setCreateDate(date);
                        insertPO.setIsneedFlag(Integer.valueOf(isneedFlag));
                        insertPO.setPartType(Integer.valueOf(actPartType));
                        if (!CommonUtils.checkIsNullStr(actCode)) {
                            insertPO.setActivityCode(actCode);
                        }

                        insertPO.setActivityType(Constant.PART_ACTIVITY_TYPE_REPLACED_01);

                        dao.insert(insertPO);
                    }
                } else {
                    BizException e1 = new BizException(errorExist);
                    throw e1;
                }

                String curPage = CommonUtils.checkNull(request
                        .getParamValue("curPage"));// 当前页
                if ("".equals(curPage)) {
                    curPage = "1";
                }
                uploadExcel();
                act.setOutData("success", "true");
                act.setOutData("curPage", curPage);
                act.setForword(VIN_UPLOAD_FILE);
            } else {
                BizException e = new BizException(actCodeExist);
                throw e;
            }

        } catch (Exception e) {// 异常方法
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setOutData("actCodeExist", e1.getMessage());
                act.setOutData("success", false);
                act.setException(e1);
                act.setForword(VIN_UPLOAD_FILE);
                act.setOutData("flag", false);
                return;
            }
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "新增活动配件明细");
            logger.error(logonUser, e1);
            act.setForword(VIN_UPLOAD_FILE);
            act.setOutData("success", false);
            act.setOutData("erroe", e1.getMessage());
            act.setOutData("flag", false);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 新增替换件活动配件(维护)
     */
    public void insertActivityReplacedPartSetForMod() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String errorExist = "";
        try {
            String parts = null;
            String desId = CommonUtils.checkNull(request.getParamValue("desId"));//描述ID
            String describe = CommonUtils.checkNull(request.getParamValue("describe"));//活动描述
            parts = request.getParamValue("parts"); //新增配件
            String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 活动开始日期
            String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 活动结束日期
            String actCode = CommonUtils.checkNull(request.getParamValue("actCode")); // 活动编码

            String actPartType = CommonUtils.checkNull(request.getParamValue("partType")); // 活动配件类型
            String[] dealerIds = request.getParamValues("dealerIds"); // 服务商ID

            PartReplacedDlrOrderDao partReplacedDlrOrderDao = PartReplacedDlrOrderDao.getInstance();
            List<Map<String, Object>> list = partReplacedDlrOrderDao.getActCode();
            String actCodeExist = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Long userId = logonUser.getUserId();// 用户ID
            Date date = new Date();
            String str = " AND SD.DESCRIBE = '" + describe + "'";
            List<Map<String, Object>> spList = dao.getDesId(str);

            if (null != spList && spList.size() > 0) {
                desId = spList.get(0).get("DESCRIBE_ID").toString();
            }

            if (null == desId || "".equals(desId) || "null".equals(desId)) {
                String desIdTmp = dao.getMaxDesId().get(0).get("MAX_DESID").toString();
                desId = (Integer.parseInt(desIdTmp) + 1) + "";

            }

            TtPartSpecialDefinePO insertPO = null;
            String partids[] = parts.split(",");
            for (int i = 0; i < partids.length; i++) {
                List existlist = dao.getExistPO(describe, partids[i]);
                if (existlist != null && existlist.size() > 0) {
                    TtPartSpecialDefinePO sdPo = (TtPartSpecialDefinePO) existlist.get(0);
                    errorExist += sdPo.getPartName() + "";
                }
            }

            Long defId = 0l;

            if ("".equals(errorExist)) {
                for (int i = 0; i < partids.length; i++) {
                    String partId = partids[i];
                    List<Map<String, Object>> List = partSTOSetDao.getInstance().getPartException(partId);

                    String partCode = List.get(0).get("PART_CODE").toString();
                    String partOldcode = List.get(0).get("PART_OLDCODE").toString();
                    String partName = List.get(0).get("PART_CNAME").toString();
                    String repartId = List.get(0).get("REPART_ID").toString();
                    String repartCode = List.get(0).get("REPART_CODE").toString();
                    String repartOldcode = List.get(0).get("REPART_OLDCODE").toString();
                    String repartName = List.get(0).get("REPART_CNAME").toString();
                    String isneedFlag = CommonUtils.checkNull(request.getParamValue("isneedFlag_" + partId)); // 是否需要回运
                    defId = Long.parseLong(SequenceManager.getSequence(""));
                    defIdList.add(defId);
                    insertPO = new TtPartSpecialDefinePO();
                    insertPO.setDefId(defId);
                    insertPO.setDescribeId(Integer.parseInt(desId));
                    insertPO.setDescribe(describe);
                    insertPO.setPartId(Long.parseLong(partId));
                    insertPO.setPartCode(partCode);
                    insertPO.setPartOldcode(partOldcode);
                    insertPO.setPartName(partName);
                    insertPO.setRepartId(Long.parseLong(repartId));
                    insertPO.setRepartCode(repartCode);
                    insertPO.setRepartOldcode(repartOldcode);
                    insertPO.setRepartName(repartName);
//						insertPO.setSpecQty(sugnumint);//默认 1
                    insertPO.setStartDate(sdf.parse(startDate));
                    insertPO.setEndDate(sdf.parse(endDate));
                    insertPO.setCreateBy(userId);
                    insertPO.setCreateDate(date);
                    insertPO.setIsneedFlag(Integer.valueOf(isneedFlag));
                    insertPO.setPartType(Integer.valueOf(actPartType));
                    if (!CommonUtils.checkIsNullStr(actCode)) {
                        insertPO.setActivityCode(actCode);
                    }

                    insertPO.setActivityType(Constant.PART_ACTIVITY_TYPE_REPLACED_01);

                    dao.insert(insertPO);
                }
            } else {
                act.setOutData("errorExist", errorExist);
            }

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
//				if(Constant.ACTIVITY_PART_TYPE_01.toString().equals(CommonUtils.checkNull(request.getParamValue("actPartType")))){
//					uploadExcel();
//				}
            act.setOutData("success", "true");
            act.setOutData("errorExist", errorExist);

            act.setOutData("curPage", curPage);
            act.setForword(VIN_UPLOAD_FILE);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "新增活动配件明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 新增活动配件
     */
    public void insertActivityPartSet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String errorExist = "";
        try {
            String desId = CommonUtils.checkNull(request.getParamValue("desId"));//描述ID
            String describe = CommonUtils.checkNull(request.getParamValue("describe"));//活动描述
            String parts = CommonUtils.checkNull(request.getParamValue("parts")); //新增配件
            String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 活动开始日期
            String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 活动结束日期

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Long userId = logonUser.getUserId();// 用户ID
            Date date = new Date();
            String str = " AND SD.DESCRIBE = '" + describe + "'";
            List<Map<String, Object>> spList = dao.getDesId(str);

            if (null != spList && spList.size() > 0) {
                desId = spList.get(0).get("DESCRIBE_ID").toString();
            }

            if (null == desId || "".equals(desId) || "null".equals(desId)) {
                String desIdTmp = dao.getMaxDesId().get(0).get("MAX_DESID").toString();
                desId = (Integer.parseInt(desIdTmp) + 1) + "";

            }

            // TtPartSpecialDefinePO
            TtPartSpecialDefinePO insertPO = null;

            String partsArr[] = parts.split(",");
            for (int i = 0; i < partsArr.length; i++) {
                List existlist = dao.getExistPO(describe, partsArr[i].toString());
                if (existlist != null && existlist.size() > 0) {
                    TtPartSpecialDefinePO sdPo = (TtPartSpecialDefinePO) existlist.get(0);
                    errorExist += sdPo.getPartName() + " ";
                }
            }
            if ("".equals(errorExist)) {
                for (int i = 0; i < partsArr.length; i++) {
                    String partId = partsArr[i];
                    String sqlStr = " AND PD.PART_ID = '" + partId + "' ";
                    List<Map<String, Object>> partList = dao.getPartList(sqlStr);
                    String partCode = partList.get(0).get("PART_CODE").toString();
                    String partOldcode = partList.get(0).get("PART_OLDCODE").toString();
                    String partName = partList.get(0).get("PART_CNAME").toString();
                    String sugNum = CommonUtils.checkNull(request.getParamValue("sugNum_" + partId));
                    long sugNumInt = 1;
                    if (null != sugNum && !"".equals(sugNum)) {
                        sugNumInt = Long.parseLong(sugNum);
                    }

                    insertPO = new TtPartSpecialDefinePO();
                    insertPO.setDefId(Long.parseLong(SequenceManager.getSequence("")));
                    insertPO.setDescribeId(Integer.parseInt(desId));
                    insertPO.setDescribe(describe);
                    insertPO.setPartId(Long.parseLong(partId));
                    insertPO.setPartCode(partCode);
                    insertPO.setPartOldcode(partOldcode);
                    insertPO.setPartName(partName);
                    insertPO.setSpecQty(sugNumInt);//默认 1
                    insertPO.setStartDate(sdf.parse(startDate));
                    insertPO.setEndDate(sdf.parse(endDate));
                    insertPO.setCreateBy(userId);
                    insertPO.setCreateDate(date);

                    dao.insert(insertPO);
                }
            }

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("errorExist", errorExist);// 关系记录存在
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "新增活动配件明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-13
     * @Title : 导出活动配件设置信息
     */
    public void exportActPartExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String actDescription = CommonUtils.checkNull(request.getParamValue("actDescription")); // 活动描述
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("partName")); // 配件名称
            String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 活动开始日期
            String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 活动结束日期

            StringBuffer sql = new StringBuffer();

            if (null != actDescription && !"".equals(actDescription)) {
                sql.append(" AND UPPER(SD.DESCRIBE) LIKE '%" + actDescription.toUpperCase() + "%' ");
            }

            if (null != partOldcode && !"".equals(partOldcode)) {
                sql.append(" AND UPPER(SD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
            }
            if (null != partName && !"".equals(partName)) {
                sql.append(" AND SD.PART_NAME LIKE '%" + partName + "%' ");
            }

            if (null != startDate && !"".equals(startDate)) {
                sql.append(" AND TO_CHAR(SD.START_DATE,'yyyy-MM-dd') >= '" + startDate + "' ");
            }
            if (null != endDate && !"".equals(endDate)) {
                sql.append(" AND TO_CHAR(SD.END_DATE,'yyyy-MM-dd') <= '" + endDate + "' ");
            }

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "活动描述";
            head[2] = "活动编号";
            head[3] = "活动类型";
            head[4] = "活动配件类型";
            head[5] = "配件编码";
            head[6] = "配件名称";
            head[7] = "件号";
            head[8] = "建议数量";
            head[9] = "是否需要回运";
            head[10] = "开始日期";
            head[11] = "结束日期";

            List<Map<String, Object>> list = dao.queryActivityPartSetList(sql.toString());

            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("DESCRIBE"));
                        detail[2] = CommonUtils.checkNull(map.get("ACTIVITY_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("ACTIVITY_TYPE"));
                        detail[4] = CommonUtils.checkNull(map.get("PART_TYPE_DESC"));
                        detail[5] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[6] = CommonUtils.checkNull(map.get("PART_NAME"));
                        detail[7] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[8] = CommonUtils.checkNull(map.get("SPEC_QTY"));
                        detail[9] = CommonUtils.checkNull(map.get("ISNEED_FLAG"));
                        detail[10] = CommonUtils.checkNull(map.get("F_START_DATE"));
                        detail[11] = CommonUtils.checkNull(map.get("F_END_DATE"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "活动配件明细设置信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出活动配件明细设置信息异常!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param : @param response
     * @param : @param request
     * @param : @param head
     * @param : @param list
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 文件导出为xls文件
     */
    public static Object exportEx(String fileName, ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = fileName + ".xls";
        jxl.write.WritableWorkbook wwb = null;
        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename="
                    + URLEncoder.encode(name, "utf-8"));
            out = response.getOutputStream();
            wwb = Workbook.createWorkbook(out);
            jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

            if (head != null && head.length > 0) {
                for (int i = 0; i < head.length; i++) {
                    ws.addCell(new Label(i, 0, head[i]));
                }
            }
            int pageSize = list.size() / 30000;
            for (int z = 1; z < list.size() + 1; z++) {
                String[] str = list.get(z - 1);
                for (int i = 0; i < str.length; i++) {
                        /*ws.addCell(new Label(i, z, str[i]));*/ //modify by yuan
                    if (CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])) {
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
                    } else {
                        ws.addCell(new Label(i, z, str[i]));
                    }
                }
            }
            wwb.write();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (null != wwb) {
                wwb.close();
            }
            if (null != out) {
                out.close();
            }
        }
        return null;
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @Date : 2013-4-3
     * @Title : 导出EXECEL模板
     * @Description: TODO
     */
    public void exportExcelTemplate() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();
            List<List<Object>> list = new LinkedList<List<Object>>();

            //标题
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
            listHead.add("VIN");
//            listHead.add("切换数量");
//            listHead.add("备注");
            list.add(listHead);
            // 导出的文件名
            String fileName = "VIN导入模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
            logger.error(logonUser, e1);
            act.setException(e1);
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void uploadExcel() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        act.setOutData("flag", true);
//        try {
        long maxSize = 1024 * 1024 * 5;
        int errNum = insertIntoTmp(request, "uploadFile1", 1, 2, maxSize);
        String err = "";
        if (errNum != 0) {
            switch (errNum) {
                case 1:
                    err += "文件列数过多!";
                    break;
                case 2:
                    err += "空行不能大于三行!";
                    break;
                case 3:
                    err += "文件不能为空!";
                    break;
                case 4:
                    err += "文件不能为空!";
                    break;
                case 5:
                    err += "文件不能大于!";
                    break;
                default:
                    break;
            }
        }
        if (!"".equals(err)) {
            act.setOutData("error", err);
            BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
            throw e1;
        }
        List excelList = this.getMapList();
        //插入上传date
        loadDataList(excelList);
    }

    /**
     * @param : @param voList
     * @param : @param list
     * @param : @param errorInfo
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :循环获取CELL生成VO
     * @Description: TODO
     */
    private Map<String, Object> loadDataList(List<Map<String, Cell[]>> list) throws Exception {
        List<Map<String, Object>> dataList = new ArrayList();
        Map<String, Object> rtnMap = new HashMap();
        String error = "";
        if (null == list) {
            list = new ArrayList<Map<String, Cell[]>>();
        }
        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            if (null == map) {
                map = new HashMap<String, Cell[]>();
            }
            Set<String> keys = map.keySet();
            Iterator it = keys.iterator();
            String key = "";
//            int j = 0 ;
//            int index=0;
//            while (it.hasNext()) {
//                key = (String) it.next();
//                index++;
//            }
//            if(index!=defIdList.size()){
//            	error += "输入的VIN编码数量和配件数量不匹配</br>";
//            	BizException e1 = new BizException("导入的VIN编码数量和配件数量不匹配");
//            	defIdList.clear();
//            	throw e1;
//            }else{
            while (it.hasNext()) {
                key = (String) it.next();
                Cell[] cells = (Cell[]) map.get(key);
                try {
//            			parseCells(dataList, key, cells,defIdList.get(Integer.valueOf(key)-2));
                    parseCells(dataList, key, cells, defIdList.get(0));
//            			j++;
                } catch (Exception e) {
                    if (e instanceof BizException) {
                        BizException e1 = (BizException) e;
                        error += "上传文件," + e1.getMessage().replaceAll("操作失败！失败原因：", "") + "</br>";
                        continue;
                    } else {
                        throw e;
                    }
                }
            }
//            }
        }
        defIdList.clear();
        rtnMap.put("dataList", dataList);
        rtnMap.put("error", error);
        return rtnMap;
    }

    /**
     * @param object
     * @param :      @param list
     * @param :      @param rowNum
     * @param :      @param cells
     * @param :      @param errorInfo
     * @param :      @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 装载VO
     * @Description: TODO
     */
    private void parseCells(List<Map<String, Object>> list, String rowNum, Cell[] cells, Long desId) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        RequestWrapper request = act.getRequest();
        if ("" == CommonUtils.checkNull(cells[0].getContents())) {
            BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "请输入VIN");
            throw e;
        }

        TtPartVinPO po = new TtPartVinPO();
        po.setVin((CommonUtils.checkNull(cells[0].getContents())));
        po.setDefId(desId);
        dao.insert(po);
    }

    public String getFunName() {
        StackTraceElement stack[] = new Throwable().getStackTrace();
        StackTraceElement ste = stack[1];
        //System.out.println(ste.getClassName() + "." + ste.getMethodName() + ste.getLineNumber());
        StringBuilder strBuilder = new StringBuilder();
        return strBuilder.append(ste.getClassName()).
                append(".").append(ste.getMethodName()).
                append(ste.getLineNumber()).
                toString();
    }
}
