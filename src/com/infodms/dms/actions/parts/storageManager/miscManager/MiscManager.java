package com.infodms.dms.actions.parts.storageManager.miscManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.tag.ExcelUtil;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderInDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrInstockDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.dao.parts.storageManager.miscManager.MiscManagerDAO;
import com.infodms.dms.dao.parts.storageManager.miscManager.MiscPrintDAO;
import com.infodms.dms.dao.parts.storageManager.partReturnManager.PartDlrReturnInDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmpPartUploadPO;
import com.infodms.dms.po.TtPartRecordPO;
import com.infodms.dms.po.TtPartsMiscDetailPO;
import com.infodms.dms.po.TtPartsMiscMainPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import net.sf.json.JSONObject;

/**
 * <p>ClassName: MiscManager</p>
 * <p>Description: 配件杂项出入库管理</p>
 * <p>Author: MEpaper</p>
 * <p>Date: 2017年8月21日</p>
 */
public class MiscManager extends BaseImport {

    private static final String INPUT_ERROR_URL = "/jsp/parts/storageManager/miscManager/inputError.jsp";//数据导入出错页面
    private static final int unLockedVal = Constant.PART_STATE_UN_LOCKED;  //配件未锁定
    private final String miscMainInit = "/jsp/parts/storageManager/miscManager/miscMain.jsp";
    private final String miscViewInit = "/jsp/parts/storageManager/miscManager/miscView.jsp";
    private final String miscAddInit_URL = "/jsp/parts/storageManager/miscManager/miscAdd.jsp";
    private final String miscPrintInit = "/jsp/parts/storageManager/miscManager/miscPrint.jsp";

