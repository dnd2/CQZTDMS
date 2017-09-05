package com.infodms.dms.actions.parts.salesManager.partResaleReceiveManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.salesManager.partResaleReceiveManager.partResRecDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartRecordPO;
import com.infodms.dms.po.TtPartRetailDtlPO;
import com.infodms.dms.po.TtPartRetailMainPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 配件零售/领用出库业务处理
 * @Date: 2013-4-28
 * @remark
 */
public class partResRecOutAction extends BaseImport {
    public Logger logger = Logger.getLogger(partResRecOutAction.class);
    private static final partResRecDao dao = partResRecDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    private static final int orderType1 = Constant.PART_SALE_STOCK_REMOVAL_TYPE_01;
    private static final int orderType2 = Constant.PART_SALE_STOCK_REMOVAL_TYPE_02;
    private static long preDetalId = 0;
    private static final int unLockedVal = Constant.PART_STATE_UN_LOCKED;  //配件未锁定

    //配件零售/领用出库URL
    private static final String PART_RESALE_RECCIVE_OUT = "/jsp/parts/salesManager/partResRecOut/partResRecOut.jsp";//配件零售/领用出库首页
    private static final String PART_RES_REC_OUT_VIEW = "/jsp/parts/salesManager/partResRecOut/partResRecOutView.jsp";//配件零售/领用出库查看页面


    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-28
     * @Title : 跳转至配件零售/领用出库页面
     */
    public void partResRecOutInit() {
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
            act.setOutData("oemId", Constant.OEM_ACTIVITIES);
            act.setForword(PART_RESALE_RECCIVE_OUT);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件零售/领用出库页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-28
     * @Title : 查询配件零售/领用出库信息
     */
    public void partResRecOutSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String changeCode = CommonUtils.checkNull(request
                    .getParamValue("changeCode")); // 制单单号
            String orderType = CommonUtils.checkNull(request
                    .getParamValue("orderType")); // 类型
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 截止时间
            String whId = CommonUtils.checkNull(request
                    .getParamValue("whId")); // 仓库ID
            int orderState = Constant.PART_RESALE_RECEIVE_ORDER_TYPE_02; // 状态：已提交
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            if (Constant.OEM_ACTIVITIES.equals(parentOrgId)) {
                orderState = Constant.PART_RESALE_RECEIVE_ORDER_TYPE_04; //状态：已通过
            }
            StringBuffer sbString = new StringBuffer();
            if (null != changeCode && !"".equals(changeCode)) {
                sbString.append(" AND TM.RETAIL_CODE LIKE '%" + changeCode + "%' ");
            }
            if (null != orderType && !"".equals(orderType)) {
                sbString.append(" AND TM.CHG_TYPE = '" + orderType + "' ");
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
                sbString.append(" AND TM.SORG_ID = '" + parentOrgId + "' ");
            }
            sbString.append(" AND TM.STATE = '" + orderState + "' ");

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartSaleOrders(
                    sbString.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件零售/领用出库信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-22
     * @Title : 跳转至配件零售/领用单查看页面
     */
    public void viewOrderDeatilInint() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
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
            String changeId = CommonUtils.checkNull(request.getParamValue("changeId"));// 变更单ID
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.RETAIL_ID = '" + changeId + "' ");
            Map<String, Object> map = dao.queryAllPartSaleOrders(sbString.toString()).get(0);

            Integer orderType = Integer.parseInt(map.get("CHG_TYPE").toString());

            if (orderType1 == orderType) {
                map.put("CHG_TYPE", "零售");
            } else if (orderType2 == orderType) {
                map.put("CHG_TYPE", "领用");
            }
            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("companyName", companyName);
            act.setOutData("map", map);
            act.setOutData("oemId", Constant.OEM_ACTIVITIES);
            act.setForword(PART_RES_REC_OUT_VIEW);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件零售/领用单查看页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-28
     * @Title : 保存配件零售/领用出库信息
     */
    @SuppressWarnings("unchecked")
    public void saveOrderInfos() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String errorExist = "";
        String success = "";
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            String retailId = CommonUtils.checkNull(req.getParamValue("changeId")); //零售/领用单ID
            String parentOrgId = CommonUtils.checkNull(req.getParamValue("parentOrgId")); //制单单位ID
            String parentOrgCode = CommonUtils.checkNull(req.getParamValue("parentOrgCode")); //制单单位编码
            String chgorgCname = CommonUtils.checkNull(req.getParamValue("chgorgCname")); //制单单位名称
            String configId = Constant.PART_CODE_RELATION_14 + "";//配置ID
            String partVenId = Constant.PART_RECORD_VENDER_ID;//供应商ID
            String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
            Long userId = logonUser.getUserId(); //出库人ID
            String name = logonUser.getName();

            // 查询当前所有出库配件的信息
            List<Map<String, Object>> partList = dao.getLatestQtys(retailId);
            Date date = new Date();
            TtPartRecordPO insertPRPo = null;
            TtPartRetailDtlPO selectRDPo = null;
            TtPartRetailDtlPO updateRDPo = null;
            List<Object> ins = null;
            POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);//开启事务
            for(Map<String, Object> map: partList){
                String dtlId = map.get("DTL_ID").toString();
                String reqDtlId = CommonUtils.checkNull(req.getParamValue(map.get("DTL_ID").toString()));
                if(CommonUtils.isEmpty(reqDtlId) || !dtlId.equals(reqDtlId)){
                    errorExist += "出库失败,请联系管理员!<br/>";
                    break;
                }
                
                Integer isLocked = Integer.parseInt(CommonUtils.checkNull(map.get("IS_LOCKED")));//是否锁定
                Long qty = Long.parseLong(CommonUtils.checkNull(map.get("QTY"))); //零售/领用数量
                Long outQty = Long.parseLong(CommonUtils.checkNull(map.get("OUT_QTY"))); // 已出库数量
                Long reqQty = Long.parseLong(CommonUtils.checkNull(req.getParamValue("outableQty_"+dtlId))); //请求出库数量
                String partOldcode = CommonUtils.checkNull(map.get("PART_OLDCODE"));   //配件编码
                if (unLockedVal != isLocked) {
                    errorExist += "配件：" + partOldcode + "已锁定,目前不能进行库存操作!<br/>";
                } else if (qty == outQty) {
                    errorExist += "配件：" + partOldcode + " 已完成出库，可出库数为0!<br/>";
                } else if (qty < (outQty + reqQty)) {
                    errorExist += "配件：" + partOldcode + " 请求出库总数量不能大零售/领用数：" + qty + " !<br/>";
                }
                if(!"".equals(errorExist)) break;

                String partId = CommonUtils.checkNull(map.get("PART_ID")); // 配件id
                String partCode = CommonUtils.checkNull(map.get("PART_CODE"));//件号
                String partCname = CommonUtils.checkNull(map.get("PART_CNAME")); //配件名称
                String locId = CommonUtils.checkNull(map.get("LOC_ID")); // 货位id
                String locCode = CommonUtils.checkNull(map.get("LOC_CODE")); // 货位编码
                String locName = CommonUtils.checkNull(map.get("LOC_NAME")); // 货位名
                String partBatch = CommonUtils.checkNull(map.get("BATCH_NO")); // 批次号
                String whName = CommonUtils.checkNull(map.get("WH_CNAME")); // 仓库名
                String whCode = CommonUtils.checkNull(map.get("WH_CODE")); // 仓库名
                int curVer = Integer.parseInt(CommonUtils.checkNull(map.get("VER")));//VER值
                long lineNo = Long.parseLong(CommonUtils.checkNull(map.get("LINE_NO")));//行号
                Long stockQty = Long.parseLong(map.get("STOCK_QTY").toString());// 当前库存

                // 更新出库数量
                selectRDPo = new TtPartRetailDtlPO();
                selectRDPo.setDtlId(Long.parseLong(dtlId));
                updateRDPo = new TtPartRetailDtlPO();
                updateRDPo.setStockQty(stockQty);
                updateRDPo.setOutQty((outQty + reqQty));
                updateRDPo.setUpdateBy(userId);
                updateRDPo.setUpdateDate(date);
                updateRDPo.setVer((curVer + 1));//版本号
                dao.update(selectRDPo, updateRDPo);
                
                // 插入出库记录
                insertPRPo = new TtPartRecordPO();
                Long recId = Long.parseLong(SequenceManager.getSequence(""));
                insertPRPo.setRecordId(recId);
                insertPRPo.setAddFlag(2);//出库
                insertPRPo.setPartId(Long.parseLong(partId));
                insertPRPo.setPartCode(partCode);
                insertPRPo.setPartOldcode(partOldcode);
                insertPRPo.setPartName(partCname);
                insertPRPo.setPartBatch(partBatch);
                insertPRPo.setVenderId(Long.parseLong(partVenId));
                insertPRPo.setPartNum(reqQty);//出库数量
                insertPRPo.setConfigId(Long.parseLong(configId));
                insertPRPo.setOrderId(Long.parseLong(retailId));//业务ID
                insertPRPo.setLineId(lineNo);//行ID
                insertPRPo.setOrgId(Long.parseLong(parentOrgId));
                insertPRPo.setOrgCode(parentOrgCode);
                insertPRPo.setOrgName(chgorgCname);
                insertPRPo.setWhId(Long.parseLong(whId));
                insertPRPo.setWhCode(whCode);
                insertPRPo.setWhName(whName);
                insertPRPo.setLocId(Long.parseLong(locId));
                insertPRPo.setLocCode(locCode);
                insertPRPo.setLocName(locName);
                insertPRPo.setOptDate(date);
                insertPRPo.setCreateDate(date);
                insertPRPo.setPersonId(userId);
                insertPRPo.setPersonName(name);
                insertPRPo.setPartState(1);//配件状态

                dao.insert(insertPRPo);
                
                //调用出入库逻辑
                ins = new LinkedList<Object>();
                ins.add(0, retailId);
                ins.add(1, configId);
                ins.add(2, 1);// 已预占出库

                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
            }

            if("".equals(errorExist)){
                POContext.endTxn(true);//提交数据
                POContext.cleanTxn();//清空事务
            }else{
                POContext.endTxn(false);//回滚
                POContext.cleanTxn();//清空事务
            }
            
            if ("".equals(errorExist)) {
                StringBuffer sb = new StringBuffer();
                if (null != retailId && !"".equals(retailId)) {
                    sb.append(" AND TD.RETAL_ID = '" + retailId + "' ");
                }
                sb.append(" AND (TD.QTY - TD.OUT_QTY) > 0 ");
                List<Map<String, Object>> deatilList = dao.queryPartSaleOrderDeatilList(sb.toString());
                if (null == deatilList || deatilList.size() <= 0) {
                    TtPartRetailMainPO selectRMPo = new TtPartRetailMainPO();
                    TtPartRetailMainPO updateRMPo = new TtPartRetailMainPO();
                    selectRMPo.setRetailId(Long.parseLong(retailId));
                    updateRMPo.setState(Constant.PART_RESALE_RECEIVE_ORDER_TYPE_03);//已完成
                    dao.update(selectRMPo, updateRMPo);
                    success = "all";
                }
                act.setOutData("success", success);
            } else {
                act.setOutData("errorExist", errorExist);// 返回错误信息
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, errorExist);
            }
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件零售/领用出库失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-22
     * @Title : 配件零售/领用出库详情查询
     */
    public void partOrderDetailSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String changeId = CommonUtils.checkNull(request.getParamValue("changeId")); // 订单ID

