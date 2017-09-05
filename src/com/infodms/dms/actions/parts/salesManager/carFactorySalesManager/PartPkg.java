package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.claim.laborList.LaborListSummaryReportDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.partPlanQuery.PartPlanQueryDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.salesManager.*;
import com.infodms.dms.dao.sales.storage.sendManage.SendAssignmentDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 拣货单、装箱单管理
 * @author  
 * @version 2017-8-1
 * @see 
 * @since 
 * @deprecated
 */
public class PartPkg extends BaseImport implements PTConstants {
    private Logger logger = Logger.getLogger(PartPkg.class);
    PartPkgDao partPkgDao = PartPkgDao.getInstance();
    /**
     * 拣货单批量打印
     */
    private String batchPrintUrl = "/jsp/parts/salesManager/carFactorySalesManager/partPickOrderBatchPrint.jsp";
    /**
     * 装箱清单
     */
    private String partPkgPrintUrl = "/jsp/parts/salesManager/carFactorySalesManager/partPkgDetailPrint.jsp";
    /**
     * 拣货单明细查询
     */
    private String partPikDtlUrl = "/jsp/parts/salesManager/carFactorySalesManager/partPikDtl.jsp";
    /**
     * 发运单查询
     */
    private String partTransDtlUrl = "/jsp/parts/salesManager/carFactorySalesManager/partTransDtl.jsp";
    /**
     * 装箱明细查询
     */
    private String partPkgDtlUrl = "/jsp/parts/salesManager/carFactorySalesManager/partPkgDtl.jsp";
    /**
     * 未装箱明细查询
     */
    private String partNonePkgDtlUrl = "/jsp/parts/salesManager/carFactorySalesManager/partNonePkgDtl.jsp";
    /**
     * 发运分派查询
     */
    private String partZCPlanQueryDtlUrl = "/jsp/parts/salesManager/carFactorySalesManager/sendAssignmentListQuery.jsp";

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