    public Logger logger = Logger.getLogger(MiscManager.class);
    MiscManagerDAO dao = MiscManagerDAO.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 杂项入库初始化
     * @author : andyzhou
     * @LastDate : 2013-7-1
     */
    public void MiscMainInit() {
        try {
            act.setForword(miscMainInit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 杂项入库初始化
     * @author : andyzhou
     * @LastDate : 2013-7-1
     */
    public void getMainList() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orgId = "";//制单单位ID
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                orgId = Constant.OEM_ACTIVITIES;
            } else {
                orgId = logonUser.getDealerId();
            }
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

            PageResult<Map<String, Object>> ps = dao.getMainList(request, orgId, 1, Constant.PAGE_SIZE, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "杂项入库初始化错误!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 杂项入库查看初始化
     * @author : andyzhou
     * @LastDate : 2013-7-1
     */
    public void MiscViewInit() {
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            Map<String, Object> mainMap = dao.getMainData(orderId);
            act.setOutData("orderId", orderId);
            act.setOutData("mainMap", mainMap);
            act.setForword(miscViewInit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 杂项入库查看明细
     * @author : andyzhou
     * @LastDate : 2013-7-1
     */
    public void getDetailList() {
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            PageResult<Map<String, Object>> ps = dao.getDetailList(orderId, Constant.PAGE_SIZE, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 杂项入库新增初始化
     * @author : andyzhou
     * @LastDate : 2013-7-1
     */
    public void MiscAddInit() {
        //前台页面数据集合
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String orderId = SequenceManager.getSequence("");
            logger.info("---orderId=" + orderId);
            String orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_31);
            String orgId = "";
            String orgCode = "";
            String orgName = "";
            PartWareHouseDao dao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = dao.getOrgInfo(logonUser);
            if (null != beanList && beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
                orgCode = beanList.get(0).getOrgCode();
                orgName = beanList.get(0).getOrgName();
            }
            if ("".equals(orgId)) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, " </br>此工号没有操作权限,请联系管理员!");
                throw e1;
            }
            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            //获取仓库
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(orgId);
            List<Map<String, Object>> departmentList = purchasePlanSettingDao.getDepartment("009");
            dataMap.put("orderId", orderId);
            dataMap.put("orderCode", orderCode);
            dataMap.put("userName", logonUser.getName());
            dataMap.put("now", now);
            dataMap.put("orgId", orgId);
            dataMap.put("dealerId", logonUser.getDealerId());
            dataMap.put("orgCode", orgCode);
            dataMap.put("orgName", orgName);
            dataMap.put("OEM", Constant.OEM_ACTIVITIES);
            act.setOutData("dataMap", dataMap);
            act.setOutData("wareHouseList", wareHouseList);
            act.setOutData("departmentList", departmentList);
            act.setForword(miscAddInit_URL);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 查询配件基本信息
     * @author : andyzhou
     * @LastDate : 2013-7-1
     */
    public void showPartBase() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = req.getParamValue("curPage") != null ? Integer.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.showPartBase(req, Constant.PAGE_SIZE, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询配件信息出错,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 杂项入库单保存
     * @author : andyzhou
     * @LastDate : 2013-7-1
     */
    public void MiscSave() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String error = "";
        String success = "";
        try {
            String orgId = CommonUtils.checkNull(req.getParamValue("orgId")); //制单单位ID
            String orgCode = CommonUtils.checkNull(req.getParamValue("orgCode")); //制单单位编码
            String orgName = CommonUtils.checkNull(req.getParamValue("orgName")); //制单单位名称
            String eiType = CommonUtils.checkNull(req.getParamValue("EI_TYPE")); // 入库类型
            String orderCode = CommonUtils.checkNull(req.getParamValue("orderCode"));//获取制单单号
            String configId = Constant.PART_CODE_RELATION_31 + "";//配置ID
            Long userId = logonUser.getUserId(); //制单人ID
            String name = logonUser.getName();
            String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
            String remark = CommonUtils.checkNull(req.getParamValue("textarea1")); //备注
            String department = CommonUtils.checkNull(req.getParamValue("department")); //部门
            String[] partIdArr = req.getParamValues("cb");

            List<Map<String, Object>> partList = null;

            if (null != partIdArr) {
                for (int i = 0; i < partIdArr.length; i++) {
                    String partId = partIdArr[i];
                    String partOldcode = CommonUtils.checkNull(req.getParamValue("partOldcode_" + partId));   //配件编码

                    String sqlStr = " AND VM.PART_ID = '" + partId + "' AND VM.WH_ID = '" + whId + "' ";
                    partList = dao.checkPartState(sqlStr);

                    if (null != partList && partList.size() == 1) {
                        int isLocked = Integer.parseInt(partList.get(0).get("IS_LOCKED").toString());//是否锁定
                        if (unLockedVal != isLocked) {
                            error += "配件：" + partOldcode + "已锁定,目前不能进行库存操作!<br/>";
                        }
                    }
                }
            }

            if ("".equals(error)) {
                Date date = new Date();
                Long changeId = Long.parseLong(SequenceManager.getSequence(""));

                StringBuffer sbStr = new StringBuffer();
                if (null != whId && !"".equals(whId)) {
                    sbStr.append(" AND TM.WH_ID = '" + whId + "' ");
                }
                Map<String, Object> mapWH = dao.getWareHouses(sbStr.toString()).get(0);
                String whName = mapWH.get("WH_NAME").toString();
                String whCode = mapWH.get("WH_CODE").toString();

                TtPartsMiscMainPO po = new TtPartsMiscMainPO();

                po.setMiscOrderId(changeId);
                po.setMiscOrderCode(orderCode);
                po.setCreateBy(logonUser.getUserId());
                po.setCreateDate(date);
                po.setOrgId(Long.parseLong(orgId));
                po.setWhId(Long.parseLong(whId));
                po.setRemark(remark);
                if (!"".equals(eiType) && null != eiType) {
                    po.setExType(Integer.valueOf(eiType));
                }
                if (!"".equals(department) && null != department) {
                    po.setDepartmentCode(department);
                }
                dao.insert(po);

                TtPartsMiscDetailPO insertRDPo = null;
                TtPartRecordPO insertPRPo = null;

                PartDlrReturnInDao inDao = PartDlrReturnInDao.getInstance();
                String batchNo = inDao.getBatchNo(changeId.toString());
                String partVenId = Constant.PART_RECORD_VENDER_ID;//供应商ID
                if (null != partIdArr) {
                    for (int i = 0; i < partIdArr.length; i++) {

                        String partId = partIdArr[i];
                        Long dtlId = Long.parseLong(SequenceManager.getSequence(""));
                        String partCode = CommonUtils.checkNull(req.getParamValue("partCode_" + partId));//件号
                        String partOldcode = CommonUtils.checkNull(req.getParamValue("partOldcode_" + partId));   //配件编码
                        String partCname = CommonUtils.checkNull(req.getParamValue("partCname_" + partId)); //配件名称
                        String unit = CommonUtils.checkNull(req.getParamValue("unit_" + partId)); //单位
                        String loc_id = CommonUtils.checkNull(req.getParamValue("locId_" + partId)); //货位id
                        String locCode = CommonUtils.checkNull(req.getParamValue("locCode_" + partId)); //货位编码
                        String locName = CommonUtils.checkNull(req.getParamValue("locName_" + partId)); //货位名称
                        String minPackage = CommonUtils.checkNull(req.getParamValue("minPackage_" + partId)); // 最小包装量
                        String returnQty = CommonUtils.checkNull(req.getParamValue("inQty_" + partId)); //入库数量
//						String deRemark =  CommonUtils.checkNull(req.getParamValue("remark_"+partId)); //详细备注

                        insertRDPo = new TtPartsMiscDetailPO();
//                        String relocId = "";//实际货位ID
                        insertRDPo.setMiscDetailId(dtlId);
                        insertRDPo.setMiscOrderId(changeId);
                        insertRDPo.setPartId(Long.parseLong(partId));
                        insertRDPo.setUnit(unit);
                        insertRDPo.setMinPackage(Long.parseLong(minPackage));
                        insertRDPo.setInQty(Long.parseLong(returnQty));
//						insertRDPo.setRemark(deRemark);
                        insertRDPo.setCreateBy(userId);
                        insertRDPo.setCreateDate(date);
                        insertRDPo.setLocId(Long.valueOf(loc_id));
                        insertRDPo.setLocCode(locCode);
                        insertRDPo.setLocName(locName);
                        insertRDPo.setBatchNo(batchNo);
                        dao.insert(insertRDPo);

                        //TtPartRecordPO 入库记录
                        insertPRPo = new TtPartRecordPO();
                        Long recId = Long.parseLong(SequenceManager.getSequence(""));
                        insertPRPo.setRecordId(recId);
                        insertPRPo.setAddFlag(1);//入库
                        insertPRPo.setPartId(Long.parseLong(partId));
                        insertPRPo.setPartCode(partCode);
                        insertPRPo.setPartOldcode(partOldcode);
                        insertPRPo.setPartName(partCname);
                        insertPRPo.setPartBatch(batchNo); // 批次号
                        insertPRPo.setVenderId(Long.parseLong(partVenId));
                        insertPRPo.setPartNum(Long.parseLong(returnQty));//出库数量
                        insertPRPo.setConfigId(Long.parseLong(configId));
                        insertPRPo.setOrderId(changeId);//业务ID
                        insertPRPo.setLineId(dtlId);//行ID
                        insertPRPo.setOrgId(Long.parseLong(orgId));
                        insertPRPo.setOrgCode(orgCode);
                        insertPRPo.setOrgName(orgName);
                        insertPRPo.setWhId(Long.parseLong(whId));
                        insertPRPo.setWhCode(whCode);
                        insertPRPo.setWhName(whName);
                        insertPRPo.setLocId(Long.valueOf(loc_id));
                        insertPRPo.setLocCode(locCode);
                        insertPRPo.setLocName(locCode);
                        insertPRPo.setOptDate(date);
                        insertPRPo.setCreateDate(date);
                        insertPRPo.setPersonId(userId);
                        insertPRPo.setPersonName(name);
                        insertPRPo.setPartState(1);//配件状态

                        dao.insert(insertPRPo);

                        //调用出入库逻辑
                        List<Object> ins = new LinkedList<Object>();
                        ins.add(0, changeId);
                        ins.add(1, configId);
                        dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
                    }
                }
                success = "杂项入库单：" + orderCode + "保存成功!";
            }
            act.setOutData("error", error);// 返回错误信息
            act.setOutData("success", success);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "杂项入库单保存出错,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 杂项入库单保存子表
     * @author : andyzhou
     * @LastDate : 2013-7-1
     */
    public void InsertDetail(JSONObject dataObject) {
        try {
            String[] partIdArr = request.getParamValues("cb");
            for (int i = 0; i < partIdArr.length; i++) {
                POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
                PartDlrInstockDao partDlrInstockDao = PartDlrInstockDao.getInstance();
                Long detailId = Long.parseLong(SequenceManager.getSequence(""));
                String partId = partIdArr[i];
                String inQty = CommonUtils.checkNull(request.getParamValue("inQty_" + partId));
                String unit = CommonUtils.checkNull(request.getParamValue("unit_" + partId));
//                PurchaseOrderInDao inDao = PurchaseOrderInDao.getInstance();
//                Map<String, Object> map = inDao.queryPartAndLocationInfo(Long.parseLong(partId), dataObject.getLong("orgId"), dataObject.getLong("whId"));//查询当前配件信息及其货位信息
                TtPartsMiscDetailPO po = new TtPartsMiscDetailPO();
                po.setMiscDetailId(detailId);
                po.setMiscOrderId(dataObject.getLong("orderId"));
                po.setPartId(Long.parseLong(partId));
                po.setUnit(unit);
                po.setInQty(Long.parseLong(inQty));
                po.setCreateBy(logonUser.getUserId());
                po.setCreateDate(new Date());
                logger.info("---子表新增开始");
                dao.insert(po);
                logger.info("---子表新增结束");
                TtPartRecordPO ttPartRecordPO = new TtPartRecordPO();
                ttPartRecordPO.setRecordId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                ttPartRecordPO.setAddFlag(1);//入库标记
                ttPartRecordPO.setState(1);//正常入库		    
                ttPartRecordPO.setPartNum(CommonUtils.parseLong(inQty));//入库数量
                ttPartRecordPO.setPartId(po.getPartId());//配件ID
                ttPartRecordPO.setPartBatch("1306");//////////////////配件批次
                ttPartRecordPO.setVenderId(21799l);///////////////////配件供应商
                ttPartRecordPO.setConfigId(Long.valueOf(Constant.PART_CODE_RELATION_31));//杂项入库单
                ttPartRecordPO.setOrderId(dataObject.getLong("orderId"));//杂项入库单ID
                ttPartRecordPO.setOrderCode(dataObject.getString("orderCode"));//杂项入库单编码
                ttPartRecordPO.setOrgId(dataObject.getLong("orgId"));
                ttPartRecordPO.setOrgCode(dataObject.getString("orgCode"));
                ttPartRecordPO.setOrgName(dataObject.getString("orgName"));
                ttPartRecordPO.setWhId(dataObject.getLong("whId"));
                Map<String, Object> locMap = partDlrInstockDao.getLoc(po.getPartId().toString(), dataObject.getString("whId"), dataObject.getString("orgId"));
                //String locName = CommonUtils.checkNull(locMap.get("LOC_NAME"));
                //String locId = CommonUtils.checkNull(locMap.get("LOC_ID"));
                //String locCode = CommonUtils.checkNull(locMap.get("LOC_CODE"));
                ttPartRecordPO.setLocId(Long.valueOf(CommonUtils.checkNull(locMap.get("LOC_ID"))));
                ttPartRecordPO.setLocCode(CommonUtils.checkNull(locMap.get("LOC_CODE")));
                ttPartRecordPO.setOptDate(new Date());
                ttPartRecordPO.setCreateDate(new Date());
                ttPartRecordPO.setPersonId(logonUser.getUserId());
                ttPartRecordPO.setPersonName(logonUser.getName());
                ttPartRecordPO.setPartState(1);
                logger.info("---记录表新增开始");
                dao.insert(ttPartRecordPO);//新增出入库记录
                logger.info("---记录表新增结束");
                List<Object> ins = new LinkedList<Object>();
                ins.add(0, dataObject.getLong("orderId"));
                ins.add(1, Constant.PART_CODE_RELATION_31);
                logger.info("---存储过程执行开始");
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
                logger.info("---存储过程执行结束s");
                POContext.endTxn(true);//提交事务
                POContext.cleanTxn();
            }
        } catch (Exception e) {//异常方法
            POContext.endTxn(false);//回滚事务
            POContext.cleanTxn();
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "杂项入库单行表新增错误,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 杂项入库单保存主表
     * @author : andyzhou
     * @LastDate : 2013-7-1
     */
    public void InsertMain(JSONObject dataObject) {
        try {
            TtPartsMiscMainPO po = new TtPartsMiscMainPO();
            po.setMiscOrderId(dataObject.getLong("orderId"));
            po.setMiscOrderCode(dataObject.getString("orderCode"));
            po.setCreateBy(logonUser.getUserId());
            po.setCreateDate(new Date());
            po.setOrgId(dataObject.getLong("orgId"));
            po.setWhId(dataObject.getLong("whId"));
            po.setRemark(dataObject.getString("textarea1"));
            dao.insert(po);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "杂项入库单主表新增错误,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate: 2013-7-6
     * @Title : 导出EXCEL模板
     * @Description: 
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
            listHead.add("配件编码");
            listHead.add("入库数量");
            list.add(listHead);
            // 导出的文件名
            String fileName = "杂项入库单模板.xls";
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

    public void uploadExcel() {

        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        try {
            String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";
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
            List<Map<String, String>> errorInfo = new ArrayList<Map<String, String>>();
            List<Map<String, String>> maxLineErro = new ArrayList<Map<String, String>>();
            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmp(request, "uploadFile", 2, 3, maxSize);

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
                act.setForword(INPUT_ERROR_URL);
            } else {
                List<Map> list = getMapList();
                //将导入的数据先放到临时表中
                insertTmpPart(list, errorInfo, maxLineErro);

                if (maxLineErro.size() > 0) {
                    err = maxLineErro.get(0).get("1").toString();
                    act.setOutData("error", err);
                    act.setForword(INPUT_ERROR_URL);
                } else if (errorInfo.size() > 0) {
                    act.setOutData("errorInfo", errorInfo);
                    act.setForword(INPUT_ERROR_URL);
                } else {

					/*List<Map<String,String>> voList = new ArrayList<Map<String,String>>();
                    loadVoList(voList,list, errorInfo, maxLineErro, parentOrgId, whId);*/

                    //验证临时表中的数据,并获取需要的数据
                    List<Map<String, Object>> voList = new ArrayList<Map<String, Object>>();
                    voList = dao.getPartInfo();

                    //前台页面数据集合
                    Map<String, Object> dataMap = new HashMap<String, Object>();

                    StringBuffer sbString = new StringBuffer();
                    sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
                    List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());
                    //获取当前时间
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String now = sdf.format(date);

                    String marker = logonUser.getName();
                    String changeCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_31);//获取制单单号

                    act.setOutData("selectedWhId", whId);

                    dataMap.put("orderCode", changeCode);
                    dataMap.put("userName", marker);
                    dataMap.put("now", now);
                    dataMap.put("orgId", parentOrgId);
                    dataMap.put("orgCode", parentOrgCode);
                    dataMap.put("orgName", companyName);
                    act.setOutData("dataMap", dataMap);
                    act.setOutData("wareHouseList", WHList);
                    act.setOutData("list", voList);
                    act.setForword(miscAddInit_URL);
                }

            }
        } catch (Exception e) {// 异常方法
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    private void insertTmpPart(List<Map> list,
                               List<Map<String, String>> errorInfo, List<Map<String, String>> maxLineErro) {
        if (null == list) {
            list = new ArrayList();
        }
        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            if (null == map) {
                map = new HashMap<String, Cell[]>();
            }
            Set<String> keys = map.keySet();
            Iterator it = keys.iterator();
            String key = "";
            while (it.hasNext()) {
                key = (String) it.next();
                Cell[] cells = (Cell[]) map.get(key);
                int maxLineNum = Constant.PART_STOCK_STATUS_CHANGE_MAX_LINENUM;
                if (Integer.parseInt(key) > maxLineNum) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "导入数据行数不能超过 " + maxLineNum + "行!");
                    maxLineErro.add(errormap);
                } else if ("".equals(cells[0].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "配件编码");
                    errormap.put("3", "空");
                    errorInfo.add(errormap);
                }

                if (cells.length < 2 || CommonUtils.isEmpty(cells[1].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行" + cells[1].getContents());
                    errormap.put("2", "入库数量");
                    errormap.put("3", "空");
                    errorInfo.add(errormap);
                } else {
                    String accTemp = cells[1].getContents().trim();
                    if (null == accTemp || "".equals(accTemp)) {
                        accTemp = "0";
                    } else {
                        accTemp = accTemp.replace(",", "");
                    }

                    String regex = "((^[0]$)|(^[-]?[1-9]+(\\d)*$))";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(accTemp);

                    if (!matcher.find()) {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "入库数量");
                        errormap.put("3", "非法数值!");
                        errorInfo.add(errormap);
                    } else {
                        TmpPartUploadPO po = new TmpPartUploadPO();
                        po.setPartOldcode(cells[0].getContents().trim().toUpperCase());
                        po.setQty(CommonUtils.parseLong(cells[1].getContents()));
                        dao.insert(po);
                    }
                }
            }
        }

    }

    /**
     * @param : @param voList
     * @param : @param list
     * @param : @param errorInfo
     * @return :
     * LastDate    : 2013-4-12
     * @Title : 读取CELL
     */
    private void loadVoList(List<Map<String, String>> voList, List<Map> list, List<Map<String, String>> errorInfo, List<Map<String, String>> maxLineErro, String parentOrgId, String whId) {
        if (null == list) {
            list = new ArrayList();
        }
        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            if (null == map) {
                map = new HashMap<String, Cell[]>();
            }
            Set<String> keys = map.keySet();
            Iterator it = keys.iterator();
            double salePrice = 0.00;
            String key = "";
            while (it.hasNext()) {
                key = (String) it.next();
                Cell[] cells = (Cell[]) map.get(key);
                Map<String, String> tempmap = new HashMap<String, String>();
                int maxLineNum = Constant.PART_STOCK_STATUS_CHANGE_MAX_LINENUM;
                if (Integer.parseInt(key) > maxLineNum) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "导入数据行数不能超过 " + maxLineNum + "行!");
                    maxLineErro.add(errormap);
                } else if ("".equals(cells[0].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "配件编码");
                    errormap.put("3", "空");
                    errorInfo.add(errormap);
                } else {
                    String partOldcode = cells[0].getContents().trim().toUpperCase();
                    List<Map<String, Object>> partCheck = dao.checkOldCode(partOldcode);
                    if (partCheck.size() == 1) {
                        tempmap.put("partOldcode", cells[0].getContents().trim().toUpperCase());
                        tempmap.put("partCode", partCheck.get(0).get("PART_CODE").toString());
                        tempmap.put("partCname", partCheck.get(0).get("PART_CNAME").toString());
                        tempmap.put("unit", partCheck.get(0).get("UNIT").toString());
                        tempmap.put("partId", partCheck.get(0).get("PART_ID").toString());

                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "配件编码");
                        errormap.put("3", "不存在");
                        errorInfo.add(errormap);
                    }
                }

                if (cells.length < 2 || CommonUtils.isEmpty(cells[1].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "入库数量");
                    errormap.put("3", "空");
                    errorInfo.add(errormap);
                } else {
                    String accTemp = cells[1].getContents().trim();
                    if (null == accTemp || "".equals(accTemp)) {
                        accTemp = "0";
                    } else {
                        accTemp = accTemp.replace(",", "");
                    }

                    String regex = "((^[0]$)|(^[-]?[1-9]+(\\d)*$))";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(accTemp);

                    if (matcher.find()) {
                        tempmap.put("inQty", accTemp);
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "入库数量");
                        errormap.put("3", "非法数值!");
                        errorInfo.add(errormap);
                    }
                }
//				tempmap.put("remark", cells.length < 3 || null == cells[2].getContents() ? "" : cells[2].getContents().trim());
                voList.add(tempmap);
            }
        }
    }

    public List<Map<String, Cell[]>> readExcel(InputStream is, int rowSize) throws Exception {
        List<Map<String, Cell[]>> list = new ArrayList();
        Sheet sheet = null;
        try {
            Workbook wb = Workbook.getWorkbook(is);
            int len = is.read();
            // 如果多页则遍历
            Sheet[] sheets = wb.getSheets();
            for (int i = 0; i < sheets.length; i++) {  //页
                sheet = sheets[i];
                for (int j = 1; j < sheet.getRows(); j++) {  //行
                    Map<String, Cell[]> map = new HashMap<String, Cell[]>();
                    Cell[] cells = sheet.getRow(j);
                    map.put(j + "", cells);
                    list.add(map);
                }
            }
            return list;
        } catch (Exception ex) {
            throw ex;
        }
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
            list = new ArrayList();
        }
        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            if (null == map) {
                map = new HashMap<String, Cell[]>();
            }
            Set<String> keys = map.keySet();
            Iterator it = keys.iterator();
            String key = "";
            while (it.hasNext()) {
                key = (String) it.next();
                Cell[] cells = (Cell[]) map.get(key);
                try {
                    parseCells(dataList, key, cells);
                } catch (Exception e) {
                    if (e instanceof BizException) {
                        BizException e1 = (BizException) e;
                        error += "上传文件的第" + ((i + 1) + "") + "行," + e1.getMessage().replaceAll("操作失败！失败原因：", "") + "</br>";
                        continue;
                    } else {
                        throw e;
                    }
                }
            }
        }
        rtnMap.put("dataList", dataList);
        rtnMap.put("error", error);
        return rtnMap;
    }