            StringBuffer sbString = new StringBuffer();
            if (null != changeId && !"".equals(changeId)) {
                sbString.append(" AND TD.RETAL_ID = '" + changeId + "' ");
            }

            sbString.append(" AND (TD.QTY - TD.OUT_QTY) > 0 ");

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartSaleOrderDeatil(
                    sbString.toString(), Constant.PAGE_SIZE_MIDDLE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件零售/领用出库详情信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-27
     * @Title :导出配件零售/领用单信息
     */
    public void exportSaleOrdersExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String changeCode = CommonUtils.checkNull(request
                    .getParamValue("changeCode")); // 制单单号
            String orderType = CommonUtils.checkNull(request
                    .getParamValue("orderType")); // 类型
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 截止时间
            String whId = CommonUtils.checkNull(request
                    .getParamValue("whId")); // 仓库ID
            int orderState = Constant.PART_RESALE_RECEIVE_ORDER_TYPE_02; // 状态：已提交
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            if (Constant.OEM_ACTIVITIES.equals(parentOrgId)) {
                orderState = Constant.PART_RESALE_RECEIVE_ORDER_TYPE_04; //状态：已通过
            }
            StringBuffer sbString = new StringBuffer();
            if (null != changeCode && !"".equals(changeCode)) {
                sbString.append(" AND TM.RETAIL_CODE LIKE '%" + changeCode + "%' ");
            }
            if (null != orderType && !"".equals(orderType)) {
                sbString.append(" AND TM.CHG_TYPE = '" + orderType + "' ");
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
                sbString.append(" AND TM.SORG_ID = '" + parentOrgId + "' ");
            }
            sbString.append(" AND TM.STATE = '" + orderState + "' ");

            String[] head = new String[9];
            head[0] = "序号";
            head[1] = "制单单号";
            head[2] = "类型";
            head[3] = "制单单位";
            head[4] = "仓库";
            head[5] = "制单人";
            head[6] = "制单日期";
            head[7] = "制单金额(元)";
            List<Map<String, Object>> list = dao.queryAllPartSaleOrders(sbString.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[9];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("RETAIL_CODE"));
                        Integer oderType = Integer.parseInt(CommonUtils.checkNull(map.get("CHG_TYPE")));
                        if (orderType1 == oderType) {
                            detail[2] = "零售";
                        } else if (orderType2 == oderType) {
                            detail[2] = "领用";
                        }
                        detail[3] = CommonUtils
                                .checkNull(map.get("SORG_CNAME"));
                        detail[4] = CommonUtils
                                .checkNull(map.get("WH_CNAME"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("NAME"));
                        detail[6] = CommonUtils.checkNull(map
                                .get("CREATE_DATE"));
                        detail[7] = CommonUtils.checkNull(map
                                .get("AMOUNTS"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件零售/领用出库信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件零售/领用出库信息");
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
}
