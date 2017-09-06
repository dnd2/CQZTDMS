package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.partPlanQuery.PartPlanQueryDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.salesManager.*;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 配件出库
 * @author 
 * @version 2017-8-3
 * @see 
 * @since 
 * @deprecated
 */
public class PartOutstock extends BaseImport implements PTConstants {
	
    public Logger logger = Logger.getLogger(PartOutstock.class);
    PartOutstockDao dao = PartOutstockDao.getInstance();
	
	
    private static final String PKG_NO_SELECT_URL = "/jsp/parts/salesManager/carFactorySalesManager/pkgNoSelect.jsp";
    private static final String PART_LOC_BO_QUERY = "/jsp/parts/salesManager/carFactorySalesManager/partLocBoQuery.jsp";

    String partDetailUrl = "/jsp/parts/salesManager/carFactorySalesManager/pickOutOrderDetail.jsp";

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:导出
     */
    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "配件出库列表.xls";
        jxl.write.WritableWorkbook wwb = null;
        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8"));
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
                    ws.addCell(new Label(i, z, str[i]));
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
     * 出库单初始化
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartDlrOrderDao
     * @Description: 
     */
    public void partOutstockInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orgId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(orgId);
           
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            request.setAttribute("wareHouseList", wareHouseList);
            
            act.setOutData("transList", list);
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(PART_OUTSTOCK_ORDER_MAIN);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "出库单初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    
    
