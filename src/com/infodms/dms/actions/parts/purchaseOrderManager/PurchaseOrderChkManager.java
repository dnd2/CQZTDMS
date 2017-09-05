package com.infodms.dms.actions.parts.purchaseOrderManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.*;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderChkDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 采购入库
 * @author : chenjunjiang
 *         CreateDate     : 2013-4-15
 * @ClassName : PurchaseOrderChkManager
 * @Description : 采购订单验收
 */
public class PurchaseOrderChkManager implements PTConstants {

    private static final Integer PRINT_SIZE = 10;
    public Logger logger = Logger.getLogger(PurchaseOrderChkManager.class);
    private PurchaseOrderChkDao dao = PurchaseOrderChkDao.getInstance();

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "采购订单验收.xls";
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
                     if(CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])){
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
                    }else{
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
     * @throws : LastDate    : 2013-4-15
     * @Title :
     * @Description: 查询初始化, 转到采购订单验收查询页面
     */
    public void purchaseOrderChkQueryInit() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PurchasePlanSettingDao dao1 = PurchasePlanSettingDao.getInstance();
        try {
            List list = dao.getPartWareHouseList(logonUser);//获取配件库房信息
            List<Map<String, Object>> planerList = dao1.getUserPoseLise(1, null);
            request.setAttribute("planerList", planerList);

            act.setOutData("curUserId", logonUser.getUserId());
            act.setOutData("wareHouses", list);
            act.setForword(PART_PURCHASEORDERINSTOCK_PRINT_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单验收");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 入库确认查询
     */
    public void purchaseOrderInStockMng() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String checkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));//验收单号
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//验收开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//验收结束时间
            String pBeginTime = CommonUtils.checkNull(request.getParamValue("pBeginTime"));//打印开始时间
            String pEndTime = CommonUtils.checkNull(request.getParamValue("pEndTime"));//打印结束时间
            String planId = CommonUtils.checkNull(request.getParamValue("PLANER_ID"));//计划员
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOrderChkAllList(checkCode, venderId, beginTime, endTime,
                    pBeginTime, pEndTime, planId, null, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
            act.setOutData("CHECK_CODE", checkCode);
            act.setOutData("VENDER_ID", venderId);
            act.setOutData("beginTime", beginTime);
            act.setOutData("endTime", endTime);
            act.setOutData("pBeginTime", pBeginTime);
            act.setOutData("pEndTime", pEndTime);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "验收单打印");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title :
     * @Description: 查询采购订单验收信息-明细查询
     */
    public void queryOrderChkInfo() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//采购订单号
            String checkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE2"));//验收单号
            String buyer = CommonUtils.checkNull(request.getParamValue("BUYER"));//采购员
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime2"));//开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime2"));//结束时间
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房id
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件种类
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//件号
            String state = CommonUtils.checkNull(request.getParamValue("STATE2"));//状态
            String isPrint = CommonUtils.checkNull(request.getParamValue("ISPRINT"));//是否打印
            String chkId = CommonUtils.checkNull(request.getParamValue("chkId"));//验收单id
            String originType = CommonUtils.checkNull(request.getParamValue("ORDER_ORIGIN_TYPE1"));//来源

            TtPartOemPoPO po = new TtPartOemPoPO();
            po.setOrderCode(orderCode);
            if (Utility.testString(whId)) {
                po.setWhId(CommonUtils.parseLong(whId));
            }
            if (Utility.testString(checkCode)) {
                po.setChkCode(checkCode);
            }
            if (Utility.testString(partType)) {
                po.setPartType(CommonUtils.parseInteger(partType));
            }
            if (Utility.testString(venderId)) {
                po.setVenderId(CommonUtils.parseLong(venderId));
            }
            if (Utility.testString(state)) {
                po.setState(CommonUtils.parseInteger(state));
            }
            po.setBuyer(buyer);
            po.setPartOldcode(partOldCode);
            po.setPartCname(partName);
            po.setPartCode(partCode);

            Integer pageSize = Constant.PAGE_SIZE;

            //如果输入了验收单号,那么每页就默认显示999条
            if (!"".equals(checkCode)) {
                pageSize = 999;
            }
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOrderChkList(po, originType, chkId, state,isPrint, beginTime, endTime, curPage, pageSize);

            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单验收");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-10
     * @Title :
     * @Description: 验收单打印 初始化
     */
    public void purchaseOrderChkPrintInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PurchasePlanSettingDao dao1 = PurchasePlanSettingDao.getInstance();
        try {
            List list = dao.getPartWareHouseList(logonUser);//获取配件库房信息
            List<Map<String, Object>> planerList = dao1.getUserPoseLise(1, null);
            List whmans = dao.queryWhmanInfo();
            act.setOutData("whmans", whmans);
            request.setAttribute("planerList", planerList);
            act.setOutData("wareHouses", list);
            act.setOutData("userId", logonUser.getUserId());
            act.setForword(PART_PURORDERCHK_PRINT_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单验收");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 验收采购订单
     */
    @SuppressWarnings("unchecked")
    public void checkPurchaseUpdateOrderState() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String[] poIds = request.getParamValues("ids");//获取采购订单id
            String remark2 = CommonUtils.checkNull(request.getParamValue("REMARK2"));//入库确认备注
            //String planerRemark = CommonUtils.checkNull(request.getParamValue("PLANER_REMARK"));//入库确认计划员备注
            String chkId = CommonUtils.checkNull(request.getParamValue("chkId"));
            String flag = request.getParamValue("flag");
            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            //更新备注
            TtPartOemPoPO ttPartOemPoPO1 = new TtPartOemPoPO();
            TtPartOemPoPO ttPartOemPoPO2 = new TtPartOemPoPO();
            ttPartOemPoPO1.setChkId(CommonUtils.parseLong(chkId));
            if (!"".equals(remark2)) {
                ttPartOemPoPO2.setRemark2(remark2);
                dao.update(ttPartOemPoPO1, ttPartOemPoPO2);
            }
            /*if(!"".equals(planerRemark)){
                ttPartOemPoPO2.setPlanerRemark(planerRemark);
            }
            if(!"".equals(remark2)||!"".equals(planerRemark)){
            	dao.update(ttPartOemPoPO1, ttPartOemPoPO2);
            }*/

            for (int i = 0; i < poIds.length; i++) {
                if (flag.equals("1")) {  //入库确认完成
                    TtPartOemPoPO partOemPoPO = new TtPartOemPoPO();
                    partOemPoPO.setPoId(Long.valueOf(poIds[i]));

                    TtPartOemPoPO partOemPoPO1 = new TtPartOemPoPO();
                    partOemPoPO1.setState(Constant.PART_PURCHASE_ORDERCHK_STATUS_03);//完成

                    dao.update(partOemPoPO, partOemPoPO1);
                } else {//入库确认关闭
                    TtPartOemPoPO oemPoPO = new TtPartOemPoPO();
                    oemPoPO.setPoId(Long.valueOf(poIds[i]));

                    oemPoPO = (TtPartOemPoPO) dao.select(oemPoPO).get(0);

                    //如果待入库数量等于验货数量,那么关闭的时候就要删除验收表中数据,否则就只更新状态
                    if (oemPoPO.getSpareinQty().longValue() == oemPoPO.getCheckQty().longValue()) {
                        TtPartPoChkPO chkPO = new TtPartPoChkPO();
                        chkPO.setPoId(CommonUtils.parseLong(poIds[i]));
                        dao.delete(chkPO);
                    } else {
                        //更新验收表中数据状态为已关闭
                        ArrayList al = new ArrayList();
                        al.add(0, poIds[i]);
                        dao.update("UPDATE TT_PART_PO_CHK CHK SET CHK.STATE=" + Constant.PART_PURCHASE_ORDERCIN_STATUS_02 + "WHERE CHK.STATE!=" + Constant.PART_PURCHASE_ORDERCIN_STATUS_02 + " AND CHK.PO_ID=?", al);
                    }

                    //部分完成更新采购订单表中状态为已完成
                    TtPartOemPoPO po = new TtPartOemPoPO();
                    po.setPoId(Long.valueOf(poIds[i]));
                    po.setState(Constant.PART_PURCHASE_ORDERCHK_STATUS_01);

                    TtPartOemPoPO po1 = new TtPartOemPoPO();
                    ////modify by yuan 20131011
                    //po1.setState(Constant.PART_PURCHASE_ORDERCHK_STATUS_02);
                    po1.setState(Constant.PART_PURCHASE_ORDERCHK_STATUS_03);
                    //end
                    dao.update(po, po1);

                    //完全关闭时删除对应验收记录防止验收确任时打印出来
                    TtPartOemPoPO spdel = new TtPartOemPoPO();
                    spdel.setPoId(Long.valueOf(poIds[i]));
                    ////modify by yuan 20131011
                    //spdel.setState(Constant.PART_PURCHASE_ORDERCHK_STATUS_02);
                    spdel.setState(Constant.PART_PURCHASE_ORDERCHK_STATUS_03);
                    //end

                    if(((TtPartOemPoPO)dao.select(spdel).get(0)).getInQty()==0){
                        dao.delete(spdel);
                    }

                    //关闭后要更新总部采购订单主表和明细表数据
                    TtPartPoMainPO poMainPO1 = new TtPartPoMainPO();
                    TtPartPoMainPO poMainPO2 = new TtPartPoMainPO();
                    poMainPO1.setOrderId(oemPoPO.getOrderId());
                    poMainPO2.setState(Constant.PURCHASE_ORDER_STATE_01);//未完成
                    dao.update(poMainPO1,poMainPO2);

                    ArrayList list = new ArrayList();
                    list.add(0, oemPoPO.getSpareinQty());
                    list.add(1, oemPoPO.getSpareinQty());
                    list.add(2, oemPoPO.getOdlineId());
                    dao.update("UPDATE TT_PART_PO_DTL OP SET OP.CHECK_QTY=OP.CHECK_QTY-(?),OP.SPARE_QTY=OP.SPARE_QTY+(?) WHERE OP.POLINE_ID=?", list);

                }
            }

            TtPartOemPoPO oemPoPO = new TtPartOemPoPO();
            oemPoPO.setChkId(Long.valueOf(chkId));
            oemPoPO.setState(Constant.PART_PURCHASE_ORDERCHK_STATUS_01);
            if(dao.select(oemPoPO).size() == 0 ){
                act.setOutData("toF", 1);
            }
            act.setOutData("success", "操作成功！");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            POContext.endTxn(false);//回滚事务
            POContext.cleanTxn();
            BizException e1 = new BizException(act,
                    ErrorCodeConstant.SPECIAL_MEG, "入库确认失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 验收采购订单
     */
    public void checkPurchaseOrder() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String[] poIds = request.getParamValues("ids");//获取采购订单id
            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            String errors = "";//错误信息
            String success = "";//成功信息
            for (int i = 0; i < poIds.length; i++) {
                POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
                String checkDate = request.getParamValue("CHECK_DATE" + poIds[i]);//获取验货日期
                String relQty = request.getParamValue("REL_QTY" + poIds[i]);//实际验货量
                String remark = request.getParamValue("REMARK" + poIds[i]);//备注
                String makerId = request.getParamValue("MAKER_ID" + poIds[i]);//制造商id

                TtPartMakerDefinePO makerDefinePO = new TtPartMakerDefinePO();
                makerDefinePO.setMakerId(CommonUtils.parseLong(makerId));
                makerDefinePO = (TtPartMakerDefinePO) dao.select(makerDefinePO).get(0);

                //查询总部采购订单（交接单）
                TtPartOemPoPO po = new TtPartOemPoPO();
                po.setPoId(CommonUtils.parseLong(poIds[i]));
                po = (TtPartOemPoPO) dao.select(po).get(0);
                po.setMakerId(makerDefinePO.getMakerId());

                //获取当前版本信息
                int ver = po.getVer();

                //防止重复验收
                if (po.getSpareQty().longValue() == 0) {//如果已经验收完毕,重复验收就提示错误信息

                    errors += "采购订单【" + po.getOrderCode() + "】中配件【" + po.getPartCname() + "】已经验收完成,请选择其他采购订单进行验收!<br>";
                    POContext.endTxn(false);//回滚事务
                    POContext.cleanTxn();

                } else if ((po.getSpareQty().longValue()) < CommonUtils.parseLong(relQty).longValue()) {
                    errors += "采购订单【" + po.getOrderCode() + "】中配件【" + po.getPartCname() + "】的验货数量不能大于待验货数量,请刷新页面重试!<br>";
                    POContext.endTxn(false);//回滚事务
                    POContext.cleanTxn();
                } else {

                    po.setCheckQty(po.getCheckQty() + CommonUtils.parseLong(relQty));//设置已验收数量
                    po.setSpareQty((po.getSpareQty() - CommonUtils.parseLong(relQty)));//设置待验收数量
                    po.setSpareinQty(po.getSpareinQty() + CommonUtils.parseLong(relQty));//设置待入库数量
                    po.setRemark(remark);

                    if (po.getSpareQty().longValue() == 0) {//如果该订单的数量已经全部验收完毕,那么该订单的状态就变为验收完成
                        po.setState(Constant.PART_PURCHASE_ORDERCHK_STATUS_03);
                    }
                    //同时需要将每次验收的信息保存到采购验收明细表中,在验收的时候要查询当前验收的配件是否已经存在于验收明细表中(同一个验收单号下的)
                    //如果存在就更新，否则新增

                    String checkCode = request.getParamValue("CHK_CODE" + poIds[i]);//验收单号

                    Map<String, Object> cmap = dao.getChkInfoByPartIdAndChkCode(po.getPartId(), checkCode);

                    TtPartPoChkPO chkPO = new TtPartPoChkPO();

                    if (cmap != null) {
                        TtPartPoChkPO poChkPO1 = new TtPartPoChkPO();
                        TtPartPoChkPO poChkPO2 = new TtPartPoChkPO();
                        poChkPO1.setCheckId(((BigDecimal) cmap.get("CHECK_ID")).longValue());
                        poChkPO2.setCheckQty(((BigDecimal) cmap.get("CHECK_QTY")).longValue() + CommonUtils.parseLong(relQty));
                        poChkPO2.setSpareQty(((BigDecimal) cmap.get("SPARE_QTY")).longValue() + CommonUtils.parseLong(relQty));
                        poChkPO2.setState(Constant.PART_PURCHASE_ORDERCIN_STATUS_01);
                        dao.update(poChkPO1, poChkPO2);
                    } else {

                        if (checkDate == null || "".equals(checkDate)) {//如果验货日期为空,那么验货日期就默认是当前日期
                            chkPO.setCheckDate(new Date());
                        } else {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            chkPO.setCheckDate(format.parse(checkDate));
                        }
                        chkPO.setCheckId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                        chkPO.setCheckCode(checkCode);
                        chkPO.setPoId(po.getPoId());
                        chkPO.setOrderCode(po.getOrderCode());
                        chkPO.setPlanCode(po.getPlanCode());
                        chkPO.setPartId(po.getPartId());
                        chkPO.setPartCode(po.getPartCode());
                        chkPO.setPartOldcode(po.getPartOldcode());
                        chkPO.setPartCname(po.getPartCname());
                        chkPO.setUnit(po.getUnit());
                        chkPO.setVenderId(po.getVenderId());
                        chkPO.setVenderCode(po.getVenderCode());
                        chkPO.setVenderName(po.getVenderName());
                        chkPO.setBuyPrice(po.getBuyPrice());
                        chkPO.setBuyQty(po.getBuyQty());
                        chkPO.setCheckQty(CommonUtils.parseLong(relQty));
                        chkPO.setSpareQty(CommonUtils.parseLong(relQty));//待入库数量默认等于验收数量 modify by yuan 20130513
                        chkPO.setWhId(po.getWhId());
                        chkPO.setWhName(po.getWhName());
                        chkPO.setCreateDate(new Date());
                        chkPO.setCreateBy(logonUser.getUserId());
                        chkPO.setOrgId(po.getOrgId());
                        chkPO.setCheckBy(logonUser.getUserId());
                        chkPO.setPartType(po.getPartType());
                        chkPO.setState(Constant.PART_PURCHASE_ORDERCIN_STATUS_01);//一旦点击验收,验收表中的单据状态变为入库中
                        chkPO.setMakerId(po.getMakerId());

                    }


                    dao.update(po);//更新采购订单表,此时将会锁定该记录,直到事务提交才释放
                    po.setVer(ver + 1);//让版本号加1

                    //提交事务之前需要比较开始读取的版本号(ver)与此时版本号(po.getVer()),如果发现po.getVer()>ver就入库成功,否则失败并回滚数据
                    Map map = dao.getVerByPoId(po.getPoId());
                    int ver1 = ((BigDecimal) map.get("VER")).intValue();
                    if (po.getVer() > ver1) {
                        if (chkPO.getCheckId() != null) {
                            dao.insert(chkPO);//插入订单验收信息
                        }
                        dao.updateVer(po);//把当前最新版本号更新到数据库

                        POContext.endTxn(true);//提交事务
                        POContext.cleanTxn();
                        success = "验收成功!";
                    } else {
                        errors += "采购订单" + po.getOrderCode() + "正在验收,请稍后再试!<br>";
                        POContext.endTxn(false);//回滚事务
                        POContext.cleanTxn();
                    }
                }

            }

            act.setOutData("success", success);
            act.setOutData("error", errors);
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            POContext.endTxn(false);//回滚事务
            POContext.cleanTxn();
            BizException e1 = new BizException(act,
                    ErrorCodeConstant.SPECIAL_MEG, "采购订单验收失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-20
     * @Title :
     * @Description: 查询制造商信息
     */
    public void queryMakerInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String curMakerId = CommonUtils.checkNull(request.getParamValue("curMakerId"));
            String poId = CommonUtils.checkNull(request.getParamValue("poId"));
            String curVenderId = CommonUtils.checkNull(request.getParamValue("curVenderId"));
            String curPartId = CommonUtils.checkNull(request.getParamValue("curPartId"));
            List makers = dao.queryMakerInfo(CommonUtils.parseLong(curPartId));
            act.setOutData("makers", makers);
            act.setOutData("poId", poId);
            act.setOutData("curMakerId", curMakerId);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "制造商信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-20
     * @Title :
     * @Description: 查询库管员信息
     */
    public void queryWhmanInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String curwhmanId = CommonUtils.checkNull(request.getParamValue("curwhmanId"));
            String poId = CommonUtils.checkNull(request.getParamValue("poId"));
            List whmans = new ArrayList();
            whmans = dao.queryWhmanInfo();
            act.setOutData("whmans", whmans);
            act.setOutData("poId", poId);
            act.setOutData("curwhmanId", curwhmanId);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库管员");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title :
     * @Description: 关闭采购订单
     */
    public void closePo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);

        try {
            String poId = CommonUtils.checkNull(request
                    .getParamValue("poId")); // 采购订单Id
            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            //关闭之后需要把数据返回到生成验收指令处
            TtPartOemPoPO oemPoPO = new TtPartOemPoPO();
            oemPoPO.setPoId(CommonUtils.parseLong(poId));
            oemPoPO = (TtPartOemPoPO) dao.select(oemPoPO).get(0);

            TtPartPoMainPO poMainPO1 = new TtPartPoMainPO();
            TtPartPoMainPO poMainPO2 = new TtPartPoMainPO();
            poMainPO1.setOrderId(oemPoPO.getOrderId());
            poMainPO2.setState(Constant.PURCHASE_ORDER_STATE_01);

            TtPartPoDtlPO dtlPO = new TtPartPoDtlPO();
            dtlPO.setPlineId(oemPoPO.getPlineId());
            dtlPO.setOrderId(oemPoPO.getOrderId());
            dtlPO = (TtPartPoDtlPO) dao.select(dtlPO).get(0);

            TtPartPoDtlPO dtlPO1 = new TtPartPoDtlPO();
            TtPartPoDtlPO dtlPO2 = new TtPartPoDtlPO();
            dtlPO1.setPlineId(oemPoPO.getPlineId());
            dtlPO1.setOrderId(oemPoPO.getOrderId());
            dtlPO2.setSpareQty(dtlPO.getSpareQty() + oemPoPO.getSpareQty());
            dtlPO2.setCheckQty(dtlPO.getCheckQty() - oemPoPO.getSpareQty());

            //关闭验收单
            TtPartOemPoPO spo = new TtPartOemPoPO();
            spo.setPoId(CommonUtils.parseLong(poId));
            TtPartOemPoPO po = new TtPartOemPoPO();
            po.setSpareQty(0l);
            po.setState(Constant.PART_PURCHASE_ORDERCHK_STATUS_02);


            dao.update(poMainPO1, poMainPO2);
            dao.update(dtlPO1, dtlPO2);
            dao.update(spo, po);

            act.setOutData("success", "关闭成功!");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "采购订单关闭失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title :
     * @Description: 打开采购订单
     */
    public void openPo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);

        try {
            String poId = CommonUtils.checkNull(request
                    .getParamValue("poId")); // 采购订单Id
            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            TtPartOemPoPO spo = new TtPartOemPoPO();
            spo.setPoId(CommonUtils.parseLong(poId));
            TtPartOemPoPO po = new TtPartOemPoPO();
            po.setState(Constant.PART_PURCHASE_ORDERCHK_STATUS_01);

            dao.update(spo, po);
            act.setOutData("success", "打开成功!");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "采购订单打开失败,请联系管理员!");
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
    public void exportOrderChkExcel() {


        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        List wareHouses = new ArrayList();
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[16];
            head[0] = "采购订单号";
            head[1] = "验收单号";
            head[2] = "采购员";
            head[3] = "配件类型";
            head[4] = "配件件号";
            head[5] = "配件编码";
            head[6] = "配件名称";
            head[7] = "订购数量";
            head[8] = "已验货数量";
            head[9] = "已入库数量";
            head[10] = "供应商名称";
            head[11] = "制造商名称";
            head[12] = "库房";
            head[13] = "预计到货日期";
            head[14] = "状态";
            List<Map<String, Object>> list = dao.queryPurchaseOrderChk(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[16];
                        detail[0] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                        detail[1] = CommonUtils.checkNull(map.get("CHK_CODE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("BUYER"));
                        detail[3] = CommonUtils
                                .checkNull(map.get("PART_TYPE"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("PART_CODE"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("PART_OLDCODE"));
                        detail[6] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[7] = CommonUtils.checkNull(map.get("BUY_QTY"));
                        detail[8] = CommonUtils.checkNull(map.get("CHECK_QTY"));
                        detail[9] = CommonUtils.checkNull(map.get("IN_QTY"));
                        detail[10] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                        detail[11] = CommonUtils.checkNull(map.get("MAKER_NAME"));
                        detail[12] = CommonUtils.checkNull(map.get("WH_NAME"));
                        detail[13] = CommonUtils.checkNull(map.get("FORECAST_DATE"));
                        detail[14] = CommonUtils.checkNull(map.get("STATE"));
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
            act.setForword(PART_PURCHASEORDERCHK_QUERY_URL);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-1
     * @Title :
     * @Description: 采购入库-打印
     */
    public void opPrintHtml() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        Map<String, Object> dataMap = new HashMap();
        try {

            String chkId = CommonUtils.checkNull(request.getParamValue("chkId"));
            String remark = CommonUtils.checkNull(request.getParamValue("remark"));
            Map map = new HashMap();
            request.setAttribute("po", map);

            List allList = new ArrayList();

            ArrayList al = new ArrayList();
            al.add(0, chkId);
            dao.update("UPDATE tt_part_oem_po t SET t.print_times2=nvl(t.print_times2,0)+1,t.print_date2=sysdate WHERE t.chk_Id = ?", al);

            List<Map<String, Object>> detailList = dao.queryOrderInfo(chkId);

            for (int i = 0; i < detailList.size(); ) {
                List subList = detailList.subList(i, i + PRINT_SIZE > detailList.size() ? detailList.size() : i + PRINT_SIZE);
                i = i + PRINT_SIZE;
                allList.add(subList);
            }

            Map<String, Object> map1 = new HashMap<String, Object>();
            if (detailList.size() > 0) {
                map1 = detailList.get(0);
            }
            map.put("orderCode", map1.get("ORDER_CODE"));
            map.put("chkCode", map1.get("CHK_CODE"));
            map.put("whName", map1.get("WH_NAME"));
            map.put("venderName", map1.get("VENDER_NAME"));
            map.put("rate", map1.get("ORATE"));
            map.put("name", map1.get("NAME"));
            map.put("makerName", map1.get("MAKER_NAME"));
            map.put("date", map1.get("CREATE_DATE"));
            map.put("curDate", DateUtil.getDateStr(new Date(), 4));

            int allQty = 0;
            double amount = 0;
            double famount = 0;
            for (Map<String, Object> m : detailList) {
                allQty += ((BigDecimal) m.get("GENERATE_QTY")).intValue();
                amount = Arith.add(amount, ((BigDecimal) m.get("AMOUNT")).doubleValue());
            }

            BigDecimal b = new BigDecimal(amount);
            famount = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            DecimalFormat df = new DecimalFormat("#.00");
            String samonut = df.format(famount);
            String ramount = df.format(Arith.div(amount, (1 - Constant.PART_TAX_RATE), 2));
            map.put("allQty", allQty);
            map.put("amount", samonut);
            map.put("ramount", ramount);
            map.put("remark", remark);
            dataMap.put("mainMap", map);
            dataMap.put("detailList", detailList);
            act.setOutData("allList", allList);
            if(allList.size()==0){
                act.setOutData("listAcount", allList.size()+1);
                }else{
                 act.setOutData("listAcount", allList.size());
                }
            act.setOutData("dataMap", dataMap);
            //act.setForword(PART_PURCHASEORDERCHK_PRINT_URL);
            act.setForword(PART_PURORDERCHK_PRINT_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "进货单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-1
     * @Title :
     * @Description: 打印明细
     */
    public void opDtlPrintHtml() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        Map<String, Object> dataMap = new HashMap();
        try {

            String poIdStr = CommonUtils.checkNull(request.getParamValue("ids"));
            String[] poIds = poIdStr.split(",");
            Map map = new HashMap();

            List allList = new ArrayList();

            List<Map<String, Object>> detailList = dao.queryOrderDtlInfo(poIds);

            for (int i = 0; i < detailList.size(); ) {
                List subList = detailList.subList(i, i + PRINT_SIZE > detailList.size() ? detailList.size() : i + PRINT_SIZE);
                i = i + PRINT_SIZE;
                allList.add(subList);
            }

            Map<String, Object> map1 = new HashMap<String, Object>();
            if (detailList.size() > 0) {
                map1 = detailList.get(0);
            }
            map.put("rate", map1.get("ORATE"));
            map.put("name", map1.get("NAME"));
            map.put("curDate", DateUtil.getDateStr(new Date(), 4));

            int allQty = 0;
            double amount = 0;
            double famount = 0;
            for (Map<String, Object> m : detailList) {
                allQty += ((BigDecimal) m.get("GENERATE_QTY")).intValue();
                amount = Arith.add(amount, ((BigDecimal) m.get("AMOUNT")).doubleValue());
            }

            BigDecimal b = new BigDecimal(amount);
            famount = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            DecimalFormat df = new DecimalFormat("#.00");
            String samonut = df.format(famount);
            String ramount = df.format(Arith.div(amount, (1 - Constant.PART_TAX_RATE), 2));
            map.put("allQty", allQty);
            map.put("amount", samonut);
            map.put("ramount", ramount);
            dataMap.put("mainMap", map);
            dataMap.put("detailList", detailList);
            act.setOutData("allList", allList);
            act.setOutData("dataMap", dataMap);
            act.setForword(PART_PURORDERDTL_PRINT_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "进货单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-1
     * @Title :
     * @Description: 打印入库确认明细
     */
    public void printConfirmDtl() {
    	ActionContext act = ActionContext.getContext();
    	AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
    	RequestWrapper request = act.getRequest();
    	Map<String, Object> dataMap = new HashMap();
    	try {
    		
    		String poIdStr = CommonUtils.checkNull(request.getParamValue("ids"));
    		String[] poIds = poIdStr.split(",");
    		Map map = new HashMap();
    		
    		for(int i=0;i<poIds.length;i++){
    			ArrayList al = new ArrayList();
                al.add(0, poIds[i]);
                dao.update("UPDATE TT_PART_OEM_PO T SET T.PRINT_TIMES2=NVL(T.PRINT_TIMES2,0)+1,T.PRINT_DATE2=SYSDATE WHERE T.PO_ID = ?", al);
    		}
    		
    		List allList = new ArrayList();
    		
    		List<Map<String, Object>> detailList = dao.queryOrderDtlInfo(poIds);
    		
    		for (int i = 0; i < detailList.size(); ) {
    			List subList = detailList.subList(i, i + PRINT_SIZE > detailList.size() ? detailList.size() : i + PRINT_SIZE);
    			i = i + PRINT_SIZE;
    			allList.add(subList);
    		}
    		
    		Map<String, Object> map1 = new HashMap<String, Object>();
    		if (detailList.size() > 0) {
    			map1 = detailList.get(0);
    		}
    		map.put("rate", map1.get("ORATE"));
    		map.put("name", map1.get("NAME"));
    		map.put("curDate", DateUtil.getDateStr(new Date(), 4));
    		
    		int allQty = 0;
    		double amount = 0;
    		double famount = 0;
    		for (Map<String, Object> m : detailList) {
    			allQty += ((BigDecimal) m.get("GENERATE_QTY")).intValue();
    			amount = Arith.add(amount, ((BigDecimal) m.get("AMOUNT")).doubleValue());
    		}
    		
    		BigDecimal b = new BigDecimal(amount);
    		famount = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    		
    		DecimalFormat df = new DecimalFormat("#.00");
    		String samonut = df.format(famount);
    		String ramount = df.format(Arith.div(amount, (1 - Constant.PART_TAX_RATE), 2));
    		map.put("allQty", allQty);
    		map.put("amount", samonut);
    		map.put("ramount", ramount);
    		dataMap.put("mainMap", map);
    		dataMap.put("detailList", detailList);
    		act.setOutData("allList", allList);
    		act.setOutData("dataMap", dataMap);
    		act.setForword(PART_PURORDERDTL_PRINT_URL);
    	} catch (Exception e) {//异常方法
    		BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "进货单打印错误,请联系管理员!");
    		logger.error(loginUser, e1);
    		act.setException(e1);
    	}
    }

    /**
     * *
     * 按验收单批量打印
     */
    public void opPrintListHtml() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        Map<String, Object> dataMap = new HashMap();
        try {

            String chkIds = CommonUtils.checkNull(request.getParamValue("chkIds"));
            Map map = new HashMap();
            request.setAttribute("po", map);

            for (int i = 0; i < chkIds.split(",").length; i++) {
                ArrayList al = new ArrayList();
                al.add(0, chkIds.split(",")[i]);
                dao.update("UPDATE tt_part_oem_po t SET t.print_times=nvl(t.print_times,0)+1,t.print_date=sysdate WHERE t.chk_Id in ( ? )", al);
            }

            List<Map<String, Object>> detailList = dao.queryOrderListInfo(chkIds.substring(0, chkIds.length() - 1));
            Map<String, Object> map1 = new HashMap<String, Object>();
            if (detailList.size() > 0) {
                map1 = detailList.get(0);
            }

            map.put("whName", map1.get("WH_NAME"));
            map.put("createBy", loginUser.getName());
            map.put("curDate", DateUtil.getDateStr(new Date(), 4));

            dataMap.put("mainMap", map);
            dataMap.put("detailList", detailList);
            act.setOutData("dataMap", dataMap);
            act.setForword(PART_PURORDERCHK_PRINT_URL2);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "验收单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * *
     * 按配件批量打印明细
     */
    public void opPrintChkHtml() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        Map<String, Object> dataMap = new HashMap();
        try {

            String poIdStr = CommonUtils.checkNull(request.getParamValue("poIds"));
            String[] poIds = poIdStr.split(",");
            Map map = new HashMap();
            request.setAttribute("po", map);

            for (int i = 0; i < poIds.length; i++) {
                ArrayList al = new ArrayList();
                al.add(0, poIds[i]);
                dao.update("UPDATE tt_part_oem_po t SET t.print_times=nvl(t.print_times,0)+1,t.print_date=sysdate WHERE t.PO_ID =?", al);
            }

            List<Map<String, Object>> detailList = dao.queryOrderListInfo1(poIds);
            Map<String, Object> map1 = new HashMap<String, Object>();
            if (detailList.size() > 0) {
                map1 = detailList.get(0);
            }

            map.put("whName", map1.get("WH_NAME"));
            map.put("createBy", loginUser.getName());
            map.put("curDate", DateUtil.getDateStr(new Date(), 4));

            dataMap.put("mainMap", map);
            dataMap.put("detailList", detailList);
            act.setOutData("dataMap", dataMap);
            act.setForword(PART_PURORDERCHK_PRINT_URL2);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "验收单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-5
     * @Title :
     * @Description: 查询订单详细信息
     */
    public void orderInStockHandle() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String chkId = CommonUtils.checkNull(request.getParamValue("chkId")); // 订单Id
           /* Map<String, Object> mainInfo = dao.getOrderMain(orderId);*/
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            List list = dao.getPartWareHouseList(logonUser);//获取配件库房信息
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("curPage", curPage);
            act.setOutData("chkId", chkId);
            act.setOutData("wareHouses", list);
            act.setForword(PART_PURCHASEORDERCHK_QUERY_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 采购入库--查询
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-10
     * @Title :
     * @Description:
     */
    public void queryOrderChkInstockInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOrderChkInstockList(request, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "验收单打印");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-10
     * @Title :
     * @Description:汇总查询验收信息
     */
    public void queryOrderChkAllInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String checkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));//验收单号
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//验收开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//验收结束时间
            String pBeginTime = CommonUtils.checkNull(request.getParamValue("pBeginTime"));//打印开始时间
            String pEndTime = CommonUtils.checkNull(request.getParamValue("pEndTime"));//打印结束时间
            String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_ID"));//计划员
            String whmanId = CommonUtils.checkNull(request.getParamValue("WHMAN_ID"));//保管员
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOrderChkAllList(checkCode, venderId, beginTime, endTime,
                    pBeginTime, pEndTime, planerId, whmanId, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "验收单打印");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-10
     * @Title :
     * @Description:查询验收信息
     */
    public void queryOrderChkInfo1() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOrderChkList1(request, curPage, Constant.PAGE_SIZE_PART_MINI);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "验收单打印");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-10
     * @Title :
     * @Description: 打印验收单
     */
    public void chkOrderPrintHtml() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        Map<String, Object> dataMap = new HashMap();
        try {

            String chkCode = CommonUtils.checkNull(request.getParamValue("chkCode"));
            Map<String, Object> map = dao.getChkOrderMain(chkCode);

            List<Map<String, Object>> detailList = dao.queryChkOrderDetailList(chkCode);

            act.setOutData("mainMap", map);
            act.setOutData("detailList", detailList);
            act.setForword(PART_PURORDERCHK_PRINT_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "验收单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-10
     * @Title :
     * @Description: 更新打印时间
     */
    public void updatePrintDate() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String chkCode = CommonUtils.checkNull(request
                    .getParamValue("chkCode"));

            TtPartOemPoPO spo = new TtPartOemPoPO();
            TtPartOemPoPO po = new TtPartOemPoPO();
            spo.setChkCode(chkCode);
            po.setPrintDate(new Date());

            dao.update(spo, po);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "验收单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-20
     * @Title :
     * @Description: 查询验收单明细
     */
    public void queryChkOrderDetailInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String chkId = CommonUtils.checkNull(request.getParamValue("chkId"));
            act.setOutData("chkId", chkId);
            act.setForword(PART_PURCHASEORDERVIEW_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "验收单信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-20
     * @Title :
     * @Description: 采购入库查询-入库明细查询
     */
    public void orderInStockView() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String chkId = CommonUtils.checkNull(request.getParamValue("chkId"));
            act.setOutData("chkId", chkId);
            act.setForword(PART_PURCHASEORDERINSTOCK_VIEW_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "入库确认明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-20
     * @Title :
     * @Description: 查询验收单明细
     */
    public void queryChkOrderDetail() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String chkId = CommonUtils.checkNull(request.getParamValue("chkId"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPurOrderDtlList(chkId, partOldCode, partCname,
                    curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "验收单信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 
     * @Title      : 
     * @Description: 进货明细 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-9-21
     */
    public void queryOrderChkDtl(){
    	 ActionContext act = ActionContext.getContext();
         RequestWrapper request = act.getRequest();
         AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
         try {
        	 
        	 Map<String,Object> map = dao.queryMainInfo(request);
 			 act.setOutData("chkCount",map==null?0:((BigDecimal)map.get("CHK_COUNT")).longValue());
 			 act.setOutData("chkNum",map==null?0:((BigDecimal)map.get("CHK_NUM")).longValue());
 			 act.setOutData("chkAmount",map==null?0:(String)map.get("CHK_AMOUNT"));
             //分页方法 begin
             Integer curPage = request.getParamValue("curPage") != null ? Integer
                     .parseInt(request.getParamValue("curPage"))
                     : 1; // 处理当前页
             PageResult<Map<String, Object>> ps = dao.queryPurOrderDtlList(request,
                     curPage, Constant.PAGE_SIZE);
             //分页方法 end
             act.setOutData("ps", ps);
         } catch (Exception e) {//异常方法
             BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "进货明细");
             logger.error(logonUser, e1);
             act.setException(e1);
         }
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-20
     * @Title :
     * @Description: 明细打印
     */
    public void purchaseOrderDtlPrintInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PART_PURORDERDTL_PRINT_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单验收");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}
