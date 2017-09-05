package com.infodms.dms.actions.parts.purchaseOrderManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderChkDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderInDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrInstockDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.POContext;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : chenjunjiang
 *         CreateDate     : 2013-4-16
 * @ClassName : PurchaseOrderInManager
 * @Description : 采购订单入库
 */
public class PurchaseOrderInManager implements PTConstants {

    public Logger logger = Logger.getLogger(PurchaseOrderInManager.class);
    private PurchaseOrderInDao dao = PurchaseOrderInDao.getInstance();
    private PurchaseOrderChkDao dao2 = PurchaseOrderChkDao.getInstance();

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "采购订单入库.xls";
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

    //去除数组中重复的记录
    public static String[] array_unique(String[] a) {
        // array_unique
        List<String> list = new LinkedList<String>();
        for (int i = 0; i < a.length; i++) {
            if (!list.contains(a[i])) {
                list.add(a[i]);
            }
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 采购订单入库查询初始化, 转到查询页面, 并准备页面数据
     */
    public void purchaseOrderInQueryInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PurchasePlanSettingDao dao1 = PurchasePlanSettingDao.getInstance();
        try {

            List list = dao.getPartWareHouseList(logonUser);//获取配件库房信息
            List whmans = dao2.queryWhmanInfo();
            List<Map<String, Object>> planerList = dao1.getUserPoseLise(1, null);
            request.setAttribute("planerList", planerList);
            act.setOutData("whmans", whmans);
            act.setOutData("wareHouses", list);
            act.setOutData("userId", logonUser.getUserId());
            act.setForword(PART_PURCHASEORDERIN_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单入库");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询库存数量
     */
    public void getNormalQty() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
            String checkId = CommonUtils.checkNull(request.getParamValue("checkId"));
            Long normalQty = 0l;
            Map<String, Object> map = dao.getNormalQty(partId, whId);
            if (map != null && map.get("NORMAL_QTY") != null) {
                normalQty = ((BigDecimal) (map.get("NORMAL_QTY"))).longValue();
            }
            act.setOutData("normalQty", normalQty);
            act.setOutData("checkId", checkId);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "当前配件库存");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询入库信息
     */
    public void queryOrderInInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//采购订单号
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));//验收单号
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//制单开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//制单结束时间
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房id
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件种类
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String checkBeginTime = CommonUtils.checkNull(request.getParamValue("checkBeginTime"));//验货开始时间
            String checkEndTime = CommonUtils.checkNull(request.getParamValue("checkEndTime"));//验货结束时间
            String checkName = CommonUtils.checkNull(request.getParamValue("CHECK_NAME"));//验货人员
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//件号
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//状态
            String whmanId = CommonUtils.checkNull(request.getParamValue("WHMAN_ID"));//库管员
            String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_ID"));//计划员

            TtPartPoChkPO po = new TtPartPoChkPO();
            po.setOrderCode(orderCode);
            if (!"".equals(whId)) {
                po.setWhId(CommonUtils.parseLong(whId));
            }
            if (!"".equals(partType)) {
                po.setPartType(CommonUtils.parseInteger(partType));
            }
            if (!"".equals(venderId)) {
                po.setVenderId(CommonUtils.parseLong(venderId));
            }
            po.setPartOldcode(partOldCode);
            po.setPartCname(partName);
            po.setPartCode(partCode);
            if (Utility.testString(state)) {
                po.setState(CommonUtils.parseInteger(state));
            }

            Integer pageSize = Constant.PAGE_SIZE;
            if (!"".equals(chkCode)) {
                pageSize = 1000;
            }
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOrderInList(po, beginTime, endTime, checkBeginTime, checkEndTime,
                    checkName, chkCode, planerId, whmanId, curPage, pageSize);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单入库");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title :
     * @Description: 订单入库
     */
    @SuppressWarnings("unchecked")
    public void inPurchaseOrder() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrInstockDao partDlrInstockDao = PartDlrInstockDao.getInstance();
        try {
            String[] checkIds = request.getParamValues("ids");//获取验收单ID
            //String remark1 = CommonUtils.checkNull(request.getParamValue("REMARK1"));//入库备注
            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            String errors = "";//错误信息
            String success = "";//成功信息
            String[] chkCodes = new String[checkIds.length];//验收单号
            String inCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_04);//入库单编码
            for (int i = 0; i < checkIds.length; i++) {
                //POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
                String str_whId = request.getParamValue("WH_ID" + checkIds[i]);//入库库房
                String relInQty = request.getParamValue("RELIN_QTY" + checkIds[i]);//入库数量
                String remark = request.getParamValue("REMARK" + checkIds[i]);//备注

                TtPartPoChkPO po = new TtPartPoChkPO();
                po.setCheckId(CommonUtils.parseLong(checkIds[i]));
                po = (TtPartPoChkPO) dao.select(po).get(0);

                boolean isLocked = dao.isLocked(po.getPartId(), str_whId, "", logonUser.getDealerId() == null ? logonUser.getOrgId() : Long.valueOf(logonUser.getDealerId()));//当前仓库中的配件是否被锁定

                if (isLocked) {//如果该配件在库存中已经被锁定
                    errors += "验收单【" + po.getCheckCode() + "】中的配件【" + po.getPartCname() + "】已经被锁定,请重新选择!<br>";
                    break;
                } else {

                    //判断当前要入库的仓库是否有效
                    TtPartWarehouseDefinePO warehouseDefinePO = new TtPartWarehouseDefinePO();
                    warehouseDefinePO.setWhId(CommonUtils.parseLong(str_whId));
                    warehouseDefinePO.setState(Constant.STATUS_ENABLE);
                    warehouseDefinePO.setStatus(1);

                    List wList = dao.select(warehouseDefinePO);
                    if (wList.size() == 0) {
                        errors += "验收单【" + po.getCheckCode() + "】中配件【" + po.getPartCname() + "】对应的入库库房已经失效,请选择重新选择!<br>";
                        break;
                        //POContext.endTxn(false);//回滚事务
                        //POContext.cleanTxn();
                    } else {

                        //获取采购订单验收表中的版本号
                        int ver = po.getVer();

                        if ((po.getCheckQty().longValue() - po.getInQty().longValue()) == 0) {//如果验货数量等于已入库数量就表示已经入库完成

                            errors += "验收单【" + po.getCheckCode() + "】中配件【" + po.getPartCname() + "】已经入库完成,请选择其他!<br>";
                            POContext.endTxn(false);//回滚事务
                            POContext.cleanTxn();
                            break;

                        } else if ((po.getCheckQty().longValue() - po.getInQty().longValue()) < CommonUtils.parseLong(relInQty).longValue()) {
                            errors += "验收单【" + po.getCheckCode() + "】中配件【" + po.getPartCname() + "】的入库数量不能大于验货数量与已入库数量之差!";
                            POContext.endTxn(false);//回滚事务
                            POContext.cleanTxn();
                            break;
                        } else {

                            //判断是否有对应货位信息 add by yuan 20130515
                            /*TtPartLoactionDefinePO locPo = new TtPartLoactionDefinePO();
                            locPo.setWhId(Long.valueOf(str_whId));
                            locPo.setPartId(po.getPartId());
                            locPo.setStatus(1);
                            locPo.setState(Constant.STATUS_ENABLE);

                            List<TtPartLoactionDefinePO> ll = dao.select(locPo);
                            if (ll.size() == 0) {
                                //无货位则为该配件插入一条货位信息
                                //act.setOutData("error",po.getPartOldcode()+"无对应货位,请维护后再点入库!");
                                locPo.setLocId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                                locPo.setLocCode("暂无");
                                locPo.setLocName("暂无");
                                locPo.setOrgId(logonUser.getOrgId());
                                locPo.setCreateDate(new Date());
                                locPo.setCreateBy(-1l);
                                dao.insert(locPo);

                            }*/

                            po.setInQty(po.getInQty() + CommonUtils.parseLong(relInQty));//已入库数量
                            po.setSpareQty((po.getCheckQty() - po.getInQty()));//设置待入库数量


                            Long partId = po.getPartId();//获取配件id
                            Long orgId = po.getOrgId();//获取单位id
                            Long whId = CommonUtils.parseLong(str_whId);//库房id

                            List<OrgBean> orgBeanList = PartWareHouseDao.getInstance().getOrgInfo(logonUser);
                            Map<String, Object> locMap = partDlrInstockDao.getLoc(po.getPartId().toString(), whId.toString(), orgBeanList.get(0).getOrgId().toString());

                            Map map = dao.queryPartAndLocationInfo(partId, orgId, whId);//查询当前配件信息及其货位信息

                            TtPartWarehouseDefinePO warehousePO = new TtPartWarehouseDefinePO();
                            warehousePO.setWhId(whId);
                            warehousePO = (TtPartWarehouseDefinePO) dao.select(warehousePO).get(0);//获取当前所选入库仓库信息

                            //modify by yuan 20130809
                            if (1 == 2) {//如果没有采购员就提示错误信息
                                errors += "验收单" + po.getCheckCode() + "下的配件【" + po.getPartCname() + "】没有采购员,请设置!<br>";
                            } else {

                                chkCodes[i] = po.getCheckCode();
                                //更新tt_part_oem_po中的待入库数量与已入库数量
                                TtPartOemPoPO oemPoPO = new TtPartOemPoPO();
                                TtPartOemPoPO oemPoPO1 = new TtPartOemPoPO();
                                TtPartOemPoPO oemPoPO2 = new TtPartOemPoPO();

                                oemPoPO.setPoId(po.getPoId());
                                oemPoPO = (TtPartOemPoPO) dao.select(oemPoPO).get(0);

                                oemPoPO1.setPoId(po.getPoId());
                                oemPoPO2.setSpareinQty(oemPoPO.getSpareinQty() - CommonUtils.parseLong(relInQty));
                                oemPoPO2.setInQty(oemPoPO.getInQty() + CommonUtils.parseLong(relInQty));
                                oemPoPO2.setRemark1(remark);

                                dao.update(oemPoPO1, oemPoPO2);

                                //入库的时候要判断相同验收单号、相同配件、相同库房是否已经入过库了,如果已经入库,那再次入库就更新入库数量,否则新增入库信息
                                Map<String, Object> Imap = dao.getInfoByChkCodeAndWhIdAndPartId(po.getCheckCode(), whId, partId);

                                Long InId = 0l;
                                TtPartPoInPO inPo = new TtPartPoInPO();//需要将每次入库的信息保存到入库表中

                                if (Imap != null) {

                                    InId = ((BigDecimal) (Imap.get("IN_ID"))).longValue();
                                    inCode = (String) Imap.get("IN_CODE");
                                    TtPartPoInPO inPO1 = new TtPartPoInPO();
                                    TtPartPoInPO inPO2 = new TtPartPoInPO();
                                    inPO1.setInId(InId);
                                    inPO2.setInQty(((BigDecimal) (Imap.get("IN_QTY"))).longValue() + CommonUtils.parseLong(relInQty));
                                    double camount = Arith.mul(po.getBuyPrice(), inPO2.getInQty());
                                    inPO2.setInAmount(camount);
                                    inPO2.setInAmountNotax(Arith.mul(((BigDecimal) (Imap.get("BUY_PRICE_NOTAX"))).doubleValue(), inPO2.getInQty()));
                                    inPO2.setInDate(new Date());
                                    inPO2.setInBy(logonUser.getUserId());

                                    //查询库存数量 add by yuan 20131208
                                    VwPartStockPO itemStockPO = new VwPartStockPO();
                                    itemStockPO.setPartId(po.getPartId());
                                    itemStockPO.setOrgId(po.getOrgId());
                                    itemStockPO.setWhId(po.getWhId());
                                    itemStockPO.setLocId(((BigDecimal) locMap.get("LOC_ID")).longValue());
                                    itemStockPO.setState(Constant.STATUS_ENABLE);
                                    List list = dao.select(itemStockPO);
                                    if (list.size() > 0) {
                                        itemStockPO = (VwPartStockPO) list.get(0);
                                        inPO2.setItemQty(itemStockPO.getItemQty());
                                    }
                                    // add end
                                    dao.update(inPO1, inPO2);
                                } else {

                                    //设置入库信息
                                    InId = CommonUtils.parseLong(SequenceManager.getSequence(""));
                                    inPo.setInId(InId);
                                    inPo.setInCode((inCode));
                                    inPo.setInType(Constant.PART_PURCHASE_ORDERCIN_TYPE_01);
                                    //inPo.setCheckId(CommonUtils.parseLong(checkIds[i]));//验货ID
                                    inPo.setCheckId(po.getChkId());
                                    inPo.setCheckCode(po.getCheckCode());//验货单号
                                    inPo.setPoId(po.getPoId());//采购订单iD
                                    inPo.setOrderCode(po.getOrderCode());//采购订单号
                                    inPo.setPlanCode(po.getPlanCode());//计划单号
                                    if (map != null && map.get("BUYER_ID") != null) {
                                        inPo.setBuyerId(((BigDecimal) map.get("BUYER_ID")).longValue());//采购员ID
                                    }
                                    inPo.setPartType(po.getPartType());//配件类型
                                    inPo.setPartId(po.getPartId());//配件ID
                                    inPo.setPartCode(po.getPartCode());//配件件号
                                    inPo.setPartOldcode(po.getPartOldcode());//配件编码
                                    inPo.setPartCname(po.getPartCname());//配件名称
                                    inPo.setUnit(po.getUnit());//配件包装单位
                                    inPo.setVenderId(po.getVenderId());//配件供应商ID
                                    inPo.setVenderCode(po.getVenderCode());//配件供应商编码
                                    inPo.setVenderName(po.getVenderName());//配件供应商名称
                                    inPo.setMakerId(po.getMakerId());//制造商id
                                    inPo.setBuyPrice(po.getBuyPrice());//配件采购单价，通过和供应商关系获取
                                    inPo.setBuyQty(po.getBuyQty());//采购数量
                                    inPo.setCheckQty(po.getCheckQty());//验货数量
                                    inPo.setInQty(CommonUtils.parseLong(relInQty));//已入库数量
                                    inPo.setBuyPriceNotax(Arith.div(po.getBuyPrice(), Arith.add(1, Constant.PART_TAX_RATE)));//无税单价
                                    inPo.setInAmount(Arith.mul(po.getBuyPrice(), inPo.getInQty()));//入库金额
                                    inPo.setTaxRate(Constant.PART_TAX_RATE);//税率
                                    inPo.setInAmountNotax(Arith.mul(inPo.getBuyPriceNotax(), inPo.getInQty()));//无税总金额
                                    inPo.setWhId(whId);//库房iD
                                    inPo.setWhName(warehousePO.getWhName());//库房名称
                                    inPo.setLocCode((String) locMap.get("LOC_CODE"));//货位编码
                                    inPo.setLocId(((BigDecimal) locMap.get("LOC_ID")).longValue());//货位ID

                                    //查询库存数量
                                    VwPartStockPO itemStockPO = new VwPartStockPO();
                                    itemStockPO.setPartId(inPo.getPartId());
                                    itemStockPO.setLocId(inPo.getLocId());
                                    itemStockPO.setOrgId(inPo.getOrgId());
                                    itemStockPO.setWhId(inPo.getWhId());
                                    itemStockPO.setState(Constant.STATUS_ENABLE);
                                    List list = dao.select(itemStockPO);
                                    if (list.size() > 0) {
                                        itemStockPO = (VwPartStockPO) list.get(0);
                                        inPo.setItemQty(itemStockPO.getItemQty());
                                    }
                                    inPo.setRemark(remark);
                                    inPo.setInDate(new Date());
                                    inPo.setInBy(logonUser.getUserId());
                                    inPo.setCheckDate(po.getCheckDate());
                                    inPo.setCheckBy(po.getCheckBy());
                                    inPo.setCreateDate((new Date()));
                                    inPo.setCreateBy(po.getCreateBy());
                                    inPo.setOrgId(logonUser.getOrgId());
                                    inPo.setOriginType(po.getOriginType());
                                    //配件只要入库，入库单的状态就变为结算中
                                    inPo.setState(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01);

                                }


                                //如果验货数量等于已入库数量就要更新该验收单的状态为入库完成
                                //modify by yuan  20130521
                                //if(po.getCheckQty()==po.getInQty()){
                                if (po.getCheckQty().equals(po.getInQty())) {
                                    po.setState(Constant.PART_PURCHASE_ORDERCIN_STATUS_02);
                                }
                                po.setRemark(remark);

                                //插入出入库记录表 add by yuan 20130513
                                TtPartRecordPO ttPartRecordPO = new TtPartRecordPO();
                                ttPartRecordPO.setRecordId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                                ttPartRecordPO.setAddFlag(1);//入库标记
                                ttPartRecordPO.setState(1);//正常入库
                                ttPartRecordPO.setPartNum(CommonUtils.parseLong(relInQty));//入库数量
                                ttPartRecordPO.setTranstypeId(0l);//默认0
                                ttPartRecordPO.setPartId(po.getPartId());//配件ID
                                ttPartRecordPO.setPartCode(po.getPartCode());//配件件号
                                ttPartRecordPO.setPartOldcode(po.getPartOldcode());//配件编码
                                ttPartRecordPO.setPartName(po.getPartCname());//配件名称
                                ttPartRecordPO.setPartBatch("1306");//////////////////配件批次
                                ttPartRecordPO.setVenderId(21799l);///////////////////配件供应商
                                ttPartRecordPO.setConfigId(Long.valueOf(Constant.PART_CODE_RELATION_04));//入库单
                                ttPartRecordPO.setOrderId(InId);//入库单ID
                                ttPartRecordPO.setOrderCode(inCode);//入库单编码
                                //ttPartRecordPO.setLineId();
                                ttPartRecordPO.setOrgId(orgBeanList.get(0).getOrgId());
                                ttPartRecordPO.setOrgCode(orgBeanList.get(0).getOrgCode());
                                ttPartRecordPO.setOrgName(orgBeanList.get(0).getOrgName());
                                ttPartRecordPO.setWhId(whId);
                                ttPartRecordPO.setWhName(warehousePO.getWhName());
                                //modify by yuan 20130809
                                //ttPartRecordPO.setLocId(((BigDecimal) map.get("LOC_ID")).longValue());
                                //ttPartRecordPO.setLocCode((String) map.get("LOC_CODE"));
                                //ttPartRecordPO.setLocName((String) map.get("LOC_NAME"));
                                ttPartRecordPO.setLocId(Long.valueOf(CommonUtils.checkNull(locMap.get("LOC_ID"))));
                                ttPartRecordPO.setLocCode(CommonUtils.checkNull(locMap.get("LOC_CODE")));
                                ttPartRecordPO.setLocName(CommonUtils.checkNull(locMap.get("LOC_NAME")));
                                ttPartRecordPO.setOptDate(new Date());
                                ttPartRecordPO.setCreateDate(new Date());
                                ttPartRecordPO.setPersonId(logonUser.getUserId());
                                ttPartRecordPO.setPersonName(logonUser.getName());
                                ttPartRecordPO.setPartState(1);

                                dao.update(po);//更新验收表,此时将会锁定该记录,直到事务提交才释放
                                po.setVer(ver + 1);//让版本号加1
                                //提交事务之前需要比较开始读取的版本号(ver)与此时版本号(po.getVer()),如果发现po.getVer()>ver就入库成功,否则失败并回滚数据
                                Map map1 = dao.getVerByCheckId(po.getCheckId());
                                int ver1 = ((BigDecimal) map1.get("VER")).intValue();
                                if (po.getVer() > ver1) {
                                    if (inPo.getInId() != null) {
                                        dao.insert(inPo);//新增订单入库信息
                                    }
                                    dao.insert(ttPartRecordPO);//新增出入库记录
                                    dao.updateVer(po);//把当前最新版本号更新到数据库
                                    //调用入库逻辑
                                    List ins = new LinkedList<Object>();
                                    ins.add(0, InId);
                                    ins.add(1, Constant.PART_CODE_RELATION_04);
                                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
                                    //POContext.endTxn(true);//提交事务
                                    //POContext.cleanTxn();
                                } else {
                                    errors += "验收单【" + po.getCheckCode() + "】中的配件【" + po.getPartCname() + "】正在入库,请稍后再试!<br>";
                                    POContext.endTxn(false);//回滚事务
                                    POContext.cleanTxn();
                                    break;
                                }
                                //add by yuan 20130831
                                //dao.updateRemark1(array_unique(chkCodes),remark1,po.getPartId().toString());
                            }

                        }

                    }

                }

            }

            if ("".equals(errors)) {
                success = "入库成功!";
                //更新tt_part_oem_po里面同一验收单号的最后入库人和最后入库时间
                //如果入库备注不为空,就需要更新tt_part_oem_po中的入库备注
                dao.updateInDateAndInby(array_unique(chkCodes));
                //dao.updateRemark1(array_unique(chkCodes),remark1);//需指定具体配件
            }

            act.setOutData("success", success);
            act.setOutData("error", errors);
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            POContext.endTxn(false);//回滚事务
            POContext.cleanTxn();
            BizException e1 = new BizException(act,
                    ErrorCodeConstant.SPECIAL_MEG, "采购订单入库失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title :
     * @Description: 入库删除
     */
    public void deletePo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);

        try {
            String poId = CommonUtils.checkNull(request
                    .getParamValue("poId")); // 采购订单Id
            String checkId = CommonUtils.checkNull(request
                    .getParamValue("checkId")); // 验收单Id
            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            TtPartPoChkPO chkPO = new TtPartPoChkPO();
            chkPO.setCheckId(CommonUtils.parseLong(checkId));
            chkPO = (TtPartPoChkPO) dao.select(chkPO).get(0);

            //删除该验收单
            TtPartPoChkPO chkPO1 = new TtPartPoChkPO();
            chkPO1.setCheckId(CommonUtils.parseLong(checkId));
            //TtPartPoChkPO chkPO2 = new TtPartPoChkPO();
            //chkPO2.setStatus(0);

            //删除验收单之后要把采购订单中的待验收数量增加,已验收数量减少,待入库数量减少,如果该采购订单的状态为验收完成,就要将其状态改为验收中
            TtPartOemPoPO oemPo = new TtPartOemPoPO();
            oemPo.setPoId(CommonUtils.parseLong(poId));
            oemPo = (TtPartOemPoPO) dao.select(oemPo).get(0);

            TtPartOemPoPO spo = new TtPartOemPoPO();
            spo.setPoId(CommonUtils.parseLong(poId));
            TtPartOemPoPO po = new TtPartOemPoPO();
            po.setSpareQty(oemPo.getSpareQty() + chkPO.getSpareQty());
            po.setCheckQty(oemPo.getCheckQty() - chkPO.getSpareQty());
            po.setSpareinQty(oemPo.getSpareinQty() - chkPO.getSpareQty());
            po.setState(Constant.PART_PURCHASE_ORDERCHK_STATUS_01);

            //删除制造商信息
            //po.setMakerId(0l);

            dao.delete(chkPO1);
            //dao.update(chkPO1, chkPO2);
            dao.update(spo, po);
            act.setOutData("success", "关闭成功!");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "删除失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-27
     * @Title :
     * @Description: 导出
     */
    public void exportOrderInExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        List wareHouses = new ArrayList();
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[18];
            head[0] = "采购订单号";
            head[1] = "验收单号";
            head[2] = "配件类型";
            head[3] = "配件件号";
            head[4] = "配件编码";
            head[5] = "配件名称";
            head[6] = "订购数量";
            head[7] = "验货数量";
            head[8] = "已入库数量";
            //zhumingwei 2013-09-11 begin
            head[9] = "计划价";
            head[10] = "计划金额";
            //zhumingwei 2013-09-11 end
            head[11] = "供应商名称";
            head[12] = "制造商名称";
            head[13] = "入库库房";
            head[14] = "验货日期";
            head[15] = "验货人员";
            head[16] = "状态";


            List<Map<String, Object>> list = dao.queryPurchaseOrderIn(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[18];
                        detail[0] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                        detail[1] = CommonUtils.checkNull(map
                                .get("CHECK_CODE"));
                        detail[2] = CommonUtils
                                .checkNull(map.get("PART_TYPE"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("PART_CODE"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("PART_OLDCODE"));
                        detail[5] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[6] = CommonUtils.checkNull(map.get("BUY_QTY"));
                        detail[7] = CommonUtils.checkNull(map.get("CHECK_QTY"));
                        detail[8] = CommonUtils.checkNull(map.get("IN_QTY"));
                        //zhumingwei 2013-09-11 begin
                        detail[9] = CommonUtils.checkNull(map.get("SALE_PRICE3"));
                        detail[10] = CommonUtils.checkNull(map.get("IN_AMOUNT"));
                        //zhumingwei 2013-09-11 end
                        detail[11] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                        detail[12] = CommonUtils.checkNull(map.get("MAKER_NAME"));
                        detail[13] = CommonUtils.checkNull(map.get("WH_NAME"));
                        detail[14] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[15] = CommonUtils.checkNull(map.get("BUYER"));
                        detail[16] = CommonUtils.checkNull(map.get("STATE"));

                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1);
            } else {
                wareHouses = dao.getPartWareHouseList(logonUser);//获取配件库房信息
                BizException e1 = new BizException(act,
                        ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
                        "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("wareHouses", wareHouses);
            act.setForword(PART_PURCHASEORDERIN_QUERY_URL);
        }

    }

    /**
     * zhumingwei
     *
     * @param :
     * @return :
     * @throws : LastDate    : 2013-9-11
     * @Title :
     * @Description: 采购订单入库打印
     */
    public void selectPartId() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));
            String start = CommonUtils.checkNull(request.getParamValue("start"));
            Date date = new Date();
            DateFormat myDate = new SimpleDateFormat("yyMMdd");
            ;
            TtPartDefinePO po = new TtPartDefinePO();
            po.setPartId(CommonUtils.parseLong(partId));
            if (dao.select(po).size() > 0) {
                TtPartDefinePO poValue = (TtPartDefinePO) dao.select(po).get(0);
                act.setOutData("vpoldcode", poValue.getPartOldcode() + "    [" + myDate.format(date) + "]");
                act.setOutData("vpCname", poValue.getPartCname());
                act.setOutData("vpEname", poValue.getPartEname());
                act.setOutData("vpCode", poValue.getPartCode());
                act.setOutData("vQty", poValue.getMinPkg());
                act.setOutData("start", start);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-9-13
     * @Title :  zhumingwei
     * @Description: 部分条码打印
     */
    public void selectPartIdByPrint() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));
            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));
            String WH_ID = CommonUtils.checkNull(request.getParamValue("WH_ID"));

            List<Map<String, Object>> list = dao.getDefinePrint(partId, venderId, WH_ID);
            for (int i = 0; i < list.size(); i++) {
                Map map = list.get(i);

                act.setOutData("vpoldcode", map.get("PART_OLDCODE").toString());
                act.setOutData("vpCname", map.get("PART_CNAME"));
                act.setOutData("vpEname", map.get("PART_ENAME"));
                act.setOutData("vpCode", map.get("PART_CODE").toString());
                act.setOutData("barCode", map.get("BARCODE").toString());
            }

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //zhumingwei 2013-11-19 条码打印新改（填写配件编码回车后自动带出相关信息）
    public void selectPartIdBy() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String PART_OLDCODE = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String WH_ID = CommonUtils.checkNull(request.getParamValue("WH_ID"));

            List<Map<String, Object>> list = dao.getDefine(PART_OLDCODE.toUpperCase(), WH_ID);//.toUpperCase()  是把小写的转化为大写
            for (int i = 0; i < list.size(); i++) {
                Map map = list.get(i);

                act.setOutData("PART_DATA", map.get("PART_ID"));
                act.setOutData("PART_OLDCODE", map.get("PART_OLDCODE").toString());
                act.setOutData("normalQty", map.get("NORMAL_QTY"));
                act.setOutData("min_package", map.get("OEM_MIN_PKG").toString());
                act.setOutData("locCode", map.get("LOC_CODE").toString());
            }

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}