    public void pkgNoSelect() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartOutstockDao dao = PartOutstockDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));
            //未打印
            List<TtPartPkgBoxDtlPO> list = dao.queryPkgNo(pickOrderId, "0", Constant.CAR_FACTORY_PKG_STATE_05 + ""); //已随车箱号
            List<TtPartPkgBoxDtlPO> list1 = dao.queryPkgNo(pickOrderId, "0", Constant.CAR_FACTORY_PKG_STATE_02 + ""); //未随车箱号
            act.setOutData("list", list);
            act.setOutData("list1", list1);
            act.setOutData("flag", flag);//如果为1就是在打印装箱单时弹出的页面
            act.setForword(PKG_NO_SELECT_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "出库单初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void pkgNoSelect2() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartOutstockDao dao = PartOutstockDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));
            //已打印
            List<TtPartPkgBoxDtlPO> list = dao.queryPkgNo(pickOrderId, "1", Constant.CAR_FACTORY_PKG_STATE_05 + "");
            List<TtPartPkgBoxDtlPO> list1 = dao.queryPkgNo(pickOrderId, "1", Constant.CAR_FACTORY_PKG_STATE_02 + "");
            act.setOutData("list", list);
            act.setOutData("list1", list1);
            act.setOutData("flag", flag);//如果为1就是在打印装箱单时弹出的页面
            act.setForword(PKG_NO_SELECT_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "出库单初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void amount() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartOutstockDao dao = PartOutstockDao.getInstance();
            Map<String, Object> map = dao.queryAmount(request, loginUser);
            //System.out.println(map.get("DCK_AMOUNT"));
            //System.out.println(map.get("YCK_AMOUNT"));
            act.setOutData("DCK_AMOUNT", map.get("DCK_AMOUNT"));
            act.setOutData("YCK_AMOUNT", map.get("YCK_AMOUNT"));

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "出库单错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件出库查询
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 
     */
    public void partOutstockQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartOutstockDao dao = PartOutstockDao.getInstance();
            PartPkgDao partPkgDao = PartPkgDao.getInstance();
            PartPickOrderDao partPickOrderDao = PartPickOrderDao.getInstance();
            //分页方法 beginfin
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            
            PageResult<Map<String, Object>> ps = dao.queryPkgOrder(request, curPage, Constant.PAGE_SIZE_PART_MINI);
            
            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_OUTSTOCK_ORDER_MAIN);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件出库单查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_OUTSTOCK_ORDER_MAIN);
        }
    }

    public void queryPartLocBo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartOutstockDao dao = PartOutstockDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryPartLocBo(request, curPage, Constant.PAGE_SIZE_MIDDLE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "现场BO查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件出库-出库信息查询
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 
     */
    public void partOutstock() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPickOrderDao dao = PartPickOrderDao.getInstance();
        PartPkgDao partPkgDao = PartPkgDao.getInstance();
        String msg = "配件出库初始化错误,请联系管理员!";
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String pkgNos = CommonUtils.checkNull(request.getParamValue("pkgNos"));
            String trplanId = CommonUtils.checkNull(request.getParamValue("trplanId"));

            TtPartTransPlanPO partTransPlanPO = new TtPartTransPlanPO();
            partTransPlanPO.setTrplanId(Long.valueOf(trplanId));

            partTransPlanPO = (TtPartTransPlanPO) dao.select(partTransPlanPO).get(0);
            //出库单重复性判断
            if (!"0".equals(partTransPlanPO.getOutId().toString())) {
                msg = "已出库，不允许重复出库,页面将自动刷新!";
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "不能重复出库!");
                throw e1;
            }

            List<Map<String, Object>> soOrderList = dao.getSoMainList(pickOrderId);
            Map<String, Object> mainMap = soOrderList.get(0);

            String whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
            String whName = partPkgDao.getWhName(whId);
            // List<Map<String, Object>> list = dao.getPartGroup(pickOrderId, whId,pkgNos);
            
            List<Map<String, Object>> list = dao.getPartGroupInfo(pickOrderId,whId, trplanId);
            
            List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();

            mainMap.put("whName", whName);
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                int pkgedQty = Integer.valueOf(list.get(i).get("PKGEDQTY") == null ? "0" : CommonUtils.checkNull(list.get(i).get("PKGEDQTY")));
                int salesQty = Integer.valueOf(list.get(i).get("SALES_QTY") == null ? "0" : CommonUtils.checkNull(list.get(i).get("SALES_QTY")));
                map.put("boQty", (salesQty - pkgedQty));
                detailList.add(map);
            }

            Map<String, Object> transMap = this.getTransMap();
            String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
            transType = CommonUtils.checkNull(transMap.get(transType));
            //String boxNo = dao.getBoxQty(pickOrderId);
            mainMap.put("TRANS_TYPE", transType);
            mainMap.put("boxAllNo", pkgNos.split(",").length);
            mainMap.put("logUserName", loginUser.getName());
            mainMap.put("nowDate", CommonUtils.getDate());

            act.setOutData("mainMap", mainMap);
            act.setOutData("pickOrderId", pickOrderId);
            act.setOutData("detailList", detailList);
            act.setOutData("pkgNos", pkgNos);
            act.setOutData("trplanId", trplanId);//发运计划单id
            act.setOutData("estimatedAmount", CommonUtils.formatPrice(partTransPlanPO.getTransSumprice()));//生成发运单是预估的运费
            act.setForword(PART_OUTSTOCK_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, msg);
            logger.error(loginUser, e1);
            act.setException(e1);
            this.partOutstockInit();
        }
    }

    private List<Map<String, Object>> getDtlList(Map<String, Object> mainMap) {
        String soId = CommonUtils.checkNull(mainMap.get("SO_ID"));
        String whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
        PartSoManageDao dao = PartSoManageDao.getInstance();
        PartPkgDao partPkgDao = PartPkgDao.getInstance();
        PartOutstockDao partOutstockDao = PartOutstockDao.getInstance();
        List<Map<String, Object>> detailList = dao.getSoOrderMDetail(soId);
        for (Map<String, Object> map : detailList) {
            String partId = CommonUtils.checkNull(map.get("PART_ID"));
            //获取出库货位
            Map<String, Object> locMap = partPkgDao.queryPartLocationByPartId(partId, whId);
            String locName = CommonUtils.checkNull(locMap.get("LOC_NAME"));
            String locId = CommonUtils.checkNull(locMap.get("LOC_ID"));
            map.put("locName", locName);
            map.put("locId", locId);
            //获取装箱数据
            List<Map<String, Object>> pkgList = partPkgDao.queryPkg(soId, partId);
            String pkgNo = "";
            if (pkgList.size() <= 0) {
                pkgNo = "0";
                map.put("pkgedNo", pkgNo);
            } else {
                pkgNo = CommonUtils.checkNull(pkgList.get(0).get("PKG_QTY")) == null ? "0" : CommonUtils.checkNull(pkgList.get(0).get("PKG_QTY"));
                map.put("pkgedNo", pkgNo);
            }
            String saleQty = CommonUtils.checkNull(map.get("SALES_QTY"));
            int boQty = Integer.valueOf(saleQty) - Integer.valueOf(pkgNo);
            map.put("boQty", boQty);

            //获取箱数
            String pkgedQty = partOutstockDao.getPkgQty(soId, partId);
            map.put("pkgedQty", pkgedQty);
        }
        return detailList;
    }

    /**
     * 配件出库-生成出库单
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : 出库单
     * @Description: 
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void createPartOutstockOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String forwardFlag = CommonUtils.checkNull(request.getParamValue("forwardFlag"));
            //发运计划id
            String[] ids = request.getParamValues("ids");
            //需出库的配件信息
            String[] idArr = null;
            if (ids != null && ids.length > 0) {//如果不为空，说明是批量出库
                idArr = new String[ids.length];
                for (int i = 0, len = ids.length; i < len; i++) {
                    idArr[i] = ids[i];
                }
            } else {//单个出库
                String trplanId = CommonUtils.checkNull(request.getParamValue("trplanId"));
                idArr = new String[1];
                idArr[0] = trplanId;
            }

            //循环插入出库信息（出库单明细、发运单明细、出入库记录）
            for (int i = 0, len = idArr.length; i < len; i++) {
                String trplanId = idArr[i];
                String id1 = CommonUtils.checkNull(request.getParamValue("pickOrderId_" + trplanId));
                String id2 = CommonUtils.checkNull(request.getParamValue("pickOrderId"));

                String pickOrderId = id1.equals("") ? id2 : id1;
                if (dao.isOutStock(trplanId)) {
                    BizException be = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "发运单[" + trplanId + "】已经出库,不能重复出库,请返回重试!");
                    throw be;
                }
                //插入出库单明细和发运单明细,并插入出入库记录
                insertOutStockDtl(pickOrderId, trplanId);
                
                Long outId = Long.valueOf(request.getAttribute("recordOrderId") + "");
                ArrayList ins = new ArrayList();
                ins.add(0, outId);
                ins.add(1, Constant.PART_CODE_RELATION_09);
                ins.add(2, 1);//
                dao.callProcedure("PKG_PART.p_updatepartstock", ins, null);
                //拆分拣货单，逐单执行以下逻辑 start
                String[] pickIds = pickOrderId.split(",");
                for (int j = 0; j < pickIds.length; j++) {
                	//更新状态
                    changeStatus(pickIds[j], "");
                    //一次性生成拣货单下的所有现场bo
                    List<Map<String, Object>> boList = dao.getLocBoList(pickIds[j]);
                    if (boList.size() > 0) {
                        //得到该拣货单对应的一个订单id(orderId)
                        TtPartSoMainPO soMainPO = new TtPartSoMainPO();
                        soMainPO.setPickOrderId(pickIds[j]);
                        soMainPO = (TtPartSoMainPO) dao.select(soMainPO).get(0);
                        Long orderId = soMainPO.getOrderId();
                        createBoOrder(boList, orderId, pickIds[j]);
                        //更新装箱明细状态,保证只生成一次现场bo
                        dao.updatePkgDtlStatus(pickIds[j]);
                    }
                    insertHistory(pickIds[j], "");
                }
                //更改箱子状态,已经出库的箱子不能再出库,把outId回写到tt_part_pkg_box_dtl
                //dao.updatePkgStatus(pkgNos,pickOrderId,outId);
                dao.updatePkgStatus(trplanId, outId);//回写出库单ID
                dao.updateTranPlanStatus(trplanId, outId);//回写出库单ID
            }
            //以拣货单释放预扣款
            ArrayList list2 = new ArrayList();
            list2.add(0, 0);
            dao.callProcedure("PKG_PART_TOOLS.P_RELEASEAMOUNT", list2, null);
            if ("1".equals(forwardFlag)) {
//                this.partOutstockInit();
                act.setOutData("success", "生成出库单成功!");
            } else {
                String boId = CommonUtils.checkNull(CommonUtils.checkNull(request.getAttribute("boId")));
                act.setOutData("boId", boId);
                act.setOutData("success", "生成出库单成功!");
            }
        } catch (Exception e) {//异常方法
            act.setOutData("error", "生成出库单失败!");
            if (e instanceof BizException) {
//                POContext.endTxn(false);
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
//            POContext.endTxn(false);
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "生成出库单出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        } finally {
            try {
//                POContext.cleanTxn();
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "生成出库单出错,请联系管理员!");
            }
        }
    }


    public void showPartLocBoInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PART_LOC_BO_QUERY);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "bo信息查询失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void showPartLocBo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartOutstockDao dao = PartOutstockDao.getInstance();
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")): 1; // 处理当前页
            
            PageResult<Map<String, Object>> ps = dao.queryPartLocBoById(request, curPage, Constant.PAGE_SIZE);
            
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "现场bo信息查询失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

//    private void dealGift(AclUserBean loginUser, RequestWrapper request) {
//        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
//        PartPkgDao dao = PartPkgDao.getInstance();
//        PartOutstockDao partOutstockDao = PartOutstockDao.getInstance();
//        List<Map<String, Object>> pkgedList = dao.getPkgedGift(pickOrderId);
//        List<Map<String, Object>> mainList = dao.soMainList(pickOrderId);
//        if (null == mainList || mainList.size() <= 0) {
//            return;
//        }
//        String sellerId = mainList.get(0).get("SELLER_ID") + "";
//        String whId = mainList.get(0).get("WH_ID") + "";
//        for (Map<String, Object> map : pkgedList) {
//            String partId = CommonUtils.checkNull(map.get("PART_ID"));
//            Long pkgedQty = 0L;
//
//            try {
//                pkgedQty = Long.valueOf(CommonUtils.checkNull(map.get("PKG_QTY")));
//            } catch (Exception e) {
//                //什么也不做 默认0
//            }
//            List<Map<String, Object>> soGiftList = dao.getSoGiftList(partId, pickOrderId);
//            for (Map<String, Object> giftMap : soGiftList) {
//                if (pkgedQty <= 0) {
//                    continue;
//                }
//                String soId = CommonUtils.checkNull(giftMap.get("SO_ID"));
//                Long giftQty = 0L;
//                try {
//                    giftQty = Long.valueOf(CommonUtils.checkNull(giftMap.get("GIFT_QTY")));
//                } catch (Exception e) {
//                    //什么也不做 默认0
//                }
//                if (giftQty <= 0) {
//                    continue;
//                }
//
//                TtPartOutstockDtlPO po = new TtPartOutstockDtlPO();
//                List<Map<String, Object>> outMainList = dao.getOutStockMain(soId);
//                if (null == outMainList || outMainList.size() <= 0) {
//                    continue;
//                }
//                List<Map<String, Object>> partInfoList = dao.getPartInfo(partId, sellerId, whId);
//                if (null == partInfoList || partInfoList.size() <= 0) {
//                    continue;
//                }
//                Map<String, Object> partInfoMap = partInfoList.get(0);
//                String outId = outMainList.get(0).get("OUT_ID") + "";
//                Long trlineId = Long.parseLong(SequenceManager.getSequence(""));
//                po.setOutId(Long.valueOf(outId));
//                po.setOutlineId(trlineId);
//                po.setCreateBy(loginUser.getUserId());
//                po.setRemark("赠品");
//                po.setSalePrice(0D);
//                po.setPartId(Long.valueOf(partId));
//                po.setSoId(Long.valueOf(soId));
//                po.setBoQty(0L);
//                po.setIsGift(Constant.IF_TYPE_YES);
//                po.setSaleAmount(0D);
//                po.setCreateDate(new Date());
//                po.setPartCode(CommonUtils.checkNull(partInfoMap.get("PART_CODE")));
//                po.setUnit(CommonUtils.checkNull(partInfoMap.get("UNIT")));
//
//                po.setPartCname(CommonUtils.checkNull(partInfoMap.get("PART_CNAME")));
//                po.setPartOldcode(CommonUtils.checkNull(partInfoMap.get("PART_OLDCODE")));
//                po.setUnit(CommonUtils.checkNull(partInfoMap.get("UNIT")));
//                po.setMinPackage(Long.valueOf(CommonUtils.checkNull(partInfoMap.get("MIN_PACKAGE"))));
//                if (giftQty <= pkgedQty) {
//                    po.setPackgQty(giftQty);
//                    po.setBuyQty(giftQty);
//                    po.setOutstockQty(giftQty);
//                    pkgedQty = pkgedQty - giftQty;
//                } else {
//                    po.setPackgQty(pkgedQty);
//                    po.setBuyQty(pkgedQty);
//                    po.setOutstockQty(pkgedQty);
//                    pkgedQty = 0L;
//                }
//                List<Map<String, Object>> stockList = dao.getStock(partId, sellerId);
//                Long stock = 0L;
//                if (null != stockList && stockList.size() > 0) {
//                    stock = "".equals(CommonUtils.checkNull(stockList.get(0).get("stock_qty"))) ? 0L : Long.valueOf(CommonUtils.checkNull(stockList.get(0).get("stock_qty")));
//                }
//                po.setStockQty(stock);
//                dao.insert(po);
//
//                //只有主机厂 占用库存
//                if (sellerId.equals(Constant.OEM_ACTIVITIES)) {
//                    TtPartRecordPO insertPRPo = new TtPartRecordPO();
//                    String partBatch = Constant.PART_RECORD_BATCH;//配件批次
//                    String partVenId = Constant.PART_RECORD_VENDER_ID;//供应商ID
//                    String configId = Constant.PART_CODE_RELATION_09 + "";//配置ID
//                    String orgId = "";
//                    String orgName = "";
//                    String orgCode = "";
//                    PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
//                    List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
//                    if (null != beanList || beanList.size() >= 0) {
//                        orgId = beanList.get(0).getOrgId() + "";
//                        orgName = beanList.get(0).getOrgDesc() + "";
//                        orgCode = beanList.get(0).getOrgCode() + "";
//                    }
//                    Map<String, Object> warehouseMap = partOutstockDao.getWarehouse(whId);
//                    Map<String, Object> locMap = partOutstockDao.queryPartLocationByPartId((partId + ""), whId);
//                    Long recId = Long.parseLong(SequenceManager.getSequence(""));
//                    insertPRPo.setOrderId(po.getOutId());//业务ID
//                    insertPRPo.setRecordId(recId);
//                    insertPRPo.setAddFlag(2);
//                    insertPRPo.setPartId(Long.valueOf(partId));
//                    insertPRPo.setPartCode(po.getPartCode());
//                    insertPRPo.setPartOldcode(po.getPartOldcode());
//                    insertPRPo.setPartName(po.getPartCname());
//                    insertPRPo.setPartBatch(partBatch);
//                    insertPRPo.setVenderId(Long.parseLong(partVenId));
//                    insertPRPo.setPartNum(po.getOutstockQty());// 出库数量
//                    insertPRPo.setConfigId(Long.parseLong(configId));
//                    insertPRPo.setOrgId(Long.parseLong(orgId));
//                    insertPRPo.setOrgCode(orgCode);
//                    insertPRPo.setOrgName(orgName);
//                    insertPRPo.setWhId(Long.valueOf(whId));
//                    insertPRPo.setWhCode(CommonUtils.checkNull(warehouseMap.get("WH_CODE")));
//                    insertPRPo.setWhName(CommonUtils.checkNull(warehouseMap.get("WH_NAME")));
//                    insertPRPo.setLocId(Long.parseLong(CommonUtils.checkNull(locMap.get("LOC_ID"))));
//                    insertPRPo.setLocCode(CommonUtils.checkNull(locMap.get("LOC_CODE")));
//                    insertPRPo.setLocName(CommonUtils.checkNull(locMap.get("LOC_NAME")));
//                    insertPRPo.setOptDate(new Date());
//                    insertPRPo.setCreateDate(new Date());
//                    insertPRPo.setPersonId(loginUser.getUserId());
//                    insertPRPo.setPersonName(loginUser.getName());
//                    insertPRPo.setPartState(1);// 配件状态
//                    dao.insert(insertPRPo);
//                }
//                //发运单
//                TtPartTransDtlPO ttPartTransDtlPO = new TtPartTransDtlPO();
//                Long id = Long.parseLong(SequenceManager.getSequence(""));
//                List<Map<String, Object>> transList = dao.getTransList(soId);
//                if (null == transList || transList.size() <= 0) {
//                    continue;
//                }
//                Long transId = Long.valueOf(transList.get(0).get("TRANS_ID") + "");
//                ttPartTransDtlPO.setOutId(Long.valueOf(outId));
//                ttPartTransDtlPO.setTrlineId(id);
//                ttPartTransDtlPO.setTransId(transId);
//                ttPartTransDtlPO.setOutlineId(trlineId);
//                ttPartTransDtlPO.setPartId(Long.valueOf(partId));
//                ttPartTransDtlPO.setPartCode(po.getPartCode());
//                ttPartTransDtlPO.setPartOldcode(po.getPartOldcode());
//                ttPartTransDtlPO.setPartCname(po.getPartCname());
//                ttPartTransDtlPO.setUnit(po.getUnit());
//                ttPartTransDtlPO.setMinPackage(po.getMinPackage());
//                ttPartTransDtlPO.setBuyQty(po.getBuyQty());
//                ttPartTransDtlPO.setPackgQty(po.getPackgQty());
//                ttPartTransDtlPO.setOutstockQty(po.getOutstockQty());
//                ttPartTransDtlPO.setTransQty(po.getOutstockQty());
//                ttPartTransDtlPO.setRemark("赠品");
//                ttPartTransDtlPO.setCreateBy(loginUser.getUserId());
//                ttPartTransDtlPO.setCreateDate(new Date());
//                ttPartTransDtlPO.setIsGift(Constant.IF_TYPE_YES + "");
//                dao.insert(ttPartTransDtlPO);
//            }
//        }
//    }
/* 20170829 屏蔽
    private void insertOutStock(Map<String, Object> outMap) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();

        try {
            PartPickOrderDao dao = PartPickOrderDao.getInstance();
            PartOutstockDao partOutstockDao = PartOutstockDao.getInstance();
            PartSoManageDao partSoManageDao = PartSoManageDao.getInstance();

            //统一时间
            Date date = new Date();
            request.setAttribute("date", date);
            Iterator<String> iterator = outMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Map<String, Object> mainMap = partSoManageDao.getSoOrderMain(key);

                List<TtPartOutstockDtlPO> outList = (List<TtPartOutstockDtlPO>) outMap.get(key);
                Double amount = 0D;
                Long outId = Long.parseLong(SequenceManager.getSequence(""));
                if (null == request.getAttribute("recordOrderId")) {
                    request.setAttribute("recordOrderId", outId);
                }
                Long transId = Long.parseLong(SequenceManager.getSequence(""));
                request.setAttribute("transId", transId);
                for (TtPartOutstockDtlPO po : outList) {
                    Long outlineId = Long.parseLong(SequenceManager.getSequence(""));
                    po.setOutId(outId);
                    po.setOutlineId(outlineId);
                    amount = Arith.add(amount, po.getSaleAmount());
                    List<Map<String, Object>> list = partOutstockDao.getLock(po.getSoId() + "", CommonUtils.checkNull(mainMap.get("WH_ID")), po.getPartId() + "");
                    if (list != null && list.size() > 0) {
                        BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "销售单中配件:" + po.getPartCname() + "已被锁定！  " + "销售单无法出库!");
                        throw e;
                    }
                    dao.insert(po);
                    //insertPartRecord(po);
                    insertTransDtl(po);
                }
                insertOutMain(Long.valueOf(key), amount, outId);
            }

        } catch (Exception ex) {
            throw ex;
        }
    }*/

    private void insertOutMain(Long soId, Double amount, Long outId) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();

        try {
            String outCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_09);
            PartSoManageDao dao = PartSoManageDao.getInstance();
            PartDlrOrderDao partDlrOrderDao = PartDlrOrderDao.getInstance();
            Map<String, Object> mainMap = dao.getSoOrderMain(soId + "");
            String soCode = CommonUtils.checkNull(mainMap.get("SO_CODE"));
            String orderId = CommonUtils.checkNull(mainMap.get("ORDER_ID"));
            request.setAttribute("soCode", soCode);
            request.setAttribute("orderId", CommonUtils.checkNull(mainMap.get("ORDER_ID")));
            String orderType = CommonUtils.checkNull(mainMap.get("ORDER_TYPE"));
            String isBatchso = CommonUtils.checkNull(mainMap.get("IS_BATCHSO"));
            String dealerId = CommonUtils.checkNull(mainMap.get("DEALER_ID"));
            String dealerCode = CommonUtils.checkNull(mainMap.get("DEALER_CODE"));
            String dealerName = CommonUtils.checkNull(mainMap.get("DEALER_NAME"));
            String sellerId = CommonUtils.checkNull(mainMap.get("SELLER_ID"));
            String sellerCode = CommonUtils.checkNull(mainMap.get("SELLER_CODE"));
            String sellerName = CommonUtils.checkNull(mainMap.get("SELLER_NAME"));
            String buyerId = CommonUtils.checkNull(mainMap.get("BUYER_ID"));
            String buyerName = CommonUtils.checkNull(mainMap.get("BUYER_NAME"));
            String consigneesId = CommonUtils.checkNull(mainMap.get("CONSIGNEES_ID"));
            String consignees = CommonUtils.checkNull(mainMap.get("CONSIGNEES"));
            String addrId = CommonUtils.checkNull(mainMap.get("ADDR_ID"));
            String addr = CommonUtils.checkNull(mainMap.get("ADDR"));
            String receiver = CommonUtils.checkNull(mainMap.get("RECEIVER"));
            String tel = CommonUtils.checkNull(mainMap.get("TEL"));
            String postCode = CommonUtils.checkNull(mainMap.get("POST_CODE"));
            String station = CommonUtils.checkNull(mainMap.get("STATION"));
            String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String discount = CommonUtils.checkNull(mainMap.get("DISCOUNT"));
            String remark = CommonUtils.checkNull(mainMap.get("REMARK"));
            String payType = CommonUtils.checkNull(mainMap.get("PAY_TYPE"));
            String transpayType = CommonUtils.checkNull(mainMap.get("TRANSPAY_TYPE"));
            String sDate = CommonUtils.checkNull(mainMap.get("SALE_DATE"));
            String soFrom = CommonUtils.checkNull(mainMap.get("SO_FROM"));
            String whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
            String isLock = CommonUtils.checkNull(mainMap.get("LOCK_FREIGHT"));
            String lockFreight = CommonUtils.checkNull(mainMap.get("FREIGHT"));//锁定运费

            String freight = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date saleDate = sdf.parse(sDate);
            TtPartOutstockMainPO po = new TtPartOutstockMainPO();
            po.setPickOrderId(pickOrderId);
            po.setOutId(outId);
            po.setOutCode(outCode);
            po.setSoId(Long.valueOf(soId));
            po.setSoCode(soCode);
            po.setOrderType(Integer.valueOf(orderType));
            if (!"".equals(isBatchso)) {
                po.setIsBatchso(Integer.valueOf(isBatchso));
            }
            po.setSaleDate(saleDate);
            po.setDealerId(Long.valueOf(dealerId));
            po.setDealerCode(dealerCode);
            po.setDealerName(dealerName);
            po.setSellerId(Long.valueOf(sellerId));
            po.setSellerCode(sellerCode);
            po.setSellerName(sellerName);
            if (buyerId != null && !"".equals(buyerId)) {
                po.setBuyerId(Long.valueOf(buyerId));
            }
            po.setBuyerName(buyerName);
            po.setConsignees(consignees);
            po.setConsigneesId(Long.valueOf(consigneesId));
            po.setAddr(addr);
            po.setWhId(Long.valueOf(whId));
            po.setAddrId(Long.valueOf(addrId));
            po.setReceiver(receiver);
            po.setTel(tel);
            po.setSoFrom(Integer.valueOf(soFrom));
            po.setPostCode(postCode);
            po.setStation(station);
            po.setTransType(transType);

            //重新计算运费
            if (sellerId.equals(Constant.OEM_ACTIVITIES.toString()) && CommonUtils.checkNull(mainMap.get("IS_TRANSFREE")).
                    equals(Constant.IF_TYPE_NO.toString()) && !"0".equals(CommonUtils.checkNull(mainMap.get("FREIGHT")))) {
                freight = (partDlrOrderDao.getFreight(dealerId, orderType, amount + "")).replace(",", "");
            } else {
                if (!sellerId.equals(Constant.OEM_ACTIVITIES.toString()) && CommonUtils.checkNull(mainMap.get("IS_TRANSFREE")).
                        equals(Constant.IF_TYPE_NO.toString()) && !"0".equals(CommonUtils.checkNull(mainMap.get("FREIGHT")))) {
                    freight = CommonUtils.checkNull(mainMap.get("FREIGHT"));
                } else {
                    freight = "0";
                }

            }
            if (isLock.equals("1")) {
                po.setLockFreight(1);
                freight = lockFreight;
            }
            amount = Arith.add(Double.valueOf(freight), amount);

            Map<String, Object> freightMap = (Map) request.getAttribute("freightMap");
            if (null == freightMap) {
                freightMap = new HashMap<String, Object>();
            }
            freightMap.put(soId + "", freight);
            request.setAttribute("freightMap", freightMap);
            po.setFreight(Double.valueOf(freight));
            if (Double.valueOf(freight) != 0) {
                po.setIsTransfree(Constant.IF_TYPE_NO);
            } else {
                po.setIsTransfree(Constant.IF_TYPE_YES);
            }
            po.setAmount(amount);
            po.setDiscount(Double.valueOf(discount));
            po.setRemark(remark);
            po.setCreateDate((Date) request.getAttribute("date"));
            po.setCreateBy(loginUser.getUserId());
            po.setPayType(Integer.valueOf(payType));
            po.setTranspayType(Integer.valueOf(transpayType));
            po.setVer(1);
            po.setState(Constant.CAR_FACTORY_OUTSTOCK_STATE_02);
            dao.insert(po);

            //重新更新预占金额
            this.updateYZAccount(soCode, amount);
            //非主机厂释放预占资金
            if (!sellerId.equals(Constant.OEM_ACTIVITIES.toString())) {
                this.updateDlrAccount(dealerId, sellerId, amount.toString(), soCode);
            }
            insertTransMain(po);
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 插入出库单明细和发运单明细,并插入出入库记录
     * @param pickOrderId
     * @param trplanId
     * @throws Exception
     */
    private void insertOutStockDtl(String pickOrderId, String trplanId) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPickOrderDao pickOrderDao = PartPickOrderDao.getInstance();
        try {        	
        	//根据拣货单id查询销售单信息，获取仓库id
            List<Map<String, Object>> soOrderList = pickOrderDao.getSoMainList(pickOrderId);
            Map<String, Object> mainMap = soOrderList.get(0);
            String whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
            request.setAttribute("whId", whId);
            //统一时间
            Date date = new Date();
            request.setAttribute("date", date);
            //出库单id
            Long outId = Long.parseLong(SequenceManager.getSequence(""));
            request.setAttribute("recordOrderId", outId);
            //发运单id
            Long transId = Long.parseLong(SequenceManager.getSequence(""));
            request.setAttribute("transId", transId);

            double amount = 0d;
            int count = 0;

            //根据仓库id、发运计划id查询配件信息，包括批次、货位
            List<Map<String, Object>> list = pickOrderDao.getPartGroupInfo(pickOrderId,whId, trplanId);
            
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> dtlMap = list.get(i);
                //出库明细
                TtPartOutstockDtlPO dtlPo = new TtPartOutstockDtlPO();
                Long outlineId = Long.parseLong(SequenceManager.getSequence(""));//出库明细id
                Long partId = ((BigDecimal) dtlMap.get("PART_ID")).longValue();
                Long locId = ((BigDecimal) dtlMap.get("LOC_ID")).longValue();
                String batchNo=CommonUtils.checkNull(dtlMap.get("BATCH_NO")) ;//批次号
                
                TtPartDefinePO partPo = new TtPartDefinePO();
                partPo.setPartId(partId);
                partPo = (TtPartDefinePO) dao.select(partPo).get(0);

                //重新获取账面库存
                Map<String, Object> stockMap = dao.getStockQty(whId, partId, locId);
                long stockQty = ((BigDecimal) stockMap.get("STOCK_QTY")).longValue();
                long pkgedQty = ((BigDecimal) dtlMap.get("PKGEDQTY")).longValue();
                long saleQty = ((BigDecimal) dtlMap.get("SALES_QTY")).longValue();
                long soId = ((BigDecimal) dtlMap.get("SO_ID")).longValue();//销售单id
                long slineId = ((BigDecimal) dtlMap.get("SLINE_ID")).longValue();//销售单明细id
                long outstockQty = pkgedQty;
                //如果账面库存小于出库数量
                if (stockQty < outstockQty) {
                    continue;
                }
                count++;
                //获取该配件的销售单价
                Map<String, Object> map = dao.getBuyprice(pickOrderId, partId);
                double salePrice = ((BigDecimal) map.get("BUY_PRICE")).doubleValue();
                double saleAmount = Arith.mul(salePrice, outstockQty);
                amount = Arith.add(amount, saleAmount);

                dtlPo.setOutlineId(outlineId);
                dtlPo.setOutId(outId);
                dtlPo.setPartId(partId);
                dtlPo.setPartCode(partPo.getPartCode());
                dtlPo.setPartOldcode(partPo.getPartOldcode());
                dtlPo.setPartCname(partPo.getPartCname());
                dtlPo.setUnit(partPo.getUnit());
                dtlPo.setIsDirect(partPo.getIsDirect());
                dtlPo.setIsPlan(partPo.getIsPlan());
                dtlPo.setIsLack(partPo.getIsLack());
                dtlPo.setIsReplaced(partPo.getIsReplaced());
                dtlPo.setIsGift(partPo.getIsGift());
                dtlPo.setStockQty(stockQty);
                dtlPo.setMinPackage(partPo.getBuyMinPkg());
                dtlPo.setReservedQty(saleQty);
                dtlPo.setPackgQty(pkgedQty);
                dtlPo.setOutstockQty(outstockQty);
                dtlPo.setSalePrice(salePrice);
                dtlPo.setSaleAmount(saleAmount);
                dtlPo.setCreateDate(date);
                dtlPo.setCreateBy(loginUser.getUserId());
                dtlPo.setLocId(locId);//货位id
                dtlPo.setBatchNo(batchNo);//批次
                dtlPo.setSoId(soId);
                dtlPo.setSlineId(slineId);
                
                dao.insert(dtlPo);
                //插入配件出入库记录
                insertPartRecord(dtlPo, locId, whId,batchNo);
                //插入发运明细表
                insertTransDtl(dtlPo,batchNo);
            }

            if (count > 0) {
            	//添加出库单主表、发运单主表数据
                insertOutStockMain(amount, outId, mainMap, pickOrderId, whId, trplanId);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 创建现场BO单
     *
     * @param boList BO明细，BO主表
     * @throws Exception
     */
    public void createBoOrder(List<Map<String, Object>> boList, Long orderId, String pickOrderId) throws Exception {
        try {
            ActionContext act = ActionContext.getContext();
            RequestWrapper request = act.getRequest();
            AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            PartOutstockDao dao = PartOutstockDao.getInstance();

//            List<Map<String, Object>> boWithLocIdList = dao.getLocBoListWithLocId(pickOrderId);
            //构造现场BO主表
            TtPartBoMainPO po = new TtPartBoMainPO();
            Long boId = Long.parseLong(SequenceManager.getSequence(""));
            request.setAttribute("boId", boId);
            String boCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_20);
            po.setBoId(boId);
            po.setBoType("2");
            po.setBoCode(boCode);
            po.setOrderId(orderId);
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            po.setState(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_01);
            po.setVer(1);
            po.setPickOrderId(Long.parseLong(pickOrderId));
            dao.insert(po);
            //构造现场BO明细
            for (Map<String, Object> map : boList) {
                Long bolineId = Long.parseLong(SequenceManager.getSequence(""));
                TtPartBoDtlPO dtlPo = new TtPartBoDtlPO();

                dtlPo.setBolineId(bolineId);
                dtlPo.setBoId(boId);

                Long partId = ((BigDecimal) map.get("PART_ID")).longValue();
                Long locId = ((BigDecimal) map.get("LOC_ID")).longValue();
                String partCode = (String) map.get("PART_CODE");
                String partOldCode = (String) map.get("PART_OLDCODE");
                String partCName = (String) map.get("PART_CNAME");
                String unit = (String) map.get("UNIT");
                Long boQty = ((BigDecimal) map.get("LOC_BO_QTY")).longValue();
                Long saleQty = ((BigDecimal) map.get("SALES_QTY")).longValue();
                Long pkgQty = ((BigDecimal) map.get("PKG_QTY")).longValue();
                double buyPrice = ((BigDecimal) map.get("BUY_PRICE")).doubleValue();//取采购单价
                String batchNo=map.get("BATCH_NO")+"";//20170830 add

                dtlPo.setPartId(partId);
                dtlPo.setLocId(locId);
                dtlPo.setPartCode(partCode);
                dtlPo.setBoOddqty(boQty);
                dtlPo.setPartOldcode(partOldCode);
                dtlPo.setPartCname(partCName);
                dtlPo.setUnit(unit);
                dtlPo.setBuyQty(saleQty);
                dtlPo.setSalesQty(pkgQty);
                dtlPo.setBoQty(boQty);
                dtlPo.setCreateBy(loginUser.getUserId());
                dtlPo.setCreateDate(new Date());
                dtlPo.setBuyPrice(buyPrice);
                dtlPo.setBatchNo(batchNo);
                dao.insert(dtlPo);
                //预占资源释放
                reStoreBoStock(dtlPo);
                //封存现场BO
                fc_xc(dtlPo,batchNo);
            }

        } catch (Exception e) {
            throw e;
        }

    }

    private void fc_xc(TtPartBoDtlPO dtlPo,String batchNo) throws Exception {
        try {
            ActionContext act = ActionContext.getContext();
            AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            RequestWrapper request = act.getRequest();
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            PartOutstockDao partOutstockDao = PartOutstockDao.getInstance();
//            String partBatch = Constant.PART_RECORD_BATCH;//配件批次 
            String partBatch = batchNo;//配件批次 20170029 update
            String partVenId = Constant.PART_RECORD_VENDER_ID;//供应商ID
            String configId = Constant.PART_CODE_RELATION_19 + "";//配置ID
            String orgId = "";
            String orgName = "";
            String orgCode = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
                orgName = beanList.get(0).getOrgDesc() + "";
                orgCode = beanList.get(0).getOrgCode() + "";
            }
            String whId = CommonUtils.checkNull(request.getAttribute("whId"));
            Map<String, Object> warehouseMap = partOutstockDao.getWarehouse(whId);
            Long recId = Long.parseLong(SequenceManager.getSequence(""));
            Long recId2 = Long.parseLong(SequenceManager.getSequence(""));
            Long outId = Long.valueOf(CommonUtils.checkNull(request.getAttribute("recordOrderId")));

            TtPartRecordPO insertPRPo2 = new TtPartRecordPO();
            insertPRPo2.setRecordId(recId2);
            insertPRPo2.setAddFlag(2);//出库标识
            insertPRPo2.setPartId(dtlPo.getPartId());
            insertPRPo2.setPartCode(dtlPo.getPartCode());
            insertPRPo2.setPartOldcode(dtlPo.getPartOldcode());
            insertPRPo2.setPartName(dtlPo.getPartCname());
            insertPRPo2.setPartBatch(partBatch);
            insertPRPo2.setVenderId(Long.parseLong(partVenId));
            insertPRPo2.setPartNum(dtlPo.getBoQty());//出入库数量
            insertPRPo2.setConfigId(Long.parseLong(configId));
            insertPRPo2.setOrderId(outId);//变更单ID
            insertPRPo2.setOrgId(Long.parseLong(orgId));
            insertPRPo2.setOrgCode(orgCode);
            insertPRPo2.setOrgName(orgName);
            insertPRPo2.setWhId(Long.parseLong(whId));
            insertPRPo2.setWhCode(CommonUtils.checkNull(warehouseMap.get("WH_CODE")));
            insertPRPo2.setWhName(CommonUtils.checkNull(warehouseMap.get("WH_NAME")));
            insertPRPo2.setLocId(dtlPo.getLocId());

            TtPartLoactionDefinePO locPo = new TtPartLoactionDefinePO();
            locPo.setLocId(dtlPo.getLocId());
            locPo = (TtPartLoactionDefinePO) dao.select(locPo).get(0);
            insertPRPo2.setLocCode(locPo.getLocCode());
            insertPRPo2.setLocName(locPo.getLocName());
            insertPRPo2.setOptDate(new Date());
            insertPRPo2.setCreateDate(new Date());
            insertPRPo2.setPersonId(loginUser.getUserId());
            insertPRPo2.setPersonName(loginUser.getName());
            insertPRPo2.setPartState(1);//配件状态
            dao.insert(insertPRPo2);

            List<Object> ins2 = new LinkedList<Object>();
            //调用出入库逻辑
            ins2 = new LinkedList<Object>();
            ins2.add(0, outId);
            ins2.add(1, configId);
            ins2.add(2, 0);
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins2, null);
            /////////////////////////////////////////////////////////
            TtPartRecordPO insertPRPo = new TtPartRecordPO();
            insertPRPo.setRecordId(recId);
            insertPRPo.setAddFlag(1);//入库标识
            insertPRPo.setPartId(dtlPo.getPartId());
            insertPRPo.setPartCode(dtlPo.getPartCode());
            insertPRPo.setPartOldcode(dtlPo.getPartOldcode());
            insertPRPo.setPartName(dtlPo.getPartCname());
            insertPRPo.setPartBatch(partBatch);
            insertPRPo.setVenderId(Long.parseLong(partVenId));
            insertPRPo.setPartNum(dtlPo.getBoQty());//出入库数量
            insertPRPo.setConfigId(Long.parseLong(configId));
            insertPRPo.setOrderId(outId);//变更单ID
            insertPRPo.setOrgId(Long.parseLong(orgId));
            insertPRPo.setOrgCode(orgCode);
            insertPRPo.setOrgName(orgName);
            insertPRPo.setWhId(Long.parseLong(whId));
            insertPRPo.setWhCode(CommonUtils.checkNull(warehouseMap.get("WH_CODE")));
            insertPRPo.setWhName(CommonUtils.checkNull(warehouseMap.get("WH_NAME")));
            insertPRPo.setLocId(dtlPo.getLocId());
            insertPRPo.setLocCode(locPo.getLocCode());
            insertPRPo.setLocName(locPo.getLocName());
            insertPRPo.setOptDate(new Date());
            insertPRPo.setCreateDate(new Date());
            insertPRPo.setPersonId(loginUser.getUserId());
            insertPRPo.setPersonName(loginUser.getName());
            insertPRPo.setPartState(2);//配件状态
            dao.insert(insertPRPo);
            List<Object> ins = new LinkedList<Object>();
            //调用出入库逻辑
            ins = new LinkedList<Object>();
            ins.add(0, outId);
            ins.add(1, configId);
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);

        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * 添加tt_part_outstock_main出库单主表、Tt_Part_Trans发运单表数据
     * @param amount
     * @param outId
     * @param mainMap
     * @param pickOrderId
     * @param whId
     * @param trplanId
     * @throws Exception
     */
    private void insertOutStockMain(Double amount, Long outId, Map<String, Object> mainMap, String pickOrderId, String whId, String trplanId) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
        	//生成出库单编码
            String outCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_09);
            request.setAttribute("outCode", outCode);
            
            PartSoManageDao dao =  PartSoManageDao.getInstance();
            Integer orderType = ((BigDecimal) mainMap.get("ORDER_TYPE")).intValue();
            Long dealerId = ((BigDecimal) mainMap.get("DEALER_ID")).longValue();
            String dealerCode = CommonUtils.checkNull(mainMap.get("DEALER_CODE"));
            String dealerName = CommonUtils.checkNull(mainMap.get("DEALER_NAME"));
            Long sellerId = ((BigDecimal) mainMap.get("SELLER_ID")).longValue();
            request.setAttribute("sellerId", sellerId);
            String sellerCode = CommonUtils.checkNull(mainMap.get("SELLER_CODE"));
            String sellerName = CommonUtils.checkNull(mainMap.get("SELLER_NAME"));
            String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
            String accountId = CommonUtils.checkNull(mainMap.get("ACCOUNT_ID"));

            TtPartTransPlanPO planPO = new TtPartTransPlanPO();
            planPO.setTrplanId(Long.valueOf(trplanId));

            TtPartAddrDefinePO addPo = new TtPartAddrDefinePO();
            addPo.setDealerId(dealerId);
            addPo.setAddrId(((TtPartTransPlanPO) dao.select(planPO).get(0)).getAddrId());//重新获取发运计划上的地址ID  20140628
            addPo = (TtPartAddrDefinePO) dao.select(addPo).get(0);

            //添加出库单主表数据
            TtPartOutstockMainPO po = new TtPartOutstockMainPO();
            po.setPickOrderId(pickOrderId);
            po.setOutId(outId);
            po.setOutCode(outCode);
            po.setOrderType(orderType);
            po.setDealerId(dealerId);
            po.setDealerCode(dealerCode);
            po.setDealerName(dealerName);
            po.setSellerId(sellerId);
            po.setSellerCode(sellerCode);
            po.setSellerName(sellerName);
            po.setConsigneesId(dealerId);
            po.setConsignees(dealerName);
            po.setAddrId(addPo.getAddrId());
            po.setAddr(addPo.getAddr());
            po.setReceiver(addPo.getLinkman());
            po.setTel(addPo.getTel());
            po.setPostCode(addPo.getPostCode());
            po.setStation(addPo.getStation());
            po.setWhId(Long.valueOf(whId));
            po.setTransType(transType);
            po.setTrplanId(Long.valueOf(trplanId));
            
            //20160304 add 
            po.setSoCode((String)mainMap.get("SO_CODE"));
            po.setSoId(Long.valueOf(CommonUtils.checkNull(mainMap.get("SO_ID"))));
            po.setIsBatchso(Integer.valueOf(CommonUtils.checkNull(mainMap.get("IS_BATCHSO"))));
            po.setSoFrom(Integer.valueOf(CommonUtils.checkNull(mainMap.get("SO_FROM"))));
            po.setPayType(Integer.valueOf(CommonUtils.checkNull(mainMap.get("PAY_TYPE"))));

            //重新计算运费
            /*if (sellerId.equals(Constant.OEM_ACTIVITIES.toString()) && CommonUtils.checkNull(mainMap.get("IS_TRANSFREE")).
                    equals(Constant.IF_TYPE_NO.toString()) && !"0".equals(CommonUtils.checkNull(mainMap.get("FREIGHT")))) {
    			freight = (partDlrOrderDao.getFreight(dealerId, orderType, amount + "")).replace(",","");
    		} else {
    			if(!sellerId.equals(Constant.OEM_ACTIVITIES.toString()) && CommonUtils.checkNull(mainMap.get("IS_TRANSFREE")).
    					equals(Constant.IF_TYPE_NO.toString()) && !"0".equals(CommonUtils.checkNull(mainMap.get("FREIGHT")))){
    				freight = CommonUtils.checkNull(mainMap.get("FREIGHT"));
    			}else{
    				freight = "0";
    			}
    			
    		}
    		if(isLock.equals("1")){
    			po.setLockFreight(1);
    			freight = lockFreight;
    		}
    		amount = Arith.add(Double.valueOf(freight), amount);
    		
    		Map<String, Object> freightMap = (Map) request.getAttribute("freightMap");
    		if (null == freightMap) {
    			freightMap = new HashMap<String, Object>();
    		}
    		freightMap.put(soId + "", freight);
    		request.setAttribute("freightMap", freightMap);
    		po.setFreight(Double.valueOf(freight));
    		if (Double.valueOf(freight) != 0) {
    			po.setIsTransfree(Constant.IF_TYPE_NO);
    		} else {
    			po.setIsTransfree(Constant.IF_TYPE_YES);
    		}*/

            po.setAmount(amount);
            //po.setRemark(remark);
            po.setCreateDate((Date) request.getAttribute("date"));
            po.setCreateBy(loginUser.getUserId());
            po.setVer(1);
            po.setState(Constant.CAR_FACTORY_OUTSTOCK_STATE_02);
            po.setAccountId(Long.valueOf(accountId));
            
            dao.insert(po);

            //重新更新预占金额
            /*this.updateYZAccount(soCode, amount);
            //非主机厂释放预占资金
    		if (!sellerId.equals(Constant.OEM_ACTIVITIES.toString())) {
    			this.updateDlrAccount(dealerId, sellerId, amount.toString(), soCode);
    		}*/
            //添加发运单
            insertTransMain(po);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 添加发运单
     * @param outMainPo
     * @throws Exception
     */
    public void insertTransMain(TtPartOutstockMainPO outMainPo) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {

            Long transId = Long.valueOf(request.getAttribute("transId") + "");
            PartTransDao partTransDao = PartTransDao.getInstance();
           
        	Map<String, Object> orderMainMap = partTransDao.getOrderMainInfo2(outMainPo.getPickOrderId() + "",outMainPo.getSellerId());
            String orderId = CommonUtils.checkNull(orderMainMap.get("ORDER_ID"));;
            String orderCode = CommonUtils.checkNull(orderMainMap.get("ORDER_CODE"));
            //添加发运单
            TtPartTransPO po = new TtPartTransPO();
            String transCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_10);
            String outCode = CommonUtils.checkNull(outMainPo.getOutCode());
            String soCode = CommonUtils.checkNull(outMainPo.getSoCode());