    /**
     * @param : @param list
     * @param : @param rowNum
     * @param : @param cells
     * @param : @param errorInfo
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 装载VO
     * @Description: TODO
     */
    private void parseCells(List<Map<String, Object>> list, String rowNum, Cell[] cells) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        RequestWrapper request = act.getRequest();
        String error = "";
        if ("" == CommonUtils.checkNull(cells[0].getContents())) {
            BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件编码为空!");
            throw e;
        }

        request.setAttribute("partOldcode", CommonUtils.checkNull(cells[0].getContents()));
        request.setAttribute("uploadFlag", "upload");


        PageResult<Map<String, Object>> ps = null;
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        request.setAttribute("bookDealerId", dealerId);
        ps = dao.showPartBase(request, logonUser.getOrgId() + "", 1, Constant.PAGE_SIZE);
        request.removeAttribute("uploadFlag");
        if (null == ps) {
            BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件" + cells[0].getContents() + "不存在!");
            throw e;
        }
        if (null == ps.getRecords() || ps.getRecords().size() <= 0) {
            BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件" + cells[0].getContents() + "不存在!");
            throw e;
        }
        List<Map<String, Object>> partList = ps.getRecords();
        Map<String, Object> partMap = partList.get(0);
        if (null == partMap) {
            BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件" + cells[0].getContents() + "不存在!");
            throw e;
        }
        if ("" != CommonUtils.checkNull(cells[1].getContents())) {
            try {
                Double.valueOf(cells[1].getContents());
                partMap.put("inQty", Double.valueOf(cells[1].getContents()));
                list.add(partMap);
            } catch (Exception e) {
                //即使错误了，也只是不显示数量
                list.add(partMap);
                BizException ex = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "销售数量格式错误!");
                throw ex;
            }
        }

    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-12-23
     * @Title : 打印标准杂收单
     */
    public void miscPrint() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        MiscPrintDAO miscPrintDao = MiscPrintDAO.getInstance();
        try {
            //制单单位ID
            String orgId = logonUser.getDealerId();
            int indexNO = 1;
            Map<String, Object> dataMap = new HashMap<String, Object>();
            //5.获取装箱单表头信息
            List<Map<String, Object>> soMainList = miscPrintDao.getMainPrintHead(request, orgId);//上部出库单信息
            if (soMainList != null) {
                String misc_order_id = "";
                String misc_order_code = "";
                String b_type = "";
                String org_name = "";
                String wh_name = "";
                String remark = "";
                String create_date = "";
                String department_name = "";

                for (int i = 0; i < soMainList.size(); i++) {
                    Map<String, Object> mainMap = soMainList.get(i);
                    misc_order_id = CommonUtils.checkNull(mainMap.get("MISC_ORFER_ID"));
                    misc_order_code = CommonUtils.checkNull(mainMap.get("MISC_ORDER_CODE"));
                    b_type = CommonUtils.checkNull(mainMap.get("B_TYPE"));
                    org_name = CommonUtils.checkNull(mainMap.get("ORG_NAME"));
                    wh_name = CommonUtils.checkNull(mainMap.get("WH_NAME"));
                    remark = CommonUtils.checkNull(mainMap.get("REMARK"));
                    create_date = CommonUtils.checkNull(mainMap.get("CREATE_DATE"));
                    department_name = CommonUtils.checkNull(mainMap.get("DEPARTMENT_NAME"));
                }
                dataMap.put("misc_order_id", misc_order_id);
                dataMap.put("misc_order_code", misc_order_code);
                dataMap.put("b_type", b_type);
                dataMap.put("org_name", org_name);
                dataMap.put("wh_name", wh_name);
                dataMap.put("remark", remark);
                dataMap.put("create_date", create_date);
                dataMap.put("department_name", department_name);

                //获取出库单明细条目
                List<Map<String, Object>> miscDetailList = miscPrintDao.getMainPrintList(request, orgId);

                //获取本单合计数量
                List<Map<String, Object>> sumList = miscPrintDao.getMainPrintSum(request, orgId);


                List<Map<String, Object>> miscList = new ArrayList<Map<String, Object>>();
                for (int k = 0; k < miscDetailList.size(); k++) {//indexNO
                    Map<String, Object> row = miscDetailList.get(k);
                    row.put("indexNO", indexNO);
                    miscList.add(row);
                    indexNO++;
                }

                act.setOutData("miscList", miscList);
                act.setOutData("listSize", (10 - miscList.size()));
                act.setOutData("sumInQty", sumList.get(0));
            }
            indexNO = 1;
            act.setOutData("dataMap", dataMap);
            act.setForword(miscPrintInit);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "标准杂收发单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 明细导出
     */
    public void exportDtl() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String orgId = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            String[] head = new String[12];
            head[0] = "杂项单号 ";
            head[1] = "类型";
            head[2] = "配件编码";
            head[3] = "配件名称";
            head[4] = "单位";
            head[5] = "数量";
            head[6] = "操作人";
            head[7] = "日期";
            head[8] = "备注";
            List<Map<String, Object>> list = dao.getMiscDtl(request, orgId,"1", Constant.PAGE_SIZE_MAX, 1).getRecords();
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[12];
                    detail[0] = CommonUtils.checkNull(map.get("MISC_ORDER_CODE"));
                    detail[1] = CommonUtils.checkNull(map.get("B_TYPE"));
                    detail[2] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                    detail[3] = CommonUtils.checkNull(map.get("PART_CNAME"));
                    detail[4] = CommonUtils.checkNull(map.get("UNIT"));
                    detail[5] = CommonUtils.checkNull(map.get("IN_QTY"));
                    detail[6] = CommonUtils.checkNull(map.get("NAME"));
                    detail[7] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[8] = CommonUtils.checkNull(map.get("REMARK"));
                    list1.add(detail);
                }
            }
            ExcelUtil.toExceUtil(ActionContext.getContext().getResponse(), request, head, list1, "配件杂项出入库.xls");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

}
