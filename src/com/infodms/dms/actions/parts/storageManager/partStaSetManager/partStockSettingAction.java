package com.infodms.dms.actions.parts.storageManager.partStaSetManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.storageManager.partStaSetManager.partStockSettingDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartChgStateDtlPO;
import com.infodms.dms.po.TtPartChgStateMainPO;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartRecordPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理配件库存状态变更业务
 * @Date: 2013-4-22
 * @remark
 */
public class partStockSettingAction extends BaseImport {
    private static final partStockSettingDao dao = partStockSettingDao.getInstance();
    private static final int unLockedVal = Constant.PART_STATE_UN_LOCKED;  //配件未锁定
    //配件库存状态变更
    private static final String PART_STOCK_SETTING = "/jsp/parts/storageManager/partStaSetManager/partStockSetting/partStatusSetting.jsp";//配件库存状态变更首页
    private static final String PART_STOCK_VIEW = "/jsp/parts/storageManager/partStaSetManager/partStockSetting/partStatusSettingView.jsp";//库存状态变更查看页面
    private static final String PART_STOCK_HANDLE = "/jsp/parts/storageManager/partStaSetManager/partStockSetting/partStatusSettingHdle.jsp";//库存状态变更处理页面
    private static final String PART_STOCK_ADD = "/jsp/parts/storageManager/partStaSetManager/partStockSetting/partStatusAdd.jsp";///库存状态变更新增页面
    private static final String INPUT_ERROR_URL = "/jsp/parts/storageManager/partStaSetManager/partStockSetting/inputError.jsp";//数据导入出错页面
    public Logger logger = Logger.getLogger(partStockSettingAction.class);

    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();

    //tt_part_config_code 获取单据编码使用表

    /**
     * @param : @param response
     * @param : @param request
     * @param : @param head
     * @param : @param list
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-22
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
//            int pageSize = list.size() / 30000;
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
     * @throws : LastDate : 2013-4-22
     * @Title : 跳转至配件库存状态变更页面
     */
    public void partStockSettingInit() {
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
            act.setOutData("maker", logonUser.getName());
            act.setForword(PART_STOCK_SETTING);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件库存状态变更页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-22
     * @Title : 查询配件库存状态变更信息
     */
    public void partStockSettingSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String changeCode = CommonUtils.checkNull(request
                    .getParamValue("changeCode")); // 变更单号
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 截止时间
            String whId = CommonUtils.checkNull(request
                    .getParamValue("whId")); // 截止时间
            String remark = CommonUtils.checkNull(request
                    .getParamValue("remark")); // 备注
            String isFinish = CommonUtils.checkNull(request
                    .getParamValue("isFinish")); // 完成状态
            String partOldcode = CommonUtils.checkNull(request
                    .getParamValue("partOldcode")); // 配件编码
            String maker = CommonUtils.checkNull(request
                    .getParamValue("maker")); // 制单人

            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            StringBuffer sbString = new StringBuffer();
            StringBuffer sbStr = new StringBuffer();
            if (null != changeCode && !"".equals(changeCode)) {
                sbString.append(" AND TM.CHANGE_CODE LIKE '%" + changeCode + "%' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND TM.WH_ID = '" + whId + "' ");
            }
            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
            }