        String name = null;
        if (CommonUtils.checkNull(request.getParamValue("pickOrderId2")).equals("")) {
            name = CommonUtils.checkNull(request.getParamValue("pickOrderId")) + "配件装箱明细.xls";
        } else {
            name = CommonUtils.checkNull(request.getParamValue("pickOrderId2")) + "配件装箱明细.xls";
        }
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
     * 拣货单-初始化查询
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 配件装箱初始化
     */
    public void partPkgInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orgId = "";
            //获取orgid
            PartWareHouseDao partWareHouseDao =  PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            PurchasePlanSettingDao purchasePlanSettingDao =  PurchasePlanSettingDao.getInstance();
            //仓库查询
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(orgId);
            //获取配件发运方式
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "", "已提交财务");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "", "财务审核通过");
            stateMap.put(Constant.CAR_FACTORY_PKG_STATE_01 + "", "装箱中");
            stateMap.put(Constant.CAR_FACTORY_PKG_STATE_02 + "", "已装箱");
            stateMap.put(Constant.CAR_FACTORY_TRANS_STATE_01 + "", "已发运");
            Map<String, Object> map = new HashMap<String, Object>();
            if (null != act.getSession().get("condition")) {
                if (null != request.getParamValue("flag")) {
                    map = (Map) act.getSession().get("condition");
                    act.getSession().remove("condition");
                } else {
                    act.getSession().remove("condition");
                }
            }
            act.setOutData("condition", map);
            act.setOutData("stateMap", stateMap);
            act.setOutData("transList", list);
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            request.setAttribute("wareHouseList", wareHouseList);
            act.setForword(PART_PKG_MAIN);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 拣货单-查询
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 配件销售查询
     */
    public void partPkgQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = partPkgDao.queryPkgOrder(request, curPage, Constant.PAGE_SIZE);

            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 获取货位
     */
    public void queryPartLocation() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String soId = CommonUtils.checkNull(request.getParamValue("soId"));
            List<Map<String, Object>> list = partPkgDao.queryPartLocation(soId);
            act.setOutData("loc", list);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取配件货位数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
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
            String soId = CommonUtils.checkNull(request.getParamValue("soId"));
            PartSoManageDao dao =  PartSoManageDao.getInstance();
            Map<String, Object> mainMap = dao.getSoOrderMain(soId);
            List<Map<String, Object>> detailList = dao.getSoOrderMDetail(soId);
            List<Map<String, Object>> historyList = dao.queryOrderHistory(CommonUtils.checkNull(mainMap.get("SO_CODE")));
            Map<String, Object> transMap = this.getTransMap();
            String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
            transType = CommonUtils.checkNull(transMap.get(transType));
            mainMap.put("TRANS_TYPE", transType);
            act.setOutData("mainMap", mainMap);
            act.setOutData("detailList", detailList);
            act.setOutData("historyList", historyList);
            act.setForword(PART_DLR_ORDER_FIN_CHECK_DETAIL);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_PKG_DETAIL);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_PKG_DETAIL);
        }

    }

    /**
     * 配件装箱初始化
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartPkgDao
     * @Description: 配件装箱
     */
    public void pkgOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPickOrderDao dao =  PartPickOrderDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));

            List<Map<String, Object>> soOrderList = dao.getSoMainList(pickOrderId);
            Map<String, Object> mainMap = soOrderList.get(0);
            //仓库
            String whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
            String whName = partPkgDao.getWhName(whId);
            mainMap.put("whName", whName);
            mainMap.put("whId", whId);
            //发运方式
            String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
            Map<String, Object> transMap = this.getTransMap();
            transType = CommonUtils.checkNull(transMap.get(transType));
            mainMap.put("TRANS_TYPE", transType);
            
            act.setOutData("mainMap", mainMap);
            act.setOutData("CREATE_BY_NAME", loginUser.getName());
            
            List<Map<String, Object>> list = partPkgDao.queryUnPkgedPartInfo(pickOrderId, whId);

            if (list.size() > 0) {
                act.setOutData("hasDtl", "1");//是否存在装箱的明细
            }
            act.setForword(PART_PKG_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 装箱(包括装箱、完成装箱)
     * 装箱：只装一部分
     * 完成装箱：未装箱的产生现场bo
     */
    public void savePkg() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
        	//保存装箱明细
            createPkgOrder();
            //装箱更新销售信息、完全装箱更新销售、拣货单信息
            changeStatus();
            //添加配件操作记录
            saveHistory();
            
            //createBoOrder();//已经移到出库时产生现场BO
            
            String msg = "";
            String status = CommonUtils.checkNull(request.getParamValue("status"));// 2为完成装箱 1为装箱  CAR_FACTORY_PKG_STATUS_02
            if ("1".equals(status)) {
                msg = "装箱成功!";
            } else if ("2".equals(status)) {
                msg = "完成装箱!";
            }
            
            /*List<String> list = (List<String>) request.getAttribute("pkgIdList");
            String pkgIds = "";
            for (String str : list) {
                pkgIds = pkgIds + "," + str;
            }
            if (!"".equals(pkgIds)) {
                pkgIds = pkgIds.replaceFirst(",", "");
            }
            act.setOutData("pkgIds", pkgIds);*/
            act.setOutData("success", msg);
        } catch (Exception e) {//异常方法
            act.setOutData("error", "装箱失败!");
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "装箱出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 保存装箱单
     * @throws Exception
     */
    private void createPkgOrder() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPickOrderDao partPickOrderDao =  PartPickOrderDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));//当前拣货单
            String hasDtl = CommonUtils.checkNull(request.getParamValue("hasDtl"));//是否有装箱的明细 

            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
            String[] partIdArr = request.getParamValues("cb");
            String pkg_no = CommonUtils.checkNull(request.getParamValue("PKG_NO"));//装箱单号
            String status = CommonUtils.checkNull(request.getParamValue("status"));//2为完成装箱 1为装箱

            //更新装箱起始日期
            partPkgDao.updatePkgBeginDate(pickOrderId);
            
            //如果是完成装箱,就要把所有还未装箱的配件预生成现场bo
            if ("2".equals(status)) {
                List<Map<String, Object>> list = partPkgDao.queryUnPkgedPartInfo(pickOrderId, whId);
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    long partId = ((BigDecimal) map.get("PART_ID")).longValue();
                    long locId = ((BigDecimal) map.get("LOC_ID")).longValue();
                    long salesQty = ((BigDecimal) map.get("SALES_QTY")).longValue();//销售数量
                    long pkgQty = ((BigDecimal) map.get("PKGEDQTY")).longValue();//装箱数量
                    long soId = ((BigDecimal) map.get("SO_ID")).longValue();
                    long slineId = ((BigDecimal) map.get("SLINE_ID")).longValue();
                    String partCname = (String) map.get("PART_CNAME");
                    String partCode = (String) map.get("PART_CODE");
                    String partOldCode = (String) map.get("PART_OLDCODE");
                    String unit = (String) map.get("UNIT");
                    String batchNo=(String) map.get("BATCH_NO");
                    TtPartPkgDtlPO po = new TtPartPkgDtlPO();
                    po.setPkgId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                    po.setPickOrderId(pickOrderId);
                    po.setPartCname(partCname);
                    po.setPartCode(partCode);
                    po.setPartOldcode(partOldCode);
                    po.setPartId(partId);
                    po.setLocId(locId);
                    po.setUnit(unit);
                    po.setCreateBy(loginUser.getUserId());
                    po.setSalesQty(salesQty);
                    po.setCreateDate(new Date());
                    po.setPkgType(Constant.CAR_FACTORY_PKG_TYPE_01);
                    po.setVer(1);
                    po.setSoId(soId);
                    po.setSlineId(slineId);
                    po.setPkgQty(pkgQty);
                    po.setLocBoQty(salesQty - pkgQty);
                    po.setBatchNo(batchNo);

                    partPkgDao.insert(po);
                }
            } else {
            	Long boxId=CommonUtils.parseLong(SequenceManager.getSequence(""));//箱号id
            			
                //判断当前箱号和拣货单是否已经有装箱信息
                TtPartPkgBoxDtlPO boxDtlPO1 = new TtPartPkgBoxDtlPO();
                boxDtlPO1.setPkgNo(pkg_no);
                boxDtlPO1.setPickOrderId(pickOrderId);
                
                //zhumingwei 2017-02-09 判断此拣货单和箱号是否被发运
                String sql ="select * from TT_PART_PKG_BOX_DTL where pick_order_id ="+pickOrderId+" and pkg_no="+pkg_no+" and trplan_id is not null";
                List<TtPartPkgBoxDtlPO> list11 = partPkgDao.select(TtPartPkgBoxDtlPO.class, sql.toString(), null);
                if(list11.size()>0){
                	BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "此箱号:["+pkg_no+"]已成发运计划,请先出库!");
                    throw e1;
                }
                
                //装箱&&无装箱记录，则进行装箱插入记录
                List<TtPartPkgBoxDtlPO> list = partPkgDao.select(boxDtlPO1);
                if ("1".equals(hasDtl) && list.size() == 0) {
                    String box_len = CommonUtils.checkNull(request.getParamValue("BOX_LEN")) == "" ? "0" : CommonUtils.checkNull(request.getParamValue("BOX_LEN"));//包装尺寸(长)
                    String box_wid = CommonUtils.checkNull(request.getParamValue("BOX_WID")) == "" ? "0" : CommonUtils.checkNull(request.getParamValue("BOX_WID"));//包装尺寸(宽)
                    String box_hei = CommonUtils.checkNull(request.getParamValue("BOX_HEI")) == "" ? "0" : CommonUtils.checkNull(request.getParamValue("BOX_HEI"));//包装尺寸(高)
                    String volume = CommonUtils.checkNull(request.getParamValue("VOLUME")) == "" ? "0" : CommonUtils.checkNull(request.getParamValue("VOLUME"));//体积
                    String box_wei = CommonUtils.checkNull(request.getParamValue("BOX_WEI")) == "" ? "0" : CommonUtils.checkNull(request.getParamValue("BOX_WEI"));//单箱重量
                    String ch_weight = CommonUtils.checkNull(request.getParamValue("CH_WEIGHT")) == "" ? "0" : CommonUtils.checkNull(request.getParamValue("CH_WEIGHT"));//计费重量
                    String eq_weight = CommonUtils.checkNull(request.getParamValue("EQ_WEIGHT")) == "" ? "0" : CommonUtils.checkNull(request.getParamValue("EQ_WEIGHT"));//折合重量
                    
                    //保存装箱包装信息
                    TtPartPkgBoxDtlPO boxDtlPO = new TtPartPkgBoxDtlPO();
                    boxDtlPO.setBoxId(boxId);
                    boxDtlPO.setPickOrderId(pickOrderId);
                    boxDtlPO.setPkgNo(pkg_no);
                    boxDtlPO.setLength(CommonUtils.parseDouble(box_len));
                    boxDtlPO.setWidth(CommonUtils.parseDouble(box_wid));
                    boxDtlPO.setHeight(CommonUtils.parseDouble(box_hei));
                    boxDtlPO.setWeight(CommonUtils.parseDouble(box_wei));
                    boxDtlPO.setVolume(CommonUtils.parseDouble(volume));
                    boxDtlPO.setEqWeight(CommonUtils.parseDouble(eq_weight));
                    boxDtlPO.setChWeight(CommonUtils.parseDouble(ch_weight));
                    boxDtlPO.setCreateDate(new Date());
                    boxDtlPO.setCreateBy(loginUser.getUserId());

                    partPkgDao.insert(boxDtlPO);
                }

                List<TtPartPkgDtlPO> normalList = new ArrayList<TtPartPkgDtlPO>();
                //遍历前端传入的装箱信息
                if (partIdArr != null && partIdArr.length > 0 && !"".equals(partIdArr[0])) {
                    for (int i = 0; i < partIdArr.length; i++) {
                        String partId = partIdArr[i].split(",")[0];//配件id
                        String locId = partIdArr[i].split(",")[1];//货位id
                        //查询货位信息
                        TtPartLoactionDefinePO locationDefinePO = new TtPartLoactionDefinePO();
                        locationDefinePO.setLocId(CommonUtils.parseLong(locId));
                        locationDefinePO = (TtPartLoactionDefinePO) partPkgDao.select(locationDefinePO).get(0);
                        String locName = locationDefinePO.getLocCode();

//                        String partCode = "";
//                        String partOldCode = "";
//                        String partCname = "";
//                        String remark = "";
//                        String unit = "";
//                        String pkgType = "";
//                        Long salesQty = 0l;
//                        Long pkgQty = 0l;

                        long soId = 0l;
                        long slineId = 0l;

                        String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE" + partIdArr[i]));
                        String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE" + partIdArr[i]));
                        String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME" + partIdArr[i]));
                        String remark = CommonUtils.checkNull(request.getParamValue("remark_" + partIdArr[i]));
                        String pkgType = CommonUtils.checkNull(request.getParamValue("pkgType_" + partIdArr[i]));
                        String unit = request.getParamValue("UNIT" + partIdArr[i]);
                        Long salesQty = Long.valueOf(CommonUtils.parseLong(request.getParamValue("salQty_" + partIdArr[i])));//总销售数量
                        Long pkgQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("pkgQty_" + partIdArr[i])));//装箱数量
                        String batchNo = CommonUtils.checkNull(request.getParamValue("BATCHNO_" + partIdArr[i]));//批次号
                        //根据拣货号、仓库、配件id、货位id获取订单信息
                        List<Map<String, Object>> dtlList = partPickOrderDao.getPartGroup(pickOrderId, whId, partId, locId,batchNo);
                        if (dtlList != null && dtlList.size() > 0) {
                            Long pkgedQty = Long.valueOf(dtlList.get(0).get("PKGEDQTY") == null ? "0" : CommonUtils.checkNull(dtlList.get(0).get("PKGEDQTY")));
                            if (pkgQty > (salesQty - pkgedQty)) {
                                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "第" + (i + 1) + "行，批次"+batchNo+"中的配件，货位" + locName + "中的配件" + partOldCode + "，已装箱数量为" + pkgedQty + ",本次装箱数量不能大于" + (salesQty - pkgedQty) + "!");
                                throw e1;
                            }
                            soId = ((BigDecimal) dtlList.get(0).get("SO_ID")).longValue();
                            slineId = ((BigDecimal) dtlList.get(0).get("SLINE_ID")).longValue();
                        }

                        //判断当前货位中的配件对应的箱号是否已经存在
                        Map<String, Object> map = partPkgDao.isExist(partId, locId, pkg_no, pickOrderId);
                        if (map != null) {
                        	//如果已经存在就更新装箱数量
                            Long pkgId = ((BigDecimal) map.get("PKG_ID")).longValue();
                            partPkgDao.updatePkgQty(pkgId, pkgQty);
                        } else {
                        	//否则新增
                            //新增时需要验证当前拣货单中的配件在占用明细中是否存在,若存在就继续往下执行,否则不执行
                            boolean flag = partPkgDao.isExistBookDtl(pickOrderId, partId, locId,batchNo);
                            
                            if (flag) {
                                TtPartPkgDtlPO po = new TtPartPkgDtlPO();
                                po.setPickOrderId(pickOrderId);
                                po.setPartCname(partCname);
                                po.setPartCode(partCode);
                                po.setPartOldcode(partOldCode);
                                po.setPartId(Long.valueOf(partId));
                                po.setLocId(Long.valueOf(locId));
                                po.setUnit(unit);
                                po.setRemark(remark);
                                po.setCreateBy(loginUser.getUserId());
                                po.setPkgType(Integer.valueOf(pkgType));
                                po.setSalesQty(Long.valueOf(salesQty));
                                po.setCreateDate(new Date());
                                po.setVer(1);
                                po.setSoId(soId);
                                po.setSlineId(slineId);
                                po.setPkgQty(pkgQty);
                                po.setBoxId(boxId);
                                po.setBatchNo(batchNo);//20170828 add
                                normalList.add(po);
                            }
                        }
                    }
                    //保存装箱明细
                    if (normalList.size() > 0) {
                        insertPkgDtl("1", normalList, whId,status,pkg_no);
                    }
                }
            }
        } catch (Exception e) {//异常方法
            throw e;
        }
    }

    /**
     * 保存装箱明细
     * @param CODE为1  是正常装箱的  CODE为2的是有最小装箱量的
     * @param dtlList
     * @param whId
     * @param status 1装箱 2完全装箱
     * @param pkg_no 箱号
     * @throws Exception
     */
    private void insertPkgDtl(String code, List<TtPartPkgDtlPO> dtlList, String whId,String status,String pkg_no) throws Exception {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        PartPkgDao dao = PartPkgDao.getInstance();
        try {
            for (TtPartPkgDtlPO po : dtlList) {
                if (code.equals("1")) {
                    Long pkgId = Long.parseLong(SequenceManager.getSequence(""));
                    po.setPkgId(pkgId);
                    po.setPkgNo(pkg_no);
                    dao.insert(po);
                } else if (code.equals("2")) {
                    Map<String, Object> locMap = dao.queryPartLocationByPartId(po.getPartId() + "", whId);
                    //最小包装箱量
                    Long minPkg = Long.valueOf(CommonUtils.checkNull(locMap.get("MIN_PKG")));
                    //装箱的数量是不能小于最小包装箱量的
                    if (po.getPkgQty() < minPkg) {
                        if ("1".equals(status)) {
                            continue;
                        }
                    }
                    long pkgQty = po.getPkgQty();
                    long temp = pkgQty % Long.valueOf(CommonUtils.checkNull(locMap.get("MIN_PKG")));
                    long tempBoxNo = (pkgQty - temp) / Long.valueOf(CommonUtils.checkNull(locMap.get("MIN_PKG")));  //获得整除后的箱数转换INT
                    int boxNo = Integer.valueOf(tempBoxNo + "");

                    for (int i = 0; i < boxNo; i++) {
                        Long pkgId = Long.parseLong(SequenceManager.getSequence(""));
                        po.setPkgId(pkgId);
                        po.setPkgNo(pkg_no);
                        dao.insert(po);
                    }
                    if ("2".equals(status) && boxNo == 0) {
                        Long pkgId = Long.parseLong(SequenceManager.getSequence(""));
                        po.setPkgId(pkgId);
                        po.setPkgNo(pkg_no);
                        po.setPkgQty(Long.valueOf(pkgQty));
                        dao.insert(po);
                    }
                }
            }
        } catch (Exception ex) {
            throw ex;
        }

    }

    //修改状态
    private void changeStatus() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String status = CommonUtils.checkNull(request.getParamValue("status"));//2为完成装箱 1为装箱
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            if ("2".equals(status)) {
            	//更新销售单状态
                PartPkgDao dao = PartPkgDao.getInstance();
                TtPartSoMainPO oldPo = new TtPartSoMainPO();
                oldPo.setPickOrderId(pickOrderId);
                TtPartSoMainPO newPo = new TtPartSoMainPO();
                newPo.setPickOrderId(pickOrderId);
                newPo.setState(Constant.CAR_FACTORY_PKG_STATE_02);
                newPo.setPkgBy(loginUser.getUserId());
                newPo.setPackgDate(new Date());
                newPo.setPkgOverDate(new Date());
                dao.update(oldPo, newPo);
                
                //更新拣货单状态
                String num = dao.getPkgBoxNum(pickOrderId);
                TtPartPickOrderMainPO oldPickOrder = new TtPartPickOrderMainPO();
                TtPartPickOrderMainPO newPickOrder = new TtPartPickOrderMainPO();
                newPickOrder.setPickOrderId(pickOrderId);
                newPickOrder.setPkgBy(loginUser.getName());
                newPickOrder.setPkgNum(Integer.valueOf(num));
                oldPickOrder.setPickOrderId(pickOrderId);
                dao.update(oldPickOrder, newPickOrder);
            } else if ("1".equals(status)) {
            	//更新销售单状态
                PartPkgDao dao = PartPkgDao.getInstance();
                TtPartSoMainPO oldPo = new TtPartSoMainPO();
                oldPo.setPickOrderId(pickOrderId);
                TtPartSoMainPO newPo = new TtPartSoMainPO();
                newPo.setPickOrderId(pickOrderId);
                newPo.setPkgBy(loginUser.getUserId());
                newPo.setPackgDate(new Date());
                newPo.setState(Constant.CAR_FACTORY_PKG_STATE_01);
                dao.update(oldPo, newPo);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void queryBoxInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String pkgNo = CommonUtils.checkNull(request.getParamValue("pkgNo"));
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));

            Map<String, Object> boxDtlMap = partPkgDao.queryBoxInfo(pkgNo);
            Map<String, Object> dlrPkgNoMap = partPkgDao.queryDealerPKGNos(dealerId, pkgNo);