//                String isBatchso = CommonUtils.checkNull(outMainPo.getIsBatchso());
//                String soFrom = CommonUtils.checkNull(outMainPo.getSoFrom());
            String dealerId = CommonUtils.checkNull(outMainPo.getDealerId());
            String dealerCode = CommonUtils.checkNull(outMainPo.getDealerCode());
            String dealerName = CommonUtils.checkNull(outMainPo.getDealerName());
            String sellerId = CommonUtils.checkNull(outMainPo.getSellerId());
            String sellerCode = CommonUtils.checkNull(outMainPo.getSellerCode());
            Date saleDate = outMainPo.getSaleDate();
            String sellerName = CommonUtils.checkNull(outMainPo.getSellerName());
            String buyerId = CommonUtils.checkNull(outMainPo.getBuyerId());
            String buyerName = CommonUtils.checkNull(outMainPo.getBuyerName());
            String consigneesId = CommonUtils.checkNull(outMainPo.getConsigneesId());
            String consignees = CommonUtils.checkNull(outMainPo.getConsignees());
            String addrId = CommonUtils.checkNull(outMainPo.getAddrId());
            String addr = CommonUtils.checkNull(outMainPo.getAddr());
            String receiver = CommonUtils.checkNull(outMainPo.getReceiver());
            String tel = CommonUtils.checkNull(outMainPo.getTel());
            String tel2 = "";
            String linkman = "";
            String postCode = CommonUtils.checkNull(outMainPo.getPostCode());
            String station = CommonUtils.checkNull(outMainPo.getStation());
            String orderType = CommonUtils.checkNull(outMainPo.getOrderType() + "");
            String amount = CommonUtils.checkNull(outMainPo.getAmount());
            //查询发运计划信息
            TtPartTransPlanPO transPlanPO = new TtPartTransPlanPO();
            transPlanPO.setTrplanId(outMainPo.getTrplanId());
            transPlanPO = (TtPartTransPlanPO) dao.select(transPlanPO).get(0);

            po.setTransId(transId);
            po.setTransCode(transCode);
            po.setOutId(outMainPo.getOutId());
            po.setOutCode(outCode);
            po.setSoCode(soCode);
            po.setSoId(outMainPo.getSoId());
            po.setOrderType(Integer.valueOf(orderType));
            if (!"".equals(orderId)) {
                po.setOrderId(Long.valueOf(orderId));
            }
            if (!"".equals(orderCode)) {
                po.setOrderCode(orderCode);
            }
            po.setDealerId(Long.valueOf(dealerId));
            po.setAmount(Double.valueOf(amount));
            po.setDealerName(dealerName);
            po.setDealerCode(dealerCode);
            po.setSellerId(Long.valueOf(sellerId));
            po.setSellerCode(sellerCode);
            po.setSellerName(sellerName);
            po.setSaleDate(saleDate);
            if (buyerId != "" && !"".equals(buyerId)) {
                po.setBuyerId(Long.valueOf(buyerId));
            } else {
                po.setBuyerId(-1l);
            }
            po.setBuyerName(buyerName);
            po.setConsignees(consignees);
            po.setConsigneesId(Long.valueOf(consigneesId));
            po.setAddr(addr);
            po.setAddrId(Long.valueOf(addrId));
            po.setReceiver(receiver);
            po.setTel(tel);
            po.setTel2(tel2);
            po.setLinkman(linkman);
            po.setPostCode(postCode);
            po.setStation(station);
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            po.setState(Constant.CAR_FACTORY_TRANS_STATE_01);
            po.setWhId(outMainPo.getWhId());
            po.setTransType(transPlanPO.getTransType());
            po.setTransportOrg(transPlanPO.getTransportOrg());
            po.setVer(1);
            //20170804 add 新增
            po.setEsAmount(Double.parseDouble(request.getParamValue("estimatedAmount")));//预估运费
            po.setAcAmount(Double.parseDouble(request.getParamValue("actualAmount")));//实际运费
            //20180831 add
            po.setWuliuCode(request.getParamValue("WULIU_CODE"));//物流单号
        	
            dao.insert(po);
            
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 插入发运明细
     * @param outDtlPo
     * @throws Exception
     */
    public void insertTransDtl(TtPartOutstockDtlPO outDtlPo,String batchNo) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartTransDao partTrasDao = PartTransDao.getInstance();
        try {
            Long outId = outDtlPo.getOutId();
            TtPartTransDtlPO po = new TtPartTransDtlPO();
            
            Long trlineId = Long.parseLong(SequenceManager.getSequence(""));
            Long transId = Long.valueOf(request.getAttribute("transId") + "");
//            Long outlineId = outDtlPo.getOutlineId();
//            Long partId = outDtlPo.getPartId();
//            String partCode = outDtlPo.getPartCode();
//            String partOldcode = outDtlPo.getPartOldcode();
//            String partCname = outDtlPo.getPartCname();
//            String unit = outDtlPo.getUnit();
//            Long minPackage = outDtlPo.getMinPackage();
//            Long reservedQty = outDtlPo.getReservedQty();
//            Long packgQty = outDtlPo.getPackgQty();
//            Long outstockQty = outDtlPo.getOutstockQty();
//            String remark = outDtlPo.getRemark();
            po.setOutId(outId);
            po.setTrlineId(trlineId);
            po.setTransId(transId);
            po.setOutlineId(outDtlPo.getOutlineId());
            po.setPartId(outDtlPo.getPartId());
            po.setPartCode( outDtlPo.getPartCode());
            po.setPartOldcode(outDtlPo.getPartOldcode());
            po.setPartCname(outDtlPo.getPartCname());
            po.setUnit(outDtlPo.getUnit());
            po.setMinPackage(outDtlPo.getMinPackage());
            po.setBuyQty(outDtlPo.getReservedQty());
            po.setPackgQty(outDtlPo.getPackgQty());
            po.setOutstockQty(outDtlPo.getOutstockQty());
            po.setTransQty(outDtlPo.getOutstockQty());
            po.setRemark(outDtlPo.getRemark());
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            po.setBatchNo(batchNo);//20170829 add
            partTrasDao.insert(po);

        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 插入配件出入库记录
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 
     */
    public void insertPartRecord(TtPartOutstockDtlPO po, Long locId, String whId,String batchNo) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        TtPartRecordPO insertPRPo = new TtPartRecordPO();
//        String partBatch = Constant.PART_RECORD_BATCH;//配件批次
        String partBatch = batchNo;//配件批次
        String partVenId = Constant.PART_RECORD_VENDER_ID;//供应商ID
        String configId = Constant.PART_CODE_RELATION_09 + "";//配置ID
        String orgId = "";
        String orgName = "";
        String orgCode = "";
        try {
            //判断是否为车厂  partOutstock
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
                orgName = beanList.get(0).getOrgDesc() + "";
                orgCode = beanList.get(0).getOrgCode() + "";
            }
            //优化出库效率
//            Map<String, Object> warehouseMap = dao.getWarehouse(whId);

//            TtPartLoactionDefinePO locPo = new TtPartLoactionDefinePO();
//            locPo.setLocId(locId);
//            locPo = (TtPartLoactionDefinePO) dao.select(locPo).get(0);

            Long partId = po.getPartId();
            Long recId = Long.parseLong(SequenceManager.getSequence(""));
            Long outId = Long.valueOf(CommonUtils.checkNull(request.getAttribute("recordOrderId")));
            
            insertPRPo.setOrderId(outId);//业务ID //modify by yuan 20131013
            insertPRPo.setRecordId(recId);
            insertPRPo.setAddFlag(2);
            insertPRPo.setPartId(partId);
            insertPRPo.setPartCode(po.getPartCode());
            insertPRPo.setPartOldcode(po.getPartOldcode());
            insertPRPo.setPartName(po.getPartCname());
            insertPRPo.setPartBatch(partBatch);
            insertPRPo.setVenderId(Long.parseLong(partVenId));
            insertPRPo.setPartNum(po.getOutstockQty());//出库数量
            insertPRPo.setConfigId(Long.parseLong(configId));
            insertPRPo.setOrgId(Long.parseLong(orgId));
            insertPRPo.setOrgCode(orgCode);
            insertPRPo.setOrgName(orgName);
            insertPRPo.setWhId(Long.valueOf(whId));
//            insertPRPo.setWhCode(CommonUtils.checkNull(warehouseMap.get("WH_CODE")));
//            insertPRPo.setWhName(CommonUtils.checkNull(warehouseMap.get("WH_NAME")));
            insertPRPo.setLocId(locId);
//            insertPRPo.setLocCode(locPo.getLocCode());
//            insertPRPo.setLocName(locPo.getLocName());
            insertPRPo.setOptDate(new Date());
            insertPRPo.setCreateDate(new Date());
            insertPRPo.setPersonId(loginUser.getUserId());
            insertPRPo.setPersonName(loginUser.getName());
            insertPRPo.setPartState(1);//配件状态
//            insertPRPo.setBatchNo(batchNo);//批次
            
            dao.insert(insertPRPo);
        } catch (Exception ex) {
            throw ex;
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description:
     */
    private Map<String, Object> loadData() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPickOrderDao dao =  PartPickOrderDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String[] partIdArr = request.getParamValues("cb");
            List<Map<String, Object>> soOrderList = dao.getSoMainList(pickOrderId);
            Map<String, Object> mainMap = soOrderList.get(0);
            String soIds = "";
            for (Map<String, Object> map : soOrderList) {
                String soId = CommonUtils.checkNull(map.get("SO_ID"));
                soIds = soIds + "," + soId;
            }
            if (!"".equals(soIds)) {
                soIds = soIds.replaceFirst(",", "");
            }
            String whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
            request.setAttribute("whId", whId);
            Map<String, Object> boMap = new HashMap<String, Object>();
            Map<String, Object> outMap = new HashMap<String, Object>();
            for (int i = 0; i < partIdArr.length; i++) {
                String partId = partIdArr[i].split(",")[0];
                Long salesQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("saleQty_" + partId)));
                Long outQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("outstockNo_" + partId)));
                List<Map<String, Object>> list = dao.getSoMain(partId, soIds);   //配件的所有订单
                if (list == null || list.size() <= 0) {
                    BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "销售单数据错误，请联系管理员!");
                    throw e;
                }
                if (salesQty > outQty) {
                    //生成bo
                    Long totalBoQty = salesQty - outQty;
                    //计算需要几个订单产生bo才能满足
                    for (Map<String, Object> map : list) {
                        String soId = CommonUtils.checkNull(map.get("SO_ID"));
                        Map<String, Object> soDtlMap = dao.getSoOrderDtl(partId, soId);
                        Long soDtlSalesQty = Long.valueOf(soDtlMap.get("SALES_QTY") + "");
                        TtPartOutstockDtlPO po = new TtPartOutstockDtlPO();
                        boolean flag = false;
                        //组装map
                        if (totalBoQty > soDtlSalesQty) {
                            Long boQty = soDtlSalesQty;
                            totalBoQty = totalBoQty - soDtlSalesQty;
                            soDtlMap.put("boQty", boQty);

                        } else {
                            Long boQty = totalBoQty;
                            soDtlMap.put("boQty", boQty);
                            flag = true;
                        }
                        soDtlMap.put("orderCode", CommonUtils.checkNull(map.get("ORDER_CODE")));
                        soDtlMap.put("orderId", CommonUtils.checkNull(map.get("ORDER_ID")));
                        this.loadDtlPo(soDtlMap, po);
                        if (outMap.get(soId) == null) {
                            List<TtPartOutstockDtlPO> outList = new ArrayList<TtPartOutstockDtlPO>();
                            outList.add(po);
                            outMap.put(soId, outList);
                        } else {
                            ((List<TtPartOutstockDtlPO>) outMap.get(soId)).add(po);
                        }
                        if (totalBoQty <= 0) {
                            continue;
                        }
                        //以soid为key 方便后续生成表  以及保持唯一性
                        if (boMap.get(soId) == null) {
                            List<Map<String, Object>> boList = new ArrayList<Map<String, Object>>();
                            boList.add(soDtlMap);
                            boMap.put(soId, boList);
                        } else {
                            ((List<Map<String, Object>>) boMap.get(soId)).add(soDtlMap);
                        }
                        if (flag) {
                            totalBoQty = 0L;
                        }
                    }
                } else {
                    //无bo全额出库
                    for (Map<String, Object> map : list) {
                        String soId = CommonUtils.checkNull(map.get("SO_ID"));
                        TtPartOutstockDtlPO po = new TtPartOutstockDtlPO();
                        Map<String, Object> soDtlMap = dao.getSoOrderDtl(partId, soId);
                        this.loadDtlPo(soDtlMap, po);
                        if (outMap.get(soId) == null) {
                            List<TtPartOutstockDtlPO> outList = new ArrayList<TtPartOutstockDtlPO>();
                            outList.add(po);
                            outMap.put(soId, outList);
                        } else {
                            ((List<TtPartOutstockDtlPO>) outMap.get(soId)).add(po);
                        }
                    }
                }

            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("boMap", boMap);
            map.put("outMap", outMap);
            return map;
        } catch (Exception e) {
            throw e;
        }
    }

    private void loadDtlPo(Map<String, Object> map, TtPartOutstockDtlPO po) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Long salesQty = Long.valueOf(map.get("SALES_QTY") + "");
            Long boQty = Long.valueOf(map.get("boQty") == null ? "0" : (map.get("boQty") + ""));
            Long outQty = salesQty - boQty;
            Long soId = Long.valueOf(map.get("SO_ID") + "");
            Long partId = Long.valueOf(map.get("PART_ID") + "");
            String partCode = CommonUtils.checkNull(map.get("PART_CODE"));
            String partCname = CommonUtils.checkNull(map.get("PART_CNAME"));
            String partOldcode = CommonUtils.checkNull(map.get("PART_OLDCODE"));
            String unit = CommonUtils.checkNull(map.get("UNIT"));
            String isPlan = CommonUtils.checkNull(map.get("IS_PLAN"));
            String isLack = CommonUtils.checkNull(map.get("IS_LACK"));
            String isReplaced = CommonUtils.checkNull(map.get("IS_REPLACED"));
            String isGift = CommonUtils.checkNull(map.get("IS_GIFT"));
            String stockQty = CommonUtils.checkNull(map.get("STOCK_QTY"));
            String minPackage = CommonUtils.checkNull(map.get("MIN_PACKAGE"));
            String isDirect = CommonUtils.checkNull(map.get("IS_DIRECT"));
            Double buyPrice = Double.valueOf(CommonUtils.checkNull(map.get("BUY_PRICE")));
            Double amount = Arith.mul(buyPrice, outQty);
            String remark = CommonUtils.checkNull(map.get("REMARK"));

            po.setReservedQty(outQty);
            po.setOutstockQty(outQty);
            po.setPackgQty(outQty);
            po.setSoId(soId);
            po.setPartId(partId);
            po.setPartCode(partCode);
            po.setPartCname(partCname);
            po.setPartOldcode(partOldcode);
            po.setUnit(unit);
            po.setBoQty(boQty);
            if (!"".equals(isPlan)) {
                po.setIsPlan(Integer.valueOf(isPlan));
            }
            if (!"".equals(isDirect)) {
                po.setIsDirect(Integer.valueOf(isDirect));
            }
            if (!"".equals(isGift)) {
                po.setIsGift(Integer.valueOf(isGift));
            }
            if (!"".equals(isLack)) {
                po.setIsLack(Integer.valueOf(isLack));
            }
            if (!"".equals(isReplaced)) {
                po.setIsReplaced(Integer.valueOf(isReplaced));
            }
            // add by yuan   20131208
            PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
            TtPartSoMainPO soMainPO = new TtPartSoMainPO();
            soMainPO.setSoId(Long.valueOf(soId));
            soMainPO = (TtPartSoMainPO) dao.select(soMainPO).get(0);
            Map<String, Object> locMap = dao.getLoc(po.getPartId().toString(), soMainPO.getWhId().toString(), soMainPO.getSellerId().toString());
            //查询库存数量 add by yuan 20131208
            VwPartStockPO itemStockPO = new VwPartStockPO();
            itemStockPO.setPartId(po.getPartId());
            itemStockPO.setOrgId(soMainPO.getSellerId());
            itemStockPO.setWhId(soMainPO.getWhId());
            itemStockPO.setLocId(((BigDecimal) locMap.get("LOC_ID")).longValue());
            itemStockPO.setState(Constant.STATUS_ENABLE);
            List list = dao.select(itemStockPO);
            if (list.size() > 0) {
                itemStockPO = (VwPartStockPO) list.get(0);
                po.setStockQty(itemStockPO.getItemQty());
            }
            //po.setStockQty(Long.valueOf(stockQty));
            //end
            po.setMinPackage(Long.valueOf(minPackage));

            po.setBuyQty(salesQty);

            po.setSalePrice(buyPrice);
            po.setSaleAmount(amount);
            po.setRemark(remark);
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            po.setVer(1);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:生成BO单
     */