            if (null != remark && !"".equals(remark)) {
                sbString.append(" AND TM.REMARK LIKE '%" + remark.trim() + "%' ");
            }
            if (null != isFinish && !"".equals(isFinish)) {
                sbString.append(" AND TM.STATE = '" + isFinish + "' ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbStr.append(" AND UPPER(TD.PART_OLDCODE) LIKE '%" + partOldcode.trim().toUpperCase() + "%'");
            }
            if (null != maker && !"".equals(maker)) {
                sbString.append(" AND U.NAME LIKE '%" + maker.trim() + "%' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartStockStuatus(
                    sbString.toString(), sbStr.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件库存状态变更信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-22
     * @Title : 跳转至库存状态变更查看页面
     */
    public void viewStockDeatilInint() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String changeId = CommonUtils.checkNull(request.getParamValue("changeId"));// 变更单ID
            String option = CommonUtils.checkNull(request.getParamValue("option"));// 操作类型
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.CHANGE_ID = '" + changeId + "' ");
            Map<String, Object> map = dao.queryAllPartStockStatus(sbString.toString(), "").get(0);

            act.setOutData("map", map);
            if ("view".equalsIgnoreCase(option)) {
                act.setForword(PART_STOCK_VIEW);
            } else {
                act.setForword(PART_STOCK_HANDLE);
            }
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "库存状态变更查看页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-22
     * @Title : 跳转至库存状态变更新增页面
     */
    public void partStockAddInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String actionURL = CommonUtils.checkNull(request.getParamValue("actionURL"));// action地址
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

            String marker = logonUser.getName();
            String changeCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_19);//获取变更单号
            List<Map<String, String>> voList = null;

            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("changeCode", changeCode);
            act.setOutData("actionURL", actionURL);
            act.setOutData("marker", marker);
            act.setOutData("companyName", companyName);
            act.setOutData("WHList", WHList);
            act.setOutData("list", voList);
            act.setForword(PART_STOCK_ADD);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "库存状态变更新增页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title : 显示仓库配件库存信息
     */
    public void showPartStockBase() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        StringBuffer sbStr = new StringBuffer();
        try {
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构ID
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));// 仓库ID
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));// 件号
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));// 配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));// 配件名称
            String pdResult = CommonUtils.checkNull(request.getParamValue("pdResult"));// 盘点结果

            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbStr.append(" AND TD.ORG_ID = '" + parentOrgId + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbStr.append(" AND TD.WH_ID = '" + whId + "' ");
            }
            if (null != partCode && !"".equals(partCode)) {
                sbStr.append(" AND TD.PART_CODE LIKE '%" + partCode + "%' ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbStr.append(" AND TD.PART_OLDCODE LIKE '%" + partOldcode + "%' ");
            }
            if (null != partCname && !"".equals(partCname)) {
                sbStr.append(" AND TD.PART_CNAME LIKE '%" + partCname + "%' ");
            }
            if (null != pdResult && !"".equals(pdResult)) {
                if ("1".equals(pdResult)) {
                    sbStr.append(" AND TD.PDFC_QTY >= '1' ");
                } else {
                    sbStr.append(" AND TD.PKFC_QTY >= '1' ");
                }
            }

            sbStr.append(" AND TD.IS_LOCKED = '" + unLockedVal + "' "); //获取未锁定的配件

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.showPartStockBase(
                    sbStr.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "显示仓库配件库存信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * <p>
     * Description: 
     * </p>
     * @param po
     * @param inOrOutFlag 出入库标识1
     * @param partRecState
     * @param inOrOutFlag2
     * @param partRecState2
     */
    @SuppressWarnings("unchecked")
    private void outAndInStock(TtPartRecordPO po, int inOrOutFlag, int partRecState, int inOrOutFlag2, int partRecState2) {
        List<Object> ins = null;
        
        // 出库
        if(inOrOutFlag > 0 && partRecState > 0){
            po = this.getPartRecordPo(po, inOrOutFlag, partRecState);
            dao.insert(po);
            
            //调用出入库逻辑
            ins = new LinkedList<Object>();
            ins.add(0, po.getOrderId());
            ins.add(1, po.getConfigId());
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
        }

        // 入库
        if(inOrOutFlag2 > 0 && partRecState2 > 0){
            po = this.getPartRecordPo(po, inOrOutFlag2, partRecState2);
            dao.insert(po);
            
            //调用出入库逻辑
            ins = new LinkedList<Object>();
            ins.add(0, po.getOrderId());
            ins.add(1, po.getConfigId());
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
        }
    }
    
    /**
     * <p>
     * Description: TODO
     * </p>
     * @param po
     * @param inOrOutFlag
     * @param partRecState
     * @return
     */
    private TtPartRecordPO getPartRecordPo(TtPartRecordPO po, int inOrOutFlag, int partRecState){
        Long recId = Long.parseLong(SequenceManager.getSequence(""));
        po.setRecordId(recId);
        po.setAddFlag(inOrOutFlag);//出入库标识
        po.setPartState(partRecState);//配件状态
        return po;
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title : 保存新增配件库存变更信息
     */
    @SuppressWarnings("unchecked")
    public void saveStockInfos() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String errorExist = "";
        String LockErr = "";
        String nomUnLockErr = "";
        String padUnlockErr = "";
        try {
            String parentOrgId = CommonUtils.checkNull(req.getParamValue("parentOrgId")); //制单单位ID
            String parentOrgCode = CommonUtils.checkNull(req.getParamValue("parentOrgCode")); //制单单位编码
            String chgorgCname = CommonUtils.checkNull(req.getParamValue("chgorgCname")); //制单单位名称
            String changeCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_19);//获取变更单号
            Long userId = logonUser.getUserId(); //制单人ID
            String name = logonUser.getName();
            String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
            String remark = CommonUtils.checkNull(req.getParamValue("remark")); //备注
            String configId = Constant.PART_CODE_RELATION_19 + "";//配置ID
            String[] partIdArr = req.getParamValues("cb");
            String partBatch = "";
            String venderId = "";
            venderId = Constant.PART_RECORD_VENDER_ID;
            int bzType1 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01;//正常
            int bzType2 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02;//盘盈
            int bzType3 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03;//盘亏
            int cgType1 = Constant.PART_STOCK_STATUS_CHANGE_TYPE_01;//封存
            int cgType2 = Constant.PART_STOCK_STATUS_CHANGE_TYPE_02;//解封
            List<Map<String, Object>> partList = null;

            if (null != partIdArr) {
                for (int i = 0; i < partIdArr.length; i++) {
                    String partId = partIdArr[i];
                    String partOldcode = CommonUtils.checkNull(req.getParamValue("partOldcode_" + partId));   //配件编码
                    Integer bzType = Integer.parseInt(CommonUtils.checkNull(req.getParamValue("bzType_" + partId)));   //业务类型
                    Integer cgType = Integer.parseInt(CommonUtils.checkNull(req.getParamValue("cgType_" + partId))); //调整类型
                    Integer returnQty = Integer.parseInt(CommonUtils.checkNull(req.getParamValue("returnQty_" + partId))); //调整数量
//                    venderId = CommonUtils.checkNull(req.getParamValue("stockVenderId" + partId)); //调整数量
                    String loc = CommonUtils.checkNull(req.getParamValue("locName_" + partId)); //货位信息
                    String locId = loc.split(",")[1];
                    partBatch = loc.split(",")[4];
                    partList = dao.getPartStockInfos2(partOldcode, parentOrgId, whId, locId, partBatch);
                    
                    Integer normalQty = Integer.parseInt(partList.get(0).get("NORMAL_QTY").toString());//可用库存
                    Integer zcfcQty = Integer.parseInt(partList.get(0).get("ZCFC_QTY").toString());//正常封存
                    Integer pdfcQty = Integer.parseInt(partList.get(0).get("PDFC_QTY").toString());//盘盈封存
                    Integer pkfcQty = Integer.parseInt(partList.get(0).get("PKFC_QTY").toString());//盘亏封存
                    Integer isLocked = Integer.parseInt(partList.get(0).get("IS_LOCKED").toString());//是否锁定

                    if (unLockedVal != isLocked) {
                        LockErr += "配件：" + partOldcode + "已锁定,目前不能进行库存操作!<br/>";
                    }
                    //正常 （盘亏） + 封存
                    else if (bzType2 != bzType && cgType1 == cgType && normalQty < returnQty) {
                        LockErr += "配件：" + partOldcode + "封存数量不能大于可用库存数: " + normalQty + " !<br/>";
                    }
                    //正常 + 解封
                    else if (bzType1 == bzType && cgType2 == cgType && zcfcQty < returnQty) {
                        nomUnLockErr += "配件：" + partOldcode + "解封数量不能大于正常封存数: " + zcfcQty + " !<br/>";
                    }
                    //盘盈（盘亏） + 解封
                    else if (bzType1 != bzType && cgType2 == cgType) {
                        if (bzType2 == bzType && pdfcQty < returnQty) {
                            padUnlockErr += "配件：" + partOldcode + "解封数量不能大于盘盈封存数: " + pdfcQty + " !<br/>";
                        } else if (bzType3 == bzType && (pkfcQty < returnQty)) {
                            padUnlockErr += "配件：" + partOldcode + "解封数量不能大于盘亏封存数: " + pkfcQty + " !<br/>";
                        }
                    }

                }
            }
            errorExist = LockErr + nomUnLockErr + padUnlockErr;
            if ("".equals(errorExist)) {
                Date date = new Date();
                Long changeId = Long.parseLong(SequenceManager.getSequence(""));
                int countNum = 0;//用于统计封存次数

                StringBuffer sbStr = new StringBuffer();
                if (null != whId && !"".equals(whId)) {
                    sbStr.append(" AND TM.WH_ID = '" + whId + "' ");
                }
                Map<String, Object> mapWH = dao.getWareHouses(sbStr.toString()).get(0);
                String whName = mapWH.get("WH_CNAME").toString();

                TtPartChgStateMainPO inserCSMPo = new TtPartChgStateMainPO();

                inserCSMPo.setChangeId(changeId);
                inserCSMPo.setChangeCode(changeCode);
                inserCSMPo.setChgorgId(Long.parseLong(parentOrgId));
                inserCSMPo.setChgorgCode(parentOrgCode);
                inserCSMPo.setChgorgCname(chgorgCname);
                inserCSMPo.setWhId(Long.parseLong(whId));
                inserCSMPo.setWhCname(whName);
                inserCSMPo.setRemark(remark);
                inserCSMPo.setCreateBy(userId);
                inserCSMPo.setCreateDate(date);
                inserCSMPo.setChgType(bzType1);

                dao.insert(inserCSMPo);

                TtPartChgStateDtlPO insertCSDPo = null;
                TtPartRecordPO insertPRPo = null;

                if (null != partIdArr) {
                    for (int i = 0; i < partIdArr.length; i++) {
                        insertCSDPo = new TtPartChgStateDtlPO();

                        String partId_locId = partIdArr[i];
                        Long dtlId = Long.parseLong(SequenceManager.getSequence(""));
                        String partCode = CommonUtils.checkNull(req.getParamValue("partCode_" + partId_locId));//件号
                        String partOldcode = CommonUtils.checkNull(req.getParamValue("partOldcode_" + partId_locId));   //配件编码
                        String partCname = CommonUtils.checkNull(req.getParamValue("partCname_" + partId_locId)); //配件名称
                        String normalQty = CommonUtils.checkNull(req.getParamValue("normalQty_" + partId_locId)); //当前可用库存
                        String unit = CommonUtils.checkNull(req.getParamValue("unit_" + partId_locId)); //单位
                        Integer bzType = Integer.parseInt(CommonUtils.checkNull(req.getParamValue("bzType_" + partId_locId)));   //业务类型
                        Integer cgType = Integer.parseInt(CommonUtils.checkNull(req.getParamValue("cgType_" + partId_locId))); //调整类型
                        Long returnQty = Long.parseLong(CommonUtils.checkNull(req.getParamValue("returnQty_" + partId_locId))); //调正数量
                        String deRemark = CommonUtils.checkNull(req.getParamValue("remark_" + partId_locId)); //详细备注

                        String loc = CommonUtils.checkNull(req.getParamValue("locName_" + partId_locId)); //货位信息
                        String[] locs = loc.trim().split(",");
                        Long locId = Long.parseLong(locs[1]);
                        String locCode = locs[2];
                        String locName = locs[3];
                        String batchNo = locs[4];
                        String whCode = mapWH.get("WH_CODE").toString();;
                        Long partId = Long.parseLong(partId_locId.substring(0, 16));

                        TtPartDefinePO definePO = new TtPartDefinePO();
                        definePO.setPartId(partId);
                        definePO = (TtPartDefinePO)dao.select(definePO).get(0);

                        insertCSDPo.setDtlId(dtlId);
                        insertCSDPo.setChangeId(changeId);
                        insertCSDPo.setPartId(partId);
                        insertCSDPo.setLineNo(Long.parseLong("0"));
                        insertCSDPo.setPartCode(definePO.getPartCode());
                        insertCSDPo.setPartOldcode(partOldcode);
                        insertCSDPo.setPartCname(partCname);
                        insertCSDPo.setUnit(unit);
                        insertCSDPo.setStockQty(Long.parseLong(normalQty));
                        insertCSDPo.setChangeReason(bzType);
                        insertCSDPo.setChangeType(cgType);
                        insertCSDPo.setReturnQty(returnQty);
                        insertCSDPo.setRemark(deRemark);
                        insertCSDPo.setCreateBy(userId);
                        insertCSDPo.setCreateDate(date);
                        insertCSDPo.setLocId(locId);
                        insertCSDPo.setBatchNo(Long.parseLong(batchNo));
                        insertCSDPo.setVenderId(Long.parseLong(venderId));
                        //如果为解封,状态直接修改为已处理
                        if (cgType2 == cgType) {
                            insertCSDPo.setStatus(0);
                            insertCSDPo.setColseQty(returnQty);
                            insertCSDPo.setUpdateDate(date);
                            insertCSDPo.setUpdateBy(userId);
                            insertCSDPo.setRemark2(deRemark);
                        }

                        dao.insert(insertCSDPo);

                        // 出入库记录
                        insertPRPo = new TtPartRecordPO();
                        Long recId = Long.parseLong(SequenceManager.getSequence(""));
                        insertPRPo.setRecordId(recId);
                        insertPRPo.setPartId(partId);
                        insertPRPo.setPartCode(partCode);
                        insertPRPo.setPartOldcode(partOldcode);
                        insertPRPo.setPartName(partCname);
                        insertPRPo.setPartBatch(batchNo);
                        insertPRPo.setVenderId(Long.parseLong(venderId));
                        insertPRPo.setPartNum(returnQty);//出入库数量
                        insertPRPo.setConfigId(Long.parseLong(configId));
                        insertPRPo.setOrderId(changeId);//变更单ID
                        insertPRPo.setLineId(dtlId);//变更单详情ID
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
                        

                        //正常  + 封存 (出入库数量为 正数)
                        if (bzType1 == bzType && cgType1 == cgType) {
                            countNum++;
                            // 1.配件正常出库
                            // 2.配件正常封存入库
                            // 2:出库标识,1:正常,1:入库标识,2:正常封存
                            outAndInStock(insertPRPo, 2, 1, 1, 2);
                            
                        }
                        //正常 + 解封(出入库数量为 正数)
                        else if (bzType1 == bzType && cgType2 == cgType) {
                            // 1.配件正常封存出库
                            // 2.配件正常入库
                            // 2:出库标识,2:正常封存,1:入库标识,2:正常
                            outAndInStock(insertPRPo, 2, 2, 1, 1);

                        }
                        //盘盈 + 封存(出入库数量为 正数)
                        else if (bzType2 == bzType && cgType1 == cgType) {
                            countNum++;
                            // 1.配件盘点封存入库
                            // 1:入库标识,3:盘点封存
                            outAndInStock(insertPRPo, 1, 3, 0, 0);

                        }
                        //盘盈 + 解封(出入库数量为 正数)
                        else if (bzType2 == bzType && cgType2 == cgType) {
                            // 1.配件盘盈封存出库
                            // 2.配件正常入库
                            // 2:出库标识,3:盘盈封存,1:入库标识,2:正常
                            outAndInStock(insertPRPo, 2, 3, 1, 1);
                        }
                        //盘亏 + 封存(出入库数量为 正数)
                        else if (bzType3 == bzType && cgType1 == cgType) {
                            countNum++;
                            // 1.正常 出库
                            // 2.盘亏入库   -> 入库数量为 正数
                            // 2:出库标识,1:盘盈封存,1:入库标识,2:盘亏封存标识
                            outAndInStock(insertPRPo, 2, 1, 1, 4);
                        }
                        //盘亏 + 解封(出入库数量为 正数)
                        else if (bzType3 == bzType && cgType2 == cgType) {
                            // 1.配件盘盈封存出库
                            // 2:出库标识,1:盘亏封存
                            outAndInStock(insertPRPo, 2, 4, 0, 0);
                            /*inOrOutFlag = 2; //出库标识
                            partRecState = 4; //盘亏封存

                            //1.配件盘盈封存出库
                            insertPRPo = this.getPartRecordPo(insertPRPo, inOrOutFlag, partRecState);
                            dao.insert(insertPRPo);

                            //调用出入库逻辑
                            ins = new LinkedList<Object>();
                            ins.add(0, changeId);
                            ins.add(1, configId);

                            dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);*/

    		                /*inOrOutFlag = 2; //出库标识
    		                partRecState = 1; //正常
    
    						//2.配件正常入库
                            insertPRPo = this.getPartRecordPo(insertPRPo, inOrOutFlag, partRecState);
                            dao.insert(insertPRPo);
    
    						//调用出入库逻辑
    						ins = new LinkedList<Object>();
    		                ins.add(0, changeId);
    		                ins.add(1, configId);
    
    		                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);*/
                        }
                    }

                }
                if (countNum == 0) {
                    TtPartChgStateMainPO selCSMPo = new TtPartChgStateMainPO();
                    TtPartChgStateMainPO updCSMPo = new TtPartChgStateMainPO();

                    selCSMPo.setChangeId(changeId);

                    updCSMPo.setState(0);//已处理(全部为解封)

                    dao.update(selCSMPo, updCSMPo);
                }
            }
            act.setOutData("errorExist", errorExist);// 返回错误信息
            act.setOutData("success", "true");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存新增配件库存变更信息");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("error", "保存失败!");
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title : 导出配件库存状态变更 EXECEL模板
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
            listHead.add("货位");
            listHead.add("调整数量 ");
            listHead.add("备注");
            list.add(listHead);
            // 导出的文件名
            String fileName = "配件库存状态变更模板.xls";
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

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-24
     * @Title : 配件库存状态变更 -> 导入文件
     */
    public void partStockSettingUpload() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        try {
            String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
            String actionURL = CommonUtils.checkNull(request.getParamValue("actionURL"));// action地址
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
            List<Map<String, String>> errorInfo = new ArrayList<Map<String, String>>();
            List<Map<String, String>> maxLineErro = new ArrayList<Map<String, String>>();
            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmp(request, "uploadFile", 4, 3, maxSize);

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
                act.setOutData("actionURL", actionURL);
                act.setForword(INPUT_ERROR_URL);
            } else {
                List<Map> list = getMapList();
                List<Map<String, String>> voList = new ArrayList<Map<String, String>>();
                loadVoList(voList, list, errorInfo, maxLineErro, parentOrgId, whId);
                if (maxLineErro.size() > 0) {
                    err = maxLineErro.get(0).get("1").toString();
                    act.setOutData("error", err);
                    act.setOutData("actionURL", actionURL);
                    act.setForword(INPUT_ERROR_URL);
                } else if (errorInfo.size() > 0) {
                    act.setOutData("errorInfo", errorInfo);
                    act.setOutData("actionURL", actionURL);
                    act.setForword(INPUT_ERROR_URL);
                } else {

                    StringBuffer sbString = new StringBuffer();
                    sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
                    List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());

                    String marker = logonUser.getName();
                    String changeCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_19);//获取变更单号

                    act.setOutData("selectedWhId", whId);
                    act.setOutData("parentOrgId", parentOrgId);
                    act.setOutData("parentOrgCode", parentOrgCode);
                    act.setOutData("changeCode", changeCode);
                    act.setOutData("marker", marker);
                    act.setOutData("companyName", companyName);
                    act.setOutData("WHList", WHList);
                    act.setOutData("list", voList);
                    act.setOutData("actionURL", actionURL);
                    act.setForword(PART_STOCK_ADD);
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
                    errormap.put("3", "为空");
                    errorInfo.add(errormap);
                } else {
                    List<Map<String, Object>> partCheck = dao.checkOldCode(cells[0].getContents().trim());
                    if (partCheck.size() == 1) {
                        String batchNo = cells[1].getContents().trim();
                        List<Map<String, Object>> partList = dao.getPartStockInfos(cells[0].getContents().trim(), parentOrgId, whId,cells[1].getContents().trim(), batchNo);
                        if (partList.size() == 1) {
                            tempmap.put("partOldcode", cells[0].getContents().trim());
                            if (null != partList.get(0).get("PART_CODE") && !"".equals(partList.get(0).get("PART_CODE"))) {
                                tempmap.put("partCode", partList.get(0).get("PART_CODE").toString());
                            } else {
                                tempmap.put("partCode", "");
                            }

                            if (null != partList.get(0).get("PART_CNAME") && !"".equals(partList.get(0).get("PART_CNAME"))) {
                                tempmap.put("partCname", partList.get(0).get("PART_CNAME").toString());
                            } else {
                                tempmap.put("partCname", "");
                            }

                            if (null != partList.get(0).get("UNIT") && !"".equals(partList.get(0).get("UNIT"))) {
                                tempmap.put("unit", partList.get(0).get("UNIT").toString());
                            } else {
                                tempmap.put("unit", "");
                            }

                            tempmap.put("partId", partList.get(0).get("PART_ID").toString()+","+ partList.get(0).get("LOC_ID").toString());
                            tempmap.put("locId", partList.get(0).get("LOC_ID").toString()+","+ partList.get(0).get("LOC_CODE").toString()+","+ partList.get(0).get("LOC_CODE").toString());
                            tempmap.put("normalQty", partList.get(0).get("NORMAL_QTY").toString());
                            tempmap.put("zcQty", partList.get(0).get("ZCFC_QTY").toString());
                            tempmap.put("pdQty", partList.get(0).get("PDFC_QTY").toString());
                            tempmap.put("pkQty", partList.get(0).get("PKFC_QTY").toString());
                        } else {
                            Map<String, String> errormap = new HashMap<String, String>();
                            errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                            errormap.put("2", "配件编码");
                            errormap.put("3", "无库存记录不能操作！");
                            errorInfo.add(errormap);
                        }
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "配件编码");
                        errormap.put("3", "不存在");
                        errorInfo.add(errormap);
                    }
                }

                if (cells.length < 3 || CommonUtils.isEmpty(cells[1].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "调整数量");
                    errormap.put("3", "空");
                    errorInfo.add(errormap);
                } else {
                    String accTemp = cells[2].getContents().trim();
                    if (null == accTemp || "".equals(accTemp)) {
                        accTemp = "0";
                    } else {
                        accTemp = accTemp.replace(",", "");
                    }

                    String regex = "(^[1-9]+\\d*$)";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(accTemp);

                    if (matcher.find()) {
                        tempmap.put("returnQty", accTemp);
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "调整数量");
                        errormap.put("3", "非法数值!");
                        errorInfo.add(errormap);
                    }
                }
                tempmap.put("locName",cells[1].getContents().trim());
                tempmap.put("remark", cells.length < 4 || null == cells[3].getContents() ? "" : cells[3].getContents().trim());
                voList.add(tempmap);
            }
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-22
     * @Title : 库存状态变更详情查询
     */
    public void partStockDetailSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String changeId = CommonUtils.checkNull(request.getParamValue("changeId")); // 变更单ID
            String queryType = request.getParamValue("queryType"); // 查询类型

            StringBuffer sbString = new StringBuffer();
            if (null != changeId && !"".equals(changeId)) {
                sbString.append(" AND TD.CHANGE_ID = '" + changeId + "' ");
            }
            if (null != queryType && !"".equals(queryType) && "handle".equalsIgnoreCase(queryType)) {
                sbString.append(" AND (NVL(TD.RETURN_QTY,'0') - NVL(TD.COLSE_QTY, '0')) > 0 ");
                sbString.append(" AND TD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 + "' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartStockDeatil(
                    sbString.toString(), Constant.PAGE_SIZE_MIDDLE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件库存状态变更信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-9-25
     * @Title : 状态变更处理
     */
    @SuppressWarnings("unchecked")
    public void commitHandleResult() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String success = "";
        String errorExist = "";
        try {
            String parentOrgId = CommonUtils.checkNull(req.getParamValue("parentOrgId")); //制单单位ID
            String parentOrgCode = CommonUtils.checkNull(req.getParamValue("parentOrgCode")); //制单单位编码
            String chgorgCname = CommonUtils.checkNull(req.getParamValue("chgorgCname")); //制单单位名称
            String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
            String whName = CommonUtils.checkNull(req.getParamValue("whName")); //仓库名称
            String configId = Constant.PART_CODE_RELATION_19 + "";//配置ID
            int bzType1 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01;//正常
            int bzType2 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02;//盘盈
            int bzType3 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03;//盘亏
            int bzType4 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_04;//来货错误
            int bzType5 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_05;//质量问题
            int bzType6 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_06;//借条
            int bzType7 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_07;//现场BO
            int cgType1 = Constant.PART_STOCK_STATUS_CHANGE_TYPE_01;//封存
            int cgType2 = Constant.PART_STOCK_STATUS_CHANGE_TYPE_02;//解封

            TtPartRecordPO insertPRPo = null;

            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            Long userId = logonUser.getUserId(); //操作人ID
            String name = logonUser.getName();
            Date date = new Date();
            String dtlIds = CommonUtils.checkNull(req.getParamValue("dtlIds")); //明细ID
            String dtlIdArr[] = null;
            if (null != dtlIds && !"".equals(dtlIds)) {
                dtlIdArr = dtlIds.trim().split(",");
            }

            if (null != dtlIdArr) {
                int leth = dtlIdArr.length;
                TtPartChgStateMainPO selBMPo = null;
                TtPartChgStateMainPO updBMPo = null;
                TtPartChgStateDtlPO selBDPo = null;
                TtPartChgStateDtlPO updBDPo = null;
                long changeId = 0;
                String sbStr = "";
                List<Map<String, Object>> chgDtlList = null;

                for (int i = 0; i < leth; i++) {
                    String dtlId = dtlIdArr[i].split(":")[0];//明细ID
                    String clsQty = dtlIdArr[i].split(":")[1];//处理数量
                    String remark = CommonUtils.checkNull(request.getParamValue("clsRmk_" + dtlId));//处理备注
                    int chgType = Integer.parseInt(request.getParamValue("chgType_" + dtlId));//调整类型
                    int bzType = Integer.parseInt(request.getParamValue("chgReason_" + dtlId));//业务类型
                    int cgType = cgType2; //默认解封
                    //如果原单据为解封，调整类型则为封存
                    if (cgType2 == chgType) {
                        cgType = cgType1;
                    }

                    String sql = " AND TD.DTL_ID = '" + dtlId + "'";
                    List<Map<String, Object>> dtlList = dao.queryPartStockDeatil(sql);

                    if (null != dtlList && dtlList.size() == 1) {
                        String unClsNum = dtlList.get(0).get("UNCLS_QTY").toString();//未（可）处理数量
                        String closeQty = dtlList.get(0).get("COLSE_QTY").toString();//已处理数量
                        changeId = Long.parseLong(dtlList.get(0).get("CHANGE_ID").toString()); //变更单ID

                        long partId = Long.parseLong(dtlList.get(0).get("PART_ID").toString());//配件ID
                        String partCode = dtlList.get(0).get("PART_CODE").toString();//配件件号
                        String partOldcode = dtlList.get(0).get("PART_OLDCODE").toString();//配件编码
                        String partCname = dtlList.get(0).get("PART_CNAME").toString();//配件名称
                        Long locId = Long.parseLong(dtlList.get(0).get("LOC_ID").toString());//货位id
                        String batchNo = dtlList.get(0).get("BATCH_NO").toString(); //批次号
                        String partVenId = dtlList.get(0).get("VENDER_ID").toString(); //供应商id
                        
                        List<Map<String, Object>> warLocList = dao.getWareLocaInfos(locId+"");
                        String whCode = warLocList.get(0).get("WH_CODE").toString();
//                        Long locId = Long.parseLong(warLocList.get(0).get("LOC_ID").toString());
                        String locCode = warLocList.get(0).get("LOC_CODE").toString();
                        String locName = warLocList.get(0).get("LOC_CODE").toString();

                        List<Map<String, Object>> partList = dao.getPartStockInfos(partOldcode, parentOrgId, whId,locCode, batchNo);

                        int zcfcQty = 0;//正常封存数量
                        int pkfcQty = 0;//盘亏封存数量
                        int pyfcQty = 0;//盘盈封存数量

                        if (null != partList && partList.size() > 0) {
                            zcfcQty = Integer.parseInt(partList.get(0).get("ZCFC_QTY").toString());
                            pkfcQty = Integer.parseInt(partList.get(0).get("PKFC_QTY").toString());
                            pyfcQty = Integer.parseInt(partList.get(0).get("PDFC_QTY").toString());
                        }

                        long clsNum = 0;//处理数量
                        long returnQty = 0;
                        long canClsNum = 0;//可以处理数量
                        long closedNum = 0;//已处理数量
                        if (null != clsQty && !"".equals(clsQty)) {
                            clsNum = Long.parseLong(clsQty);
                            returnQty = clsNum;
                        }
                        if (null != unClsNum && !"".equals(unClsNum)) {
                            canClsNum = Long.parseLong(unClsNum);
                        }
                        if (null != closeQty && !"".equals(closeQty)) {
                            closedNum = Long.parseLong(closeQty);
                        }

                        if (clsNum <= canClsNum) {
                            if ((bzType1 == bzType || bzType4 == bzType || bzType5 == bzType || bzType6 == bzType || bzType7 == bzType) && cgType2 == cgType && clsNum > zcfcQty) {
                                errorExist = "处理数量不能大于正常封存数量： " + zcfcQty + " !";
                            } else if (bzType2 == bzType && cgType2 == cgType && clsNum > pyfcQty) {
                                errorExist = "处理数量不能大于盘盈封存数量： " + pyfcQty + " !";
                            } else if (bzType3 == bzType && cgType2 == cgType && clsNum > pkfcQty) {
                                errorExist = "处理数量不能大于盘亏封存数量： " + pkfcQty + " !";
                            } else {
                                selBDPo = new TtPartChgStateDtlPO();
                                updBDPo = new TtPartChgStateDtlPO();

                                selBDPo.setDtlId(Long.parseLong(dtlId));

                                updBDPo.setColseQty(closedNum + clsNum);

                                if (null != remark && !"".equals(remark)) {
                                    updBDPo.setRemark2(remark);//处理备注
                                }
                                updBDPo.setUpdateBy(userId);
                                updBDPo.setUpdateDate(date);

                                if (0 == (canClsNum - clsNum)) {
                                    updBDPo.setStatus(0);//已完成
                                }

                                dao.update(selBDPo, updBDPo);

                                insertPRPo = new TtPartRecordPO();
                                insertPRPo.setPartId(partId);
                                insertPRPo.setPartCode(partCode);
                                insertPRPo.setPartOldcode(partOldcode);
                                insertPRPo.setPartName(partCname);
                                insertPRPo.setPartNum(returnQty);//出入库数量
                                insertPRPo.setConfigId(Long.parseLong(configId));
                                insertPRPo.setOrderId(changeId);//变更单ID
                                insertPRPo.setLineId(Long.parseLong(dtlId));//变更单详情ID
                                insertPRPo.setOrgId(Long.parseLong(parentOrgId));
                                insertPRPo.setOrgCode(parentOrgCode);
                                insertPRPo.setOrgName(chgorgCname);
                                insertPRPo.setWhId(Long.parseLong(whId));
                                insertPRPo.setWhCode(whCode);
                                insertPRPo.setWhName(whName);
                                insertPRPo.setLocId(locId);
                                insertPRPo.setLocCode(locCode);
                                insertPRPo.setLocName(locName);
                                insertPRPo.setPartBatch(batchNo);
                                insertPRPo.setVenderId(Long.parseLong(partVenId));
                                insertPRPo.setOptDate(date);
                                insertPRPo.setCreateDate(date);
                                insertPRPo.setPersonId(userId);
                                insertPRPo.setPersonName(name);
                                
                                //正常/来货错误/质量问题/借条/现场BO  + 封存 (出入库数量为 正数)
                                if ((bzType1 == bzType || bzType4 == bzType || bzType5 == bzType || bzType6 == bzType || bzType7 == bzType) && cgType1 == cgType) {
                                    // 1.配件正常出库
                                    // 2.配件正常封存入库
                                    // 2:出库标识,1:正常,1:入库标识,2:正常封存
                                    outAndInStock(insertPRPo, 2, 1, 1, 2);
                                }
                                //正常/来货错误/质量问题/借条/现场BO + 解封(出入库数量为 正数)
                                else if ((bzType1 == bzType || bzType4 == bzType || bzType5 == bzType || bzType6 == bzType || bzType7 == bzType) && cgType2 == cgType && clsNum <= zcfcQty) {
                                    // 1.配件正常封存出库
                                    // 2.配件正常入库
                                    // 2:出库标识, 2:正常封存, 1:入库标识, 1:正常
                                    outAndInStock(insertPRPo, 2, 2, 1, 1);
                                }
                                //盘盈 + 封存(出入库数量为 正数)
                                else if (bzType2 == bzType && cgType1 == cgType) {
                                    // 1.配件正常封存入库
                                    // 1:入库标识, 3:盘点封存
                                    outAndInStock(insertPRPo, 1, 3, 0, 0);
                                }
                                //盘盈 + 解封(出入库数量为 正数)
                                else if (bzType2 == bzType && cgType2 == cgType && clsNum <= pyfcQty) {
                                    // 1.配件盘盈封存出库
                                    // 2.配件正常入库
                                    // 2:出库标识, 3:盘盈封存, 1:入库标识, 1:正常
                                    outAndInStock(insertPRPo, 2, 3, 1, 1);
                                }
                                //盘亏 + 封存(出入库数量为 正数)
                                else if (bzType3 == bzType && cgType1 == cgType) {
                                    // 1.正常出库
                                    // 2.盘亏入库   -> 入库数量为 正数
                                    // 2:出库标识, 1:正常, 1:入库标识, 4:盘亏封存标识
                                    outAndInStock(insertPRPo, 2, 1, 1, 4);
                                }
                                //盘亏 + 解封(出入库数量为 正数)
                                else if (bzType3 == bzType && cgType2 == cgType && clsNum <= pkfcQty) {
                                    // 1.配件盘盈封存出库
                                    // 2.盘亏解封入库
                                    // 2:出库标识, 1:盘亏封存, 1:入库标识, 4:正常
                                    outAndInStock(insertPRPo, 2, 4, 1, 1);
                                }
                            }

                        } else {
                            errorExist = "处理数量不能大于可处理数量 " + canClsNum + " !";
                        }

                    } else {
                        errorExist = "操作失败，请联系管理员!";
                    }

                    //判断明细是否已全部处理
                    sbStr = " AND TD.CHANGE_ID = '" + changeId + "' AND TD.STATUS = '1' ";
                    chgDtlList = dao.queryPartStockDeatil(sbStr);

                    if (null != chgDtlList && chgDtlList.size() == 0) {
                        //现场BO单主表
                        selBMPo = new TtPartChgStateMainPO();
                        updBMPo = new TtPartChgStateMainPO();

                        selBMPo.setChangeId(changeId);

                        updBMPo.setUpdateBy(userId);
                        updBMPo.setUpdateDate(date);
                        updBMPo.setState(0);//已完成

                        dao.update(selBMPo, updBMPo);
                    }
                    success = "操作成功!";
                }
            }

            act.setOutData("errorExist", errorExist);//返回错误信息
            act.setOutData("success", success);
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "处理状态变更单异常");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-22
     * @Title :导出配件库存状态变更信息
     */
    public void exportPartStockStatusExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String changeCode = CommonUtils.checkNull(request
                    .getParamValue("changeCode")); // 变更单号
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 截止时间
            String whId = CommonUtils.checkNull(request
                    .getParamValue("whId")); // 截止时间
            String remark = CommonUtils.checkNull(request
                    .getParamValue("remark")); // 备注
            String isFinish = CommonUtils.checkNull(request
                    .getParamValue("isFinish")); // 完成状态
            String partOldcode = CommonUtils.checkNull(request
                    .getParamValue("partOldcode")); // 配件编码
            String maker = CommonUtils.checkNull(request
                    .getParamValue("maker")); // 制单人

            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            StringBuffer sbString = new StringBuffer();
            StringBuffer sbStr = new StringBuffer();
            if (null != changeCode && !"".equals(changeCode)) {
                sbString.append(" AND TM.CHANGE_CODE LIKE '%" + changeCode + "%' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND TM.WH_ID = '" + whId + "' ");
            }
            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
            }

            if (null != remark && !"".equals(remark)) {
                sbString.append(" AND TM.REMARK LIKE '%" + remark.trim() + "%' ");
            }
            if (null != isFinish && !"".equals(isFinish)) {
                sbString.append(" AND TM.STATE = '" + isFinish + "' ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbStr.append(" AND UPPER(TD.PART_OLDCODE) LIKE '%" + partOldcode.trim().toUpperCase() + "%'");
            }
            if (null != maker && !"".equals(maker)) {
                sbString.append(" AND U.NAME LIKE '%" + maker.trim() + "%' ");
            }

            String[] head = new String[10];
            head[0] = "序号";
            head[1] = "变更单号";
            head[2] = "制单单位";
            head[3] = "仓库";
            head[4] = "制单人";
            head[5] = "制单日期";
            head[6] = "备注";
            head[7] = "完成状态";
            List<Map<String, Object>> list = dao.queryAllPartStockStatus(sbString.toString(), sbStr.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[10];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("CHANGE_CODE"));
                        detail[2] = CommonUtils
                                .checkNull(map.get("CHGORG_CNAME"));
                        detail[3] = CommonUtils
                                .checkNull(map.get("WH_CNAME"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("NAME"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("CREATE_DATE"));
                        detail[6] = CommonUtils.checkNull(map
                                .get("REMARK"));
                        if ("1".equals(map.get("STATE"))) {
                            detail[7] = "未完成";
                        } else {
                            detail[7] = "已完成";
                        }
                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件库存状态变更信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(), request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件库存状态变更");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}