//            String para = CommonDAO.getPara("60041001");//是否校验箱号开关
//            act.setOutData("isOpen", para.toString());
            
            if (dlrPkgNoMap != null) {
                act.setOutData("flag", 1);
            }

            if (boxDtlMap != null) {
                act.setOutData("boxLen", boxDtlMap.get("LENGTH"));
                act.setOutData("boxWid", boxDtlMap.get("WIDTH"));
                act.setOutData("boxHei", boxDtlMap.get("HEIGHT"));
                act.setOutData("boxWei", boxDtlMap.get("WEIGHT"));
                act.setOutData("boxVol", boxDtlMap.get("VOLUME"));
                act.setOutData("boxEQWei", boxDtlMap.get("EQ_WEIGHT"));
                act.setOutData("boxCHWei", boxDtlMap.get("CH_WEIGHT"));
                act.setOutData("pkgNo", boxDtlMap.get("PKG_NO"));
                act.setOutData("outId", boxDtlMap.get("OUT_ID"));
                act.setOutData("pickOrderId", boxDtlMap.get("PICK_ORDER_ID"));
            } else {
                act.setOutData("outId", "");
                act.setOutData("pkgNo", "");
            }
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:生成BO的集合
     */
    private void loadBoVo(Map<String, Object> voMap) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        Map<String, Object> dataMap = (Map) request.getAttribute("dataMap");
        Map<String, Object> mainMap = (Map) request.getAttribute("mainMap");
        if (voMap == null || null == voMap.get("mainVo")) {
            //判断主表VO是否生成
            String orderId = CommonUtils.checkNull(mainMap.get("ORDER_ID"));
            Long boId = Long.parseLong(SequenceManager.getSequence(""));
            String boCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_21);
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode")); //订单CODE
            String soId = CommonUtils.checkNull(mainMap.get("SO_ID"));
            TtPartBoMainPO po = new TtPartBoMainPO();
            po.setBoId(boId);
            po.setBoType("2");
            po.setBoCode(boCode);
            po.setSoId(Long.valueOf(soId));
            po.setOrderCode(orderCode);
            if (!"".equals(orderId)) {
                po.setOrderId(Long.valueOf(orderId));
            }
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            po.setState(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_01);
            po.setVer(1);
            voMap.put("mainVo", po);
        }
        if (voMap == null || null == voMap.get("detailVoList")) {
            List<TtPartBoDtlPO> list = new ArrayList();
            voMap.put("detailVoList", list);
        }
        Map<String, Object> partMap = (Map) request.getAttribute("dataMap");
        Long bolineId = Long.parseLong(SequenceManager.getSequence(""));
        Long boId = ((TtPartBoMainPO) voMap.get("mainVo")).getBoId();
        String partId = CommonUtils.checkNull(dataMap.get("PART_ID"));
        String partCode = CommonUtils.checkNull(dataMap.get("PART_CODE"));
        String partOldCode = CommonUtils.checkNull(dataMap.get("PART_OLDCODE"));
        String partCname = CommonUtils.checkNull(dataMap.get("PART_CNAME"));
        String salesQty = CommonUtils.checkNull(dataMap.get("salesQty"));
        String unit = CommonUtils.checkNull(dataMap.get("UNIT"));
        Double price = Double.valueOf(CommonUtils.checkNull(dataMap.get("BUY_PRICE").toString()));
        TtPartBoDtlPO po = new TtPartBoDtlPO();
        po.setBolineId(bolineId);
        po.setBoId(boId);
        po.setPartId(Long.valueOf(partId));
        po.setPartCode(partCode);
        po.setPartOldcode(partOldCode);
        po.setBuyPrice(price);
        po.setPartCname(partCname);
        po.setUnit(unit);
        po.setBuyQty(Long.valueOf(CommonUtils.checkNull(partMap.get("SALES_QTY"))));
        po.setSalesQty(Long.valueOf(salesQty));
        po.setBoQty(Long.valueOf(CommonUtils.checkNull(partMap.get("boQty"))));
        po.setCreateBy(loginUser.getUserId());
        po.setCreateDate(new Date());
        ((List) voMap.get("detailVoList")).add(po);
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:生成BO单
     */
    private void createBoOrder() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao =  PartDlrOrderDao.getInstance();
        //存在BO
        if (request.getAttribute("boMap") != null) {
            Map<String, Object> boMap = (Map<String, Object>) request.getAttribute("boMap");
            TtPartBoMainPO ttPartBoMainPO = (TtPartBoMainPO) boMap.get("mainVo");
            List<TtPartBoDtlPO> detailVoList = (List<TtPartBoDtlPO>) boMap.get("detailVoList");
            dao.insert(ttPartBoMainPO);
            for (int i = 0; i < detailVoList.size(); i++) {
                dao.insert(detailVoList.get(i));
                insertAccount(detailVoList.get(i), ttPartBoMainPO.getSoId());
            }
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:插入BO单详细表
     */
    private void insertBoDetail() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        String soId = CommonUtils.checkNull(request.getParamValue("soId"));
        try {
            PartDlrOrderDao dao =  PartDlrOrderDao.getInstance();
            String partId = CommonUtils.checkNull(request.getAttribute("boPartId"));
            String pkgQty = CommonUtils.checkNull(request.getAttribute("boQty"));
            String salesQty = CommonUtils.checkNull(request.getAttribute("salesQty"));
            String unit = CommonUtils.checkNull(request.getAttribute("unit"));
            String partCname = CommonUtils.checkNull(request.getAttribute("partCname"));
            String partCode = CommonUtils.checkNull(request.getAttribute("partCode"));
            String partOldCode = CommonUtils.checkNull(request.getAttribute("partOldCode"));
            Long boId = Long.valueOf(CommonUtils.checkNull(request.getAttribute("boId")));
            Long bolineId = Long.parseLong(SequenceManager.getSequence(""));
            TtPartBoDtlPO po = new TtPartBoDtlPO();
            po.setBolineId(bolineId);
            po.setBoId(boId);
            po.setPartId(Long.valueOf(partId));
            po.setPartCode(partCode);
            po.setPartOldcode(partOldCode);
            po.setPartCname(partCname);
            po.setUnit(unit);
            po.setSalesQty(Long.valueOf(salesQty));
            po.setBoQty(Long.valueOf(pkgQty));
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            dao.insert(po);
        } catch (Exception ex) {
            throw ex;
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
            PartDlrOrderDao dao =  PartDlrOrderDao.getInstance();
            PartSoManageDao partSoManageDao =  PartSoManageDao.getInstance();
            TtPartAccountRecordPO po = new TtPartAccountRecordPO();
            Long recordId = Long.parseLong(SequenceManager.getSequence(""));
            Map<String, Object> mainMap = partSoManageDao.getSoOrderMain(soId.toString());
            String discount = CommonUtils.checkNull(mainMap.get("DISCOUNT"));
            Double price = ttPartBoDtlpo.getBuyPrice();
            Double boQty = Double.valueOf(ttPartBoDtlpo.getBuyQty());
            if (!"".equals(discount)) {
                price = Arith.mul(price, Double.valueOf(discount));
            }
            Double amount = Arith.mul(boQty, price);

            po.setRecordId(recordId);
            po.setChangeType(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_02);
            po.setDealerId(Long.valueOf(mainMap.get("DEALER_ID").toString()));
            //获取账户余额等
            Map<String, Object> acountMap = dao.getAccount(mainMap.get("DEALER_ID").toString(), mainMap.get("SELLER_ID").toString(), "");
            if (null != acountMap) {
                po.setAccountId(Long.valueOf(acountMap.get("ACCOUNT_ID").toString()));
            }

            po.setAmount((0D - amount));
            po.setFunctionName("装箱生成现场BO单释放");
            po.setOrderId(soId);
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            dao.insert(po);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 配件销售查询
     */
    public void exportExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartPlanQueryDao partPlanQueryDao =  PartPlanQueryDao.getInstance();
            String[] head = new String[12];
            head[0] = "销售单号 ";
            head[1] = "订货单位";
            head[2] = "制单人";
            head[3] = "销售日期";
            head[4] = "销售单位";
            head[5] = "订单类型";
            head[6] = "销售金额";
            head[7] = "出库仓库";
            PageResult<Map<String, Object>> ps = partPkgDao.queryPkgOrder(request, 1, Constant.PAGE_SIZE_MAX);
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
     * @Description: 保存历史记录
     */
    public void saveHistory() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao =  PartDlrOrderDao.getInstance();
        PartPickOrderDao partPickOrderDao =  PartPickOrderDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String status = CommonUtils.checkNull(request.getParamValue("status"));// 

            List<Map<String, Object>> soOrderList = partPickOrderDao.getSoMainList(pickOrderId);
            for (Map<String, Object> map : soOrderList) {
                TtPartOperationHistoryPO po = new TtPartOperationHistoryPO();
                Long optId = Long.parseLong(SequenceManager.getSequence(""));
                po.setBussinessId(CommonUtils.checkNull(map.get("SO_CODE")));
                po.setOptId(optId);
                po.setOptBy(loginUser.getUserId());
                po.setOptDate(new Date());
                po.setOptType(Constant.PART_OPERATION_TYPE_02);
                po.setWhat("配件装箱单");
                po.setOptName(loginUser.getName());
                if (!"".equals(CommonUtils.checkNull(map.get("ORDER_ID")))) {
                    po.setOrderId(Long.valueOf(CommonUtils.checkNull(map.get("ORDER_ID"))));
                }
                if ("2".equals(status)) {
                    po.setStatus(Constant.CAR_FACTORY_PKG_STATE_02);
                } else {
                    po.setStatus(Constant.CAR_FACTORY_PKG_STATE_01);
                }
                dao.insert(po);
            }

        } catch (Exception ex) {
            throw ex;
        }
    }

    public void validatePrintData() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String[] soIdArr = CommonUtils.checkNull(request.getParamValue("cbAr")).split(",");
            boolean flag = false;
            String whId = "";
            String whName = "";
            String dealerId = "";
            String transType = "";
            if (null != soIdArr) {
                String soIds = "";
                for (int i = 0; i < soIdArr.length; i++) {
                    String soId = soIdArr[i];
                    soIds = soIds + "," + soId;
                }
                if (!"".equals(soIds)) {
                    soIds = soIds.replaceFirst(",", "");
                }
                List<Map<String, Object>> list = partPkgDao.getSoMainGroup(soIds);
                if (list.size() == 1) {
                    flag = true;
                    whId = CommonUtils.checkNull(list.get(0).get("WH_ID"));
                    dealerId = CommonUtils.checkNull(list.get(0).get("DEALER_ID"));
                    transType = CommonUtils.checkNull(list.get(0).get("TRANS_TYPE"));

                }
            }
            act.setOutData("flag", flag);
            act.setOutData("whId", whId);
            act.setOutData("dealerId", dealerId);
            act.setOutData("transType", transType);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @Title : 打印随车装箱单
     */
    public void partCarConfirmPrint() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        LaborListSummaryReportDao laborListSummaryReportDao = LaborListSummaryReportDao.getInstance();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String assNo = CommonUtils.checkNull(request.getParamValue("assNo"));
            List<Map<String, Object>> soMainList = partPkgDao.getSoMain(pickOrderId);
            partPkgDao.updateTt_part_pkg_box_dtl(assNo, pickOrderId, loginUser.getUserId() + "");//更新随车装箱单打印次数
            List<Object> detailList = new ArrayList<Object>();

            if (soMainList != null) {
                Double totalAmount = 0D;
                String remark = "";
                String soIds = "";
                String transType = "";
                String whId = "";
                String dealerName = "";
                String dealerCode = "";
                String phone = "";
                String orderType = "";
                for (int i = 0; i < soMainList.size(); i++) {
                    Map<String, Object> mainMap = soMainList.get(i);
                    String soId = CommonUtils.checkNull(mainMap.get("SO_ID"));
                    totalAmount = Arith.add(totalAmount, Double.valueOf(mainMap.get("AMOUNT") + ""));
                    remark += CommonUtils.checkNull(mainMap.get("REMARK2"));
                    soIds = soIds + "," + soId;
                    whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
                    transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
                    dealerName = CommonUtils.checkNull(mainMap.get("DEALER_NAME"));
                    dealerCode = CommonUtils.checkNull(mainMap.get("DEALER_CODE"));
                    phone = CommonUtils.checkNull(mainMap.get("TEL"));
                    orderType = CommonUtils.checkNull(mainMap.get("CODE_DESC"));
                }
                String whName = partPkgDao.getWhName(whId);
                if (!"".equals(soIds)) {
                    soIds = soIds.replaceFirst(",", "");
                }
                String chineseAmount = laborListSummaryReportDao.numToChinese(totalAmount + "");
                transType = partPkgDao.getTranTypeName(transType);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                dataMap.put("phone", phone);
                dataMap.put("transType", transType);
                dataMap.put("whName", whName);
                dataMap.put("pickOrderId", pickOrderId);
                dataMap.put("remark", remark);
                dataMap.put("dealerName", dealerName);
                dataMap.put("dealerCode", dealerCode);
                dataMap.put("totalAmount", totalAmount);
                dataMap.put("userName", loginUser.getName());
                dataMap.put("date", sdf.format(new Date()));
                dataMap.put("createDate", sdf.format(new Date()));//获取当前日期
                dataMap.put("creater", loginUser.getName());//取当前操作者姓名
                dataMap.put("orderType", orderType);
                dataMap.put("soIds", soIds);
                dataMap.put("chineseAmount", chineseAmount);
                List<Map<String, Object>> tempDetailList = partPkgDao.getDtlSuiche(pickOrderId, assNo);
                detailList.add(tempDetailList);
                dataMap.put("detailList", detailList);

                List list = new ArrayList();
                List tempList = new ArrayList();
                int listAcount = 0;
                int dtlListAct = tempDetailList.size() % 20;//用于确定最后一页明细行数
                act.setOutData("dtlListAct", dtlListAct);
                //目前不分页
                if (tempDetailList.size() > 20) {
                    for (int i = 0; i < tempDetailList.size(); i++) {
                        tempList.add(tempDetailList.get(i));
                        if (tempList.size() == 20) {
                            list.add(tempList);
                            tempList = new ArrayList();
                        }
                        if (i == tempDetailList.size() - 1) {
                            list.add(tempList);
                        }
                    }
                    dataMap.put("detailList", list);
                }
                if (list.size() == 0) {
                    act.setOutData("listAcount", list.size() + 1);
                } else {
                    act.setOutData("listAcount", list.size());
                }
            }
            act.setOutData("dataMap", dataMap);
            act.setForword(PART_CAR_CFM_PRINT);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "拣货单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 打印拣货单
     * 打印明细，包括批次、货位、数量
     */
    public void opPrintHtml() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        LaborListSummaryReportDao laborListSummaryReportDao = LaborListSummaryReportDao.getInstance();//金额转中文
        Map<String, Object> dataMap = new HashMap();
        try {
        	//拣货单id
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            //更新拣货次数
            List<Map<String, Object>> soMainList = partPkgDao.getSoMain(pickOrderId);
            partPkgDao.updatePickInfo(pickOrderId);

            if (soMainList != null) {
                Double totalAmount = 0D;
                String remark = "";
                String soIds = "";
                String soCodes = "";
                String transType = "";
                String whId = "";
                String dealerName = "";
                String dealerCode = "";
                String phone = "";
//                String createDate = "";
//                String creater = "";
                String orderType = "";
                String orderCodes = "";
                String companyName = "";
                String addr = "";
                for (int i = 0; i < soMainList.size(); i++) {
                    Map<String, Object> mainMap = soMainList.get(i);
                    String soId = CommonUtils.checkNull(mainMap.get("SO_ID"));
                    totalAmount = Arith.add(totalAmount, Double.valueOf(mainMap.get("AMOUNT") + ""));
                    remark += CommonUtils.checkNull(mainMap.get("REMARK2"));
                    soIds = soIds + "," + soId;
//                    soCodes = soCodes + "," + CommonUtils.checkNull(mainMap.get("SO_CODE")).substring(11, CommonUtils.checkNull(mainMap.get("SO_CODE")).length());
                    whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
                    transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
                    dealerName = CommonUtils.checkNull(mainMap.get("DEALER_NAME"));
                    dealerCode = CommonUtils.checkNull(mainMap.get("DEALER_CODE"));
                    phone = CommonUtils.checkNull(mainMap.get("TEL"));
                    addr = CommonUtils.checkNull(mainMap.get("ADDR"));
//                    createDate = CommonUtils.checkNull(mainMap.get("CREATE_DATE_FM"));
//                    creater = CommonUtils.checkNull(mainMap.get("NAME"));
                    orderType = CommonUtils.checkNull(mainMap.get("CODE_DESC"));
                    String orderCode = CommonUtils.checkNull(mainMap.get("ORDER_CODE"));
                    orderCodes = orderCodes + "," + orderCode;
                }
                String whName = partPkgDao.getWhName(whId);
                if (!"".equals(soIds)) {
                    soIds = soIds.replaceFirst(",", "");
                }
                if (!"".equals(soCodes)) {
                    soCodes = soCodes.replaceFirst(",", "");
                }
                if (!"".equals(orderCodes)) {
                    orderCodes = orderCodes.replaceFirst(",", "");
                }
                if (loginUser.getDealerId() == null) {
                    TmCompanyPO tmCompanyPO = new TmCompanyPO();
                    tmCompanyPO.setCompanyType(Integer.valueOf(Constant.COMPANY_TYPE_SGM));
                    companyName = ((TmCompanyPO) partPkgDao.select(tmCompanyPO).get(0)).getCompanyName();
                } else {
                    TmDealerPO tmDealerPO = new TmDealerPO();
                    tmDealerPO.setDealerId(Long.valueOf(loginUser.getDealerId()));
                    companyName = ((TmDealerPO) partPkgDao.select(tmDealerPO).get(0)).getDealerName();
                }
                //获取发运方式
                transType = partPkgDao.getTranTypeName(transType);
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                dataMap.put("phone", phone);
                dataMap.put("transType", transType);
                dataMap.put("whName", whName);
                dataMap.put("pickOrderId", pickOrderId);
                dataMap.put("remark", remark);
                dataMap.put("soCodes", soCodes);
                dataMap.put("dealerName", dealerName);
                dataMap.put("dealerCode", dealerCode);
                dataMap.put("totalAmount", totalAmount);
                dataMap.put("userName", loginUser.getName());
                dataMap.put("date", sdf.format(new Date()));
                dataMap.put("createDate", sdf.format(new Date()));//获取当前日期
                dataMap.put("creater", loginUser.getName());//取当前操作者姓名
                dataMap.put("orderType", orderType);
                dataMap.put("soIds", soIds);
                dataMap.put("orderCode", orderCodes);
                dataMap.put("addr", addr);
                dataMap.put("companyName", companyName);
                dataMap.put("chineseAmount", laborListSummaryReportDao.numToChinese(totalAmount + ""));//金额大小写转换
                //拣货单明细查询
                List<Map<String, Object>> tempDetailList = partPkgDao.getDtlLoc(soIds, whId);

                List detailList = new ArrayList();
                detailList.add(tempDetailList);
                
                dataMap.put("detailList", detailList);
                List list = new ArrayList();
                List tempList = new ArrayList();
                int listAcount = 0;
                int dtlListAct = tempDetailList.size() % 20;//用于确定最后一页明细行数
                act.setOutData("dtlListAct", dtlListAct);
                //目前不分页
                if (tempDetailList.size() > 20) {
                    for (int i = 0; i < tempDetailList.size(); i++) {
                        tempList.add(tempDetailList.get(i));
                        if (tempList.size() == 20) {
                            list.add(tempList);
                            tempList = new ArrayList();
                        }
                        if (i == tempDetailList.size() - 1) {
                            list.add(tempList);
                        }
                    }
                    dataMap.put("detailList", list);
                }
                if (list.size() == 0) {
                    act.setOutData("listAcount", list.size() + 1);
                } else {
                    act.setOutData("listAcount", list.size());
                }
            }
            act.setOutData("dataMap", dataMap);
//            Runtime.getRuntime().exec("d:\\wkhtmltopdf\\bin\\wkhtmltopdf.exe http://localhost:8080/BQYXDMS/parts/salesManager/carFactorySalesManager/PartPkg/opPrintHtml.do?pickOrderId=140400001 cc.pdf");
            act.setForword(PART_PKG_PRINT);
//            Runtime.getRuntime().exec("calc");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "拣货单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-12-23
     * @Title : 打印装箱清单
     */
    public void opPkgDtlPrintHtml() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        //PartSoManageDao dao = new PartSoManageDao();
        //PartDlrOrderDao partDlrOrderDao = new PartDlrOrderDao();
        LaborListSummaryReportDao laborListSummaryReportDao = LaborListSummaryReportDao.getInstance();
        Map<String, Object> dataMap = new HashMap();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String pkgNos = CommonUtils.checkNull(request.getParamValue("pkgNos"));
            partPkgDao.updatePkgPrintInfo(pickOrderId, pkgNos);
            List<Map<String, Object>> soMainList = partPkgDao.getSoMainByPkgNo(pickOrderId, pkgNos);

            List detailList = new ArrayList();

            if (soMainList != null) {
                Double totalAmount = 0D;
                String remark = "";
                String soIds = "";
                String soCodes = "";
                String transType = "";
                String whId = "";
                String dealerName = "";
                String dealerCode = "";
                String phone = "";
                String createDate = "";
                String creater = "";
                String orderType = "";
                String addr = "";
                String companyName = "";
                String orderCodes = "";
                for (int i = 0; i < soMainList.size(); i++) {
                    Map<String, Object> mainMap = soMainList.get(i);
                    String soId = CommonUtils.checkNull(mainMap.get("SO_ID"));
//                    totalAmount = Arith.add(totalAmount, Double.valueOf(mainMap.get("AMOUNT")+""));
                    remark += CommonUtils.checkNull(mainMap.get("REMARK2"));
                    soIds = soIds + "," + soId;
                    //soCodes = soCodes + "," + CommonUtils.checkNull(mainMap.get("SO_CODE")).substring(11, CommonUtils.checkNull(mainMap.get("SO_CODE")).length());
                    whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
                    transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
                    dealerName = CommonUtils.checkNull(mainMap.get("DEALER_NAME"));
                    dealerCode = CommonUtils.checkNull(mainMap.get("DEALER_CODE"));
                    phone = CommonUtils.checkNull(mainMap.get("TEL"));
                    createDate = CommonUtils.checkNull(mainMap.get("CREATE_DATE_FM"));
                    creater = CommonUtils.checkNull(mainMap.get("NAME"));
                    orderType = CommonUtils.checkNull(mainMap.get("CODE_DESC"));
                    addr = CommonUtils.checkNull(mainMap.get("ADDR"));
                    String orderCode = CommonUtils.checkNull(mainMap.get("ORDER_CODE"));
                    orderCodes = orderCodes + "," + orderCode;
                }
                String whName = partPkgDao.getWhName(whId);
                if (!"".equals(soIds)) {
                    soIds = soIds.replaceFirst(",", "");
                }
                if (!"".equals(soCodes)) {
                    soCodes = soCodes.replaceFirst(",", "");
                }
                if (!"".equals(orderCodes)) {
                    orderCodes = orderCodes.replaceFirst(",", "");
                }
                if (loginUser.getDealerId() == null) {
                    TmCompanyPO tmCompanyPO = new TmCompanyPO();
                    tmCompanyPO.setCompanyType(Integer.valueOf(Constant.COMPANY_TYPE_SGM));
                    companyName = ((TmCompanyPO) partPkgDao.select(tmCompanyPO).get(0)).getCompanyName();
                } else {
                    TmDealerPO tmDealerPO = new TmDealerPO();
                    tmDealerPO.setDealerId(Long.valueOf(loginUser.getDealerId()));
                    companyName = ((TmDealerPO) partPkgDao.select(tmDealerPO).get(0)).getDealerName();

                }
                transType = partPkgDao.getTranTypeName(transType);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                dataMap.put("phone", phone);
                dataMap.put("transType", transType);
                dataMap.put("whName", whName);
                dataMap.put("pickOrderId", pickOrderId);
                dataMap.put("remark", remark);
                dataMap.put("soCodes", soCodes);
                dataMap.put("dealerName", dealerName);
                dataMap.put("dealerCode", dealerCode);
                dataMap.put("userName", loginUser.getName());
                dataMap.put("date", sdf.format(new Date()));
                dataMap.put("createDate", createDate);
                dataMap.put("creater", creater);
                dataMap.put("orderType", orderType);
                dataMap.put("soIds", soIds);
                dataMap.put("orderCode", orderCodes);
                dataMap.put("addr", addr);
                dataMap.put("companyName", companyName);

                List<Map<String, Object>> pkgAmountList = partPkgDao.getPkgDetailAmountByPkgNo(pickOrderId, pkgNos);

                if (null != pkgAmountList && pkgAmountList.size() > 0) {
                    for (int m = 0; m < pkgAmountList.size(); m++) {
                        totalAmount = Arith.add(totalAmount, Double.valueOf(pkgAmountList.get(m).get("PKG_AMOUNTS") + ""));
                    }
                }

                dataMap.put("totalAmount", totalAmount);
                dataMap.put("chineseAmount", laborListSummaryReportDao.numToChinese(totalAmount + ""));

                List<Map<String, Object>> tempDetailList = partPkgDao.getPkgDetailListByPkgNo(pickOrderId, pkgNos);
                detailList.add(tempDetailList);
                dataMap.put("detailList", detailList);
                List list = new ArrayList();
                List tempList = new ArrayList();
                int listAcount = 0;
                int dtlListAct = tempDetailList.size() % 20;//用于确定最后一页明细行数
                act.setOutData("dtlListAct", dtlListAct);
                if (tempDetailList.size() > 20) {
                    for (int i = 0; i < tempDetailList.size(); i++) {
                        tempList.add(tempDetailList.get(i));
                        if (tempList.size() == 20) {
                            list.add(tempList);
                            tempList = new ArrayList();
                        }
                        if (i == tempDetailList.size() - 1 && tempList != null && tempList.size() > 0) {
                            list.add(tempList);
                        }
                    }
                    dataMap.put("detailList", list);
                }
                if (list.size() == 0) {
                    act.setOutData("listAcount", list.size() + 1);
                } else {
                    act.setOutData("listAcount", list.size());
                }
            }

            partPkgDao.updatePkgInfo(pickOrderId);//更新装箱单打印

            act.setOutData("dataMap", dataMap);
            act.setForword(PART_PKG_DTL_PRINT);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "装箱清单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 装箱单-批量打印
     * @param :
     * @return :
     * @throws : LastDate    : 2013-12-23
     * @Title : 批量打印装箱清单
     */
    public void opPkgDtlPrintHtmlBatch() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();

        PartOutstockDao dao =  PartOutstockDao.getInstance();
        LaborListSummaryReportDao laborListSummaryReportDao = LaborListSummaryReportDao.getInstance();

        List<Map<String, Object>> batchDatas = new ArrayList<Map<String, Object>>();
        try {
            //1.获取拣货单ID集合
            String[] pickOrderIds = request.getParamValues("cb");
            for (String pickOrderId : pickOrderIds) {
                //2.根据拣货单ID获取对应箱号
                List<TtPartPkgBoxDtlPO> list1 = dao.queryPkgNo(pickOrderId, "0", Constant.CAR_FACTORY_PKG_STATE_02 + ""); // 未随车箱号
                if (list1 == null || list1.size() <= 0) {
                    list1 = dao.queryPkgNo(pickOrderId, "1", Constant.CAR_FACTORY_PKG_STATE_02 + "");// 已经打印的未随车箱号
                }
                String pkgNos = "";
                for (TtPartPkgBoxDtlPO tppbdp : list1) {
                    pkgNos = pkgNos + tppbdp.getPkgNo() + ",";
                }
                if (pkgNos == "") {
                    String msg = "拣货单[ " + pickOrderId + " ]没有获取到箱号，批量打印装箱单出现异常，请联系管理人员！";
                    BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, msg);
                    throw e1;
                }
                pkgNos = pkgNos.substring(0, pkgNos.length() - 1);
                partPkgDao.updatePkgPrintInfo(pickOrderId, pkgNos);//3.更新装装箱明细信息

                partPkgDao.updatePkgInfo(pickOrderId);//4. 更新装箱单打印

                int indexNO = 1;
                Map<String, Object> dataMap = new HashMap<String, Object>();
                //5.获取装箱单表头信息
                List<Map<String, Object>> soMainList = partPkgDao.getSoMainByPkgNo(pickOrderId, pkgNos);
                if (soMainList != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                    String nowDate = sdf.format(new Date());
                    Double totalAmount = 0D;
                    String remark = "";
                    String soIds = "";
                    String soCodes = "";
                    String transType = "";
                    String whId = "";
                    String dealerName = "";
                    String dealerCode = "";
                    String phone = "";
                    String createDate = "";
                    String creater = "";
                    String orderType = "";
                    String orderCodes = "";
                    String companyName = "";
                    String addr = "";
                    for (int i = 0; i < soMainList.size(); i++) {
                        Map<String, Object> mainMap = soMainList.get(i);
                        String soId = CommonUtils.checkNull(mainMap.get("SO_ID"));
                        remark += CommonUtils.checkNull(mainMap.get("REMARK2"));
                        soIds = soIds + "," + soId;
                        whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
                        transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
                        dealerName = CommonUtils.checkNull(mainMap.get("DEALER_NAME"));
                        dealerCode = CommonUtils.checkNull(mainMap.get("DEALER_CODE"));
                        phone = CommonUtils.checkNull(mainMap.get("TEL"));
                        createDate = CommonUtils.checkNull(mainMap.get("CREATE_DATE_FM"));
//						creater = CommonUtils.checkNull(mainMap.get("NAME"));
                        orderType = CommonUtils.checkNull(mainMap.get("CODE_DESC"));
                        String orderCode = CommonUtils.checkNull(mainMap.get("ORDER_CODE"));
                        orderCodes = orderCodes + "," + orderCode;
                        addr = CommonUtils.checkNull(mainMap.get("ADDR"));
                    }
                    if (!"".equals(soIds)) {
                        soIds = soIds.replaceFirst(",", "");
                    }
                    if (!"".equals(soCodes)) {
                        soCodes = soCodes.replaceFirst(",", "");
                    }
                    if (!"".equals(orderCodes)) {
                        orderCodes = orderCodes.replaceFirst(",", "");
                    }
                    if (loginUser.getDealerId() == null) {
                        TmCompanyPO tmCompanyPO = new TmCompanyPO();
                        tmCompanyPO.setCompanyType(Integer.valueOf(Constant.COMPANY_TYPE_SGM));
                        companyName = ((TmCompanyPO) partPkgDao.select(tmCompanyPO).get(0)).getCompanyName();
                    } else {
                        TmDealerPO tmDealerPO = new TmDealerPO();
                        tmDealerPO.setDealerId(Long.valueOf(loginUser.getDealerId()));
                        companyName = ((TmDealerPO) partPkgDao.select(tmDealerPO).get(0)).getDealerName();

                    }
                    transType = partPkgDao.getTranTypeName(transType);
                    String whName = partPkgDao.getWhName(whId);
                    dataMap.put("phone", phone);
                    dataMap.put("transType", transType);
                    dataMap.put("whName", whName);
                    dataMap.put("pickOrderId", pickOrderId);
                    dataMap.put("remark", remark);
                    dataMap.put("soCodes", soCodes);
                    dataMap.put("dealerName", dealerName);
                    dataMap.put("dealerCode", dealerCode);
                    dataMap.put("userName", loginUser.getName());
                    dataMap.put("date", nowDate);
                    dataMap.put("createDate", createDate);
                    dataMap.put("creater", loginUser.getName());
                    dataMap.put("orderType", orderType);
                    dataMap.put("soIds", soIds);
                    dataMap.put("orderCode", orderCodes);
                    dataMap.put("addr", addr);
                    dataMap.put("companyName", companyName);

                    List<Map<String, Object>> pkgAmountList = partPkgDao.getPkgDetailAmountByPkgNo(pickOrderId, pkgNos);
                    if (null != pkgAmountList && pkgAmountList.size() > 0) {
                        for (int m = 0; m < pkgAmountList.size(); m++) {
                            String pkgAmounts = pkgAmountList.get(m).get("PKG_AMOUNTS").toString();
                            totalAmount = Arith.add(totalAmount, Double.valueOf(pkgAmounts));
                        }
                    }
                    dataMap.put("totalAmount", totalAmount);
                    dataMap.put("chineseAmount", laborListSummaryReportDao.numToChinese(totalAmount + ""));

                    //6.获取装箱单明细条目
                    List<Map<String, Object>> tempDetailList = partPkgDao.getPkgDetailListByPkgNo(pickOrderId, pkgNos);

                    List<Map<String, Object>> sumList = partPkgDao.getPkgSumListByPkgNo(pickOrderId, pkgNos);

                    int pageSize = 20;
                    int dtlListAct = tempDetailList.size() % pageSize;//用于确定最后一页明细行数

                    ArrayList<String> kongList = new ArrayList<String>();
                    //无余数时不需要增加空行
                    if (dtlListAct != 0) {
                        for (int k = 0; k < (pageSize - dtlListAct); k++) {
                            kongList.add((k + 1) + "");
                        }
                    }
                    dataMap.put("dtlListAct", kongList);


                    List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
                    List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
                    for (int k = 0; k < tempDetailList.size(); k++) {//indexNO
                        Map<String, Object> row = tempDetailList.get(k);
                        row.put("indexNO", indexNO);
                        tempList.add(row);
                        indexNO++;
                        if (tempList.size() == pageSize) {
                            list.add(tempList);
                            tempList = new ArrayList<Map<String, Object>>();
                        } else if (tempDetailList.size() == (k + 1)) {
                            list.add(tempList);
                        }
                    }

                    dataMap.put("detailList", list);
                    dataMap.putAll(sumList.get(0));
                    sumList = new ArrayList<Map<String, Object>>();
                    act.setOutData("listAcount", list.size());
                }
                indexNO = 1;
                batchDatas.add(dataMap);
            }
            act.setOutData("batchDatas", batchDatas);

            act.setForword(PART_PKG_DTL_PRINT_BATCH);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "批量装箱清单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
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

    private boolean validatePkg(String soId, String partId, String buyQty) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartSoManageDao partSoManageDao =  PartSoManageDao.getInstance();
        String sumQty = partPkgDao.getPkgNum(soId, partId);
        if (Long.valueOf(buyQty) < Long.valueOf(sumQty)) {
            return false;
        }
        return true;
    }

    /**
     *  合并捡货单 或 生成拣货单 
     */
    public void unitPickOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartTransDao partTransDao =  PartTransDao.getInstance();
        try {
            String[] soIdArr = CommonUtils.checkNull(request.getParamValue("cbAr")).split(",");
            String unitType = CommonUtils.checkNull(request.getParamValue("unitType"));

            //合并拣货
            if ("1".equals(unitType)) {
                if (null != soIdArr) {
                    String soIds = "";
                    for (int i = 0; i < soIdArr.length; i++) {
                        String soId = soIdArr[i];
                        soIds = soIds + "," + soId;
                    }
                    if (!"".equals(soIds)) {
                        soIds = soIds.replaceFirst(",", "");
                    }
                    List<Map<String, Object>> list = partPkgDao.getSoMainGroup(soIds);
                    // ,wh_id,TRANS_TYPE
                    for (Map<String, Object> map : list) {
                        String dealerId = CommonUtils.checkNull(map.get("DEALER_ID"));
                        String whId = CommonUtils.checkNull(map.get("WH_ID"));
                        String transType = CommonUtils.checkNull(map.get("TRANS_TYPE"));
                        String orderType = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                        String addrId = CommonUtils.checkNull(map.get("ADDR_ID"));
                        String pickOrderId = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_28, dealerId);
//                    String soId = CommonUtils.checkNull(map.get("SO_ID"));
                        partPkgDao.savePickOrderId(soIds, dealerId, whId, pickOrderId, transType, loginUser.getUserId() + "", orderType, addrId);
                        partPkgDao.updateBookTtlPickOrderId(soIds, dealerId, whId, pickOrderId, transType, loginUser.getUserId() + "", orderType, addrId);
                        List<Map<String, Object>> soOrderList = partTransDao.getSoMain(pickOrderId);
                        Map<String, Object> mainMap = soOrderList.get(0);
                        TtPartPickOrderMainPO po = new TtPartPickOrderMainPO();
                        Long pickId = Long.parseLong(SequenceManager.getSequence(""));
                        po.setPickId(pickId);
                        po.setPickOrderId(pickOrderId);
                        //po.setCheckPickBy(loginUser.getName());//不需要设置
                        po.setCreateDate(new Date());
                        po.setCreateByName(loginUser.getName());
                        po.setWhId(Long.valueOf(CommonUtils.checkNull(mainMap.get("WH_ID"))));
                        po.setDealerName(CommonUtils.checkNull(mainMap.get("DEALER_NAME")));
                        po.setSellerId(Long.valueOf(CommonUtils.checkNull(mainMap.get("SELLER_ID"))));
                        po.setDealerCode(CommonUtils.checkNull(mainMap.get("DEALER_CODE")));
                        po.setState(Constant.CAR_FACTORY_PKG_STATE_01);
                        partPkgDao.insert(po);
                        //插入配件操作记录
                        List<Map<String, Object>> rs = PartTransPlanDao.getInstance().query4History(pickOrderId);
                        for (Map<String, Object> m : rs) {
                            String bussinessId = m.get("SO_CODE").toString();
                            long orderId = Long.valueOf((m.get("ORDER_ID") == null ? 0 : m.get("ORDER_ID")).toString());
                            PartTransPlan.insertPartHistory(bussinessId, "拣货中", Constant.PART_ORDER_STATE_01, Constant.PART_OPERATION_TYPE_02, "无", orderId);

                        }
                    }

                }
            } else {//不合并拣货，每个销售单一张拣货单
                for (int i = 0; i < soIdArr.length; i++) {
                    String soId = soIdArr[i];
                    List<Map<String, Object>> list = partPkgDao.getSoMainGroup(soId);
                    for (Map<String, Object> map : list) {
                        String dealerId = CommonUtils.checkNull(map.get("DEALER_ID"));
                        String whId = CommonUtils.checkNull(map.get("WH_ID"));
                        String transType = CommonUtils.checkNull(map.get("TRANS_TYPE"));
                        String orderType = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                        String addrId = CommonUtils.checkNull(map.get("ADDR_ID"));
                        String pickOrderId = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_28, dealerId);
                        //保存拣货单
                        partPkgDao.savePickOrderId(soId, dealerId, whId, pickOrderId, transType, loginUser.getUserId() + "", orderType, addrId);
                        //更新配件占有信息
                        partPkgDao.updateBookTtlPickOrderId(soId, dealerId, whId, pickOrderId, transType, loginUser.getUserId() + "", orderType, addrId);
                        List<Map<String, Object>> soOrderList = partTransDao.getSoMain(pickOrderId);
                        Map<String, Object> mainMap = soOrderList.get(0);
                        TtPartPickOrderMainPO po = new TtPartPickOrderMainPO();
                        Long pickId = Long.parseLong(SequenceManager.getSequence(""));
                        po.setPickId(pickId);
                        po.setPickOrderId(pickOrderId);
                        //po.setCheckPickBy(loginUser.getName());//不需要设置
                        po.setCreateDate(new Date());
                        po.setCreateByName(loginUser.getName());
                        po.setWhId(Long.valueOf(CommonUtils.checkNull(mainMap.get("WH_ID"))));
                        po.setDealerName(CommonUtils.checkNull(mainMap.get("DEALER_NAME")));
                        po.setSellerId(Long.valueOf(CommonUtils.checkNull(mainMap.get("SELLER_ID"))));
                        po.setDealerCode(CommonUtils.checkNull(mainMap.get("DEALER_CODE")));
                        po.setState(Constant.CAR_FACTORY_PKG_STATE_01);
                        partPkgDao.insert(po);
                        //插入配件操作记录
                        List<Map<String, Object>> rs = PartTransPlanDao.getInstance().query4History(pickOrderId);
                        for (Map<String, Object> m : rs) {
                            String bussinessId = m.get("SO_CODE").toString();
                            long orderId = Long.valueOf((m.get("ORDER_ID") == null ? 0 : m.get("ORDER_ID")).toString());
                            PartTransPlan.insertPartHistory(bussinessId, "拣货中", Constant.PART_ORDER_STATE_01, Constant.PART_OPERATION_TYPE_02, "无", orderId);

                        }
                    }
                }
            }
            act.setOutData("success", "生成成功!");
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "拣货单错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER);
        }
    }