//    private void createBoOrder(Map<String, Object> boMap) throws Exception {
//        ActionContext act = ActionContext.getContext();
//        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//        RequestWrapper request = act.getRequest();
//        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
//        PartSoManageDao partSoManageDao = PartSoManageDao.getInstance();
//        if (!boMap.isEmpty()) {
//            Iterator<String> iterator = boMap.keySet().iterator();
//            while (iterator.hasNext()) {
//                String soId = iterator.next();
//                List<Map<String, Object>> dtlList = (List<Map<String, Object>>) boMap.get(soId);
//                TtPartBoMainPO po = new TtPartBoMainPO();
//                Long boId = Long.parseLong(SequenceManager.getSequence(""));
//                String boCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_20);
//                Map<String, Object> mainMap = partSoManageDao.getSoOrderMain(soId);
//                String orderCode = CommonUtils.checkNull(mainMap.get("ORDER_CODE"));
//                String orderId = CommonUtils.checkNull(mainMap.get("ORDER_ID"));
//                po.setBoId(boId);
//                po.setBoType("2");
//                po.setBoCode(boCode);
//                po.setSoId(Long.valueOf(soId));
//                po.setOrderCode(orderCode);
//                if (!"".equals(orderId)) {
//                    po.setOrderId(Long.valueOf(orderId));
//                }
//                po.setCreateBy(loginUser.getUserId());
//                po.setCreateDate(new Date());
//                po.setState(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_01);
//                po.setVer(1);
//                dao.insert(po);
//                for (Map<String, Object> map : dtlList) {
//                    Long bolineId = Long.parseLong(SequenceManager.getSequence(""));
//                    TtPartBoDtlPO dtlPo = new TtPartBoDtlPO();
//                    if (CommonUtils.checkNull(map.get("IS_GIFT")).equals(Constant.IF_TYPE_YES + "")) {
//                        continue;
//                    }
//                    String partId = CommonUtils.checkNull(map.get("PART_ID"));
//                    String partCode = CommonUtils.checkNull(map.get("PART_CODE"));
//                    String partOldCode = CommonUtils.checkNull(map.get("PART_OLDCODE"));
//                    Double price = Double.valueOf(map.get("BUY_PRICE") + "");
//                    String partCname = CommonUtils.checkNull(map.get("PART_CNAME"));
//                    String unit = CommonUtils.checkNull(map.get("UNIT"));
//                    Long boQty = Long.valueOf(map.get("boQty") + "");
//                    Long buyQty = Long.valueOf(map.get("BUY_QTY") + "");
//                    Long salesQty = Long.valueOf(map.get("SALES_QTY") + "") - boQty;
//                    dtlPo.setBolineId(bolineId);
//                    dtlPo.setBoId(boId);
//                    dtlPo.setPartId(Long.valueOf(partId));
//                    dtlPo.setPartCode(partCode);
//                    dtlPo.setBoOddqty(boQty);
//                    dtlPo.setPartOldcode(partOldCode);
//                    dtlPo.setBuyPrice(price);
//                    dtlPo.setPartCname(partCname);
//                    dtlPo.setUnit(unit);
//                    dtlPo.setBuyQty(buyQty);
//                    dtlPo.setSalesQty(Long.valueOf(salesQty));
//                    dtlPo.setBoQty(boQty);
//                    dtlPo.setCreateBy(loginUser.getUserId());
//                    dtlPo.setCreateDate(new Date());
//                    dao.insert(dtlPo);
//                    // modify by yuan 20310921 start
//                    //insertAccount(dtlPo, Long.valueOf(soId));
//                    //end
//                    //reStoreBoStock(dtlPo, po);
//                    //封存
//                    fc(map);
//                }
//            }
//        }
//    }
//
//    private void fc(Map<String, Object> map) {
//        ActionContext act = ActionContext.getContext();
//        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//        RequestWrapper request = act.getRequest();
//        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
//        PartOutstockDao partOutstockDao = PartOutstockDao.getInstance();
//        String partBatch = Constant.PART_RECORD_BATCH;//配件批次
//        String partVenId = Constant.PART_RECORD_VENDER_ID;//供应商ID
//        String configId = Constant.PART_CODE_RELATION_19 + "";//配置ID
//        String orgId = "";
//        String orgName = "";
//        String orgCode = "";
//        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
//        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
//        if (null != beanList || beanList.size() >= 0) {
//            orgId = beanList.get(0).getOrgId() + "";
//            orgName = beanList.get(0).getOrgDesc() + "";
//            orgCode = beanList.get(0).getOrgCode() + "";
//        }
//        String whId = CommonUtils.checkNull(request.getAttribute("whId"));
//        Map<String, Object> warehouseMap = partOutstockDao.getWarehouse(whId);
//        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
//        String partId = CommonUtils.checkNull(map.get("PART_ID"));
//        Map<String, Object> locMap = partOutstockDao.queryPartLocationByPartId((partId + ""), whId);
//        String locId = CommonUtils.checkNull(locMap.get("LOC_ID"));
//        Long recId = Long.parseLong(SequenceManager.getSequence(""));
//        Long recId2 = Long.parseLong(SequenceManager.getSequence(""));
//        Long outId = Long.valueOf(CommonUtils.checkNull(request.getAttribute("recordOrderId")));
//        String partCode = CommonUtils.checkNull(map.get("PART_CODE"));
//        String partOldCode = CommonUtils.checkNull(map.get("PART_OLDCODE"));
//        Double price = Double.valueOf(map.get("BUY_PRICE") + "");
//        String partCname = CommonUtils.checkNull(map.get("PART_CNAME"));
//        String unit = CommonUtils.checkNull(map.get("UNIT"));
//        String partOldcode = CommonUtils.checkNull(map.get("PART_OLDCODE"));
//        Long boQty = Long.valueOf(map.get("boQty") + "");
//
//
//        TtPartRecordPO insertPRPo2 = new TtPartRecordPO();
//        insertPRPo2.setRecordId(recId2);
//        insertPRPo2.setAddFlag(2);//出入库标识
//        insertPRPo2.setPartId(Long.valueOf(partId));
//        insertPRPo2.setPartCode(partCode);
//        insertPRPo2.setPartOldcode(partOldcode);
//        insertPRPo2.setPartName(partCname);
//        insertPRPo2.setPartBatch(partBatch);
//        insertPRPo2.setVenderId(Long.parseLong(partVenId));
//        insertPRPo2.setPartNum(boQty);//出入库数量
//        insertPRPo2.setConfigId(Long.parseLong(configId));
//        insertPRPo2.setOrderId(outId);//变更单ID
//        insertPRPo2.setOrgId(Long.parseLong(orgId));
//        insertPRPo2.setOrgCode(orgCode);
//        insertPRPo2.setOrgName(orgName);
//        insertPRPo2.setWhId(Long.parseLong(whId));
//        insertPRPo2.setWhCode(CommonUtils.checkNull(warehouseMap.get("WH_CODE")));
//        insertPRPo2.setWhName(CommonUtils.checkNull(warehouseMap.get("WH_NAME")));
//        insertPRPo2.setLocId(Long.valueOf(locId));
//        insertPRPo2.setLocCode(CommonUtils.checkNull(locMap.get("LOC_CODE")));
//        insertPRPo2.setLocName(CommonUtils.checkNull(locMap.get("LOC_NAME")));
//        insertPRPo2.setOptDate(new Date());
//        insertPRPo2.setCreateDate(new Date());
//        insertPRPo2.setPersonId(loginUser.getUserId());
//        insertPRPo2.setPersonName(loginUser.getName());
//        insertPRPo2.setPartState(1);//配件状态
//        dao.insert(insertPRPo2);
//
//        List<Object> ins2 = new LinkedList<Object>();
//        //调用出入库逻辑
//        ins2 = new LinkedList<Object>();
//        ins2.add(0, outId);
//        ins2.add(1, configId);
//        ins2.add(2, 0);
//        dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins2, null);
//        /////////////////////////////////////////////////////////
//        TtPartRecordPO insertPRPo = new TtPartRecordPO();
//        insertPRPo.setRecordId(recId);
//        insertPRPo.setAddFlag(1);//出入库标识
//        insertPRPo.setPartId(Long.valueOf(partId));
//        insertPRPo.setPartCode(partCode);
//        insertPRPo.setPartOldcode(partOldcode);
//        insertPRPo.setPartName(partCname);
//        insertPRPo.setPartBatch(partBatch);
//        insertPRPo.setVenderId(Long.parseLong(partVenId));
//        insertPRPo.setPartNum(boQty);//出入库数量
//        insertPRPo.setConfigId(Long.parseLong(configId));
//        insertPRPo.setOrderId(outId);//变更单ID
//        insertPRPo.setOrgId(Long.parseLong(orgId));
//        insertPRPo.setOrgCode(orgCode);
//        insertPRPo.setOrgName(orgName);
//        insertPRPo.setWhId(Long.parseLong(whId));
//        insertPRPo.setWhCode(CommonUtils.checkNull(warehouseMap.get("WH_CODE")));
//        insertPRPo.setWhName(CommonUtils.checkNull(warehouseMap.get("WH_NAME")));
//        insertPRPo.setLocId(Long.valueOf(locId));
//        insertPRPo.setLocCode(CommonUtils.checkNull(locMap.get("LOC_CODE")));
//        insertPRPo.setLocName(CommonUtils.checkNull(locMap.get("LOC_NAME")));
//        insertPRPo.setOptDate(new Date());
//        insertPRPo.setCreateDate(new Date());
//        insertPRPo.setPersonId(loginUser.getUserId());
//        insertPRPo.setPersonName(loginUser.getName());
//        insertPRPo.setPartState(2);//配件状态
//        dao.insert(insertPRPo);
//        List<Object> ins = new LinkedList<Object>();
//        //调用出入库逻辑 释放占用
//        ins = new LinkedList<Object>();
//        ins.add(0, outId);
//        ins.add(1, configId);
//        dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
//    }

    private void reStoreBoStock(TtPartBoDtlPO dtlVo) throws Exception {
        //先插入RECORD
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        PartSoManageDao dao = PartSoManageDao.getInstance();
        PartOutstockDao partOutstockDao = PartOutstockDao.getInstance();
        Long partId = dtlVo.getPartId();
        Long locId = dtlVo.getLocId();

        String whId = CommonUtils.checkNull(CommonUtils.checkNull(request.getAttribute("whId")));
        String sellerId = CommonUtils.checkNull(CommonUtils.checkNull(request.getAttribute("sellerId")));
        Long outId = Long.valueOf(request.getAttribute("recordOrderId") + "");
        Long boQty = dtlVo.getBoQty();
        try {
            TtPartBookRecordPO po = new TtPartBookRecordPO();
            Long recordId = Long.parseLong(SequenceManager.getSequence(""));
            po.setRecordId(recordId);
            po.setPartId(partId);
            po.setWhId(Long.valueOf(whId));
            po.setOrgId(Long.valueOf(sellerId));
            po.setBookedQty(0 - boQty);
            po.setOrderId(outId);
            po.setNormalQty(boQty);
            po.setProcName("现场BO回滚预占资源");
            dao.insert(po);
            partOutstockDao.updateBook(partId + "", locId + "", whId, boQty + "");

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:释放占用的资金
     */
    private void insertAccount(TtPartBoDtlPO ttPartBoDtlpo, Long soId) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            PartPickOrderDao partPickOrderDao = PartPickOrderDao.getInstance();
            PartSoManageDao partSoManageDao = PartSoManageDao.getInstance();
            //TtPartAccountRecordPO po = new TtPartAccountRecordPO();
            Long recordId = Long.parseLong(SequenceManager.getSequence(""));
            Map<String, Object> mainMap = partSoManageDao.getSoOrderMain(soId.toString());
            String discount = CommonUtils.checkNull(mainMap.get("DISCOUNT"));
            Double price = ttPartBoDtlpo.getBuyPrice();
            Double boQty = Double.valueOf(ttPartBoDtlpo.getBoQty());
            if (!"".equals(discount)) {
                price = Arith.mul(price, Double.valueOf(discount));
            }
            Double amount = Arith.mul(boQty, price);

            Map<String, Object> freightMap = (Map) request.getAttribute("freightMap");
            Double freight = Double.valueOf(CommonUtils.checkNull(freightMap.get(soId)) == "" ? "0" : CommonUtils.checkNull(freightMap.get(soId)));
            Double orderFreight = Double.valueOf(CommonUtils.checkNull(mainMap.get("FREIGHT")) == "" ? "0" : CommonUtils.checkNull(mainMap.get("FREIGHT")));
            amount = Arith.add(amount, Arith.sub(freight, orderFreight));//什么意思？
            //po.setRecordId(recordId);
            //po.setChangeType(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_02);
            //po.setDealerId(Long.valueOf(mainMap.get("DEALER_ID").toString()));
            //获取账户余额等seller_id
            Map<String, Object> acountMap = dao.getAccount(mainMap.get("DEALER_ID").toString(), mainMap.get("SELLER_ID").toString(), "");
            if (null != acountMap) {
                //po.setAccountId(Long.valueOf(acountMap.get("ACCOUNT_ID").toString()));
            }

            //po.setAmount((0D - amount));
            //po.setFunctionName("出库生成现场BO单释放");
            if (CommonUtils.checkNull(mainMap.get("SO_FROM")).equals(Constant.CAR_FACTORY_SO_FORM_02 + "") && !"".equals(mainMap.get("ORDER_ID")) && null != (mainMap.get("ORDER_ID"))) {
                //po.setSourceId(Long.valueOf(CommonUtils.checkNull(mainMap.get("ORDER_ID"))));
                //po.setSourceCode(CommonUtils.checkNull(mainMap.get("ORDER_CODE")));
            } else {
                //po.setSourceId(soId);
                //po.setSourceCode(CommonUtils.checkNull(mainMap.get("SO_CODE")));
            }

            Map<String, Object> outMap = partPickOrderDao.getOutMain(soId + "");
            Long outId = Long.valueOf(outMap.get("OUT_ID") + "");
            String outCode = outMap.get("OUT_CODE") + "";
            //po.setOrderId(outId);
            //po.setOrderCode(outCode);
            //po.setCreateBy(loginUser.getUserId());
            //po.setCreateDate(new Date());
            //dao.insert(po); modify by yuan 20130626


            TtPartAccountDefinePO accountDefinePO = new TtPartAccountDefinePO();
            accountDefinePO.setChildorgId(Long.valueOf(mainMap.get("DEALER_ID").toString()));
            accountDefinePO.setParentorgId(Long.valueOf(mainMap.get("SELLER_ID").toString()));
            accountDefinePO.setState(Constant.STATUS_ENABLE);
            accountDefinePO.setStatus(1);

            TtPartAccountDefinePO definePO = (TtPartAccountDefinePO) dao.select(accountDefinePO).get(0);

            TtPartAccountDefinePO definePO1 = new TtPartAccountDefinePO();
            ;
            definePO1.setAccountSum(definePO.getAccountSum() + amount);

            dao.update(accountDefinePO, definePO1);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 修改状态
     */
    public void changeStatus(String pickOrderId, String flag) throws Exception {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        PartPkgDao dao = PartPkgDao.getInstance();
        try {
            //判断当前拣货单下的所有箱子是否全部出库,如果全部出库就更新状态
            TtPartPkgBoxDtlPO boxDtlPO = new TtPartPkgBoxDtlPO();
            boxDtlPO.setPickOrderId(pickOrderId);
            boxDtlPO.setStatus(1);//未出库的箱子
            List list = dao.select(boxDtlPO);

            if (list.size() == 0) {
                //更新销售单状态
                TtPartSoMainPO po = new TtPartSoMainPO();
                po.setState(Constant.CAR_FACTORY_TRANS_STATE_01);
                TtPartSoMainPO oldPo = new TtPartSoMainPO();
                oldPo.setPickOrderId(pickOrderId);
                //更新拣货单状态
                TtPartPickOrderMainPO newTp = new TtPartPickOrderMainPO();
                newTp.setTransDate(new Date());
                newTp.setState(Constant.CAR_FACTORY_TRANS_STATE_01);
                TtPartPickOrderMainPO oldTp = new TtPartPickOrderMainPO();
                oldTp.setPickOrderId(pickOrderId);
                dao.update(oldTp, newTp);
                dao.update(oldPo, po);
            }
            //强制出库更新销售单状态为强制关闭并删除预扣金额
            if ("1".equals(flag)) {
                //强制关闭销售单
                TtPartSoMainPO soMainPO = new TtPartSoMainPO();
                soMainPO.setState(Constant.CAR_FACTORY_SALE_ORDER_STATE_09);
                TtPartSoMainPO soMainPO1 = new TtPartSoMainPO();
                soMainPO1.setPickOrderId(pickOrderId);
                //删除预扣资金
                TtPartAccountRecordPO recordPO = new TtPartAccountRecordPO();
                recordPO.setOrderId(((TtPartSoMainPO) dao.select(soMainPO1).get(0)).getSoId());
                dao.delete(recordPO);
                dao.update(soMainPO1, soMainPO);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 查看
     */
    public void detailOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            PartPickOrderDao dao = PartPickOrderDao.getInstance();
            List<Map<String, Object>> soOrderList = dao.getSoMainList(pickOrderId);
            String state = CommonUtils.checkNull(soOrderList.get(0).get("STATE"));
            act.setOutData("soOrderList", soOrderList);
            act.setForword(PART_OUTSTOCK_ORDER_DETAIL);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "出库查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 导出
     */
    public void exportExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartOutstockDao dao = PartOutstockDao.getInstance();
            PartPlanQueryDao partPlanQueryDao = PartPlanQueryDao.getInstance();
            String[] head = new String[12];
            head[0] = "销售单号 ";
            head[1] = "订货单位";
            head[2] = "制单人";
            head[3] = "销售日期";
            head[4] = "销售单位";
            head[5] = "订单类型";
            head[6] = "销售金额";
            head[7] = "出库仓库";

            PageResult<Map<String, Object>> ps = dao.queryPkgOrder(request, 1, Constant.PAGE_SIZE_MAX);
            Map<String, Object> planTypeMap = partPlanQueryDao.getTcCodeMap(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE.toString());//类型
            List<Map<String, Object>> list = ps.getRecords();
            list = list == null ? new ArrayList() : list;
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[12];
                    detail[0] = CommonUtils.checkNull(map.get("SO_CODE"));
                    detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[2] = CommonUtils.checkNull(map.get("CREATE_BY_NAME"));
                    detail[3] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[4] = CommonUtils.checkNull(map.get("SELLER_NAME"));
                    detail[5] = CommonUtils.checkNull(map.get("ORDER_TYPE") == null ? null : planTypeMap.get(CommonUtils.checkNull(map.get("ORDER_TYPE"))));
                    detail[6] = CommonUtils.checkNull(map.get("AMOUNT"));
                    detail[7] = CommonUtils.checkNull(map.get("WH_NAME"));
                    list1.add(detail);
                }
            }
            this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 保存日志
     */
    public void insertHistory(String pickOrderId, String flag) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartOutstockDao dao = PartOutstockDao.getInstance();

        try {
            TtPartSoMainPO soMainPO = new TtPartSoMainPO();
            soMainPO.setPickOrderId(pickOrderId);
            List<TtPartSoMainPO> list = dao.select(soMainPO);
            for (TtPartSoMainPO soPo : list) {
                TtPartOperationHistoryPO po = new TtPartOperationHistoryPO();
                Long optId = Long.parseLong(SequenceManager.getSequence(""));
                po.setOptId(optId);
                po.setBussinessId(soPo.getSoCode());
                po.setOptBy(logonUser.getUserId());
                po.setOptDate(new Date());
                po.setOptType(Constant.PART_OPERATION_TYPE_02);
                if ("1".equals(flag)) {
                    po.setWhat("整单现场BO强制关闭销售单");
                } else {
                    po.setWhat("配件出库单");
                }
                po.setOptName(logonUser.getName());
                po.setOrderId(soPo.getOrderId());
                po.setStatus(Constant.CAR_FACTORY_OUTSTOCK_STATE_02);
                dao.insert(po);

                TtPartOperationHistoryPO po1 = new TtPartOperationHistoryPO();
                Long optId1 = Long.parseLong(SequenceManager.getSequence(""));
                po1.setOptId(optId1);
                po1.setBussinessId(soPo.getSoCode());
                po1.setOptBy(logonUser.getUserId());
                po1.setOptDate(new Date());
                po1.setOptType(Constant.PART_OPERATION_TYPE_02);
                po1.setWhat("配件发运单");
                po1.setOptName(logonUser.getName());
                po1.setOrderId(soPo.getOrderId());
                po1.setStatus(Constant.CAR_FACTORY_OUTSTOCK_STATE_03);
                dao.insert(po1);
            }

        } catch (Exception ex) {
            throw ex;
        }
    }

    //后修改发运类型为配置！！！
    public Map<String, Object> getTransMap() throws Exception {
        List<TtPartFixcodeDefinePO> list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);
        Map<String, Object> transMap = new HashMap<String, Object>();
        for (TtPartFixcodeDefinePO po : list) {
            //list.fixValue }">${list.fixName
            transMap.put(po.getFixValue(), po.getFixName());
        }
        return transMap;
    }

    public void pickOrderDetail() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String soId = CommonUtils.checkNull(request.getParamValue("soId"));
            PartSoManageDao dao = PartSoManageDao.getInstance();
            Map<String, Object> mainMap = dao.getSoOrderMain(soId);
            List<Map<String, Object>> detailList = dao.getSoOrderMDetail(soId);
            act.setOutData("mainMap", mainMap);
            act.setOutData("detailList", detailList);
            act.setForword(partDetailUrl);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "拣货单查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param dlrId
     * @param selId
     * @param amount
     * @param soCode
     */
    public void updateDlrAccount(String dlrId, String selId, String amount, String soCode) {
        ActionContext act = ActionContext.getContext();

        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        PartSoManageDao dao = PartSoManageDao.getInstance();
        try {
            //初始化资金账号
            PartDlrOrder dlrOrder = new PartDlrOrder();
            dlrOrder.dealerAccoutMng(dlrId, selId);

            TtPartAccountDefinePO srcPO = new TtPartAccountDefinePO();
            srcPO.setChildorgId(Long.valueOf(dlrId));
            srcPO.setParentorgId(Long.valueOf(selId));

            srcPO = (TtPartAccountDefinePO) dao.select(srcPO).get(0);
            ArrayList ins = new ArrayList();
            ins.add(0, Double.valueOf(amount));
            ins.add(1, dlrId);
            ins.add(2, selId);
            String sql = "UPDATE TT_PART_ACCOUNT_DEFINE t SET t.account_sum=t.account_sum-(?) WHERE t.childorg_id=? AND t.parentorg_id=? AND t.state=10011001 AND t.status=1";

            TtPartAccountRecordPO recordPO = new TtPartAccountRecordPO();
            recordPO.setOrderCode(soCode);

            TtPartAccountRecordPO recordPO1 = new TtPartAccountRecordPO();
            recordPO1.setState(Constant.STATUS_DISABLE);
            recordPO1.setStatus(3);
            recordPO1.setRemark("供应中心出库释放预占金额");


            TtPartAccountHistoryPO historyPO = new TtPartAccountHistoryPO();
            historyPO.setHistroryId(Long.valueOf(SequenceManager.getSequence("")));
            historyPO.setChildorgId(Long.valueOf(dlrId));
            historyPO.setParentorgId(Long.valueOf(selId));
            historyPO.setAmount(Double.valueOf(amount));
            historyPO.setAcountKind(Constant.FIXCODE_CURRENCY_01);
            historyPO.setRemark("出库扣除预占金额!");
            historyPO.setCreateDate(new Date());
            historyPO.setAccountId(srcPO.getAccountId());
            historyPO.setCreateBy(logonUser.getUserId());
            historyPO.setState(Constant.STATUS_ENABLE);
            historyPO.setStatus(1);

            dao.update(sql, ins);
            dao.update(recordPO, recordPO1);
            dao.insert(historyPO);
        } catch (Exception e) {
            POContext.endTxn(false);
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "预扣释放错误,请联系管理员!");
            act.setException(e1);
        }

    }

    /**
     * @param srcId  原始订单ID
     * @param amount 订单金额
     */
    public void updateYZAccount(String soCode, Double amount) {
        PartSoManageDao dao = PartSoManageDao.getInstance();
        TtPartAccountRecordPO recordPO = new TtPartAccountRecordPO();
        recordPO.setOrderCode(soCode);

        TtPartAccountRecordPO recordPO1 = new TtPartAccountRecordPO();
        recordPO1.setAmount(amount);

        dao.update(recordPO, recordPO1);

    }

    public void viewOutRepo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {

            act.setOutData("SstartDate", CommonUtils.getMonthFirstDay());
            act.setOutData("SendDate", CommonUtils.getDate());

            act.setForword(PART_DLR_ORDER_OUT_Detail1);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.ADD_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void queryOutRepoDtl() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String dealerId = "";
        PartSoManageDao dao = PartSoManageDao.getInstance();
        try {
            String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));
            String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";

            }

            StringBuffer sql = new StringBuffer();

            if (null != SstartDate && !"".equals(SstartDate)) {
                sql.append(" AND TRUNC(SM.CREATE_DATE) >= TO_DATE('" + SstartDate + "', 'yyyy-mm-dd') ");
            }

            if (null != SendDate && !"".equals(SendDate)) {
                sql.append(" AND TRUNC(SM.CREATE_DATE) <= TO_DATE('" + SendDate + "', 'yyyy-mm-dd') ");
            }
            sql.append("AND SM.SELLER_ID = ").append(dealerId);
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = dao.getOUTSum(sql.toString(), 13, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}
