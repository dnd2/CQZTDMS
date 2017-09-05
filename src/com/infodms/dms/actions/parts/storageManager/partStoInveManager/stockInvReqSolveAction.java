package com.infodms.dms.actions.parts.storageManager.partStoInveManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.storageManager.partStoInveManager.stockInvReqSolveDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartCheckResultDtlPO;
import com.infodms.dms.po.TtPartCheckResultMainPO;
import com.infodms.dms.po.TtPartRecordPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理配件库存盘点调整处理业务
 * @Date: 2013-5-6
 * @remark
 */
public class stockInvReqSolveAction extends BaseImport {
    public Logger logger = Logger.getLogger(stockInvReqSolveAction.class);
    private static final stockInvReqSolveDao dao = stockInvReqSolveDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    private static final int PAGE_SIZE = 20;
    private static final int type1 = Constant.PART_STOCK_INVE_TYPE_01;//全部
    private static final int type2 = Constant.PART_STOCK_INVE_TYPE_02;//动态
    private static final int state1 = Constant.PART_STOCK_INVE_STATE_01;//已保存
    private static final int state2 = Constant.PART_STOCK_INVE_STATE_02;//盘点中
    private static final int state3 = Constant.PART_STOCK_INVE_STATE_03;//已盘点
    private static final int resolveType1 = Constant.PART_INVE_RESOLVE_TYPE_01;//封存
    private static final int resolveType2 = Constant.PART_INVE_RESOLVE_TYPE_02;//盈亏出入库
    private static final int orderState1 = Constant.PART_INVE_ORDER_STATE_01;//已保存
    private static final int orderState2 = Constant.PART_INVE_ORDER_STATE_02;//审核中
    private static final int orderState3 = Constant.PART_INVE_ORDER_STATE_03;//处理中
    private static final int orderState4 = Constant.PART_INVE_ORDER_STATE_04;//已驳回
    private static final int orderState5 = Constant.PART_INVE_ORDER_STATE_05;//已关闭
    private static final int orderState6 = Constant.PART_INVE_ORDER_STATE_06;//已完成
    private static final int orderState7 = Constant.PART_INVE_ORDER_STATE_07;//已作废
    private static final int limitLineNum = 300;