/*    public void pickOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPkgDao dao = PartPkgDao.getInstance();
        PartTransDao partTransDao = new PartTransDao();
        try {
            String[] soIdArr = CommonUtils.checkNull(request.getParamValue("cbAr")).split(",");

            if (null != soIdArr) {
                String soIds = "";
                for (int i = 0; i < soIdArr.length; i++) {
                    String soId = soIdArr[i];
                    soIds = soIds + "," + soId;
                }
                if (!"".equals(soIds)) {
                    soIds = soIds.replaceFirst(",", "");
                }
                List<Map<String, Object>> list = dao.getSoMains(soIds);
                // ,wh_id,TRANS_TYPE
                for (Map<String, Object> map : list) {
                    String dealerId = CommonUtils.checkNull(map.get("DEALER_ID"));
                    String whId = CommonUtils.checkNull(map.get("WH_ID"));
                    String transType = CommonUtils.checkNull(map.get("TRANS_TYPE"));
                    String orderType = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                    String addrId = CommonUtils.checkNull(map.get("ADDR_ID"));
                    String pickOrderId = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_28, dealerId);
//                    String soId = CommonUtils.checkNull(map.get("SO_ID"));
                    dao.savePickOrderId(soIds, dealerId, whId, pickOrderId, transType, loginUser.getUserId() + "", orderType, addrId);
                    List<Map<String, Object>> soOrderList = partTransDao.getSoMain(pickOrderId);
                    Map<String, Object> mainMap = soOrderList.get(0);
                    TtPartPickOrderMainPO po = new TtPartPickOrderMainPO();
                    Long pickId = Long.parseLong(SequenceManager.getSequence(""));
                    po.setPickId(pickId);
                    po.setPickOrderId(pickOrderId);
                    //po.setCheckPickBy(loginUser.getName());//不需要设置
                    po.setCreateDate(new Date());
                    po.setCreateByName(loginUser.getName());
                    po.setWhId(Long.valueOf(CommonUtils.checkNull(mainMap.get("WH_ID"))));
                    po.setDealerName(CommonUtils.checkNull(mainMap.get("DEALER_NAME")));
                    po.setSellerId(Long.valueOf(CommonUtils.checkNull(mainMap.get("SELLER_ID"))));
                    po.setDealerCode(CommonUtils.checkNull(mainMap.get("DEALER_CODE")));
                    po.setState(Constant.CAR_FACTORY_PKG_STATE_01);
                    dao.insert(po);
                }
            }
            act.setOutData("success", "操作成功!");
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "合并拣货单错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER);
        }
    }*/

    public void pkgDetailPrint() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartSoManageDao partSoManageDao =  PartSoManageDao.getInstance();
        PartPickOrderDao partPickOrderDao =  PartPickOrderDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String pkgIds = CommonUtils.checkNull(request.getParamValue("pkgIds"));
            if ("".equals(pkgIds)) {
                pkgIds = "''";
            }
            List<Map<String, Object>> detailList = partPkgDao.getPkgDtlGroup(pkgIds);
            List<Map<String, Object>> soOrderList = partPickOrderDao.getSoMainList(pickOrderId);
            Map<String, Object> mainMap = soOrderList.get(0);
            String whName = partPkgDao.getWhName(CommonUtils.checkNull(mainMap.get("WH_ID")));
            String transType = partPkgDao.getTranTypeName(CommonUtils.checkNull(mainMap.get("TRANS_TYPE")));
            mainMap.put("whName", whName);
            mainMap.put("transType", transType);
            act.setOutData("mainMap", mainMap);
            act.setOutData("pkgIds", pkgIds);
            act.setOutData("detailList", detailList);
            act.setForword(partPkgPrintUrl);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱单数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }

    public void printQxt() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String soId = CommonUtils.checkNull(request.getParamValue("soId"));
            String pkgIds = CommonUtils.checkNull(request.getParamValue("pkgIds"));
            List<Map<String, Object>> printList = new ArrayList<Map<String, Object>>();
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱单数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }


    public void printPkg() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String pkgIds = CommonUtils.checkNull(request.getParamValue("pkgIds"));
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            
            Map<String, Object> pickOrderMap = partPkgDao.getPickInfo(pickOrderId);
            
            List<Map<String, Object>> list = partPkgDao.getPkgList(pkgIds);
            
            List<Map<String, Object>> soMainList = partPkgDao.getSoMain(pickOrderId);
            
            Map<String, Object> mainMap = soMainList.get(0);
            String transTypeId = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
            String transType = partPkgDao.getTranTypeName(transTypeId);
            String soCodes = "";
            String min = "";
            String max = "";
            String head = "";
            String dealerName = "";
            String dealerCode = "";
            String recAddress = "";
            String recName = "";
            String telPhone = "";
            if (soMainList.size() > 4) {
                for (Map<String, Object> map : soMainList) {
                    dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));//经销商代码
                    recAddress = CommonUtils.checkNull(map.get("ADDR"));//收货地址
                    recName = CommonUtils.checkNull(map.get("RECEIVER"));//收货人姓名
                    telPhone = CommonUtils.checkNull(map.get("TEL"));//联系电话
                    String soCode = CommonUtils.checkNull(map.get("SO_CODE"));
                    head = CommonUtils.checkNull(map.get("SO_CODE")).substring(0, 4);
                    if (min == "" && max == "") {
                        min = soCode;
                        max = soCode;
                        continue;
                    }
                    if (min.compareTo(soCode) > 0) {
                        min = soCode;
                    }
                    if (max.compareTo(soCode) < 0) {
                        max = soCode;
                    }
                }
                soCodes = min + "-" + max.substring(max.length() - 2, max.length());
            } else {

                for (Map<String, Object> map : soMainList) {
                    dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));//经销商代码
                    recAddress = CommonUtils.checkNull(map.get("ADDR"));//收货地址
                    recName = CommonUtils.checkNull(map.get("RECEIVER"));//收货人姓名
                    telPhone = CommonUtils.checkNull(map.get("TEL"));//联系电话
                    String soCode = CommonUtils.checkNull(map.get("SO_CODE"));
                    if (soCodes == "") {
                        soCodes = soCode;
                        continue;
                    }
                    soCodes += "," + soCode.substring(soCode.length() - 2, soCode.length());
                }
            }

            List<Map<String, Object>> pkgList = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < list.size(); i++) {
                if ((i + 1) != 0 && (i + 1) % 2 == 0) {
                    String pkgNo = CommonUtils.checkNull(list.get(i).get("PKG_NO"));
                    map.put("pkgNo2", pkgNo);
                    map.put("flag", true);
                    pkgList.add(map);
                    map = new HashMap<String, Object>();
                } else {
                    String pkgNo = CommonUtils.checkNull(list.get(i).get("PKG_NO"));
                    map.put("pkgNo1", pkgNo);
                    if (i == list.size() - 1) {
                        map.put("flag", false);
                        pkgList.add(map);
                    }
                }
            }

            act.setOutData("dealerName", dealerName);
            act.setOutData("dealerCode", dealerCode);
            act.setOutData("recAddress", recAddress);
            act.setOutData("recName", recName);
            act.setOutData("telPhone", telPhone);
            act.setOutData("transType", transType);
            act.setOutData("soCodes", soCodes);
            act.setOutData("pickOrderMap", pickOrderMap);
            act.setOutData("list", pkgList);
            //act.setForword(this.printUrl);--不需要跳转
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱单数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }

    public void saveTrans() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String transOrg = CommonUtils.checkNull(request.getParamValue("transOrg"));

            String transId = CommonUtils.checkNull(request.getParamValue("transId"));
            TtPartPickOrderMainPO oldPo = new TtPartPickOrderMainPO();
            oldPo.setPickOrderId(pickOrderId);
            TtPartPickOrderMainPO newPo = new TtPartPickOrderMainPO();
            newPo.setLogisticsNo(transId);
            //newPo.setTransOrg(transOrg);
            partPkgDao.update(oldPo, newPo);
            act.setOutData("success", "success");
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱单数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }

    private void saveQueryCondition() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));//销售单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//订货单位
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//销售单位
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));//出库仓库
        String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//销售日期S
        String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//销售日期E
        String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));//订单类型
        String state = CommonUtils.checkNull(request.getParamValue("state"));//状态
        String column = CommonUtils.checkNull(request.getParamValue("column"));//字段
        String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE"));//发运方式
        String ifPick = CommonUtils.checkNull(request.getParamValue("IF_PICK"));//是否捡货
        String pickNum = CommonUtils.checkNull(request.getParamValue("pickNum"));//合并单号
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("soCode", soCode);
        map.put("dealerName", dealerName);
        map.put("dealerCode", dealerCode);
        map.put("whId", whId);
        map.put("SstartDate", SstartDate);
        map.put("SendDate", SendDate);
        map.put("orderType", orderType);
        map.put("state", state);
        map.put("column", column);
        map.put("TRANS_TYPE", transType);
        map.put("IF_PICK", ifPick);
        map.put("pickNum", pickNum);
        act.getSession().set("condition", map);
    }

    /**
     * 把装箱数量分配到每个销售单上
     *
     * @param pickOrderId 拣货单ID
     * @param partId      配件ID
     * @param pkgQty      装箱数量
     */
    public void splitPkgQty(String pickOrderId, String partId, String locId, String whId, String pkgQty) throws Exception {
        try {
            List<Map<String, Object>> btl = partPkgDao.queryBookDtl(pickOrderId, whId, locId, partId);
            //循环遍历占用明细
            for (int i = 0; i <= btl.size(); i++) {
                Map<String, Object> map = btl.get(i);
                long orderId = ((BigDecimal) map.get("ORDER_ID")).longValue(); //销售单ID
                long bookedQty = ((BigDecimal) map.get("BOOKED_QTY")).longValue();//预占数量
                //循环遍历已装箱明细
                List<Map<String, Object>> ptl = partPkgDao.queryPkgDtl(pickOrderId, whId, locId, partId, orderId);
                for (int ii = 0; ii <= ptl.size(); ii++) {
                    Map<String, Object> pmap = ptl.get(i);
                    long yzxQty = ((BigDecimal) pmap.get("PKG_QTY")).longValue();//已装箱数量
                    //已装箱数量小于总的占用数量.且本次装箱数量+已装箱数量小于等于预占数量，那么执行分配
                    if (bookedQty > yzxQty && (yzxQty + Long.valueOf(pkgQty)) <= bookedQty) {
                        System.out.println(orderId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 装箱明细查询初始化
     */
    public void partPkgDtlInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderID"));//拣货单单号
            String trplanCode = CommonUtils.checkNull(request.getParamValue("TRPLAN_CODE"));//拣货单单号
            act.setOutData("pickOrderId", pickOrderId);
            act.setOutData("trplanCode", trplanCode);
            act.setForword(partPkgDtlUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "装箱明细查询初始化,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 装箱明细查询
     */
    public void partPkgDtlQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = partPkgDao.queryPkgDtl(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "装箱明细查询,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }
    
    /**
     * 未装箱明细查询初始化
     */
    public void partNonePkgDtlInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderID"));//销售单号
            act.setOutData("pickOrderId", pickOrderId);
            act.setForword(partNonePkgDtlUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "装箱明细查询初始化,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 未装箱明细查询
     */
    public void partNonePkgDtlQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")): 1; // 处理当前页
            PageResult<Map<String, Object>> ps = partPkgDao.queryNonePkgDtl(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "装箱明细查询,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }
    
    /**
     * 导出装箱明细
     */
    public void exportPkgDtlToExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String pickOrderId=request.getParamValue("pickOrderId");//拣货单号
            
            String[] head = new String[7];
            head[0] = "经销商代码 ";
            head[1] = "经销商";
            head[2] = "拣货单号";
            head[3] = "箱号";
            head[4] = "配件编码";
            head[5] = "配件名称";
            head[6] = "装箱数量";
            PageResult<Map<String, Object>> ps = partPkgDao.queryPkgDtl(request, 1, Constant.PAGE_SIZE_MAX);
            List<Map<String, Object>> list = ps.getRecords();
            list = list == null ? new ArrayList() : list;
//            List<Map<String, Object>> list =partPkgDao.exportPkgDtl(pickOrderId);
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[7];
                    detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                    detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[2] = CommonUtils.checkNull(map.get("PICK_ORDER_ID"));
                    detail[3] = CommonUtils.checkNull(map.get("PKG_NO"));
                    detail[4] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                    detail[5] = CommonUtils.checkNull(map.get("PART_CNAME"));
                    detail[6] = CommonUtils.checkNull(map.get("PKG_QTY"));
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
     * 拣货单批量打印
     */
    public void opBatchPrintHtml() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        LaborListSummaryReportDao laborListSummaryReportDao = LaborListSummaryReportDao.getInstance();//金额转中文
        List<Map<String, Object>> batchDatas = new ArrayList<Map<String, Object>>();
        try {
            String[] pickOrderIds = request.getParamValues("cb");
            if (pickOrderIds != null) {
                for (int j = 0; j < pickOrderIds.length; j++) {
                    int indexNO = 1;
                    Map<String, Object> dataMap = new HashMap<String, Object>();
                    String pickOrderId = pickOrderIds[j];
                    partPkgDao.updatePickInfo(pickOrderId);//更新捡货次数

                    List<Map<String, Object>> soMainList = partPkgDao.getSoMain(pickOrderId);//获取表头信息
                    if (soMainList != null) {
                        Double totalAmount = 0D;
                        String remark = "";
                        String soIds = "";
                        String soCodes = "";
                        String transType = "";
                        String whId = "";
                        String dealerName = "";
                        String dealerCode = "";
                        String phone = "";
                        String orderType = "";
                        String addr = "";
                        String orderCodes = "";
                        String companyName = "";
                        for (int i = 0; i < soMainList.size(); i++) {
                            Map<String, Object> mainMap = soMainList.get(i);
                            String soId = CommonUtils.checkNull(mainMap.get("SO_ID"));
                            totalAmount = Arith.add(totalAmount, Double.valueOf(mainMap.get("AMOUNT") + ""));
                            remark += CommonUtils.checkNull(mainMap.get("REMARK2"));
                            soIds = soIds + "," + soId;
                            whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
                            transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
                            dealerName = CommonUtils.checkNull(mainMap.get("DEALER_NAME"));
                            dealerCode = CommonUtils.checkNull(mainMap.get("DEALER_CODE"));
                            phone = CommonUtils.checkNull(mainMap.get("TEL"));
                            orderType = CommonUtils.checkNull(mainMap.get("CODE_DESC"));
                            addr = CommonUtils.checkNull(mainMap.get("ADDR"));
                            String orderCode = CommonUtils.checkNull(mainMap.get("ORDER_CODE"));
                            orderCodes = orderCodes + "," + orderCode;
                        }
                        if (!"".equals(soIds)) {
                            soIds = soIds.replaceFirst(",", "");
                        }
                        if (!"".equals(soCodes)) {
                            soCodes = soCodes.replaceFirst(",", "");
                        }
                        if (!"".equals(orderCodes)) {
                            orderCodes = orderCodes.replaceFirst(",", "");
                        }
                        String whName = partPkgDao.getWhName(whId);
                        transType = partPkgDao.getTranTypeName(transType);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                        String date = sdf.format(new Date());

                        if (loginUser.getUserId() == null) {
                            TmDealerPO dealerPO = new TmDealerPO();
                            dealerPO.setDealerId(Long.valueOf(loginUser.getDealerId()));
                            companyName = ((TmDealerPO) partPkgDao.select(dealerPO).get(0)).getDealerName();
                        } else {
                            TmCompanyPO companyPO = new TmCompanyPO();
                            companyPO.setCompanyType(Integer.valueOf(Constant.COMPANY_TYPE_SGM));
                            companyName = ((TmCompanyPO) partPkgDao.select(companyPO).get(0)).getCompanyName();
                        }


                        dataMap.put("phone", phone);
                        dataMap.put("transType", transType);
                        dataMap.put("whName", whName);
                        dataMap.put("pickOrderId", pickOrderId);
                        dataMap.put("remark", remark);
                        dataMap.put("soCodes", soCodes);
                        dataMap.put("dealerName", dealerName);
                        dataMap.put("dealerCode", dealerCode);
                        dataMap.put("totalAmount", totalAmount);
                        dataMap.put("userName", loginUser.getName());
                        dataMap.put("date", date);
                        dataMap.put("createDate", date);//获取当前日期
                        dataMap.put("creater", loginUser.getName());//取当前操作者姓名
                        dataMap.put("orderType", orderType);
                        dataMap.put("soIds", soIds);
                        dataMap.put("addr", addr);
                        dataMap.put("orderCodes", orderCodes);
                        dataMap.put("companyName", companyName);
                        dataMap.put("chineseAmount", laborListSummaryReportDao.numToChinese(totalAmount + ""));
                        //获取拣货单打印明细信息
                        List<Map<String, Object>> tempDetailList = partPkgDao.getDtlLoc(soIds, whId);
                        
                        int pageSize = 20;
                        int dtlListAct = tempDetailList.size() % pageSize;//用于确定最后一页明细行数
                        ArrayList<String> kongList = new ArrayList<String>();
                        if (dtlListAct != 0) {
                            for (int k = 0; k < 20 - dtlListAct; k++) {
                                kongList.add((k + 1) + "");
                            }
                        }
                        dataMap.put("dtlListAct", kongList);

                        List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
                        List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
                        for (int i = 0; i < tempDetailList.size(); i++) {//indexNO
                            Map<String, Object> row = tempDetailList.get(i);
                            row.put("indexNO", indexNO);
                            indexNO++;
                            tempList.add(row);
                            if (tempList.size() == pageSize) {
                                list.add(tempList);
                                tempList = new ArrayList<Map<String, Object>>();
                            } else if (tempDetailList.size() == (i + 1)) {
                                list.add(tempList);
                            }
                        }
                        dataMap.put("detailList", list);
                    }
                    indexNO = 0;
                    
                    batchDatas.add(dataMap);
                }
            }
            act.setOutData("batchDatas", batchDatas);
            act.setForword(batchPrintUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "拣货单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 拣货单批量打印
     */
    public void batchPrintPickOrder() {
        this.opBatchPrintHtml();
    }

    /**
     * 拣货明细查询初始化
     */
    public void partPikViewDtlInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderID"));//销售单号
            act.setOutData("pickOrderId", pickOrderId);
            act.setForword(partPikDtlUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "装箱明细查询初始化,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 拣货单明细查询
     */
    public void partPikDtlQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = partPkgDao.queryPikDtl(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "装箱明细查询,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }

    /**
     * 发运明细查询初始化
     */
    public void partTransViewDtlInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));//销售单号
            act.setOutData("pickOrderId", pickOrderId);
            act.setForword(partTransDtlUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "装箱明细查询初始化,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 发运明细查询
     */
    public void partTransDtlQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = partPkgDao.queryTrnasDtl(request, curPage, Constant.PAGE_SIZE);

//            act.setOutData("queryogistics",dao.queryogistics(request, curPage, Constant.PAGE_SIZE).getRecords());
            act.setOutData("queryogistics", ps.getRecords());
            act.setOutData("x11", 222);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "装箱明细查询,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }

    /**
     * 发运物流明细查询
     */
    public void partLogInfoQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = partPkgDao.queryLogisticsInfo(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "物流明细查询,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }

    /**
     * 取消合并
     */
    public void canclePickOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            TtPartPkgDtlPO pkgDtlPO = new TtPartPkgDtlPO();
            pkgDtlPO.setPickOrderId(pickOrderId);
            if (partPkgDao.select(pkgDtlPO).size() > 0) {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "取消拣货单错误,请联系管理员!");
                act.setOutData("error", "【" + pickOrderId + "】已经在装箱或完成装箱，不能取消！请先取消装箱，然后再取消合并拣配单!");
                return;
            }
            //删除操作记录
            List<Map<String, Object>> rs = PartTransPlanDao.getInstance().query4History(pickOrderId);
            for (Map<String, Object> m : rs) {
                long orderId = Long.valueOf((m.get("ORDER_ID") == null ? 0 : m.get("ORDER_ID")).toString());
                partPkgDao.deleteOptHis(orderId + "");
            }
            partPkgDao.cancelPickOrderId(pickOrderId);
            partPkgDao.cancelUpdateBookedPickOrderId(pickOrderId);

            act.setOutData("success", "取消成功!");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "取消合并拣货单错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    

    /**
     * 查询整车销售计划
     */
    public void partQueryZCPlanInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); // 经销商ID
            act.setOutData("dealerId", dealerId);
            act.setForword(partZCPlanQueryDtlUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "整车销售计划查询初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void sendAssignmentQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            /******************************页面查询字段start**************************/
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商ID
            String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")); // 订单类型
            String orderNo = CommonUtils.checkNull(request.getParamValue("ORDER_NO")); // 销售订单号
            String raiseStartDate = CommonUtils.checkNull(request.getParamValue("RAISE_STARTDATE")); // 提报日期开始
            String raiseEndDate = CommonUtils.checkNull(request.getParamValue("RAISE_ENDDATE")); // 提报日期结束
            String common = CommonUtils.checkNull(request.getParamValue("common")); // 查询类型
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); //经销商ID
            /******************************页面查询字段end***************************/
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("dealerCode", dealerCode);
            map.put("orderType", orderType);
            map.put("orderNo", orderNo);
            map.put("raiseStartDate", raiseStartDate);
            map.put("raiseEndDate", raiseEndDate);
            map.put("dealerId", dealerId);
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
                    .getParamValue("curPage")) : 1;
            PageResult<Map<String, Object>> ps = SendAssignmentDao.getInstance().getSendAssignmentQuery(map, curPage,
                    Constant.PAGE_SIZE);
            act.setOutData("ps", ps);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "整车销售计划信息查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

}