    //配件库存盘点调整处理
    private static final String PART_STOCK_MAIN = "/jsp/parts/storageManager/partStoInveManager/stockInvReqSolve/stockInvReqSolve.jsp";//配件库存盘点调整处理首页
    private static final String PART_STOCK_VIEW = "/jsp/parts/storageManager/partStoInveManager/stockInvReqSolve/stockInvReqSolView.jsp";//库存盘点调整处理查看页面
    private static final String PART_STOCK_HANDLE = "/jsp/parts/storageManager/partStoInveManager/stockInvReqSolve/stockInvReqSolPg.jsp";//库存盘点调整处理操作页面

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-5-6
     * @Title : 跳转至配件库存盘点调整处理页面
     */
    public void stockInvReqSolveInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            String companyName = ""; //制单单位
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;
                companyName = dao.getMainCompanyName(parentOrgId);
            } else {
                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();
                companyName = dao.getDealerName(parentOrgId);
            }
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());


            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("companyName", companyName);
            act.setOutData("WHList", WHList);
            act.setForword(PART_STOCK_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件库存盘点调整处理页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-7
     * @Title : 查询配件库存盘点调整处理信息
     */
    public void stockInvReqSolveSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String inveCode = CommonUtils.checkNull(request
                    .getParamValue("inveCode")); // 盘点单号
            String resultCode = CommonUtils.checkNull(request
                    .getParamValue("resultCode")); // 申请单号
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 截止时间
            String whId = CommonUtils.checkNull(request
                    .getParamValue("whId")); // 仓库ID
            String inveType = CommonUtils.checkNull(request
                    .getParamValue("inveType")); // 盘点类型
            String checkName = CommonUtils.checkNull(request
                    .getParamValue("checkName")); // 导入人
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            StringBuffer sbString = new StringBuffer();
            if (null != inveCode && !"".equals(inveCode)) {
                sbString.append(" AND TM.CHANGE_CODE like '%" + inveCode + "%' ");
            }
            if (null != resultCode && !"".equals(resultCode)) {
                sbString.append(" AND TM.RESULT_CODE LIKE '%" + resultCode + "%' ");
            }
            if (null != checkName && !"".equals(checkName)) {
                sbString.append(" AND KU.NAME LIKE '%" + checkName + "%' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(TM.CHECK_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(TM.CHECK_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND TM.WH_ID = '" + whId + "' ");
            }
            if (null != inveType && !"".equals(inveType)) {
                sbString.append(" AND TM.CHECK_TYPE = '" + inveType + "' ");
            }
            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartStockInve(
                    sbString.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件库存盘点调整处理信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-6
     * @Title : 跳转至库存盘点调整处理查看页面
     */
    public void viewStockDeatilInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String resultId = CommonUtils.checkNull(request.getParamValue("resultId"));// 盘点结果单ID
            String optionType = CommonUtils.checkNull(request.getParamValue("optionType"));// 访问类型
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.RESULT_ID = '" + resultId + "' ");
            Map<String, Object> map = dao.queryAllPartStockInve(sbString.toString()).get(0);

            int type = Integer.parseInt(map.get("CHECK_TYPE").toString());
            int handleType = Integer.parseInt(map.get("HANDLE_TYPE").toString());
            int orederState = Integer.parseInt(map.get("STATE").toString());

            if (type1 == type) {
                map.put("CHECK_TYPE", "全部");
            } else if (type2 == type) {
                map.put("CHECK_TYPE", "动态");
            }

            if (resolveType1 == handleType) {
                map.put("HANDLE_TYPE", "封存");
            } else if (resolveType2 == handleType) {
                map.put("HANDLE_TYPE", "处理");
            }

            if (orderState1 == orederState) {
                map.put("STATE", "已保存");
            } else if (orderState2 == orederState) {
                map.put("STATE", "审核中");
            } else if (orderState3 == orederState) {
                map.put("STATE", "处理中");
            } else if (orderState4 == orederState) {
                map.put("STATE", "已驳回");
            } else if (orderState5 == orederState) {
                map.put("STATE", "已关闭");
            } else if (orderState6 == orederState) {
                map.put("STATE", "已完成");
            } else if (orderState7 == orederState) {
                map.put("STATE", "已作废");
            }

            act.setOutData("map", map);
            if ("view".equalsIgnoreCase(optionType)) {
                act.setForword(PART_STOCK_VIEW);
            } else if ("handle".equalsIgnoreCase(optionType)) {
                act.setForword(PART_STOCK_HANDLE);
            }
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "库存盘点调整处理查看页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-6
     * @Title : 关闭库存盘点调整处理结果
     */
    public void closeInveRes() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String errorExist = "";
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            String resultId = CommonUtils.checkNull(req.getParamValue("resultId")); //盘点单ID
            Long userId = logonUser.getUserId(); //操作人ID
            Date date = new Date();

            TtPartCheckResultMainPO selCMPo = null;
            TtPartCheckResultMainPO updCMPo = null;

            String sbStr = "";
            sbStr = " AND TM.RESULT_ID = '" + resultId + "' ";
            List<Map<String, Object>> resMainList = dao.getPartCheckResMainList(sbStr);

            if (null != resMainList && resMainList.size() == 1) {
                selCMPo = new TtPartCheckResultMainPO();
                updCMPo = new TtPartCheckResultMainPO();

                selCMPo.setResultId(Long.parseLong(resultId));

                updCMPo.setUpdateBy(userId);
                updCMPo.setUpdateDate(date);
                updCMPo.setDeleteBy(userId);//关闭
                updCMPo.setDeleteDate(date);
                updCMPo.setState(orderState5);

                dao.update(selCMPo, updCMPo);
            } else {
                errorExist = "申请单已处理,不能重复处理!";
            }

            act.setOutData("errorExist", errorExist);//返回错误信息
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "关闭库存盘点处理结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-6
     * @Title : 提交废库存盘点调整处理结果
     */
    public void commitHandleResult() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String success = "";
        String errorExist = "";
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            int inveProfile = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02; //盘盈
            String handleType = CommonUtils.checkNull(req.getParamValue("handleType")); //处理方式(单个或全部)
            String resultId = CommonUtils.checkNull(req.getParamValue("resultId")); //盘点单ID
            String resultCode = CommonUtils.checkNull(req.getParamValue("resultCode")); //盘点单单号

            String sbStr = "";
            sbStr = " AND TM.RESULT_ID = '" + resultId + "' ";
            List<Map<String, Object>> resMainList = dao.getPartCheckResMainList(sbStr);

            if (null != resMainList && resMainList.size() == 1) {
                int solveType = Integer.parseInt(resMainList.get(0).get("HANDLE_TYPE").toString());
                int partRecState = 1; //配件出入库记录状态
                if (resolveType1 == solveType) {
                    partRecState = 3;
                }
                String parentOrgId = resMainList.get(0).get("CHGORG_ID").toString(); //制单单位ID
                String parentOrgCode = resMainList.get(0).get("CHGORG_CODE").toString(); //制单单位编码
                String chgorgCname = resMainList.get(0).get("CHGORG_CNAME").toString(); //制单单位名称
                String whId = resMainList.get(0).get("WH_ID").toString(); //仓库ID
                String whName = resMainList.get(0).get("WH_CNAME").toString(); //仓库ID

                String configId = Constant.PART_CODE_RELATION_24 + "";//配置ID
                String partBatch = "";//配件批次
                String partVenId = "";//供应商ID
                Long userId = logonUser.getUserId(); //操作人ID
                String name = logonUser.getName();
                Date date = new Date();

                List<Map<String, Object>> resDtlList = null;
                List<Map<String, Object>> warLocList = null;
                TtPartCheckResultMainPO selCRMPo = null;
                TtPartCheckResultMainPO updCRMPo = null;
                TtPartCheckResultDtlPO selCRDPo = null;
                TtPartCheckResultDtlPO updCRDPo = null;
                TtPartRecordPO insertPRPo = null;

                List ins = null;

                if ("all".equalsIgnoreCase(handleType)) {
                    success = "all";
                    sbStr = " AND TD.RESULT_ID = '" + resultId + "' ";
                    resDtlList = dao.getPartCheckResDtlList(sbStr);

                    if (null != resDtlList && resDtlList.size() > 0) {
                        for (int i = 0; i < resDtlList.size(); i++) {
                            String detailId = resDtlList.get(i).get("DTL_ID").toString();//盘点结果详情ID
                            long partId = Long.parseLong(resDtlList.get(i).get("PART_ID").toString());//配件ID
                            String partCode = resDtlList.get(i).get("PART_CODE").toString();//件号
                            String partOldcode = resDtlList.get(i).get("PART_OLDCODE").toString();//配件编码
                            String partCname = resDtlList.get(i).get("PART_CNAME").toString();//配件名称
                            String unit = resDtlList.get(i).get("UNIT").toString();//配件单位
                            
                            partVenId = resDtlList.get(i).get("VENDER_ID").toString(); // 供应商id
                            partBatch = resDtlList.get(i).get("BATCH_CODE").toString(); // 批次号
                            
                            long checkQty = Long.parseLong(resDtlList.get(i).get("CHECK_QTY").toString());//盘点库存
                            long diffQty = Math.abs(Long.parseLong(resDtlList.get(i).get("DIFF_QTY").toString()));//盈亏库存（出入库数量）
                            int checkResult = Integer.parseInt(resDtlList.get(i).get("CHECK_RESULT").toString());//盘点结果

                            
                            
                            StringBuffer sb = new StringBuffer();
                            sb.append(" AND LD.PART_ID = '" + partId + "' ");
                            sb.append(" AND WD.WH_ID = '" + whId + "' ");
                            sb.append(" AND WD.ORG_ID = '" + parentOrgId + "' ");
                            warLocList = dao.getWareLocaInfos(sb.toString());
                            String whCode = warLocList.get(0).get("WH_CODE").toString();
                            Long locId = Long.parseLong(warLocList.get(0).get("LOC_ID").toString());
                            String locCode = warLocList.get(0).get("LOC_CODE").toString();
                            String locName = warLocList.get(0).get("LOC_NAME").toString();

                            //盘点结果详情表
                            selCRDPo = new TtPartCheckResultDtlPO();

                            updCRDPo = new TtPartCheckResultDtlPO();

                            selCRDPo.setDtlId(Long.parseLong(detailId));

                            updCRDPo.setIsOver(1); //已处理
                            updCRDPo.setUpdateBy(userId);
                            updCRDPo.setUpdateDate(date);

                            dao.update(selCRDPo, updCRDPo);

                            int inOrOutFlag = 1;
                            if (partRecState != 3 && partRecState != 4) {
                                if (inveProfile != checkResult) {
                                    inOrOutFlag = 2;
                                }
                                //配件出入库记录表
                                insertPRPo = new TtPartRecordPO();

                                Long recId = Long.parseLong(SequenceManager.getSequence(""));
                                insertPRPo.setRecordId(recId);
                                insertPRPo.setAddFlag(inOrOutFlag);//出入库
                                insertPRPo.setPartId(partId);
                                insertPRPo.setPartCode(partCode);
                                insertPRPo.setPartOldcode(partOldcode);
                                insertPRPo.setPartName(partCname);
                                insertPRPo.setPartBatch(partBatch);
                                insertPRPo.setVenderId(Long.parseLong(partVenId));
                                insertPRPo.setPartNum(diffQty);//出入库数量
                                insertPRPo.setConfigId(Long.parseLong(configId));
                                insertPRPo.setOrderId(Long.parseLong(resultId));//盘点结果主表ID
                                insertPRPo.setOrderCode(resultCode); //盘点结果主表盘点单号
                                insertPRPo.setLineId(Long.parseLong(detailId));//盘点结果详情ID
                                insertPRPo.setOrgId(Long.parseLong(parentOrgId));
                                insertPRPo.setOrgCode(parentOrgCode);
                                insertPRPo.setOrgName(chgorgCname);
                                insertPRPo.setWhId(Long.parseLong(whId));
                                insertPRPo.setWhCode(whCode);
                                insertPRPo.setWhName(whName);
                                insertPRPo.setLocId(locId);
                                insertPRPo.setLocCode(locCode);
                                insertPRPo.setLocName(locName);
                                insertPRPo.setOptDate(date);
                                insertPRPo.setCreateDate(date);
                                insertPRPo.setPersonId(userId);
                                insertPRPo.setPersonName(name);
                                insertPRPo.setPartState(partRecState);//配件状态

                                dao.insert(insertPRPo);

                                //调用出入库逻辑
                                ins = new LinkedList<Object>();
                                ins.add(0, resultId);
                                ins.add(1, configId);

                                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
                            } else {
                                //封存 盘盈 处理
                                if (inveProfile == checkResult) {
                                    //配件出入库记录表
                                    insertPRPo = new TtPartRecordPO();
                                    partRecState = 3; //盘盈封存标识

                                    Long recId = Long.parseLong(SequenceManager.getSequence(""));
                                    insertPRPo.setRecordId(recId);
                                    insertPRPo.setAddFlag(inOrOutFlag);//出入库
                                    insertPRPo.setPartId(partId);
                                    insertPRPo.setPartCode(partCode);
                                    insertPRPo.setPartOldcode(partOldcode);
                                    insertPRPo.setPartName(partCname);
                                    insertPRPo.setPartBatch(partBatch);
                                    insertPRPo.setVenderId(Long.parseLong(partVenId));
                                    insertPRPo.setPartNum(diffQty);//入库数量
                                    insertPRPo.setConfigId(Long.parseLong(configId));
                                    insertPRPo.setOrderId(Long.parseLong(resultId));//盘点结果主表ID
                                    insertPRPo.setLineId(Long.parseLong(detailId));//盘点结果详情ID
                                    insertPRPo.setOrderCode(resultCode); //盘点结果主表盘点单号
                                    insertPRPo.setOrgId(Long.parseLong(parentOrgId));
                                    insertPRPo.setOrgCode(parentOrgCode);
                                    insertPRPo.setOrgName(chgorgCname);
                                    insertPRPo.setWhId(Long.parseLong(whId));
                                    insertPRPo.setWhCode(whCode);
                                    insertPRPo.setWhName(whName);
                                    insertPRPo.setLocId(locId);
                                    insertPRPo.setLocCode(locCode);
                                    insertPRPo.setLocName(locName);
                                    insertPRPo.setOptDate(date);
                                    insertPRPo.setCreateDate(date);
                                    insertPRPo.setPersonId(userId);
                                    insertPRPo.setPersonName(name);
                                    insertPRPo.setPartState(partRecState);//配件状态

                                    dao.insert(insertPRPo);

                                    //调用出入库逻辑
                                    ins = new LinkedList<Object>();
                                    ins.add(0, resultId);
                                    ins.add(1, configId);

                                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
                                }
                                //封存 盘亏 特殊处理
                                else {
                                    //1.正常 出库
                                    //配件出入库记录表
                                    insertPRPo = new TtPartRecordPO();
                                    inOrOutFlag = 2;
                                    partRecState = 1;

                                    Long recId = Long.parseLong(SequenceManager.getSequence(""));
                                    insertPRPo.setRecordId(recId);
                                    insertPRPo.setAddFlag(inOrOutFlag);//出入库
                                    insertPRPo.setPartId(partId);
                                    insertPRPo.setPartCode(partCode);
                                    insertPRPo.setPartOldcode(partOldcode);
                                    insertPRPo.setPartName(partCname);
                                    insertPRPo.setPartBatch(partBatch);
                                    insertPRPo.setVenderId(Long.parseLong(partVenId));
                                    insertPRPo.setPartNum(diffQty);//出库数量
                                    insertPRPo.setConfigId(Long.parseLong(configId));
                                    insertPRPo.setOrderId(Long.parseLong(resultId));//盘点结果主表ID
                                    insertPRPo.setLineId(Long.parseLong(detailId));//盘点结果详情ID
                                    insertPRPo.setOrderCode(resultCode); //盘点结果主表盘点单号
                                    insertPRPo.setOrgId(Long.parseLong(parentOrgId));
                                    insertPRPo.setOrgCode(parentOrgCode);
                                    insertPRPo.setOrgName(chgorgCname);
                                    insertPRPo.setWhId(Long.parseLong(whId));
                                    insertPRPo.setWhCode(whCode);
                                    insertPRPo.setWhName(whName);
                                    insertPRPo.setLocId(locId);
                                    insertPRPo.setLocCode(locCode);
                                    insertPRPo.setLocName(locName);
                                    insertPRPo.setOptDate(date);
                                    insertPRPo.setCreateDate(date);
                                    insertPRPo.setPersonId(userId);
                                    insertPRPo.setPersonName(name);
                                    insertPRPo.setPartState(partRecState);//配件状态

                                    dao.insert(insertPRPo);

                                    //调用出入库逻辑
                                    ins = new LinkedList<Object>();
                                    ins.add(0, resultId);
                                    ins.add(1, configId);

                                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);

                                    //2.盘亏入库   -> 入库数量为 正数
                                    //配件出入库记录表
                                    insertPRPo = new TtPartRecordPO();
                                    inOrOutFlag = 1;
                                    partRecState = 4; //盘亏封存标识

                                    Long recId1 = Long.parseLong(SequenceManager.getSequence(""));
                                    insertPRPo.setRecordId(recId1);
                                    insertPRPo.setAddFlag(inOrOutFlag);//出入库
                                    insertPRPo.setPartId(partId);
                                    insertPRPo.setPartCode(partCode);
                                    insertPRPo.setPartOldcode(partOldcode);
                                    insertPRPo.setPartName(partCname);
                                    insertPRPo.setPartBatch(partBatch);
                                    insertPRPo.setVenderId(Long.parseLong(partVenId));
                                    insertPRPo.setPartNum(diffQty);//入库数量(正数)
                                    insertPRPo.setConfigId(Long.parseLong(configId));
                                    insertPRPo.setOrderId(Long.parseLong(resultId));//盘点结果主表ID
                                    insertPRPo.setOrderCode(resultCode); //盘点结果主表盘点单号
                                    insertPRPo.setLineId(Long.parseLong(detailId));//盘点结果详情ID
                                    insertPRPo.setOrgId(Long.parseLong(parentOrgId));
                                    insertPRPo.setOrgCode(parentOrgCode);
                                    insertPRPo.setOrgName(chgorgCname);
                                    insertPRPo.setWhId(Long.parseLong(whId));
                                    insertPRPo.setWhCode(whCode);
                                    insertPRPo.setWhName(whName);
                                    insertPRPo.setLocId(locId);
                                    insertPRPo.setLocCode(locCode);
                                    insertPRPo.setLocName(locName);
                                    insertPRPo.setOptDate(date);
                                    insertPRPo.setCreateDate(date);
                                    insertPRPo.setPersonId(userId);
                                    insertPRPo.setPersonName(name);
                                    insertPRPo.setPartState(partRecState);//配件状态

                                    dao.insert(insertPRPo);

                                    //调用出入库逻辑
                                    ins = new LinkedList<Object>();
                                    ins.add(0, resultId);
                                    ins.add(1, configId);

                                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
                                }
                            }
                        }
                    }

                    //盘点结果主表
                    selCRMPo = new TtPartCheckResultMainPO();
                    updCRMPo = new TtPartCheckResultMainPO();

                    selCRMPo.setResultId(Long.parseLong(resultId));

                    updCRMPo.setUpdateBy(userId);
                    updCRMPo.setUpdateDate(date);
                    updCRMPo.setHandleBy(userId);
                    updCRMPo.setHandleDate(date);
                    updCRMPo.setState(orderState6);

                    dao.update(selCRMPo, updCRMPo);
                } else if ("single".equalsIgnoreCase(handleType)) {
                    success = "single";
                    String detailId = CommonUtils.checkNull(req.getParamValue("detailId")); //盘点单详情ID
                    sbStr = " AND TD.DTL_ID = '" + detailId + "' ";
                    resDtlList = dao.getPartCheckResDtlList(sbStr);

                    if (null != resDtlList && resDtlList.size() == 1) {
                        long partId = Long.parseLong(resDtlList.get(0).get("PART_ID").toString());//配件ID
                        String partCode = resDtlList.get(0).get("PART_CODE").toString();//件号
                        String partOldcode = resDtlList.get(0).get("PART_OLDCODE").toString();//配件编码
                        String partCname = resDtlList.get(0).get("PART_CNAME").toString();//配件名称
                        String unit = resDtlList.get(0).get("UNIT").toString();//配件单位
                        
                        partVenId = resDtlList.get(0).get("VENDER_ID").toString(); // 供应商id
                        partBatch = resDtlList.get(0).get("BATCH_CODE").toString(); // 批次号
                        
                        long checkQty = Long.parseLong(resDtlList.get(0).get("CHECK_QTY").toString());//盘点库存
                        long diffQty = Math.abs(Long.parseLong(resDtlList.get(0).get("DIFF_QTY").toString()));//盈亏库存（出入库数量）
                        int checkResult = Integer.parseInt(resDtlList.get(0).get("CHECK_RESULT").toString());//盘点结果

                        StringBuffer sb = new StringBuffer();
                        sb.append(" AND LD.PART_ID = '" + partId + "' ");
                        sb.append(" AND WD.WH_ID = '" + whId + "' ");
                        sb.append(" AND WD.ORG_ID = '" + parentOrgId + "' ");
                        warLocList = dao.getWareLocaInfos(sb.toString());
                        String whCode = warLocList.get(0).get("WH_CODE").toString();
                        Long locId = Long.parseLong(warLocList.get(0).get("LOC_ID").toString());
                        String locCode = warLocList.get(0).get("LOC_CODE").toString();
                        String locName = warLocList.get(0).get("LOC_NAME").toString();


                        //盘点结果详情表
                        selCRDPo = new TtPartCheckResultDtlPO();

                        updCRDPo = new TtPartCheckResultDtlPO();

                        selCRDPo.setDtlId(Long.parseLong(detailId));

                        updCRDPo.setIsOver(1); //已处理
                        updCRDPo.setUpdateBy(userId);
                        updCRDPo.setUpdateDate(date);

                        dao.update(selCRDPo, updCRDPo);

                        int inOrOutFlag = 1;
                        if (partRecState != 3) {
                            if (inveProfile != checkResult) {
                                inOrOutFlag = 2;
                            }
                            //配件出入库记录表
                            insertPRPo = new TtPartRecordPO();

                            Long recId = Long.parseLong(SequenceManager.getSequence(""));
                            insertPRPo.setRecordId(recId);
                            insertPRPo.setAddFlag(inOrOutFlag);//出入库
                            insertPRPo.setPartId(partId);
                            insertPRPo.setPartCode(partCode);
                            insertPRPo.setPartOldcode(partOldcode);
                            insertPRPo.setPartName(partCname);
                            insertPRPo.setPartBatch(partBatch);
                            insertPRPo.setVenderId(Long.parseLong(partVenId));
                            insertPRPo.setPartNum(diffQty);//出入库数量
                            insertPRPo.setConfigId(Long.parseLong(configId));
                            insertPRPo.setOrderId(Long.parseLong(resultId));//盘点结果主表ID
                            insertPRPo.setOrderCode(resultCode); //盘点结果主表盘点单号
                            insertPRPo.setLineId(Long.parseLong(detailId));//盘点结果详情ID
                            insertPRPo.setOrgId(Long.parseLong(parentOrgId));
                            insertPRPo.setOrgCode(parentOrgCode);
                            insertPRPo.setOrgName(chgorgCname);
                            insertPRPo.setWhId(Long.parseLong(whId));
                            insertPRPo.setWhCode(whCode);
                            insertPRPo.setWhName(whName);
                            insertPRPo.setLocId(locId);
                            insertPRPo.setLocCode(locCode);
                            insertPRPo.setLocName(locName);
                            insertPRPo.setOptDate(date);
                            insertPRPo.setCreateDate(date);
                            insertPRPo.setPersonId(userId);
                            insertPRPo.setPersonName(name);
                            insertPRPo.setPartState(partRecState);//配件状态

                            dao.insert(insertPRPo);

                            //调用出入库逻辑
                            ins = new LinkedList<Object>();
                            ins.add(0, resultId);
                            ins.add(1, configId);

                            dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
                        } else {
                            if (inveProfile == checkResult) {
                                //配件出入库记录表
                                insertPRPo = new TtPartRecordPO();

                                Long recId = Long.parseLong(SequenceManager.getSequence(""));
                                insertPRPo.setRecordId(recId);
                                insertPRPo.setAddFlag(inOrOutFlag);//出入库
                                insertPRPo.setPartId(partId);
                                insertPRPo.setPartCode(partCode);
                                insertPRPo.setPartOldcode(partOldcode);
                                insertPRPo.setPartName(partCname);
                                insertPRPo.setPartBatch(partBatch);
                                insertPRPo.setVenderId(Long.parseLong(partVenId));
                                insertPRPo.setPartNum(diffQty);//入库数量
                                insertPRPo.setConfigId(Long.parseLong(configId));
                                insertPRPo.setOrderId(Long.parseLong(resultId));//盘点结果主表ID
                                insertPRPo.setOrderCode(resultCode); //盘点结果主表盘点单号
                                insertPRPo.setLineId(Long.parseLong(detailId));//盘点结果详情ID
                                insertPRPo.setOrgId(Long.parseLong(parentOrgId));
                                insertPRPo.setOrgCode(parentOrgCode);
                                insertPRPo.setOrgName(chgorgCname);
                                insertPRPo.setWhId(Long.parseLong(whId));
                                insertPRPo.setWhCode(whCode);
                                insertPRPo.setWhName(whName);
                                insertPRPo.setLocId(locId);
                                insertPRPo.setLocCode(locCode);
                                insertPRPo.setLocName(locName);
                                insertPRPo.setOptDate(date);
                                insertPRPo.setCreateDate(date);
                                insertPRPo.setPersonId(userId);
                                insertPRPo.setPersonName(name);
                                insertPRPo.setPartState(partRecState);//配件状态

                                dao.insert(insertPRPo);

                                //调用出入库逻辑
                                ins = new LinkedList<Object>();
                                ins.add(0, resultId);
                                ins.add(1, configId);

                                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
                            }
                            //封存 盘亏 特殊处理
                            else {

                                //1.正常 出库
                                //配件出入库记录表
                                insertPRPo = new TtPartRecordPO();
                                inOrOutFlag = 2;
                                partRecState = 1;

                                Long recId = Long.parseLong(SequenceManager.getSequence(""));
                                insertPRPo.setRecordId(recId);
                                insertPRPo.setAddFlag(inOrOutFlag);//出入库
                                insertPRPo.setPartId(partId);
                                insertPRPo.setPartCode(partCode);
                                insertPRPo.setPartOldcode(partOldcode);
                                insertPRPo.setPartName(partCname);
                                insertPRPo.setPartBatch(partBatch);
                                insertPRPo.setVenderId(Long.parseLong(partVenId));
                                insertPRPo.setPartNum(diffQty);//出库数量
                                insertPRPo.setConfigId(Long.parseLong(configId));
                                insertPRPo.setOrderId(Long.parseLong(resultId));//盘点结果主表ID
                                insertPRPo.setOrderCode(resultCode); //盘点结果主表盘点单号
                                insertPRPo.setLineId(Long.parseLong(detailId));//盘点结果详情ID
                                insertPRPo.setOrgId(Long.parseLong(parentOrgId));
                                insertPRPo.setOrgCode(parentOrgCode);
                                insertPRPo.setOrgName(chgorgCname);
                                insertPRPo.setWhId(Long.parseLong(whId));
                                insertPRPo.setWhCode(whCode);
                                insertPRPo.setWhName(whName);
                                insertPRPo.setLocId(locId);
                                insertPRPo.setLocCode(locCode);
                                insertPRPo.setLocName(locName);
                                insertPRPo.setOptDate(date);
                                insertPRPo.setCreateDate(date);
                                insertPRPo.setPersonId(userId);
                                insertPRPo.setPersonName(name);
                                insertPRPo.setPartState(partRecState);//配件状态

                                dao.insert(insertPRPo);

                                //调用出入库逻辑
                                ins = new LinkedList<Object>();
                                ins.add(0, resultId);
                                ins.add(1, configId);

                                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);

                                //2.盘亏入库   -> 入库数量为 负数
                                //配件出入库记录表
                                insertPRPo = new TtPartRecordPO();
                                inOrOutFlag = 1;
                                partRecState = 4;

                                Long recId1 = Long.parseLong(SequenceManager.getSequence(""));
                                insertPRPo.setRecordId(recId1);
                                insertPRPo.setAddFlag(inOrOutFlag);//出入库
                                insertPRPo.setPartId(partId);
                                insertPRPo.setPartCode(partCode);
                                insertPRPo.setPartOldcode(partOldcode);
                                insertPRPo.setPartName(partCname);
                                insertPRPo.setPartBatch(partBatch);
                                insertPRPo.setVenderId(Long.parseLong(partVenId));
                                insertPRPo.setPartNum(diffQty);//入库数量(正数)
                                insertPRPo.setConfigId(Long.parseLong(configId));
                                insertPRPo.setOrderId(Long.parseLong(resultId));//盘点结果主表ID
                                insertPRPo.setOrderCode(resultCode); //盘点结果主表盘点单号
                                insertPRPo.setLineId(Long.parseLong(detailId));//盘点结果详情ID
                                insertPRPo.setOrgId(Long.parseLong(parentOrgId));
                                insertPRPo.setOrgCode(parentOrgCode);
                                insertPRPo.setOrgName(chgorgCname);
                                insertPRPo.setWhId(Long.parseLong(whId));
                                insertPRPo.setWhCode(whCode);
                                insertPRPo.setWhName(whName);
                                insertPRPo.setLocId(locId);
                                insertPRPo.setLocCode(locCode);
                                insertPRPo.setLocName(locName);
                                insertPRPo.setOptDate(date);
                                insertPRPo.setCreateDate(date);
                                insertPRPo.setPersonId(userId);
                                insertPRPo.setPersonName(name);
                                insertPRPo.setPartState(partRecState);//配件状态

                                dao.insert(insertPRPo);

                                //调用出入库逻辑
                                ins = new LinkedList<Object>();
                                ins.add(0, resultId);
                                ins.add(1, configId);

                                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
                            }
                        }

                        //盘点结果表
                        selCRMPo = new TtPartCheckResultMainPO();
                        updCRMPo = new TtPartCheckResultMainPO();

                        selCRMPo.setResultId(Long.parseLong(resultId));

                        updCRMPo.setUpdateBy(userId);
                        updCRMPo.setUpdateDate(date);
                        updCRMPo.setHandleBy(userId);
                        updCRMPo.setHandleDate(date);

                        dao.update(selCRMPo, updCRMPo);

                        //判断当前盘点结果是否已全部处理
                        sbStr = " AND TD.RESULT_ID = '" + resultId + "' ";
                        resDtlList = dao.getPartCheckResDtlList(sbStr);

                        if (null != resDtlList && resDtlList.size() == 0) {
                            success = "all";
                            //TtPartCheckResultMainPO
                            selCRMPo = new TtPartCheckResultMainPO();
                            updCRMPo = new TtPartCheckResultMainPO();

                            selCRMPo.setResultId(Long.parseLong(resultId));

                            updCRMPo.setUpdateBy(userId);
                            updCRMPo.setUpdateDate(date);
                            updCRMPo.setHandleBy(userId);
                            updCRMPo.setHandleDate(date);
                            updCRMPo.setState(orderState6);//已完成

                            dao.update(selCRMPo, updCRMPo);
                        }
                    } else {
                        errorExist = "该配件已处理,不能重复处理!";
                    }
                }
            } else {
                errorExist = "申请单已处理,不能重复处理!";
            }

            act.setOutData("errorExist", errorExist);//返回错误信息
            act.setOutData("success", success);
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "提交库存盘点申请处理结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-6
     * @Title : 库存盘点调整处理详情查询
     */
    public void partStockDetailSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String resultId = CommonUtils.checkNull(request.getParamValue("resultId")); // 盘点结果单ID

            StringBuffer sbString = new StringBuffer();
            if (null != resultId && !"".equals(resultId)) {
                sbString.append(" AND TM.RESULT_ID = '" + resultId + "' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryPartStockDeatil(sbString.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件库存盘点调整处理信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-5-3
     * @Title :导出配件库存盘点调整处理信息
     */
    public void exportPartStockStatusExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String inveCode = CommonUtils.checkNull(request
                    .getParamValue("inveCode")); // 盘点单号
            String resultCode = CommonUtils.checkNull(request
                    .getParamValue("resultCode")); // 申请单号
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 截止时间
            String whId = CommonUtils.checkNull(request
                    .getParamValue("whId")); // 仓库ID
            String inveType = CommonUtils.checkNull(request
                    .getParamValue("inveType")); // 盘点类型
            String checkName = CommonUtils.checkNull(request
                    .getParamValue("checkName")); // 导入人
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            StringBuffer sbString = new StringBuffer();
            if (null != inveCode && !"".equals(inveCode)) {
                sbString.append(" AND TM.CHANGE_CODE like '%" + inveCode + "%' ");
            }
            if (null != resultCode && !"".equals(resultCode)) {
                sbString.append(" AND TM.RESULT_CODE LIKE '%" + resultCode + "%' ");
            }
            if (null != checkName && !"".equals(checkName)) {
                sbString.append(" AND KU.NAME LIKE '%" + checkName + "%' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(TM.CHECK_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(TM.CHECK_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND TM.WH_ID = '" + whId + "' ");
            }
            if (null != inveType && !"".equals(inveType)) {
                sbString.append(" AND TM.CHECK_TYPE = '" + inveType + "' ");
            }
            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
            }
            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "盘点单号";
            head[2] = "申请单号";
            head[3] = "盘点类型";
            head[4] = "盘点仓库";
            head[5] = "导入人";
            head[6] = "导入日期";
            head[7] = "提交人";
            head[8] = "提交日期";
            head[9] = "提交人";
            head[10] = "提交日期";
            head[11] = "单据状态";
            List<Map<String, Object>> list = dao.queryAllPartStockInve(sbString.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("CHANGE_CODE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("RESULT_CODE"));
                        int type = Integer.parseInt(CommonUtils.checkNull(map.get("CHECK_TYPE")).toString());
                        if (type1 == type) {
                            detail[3] = "全部";
                        } else if (type2 == type) {
                            detail[3] = "动态";
                        }
                        detail[4] = CommonUtils.checkNull(map
                                .get("WH_CNAME"));
                        detail[5] = CommonUtils
                                .checkNull(map.get("IMP_NAME"));
                        detail[6] = CommonUtils.checkNull(map
                                .get("CREATE_DATE"));
                        detail[7] = CommonUtils
                                .checkNull(map.get("COMM_NAME"));
                        detail[8] = CommonUtils.checkNull(map
                                .get("COMMIT_DATE"));
                        detail[9] = CommonUtils
                                .checkNull(map.get("CHE_NAME"));
                        detail[10] = CommonUtils.checkNull(map
                                .get("CHECK_DATE"));
                        detail[11] = "处理中";

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件库存盘点调整处理信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件库存盘点调整处理");
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
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i].replace(",", ""))));
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
}
