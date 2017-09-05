package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.parts.baseManager.PartBaseQueryDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderBalanceDao;
import com.infodms.dms.dao.parts.rebateManager.rebateManagerDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.dao.parts.salesManager.PartReplacedDlrOrderDao;
import com.infodms.dms.dao.parts.storageManager.partDistributeMgr.PartDistributeMgrDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.infox.util.StringUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.POContext;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PartReplacedDlrOrder extends BaseImport implements PTConstants {
    //替换件流程资源占用以及出库的开关flag
    public static final boolean UPDATE_PART_STATE_SWITCH = false;
    //回运资源占用开关flag
    public static final boolean UPDATE_PART_STATE_SWITCH_BACK = false;

    //自动生成订单开关flag
    public static final boolean AOTO_CREATE_ORDER_SWITCH = false;
    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);

    public String[] allowState = {Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 + "", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + "", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + ""};
    protected POFactory factory = POFactoryBuilder.getInstance();
    String UPLOADFILE = "/jsp/parts/salesManager/carFactorySalesManager/ReplacedUploadFile.jsp";
    String PART_REPLACED_DLR_ORDER = "/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrOrder.jsp";
    String PART_REPLACED_DLR_ORDER_MOD = "/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrOrderMod.jsp"; //配件订单提报修改页面
    String PART_REPLACED_DLR_ORDER_CHECK_DETAIL = "/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrOrderCheckDetail.jsp";
    String PART_REPLACED_DLR_ORDER_WAREHOUSINGINIT = "/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrWarehousingInit.jsp";
    String PART_REPLACED_DLR_ORDER_WAREHOUSING = "/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrWarehousing.jsp";
    String PART_REPLACED_DLR_ORDER_QUEYRINIT = "/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrOrderQueryInit.jsp";
    String PART_REPLACED_DLR_ORDER_QUEYRINIT_FAC = "/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrOrderQueryInitFac.jsp";

    //导入出错信息显示页面
    String INPUT_ERROR_URL = "/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrImportInfo/inputError.jsp";
    String REPEAT_ERROR_URL = "/jsp/parts/baseManager/partPkgStkManager/repeatError.jsp";
    //导入信息显示页面
    String DISPLAY_IMPORTED_DATA_URL = "/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrImportInfo/displayImportedData.jsp";


    PartReplacedDlrOrderDao dao = PartReplacedDlrOrderDao.getInstance();

    //出库开关flag
//	public static final boolean OUT_OF_WAREHOUSE_SWITCH = true;

    /**
     * 导入模板下载
     *
     * @param response
     * @param request
     * @param head
     * @param list
     * @return
     * @throws Exception
     */
    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "切换订单.xls";
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
     * 切换订单提报初始化
     */
    public void partReplacedDlrOrderInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 + "", "已保存");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "", "已提交");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + "", "已驳回");
            act.setOutData("stateMap", stateMap);
            act.setOutData("loginUser", loginUser.getCompanyId());
            act.setForword(PART_REPLACED_DLR_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "切换订单提报初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 切换订单查询初始化
     */
    public void partReplacedDlrQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "", "已提交");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 + "", "已保存");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03 + "", "已审核");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04 + "", "已作废");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + "", "已驳回");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_13 + "", "已回运");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_14 + "", "已验收");
            act.setOutData("stateMap", stateMap);
            //判断是否为车厂  PartWareHouseDao
            String dealerId = "";
            PartWareHouseDao dao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = dao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            String oemFlag = "";
            if (dealerId.equals(Constant.OEM_ACTIVITIES + "")) {
                oemFlag = Constant.PART_BASE_FLAG_YES + "";
            } else {
                oemFlag = Constant.PART_BASE_FLAG_NO + "";
            }
            act.setOutData("oemFlag", oemFlag);
            act.setOutData("loginUser", loginUser.getCompanyId());
            act.setForword(PART_REPLACED_DLR_ORDER_QUEYRINIT);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "切换订单查询初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 车厂切换订单查询初始化
     */
    public void partReplacedDlrQueryInitForFac() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 + "", "已保存");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "", "已提交");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + "", "已驳回");
            act.setOutData("stateMap", stateMap);
            act.setOutData("loginUser", loginUser.getCompanyId());
            act.setForword(PART_REPLACED_DLR_ORDER_QUEYRINIT_FAC);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "切换订单查询初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2014-3-20
     * @Title :
     * @Description: 替换件验收入库初始化
     */
    public void partDlrOrderWarehousingInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        boolean salerFlag = false;
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            List<Map<String, Object>> salerList = dao.getSaler();
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_14 + "", "已验收");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_13 + "", "已回运");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07 + "", "已关闭");
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            if (dealerId.equals(Constant.OEM_ACTIVITIES)) {
                salerFlag = true;

            }
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
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("planFlag", "noPlan");
            act.setOutData("curUserId", loginUser.getUserId());
            act.setOutData("salerList", salerList);
            act.setOutData("salerFlag", salerFlag);
            act.setOutData("stateMap", stateMap);
            act.setOutData("loginUser", loginUser.getCompanyId());
            act.setForword(PART_REPLACED_DLR_ORDER_WAREHOUSINGINIT);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "替换件验收入库初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2014-3-20
     * @Title :
     * @Description: 替换件回运初始化
     */
    public void partReplacedDlrBackhaulInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        boolean salerFlag = false;
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            List<Map<String, Object>> salerList = dao.getSaler();
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03 + "", "已审核");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_13 + "", "已回运");
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            if (dealerId.equals(Constant.OEM_ACTIVITIES)) {
                salerFlag = true;

            }
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
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("planFlag", "noPlan");
            act.setOutData("curUserId", loginUser.getUserId());
            act.setOutData("salerList", salerList);
            act.setOutData("salerFlag", salerFlag);
            act.setOutData("stateMap", stateMap);
            act.setOutData("loginUser", loginUser.getCompanyId());
            act.setForword(PART_REPLACED_DLR_BACK);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "替换件回运初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartDlrOrderDao
     * @Description: 切换订单审核初始化
     */
    public void partDlrOrderCheckInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        boolean salerFlag = false;
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            String userType = null;
            TtPartUserposeDefinePO userposeDefinePO = new TtPartUserposeDefinePO();
            userposeDefinePO.setUserId(loginUser.getUserId());
            if (dao.select(userposeDefinePO).size() > 0) {
                userposeDefinePO = (TtPartUserposeDefinePO) dao.select(userposeDefinePO).get(0);
                userType = userposeDefinePO.getUserType() + "";
            }
            List<Map<String, Object>> salerList = dao.getSaler(userType);

            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            if (dealerId.equals(Constant.OEM_ACTIVITIES)) {
                salerFlag = true;

            }
            Map<String, Object> map = new HashMap<String, Object>();
            if (null != act.getSession().get("condition")) {
                if (null != request.getParamValue("flag")) {
                    map = (Map) act.getSession().get("condition");
                    act.getSession().remove("condition");
                } else {
                    act.getSession().remove("condition");
                }
            }
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "", "已提交");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03 + "", "已审核");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + "", "已驳回");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_13 + "", "已回运");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_14 + "", "已验收");
            act.setOutData("stateMap", stateMap);
            act.setOutData("condition", map);
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("planFlag", "noPlan");
            act.setOutData("curUserId", loginUser.getUserId());
            act.setOutData("salerList", salerList);
            act.setOutData("salerFlag", salerFlag);
            act.setOutData("loginUser", loginUser.getCompanyId());
            act.setForword(PART_REPLACED_DLR_ORDER_CHECK_QUERY);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "切换订单审核初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2014-3-20
     * @Title :
     * @Description:生成切换订单
     */
    public void createOrderCheck() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            //订单编号
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String remark = CommonUtils.checkNull(request.getParamValue("remark"));

            String[] partArr = request.getParamValues("cb");
            if (null != partArr) {
                for (int i = 0; i < partArr.length; i++) {
                    long stockQty = Long.valueOf(request.getParamValue("item_qty_" + partArr[i]));
                    long checkQty = Long.valueOf(request.getParamValue("check_Qty_" + partArr[i]));
                    if (stockQty < checkQty) {
                        BizException e1 = new BizException(request.getParamValue("part_Cname_" + partArr[i]) + "库存不足");
                        throw e1;
                    }
                }
            }

            request.setAttribute("boFlag", false);
            //校验是否有同时操作的
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            String state = CommonUtils.checkNull(mainMap.get("STATE"));
            if (state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03 + "")) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "该订单已审核,请不要重复审核!");
                throw e1;
            }
            //增加判断是否订单已经生成销售单防止重复生成
            TtPartSoMainPO soMainPO = new TtPartSoMainPO();
            soMainPO.setOrderId(Long.valueOf(orderId));
            if (dao.select(soMainPO).size() > 0) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "该订单已经生成,不能重复生成!");
                throw e1;
            }
            state = "check";
            //更新明细
            this.updateOrderDetail(state);

            //更新订单
            this.updateOrderMain(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02);

            //修改状态
            this.changeOrderStatus(loginUser, orderId, act, request);

            //自动生成销售单
            List SOins = new LinkedList<Object>();
            SOins.add(0, orderId);
            SOins.add(1, Constant.PART_CODE_RELATION_52);
            SOins.add(2, 0);// 扣款
            SOins.add(3, 1);// 1:占用 0：释放占用
            SOins.add(4, loginUser.getUserId());//审核用户ID
            dao.callProcedure("P_CREATE_SO_ORDER", SOins, null);
            act.setOutData("success", "操作成功!");
            act.setForword(PART_REPLACED_DLR_ORDER_CHECK_QUERY);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "生成切换订单错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 切换订单审核查询
     */
    public void partDlrOrderQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            saveQueryCondition();
            ps = dao.queryCheckPartDlrOrder(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_REPLACED_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "切换订单审核数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_REPLACED_DLR_ORDER);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2014-3-20
     * @Title :
     * @Description: 审核订单
     */
    public void checkDlrOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            if (!(mainMap.get("STATE") + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "")) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单状态已改变!请重新查询!");
                throw e1;
            }

            String dealerId = "";
            String flag = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            mainMap.put("saleName", loginUser.getName());
            //获取账户
            Map<String, Object> accountMap = dao.getAccountMoney(mainMap.get("DEALER_ID").toString());
            String accountSumNow = "";
            String accountKyNow = "";
            String accountDjNow = "";
            boolean accountFlag = false;
            if (null != accountMap) {
                accountFlag = true;
                accountSumNow = accountMap.get("ACCOUNT_SUM").toString();
                accountKyNow = accountMap.get("ACCOUNT_KY").toString();
                accountDjNow = accountMap.get("ACCOUNT_DJ").toString();
            }
            mainMap.put("accountFlag", accountFlag);
            mainMap.put("accountSumNow", accountSumNow);
            mainMap.put("accountKyNow", accountKyNow);
            mainMap.put("accountDjNow", accountDjNow);

            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<OrgBean> lo = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(lo.get(0).getOrgId().toString());
            String sellerId = CommonUtils.checkNull(mainMap.get("SELLER_ID"));
            if (sellerId.equals(Constant.OEM_ACTIVITIES + "")) {
                wareHouseList = dao.getWareHouse(lo.get(0).getOrgId().toString(), CommonUtils.checkNull(mainMap.get("PRODUCE_FAC")));
            }
            //查询订单明细
            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail(orderId);
            List<Map<String, Object>> historyList = dao.queryOrderHistory(orderId);

            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            //获取服务商联系人和联系方式
            String orderDealerId = CommonUtils.checkNull(mainMap.get("DEALER_ID"));
            Map<String, Object> map = dao.getAddrMap(orderDealerId);
            Map<String, Object> dealerMap = dao.getDealerName(orderDealerId);

            request.setAttribute("phone", CommonUtils.checkNull(dealerMap.get("TEL")));
            request.setAttribute("linkMan", CommonUtils.checkNull(dealerMap.get("LINKMAN")));
            request.setAttribute("phone2", CommonUtils.checkNull(map.get("TEL2")));
            request.setAttribute("linkMan2", CommonUtils.checkNull(map.get("LINKMAN2")));
            request.setAttribute("transList", list);
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("mainMap", mainMap);
            request.setAttribute("historyList", historyList);
            request.setAttribute("detailList", detailList);
            request.setAttribute("planFlag", CommonUtils.checkNull(request.getParamValue("planFlag")));
            act.setForword(PART_REPLACED_DLR_ORDER_CHECK_CHECK);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_REPLACED_DLR_ORDER_CHECK_QUERY);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "切换订单审核数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_REPLACED_DLR_ORDER_CHECK_QUERY);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2014-3-20
     * @Title :
     * @Description: 切换订单驳回
     */
    public void rebut() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            //去除预占资金
            TtPartAccountRecordPO po = new TtPartAccountRecordPO();
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String reason = CommonUtils.checkNull(request.getParamValue("reason"));
            reason = new String(reason.getBytes("ISO-8859-1"), "UTF-8");
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            if (!(mainMap.get("STATE") + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "")) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单状态已改变!请重新查询!");
                throw e1;
            }

            po.setSourceId(Long.valueOf(orderId));

            //更新订单状态和驳回原因
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            oldPo.setOrderId(Long.valueOf(orderId));

            TtPartDlrOrderMainPO srcPo = new TtPartDlrOrderMainPO();
            srcPo.setOrderId(Long.valueOf(orderId));

            TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
            newPo.setOrderId(Long.valueOf(orderId));
            newPo.setRebutReason(reason);

            //增加是否下级订单判断
            oldPo = (TtPartDlrOrderMainPO) dao.select(oldPo).get(0);
            if (oldPo.getIsSuborder().equals(Constant.IF_TYPE_YES)) {//如果是转下级订单，还原采购单位和销售单位
                TmDealerPO dealerPO = new TmDealerPO();
                dealerPO.setDealerId(oldPo.getRcvOrgid());

                newPo.setDealerId(oldPo.getRcvOrgid());
                newPo.setDealerName(oldPo.getRcvOrg());
                newPo.setDealerCode(((TmDealerPO) dao.select(dealerPO).get(0)).getDealerCode());
                newPo.setSellerId(oldPo.getDealerId());
                newPo.setSellerCode(oldPo.getDealerCode());
                newPo.setSellerName(oldPo.getDealerName());
                newPo.setIsSuborder(Constant.IF_TYPE_NO);
                newPo.setUpdateDate(new Date());
                newPo.setUpdateBy(loginUser.getUserId());
            } else {
                newPo.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05);//驳回
            }
            dao.update(srcPo, newPo);
            String what = "切换订单审核";
            this.saveHistory(request, act, Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05, what, orderId);
            act.setOutData("success", "驳回成功!");
            act.setForword(PART_REPLACED_DLR_ORDER_CHECK_QUERY);
        } catch (Exception e) {
            POContext.endTxn(false);
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_REPLACED_DLR_ORDER_CHECK_QUERY);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "驳回失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_REPLACED_DLR_ORDER_CHECK_QUERY);
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:切换订单审核导出Excel
     */
    public void exportPartCheckExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[12];
            head[0] = "订单类型";
            head[1] = "订单号 ";
            head[2] = "订货单位编码";
            head[3] = "订货单位民称";
            head[4] = "备注";
            head[5] = "订货人";
            head[6] = "提交时间";
            head[7] = "订单状态";
            List<Map<String, Object>> list = dao.queryCheckPartDlrOrder(request);
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[12];
                    detail[0] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                    detail[1] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                    detail[2] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                    detail[3] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[4] = CommonUtils.checkNull(map.get("REMARK"));
                    detail[5] = CommonUtils.checkNull(map.get("BUYER_NAME"));
                    detail[6] = CommonUtils.checkNull(map.get("SUBMIT_DATE"));
                    detail[7] = CommonUtils.checkNull(map.get("STATE"));
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
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:生成回运单,回运时不扣除服务商库存，在验收入库时根据验收数量做相应的扣减。
     */
    @SuppressWarnings("unchecked")
    public void createBack() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            //订单ID
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            //判断是否占用
            if (UPDATE_PART_STATE_SWITCH_BACK) {
                List outIns = new LinkedList<Object>();
                outIns.add(0, orderId);
                outIns.add(1, Constant.PART_CODE_RELATION_41);
                outIns.add(2, 0);// 1:占用 0：释放占用
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", outIns, null);
            }
            //校验是否有同时操作的
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            String state = CommonUtils.checkNull(mainMap.get("STATE"));
            if (state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_13 + "")) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "该订单已回运,请不要重复回运!");
                throw e1;
            }

            state = "backhaul";
            //更新明细
            this.updateOrderDetail(state);

            //更新订单
            this.updateOrderMain(0);

            Long inId = CommonUtils.parseLong(SequenceManager.getSequence(""));

            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail(orderId);

            String[] partIdArr = request.getParamValues("cb");
            boolean flag = true;
            for (int i = 0; i < partIdArr.length; i++) {
//              long  checkQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("check_Qty_" + partIdArr[i])));
                long backQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("BACKHAUL_QTY_" + partIdArr[i])));
                long backQty_new = Long.valueOf(CommonUtils.checkNull(request.getParamValue("BACKHAUL_QTY_NEW_" + partIdArr[i])));
                long maxQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("DEF_QTY_" + partIdArr[i])));
                long backedQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("BACKED_QTY_" + partIdArr[i])));
                long itemQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("item_qty_" + partIdArr[i])));
                long itemQty_new = Long.valueOf(CommonUtils.checkNull(request.getParamValue("item_qty_new_" + partIdArr[i])));
                long repart_id = Long.valueOf(CommonUtils.checkNull(request.getParamValue("repart_id_" + partIdArr[i])) == "" ? "0" :
                        CommonUtils.checkNull(request.getParamValue("repart_id_" + partIdArr[i]))); //切换件ID
                if (itemQty < backQty) {
                    BizException e1 = new BizException("滴" + (i + 1) + "行" + "旧编码回运数量不能大于库存数量!");
                    throw e1;
                }
                if (itemQty_new < backQty_new) {
                    BizException e1 = new BizException("滴" + (i + 1) + "行" + "新编码回运数量不能大于库存数量!");
                    throw e1;
                }
                if (maxQty < (backQty + backQty_new)) {
                    BizException e1 = new BizException("滴" + (i + 1) + "行" + "总回运数量不能大于最大回运数量");
                    throw e1;
                } else {
                    //插入回运记录表
                    if (UPDATE_PART_STATE_SWITCH) {
                        backhaul(inId, Long.valueOf(partIdArr[i]), request);
                        //新件回运信息 add by yuan 20150907 start
                        if (backQty_new > 0) {
                            backhaul(inId, repart_id, request);
                        }
                        //end
                    }
                }
            }

            //修改状态
            if (flag) {
                this.saveHistoryForBackhaul(loginUser, orderId, act, request);
            }

            if (UPDATE_PART_STATE_SWITCH) {
                //调用出库逻辑
                LinkedList<Object> ins = new LinkedList<Object>();
                ins.add(0, inId);
                ins.add(1, Constant.PART_CODE_RELATION_41);
                ins.add(2, 0);//0表示先前未占用(默认),1表示先前已占用
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
            }
            act.setOutData("success", "操作成功!");
            act.setForword(PART_REPLACED_DLR_ORDER_CHECK_QUERY);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            if (e.getCause() instanceof BizException) {
                BizException e1 = (BizException) e.getCause();
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "生成回运单错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param inId
     * @param partId
     * @param request
     */
    private void backhaul(Long inId, Long partId, RequestWrapper request) {
        PurchaseOrderBalanceDao purchaseOrderBalanceDao = PurchaseOrderBalanceDao.getInstance();

        TtPartRecordPO ttPartRecordPO = new TtPartRecordPO();
        ttPartRecordPO.setRecordId(CommonUtils.parseLong(SequenceManager.getSequence("")));
        ttPartRecordPO.setAddFlag(2);//出库标记
        ttPartRecordPO.setState(1);//正常出库
        ttPartRecordPO.setPartNum(Long.valueOf(CommonUtils.checkNull(request.getParamValue("BACKHAUL_QTY_" + partId))));//出库数量
        ttPartRecordPO.setTranstypeId(0l);//默认0
        ttPartRecordPO.setPartId(partId);//配件ID
        ttPartRecordPO.setPartCode(CommonUtils.checkNull(request.getParamValue("part_code_" + partId)));//配件件号
        ttPartRecordPO.setPartOldcode(CommonUtils.checkNull(request.getParamValue("part_oldcode_" + partId)));//配件编码
        ttPartRecordPO.setPartName(CommonUtils.checkNull(request.getParamValue("part_cname_" + partId)));//配件名称
        ttPartRecordPO.setPartBatch("1306");//////////////////配件批次
        ttPartRecordPO.setVenderId(21799l);///////////////////配件供应商
        ttPartRecordPO.setConfigId(Long.valueOf(Constant.PART_CODE_RELATION_41));//出库单
        ttPartRecordPO.setOrderId(inId);//出库单ID
        ttPartRecordPO.setOrderCode(CommonUtils.checkNull(request.getParamValue("ORDER_CODE")));//出库单编码
        ttPartRecordPO.setLineId(Long.valueOf(CommonUtils.checkNull(request.getParamValue("LINE_ID_" + partId))));//行id
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        List<OrgBean> orgBeanList = PartWareHouseDao.getInstance().getOrgInfo(logonUser);
        ttPartRecordPO.setOrgId(orgBeanList.get(0).getOrgId());
        ttPartRecordPO.setOrgCode(orgBeanList.get(0).getOrgCode());
        ttPartRecordPO.setOrgName(orgBeanList.get(0).getOrgName());
        String str = request.getParamValue("whId");
        String[] wareHouse = null;

        TtPartLoactionDefinePO loactionDefinePO = new TtPartLoactionDefinePO();

        if (!CommonUtils.checkIsNullStr(str)) {
            wareHouse = str.split(",");
            ttPartRecordPO.setWhId(Long.valueOf(wareHouse[0]));//仓库id
            ttPartRecordPO.setWhName(CommonUtils.checkNull(wareHouse[1]));//仓库name
            loactionDefinePO.setWhId(Long.valueOf(wareHouse[0]));//仓库id
        }
        loactionDefinePO.setPartId(partId);
        loactionDefinePO.setOrgId(orgBeanList.get(0).getOrgId());
        loactionDefinePO.setState(Constant.STATUS_ENABLE);
        loactionDefinePO.setStatus(1);
        List<TtPartLoactionDefinePO> list = purchaseOrderBalanceDao.select(loactionDefinePO);
        if (list.size() != 0) {
            loactionDefinePO = list.get(0);
        }
//        loactionDefinePO = (TtPartLoactionDefinePO) purchaseOrderBalanceDao.select(loactionDefinePO).get(0);

        ttPartRecordPO.setLocId(loactionDefinePO.getLocId());
        ttPartRecordPO.setLocCode(loactionDefinePO.getLocCode());
        ttPartRecordPO.setOptDate(new Date());
        ttPartRecordPO.setCreateDate(new Date());
        ttPartRecordPO.setPersonId(logonUser.getUserId());
        ttPartRecordPO.setPersonName(logonUser.getName());
        ttPartRecordPO.setPartState(1);

        purchaseOrderBalanceDao.insert(ttPartRecordPO);

    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:验收入库
     */
    @SuppressWarnings("unchecked")
    public void createWarehousing() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        String error = "";
        try {
            request.setAttribute("boFlag", false);
            //订单编号
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            //校验是否有同时操作的
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            String state = CommonUtils.checkNull(mainMap.get("STATE"));
            if (state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_14 + "")) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "该订单已验收入库,请不要重复验收入库!");
                throw e1;
            }
            state = "warehousing";
            //更新明细
            this.updateOrderDetail(state);

            //更新订单
            this.updateOrderMain(0);

            //修改状态
            this.saveHistoryForWarehousing(loginUser, orderId, act, request);

            Long InId = CommonUtils.parseLong(SequenceManager.getSequence(""));
            Long outId = CommonUtils.parseLong(SequenceManager.getSequence(""));
//            Long fcId = CommonUtils.parseLong(SequenceManager.getSequence(""));
            //验收入库
            //先做入库、再入库、最后封存
            String[] partIdArr = request.getParamValues("cb");
            for (int i = 0; i < partIdArr.length; i++) {
                Long partId = Long.valueOf(partIdArr[i]);
                //校验loc_code
                String loc = request.getParamValue("LOC_CODE_" + partId);
                String[] locCode = null;
                if ((!StringUtil.isEmpty(loc)) && loc.contains(",")) {
                    locCode = loc.split(",");
                }
                TtPartLocationPO plp = new TtPartLocationPO();
                plp.setLocCode(locCode[1]);
                List<PO> list = PartDistributeMgrDao.getInstance().select(plp);
                if (list != null && list.size() > 0 && list.get(0) != null) {
                    warehousing(InId, request, partId, "1");//正常入库
                    warehousing(outId, request, partId, "2");//正常出库
//                    warehousing(fcId, request, partId,"3");//正常封存
                } else {
                    BizException e1 = new BizException("该货位编码[" + locCode[1] + "]不存在！");
                    throw e1;
                }
            }

            //调用正常入库逻辑
            List ins = new LinkedList<Object>();
            ins.add(0, InId);
            ins.add(1, Constant.PART_CODE_RELATION_41);
            ins.add(2, 1);
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);

            //调用正常出库逻辑
            List ots = new LinkedList<Object>();
            ots.add(0, outId);
            ots.add(1, Constant.PART_CODE_RELATION_56);//先前未占用
            ots.add(2, 0);
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ots, null);

         /*   //调用正常封存逻辑
            @SuppressWarnings("rawtypes")
            List fcs = new LinkedList<Object>();
            fcs.add(0, fcId);
            fcs.add(1, Constant.PART_CODE_RELATION_41);
            fcs.add(2,1);
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", fcs, null);*/

            //自动生成订单开关
            if (AOTO_CREATE_ORDER_SWITCH) {
                // 验收数量小于审核数量时,自动生成订单
                List<String> list = new ArrayList<String>();
                List<Long> listQty = new ArrayList<Long>();
                long checkQty = 0;
                long warehousingQty = 0;
                for (int i = 0; i < partIdArr.length; i++) {
                    String partId = partIdArr[i];
                    checkQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("check_Qty_" + partId)));
                    warehousingQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("WAREHOUSING_QTY_" + partId)));
                    if (warehousingQty < checkQty) {
                        list.add(partId);
                        listQty.add(checkQty - warehousingQty);
                    }
                }
                // 验收数量小于审核数量时,自动生成订单
                if (list.size() > 0) {
                    //				aotoCreateOrder(list,listQty);//java的自动生成订单 已backUp
                    ArrayList var = new ArrayList();
                    var.add(0, orderId);//订单号
                    var.add(1, Constant.PART_CODE_RELATION_50.toString());//业务类型
                    var.add(2, 0);//不扣钱
                    var.add(3, 0);//预留参数
                    factory.callProcedure("P_CREATEORDER", var, null);
                }
            }
            act.setOutData("success", "操作成功!");
            act.setForword(PART_REPLACED_DLR_ORDER_CHECK_QUERY);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "验收入库出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    private void warehousing(Long inId, RequestWrapper request, Long partId, String flag) {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        List<OrgBean> orgBeanList = PartWareHouseDao.getInstance().getOrgInfo(logonUser);

        TtPartRecordPO ttPartRecordPO = new TtPartRecordPO();
        ttPartRecordPO.setRecordId(CommonUtils.parseLong(SequenceManager.getSequence("")));
        if ("1".equals(flag)) {
            ttPartRecordPO.setAddFlag(1);//入库标记
            ttPartRecordPO.setConfigId(Long.valueOf(Constant.PART_CODE_RELATION_41));//入库单
        } else if ("2".equals(flag)) {
            ttPartRecordPO.setAddFlag(2);//出库标记
            ttPartRecordPO.setConfigId(Long.valueOf(Constant.PART_CODE_RELATION_56));//入库单
        } else if ("3".equals(flag)) {
            ttPartRecordPO.setAddFlag(1);//入库标记
        }
        ttPartRecordPO.setState(1);//处理标记
        ttPartRecordPO.setPartNum(CommonUtils.parseLong(CommonUtils.checkNull(request.getParamValue("WAREHOUSING_QTY_" + partId))));//入库数量
        ttPartRecordPO.setTranstypeId(0l);//默认0
        ttPartRecordPO.setPartId(partId);//配件ID
        ttPartRecordPO.setPartCode(CommonUtils.checkNull(request.getParamValue("part_code_" + partId)));//配件件号
        ttPartRecordPO.setPartOldcode(CommonUtils.checkNull(request.getParamValue("part_Oldcode_" + partId)));//配件编码
        ttPartRecordPO.setPartName(CommonUtils.checkNull(request.getParamValue("part_Cname_" + partId)));//配件名称
        ttPartRecordPO.setPartBatch("1306");//////////////////配件批次
        ttPartRecordPO.setVenderId(21799l);///////////////////配件供应商

        ttPartRecordPO.setOrderId(inId);//入库单ID
        ttPartRecordPO.setOrderCode(CommonUtils.checkNull(request.getParamValue("orderCode")));//入库单编码
        ttPartRecordPO.setLineId(Long.valueOf(CommonUtils.checkNull(request.getParamValue("LINE_ID_" + partId))));

        String str = request.getParamValue("whId");
        String[] wareHouse = null;
        String loc_id = ""; //货位ID
        String dwhId = null;
        String dealerId = null;

        if ("1".equals(flag) || "3".equals(flag)) {
            ttPartRecordPO.setOrgId(orgBeanList.get(0).getOrgId());
            ttPartRecordPO.setOrgCode(orgBeanList.get(0).getOrgCode());
            ttPartRecordPO.setOrgName(orgBeanList.get(0).getOrgName());
            if (!CommonUtils.checkIsNullStr(str)) {
                wareHouse = str.split(",");
                ttPartRecordPO.setWhId(Long.valueOf(wareHouse[0]));//仓库id
                ttPartRecordPO.setWhName(CommonUtils.checkNull(wareHouse[1]));//仓库name
            }
        } else {
            dealerId = dao.getOrderDLRWhid(CommonUtils.checkNull(request.getParamValue("orderId"))).get(0).get("DEALER_ID") + "";
            dwhId = dao.getOrderDLRWhid(CommonUtils.checkNull(request.getParamValue("orderId"))).get(0).get("WH_ID") + "";
            ttPartRecordPO.setWhId(Long.valueOf(dwhId));
            ttPartRecordPO.setOrgId(Long.valueOf(dealerId));
        }

        String loc = request.getParamValue("LOC_CODE_" + partId);
        String[] locCode = null;
        if ((!StringUtil.isEmpty(loc)) && loc.contains(",")) {
            locCode = loc.split(",");
        }

        if (logonUser.getDealerId() == null && "1".equals(flag)) {
            PartDistributeMgrDao plmDao = PartDistributeMgrDao.getInstance();
            List<Map<String, Object>> listLocId = plmDao.getLocId(partId.toString(), locCode[0], wareHouse[0]);
            if (listLocId != null && listLocId.size() > 0 && listLocId.get(0).get("LOC_ID") != null) {
                loc_id = listLocId.get(0).get("LOC_ID").toString();
            } else {
                TtPartLoactionDefinePO dp = new TtPartLoactionDefinePO();
                loc_id = CommonUtils.parseLong(SequenceManager.getSequence("")) + "";
                dp.setLocId(Long.parseLong(loc_id));
                dp.setRelocId(Long.parseLong(locCode[0]));
                dp.setLocCode(locCode[1]);
                dp.setLocName(locCode[2]);
                dp.setPartId(partId);
                dp.setWhId(Long.parseLong(wareHouse[0]));
                dp.setOrgId(logonUser.getOrgId());
                dp.setCreateBy(logonUser.getUserId());
                dp.setCreateDate(new Date());
                dp.setState(Constant.STATUS_ENABLE);
                plmDao.insert(dp);
            }
        } else {
            loc_id = OrderCodeManager.getPartLocId(dealerId, dwhId, partId.toString()) + "";
        }

        ttPartRecordPO.setLocId(Long.valueOf(loc_id));
        ttPartRecordPO.setOptDate(new Date());
        ttPartRecordPO.setCreateDate(new Date());
        ttPartRecordPO.setPersonId(logonUser.getUserId());
        ttPartRecordPO.setPersonName(logonUser.getName());
        if ("1".equals(flag)) {
            ttPartRecordPO.setPartState(1);//正常
        } else if ("2".equals(flag)) {
            ttPartRecordPO.setPartState(1);//正常
        } else if ("3".equals(flag)) {
            ttPartRecordPO.setPartState(2);//封存状态
        }


        dao.insert(ttPartRecordPO);//新增出入库记录
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 替换件回运查询（已审核的）
     */
    public void partDlrOrderBackhaulQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryCheckPartDlrOrderBackhaul(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_REPLACED_DLR_BACK);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "替换件回运查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_REPLACED_DLR_BACK);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 回运订单
     */
    public void backauilDlrOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            mainMap.put("saleName", loginUser.getName());
            //获取账户
            Map<String, Object> accountMap = dao.getAccountMoney(mainMap.get("DEALER_ID").toString());
            String accountSumNow = "";
            String accountKyNow = "";
            String accountDjNow = "";
            boolean accountFlag = false;
            if (null != accountMap) {
                accountFlag = true;
                accountSumNow = accountMap.get("ACCOUNT_SUM").toString();
                accountKyNow = accountMap.get("ACCOUNT_KY").toString();
                accountDjNow = accountMap.get("ACCOUNT_DJ").toString();
            }
            mainMap.put("accountFlag", accountFlag);
            mainMap.put("accountSumNow", accountSumNow);
            mainMap.put("accountKyNow", accountKyNow);
            mainMap.put("accountDjNow", accountDjNow);

            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<OrgBean> lo = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(lo.get(0).getOrgId().toString());
            String sellerId = CommonUtils.checkNull(mainMap.get("SELLER_ID"));
            if (sellerId.equals(Constant.OEM_ACTIVITIES + "")) {
                wareHouseList = dao.getWareHouse(lo.get(0).getOrgId().toString(), CommonUtils.checkNull(mainMap.get("PRODUCE_FAC")));
            }
            //查询订单明细
            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail(orderId);
            List<Map<String, Object>> historyList = dao.queryOrderHistory(orderId);

            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            //获取服务商联系人和联系方式
            String orderDealerId = CommonUtils.checkNull(mainMap.get("DEALER_ID"));
            Map<String, Object> map = dao.getAddrMap(orderDealerId);
            Map<String, Object> dealerMap = dao.getDealerName(orderDealerId);

            request.setAttribute("phone", CommonUtils.checkNull(dealerMap.get("TEL")));
            request.setAttribute("linkMan", CommonUtils.checkNull(dealerMap.get("LINKMAN")));
            request.setAttribute("phone2", CommonUtils.checkNull(map.get("TEL2")));
            request.setAttribute("linkMan2", CommonUtils.checkNull(map.get("LINKMAN2")));
            request.setAttribute("transList", list);
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("mainMap", mainMap);
            request.setAttribute("historyList", historyList);
            request.setAttribute("detailList", detailList);
            request.setAttribute("WULIU_CODE", detailList.get(0).get("WULIU_CODE"));
            request.setAttribute("WULIU_COMPANY", detailList.get(0).get("WULIU_COMPANY"));
            request.setAttribute("planFlag", CommonUtils.checkNull(request.getParamValue("planFlag")));
            act.setForword(PART_REPLACED_DLR_BACKHAUL);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_REPLACED_DLR_BACK);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "回运订单查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_REPLACED_DLR_BACK);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 验收入库
     */
    public void warehousingDlrOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            mainMap.put("saleName", loginUser.getName());
            //获取账户
            Map<String, Object> accountMap = dao.getAccountMoney(mainMap.get("DEALER_ID").toString());
            String accountSumNow = "";
            String accountKyNow = "";
            String accountDjNow = "";
            boolean accountFlag = false;
            if (null != accountMap) {
                accountFlag = true;
                accountSumNow = accountMap.get("ACCOUNT_SUM").toString();
                accountKyNow = accountMap.get("ACCOUNT_KY").toString();
                accountDjNow = accountMap.get("ACCOUNT_DJ").toString();
            }
            mainMap.put("accountFlag", accountFlag);
            mainMap.put("accountSumNow", accountSumNow);
            mainMap.put("accountKyNow", accountKyNow);
            mainMap.put("accountDjNow", accountDjNow);

            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<OrgBean> lo = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
            List<Map<String, Object>> wareHouseList = null;
            String sellerId = CommonUtils.checkNull(mainMap.get("SELLER_ID"));
            if (sellerId.equals(Constant.OEM_ACTIVITIES + "")) {
                wareHouseList = dao.getWareHouse(lo.get(0).getOrgId().toString());
            }
            //查询订单明细
            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail2(orderId);
            List<Map<String, Object>> historyList = dao.queryOrderHistory(orderId);

            List<TtPartFixcodeDefinePO> list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            //获取服务商联系人和联系方式
            String orderDealerId = CommonUtils.checkNull(mainMap.get("DEALER_ID"));
            Map<String, Object> map = dao.getAddrMap(orderDealerId);
            Map<String, Object> dealerMap = dao.getDealerName(orderDealerId);

            request.setAttribute("phone", CommonUtils.checkNull(dealerMap.get("TEL")));
            request.setAttribute("linkMan", CommonUtils.checkNull(dealerMap.get("LINKMAN")));
            request.setAttribute("phone2", CommonUtils.checkNull(map.get("TEL2")));
            request.setAttribute("linkMan2", CommonUtils.checkNull(map.get("LINKMAN2")));
            request.setAttribute("transList", list);
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("mainMap", mainMap);
            request.setAttribute("historyList", historyList);
            request.setAttribute("detailList", detailList);
            request.setAttribute("planFlag", CommonUtils.checkNull(request.getParamValue("planFlag")));
            act.setForword(PART_REPLACED_DLR_ORDER_WAREHOUSING);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_REPLACED_DLR_ORDER_WAREHOUSINGINIT);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "验收入库数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_REPLACED_DLR_ORDER_WAREHOUSINGINIT);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @Date : 2013-4-18
     * @Title :
     * @Description: 切换订单查询
     */
    public void queryReplacedPartDlrOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartDlrOrder(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_REPLACED_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "切换订单查询查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_REPLACED_DLR_ORDER);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @Date : 2014-3-21
     * @Title :
     * @Description: 切换订单查询
     */
    public void replacedPartDlrOrderQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            if ("2".equals(radioSelect)) {
                ps = dao.partDlrOrderDtlQuery(request, curPage, Constant.PAGE_SIZE);
            } else if ("1".equals(radioSelect)) {
                ps = dao.partDlrOrderStateQuery(request, curPage, Constant.PAGE_SIZE);
            } else {
                ps = dao.partDlrOrderSumQuery(request, curPage, Constant.PAGE_SIZE);
            }
            act.setOutData("ps", ps);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_REPLACED_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "切换订单查询查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_REPLACED_DLR_ORDER);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @Date : 2013-4-19
     * @Title :
     * @Description: 跳转到增加页面
     */
    public void addOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        //前台页面数据集合
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            List<TtPartFixcodeDefinePO> list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式

            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            //仓库
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<OrgBean> lo = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(lo.get(0).getOrgId().toString());
            //获取单位ID
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            String dealerName = "";
            String dealerCode = "";
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
                dealerName = beanList.get(0).getOrgName();
                dealerCode = beanList.get(0).getOrgCode();
            }
            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);

            dataMap.put("buyerName", loginUser.getName());
            dataMap.put("dealerId", dealerId);
            dataMap.put("dealerName", dealerName);
            dataMap.put("dealerCode", dealerCode);

            //设置默认销售单位 (只考虑有一个上级销售单位)
            String SELLER_NAME = "";
            String SELLER_CODE = "";
            String SELLER_ID = "";
            String accountId = "";
            String accountSum = "";
            String accountKy = "";
            String accountDj = "";

            PageResult<Map<String, Object>> ps = dao.getSalesRelation(dealerId, "1", "", "", "", Constant.PAGE_SIZE_MAX, 1);
            List<Map<String, Object>> selList = null;
            if (null != ps && ps.getRecords().size() > 0) {
                selList = ps.getRecords();
                SELLER_NAME = selList.get(0).get("PARENTORG_NAME").toString();
                SELLER_CODE = selList.get(0).get("PARENTORG_CODE").toString();
                SELLER_ID = selList.get(0).get("PARENTORG_ID").toString();
                accountId = selList.get(0).get("ACCOUNT_ID").toString();
                accountSum = selList.get(0).get("ACCOUNT_SUM").toString();
                accountKy = selList.get(0).get("ACCOUNT_KY").toString();
                accountDj = selList.get(0).get("ACCOUNT_DJ").toString();
            }

            dataMap.put("SELLER_NAME", SELLER_NAME);
            dataMap.put("SELLER_CODE", SELLER_CODE);
            dataMap.put("SELLER_ID", SELLER_ID);
            dataMap.put("accountId", accountId);
            dataMap.put("accountSum", accountSum);
            dataMap.put("accountKy", accountKy);
            dataMap.put("accountDj", accountDj);
            //设置默认值
            Map<String, Object> defaultValueMap = new HashMap<String, Object>();
            String defaultStation = "";
            String defaultLinkman = "";
            String defaultTel = "";
            String defaultPostCode = "";
            String defaultAddrId = "";
            String defaultAddr = "";
            defaultValueMap.put("defaultRcvOrgid", dealerId);
            defaultValueMap.put("defaultRcvOrg", dealerName);
            //默认地址
            PageResult<Map<String, Object>> addrPs = dao.getSalesRelation(dealerId, "3", "", "", "", 10, 1);
            if (addrPs != null) {
                List<Map<String, Object>> addrList = addrPs.getRecords();
                if (addrList != null && addrList.size() > 0 && addrList.get(0) != null) {
                    defaultStation = CommonUtils.checkNull(addrList.get(0).get("STATION"));
                    defaultLinkman = CommonUtils.checkNull(addrList.get(0).get("LINKMAN"));
                    defaultTel = CommonUtils.checkNull(addrList.get(0).get("TEL"));
                    defaultPostCode = CommonUtils.checkNull(addrList.get(0).get("POST_CODE"));
                    defaultAddrId = CommonUtils.checkNull(addrList.get(0).get("ADDR_ID"));
                    defaultAddr = CommonUtils.checkNull(addrList.get(0).get("ADDR"));
                }
            }
            defaultValueMap.put("defaultStation", defaultStation);
            defaultValueMap.put("defaultLinkman", defaultLinkman);
            defaultValueMap.put("defaultTel", defaultTel);
            defaultValueMap.put("defaultPostCode", defaultPostCode);
            defaultValueMap.put("defaultAddrId", defaultAddrId);
            defaultValueMap.put("defaultAddr", defaultAddr);

            request.setAttribute("defaultValueMap", defaultValueMap);
            dataMap.put("now", now);
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("dataMap", dataMap);
            request.setAttribute("transList", list);
            request.setAttribute("orderType", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10);
            Map<String, Object> actCodeMap = new HashMap<String, Object>();
            TtPartSpecialDefinePO po = new TtPartSpecialDefinePO();
            List<Map<String, Object>> actCodelist = this.dao.getCurrentActCode(dealerId);
//            for (int i = 0; i < actCodelist.size(); i++) {
//            	actCodeMap.put("actCodeMap", CommonUtils.checkNull(actCodelist.get(i).get("ACTIVITY_CODE")));
//			}
            request.setAttribute("actCodeMap", actCodelist);


            act.setForword(PART_REPLACED_DLR_ORDER_ADD);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_REPLACED_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "替换件订单报新增时错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_REPLACED_DLR_ORDER);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 查看
     */
    public void detailDlrOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String buttonFalg = "";//采购订单查看 隐藏 【返回】按钮
            if (null != request.getParamValue("buttonFalg")) {
                buttonFalg = request.getParamValue("buttonFalg");
            }
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail(orderId);
            List<Map<String, Object>> historyList = dao.queryOrderHistory(orderId);
            Map<String, Object> transMap = this.getTransMap();
            String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
            transType = CommonUtils.checkNull(transMap.get(transType));
            mainMap.put("TRANS_TYPE", transType);
            request.setAttribute("mainMap", mainMap);
            request.setAttribute("historyList", historyList);
            act.setOutData("loginUser", loginUser.getCompanyId());
            request.setAttribute("detailList", detailList);

            List<Map<String, Object>> ActCodelist = dao.getActCodeByOrderId(orderId);
            String actCode = CommonUtils.checkNull(ActCodelist.get(0).get("ACTIVITY_CODE"));
            List<Map<String, Object>> partTpyeList = dao.getPartType(actCode);
            String partTpye = CommonUtils.checkNull(partTpyeList.get(0).get("PART_TYPE"));
            String partTpyeCode = CommonUtils.checkNull(dao.getCode(partTpye).get(0).get("CODE_DESC"));
            request.setAttribute("PART_TYPE", partTpyeCode);

            //zhumingwei 2013-09-16
            act.setOutData("buttonFalg", buttonFalg);
            //zhumingwei 2013-09-16

            act.setOutData("orderId", orderId);
            act.setForword(PART_REPLACED_DLR_ORDER_CHECK_DETAIL);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_REPLACED_DLR_ORDER_CHECK_QUERY);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "切换订单提报查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_REPLACED_DLR_ORDER_CHECK_QUERY);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 查看
     */
    public void orderDetailQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));

            //zhumingwei 2013-09-16
            String buttonFalg = "";//采购订单查看 隐藏 【返回】按钮
            if (null != request.getParamValue("buttonFalg")) {
                buttonFalg = request.getParamValue("buttonFalg");
            }
            //zhumingwei 2013-09-16

            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail(orderId);
            List<Map<String, Object>> historyList = dao.queryOrderHistory(orderId);
            Map<String, Object> transMap = this.getTransMap();
            String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
            transType = CommonUtils.checkNull(transMap.get(transType));
            mainMap.put("TRANS_TYPE", transType);
            request.setAttribute("mainMap", mainMap);
            request.setAttribute("historyList", historyList);
            request.setAttribute("detailList", detailList);
            act.setOutData("loginUser", loginUser.getCompanyId());
            //zhumingwei 2013-09-16
            act.setOutData("buttonFalg", buttonFalg);
            //zhumingwei 2013-09-16

            List<Map<String, Object>> ActCodelist = dao.getActCodeByOrderId(orderId);
            String actCode = CommonUtils.checkNull(ActCodelist.get(0).get("ACTIVITY_CODE"));
            List<Map<String, Object>> partTpyeList = dao.getPartType(actCode);
            String partTpye = CommonUtils.checkNull(partTpyeList.get(0).get("PART_TYPE"));
            String partTpyeCode = CommonUtils.checkNull(dao.getCode(partTpye).get(0).get("CODE_DESC"));
            request.setAttribute("PART_TYPE", partTpyeCode);
            act.setOutData("orderId", orderId);
            act.setForword(PART_REPLACED_DLR_ORDER_CHECK_DETAIL);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_REPLACED_DLR_ORDER_QUEYRINIT);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "切换订单明细信息数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_REPLACED_DLR_ORDER_QUEYRINIT);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-12-10
     * @Title : 订单明细导出
     */
    public void exportOrderExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orderCode = CommonUtils.checkNull(request
                    .getParamValue("orderCode")); // 订单单号
            String orderId = CommonUtils.checkNull(request
                    .getParamValue("orderId")); // 订单ID
            String state = CommonUtils.checkNull(request
                    .getParamValue("STATE")); // 订单ID
            List<Map<String, Object>> ActCodelist = dao.getActCodeByOrderId(orderId);
            String actCode = CommonUtils.checkNull(ActCodelist.get(0).get("ACTIVITY_CODE"));
            List<Map<String, Object>> partTpyeList = dao.getPartType(actCode);
            String partTpye = CommonUtils.checkNull(partTpyeList.get(0).get("PART_TYPE"));
            String partTpyeCode = CommonUtils.checkNull(dao.getCode(partTpye).get(0).get("CODE_DESC"));

            String[] head = new String[15];
            switch (Integer.valueOf(state)) {
                case 92161002:
                    if (logonUser.getOrgType().equals(Constant.ORG_TYPE_OEM)) {
                        head[0] = "序号";
                        head[1] = "配件编码";
                        head[2] = "配件名称";
                        head[3] = "替换件编码";
                        head[4] = "替换件名称";
                        head[5] = "当前库存数量";
                        head[6] = "切换数量";
                        head[7] = "备注";
                    } else {
                        head[0] = "序号";
                        head[1] = "配件编码";
                        head[2] = "配件名称";
                        head[3] = "当前库存数量";
                        head[4] = "切换数量";
                        head[5] = "替换件编码";
                        head[6] = "替换件名称";
                        head[7] = "活动配件类型";
                        head[8] = "是否需要回运";
                        head[9] = "备注";
                    }
                    break;
                case 92161003:
                    if (logonUser.getOrgType().equals(Constant.ORG_TYPE_OEM)) {
                        head[0] = "序号";
                        head[1] = "配件编码";
                        head[2] = "配件名称";
                        head[3] = "替换件编码";
                        head[4] = "替换件名称";
                        head[5] = "当前库存数量";
                        head[6] = "切换数量";
                        head[7] = "审核数量";
                        head[8] = "备注";
                    } else {
                        head[0] = "序号";
                        head[1] = "配件编码";
                        head[2] = "配件名称";
                        head[3] = "当前库存数量";
                        head[4] = "切换数量";
                        head[5] = "审核数量";
                        head[6] = "替换件编码";
                        head[7] = "替换件名称";
                        head[8] = "活动配件类型";
                        head[9] = "是否需要回运";
                        head[10] = "备注";
                    }
                    break;
                case 92161013:
                    if (logonUser.getOrgType().equals(Constant.ORG_TYPE_OEM)) {
                        head[0] = "序号";
                        head[1] = "配件编码";
                        head[2] = "配件名称";
                        head[3] = "替换件编码";
                        head[4] = "替换件名称";
                        head[5] = "当前库存数量";
                        head[6] = "切换数量";
                        head[7] = "审核数量";
                        head[8] = "回运数量";
                        head[9] = "备注";

                    } else {

                        head[0] = "序号";
                        head[1] = "配件编码";
                        head[2] = "配件名称";
                        head[3] = "当前库存数量";
                        head[4] = "切换数量";
                        head[5] = "审核数量";
                        head[6] = "回运数量";
                        head[7] = "替换件编码";
                        head[8] = "替换件名称";
                        head[9] = "活动配件类型";
                        head[10] = "是否需要回运";
                        head[11] = "备注";
                    }
                    break;
                case 92161014:
                    if (logonUser.getOrgType().equals(Constant.ORG_TYPE_OEM)) {
                        head[0] = "序号";
                        head[1] = "配件编码";
                        head[2] = "配件名称";
                        head[3] = "替换件编码";
                        head[4] = "替换件名称";
                        head[5] = "当前库存数量";
                        head[6] = "切换数量";
                        head[7] = "审核数量";
                        head[8] = "回运数量";
                        head[9] = "验收入库数量";
                        head[10] = "备注";
                    } else {
                        head[0] = "序号";
                        head[1] = "配件编码";
                        head[2] = "配件名称";
                        head[3] = "当前库存数量";
                        head[4] = "切换数量";
                        head[5] = "审核数量";
                        head[6] = "回运数量";
                        head[7] = "验收入库数量";
                        head[8] = "替换件编码";
                        head[9] = "替换件名称";
                        head[10] = "备注";
                    }

                    break;

                default:
                    if (logonUser.getOrgType().equals(Constant.ORG_TYPE_OEM)) {
                        head[0] = "序号";
                        head[1] = "配件编码";
                        head[2] = "配件名称";
                        head[3] = "替换件编码";
                        head[4] = "替换件名称";
                        head[5] = "当前库存数量";
                        head[6] = "切换数量";
                        head[7] = "备注";

                    } else {

                        head[0] = "序号";
                        head[1] = "配件编码";
                        head[2] = "配件名称";
                        head[3] = "当前库存数量";
                        head[4] = "切换数量";
                        head[5] = "替换件编码";
                        head[6] = "替换件名称";
                        head[7] = "活动配件类型";
                        head[8] = "是否需要回运";
                        head[9] = "备注";
                    }
                    break;
            }

            List<Map<String, Object>> list = dao.queryPartDlrOrderDetail(orderId);
            List<String[]> list1 = new ArrayList<String[]>();
            if (list != null && list.size() != 0) {
                String[] detail;
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = (Map<String, Object>) list.get(i);
                    if (map != null && map.size() != 0) {
                        detail = new String[15];
                        switch (Integer.valueOf(state)) {
                            case 92161002:
                                if (logonUser.getOrgType().equals(Constant.ORG_TYPE_OEM)) {
                                    detail[0] = CommonUtils.checkNull(i + 1);
                                    detail[1] = CommonUtils.checkNull(map
                                            .get("PART_OLDCODE"));
                                    detail[2] = CommonUtils.checkNull(map
                                            .get("PART_CNAME"));
                                    detail[3] = CommonUtils.checkNull(map
                                            .get("REPART_OLDCODE"));
                                    detail[4] = CommonUtils.checkNull(map
                                            .get("REPART_NAME"));
                                    detail[5] = CommonUtils.checkNull(map
                                            .get("REPART_STOCK_QTY"));
                                    detail[6] = CommonUtils.checkNull(map
                                            .get("BUY_QTY"));
                                    detail[7] = CommonUtils.checkNull(map
                                            .get("REMARK"));
                                } else {
                                    detail[0] = CommonUtils.checkNull(i + 1);
                                    detail[1] = CommonUtils.checkNull(map
                                            .get("PART_OLDCODE"));
                                    detail[2] = CommonUtils.checkNull(map
                                            .get("PART_CNAME"));
                                    detail[3] = CommonUtils.checkNull(map
                                            .get("STOCK_QTY"));
                                    detail[4] = CommonUtils.checkNull(map
                                            .get("BUY_QTY"));
                                    detail[5] = CommonUtils.checkNull(map
                                            .get("REPART_OLDCODE"));
                                    detail[6] = CommonUtils.checkNull(map
                                            .get("REPART_NAME"));
                                    detail[7] = partTpyeCode;
                                    detail[8] = dao.getCode(CommonUtils.checkNull(map.get("ISNEED_FLAG"))).get(0).get("CODE_DESC") + "";
                                    detail[9] = CommonUtils.checkNull(map
                                            .get("REMARK"));
                                }
                                break;
                            case 92161003:
                                if (logonUser.getOrgType().equals(Constant.ORG_TYPE_OEM)) {
                                    detail[0] = CommonUtils.checkNull(i + 1);
                                    detail[1] = CommonUtils.checkNull(map
                                            .get("PART_OLDCODE"));
                                    detail[2] = CommonUtils.checkNull(map
                                            .get("PART_CNAME"));
                                    detail[3] = CommonUtils.checkNull(map
                                            .get("REPART_OLDCODE"));
                                    detail[4] = CommonUtils.checkNull(map
                                            .get("REPART_NAME"));
                                    detail[5] = CommonUtils.checkNull(map
                                            .get("REPART_STOCK_QTY"));
                                    detail[6] = CommonUtils.checkNull(map
                                            .get("BUY_QTY"));
                                    detail[7] = CommonUtils.checkNull(map
                                            .get("CHECK_QTY"));
                                    detail[8] = CommonUtils.checkNull(map
                                            .get("REMARK"));
                                } else {
                                    detail[0] = CommonUtils.checkNull(i + 1);
                                    detail[1] = CommonUtils.checkNull(map
                                            .get("PART_OLDCODE"));
                                    detail[2] = CommonUtils.checkNull(map
                                            .get("PART_CNAME"));
                                    detail[3] = CommonUtils.checkNull(map
                                            .get("STOCK_QTY"));
                                    detail[4] = CommonUtils.checkNull(map
                                            .get("BUY_QTY"));
                                    detail[5] = CommonUtils.checkNull(map
                                            .get("CHECK_QTY"));
                                    detail[6] = CommonUtils.checkNull(map
                                            .get("REPART_OLDCODE"));
                                    detail[7] = CommonUtils.checkNull(map
                                            .get("REPART_NAME"));
                                    detail[8] = partTpyeCode;
                                    detail[9] = dao.getCode(CommonUtils.checkNull(map.get("ISNEED_FLAG"))).get(0).get("CODE_DESC") + "";
                                    detail[10] = CommonUtils.checkNull(map
                                            .get("REMARK"));
                                }

                                break;
                            case 92161013:
                                if (logonUser.getOrgType().equals(Constant.ORG_TYPE_OEM)) {
                                    detail[0] = CommonUtils.checkNull(i + 1);
                                    detail[1] = CommonUtils.checkNull(map
                                            .get("PART_OLDCODE"));
                                    detail[2] = CommonUtils.checkNull(map
                                            .get("PART_CNAME"));
                                    detail[3] = CommonUtils.checkNull(map
                                            .get("REPART_OLDCODE"));
                                    detail[4] = CommonUtils.checkNull(map
                                            .get("REPART_NAME"));
                                    detail[5] = CommonUtils.checkNull(map
                                            .get("REPART_STOCK_QTY"));
                                    detail[6] = CommonUtils.checkNull(map
                                            .get("BUY_QTY"));
                                    detail[7] = CommonUtils.checkNull(map
                                            .get("CHECK_QTY"));
                                    detail[8] = CommonUtils.checkNull(map
                                            .get("BACKHAUL_QTY"));
                                    detail[9] = CommonUtils.checkNull(map
                                            .get("REMARK"));

                                } else {

                                    detail[0] = CommonUtils.checkNull(i + 1);
                                    detail[1] = CommonUtils.checkNull(map
                                            .get("PART_OLDCODE"));
                                    detail[2] = CommonUtils.checkNull(map
                                            .get("PART_CNAME"));
                                    detail[3] = CommonUtils.checkNull(map
                                            .get("STOCK_QTY"));
                                    detail[4] = CommonUtils.checkNull(map
                                            .get("BUY_QTY"));
                                    detail[5] = CommonUtils.checkNull(map
                                            .get("CHECK_QTY"));
                                    detail[6] = CommonUtils.checkNull(map
                                            .get("BACKHAUL_QTY"));
                                    detail[7] = CommonUtils.checkNull(map
                                            .get("REPART_OLDCODE"));
                                    detail[8] = CommonUtils.checkNull(map
                                            .get("REPART_NAME"));
                                    detail[9] = partTpyeCode;
                                    detail[10] = dao.getCode(CommonUtils.checkNull(map.get("ISNEED_FLAG"))).get(0).get("CODE_DESC") + "";
                                    detail[11] = CommonUtils.checkNull(map
                                            .get("REMARK"));
                                }
                                break;
                            case 92161014:
                                if (logonUser.getOrgType().equals(Constant.ORG_TYPE_OEM)) {
                                    detail[0] = CommonUtils.checkNull(i + 1);
                                    detail[1] = CommonUtils.checkNull(map
                                            .get("PART_OLDCODE"));
                                    detail[2] = CommonUtils.checkNull(map
                                            .get("PART_CNAME"));
                                    detail[3] = CommonUtils.checkNull(map
                                            .get("REPART_OLDCODE"));
                                    detail[4] = CommonUtils.checkNull(map
                                            .get("REPART_NAME"));
                                    detail[5] = CommonUtils.checkNull(map
                                            .get("REPART_STOCK_QTY"));
                                    detail[6] = CommonUtils.checkNull(map
                                            .get("BUY_QTY"));
                                    detail[7] = CommonUtils.checkNull(map
                                            .get("CHECK_QTY"));
                                    detail[8] = CommonUtils.checkNull(map
                                            .get("BACKHAUL_QTY"));
                                    detail[9] = CommonUtils.checkNull(map
                                            .get("WAREHOUSING_QTY"));

                                    detail[10] = CommonUtils.checkNull(map
                                            .get("REMARK"));
                                } else {
                                    detail[0] = CommonUtils.checkNull(i + 1);
                                    detail[1] = CommonUtils.checkNull(map
                                            .get("PART_OLDCODE"));
                                    detail[2] = CommonUtils.checkNull(map
                                            .get("PART_CNAME"));
                                    detail[3] = CommonUtils.checkNull(map
                                            .get("STOCK_QTY"));
                                    detail[4] = CommonUtils.checkNull(map
                                            .get("BUY_QTY"));
                                    detail[5] = CommonUtils.checkNull(map
                                            .get("CHECK_QTY"));
                                    detail[6] = CommonUtils.checkNull(map
                                            .get("BACKHAUL_QTY"));
                                    detail[7] = CommonUtils.checkNull(map
                                            .get("WAREHOUSING_QTY"));
                                    detail[8] = CommonUtils.checkNull(map
                                            .get("REPART_OLDCODE"));
                                    detail[9] = CommonUtils.checkNull(map
                                            .get("REPART_NAME"));
                                    detail[10] = CommonUtils.checkNull(map
                                            .get("REMARK"));
                                }

                                break;

                            default:
                                if (logonUser.getOrgType().equals(Constant.ORG_TYPE_OEM)) {
                                    detail[0] = CommonUtils.checkNull(i + 1);
                                    detail[1] = CommonUtils.checkNull(map
                                            .get("PART_OLDCODE"));
                                    detail[2] = CommonUtils.checkNull(map
                                            .get("PART_CNAME"));
                                    detail[3] = CommonUtils.checkNull(map
                                            .get("REPART_OLDCODE"));
                                    detail[4] = CommonUtils.checkNull(map
                                            .get("REPART_NAME"));
                                    detail[5] = CommonUtils.checkNull(map
                                            .get("REPART_STOCK_QTY"));
                                    detail[6] = CommonUtils.checkNull(map
                                            .get("BUY_QTY"));
                                    detail[7] = CommonUtils.checkNull(map
                                            .get("REMARK"));

                                } else {

                                    detail[0] = CommonUtils.checkNull(i + 1);
                                    detail[1] = CommonUtils.checkNull(map
                                            .get("PART_OLDCODE"));
                                    detail[2] = CommonUtils.checkNull(map
                                            .get("PART_CNAME"));
                                    detail[3] = CommonUtils.checkNull(map
                                            .get("STOCK_QTY"));
                                    detail[4] = CommonUtils.checkNull(map
                                            .get("BUY_QTY"));
                                    detail[5] = CommonUtils.checkNull(map
                                            .get("REPART_OLDCODE"));
                                    detail[6] = CommonUtils.checkNull(map
                                            .get("REPART_NAME"));
                                    detail[7] = partTpyeCode;
                                    detail[8] = dao.getCode(CommonUtils.checkNull(map.get("ISNEED_FLAG"))).get(0).get("CODE_DESC") + "";
                                    detail[9] = CommonUtils.checkNull(map
                                            .get("REMARK"));
                                }
                                break;
                        }
                        list1.add(detail);
                    }
                }
            }
            String fileName = "订单[" + orderCode + "]明细";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出采购订单明细失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @Date : 2013-4-19
     * @Title :
     * @Description: 查询替换件信息
     */
    public void showPartBase() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = req.getParamValue("curPage") != null ? Integer
                    .parseInt(req.getParamValue("curPage"))
                    : 1; // 处理当前页
            String dealerId = CommonUtils.checkNull(req.getParamValue("dealerId"));
            req.setAttribute("bookDealerId", dealerId);
            PageResult<Map<String, Object>> ps;
            ps = dao.showRepplacedPartBase(req, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询替换件信息出错,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @Date : 2013-4-19
     * @Title :
     * @Description: 查询替换件信息
     */
    public void showPartBaseForMod() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = req.getParamValue("curPage") != null ? Integer
                    .parseInt(req.getParamValue("curPage"))
                    : 1; // 处理当前页
            String dealerId = CommonUtils.checkNull(req.getParamValue("dealerId"));
            req.setAttribute("bookDealerId", dealerId);
            PageResult<Map<String, Object>> ps;
            ps = dao.showRepplacedPartBaseForMod(req, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询替换件件信息出错,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @Date : 2013-4-19
     * @Title :
     * @Description: 保存替换件订单
     */
    public void saveOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        String err = null;
        try {
            String dealerId = CommonUtils.checkNull(req.getParamValue("dealerId"));
            String parentId = CommonUtils.checkNull(req.getParamValue("SELLER_ID"));

            Map<String, Object> acountMap = dao.getAccount(dealerId, parentId, "");
            //保存详细订单
            String[] partArr = req.getParamValues("cb");
            Double mainAmount = 0.00D;
            Long orderId = Long.parseLong(SequenceManager.getSequence(""));
            req.setAttribute("orderId", orderId);
            String msg = "";
            req.setAttribute("msg", msg);
//            validatePkgBox();
            if (null != partArr) {
                for (int i = 0; i < partArr.length; i++) {
                    this.saveDetailPo(req, act, partArr[i]);
//                    mainAmount = Arith.add(mainAmount, detailMoney);不需要计算订单金额
                }
            }
            String seq = CommonUtils.checkNull(req.getParamValue("seq"));
            if (!"".equals(seq)) {
                List<Map<String, Object>> list = dao.getUploadList(seq);
                for (Map<String, Object> map : list) {
                    Double amount = saveDetailFromUpload(req, map, act);
                    mainAmount = Arith.add(mainAmount, amount);
                }
                List detailList = dao.queryPartDlrOrderDetail(req.getAttribute("orderId") + "");
                if (null == detailList || detailList.size() <= 0) {
                    BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "没有明细可以保存!");
                    throw e1;
                }
            }
            msg = CommonUtils.checkNull(req.getAttribute("msg"));
            if (!"".equals(msg)) {
                BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, msg);
                throw e1;
            }
            //保存主订单
            mainAmount = this.saveMainPoForBackhual(req, act, mainAmount);
            String what = "切换订单提报";
            this.saveHistory(req, act, Integer.valueOf(CommonUtils.checkNull(req.getParamValue("state"))), what, orderId.toString());
            //资源校验开关控制
            if (UPDATE_PART_STATE_SWITCH) {
                if (null != partArr) {
                    for (int i = 0; i < partArr.length; i++) {
                        long stockQty = Long.valueOf(req.getParamValue("stockQty_" + partArr[i]));
                        long buyQty = Long.valueOf(req.getParamValue("buyQty_" + partArr[i]));
                        String partOldCode = CommonUtils.checkNull(req.getParamValue("partOldcode_" + partArr[i]));
                        if (stockQty < buyQty) {
                            err = "第" + (i + 1) + "行配件【" + partOldCode + "】,库存不足!";
                            BizException e1 = new BizException(act, new Exception(), err);
                            throw e1;
                        }
                    }
                }
            }

//            if (null != partArr) {
//                for (int i = 0; i < partArr.length; i++) {
//                	long stockQty = Long.valueOf(req.getParamValue("stockQty_" + partArr[i]));
//                	long buyQty = Long.valueOf(req.getParamValue("buyQty_" + partArr[i]));
//                    if(stockQty < buyQty){
//                    	BizException e1 = new BizException(act, new Exception(), partArr[i]+"库存不足");
//                        throw e1;
//                    }
//                }
//            }
            //调用库存占用逻辑
            if (UPDATE_PART_STATE_SWITCH) {
                List ins = new LinkedList<Object>();
                ins.add(0, orderId);
                ins.add(1, Constant.PART_CODE_RELATION_41);
                ins.add(2, 1);// 1:占用 0：释放占用
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
            }
            String orderCode = CommonUtils.checkNull(req.getAttribute("orderCode"));
            act.setOutData("orderCode", orderCode);

            act.setOutData("success", "订单：" + orderCode + ",操作成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, err);
            logger.error(logonUser, e1);
            act.setException(e1);
        } finally {
            try {
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "保存订单详细信息出错,请联系管理员!");
            }
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 校验余额
     */
    public boolean validateSum(String dealerId, String parentId, String sum, String accountId) throws Exception {
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        Map<String, Object> acountMap = dao.getAccount(dealerId, parentId, accountId);
        String sumNow = acountMap.get("ACCOUNT_KY").toString();
        try {
            if (parseDouble(sumNow) < parseDouble(sum)) {
                return false;
            }
            return true;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 保存主订单
     */
    private Double saveMainPo(long orderId, RequestWrapper req, ActionContext act, Double mainAmount) throws Exception {
        String exStr = "";
        String orderCode = "";
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            TtPartDlrOrderMainPO po = new TtPartDlrOrderMainPO();
            String produceFac = CommonUtils.checkNull(req.getParamValue("produceFac"));
            po.setOrderId(orderId);
            orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_41, req.getParamValue("dealerId"));
            req.setAttribute("orderCode", orderCode);
            po.setProduceFac(produceFac);
            po.setOrderCode(orderCode);
            po.setOrderType(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02);
            po.setPayType(Integer.valueOf(CommonUtils.checkNull(req.getParamValue("PAY_TYPE"))));
            try {
                po.setDealerId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("dealerId"))));
            } catch (Exception ex) {
                exStr = "订货单位出错";
                throw ex;
            }
            po.setDealerCode(CommonUtils.checkNull(req.getParamValue("dealerCode")));
            po.setDealerName(CommonUtils.checkNull(req.getParamValue("dealerName")));
            try {
                po.setSellerId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("SELLER_ID"))));
            } catch (Exception ex) {
                exStr = "销售单位出错";
                throw ex;
            }
            po.setSellerCode(CommonUtils.checkNull(req.getParamValue("SELLER_CODE")));
            po.setSellerName(CommonUtils.checkNull(req.getParamValue("SELLER_NAME")));
            po.setBuyerId(logonUser.getUserId());
            po.setBuyerName(CommonUtils.checkNull(req.getParamValue("buyerName")));
            po.setRcvOrg(CommonUtils.checkNull(req.getParamValue("RCV_ORG")));
            try {
                po.setAddrId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("ADDR_ID"))));
            } catch (Exception ex) {
                exStr = "接收地址出错";
                throw ex;
            }
            po.setAddr(CommonUtils.checkNull(req.getParamValue("ADDR")));
            po.setReceiver(CommonUtils.checkNull(req.getParamValue("RECEIVER")));
            po.setTel(CommonUtils.checkNull(req.getParamValue("TEL")));
            po.setPostCode(CommonUtils.checkNull(req.getParamValue("POST_CODE")));
            po.setStation(CommonUtils.checkNull(req.getParamValue("STATION")));
            po.setTransType(CommonUtils.checkNull(req.getParamValue("TRANS_TYPE")));
            if (null != req.getParamValue("accountSum") && !"null".equals(req.getParamValue("accountSum"))) {
                po.setAccountSum(Double.valueOf(req.getParamValue("accountSum").replace(",", "")));
            }
            if (null != req.getParamValue("accountKy") && !"null".equals(req.getParamValue("accountKy"))) {
                po.setAccountKy(Double.valueOf(req.getParamValue("accountKy").replace(",", "")));
            }
            if (null != req.getParamValue("accountDj") && !"null".equals(req.getParamValue("accountDj"))) {
                po.setAccountDj(Double.valueOf(req.getParamValue("accountDj").replace(",", "")));
            }
            //异地发货备注
            if (!CommonUtils.checkNull(req.getParamValue("dealerId")).equals(CommonUtils.checkNull(req.getParamValue("RCV_ORGID")))) {
                po.setRemark("异地发货;" + CommonUtils.checkNull(req.getParamValue("textarea")));
            } else {
                po.setRemark(CommonUtils.checkNull(req.getParamValue("textarea")));
            }
            po.setCreateDate(new Date());
            po.setCreateBy(logonUser.getUserId());
            po.setState(Integer.valueOf(CommonUtils.checkNull(req.getParamValue("state"))));
            if (CommonUtils.checkNull(req.getParamValue("state")).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02.toString())) {
                po.setSubmitDate(new Date());
                po.setCreateBy(logonUser.getUserId());
            }
            po.setVer(1);
            //如果是服务商给供应商提报的直发订单   需要先给供应商审核   然后又供应商给车厂审核
            //获取单位ID
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";

            }
            if ((po.getOrderType() + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08 + "")) {
                po.setDiscountStatus(Constant.IF_TYPE_YES + "");
            }
            if (po.getTransType().equals("3")) {

            }
            po.setIsTransfree(Constant.IF_TYPE_YES);
            if (dealerId.equals(Constant.OEM_ACTIVITIES + "")) {
                po.setOemFlag(Constant.PART_BASE_FLAG_YES);
            } else {
                po.setOemFlag(Constant.PART_BASE_FLAG_NO);
            }
            dao.insert(po);
            req.setAttribute("orderCode", po.getOrderCode());
            return mainAmount;
        } catch (Exception ex) {
            BizException e1 = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, exStr + ",请联系管理员!");
            throw e1;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 保存主订单
     */
    private Double saveMainPoForBackhual(RequestWrapper req, ActionContext act, Double mainAmount) throws Exception {
        String exStr = "";
        String orderCode = "";
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            TtPartDlrOrderMainPO po = new TtPartDlrOrderMainPO();
            Long orderId = Long.valueOf(req.getAttribute("orderId").toString());
            String produceFac = CommonUtils.checkNull(req.getParamValue("produceFac"));
            String accountId = CommonUtils.checkNull(req.getParamValue("accountId")) == null ? "0" : CommonUtils.checkNull(req.getParamValue("accountId"));
            po.setOrderId(orderId);
            orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_41, req.getParamValue("dealerId"));
            req.setAttribute("orderCode", orderCode);
            po.setProduceFac(produceFac);
            po.setOrderCode(orderCode);
            po.setOrderType(Integer.valueOf(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"))));
            po.setPayType(Integer.valueOf(CommonUtils.checkNull(req.getParamValue("PAY_TYPE"))));
            try {
                po.setDealerId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("dealerId"))));
            } catch (Exception ex) {
                exStr = "订货单位出错";
                throw ex;
            }
            po.setDealerCode(CommonUtils.checkNull(req.getParamValue("dealerCode")));
            po.setDealerName(CommonUtils.checkNull(req.getParamValue("dealerName")));
            try {
                po.setSellerId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("SELLER_ID"))));
            } catch (Exception ex) {
                exStr = "销售单位出错";
                throw ex;
            }
            po.setSellerCode(CommonUtils.checkNull(req.getParamValue("SELLER_CODE")));
            po.setSellerName(CommonUtils.checkNull(req.getParamValue("SELLER_NAME")));
            po.setBuyerId(logonUser.getUserId());
            po.setBuyerName(CommonUtils.checkNull(req.getParamValue("buyerName")));
            po.setRcvOrgid(Long.valueOf(CommonUtils.checkNull(req.getParamValue("RCV_ORGID"))));
            po.setRcvOrg(CommonUtils.checkNull(req.getParamValue("RCV_ORG")));
            po.setIsAutchk(Constant.PART_BASE_FLAG_NO);
            try {
                po.setAddrId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("ADDR_ID"))));
            } catch (Exception ex) {
                exStr = "接收地址出错";
                throw ex;
            }
            po.setAddr(CommonUtils.checkNull(req.getParamValue("ADDR")));
            po.setReceiver(CommonUtils.checkNull(req.getParamValue("RECEIVER")));
            po.setTel(CommonUtils.checkNull(req.getParamValue("TEL")));
            po.setPostCode(CommonUtils.checkNull(req.getParamValue("POST_CODE")));
            po.setStation(CommonUtils.checkNull(req.getParamValue("STATION")));
            po.setTransType(CommonUtils.checkNull(req.getParamValue("TRANS_TYPE")));
            if (null != req.getParamValue("accountSum") && !"null".equals(req.getParamValue("accountSum"))) {
                po.setAccountSum(Double.valueOf(req.getParamValue("accountSum").replace(",", "")));
            }
            if (null != req.getParamValue("accountKy") && !"null".equals(req.getParamValue("accountKy"))) {
                po.setAccountKy(Double.valueOf(req.getParamValue("accountKy").replace(",", "")));
            }
            if (null != req.getParamValue("accountDj") && !"null".equals(req.getParamValue("accountDj"))) {
                po.setAccountDj(Double.valueOf(req.getParamValue("accountDj").replace(",", "")));
            }
            //异地发货备注
            if (!CommonUtils.checkNull(req.getParamValue("dealerId")).equals(CommonUtils.checkNull(req.getParamValue("RCV_ORGID")))) {
                po.setRemark("异地发货;" + CommonUtils.checkNull(req.getParamValue("textarea")));
            } else {
                po.setRemark(CommonUtils.checkNull(req.getParamValue("textarea")));
            }
            po.setCreateDate(new Date());
            po.setCreateBy(logonUser.getUserId());
            po.setState(Integer.valueOf(CommonUtils.checkNull(req.getParamValue("state"))));
            if (CommonUtils.checkNull(req.getParamValue("state")).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02.toString())) {
                po.setSubmitDate(new Date());
                po.setSubmitBy(logonUser.getUserId());
            }
            po.setVer(1);
            //如果是服务商给供应商提报的直发订单   需要先给供应商审核   然后又供应商给车厂审核
            //获取单位ID
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";

            }
            if ((po.getOrderType() + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08 + "")) {
                po.setDiscountStatus(Constant.IF_TYPE_YES + "");
            }
            if (po.getTransType().equals("3")) {

            }
            po.setIsTransfree(Constant.IF_TYPE_YES);
            if (dealerId.equals(Constant.OEM_ACTIVITIES + "")) {
                po.setOemFlag(Constant.PART_BASE_FLAG_YES);
            } else {
                po.setOemFlag(Constant.PART_BASE_FLAG_NO);
            }
//            po.setWhId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("whId"))));
            po.setActivityCode(CommonUtils.checkNull(req.getParamValue("ACTIVITY_CODE")));
            po.setAccountId(Long.valueOf(accountId));
            dao.insert(po);
            req.setAttribute("orderCode", po.getOrderCode());
            return mainAmount;
        } catch (Exception ex) {
            BizException e1 = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, exStr + ",请联系管理员!");
            throw e1;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 保存订单明细并返回金额
     */
    private void saveDetailPo(RequestWrapper req, ActionContext act, String partId) throws Exception {
        String exStr = "";
        String error = "";
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();

        try {
            TtPartDlrOrderDtlPO po = new TtPartDlrOrderDtlPO();
            String msg = req.getAttribute("msg") + "";
            Long orderId = null;
            if (null != req.getAttribute("orderId")) {
                orderId = Long.valueOf(CommonUtils.checkNull(req.getAttribute("orderId")));
            } else {
                orderId = Long.valueOf(CommonUtils.checkNull(req.getParamValue("orderId")));
            }
            //切换件配件明细验证  start
            TtPartSpecialDefinePO specialDefinePO = new TtPartSpecialDefinePO();
            specialDefinePO.setActivityCode(CommonUtils.checkNull(req.getParamValue("actCodeMap")));
            specialDefinePO.setPartId(Long.valueOf(partId));
            specialDefinePO.setRepartId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("repart_id_" + partId))));
            if (dao.select(specialDefinePO).size() == 0) {
                error += "活动【" + req.getParamValue("actCodeMap") + "】中，替换件编码【" + req.getParamValue("partOldcode_" + partId) + "】的关系不存在,请删除后重试!";
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "!");
                throw e1;
            }
            //验证 end
            po.setLineId(Long.parseLong(SequenceManager.getSequence("")));
            po.setOrderId(orderId);
            po.setPartId(Long.valueOf(partId));
            po.setRepartId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("repart_id_" + partId))));
            po.setPartCname(CommonUtils.checkNull(req.getParamValue("partCname_" + partId)));
            po.setPartOldcode(CommonUtils.checkNull(req.getParamValue("partOldcode_" + partId)));
            po.setPartCode(CommonUtils.checkNull(req.getParamValue("part_Code_" + partId)));
            po.setIsDirect(Constant.PART_BASE_FLAG_NO);
            po.setIsPlan(Constant.PART_BASE_FLAG_NO);
            po.setIsneedFlag(Integer.valueOf(CommonUtils.checkNull(req.getParamValue("ISNEED_FLAG_" + partId))));
//            po.setActivityCode(CommonUtils.checkNull(req.getParamValue("ACTIVITY_CODE_"+ partId)));
            if (!CommonUtils.isEmpty(req.getParamValue("min_package_" + partId))) {
                po.setMinPackage(Long.valueOf(req.getParamValue("min_package_" + partId)));
            }
            try {
                po.setStockQty(Long.valueOf(req.getParamValue("stockQty_" + partId)));
            } catch (Exception e) {
                if (null == req.getParamValue("stockQty_" + partId)) {
                    po.setStockQty(Long.valueOf(0));
                } else {
                    exStr = "服务商当前库存数据出错";
                    throw e;
                }
            }
            long buyQty = Long.valueOf(req.getParamValue("buyQty_" + partId));
            //验证切换数量必须小于当前库存数量
//             if(buyQty > po.getStockQty()){
//                BizException be = new BizException(po.getPartCname()+"库存数量不足!");
//                throw be;
//            }else{
//            po.setBuyQty(buyQty);
//            }
            po.setBuyQty(buyQty);
            if (CommonUtils.checkNull(req.getParamValue("upOrgStock_" + partId)).equals("Y") ||
                    CommonUtils.checkNull(req.getParamValue("upOrgStock_" + partId)).equals(Constant.CAR_FACTORY_SALES_MANAGER_STOCK_01.toString())) {
                po.setIsHava(Constant.CAR_FACTORY_SALES_MANAGER_STOCK_01);
            } else {
                po.setIsHava(Constant.CAR_FACTORY_SALES_MANAGER_STOCK_02);
            }
            po.setRemark(CommonUtils.checkNull(req.getParamValue("remark_" + partId)));
            po.setCreateDate(new Date());
            po.setCreateBy(logonUser.getUserId());
            po.setStatus(Constant.STATUS_ENABLE);

            TtPartSalesPricePO salesPricePO = new TtPartSalesPricePO();
            salesPricePO.setPartId(Long.valueOf(partId));
            salesPricePO = (TtPartSalesPricePO) dao.select(salesPricePO).get(0);
            po.setBuyPrice(salesPricePO.getSalePrice1());
            po.setBuyAmount(Arith.mul(salesPricePO.getSalePrice1(), Double.valueOf(req.getParamValue("buyQty_" + partId))));

            req.setAttribute("msg", msg);
            dao.insert(po);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, error);
            logger.error(logonUser, e1);
            act.setException(e1);
        } finally {
            try {
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "修改失败,请联系管理员!");
            }
        }
    }

    private Double saveDetailFromUpload(RequestWrapper req, Map<String, Object> map, ActionContext act) throws Exception {
        String exStr = "";
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();

        try {
            TtPartDlrOrderDtlPO po = new TtPartDlrOrderDtlPO();
            String msg = req.getAttribute("msg") + "";
            Long orderId = null;
            if (null != req.getAttribute("orderId")) {
                orderId = Long.valueOf(CommonUtils.checkNull(req.getAttribute("orderId")));
            } else {
                orderId = Long.valueOf(CommonUtils.checkNull(req.getParamValue("orderId")));
            }
            String partId = CommonUtils.checkNull(map.get("PART_ID"));
            if (req.getParamValue("partCname_" + partId) != null) {
                //说明已经存在   直接return 0
                return 0d;
            }
            po.setLineId(Long.parseLong(SequenceManager.getSequence("")));
            po.setOrderId(orderId);
            po.setPartId(Long.valueOf(partId));
            po.setPartCname(CommonUtils.checkNull(map.get("PART_CNAME")));
            po.setPartOldcode(CommonUtils.checkNull(map.get("PART_OLDCODE")));
            po.setPartCode(CommonUtils.checkNull(map.get("PART_CODE")));
            po.setUnit(CommonUtils.checkNull(map.get("UNIT")));
            po.setRemark(CommonUtils.checkNull(map.get("REMARK")));

            if (null != map.get("IS_LACK")) {
                po.setIsLack(Integer.valueOf(map.get("IS_LACK") + ""));
            }
            if (null != map.get("IS_REPLACED")) {
                po.setIsReplaced(Integer.valueOf(map.get("IS_REPLACED") + ""));
            }
            if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04.toString()).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {//直发
                po.setIsDirect(Constant.PART_BASE_FLAG_YES);
            } else {
                po.setIsDirect(Constant.PART_BASE_FLAG_NO);
            }
            if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03.toString()).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {//计划
                po.setIsPlan(Constant.PART_BASE_FLAG_YES);
            } else {
                po.setIsPlan(Constant.PART_BASE_FLAG_NO);
            }
            try {
                po.setStockQty(Long.valueOf(map.get("ITEM_QTY") + ""));
            } catch (Exception e) {
                if (null == map.get("ITEM_QTY")) {
                    po.setStockQty(Long.valueOf(0));
                }
                exStr = "服务商当前库存数据出错";
                throw e;
            }
            try {
                po.setMinPackage(Long.valueOf(map.get("MIN_PACKAGE") + ""));
            } catch (Exception e) {
                if (null == map.get("MIN_PACKAGE")) {
                    po.setMinPackage(Long.valueOf(0));
                }
                exStr = "最小库存量数据出错";
                throw e;
            }
            po.setBuyQty(Long.valueOf(map.get("BUYQTY") + ""));
            try {
                po.setBuyPrice(parseDouble(map.get("SALE_PRICE1") + ""));
            } catch (Exception e) {
                exStr = "订购单价出错";
                throw e;
            }
            try {
                po.setBuyAmount(reCountMoney(Double.valueOf(po.getBuyQty()), po.getBuyPrice()));
            } catch (Exception e) {
                exStr = "订购金额出错";
                throw e;
            }
            if (CommonUtils.checkNull(map.get("UPORGSTOCK")).equals("Y") ||
                    CommonUtils.checkNull(map.get("UPORGSTOCK")).equals(Constant.CAR_FACTORY_SALES_MANAGER_STOCK_01.toString())) {
                po.setIsHava(Constant.CAR_FACTORY_SALES_MANAGER_STOCK_01);
            } else {
                po.setIsHava(Constant.CAR_FACTORY_SALES_MANAGER_STOCK_02);
            }
//            po.setRemark("");
            po.setCreateDate(new Date());
            po.setCreateBy(logonUser.getUserId());
            po.setStatus(Constant.STATUS_ENABLE);
            if ((CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"))).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "")) {
                String deftId = dao.getDeftId(po.getPartId() + "", CommonUtils.checkNull(req.getParamValue("brand")));
                po.setDeftId(Long.valueOf(deftId));
            }
            req.setAttribute("msg", msg);
            dao.insert(po);
            return po.getBuyAmount();
        } catch (Exception ex) {

            throw ex;
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @Date : 2013-4-19
     * @Title :
     * @Description: 重新计算金额
     */
    public Double reCountMoney(Double buyQty, Double price) {
        Double money = Arith.mul(Double.valueOf(buyQty), Double.valueOf(price));
        return money;
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 作废
     */
    public void cancelOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            String orderId = CommonUtils.checkNull(req.getParamValue("orderId"));
            String flag = CommonUtils.checkNull(req.getParamValue("flag"));
            //校验修改
            if (!"1".equals(flag) && this.validateState(orderId, allowState)) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单状态已经改变，无法作废!");
                throw e1;
            }
            req.setAttribute("orderId", orderId);
            TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
            newPo.setOrderId(Long.valueOf(orderId));
            newPo.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04);
            newPo.setSubmitBy(logonUser.getUserId());
            newPo.setSubmitDate(new Date());
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            oldPo.setOrderId(Long.valueOf(orderId));
            dao.update(oldPo, newPo);
            String what = "切换订单提报";
            this.saveHistory(req, act, Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04, what, orderId);
            List Ins = new LinkedList<Object>();
            if (UPDATE_PART_STATE_SWITCH) {
                Ins.add(0, orderId);
                Ins.add(1, Constant.PART_CODE_RELATION_41);
                Ins.add(2, 0);// 1:占用 0：释放占用
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", Ins, null);
            }
            act.setOutData("success", "操作成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "作废失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        } finally {
            try {
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "作废失败,请联系管理员!");
            }
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @Date : 2013-4-19
     * @Title :
     * @Description: 保存日志
     */
    public void saveHistory(RequestWrapper req, ActionContext act, int status, String what, String orderId) throws Exception {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            TtPartOperationHistoryPO po = new TtPartOperationHistoryPO();
            Long optId = Long.parseLong(SequenceManager.getSequence(""));
            po.setBussinessId(CommonUtils.checkNull(req.getAttribute("orderCode")));
            po.setOptId(optId);
            po.setOptBy(logonUser.getUserId());
            po.setOptDate(new Date());
            po.setOptType(Constant.PART_OPERATION_TYPE_01);
            po.setWhat(what);
            po.setOptName(logonUser.getName());
            po.setStatus(status);
            po.setOrderId(Long.valueOf(orderId));
            dao.insert(po);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     *
     * @Title      :
     * @Description:生成切换订单
     * @param      : @param orgAmt
     * @param      : @return
     * @return     :
     * @throws     :
     * LastDate    : 2014-3-27
     */
//	public void createSaleOrder(){
//			ActionContext act = ActionContext.getContext();
//			AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
//			RequestWrapper request = act.getRequest();
//            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
//		try{
//			request.setAttribute("boFlag", false);
//			//订单编号
//			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
//            //校验是否有同时操作的
//            Map<String,Object> mainMap = dao.queryPartDlrOrderMain(orderId);
//            String state =CommonUtils.checkNull(mainMap.get("STATE"));
//            if(state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03+"")){
//                BizException e1 = new BizException(act,new RuntimeException(),ErrorCodeConstant.SPECIAL_MEG,"该订单已审核,请不要重复审核!");
//                throw e1;
//            }
//            //增加判断是否订单已经生成销售单防止重复生成
//            TtPartSoMainPO soMainPO = new TtPartSoMainPO();
//            soMainPO.setOrderId(Long.valueOf(orderId));
//            if(dao.select(soMainPO).size()>0){
//                BizException e1 = new BizException(act,new RuntimeException(),ErrorCodeConstant.SPECIAL_MEG,"该订单已经生成销售,不能重复生成!");
//                throw e1;
//            }
//			//更新明细
//            state="backhaul";
//            this.updateOrderDetail(state);
//            
//            //更新明细
//            this.updateOrderMain();
//            
//			//修改状态
//			this.changeOrderStatus(loginUser, orderId, act, request);
//		    
//			ArrayList ins = new ArrayList();
//            ins.add(0, Long.valueOf(request.getAttribute("soId").toString()));
//            ins.add(1,Constant.PART_CODE_RELATION_41);
//            ins.add(2,1);
//            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE",ins,null);
//            
//			POContext.endTxn(true);
//			act.setOutData("success", "操作成功!");
//			act.setForword(PART_REPLACED_DLR_ORDER_CHECK_QUERY);
//		}catch(Exception e){
//			POContext.endTxn(false);
//			if(e instanceof BizException){
//				BizException e1 = (BizException)e;
//				logger.error(loginUser,e1);
//				act.setException(e1);
//				return ;
//			}
//			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"生成销售单错误,请联系管理员!");
//			logger.error(loginUser,e1);
//			act.setException(e1);
//		}
//	}

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 提报订单
     */
    public void submitOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            String orderId = CommonUtils.checkNull(req.getParamValue("orderId"));
            String orderCode = CommonUtils.checkNull(req.getParamValue("orderCode"));

            //校验修改
            if (this.validateState(orderId, allowState)) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单状态已经改变，无法重复提报!");
                throw e1;
            }
            req.setAttribute("orderId", orderId);
            req.setAttribute("orderCode", orderCode);
            TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
            newPo.setOrderId(Long.valueOf(orderId));
            newPo.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02);
            newPo.setSubmitBy(logonUser.getUserId());
            newPo.setSubmitDate(new Date());
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            oldPo.setOrderId(Long.valueOf(orderId));
            dao.update(oldPo, newPo);
            String what = "切换订单提报";
            this.saveHistory(req, act, Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02, what, orderId);
            act.setOutData("success", "提报成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "提报失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        } finally {
            try {

            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "提报失败,请联系管理员!");
            }
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 修改订单
     */
    public void modifyDlrOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        //前台页面数据集合
        Map<String, Object> dataMap = new HashMap();
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail(orderId);

            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            request.setAttribute("transList", list);
            act.setOutData("isTransFreeOrg", dao.isTransFree(mainMap.get("DEALER_ID").toString()));
            //获取单位ID
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            Map<String, Object> acountMap = new HashMap<String, Object>();
            if (!"".equals(CommonUtils.checkNull(mainMap.get("ACCOUNT_SUM")))) {
                acountMap = dao.getAccount(CommonUtils.checkNull(mainMap.get("DEALER_ID")), CommonUtils.checkNull(mainMap.get("SELLER_ID")));
                dataMap.put("accountFlag", true);
                //账户数据可变   重新获取账户数据
            } else {
                dataMap.put("accountFlag", false);
            }
            dataMap.put("currAcountMap", acountMap);
            //折扣率
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<OrgBean> lo = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(lo.get(0).getOrgId().toString());
            String discount = dao.getDiscount(dealerId);
            request.setAttribute("dataMap", dataMap);
            request.setAttribute("mainMap", mainMap);
            request.setAttribute("detailList", detailList);
            request.setAttribute("wareHouseList", wareHouseList);

//            String orderId = CommonUtils.checkNull(req.getParamValue("orderId"));
            List<Map<String, Object>> ActCodelist = dao.getActCodeByOrderId(orderId);
            String actCode = CommonUtils.checkNull(ActCodelist.get(0).get("ACTIVITY_CODE"));
            List<Map<String, Object>> partTpyeList = dao.getPartType(actCode);
            String partTpye = CommonUtils.checkNull(partTpyeList.get(0).get("PART_TYPE"));
            String partTpyeCode = CommonUtils.checkNull(dao.getCode(partTpye).get(0).get("CODE_DESC"));
            request.setAttribute("PART_TYPE", partTpyeCode);

            List<Map<String, Object>> stoList = dao.getStoVender(CommonUtils.checkNull(detailList.get(0).get("DEFT_ID")));
            String brand = "";
            if (stoList != null && stoList.size() > 0 && stoList.get(0) != null) {
                brand = CommonUtils.checkNull(stoList.get(0).get("BRAND"));
            }
            request.setAttribute("brand", brand);
            //品牌
            List<Map<String, Object>> brandList = dao.getBrand();
            request.setAttribute("brandList", brandList);
            List<Map<String, Object>> actCodelist = this.dao.getCurrentActCode(dealerId);
            request.setAttribute("actCodeMap", actCodelist);

            act.setForword(PART_REPLACED_DLR_ORDER_MOD);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件切换订单提报查询数据错误,请联系管理员!");
            act.setForword(PART_REPLACED_DLR_ORDER_MOD);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 保存修改订单
     */
    public void saveModifyOrder() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            String orderId = CommonUtils.checkNull(req.getParamValue("orderId"));
            checkStock(orderId);
            PartReplacedDlrOrderDao dao = PartReplacedDlrOrderDao.getInstance();
//            String orderId = CommonUtils.checkNull(req.getParamValue("orderId"));
//            List<Map<String, Object>> list = dao.getActCodeByOrderId(orderId);
//            String actCode = CommonUtils.checkNull(list.get(0).get("actCodeMap"));
//            req.setAttribute("ACTIVITY_CODE", actCode);
            String ulrFlg = "";
            String dealerId = logonUser.getDealerId() == null ? "" : logonUser.getDealerId();
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            //服务商修改跳转到服务商订单页面，销售单位修改跳转到审核页面
            if (dealerId.equals(mainMap.get("DEALER_ID").toString())) {
                ulrFlg = "1";
            } else {
                ulrFlg = "2";
            }
//            validatePkgBox();
            //校验修改
            if ((req.getParamValue("state") + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "")) {
                if (!(mainMap.get("STATE") + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "")) {
                    BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单状态已经改变，无法修改!");
                    throw e1;
                }
            } else {
                if (dealerId != null && this.validateState(orderId, allowState) && dealerId.equals(mainMap.get("DEALER_ID").toString())) {
                    BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单状态已经改变，无法修改!");
                    throw e1;
                }
            }

            //删除订单详细然后保存
            TtPartDlrOrderDtlPO ttPartDlrOrderDtlPO = new TtPartDlrOrderDtlPO();
            ttPartDlrOrderDtlPO.setOrderId(Long.valueOf(orderId));
            dao.delete(ttPartDlrOrderDtlPO);
            //保存详细订单
            Double mainAmount = 0.00D;
            String[] partArr = req.getParamValues("cb");
            String msg = "";
            req.setAttribute("msg", msg);
            for (int i = 0; i < partArr.length; i++) {
                this.saveDetailPo(req, act, partArr[i]);
            }
            msg = CommonUtils.checkNull(req.getAttribute("msg"));
            if (!"".equals(msg)) {
                BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, msg);
                throw e1;
            }
            //更新主订单
            this.updateMainPo(req, act, mainAmount);

            //
            if (UPDATE_PART_STATE_SWITCH) {
                List Ins = new LinkedList<Object>();
                Ins.add(0, orderId);
                Ins.add(1, Constant.PART_CODE_RELATION_41);
                Ins.add(2, 1);// 1:占用 0：释放占用
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", Ins, null);
            }

            act.setOutData("success", "保存成功!");
            act.setOutData("ulrFlg", ulrFlg);
        } catch (Exception e) {//异常方法
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                return;
            }
            act.setOutData("error", "保存失败!");
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存订单详细信息出错,请联系管理员!");
            act.setException(e1);
        } finally {
            try {
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "保存订单详细信息出错,请联系管理员!");
            }
        }
    }

    private void checkStock(String orderId) throws BizException {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();

        if (UPDATE_PART_STATE_SWITCH) {
            List outIns = new LinkedList<Object>();
            outIns.add(0, orderId);
            outIns.add(1, Constant.PART_CODE_RELATION_41);
            outIns.add(2, 0);// 1:占用 0：释放占用
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", outIns, null);
        }
        //库存校验
        if (UPDATE_PART_STATE_SWITCH) {
            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail(orderId);
            long stockQty = 0l;
            String[] partArr = req.getParamValues("cb");
            for (int i = 0; i < partArr.length; i++) {
//        	String partId = CommonUtils.checkNull(detailList.get(i).get("PART_ID"));
                stockQty = Long.valueOf(CommonUtils.checkNull(detailList.get(i).get("STOCK_QTY")));
                req.setAttribute("stockQty_" + partArr[i], stockQty);
                long buyQty = Long.valueOf(CommonUtils.checkNull(req.getParamValue("buyQty_" + partArr[i])));
                if (stockQty < buyQty) {
                    BizException e1 = new BizException("第" + (i + 1) + "行配件【" + detailList.get(i).get("PART_CNAME") + "】，库存不足！");
                    throw e1;
                }
            }
        }
    }

    private void validatePkgBox() throws Exception {
        ActionContext act = ActionContext.getContext();
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String dealerId = CommonUtils.checkNull(req.getParamValue("dealerId"));
        if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04.toString()).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {//直发
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            String[] partArr = req.getParamValues("cb");
            String brand = CommonUtils.checkNull(req.getParamValue("brand"));
            List<Map<String, Object>> list = dao.getStoList(brand);
            Map<String, Object> map = list.get(0);
            int criterion = Integer.valueOf(map.get("CRITERION") + "");
            int box = 0;
            if (null != partArr) {
                for (int i = 0; i < partArr.length; i++) {
                    String partId = partArr[i];
                    int minPkg = Integer.valueOf(req.getParamValue("minPackage_" + partId));
                    int buyQty = Integer.valueOf(req.getParamValue("buyQty_" + partId));
                    box += buyQty / minPkg;
                }
            }
            String seq = CommonUtils.checkNull(req.getParamValue("seq"));
            if (!"".equals(seq)) {
                List<Map<String, Object>> upList = dao.getUploadList(seq);
                for (int i = 0; i < upList.size(); i++) {
                    String partId = (upList.get(i)).get("PART_ID") + "";
                    Map<String, Object> partMap = dao.getPartDefine(partId, dealerId);
                    int minPkg = Integer.valueOf(partMap.get("MIN_PACKAGE") + "");
                    int buyQty = Integer.valueOf((upList.get(i)).get("BUYQTY") + "");
                    box += buyQty / minPkg;
                }
            }

            if (box < criterion) {
                BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, brand + "直发时所有品种订货箱数必须大于" + criterion + "箱!");
                throw e1;
            }
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 更新主订单
     */
    private void updateMainPo(RequestWrapper req, ActionContext act, Double mainAmount) throws Exception {
        String exStr = "";
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(CommonUtils.checkNull(req.getParamValue("orderId")));
            int ver = Integer.valueOf(mainMap.get("VER") == null ? "1" : mainMap.get("VER").toString()) + 1;
            TtPartDlrOrderMainPO po = new TtPartDlrOrderMainPO();
            String produceFac = CommonUtils.checkNull(req.getParamValue("produceFac"));
            po.setProduceFac(produceFac);
            po.setOrderId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("orderId"))));
            po.setOrderCode(CommonUtils.checkNull(req.getParamValue("orderCode")));
            po.setOrderType(Integer.valueOf(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"))));
            try {
                po.setDealerId(Long.valueOf(req.getParamValue("dealerId")));
            } catch (Exception ex) {
                exStr = "订货单位出错";
                throw ex;
            }
            po.setDealerCode(CommonUtils.checkNull(req.getParamValue("dealerCode")));
            po.setDealerName(CommonUtils.checkNull(req.getParamValue("dealerName")));
            try {
                po.setSellerId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("SELLER_ID"))));
            } catch (Exception ex) {
                exStr = "销售单位出错";
                throw ex;
            }
            po.setSellerCode(CommonUtils.checkNull(req.getParamValue("SELLER_CODE")));
            po.setSellerName(CommonUtils.checkNull(req.getParamValue("SELLER_NAME")));
            po.setBuyerId(logonUser.getUserId());
            po.setBuyerName(CommonUtils.checkNull(req.getParamValue("buyerName")));
            po.setRcvOrgid(Long.valueOf(CommonUtils.checkNull(req.getParamValue("RCV_ORGID"))));
            po.setRcvOrg(CommonUtils.checkNull(req.getParamValue("RCV_ORG")));
            po.setIsAutchk(Constant.PART_BASE_FLAG_NO);
            po.setAddrId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("ADDR_ID"))));
            po.setAddr(CommonUtils.checkNull(req.getParamValue("ADDR")));
            po.setReceiver(CommonUtils.checkNull(req.getParamValue("RECEIVER")));
            po.setTel(CommonUtils.checkNull(req.getParamValue("TEL")));
            po.setPostCode(CommonUtils.checkNull(req.getParamValue("POST_CODE")));
            po.setStation(CommonUtils.checkNull(req.getParamValue("STATION")));
            po.setTransType(CommonUtils.checkNull(req.getParamValue("TRANS_TYPE")));
            if (null != req.getParamValue("accountSum")) {
                po.setAccountSum(parseDouble(req.getParamValue("accountSum")));
            }
            if (null != req.getParamValue("accountKy")) {
                po.setAccountKy(parseDouble(req.getParamValue("accountKy")));
            }
            if (null != req.getParamValue("accountDj")) {
                po.setAccountDj(parseDouble(req.getParamValue("accountDj")));
            }
            try {
                po.setOrderAmount(mainAmount);
            } catch (Exception ex) {
                exStr = "订购总金额出错";
                throw ex;
            }

            Double freight = 0D;
            if ((po.getSellerId() + "").equals(Constant.OEM_ACTIVITIES)) {
                freight = Double.valueOf((dao.getFreight(po.getDealerId() + "", po.getOrderType() + "", po.getOrderAmount() + "")).replaceAll(",", ""));
            }
            if (po.getTransType().equals("3")) {
                freight = 0D;
            }
            //2状态服务商免运费 ADD BY YUAN 20131225 START
            if (dao.isTransFree(po.getDealerId().toString()).equals("2")) {
                freight = 0D;
            }
            //END
            if (freight > 0) {
                po.setIsTransfree(Constant.IF_TYPE_NO);
            } else {
                po.setIsTransfree(Constant.IF_TYPE_YES);
            }
            po.setOrderAmount(Arith.add(po.getOrderAmount(), freight));
            po.setFreight(freight);
            po.setDiscount(Double.valueOf(CommonUtils.checkNull(req.getParamValue("DISCOUNT"))));
            po.setRemark(CommonUtils.checkNull(req.getParamValue("textarea")));
            po.setUpdateDate(new Date());
            po.setUpdateBy(logonUser.getUserId());

            po.setState(Integer.valueOf(CommonUtils.checkNull(mainMap.get("STATE"))));
            po.setVer(ver);
            po.setWhId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("whId"))));
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            oldPo.setOrderId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("orderId"))));
            po.setActivityCode(CommonUtils.checkNull(req.getParamValue("actCodeMap")));

            dao.update(oldPo, po);

        } catch (Exception ex) {
            BizException e1 = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, exStr + ",请联系管理员!");
            throw e1;
        }
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
            listHead.add("配件编码");
            listHead.add("切换数量");
            listHead.add("备注");
            list.add(listHead);
            // 导出的文件名
            String fileName = "替换件订单提报模板.xls";
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
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:导出
     */
    public void exportPartPlanExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            String[] head = new String[12];
            head[0] = "订单号 ";
            head[1] = "订单类型";
            head[2] = "订货单位编码";
            head[3] = "订货单位";
            head[4] = "订货人";
            head[5] = "制单日期";
            head[6] = "提交日期";
            head[7] = "驳回原因";
            head[8] = "订单状态";
            List<Map<String, Object>> list = dao.queryExportPartDlrOrder(request);
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[12];
                    detail[0] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                    detail[1] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                    detail[2] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                    detail[3] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[4] = CommonUtils.checkNull(map.get("BUYER_NAME"));
                    detail[5] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[6] = CommonUtils.checkNull(map.get("SUBMIT_DATE"));
                    detail[7] = CommonUtils.checkNull(map.get("REBUT_REASON"));
                    detail[8] = CommonUtils.checkNull(map.get("STATE"));
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
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:导出
     */
    public void exportPartExcelBack() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
//            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            String[] head = new String[12];
            head[0] = "订单号 ";
            head[1] = "订单类型";
            head[2] = "订货单位编码";
            head[3] = "订货单位";
            head[4] = "订货人";
            head[5] = "制单日期";
            head[6] = "提交日期";
//            head[7] = "驳回原因";
            head[7] = "订单状态";
            List<Map<String, Object>> list = dao.queryCheckPartDlrOrderBackhaul(request);
            List list1 = new ArrayList();
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[12];
                        detail[0] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                        detail[1] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[4] = CommonUtils.checkNull(map.get("BUYER_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[6] = CommonUtils.checkNull(map.get("SUBMIT_DATE"));
//                    detail[7] = CommonUtils.checkNull(map.get("REBUT_REASON"));
                        detail[7] = CommonUtils.checkNull(map.get("STATE"));
                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:导出
     */
    public void exportPartExcelIn() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
//            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            String[] head = new String[12];
            head[0] = "订单号 ";
            head[1] = "订单类型";
            head[2] = "物流单号";
            head[3] = "物流公司";
            head[4] = "订货单位编码";
            head[5] = "订货单位";
            head[6] = "订货人";
            head[7] = "制单日期";
            head[8] = "提交日期";
            head[9] = "订单状态";
            List<Map<String, Object>> list = dao.queryCheckPartDlrOrderBackhaul(request);
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[12];
                    detail[0] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                    detail[1] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                    detail[2] = CommonUtils.checkNull(map.get("WULIU_CODE"));
                    detail[3] = CommonUtils.checkNull(map.get("WULIU_COMPANY"));
                    detail[4] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                    detail[5] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[6] = CommonUtils.checkNull(map.get("BUYER_NAME"));
                    detail[7] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[8] = CommonUtils.checkNull(map.get("SUBMIT_DATE"));
                    detail[9] = CommonUtils.checkNull(map.get("STATE"));
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
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:导出
     */
    public void exportOemPartPlanExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            PartBaseQueryDao partBaseQueryDao = PartBaseQueryDao.getInstance();
            String[] head = new String[12];
            head[0] = "订单号 ";
            head[1] = "订货单位编码";
            head[2] = "订货单位";
            head[3] = "订货人";
            head[4] = "订货日期";
            head[5] = "销售单位";
            head[6] = "订单类型";
            head[7] = "总金额";
            head[8] = "提交时间";
            head[9] = "订单状态";
            List<Map<String, Object>> list = dao.queryOemExportPartDlrOrder(request);
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[12];
                    detail[0] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                    detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                    detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[3] = CommonUtils.checkNull(map.get("BUYER_NAME"));
                    detail[4] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[5] = CommonUtils.checkNull(map.get("SELLER_NAME"));
                    detail[6] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                    detail[7] = CommonUtils.checkNull(map.get("ORDER_AMOUNT"));
                    detail[8] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[9] = CommonUtils.checkNull(map.get("STATE"));
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

    public void uploadExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        act.setOutData("flag", true);
        try {
            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmp(request, "uploadFile1", 3, 2, maxSize);
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
            loadDataList(excelList);
            String seq = SequenceManager.getSequence("");
            dao.saveUploadData(request, seq);
            PageResult<Map<String, Object>> ps = dao.showPartBaseForUpload(request, CommonUtils.checkNull(request.getParamValue("SELLER_ID")), 1, Constant.PART_STOCK_STATUS_CHANGE_MAX_LINENUM);
            if (CommonUtils.checkNull(request.getParamValue("saveFlag")).equals("save")) {
                act.setOutData("saveFlag", "save");
            }
            act.setOutData("seq", seq);
            act.setForword(UPLOADFILE);
        } catch (Exception e) {
            e.printStackTrace();
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, act.getOutData("error") == null ? "文件读取错误" : act.getOutData("error").toString());
            }
            act.setOutData("false", true);
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(UPLOADFILE);
        }
    }

    public void getUploadExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String seq = CommonUtils.checkNull(request.getParamValue("seq"));
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            String dealerName = "";
            String dealerCode = "";
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
                dealerName = beanList.get(0).getOrgName();
                dealerCode = beanList.get(0).getOrgCode();
            }
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);

            List<Map<String, Object>> list = dao.getUploadList(seq);
//            if(list.size()==0){
//            throw new BizException("上传的配件占时没有活动");
//            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("discount", CommonUtils.checkNull(request.getParamValue("DISCOUNT")));
            map.put("dealerId", dealerId);
            map.put("dealerName", dealerName);
            map.put("dealerCode", dealerCode);
            map.put("buyerName", loginUser.getName());
            map.put("accountId", CommonUtils.checkNull(request.getParamValue("accountId")));
            map.put("SELLER_NAME", CommonUtils.checkNull(request.getParamValue("SELLER_NAME")));
            map.put("SELLER_CODE", CommonUtils.checkNull(request.getParamValue("SELLER_CODE")));
            map.put("SELLER_ID", CommonUtils.checkNull(request.getParamValue("SELLER_ID")));
            map.put("PAY_TYPE", CommonUtils.checkNull(request.getParamValue("PAY_TYPE")));
            map.put("produceFac", CommonUtils.checkNull(request.getParamValue("produceFac")));
            map.put("now", now);
            map.put("textarea", CommonUtils.checkNull(request.getParamValue("textarea")));
            map.put("accountSum", CommonUtils.checkNull(request.getParamValue("accountSum")));
            map.put("accountKy", CommonUtils.checkNull(request.getParamValue("accountKy")));
            map.put("accountDj", CommonUtils.checkNull(request.getParamValue("accountDj")));

            act.setOutData("dataMap", map);
            Map<String, Object> defaultValueMap = new HashMap<String, Object>();
            defaultValueMap.put("defaultRcvOrg", CommonUtils.checkNull(request.getParamValue("RCV_ORG")));
            defaultValueMap.put("RCV_CODE", CommonUtils.checkNull(request.getParamValue("RCV_CODE")));
            defaultValueMap.put("defaultRcvOrgid", CommonUtils.checkNull(request.getParamValue("RCV_ORGID")));
            defaultValueMap.put("defaultAddr", CommonUtils.checkNull(request.getParamValue("ADDR")));
            defaultValueMap.put("defaultAddrId", CommonUtils.checkNull(request.getParamValue("ADDR_ID")));
            defaultValueMap.put("defaultStation", CommonUtils.checkNull(request.getParamValue("STATION")));
            defaultValueMap.put("defaultLinkman", CommonUtils.checkNull(request.getParamValue("RECEIVER")));
            defaultValueMap.put("defaultTel", CommonUtils.checkNull(request.getParamValue("TEL")));
            defaultValueMap.put("defaultPostCode", CommonUtils.checkNull(request.getParamValue("POST_CODE")));
            act.setOutData("defaultValueMap", defaultValueMap);
            List transList = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            act.setOutData("orderType", CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")));
            act.setOutData("transType", CommonUtils.checkNull(request.getParamValue("TRANS_TYPE")));

            act.setOutData("transList", transList);
            act.setOutData("detailList", list);
            List<Map<String, Object>> brandList = dao.getBrand();
            act.setOutData("brandList", brandList);
            act.setForword(PART_REPLACED_DLR_ORDER_ADD);
        } catch (Exception e) {
            e.printStackTrace();
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, act.getOutData("error") == null ? "文件读取错误" : act.getOutData("error").toString());
            }
            act.setOutData("false", true);
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_REPLACED_DLR_ORDER_ADD);
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
                        error += "上传文件," + e1.getMessage().replaceAll("操作失败！失败原因：", "") + "</br>";
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
        if (cells.length < 2) {
            BizException ex = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "请填写配件编码和数量!");
            throw ex;
        }
        if ("" == CommonUtils.checkNull(cells[0].getContents())) {
            BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件编码为空!");
            throw e;
        }
        if ("" == CommonUtils.checkNull(cells[1].getContents())) {
            BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件订购数量为空!");
            throw e;
        }
        request.setAttribute("partOldcode", CommonUtils.checkNull(cells[0].getContents()));
        request.setAttribute("uploadFlag", "upload");
        String dealerId = "";

        request.setAttribute("bookDealerId", CommonUtils.checkNull(request.getParamValue("dealerId")));

        TmpPartUploadPO po = new TmpPartUploadPO();
        List<Map<String, Object>> tempList = dao.getTmpData(CommonUtils.checkNull(cells[0].getContents()), CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")));
        if (tempList.size() > 0) {
            return;
        }
        po.setPartOldcode(CommonUtils.checkNull(cells[0].getContents()));
        if ("" != CommonUtils.checkNull(cells[1].getContents())) {
            try {
                po.setQty(Long.valueOf(cells[1].getContents()));

            } catch (Exception e) {
                BizException ex = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订购数量格式错误!");
                throw ex;
            }
        }
        //新增备注
        if (cells.length > 2 && "" != CommonUtils.checkNull(cells[2].getContents()) && null != CommonUtils.checkNull(cells[2].getContents())) {
            po.setRemark(cells[2].getContents());
        }
        dao.insert(po);
    }

    /**
     * @param is
     * @param rowSize
     * @return
     * @throws Exception
     */
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
     * @param obj
     * @return
     * @throws Exception
     */
    private Double parseDouble(Object obj) throws Exception {
        ActionContext act = ActionContext.getContext();
        String str = CommonUtils.checkNull(obj);
        try {
            if (str.indexOf(",") > -1) {
                String[] strArr = str.split("\\,");
                str = "";
                for (int i = 0; i < strArr.length; i++) {
                    str += strArr[i];
                }
            }
            return Double.valueOf(str);
        } catch (Exception ex) {
            BizException e = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "数字转换错误!");
            throw e;
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

    /**
     * 订单提报  修改保存   作废 都需要校验状态是否是当前允许状态
     *
     * @param orderId
     * @param allowState
     * @return
     */
    public boolean validateState(String orderId, String[] allowState) {
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
        List<String> tempList = Arrays.asList(allowState);
        if (tempList.contains(CommonUtils.checkNull(mainMap.get("STATE").toString()))) {
            return false;
        } else {
            return true;
        }

    }

    private void saveQueryCondition() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
        String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
        String CstartDate = CommonUtils.checkNull(request.getParamValue("CstartDate"));// 制单日期 开始
        String CendDate = CommonUtils.checkNull(request.getParamValue("CendDate"));// 制单日期 结束
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//提交时间 开始
        String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));//订单状态
        String autoPreCheck = CommonUtils.checkNull(request.getParamValue("autoPreCheck"));//自动预审
        String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderCode", orderCode);
        map.put("dealerName", dealerName);
        map.put("sellerName", sellerName);
        map.put("state", state);
        map.put("autoPreCheck", autoPreCheck);
        map.put("salerId", salerId);
        map.put("CstartDate", CstartDate);
        map.put("CendDate", CendDate);
        map.put("orderType", orderType);
        map.put("SstartDate", SstartDate);
        map.put("SendDate", SendDate);
        act.getSession().set("condition", map);
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2014-3-19
     * @Title :1900432237232
     * @Description:更新配件明细表
     */
    private void updateOrderDetail(String state) throws Exception {
        try {
            ActionContext act = ActionContext.getContext();
            RequestWrapper request = act.getRequest();

            AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));

            String[] partIdArr = request.getParamValues("cb");
            Long soId = Long.parseLong(SequenceManager.getSequence(""));

            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            String orgId = "";
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            if (partIdArr != null) {
                for (int i = 0; i < partIdArr.length; i++) {
                    String partId = partIdArr[i];

                    TtPartDlrOrderDtlPO po1 = new TtPartDlrOrderDtlPO();
                    TtPartDlrOrderDtlPO po2 = new TtPartDlrOrderDtlPO();

                    po1.setLineId(Long.valueOf(CommonUtils.checkNull(request.getParamValue("LINE_ID_" + partId))));
                    if ("check".equals(state)) {
                        po2.setCheckQty(Long.valueOf(CommonUtils.checkNull(request.getParamValue("check_Qty_" + partId))));
                    }
                    //当前只能回运一次
                    if ("backhaul".equals(state)) {
                        po2.setBackhaulQty(Long.valueOf(CommonUtils.checkNull(request.getParamValue("BACKHAUL_QTY_" + partId)))
                                + Long.valueOf(CommonUtils.checkNull(request.getParamValue("BACKED_QTY_" + partId))));//旧件回运数量
                        po2.setNbackQty(Long.valueOf(CommonUtils.checkNull(request.getParamValue("BACKHAUL_QTY_NEW_" + partId)))
                                + Long.valueOf(CommonUtils.checkNull(request.getParamValue("NBACKED_QTY_" + partId))
                                == "" ? "0" : CommonUtils.checkNull(request.getParamValue("NBACKED_QTY_" + partId))));//新件回运数量
                    }
                    if ("warehousing".equals(state)) {
                        String flag = CommonUtils.checkNull(request.getParamValue("isFlag_" + partId));
                        if ("1".equals(flag)) {
                            po2.setWarehousingQty(Long.valueOf(CommonUtils.checkNull(request.getParamValue("WAREHOUSING_QTY_" + partId))));
                        } else {
                            po2.setNinstockQty(Long.valueOf(CommonUtils.checkNull(request.getParamValue("WAREHOUSING_QTY_" + partId))));
                        }
                        po2.setYanshourukuRemake(CommonUtils.checkNull(request.getParamValue("yanshou_remark_" + partId)));
                    }
                    po2.setUpdateBy(loginUser.getUserId());
                    po2.setUpdateDate(new Date());
                    po2.setRemark(CommonUtils.checkNull(request.getParamValue("remark_" + partId)));

                    dao.update(po1, po2);
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2014-3-19
     * @Title :1900432237232
     * @Description:更新配件订单表
     */
    private void updateOrderMain(Integer state) throws Exception {
        try {
            ActionContext act = ActionContext.getContext();
            RequestWrapper request = act.getRequest();

            AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
//			INTEGER STATE = INTEGER.VALUEOF(COMMONUTILS.CHECKNULL(REQUEST.GETPARAMVALUE("STATE")));

            Long soId = Long.parseLong(SequenceManager.getSequence(""));

            request.setAttribute("soId", soId);

            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            String orgId = "";
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            request.setAttribute("orgId", orgId);

            TtPartDlrOrderMainPO po1 = new TtPartDlrOrderMainPO();
            TtPartDlrOrderMainPO po2 = new TtPartDlrOrderMainPO();

            po1.setOrderId(Long.valueOf(orderId));
            po2.setRemark(CommonUtils.checkNull(request.getParamValue("textarea")));
            String str = request.getParamValue("whId");
            if (!CommonUtils.checkIsNullStr(str)) {
                String[] wareHouse = str.split(",");
                if (Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02.equals(state)) {
                    po2.setOemwhId(Long.valueOf(wareHouse[0]));
                }
            }
            po2.setRepartChekdate(new Date());
            po2.setWuliuCode(CommonUtils.checkNull(request.getParamValue("WULIU_CODE")));
            po2.setWuliuCompany(CommonUtils.checkNull(request.getParamValue("WULIU_COMPANY")));
            po2.setAuditOpinion(CommonUtils.checkNull(request.getParamValue("remark")));

            dao.update(po1, po2);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void changeOrderStatus(AclUserBean loginUser, String orderId, ActionContext act, RequestWrapper request) throws Exception {
        try {

            TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
            TtPartDlrOrderMainPO mainPO = new TtPartDlrOrderMainPO();
            mainPO.setOrderId(Long.valueOf(orderId));
            mainPO = (TtPartDlrOrderMainPO) dao.select(mainPO).get(0);
            TtPartSoMainPO soMainPO = new TtPartSoMainPO();
            soMainPO.setOrderId(Long.valueOf(orderId));
            newPo.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03);
            newPo.setAuditBy(loginUser.getUserId());
            newPo.setAuditDate(new Date());
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            oldPo.setOrderId(Long.valueOf(orderId));
            dao.update(oldPo, newPo);
            String what = "切换订单审核";
            this.saveHistory(request, act, Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03, what, orderId);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void saveHistoryForBackhaul(AclUserBean loginUser, String orderId, ActionContext act, RequestWrapper request) throws Exception {
        try {

            TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
            TtPartDlrOrderMainPO mainPO = new TtPartDlrOrderMainPO();
            mainPO.setOrderId(Long.valueOf(orderId));
            mainPO = (TtPartDlrOrderMainPO) dao.select(mainPO).get(0);
            TtPartSoMainPO soMainPO = new TtPartSoMainPO();
            soMainPO.setOrderId(Long.valueOf(orderId));
            newPo.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_13);
            newPo.setAuditBy(loginUser.getUserId());
            newPo.setAuditDate(new Date());
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            oldPo.setOrderId(Long.valueOf(orderId));
            dao.update(oldPo, newPo);
            String what = "切换订单回运";
            this.saveHistory(request, act, Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_13, what, orderId);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void saveHistoryForWarehousing(AclUserBean loginUser, String orderId, ActionContext act, RequestWrapper request) throws Exception {
        try {

            TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
            TtPartDlrOrderMainPO mainPO = new TtPartDlrOrderMainPO();
            mainPO.setOrderId(Long.valueOf(orderId));
            mainPO = (TtPartDlrOrderMainPO) dao.select(mainPO).get(0);
            TtPartSoMainPO soMainPO = new TtPartSoMainPO();
            soMainPO.setOrderId(Long.valueOf(orderId));
            newPo.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_14);
            newPo.setAuditBy(loginUser.getUserId());
            newPo.setAuditDate(new Date());
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            oldPo.setOrderId(Long.valueOf(orderId));
            dao.update(oldPo, newPo);
            String what = "替换件旧件验收入库";
            this.saveHistory(request, act, Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_14, what, orderId);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @param dealerID
     * @param sellerId
     */
    public void dealerAccoutMng(String dealerID, String sellerId) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        TtPartAccountDefinePO accountDefinePO = new TtPartAccountDefinePO();
        accountDefinePO.setChildorgId(Long.valueOf(dealerID));
        accountDefinePO.setParentorgId(Long.valueOf(sellerId));
        if (dao.select(accountDefinePO).size() == 0) {
            Long accountId = Long.parseLong(SequenceManager.getSequence(""));
            TtPartAccountDefinePO definePO = new TtPartAccountDefinePO();
            definePO.setAccountId(accountId);
            definePO.setChildorgId(Long.valueOf(dealerID));
            definePO.setParentorgId(Long.valueOf(sellerId));
            definePO.setAccountKind(Constant.FIXCODE_CURRENCY_01);
            definePO.setAccountSum(0.00d);
            definePO.setCreateDate(new Date());
            definePO.setCreateBy(loginUser.getUserId());
            definePO.setState(Constant.STATUS_ENABLE);
            definePO.setStatus(1);
            dao.insert(definePO);

        }
    }

    public boolean accountContorl() {
        String key = CommonDAO.getPara(Constant.PART_SALE_ACCOUNT_CONTROL + "");
        if (key.equals("0")) {
            return true;
        }
        return false;
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:占用资金
     */
    public void insertAccount(String dealerId, String parentId, Double amount, Long orderId, String orderCode, AclUserBean loginUser, String accountId) throws Exception {
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            TtPartAccountRecordPO po = new TtPartAccountRecordPO();
            Long recordId = Long.parseLong(SequenceManager.getSequence(""));
            po.setRecordId(recordId);
            po.setChangeType(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_01);
            po.setDealerId(Long.valueOf(dealerId));
            //获取账户余额等
            Map<String, Object> acountMap = dao.getAccount(dealerId, parentId, accountId);
            if (null != acountMap) {
                po.setAccountId(Long.valueOf(acountMap.get("ACCOUNT_ID").toString()));
            }
            po.setAmount(amount);
            po.setFunctionName("配件提报预占");
            po.setSourceId(Long.valueOf(orderId));
            po.setSourceCode(orderCode);
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            dao.insert(po);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 导出
     */
    public void exportToExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));//退货类型标志
        try {
            String[] head = new String[18];
            List<Map<String, Object>> list = null;
            String dealerId = "";
            if ("2".equals(radioSelect)) {
                head[0] = "订单号";
                head[1] = "订货单位代码";
                head[2] = "订货单位";
                head[3] = "配件编码";
                head[4] = "配件名称";
                head[5] = "切换件编码";
                head[6] = "切换件名称";
                head[7] = "申请数量";
                head[8] = "发出数量";
                head[9] = "回运数量";
                head[10] = "切换件回运数量";
                head[11] = "返回数量";
                head[12] = "切换件返回数量";
                head[13] = "差异数量";
                head[14] = "状态";
            } else if ("1".equals(radioSelect)) {
                head[0] = "活动编码";
                head[1] = "订单号";
                head[2] = "订单类型";
                head[3] = "订货单位编码";
                head[4] = "订货单位";
                head[5] = "订货人";
                head[6] = "制单日期";
                head[7] = "提交日期";
                head[8] = "订单状态";
            } else {
                head[0] = "订货单位编码";
                head[1] = "订货单位";
                head[2] = "切换件代码";
                head[3] = "切换名称";
                head[4] = "总发出数量";
                head[5] = "总返回数量";
                head[6] = "差异数量";
            }
            PageResult<Map<String, Object>> ps = null;
            if ("2".equals(radioSelect)) {
                ps = dao.partDlrOrderDtlQuery(request, 1, 99999);
            } else if ("1".equals(radioSelect)) {
                ps = dao.partDlrOrderStateQuery(request, 1, 99999);
            } else {
                ps = dao.partDlrOrderSumQuery(request, 1, 99999);
            }
            list = ps.getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[18];
                        if ("2".equals(radioSelect)) {
                            detail[0] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                            detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                            detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                            detail[3] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                            detail[4] = CommonUtils.checkNull(map.get("PART_CNAME"));
                            detail[5] = CommonUtils.checkNull(map.get("PART_OLDCODE2"));
                            detail[6] = CommonUtils.checkNull(map.get("PART_CNAME2"));
                            detail[7] = CommonUtils.checkNull(map.get("BUY_QTY"));
                            detail[8] = CommonUtils.checkNull(map.get("CHECK_QTY"));
                            detail[9] = CommonUtils.checkNull(map.get("BACKHAUL_QTY"));
                            detail[10] = CommonUtils.checkNull(map.get("NBACK_QTY"));
                            detail[11] = CommonUtils.checkNull(map.get("WAREHOUSING_QTY"));
                            detail[12] = CommonUtils.checkNull(map.get("NINSTOCK_QTY"));
                            detail[13] = CommonUtils.checkNull(map.get("DIFF_QTY"));
                            detail[14] = CommonUtils.getCodeDesc(CommonUtils.checkNull(map.get("STATE")));
                        } else if ("1".equals(radioSelect)) {
                            detail[0] = CommonUtils.checkNull(map.get("ACTIVITY_CODE"));
                            detail[1] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                            detail[2] = CommonUtils.getCodeDesc(CommonUtils.checkNull(map.get("ORDER_TYPE")));
                            detail[3] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                            detail[4] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                            detail[5] = CommonUtils.checkNull(map.get("BUYER_NAME"));
                            detail[6] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                            detail[7] = CommonUtils.checkNull(map.get("SUBMIT_DATE"));
                            detail[8] = CommonUtils.getCodeDesc(CommonUtils.checkNull(map.get("STATE")));
                        } else {
                            detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                            detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                            detail[2] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                            detail[3] = CommonUtils.checkNull(map.get("PART_CNAME"));
                            detail[4] = CommonUtils.checkNull(map.get("OUTSTOCK_QTY"));
                            detail[5] = CommonUtils.checkNull(map.get("PART_NUM"));
                            detail[6] = CommonUtils.checkNull(map.get("DIFF_QTY"));
                        }
                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, radioSelect);
            } else {
                BizException e1 = new BizException(act,
                        ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);

        }

    }

    /**
     * @param response
     * @param request
     * @param head
     * @param list
     * @param flag
     * @return
     * @throws Exception
     */
    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list, String flag)
            throws Exception {

        String name = "";
        if ("1".equals(flag)) {
            name = "切换单信息.xls";
        } else {
            name = "切换单明细信息.xls";
        }
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
     * 切换件关闭
     */
    public void closeOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            String orderId = CommonUtils.checkNull(req.getParamValue("orderId"));
            //校验修改
           /* if (!"1".equals(flag) && this.validateState(orderId, allowState)) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单状态已经改变，无法作废!");
                throw e1;
            }*/
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            oldPo.setOrderId(Long.valueOf(orderId));

            TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
            newPo.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07);
            newPo.setUpdateBy(logonUser.getUserId());
            newPo.setUpdateDate(new Date());

            dao.update(oldPo, newPo);
            this.saveHistory(req, act, Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07, "关闭", orderId);
            act.setOutData("success", "关闭成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "关闭失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        } finally {
            try {
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "关闭失败,请联系管理员!");
            }
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 导出EXECEL模板
     * @Description: TODO
     */
    public void templateDownload() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        ResponseWrapper response = act.getResponse();
        OutputStream os = null;
        try {
            List<List<Object>> list = new LinkedList<List<Object>>();
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
            //标题
            listHead.add("活动编码");
            listHead.add("服务商代码");
            listHead.add("配件编码");
            listHead.add("切换件编码");
            listHead.add("数量");
            listHead.add("备注");
            list.add(listHead);
            // 导出的文件名
            String fileName = "切换提报模板.xls";
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

    public void dataUpload() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                parentOrgId = Constant.OEM_ACTIVITIES;
            } else {
                parentOrgId = logonUser.getDealerId();
            }
            List<Map<String, String>> errorInfo = null;
            String err = "";

            errorInfo = new ArrayList<Map<String, String>>();
            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmp(request, "uploadFile", 6, 3, maxSize);

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
                        err += "文件不能大于" + maxSize + "!";
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
                List<Map<String, String>> voList = new ArrayList<Map<String, String>>();
                loadVoList(voList, list, errorInfo);
                if (errorInfo.size() > 0) {
                    act.setOutData("errorInfo", errorInfo);
                    act.setForword(INPUT_ERROR_URL);
                } else {
                    //保存
                    savePkgStk(voList);
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
     * @Title : 读取CELL
     */
    private void loadVoList(List<Map<String, String>> voList, List<Map> list, List<Map<String, String>> errorInfo) {
        //一次性获取所有有效活动编码
        Map<String, Object> allActiMap = dao.getAllActivityMap();
        //一次性获取所有有效服务商编码
        Map<String, Object> allDealerMap = rebateManagerDao.getInstance().getDealerInfo();
        //一次性获取配件信息
        Map<String, Object> partMap = dao.getAllActivityPartMap("1");
        //一次性获取切换件信息
        Map<String, Object> rePartMap = dao.getAllActivityPartMap("2");
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
                String partIdTmp = "";
                Map<String, String> tempmap = new HashMap<String, String>();
                if ("".equals(cells[0].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "活动编码");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                } else {
                    if (null != allActiMap.get(cells[0].getContents().trim().toUpperCase())) {
                        tempmap.put("activityCode", cells[0].getContents().trim());
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "活动编码【" + cells[0].getContents().trim() + "】");
                        errormap.put("3", "不存在!");
                        errorInfo.add(errormap);
                    }
                }

                if (cells.length < 2 || CommonUtils.isEmpty(cells[1].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "经销商代码");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                } else {
                    String dealerId = allDealerMap.get(cells[1].getContents().trim().toUpperCase()).toString().split(",")[0].toString();
                    String dealerName = allDealerMap.get(cells[1].getContents().trim().toUpperCase()).toString().split(",")[1].toString();
                    if (null != dealerId && "" != dealerId) {
                        tempmap.put("dealerId", dealerId);
                        tempmap.put("dealerName", dealerName);
                        tempmap.put("dealerCode", cells[1].getContents().trim().toUpperCase());
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "经销商代码【" + cells[1].getContents().trim() + "】");
                        errormap.put("3", "不存在!");
                        errorInfo.add(errormap);
                    }
                }

                if (cells.length < 3 || CommonUtils.isEmpty(cells[2].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "配件编码");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                } else {
                    String partId = partMap.get(cells[0].getContents().trim().toUpperCase() + "|" + cells[2].getContents().trim().toUpperCase()).toString();
                    if (null != partId && "" != partId) {
                        tempmap.put("partOldcode", cells[2].getContents().trim());
                        tempmap.put("partId", partId);
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "配件编码【" + cells[2].getContents().trim() + "】");
                        errormap.put("3", "不存在!");
                        errorInfo.add(errormap);
                    }
                }

                if (cells.length < 4 || CommonUtils.isEmpty(cells[3].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "切换件编码");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                } else {
                    String rePartId = rePartMap.get(cells[0].getContents().trim().toUpperCase() + "|" + cells[3].getContents().trim().toUpperCase()).toString();
                    if (null != rePartId && "" != rePartId) {
                        tempmap.put("repartOldcode", cells[3].getContents().trim());
                        tempmap.put("rePartId", rePartId);
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "切换件编码【" + cells[3].getContents().trim() + "】");
                        errormap.put("3", "不存在!");
                        errorInfo.add(errormap);
                    }
                }

                if (cells.length < 5 || CommonUtils.isEmpty(cells[4].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "数量");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                } else {
                    String partNum = cells[4].getContents().trim();
                    String regex = "(^[1-9]+\\d*$)";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(partNum);

                    if (matcher.find()) {
                        tempmap.put("partNum", partNum);
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "数量");
                        errormap.put("3", "必须为正整数!");
                        errorInfo.add(errormap);
                    }
                }

                if (cells.length < 6 || CommonUtils.isEmpty(cells[5].getContents())) {
                    tempmap.put("remarks", " ");
                } else {
                    tempmap.put("remarks", cells[5].getContents().trim());
                }

                voList.add(tempmap);
            }
        }
    }


    /**
     * @param : @param relList
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 批量更新配件包装储运
     */
    public void savePkgStk(List<Map<String, String>> relList) {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String orderRemark = act.getRequest().getParamValue("orderRemark");

        try {
            if (null != relList && relList.size() > 0) {
                TtPartOrderUploadPO partOrderUploadPo = new TtPartOrderUploadPO();

                int listSize = relList.size();

                long uploadId = Long.parseLong(OrderCodeManager.getSequence(""));
                int orderType = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10;
                long createBy = logonUser.getUserId();

                for (int i = 0; i < listSize; i++) {

                    String activityCode = relList.get(i).get("activityCode");//活动编码
                    Long dealerId = Long.parseLong(relList.get(i).get("dealerId"));//服务商ID
                    String dealerCode = relList.get(i).get("dealerCode");//服务商代码
                    String dealerName = relList.get(i).get("dealerName");//服务商名称
                    Long partId = Long.parseLong(relList.get(i).get("partId"));//配件ID
                    Long rePartId = Long.parseLong(relList.get(i).get("rePartId"));//替换件配件ID
                    String partOldcode = relList.get(i).get("partOldcode");//配件编码
                    String repartOldcode = relList.get(i).get("repartOldcode");//切换件编码
                    long partNum = Long.parseLong(relList.get(i).get("partNum"));//数量
                    String remark = relList.get(i).get("remarks");//备注

                    partOrderUploadPo.setUploadId(uploadId);
                    partOrderUploadPo.setActivityCode(activityCode);
                    partOrderUploadPo.setDealerCode(dealerCode);
                    partOrderUploadPo.setDealerId(dealerId);
                    partOrderUploadPo.setDealerName(dealerName);
                    partOrderUploadPo.setPartId(partId);
                    partOrderUploadPo.setRepartId(rePartId);
                    partOrderUploadPo.setPartOldcode(partOldcode);
                    partOrderUploadPo.setRepartOldcode(repartOldcode);
                    partOrderUploadPo.setOrderType(orderType);
                    partOrderUploadPo.setPartNum(partNum);
                    partOrderUploadPo.setRemark(remark);
                    partOrderUploadPo.setCreateBy(createBy);
                    partOrderUploadPo.setCreateDate(new Date());

                    dao.insert(partOrderUploadPo);
                }
                act.setOutData("uploadId", uploadId);

            }

            act.setOutData("relList", relList);
            act.setOutData("orderRemark", orderRemark);
            act.setForword(DISPLAY_IMPORTED_DATA_URL);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "批量更新配件包装储运失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     *
     */
    public void saveData() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper request = act.getRequest();
        String uploadId = request.getParamValue("uploadId");
        String orderRemark = CommonUtils.checkNull(request.getParamValue("orderRemark"));
        try {
            List<Map<String, Object>> dataRepeat = null;
            dataRepeat = dao.checkDataRepeat(uploadId);
            if (dataRepeat.size() > 0) {
                act.setOutData("error", "操作失败，数据重复!");
                return;
            }
            List<Map<String, Object>> dearlerIDList = dao.dealerIDQuery(uploadId);//从临时表中提取该次导入订单信息所涉及到的经销商
            for (Map<String, Object> map : dearlerIDList) {
                //查询得到订单头部信息
                Map<String, Object> mainInfo = orderMainInfoQuery(map);
                Long orderId = Long.parseLong(OrderCodeManager.getSequence(""));
                request.setAttribute("orderId", orderId);
                String msg = "";
                request.setAttribute("msg", msg);

                //从临时表中提取同一经销商及统一活动编号的订单信息
                String dealerId = map.get("DEALER_ID").toString();
                String activityCode = map.get("ACTIVITY_CODE").toString();

                List<Map<String, Object>> partlist = dao.dataExtraction(uploadId, dealerId, activityCode);
                mainInfo.put("activityCode", activityCode);
                mainInfo.put("orderRemark", orderRemark);

//                for (Map<String, Object> partmap : partlist) {
//                    给每条配件信息补充必要数据
//                    List<Map<String, Object>> repPartInfo = dao.RepplacedPartInfo(partmap, mainInfo.get("SELLER_ID").toString());
//                    repPartInfo.get(0).put("BUYQTY", partmap.get("PART_NUM"));
//                    repPartInfo.get(0).put("REMARK", partmap.get("REMARK"));
//                    double salePrice = Double.parseDouble(repPartInfo.get(0).get("SALE_PRICE1").toString());
//                    double num = Double.parseDouble(partmap.get("PART_NUM").toString());
//                    mainAmount = salePrice * num + mainAmount;
//                    加入订单详细信息
//                    savePartDetailPo(request, act, repPartInfo.get(0));
//                }
                //保存主订单
                if (partlist.size() > 0) {
                    saveMainPo(request, act, savePartDetailPo(request, act, partlist), mainInfo);
                }
                String what = "切换订单提报";
                this.saveHistory(request, act, Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02, what, orderId.toString());

                //资源校验开关控制
                //调用库存占用逻辑
                if (UPDATE_PART_STATE_SWITCH) {
                    List ins = new LinkedList<Object>();
                    ins.add(0, orderId);
                    ins.add(1, Constant.PART_CODE_RELATION_41);
                    ins.add(2, 1);// 1:占用 0：释放占用
                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
                }
            }
            act.setOutData("success", "操作成功!");
//        partDlrOrderCheckInit();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "批量导入切换订单失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     *
     */
    public void goError() {

        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();

        String uploadId = request.getParamValue("uploadId");
        try {
            List<Map<String, Object>> dataRepeat = dao.checkDataRepeat(uploadId);
            List<Map<String, String>> dataError = new ArrayList<Map<String, String>>();
            if (dataRepeat.size() > 0 || dataRepeat != null) {

                for (Map<String, Object> repeat : dataRepeat) {
                    Map<String, String> errordata = new HashMap<String, String>();
                    errordata.put("DEALER_CODE", repeat.get("DEALER_CODE").toString());
                    errordata.put("ACTIVITY_CODE", repeat.get("ACTIVITY_CODE").toString());
                    errordata.put("PART_OLDCODE", repeat.get("PART_OLDCODE").toString());
                    errordata.put("REPART_OLDCODE", repeat.get("REPART_OLDCODE").toString());
                    dataError.add(errordata);
                }
            }

            act.setOutData("dataError", dataError);
            act.setForword(REPEAT_ERROR_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "错误页面无法显示,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param map
     * @return
     */
    public Map<String, Object> orderMainInfoQuery(Map<String, Object> map) {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            PartDlrOrderDao partDlrOrderDao = PartDlrOrderDao.getInstance();
            //仓库
            //获取单位信息
            String dealerId = map.get("DEALER_ID").toString();

            dataMap.put("buyerName", logonUser.getName());
            dataMap.put("dealerId", dealerId);
            dataMap.put("dealerName", map.get("DEALER_NAME").toString());
            dataMap.put("dealerCode", map.get("DEALER_CODE").toString());

            //设置默认销售单位 (只考虑有一个上级销售单位)
            PageResult<Map<String, Object>> ps = partDlrOrderDao.getSalesRelation(dealerId, "UN", "", "", "", 1, 1);
            List<Map<String, Object>> selList = null;
            if (null != ps && ps.getRecords().size() > 0) {
                selList = ps.getRecords();
                dataMap.put("SELLER_NAME", selList.get(0).get("PARENTORG_NAME").toString());
                dataMap.put("SELLER_CODE", selList.get(0).get("PARENTORG_CODE").toString());
                dataMap.put("SELLER_ID", selList.get(0).get("PARENTORG_ID").toString());
                dataMap.put("accountId", selList.get(0).get("ACCOUNT_ID").toString());
                dataMap.put("accountSum", selList.get(0).get("ACCOUNT_SUM").toString());
                dataMap.put("accountKy", selList.get(0).get("ACCOUNT_KY").toString());
                dataMap.put("accountDj", selList.get(0).get("ACCOUNT_DJ").toString());

                //设置收货地址默认值
                Map<String, Object> defaultValueMap = new HashMap<String, Object>();
                defaultValueMap.put("defaultStation", CommonUtils.checkNull(selList.get(0).get("STATION")));
                defaultValueMap.put("defaultLinkman", CommonUtils.checkNull(selList.get(0).get("LINKMAN")));
                defaultValueMap.put("defaultTel", CommonUtils.checkNull(selList.get(0).get("TEL")));
                defaultValueMap.put("defaultPostCode", CommonUtils.checkNull(selList.get(0).get("POST_CODE")));
                defaultValueMap.put("defaultAddrId", CommonUtils.checkNull(selList.get(0).get("ADDR_ID")));
                defaultValueMap.put("defaultAddr", CommonUtils.checkNull(selList.get(0).get("ADDR")));
                defaultValueMap.put("defaultRcvOrgid", dealerId);
                defaultValueMap.put("defaultRcvOrg", map.get("DEALER_NAME").toString());
                dataMap.putAll(defaultValueMap);
            }

            dataMap.put("orderType", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10);
            return dataMap;
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "替换件订单报新增时错误,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_REPLACED_DLR_ORDER);
            return null;
        }
    }

    /**
     * @param req
     * @param act
     * @param list
     * @return
     * @throws Exception
     */
    private Double savePartDetailPo(RequestWrapper req, ActionContext act, List<Map<String, Object>> list) throws Exception {
        String error = "";
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        List<TtPartDlrOrderDtlPO> dtlPOs = new ArrayList<TtPartDlrOrderDtlPO>();
        String msg = req.getAttribute("msg") + "";
        Double orderAmount = 0d;
        try {
            for (Map<String, Object> partmap : list) {
                TtPartDlrOrderDtlPO po = new TtPartDlrOrderDtlPO();
                Long orderId = null;
                if (null != req.getAttribute("orderId")) {
                    orderId = Long.valueOf(CommonUtils.checkNull(req.getAttribute("orderId")));
                } else {
                    orderId = Long.valueOf(CommonUtils.checkNull(req.getParamValue("orderId")));
                }

                po.setLineId(Long.parseLong(OrderCodeManager.getSequence("")));
                po.setOrderId(orderId);
                po.setPartId(Long.valueOf(partmap.get("PART_ID").toString()));
                po.setRepartId(Long.valueOf(partmap.get("REPART_ID").toString()));
                po.setPartCname(partmap.get("PART_CNAME").toString());
                po.setPartOldcode(partmap.get("PART_OLDCODE").toString());
                po.setPartCode(partmap.get("PART_CODE").toString());
                po.setIsDirect(Constant.PART_BASE_FLAG_NO);
                po.setIsPlan(Constant.PART_BASE_FLAG_NO);
                po.setIsneedFlag(Integer.valueOf(partmap.get("ISNEED_FLAG").toString()));
                String minPackage = partmap.get("MIN_PACKAGE").toString();
                if (!CommonUtils.isEmpty(minPackage)) {
                    po.setMinPackage(Long.valueOf(minPackage));
                }
                long buyQty = Long.valueOf(partmap.get("PART_NUM").toString());
                po.setBuyQty(buyQty);
                po.setRemark(partmap.get("REMARK") == null ? "" : partmap.get("REMARK").toString());
                po.setCreateDate(new Date());
                po.setCreateBy(logonUser.getUserId());
                po.setStatus(Constant.STATUS_ENABLE);

                po.setBuyPrice(Double.valueOf(partmap.get("SALE_PRICE1").toString()));
                po.setBuyAmount(Arith.mul(Double.valueOf(partmap.get("SALE_PRICE1").toString()), Double.valueOf(partmap.get("PART_NUM").toString())));
                orderAmount += Arith.mul(Double.valueOf(partmap.get("SALE_PRICE1").toString()), Double.valueOf(partmap.get("PART_NUM").toString()));
                dtlPOs.add(po);
            }

            req.setAttribute("msg", msg);
            dao.insert(dtlPOs);

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, error);
            logger.error(logonUser, e1);
            act.setException(e1);
        }
        return orderAmount;
    }

    /**
     * @param req
     * @param act
     * @param mainAmount
     * @param mainInfo
     * @return
     * @throws Exception
     */
    private Double saveMainPo(RequestWrapper req, ActionContext act, Double mainAmount, Map<String, Object> mainInfo) throws Exception {
        String exStr = "";
        String orderCode = "";
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            TtPartDlrOrderMainPO po = new TtPartDlrOrderMainPO();
            Long orderId = Long.valueOf(req.getAttribute("orderId").toString());
            String produceFac = "9703001";
            String accountId = mainInfo.get("accountId").toString() == null ? "0" : mainInfo.get("accountId").toString();
            po.setOrderId(orderId);
            orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_41, mainInfo.get("dealerId").toString());
            req.setAttribute("orderCode", orderCode);
            po.setProduceFac(produceFac);
            po.setOrderCode(orderCode);
            po.setOrderType(Integer.valueOf(mainInfo.get("orderType").toString()));
            po.setPayType(Integer.valueOf(Constant.CAR_FACTORY_SALES_PAY_TYPE_01));
            try {
                po.setDealerId(Long.valueOf(mainInfo.get("dealerId").toString()));
            } catch (Exception ex) {
                exStr = "订货单位出错";
                throw ex;
            }
            po.setDealerCode(mainInfo.get("dealerCode").toString());
            po.setDealerName(mainInfo.get("dealerName").toString());
            try {
                po.setSellerId(Long.valueOf(mainInfo.get("SELLER_ID").toString()));
            } catch (Exception ex) {
                exStr = "销售单位出错";
                throw ex;
            }
            po.setSellerCode(mainInfo.get("SELLER_CODE").toString());
            po.setSellerName(mainInfo.get("SELLER_NAME").toString());
            po.setBuyerId(logonUser.getUserId());
            po.setBuyerName(mainInfo.get("buyerName").toString());
            po.setRcvOrgid(Long.valueOf(mainInfo.get("defaultRcvOrgid").toString()));
            po.setRcvOrg(mainInfo.get("defaultRcvOrg").toString());
            po.setIsAutchk(Constant.PART_BASE_FLAG_NO);
            try {
                if (CommonUtils.checkNull(mainInfo.get("defaultAddrId").toString()) != "") {
                    po.setAddrId(Long.valueOf(mainInfo.get("defaultAddrId").toString()));
                }
            } catch (Exception ex) {
                exStr = "接收地址出错";
                throw ex;
            }
            po.setAddr(mainInfo.get("defaultAddr").toString());
            po.setReceiver(mainInfo.get("defaultLinkman").toString());
            po.setTel(mainInfo.get("defaultTel").toString());
            po.setPostCode(mainInfo.get("defaultPostCode").toString());
            po.setStation(mainInfo.get("defaultStation").toString());

            List<TtPartFixcodeDefinePO> fixList = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式

            po.setTransType(fixList.get(0).getFixValue());
            if (null != mainInfo.get("accountSum").toString() && !"null".equals(mainInfo.get("accountSum").toString())) {
                po.setAccountSum(Double.valueOf(mainInfo.get("accountSum").toString().replace(",", "")));
            }
            if (null != mainInfo.get("accountKy") && !"null".equals(mainInfo.get("accountKy").toString())) {
                po.setAccountKy(Double.valueOf(mainInfo.get("accountKy").toString().replace(",", "")));
            }
            if (null != mainInfo.get("accountDj").toString() && !"null".equals(mainInfo.get("accountDj").toString())) {
                po.setAccountDj(Double.valueOf(mainInfo.get("accountDj").toString().replace(",", "")));
            }

            po.setCreateDate(new Date());
            po.setCreateBy(logonUser.getUserId());
            po.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04);

            po.setSubmitDate(new Date());
            po.setSubmitBy(logonUser.getUserId());
            po.setVer(1);
            //如果是服务商给供应商提报的直发订单   需要先给供应商审核   然后又供应商给车厂审核
            //获取单位ID
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";

            }
            if ((po.getOrderType() + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08 + "")) {
                po.setDiscountStatus(Constant.IF_TYPE_YES + "");
            }
            if (po.getTransType().equals("3")) {

            }
            po.setIsTransfree(Constant.IF_TYPE_YES);
            if (dealerId.equals(Constant.OEM_ACTIVITIES + "")) {
                po.setOemFlag(Constant.PART_BASE_FLAG_YES);
            } else {
                po.setOemFlag(Constant.PART_BASE_FLAG_NO);
            }
            po.setActivityCode(mainInfo.get("activityCode").toString());
            po.setAccountId(Long.valueOf(accountId));

            Double freight = 0D;
            if ((po.getSellerId() + "").equals(Constant.OEM_ACTIVITIES)) {
                freight = Double.valueOf((dao.getFreight(po.getDealerId() + "", po.getOrderType() + "", mainAmount + "")).replaceAll(",", ""));
            }
            if (po.getTransType().equals("3")) {
                freight = 0D;
            }
            //2状态服务商免运费 ADD BY YUAN 20131225 START
            if (dao.isTransFree(po.getDealerId().toString()).equals("2")) {
                freight = 0D;
            }
            //END
            if (freight > 0) {
                po.setIsTransfree(Constant.IF_TYPE_NO);
            } else {
                po.setIsTransfree(Constant.IF_TYPE_YES);
            }
            po.setOrderAmount(Arith.add(mainAmount, freight));
            po.setFreight(freight);
            po.setRemark(mainInfo.get("orderRemark").toString());//订单备注
            mainAmount += freight;
            dao.insert(po);
            req.setAttribute("orderCode", po.getOrderCode());
            return mainAmount;
        } catch (Exception ex) {
            BizException e1 = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, exStr + ",请联系管理员!");
            throw e1;
        }
    }

    /**
     * 切换件验收驳回
     */
    public void reButOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            String orderId = CommonUtils.checkNull(req.getParamValue("orderId"));
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            oldPo.setOrderId(Long.valueOf(orderId));

            TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
            newPo.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03);
            newPo.setUpdateBy(logonUser.getUserId());
            newPo.setUpdateDate(new Date());

            TtPartDlrOrderDtlPO orderDtlPO = new TtPartDlrOrderDtlPO();
            orderDtlPO.setOrderId(Long.valueOf(orderId));

            TtPartDlrOrderDtlPO orderDtlPO1 = new TtPartDlrOrderDtlPO();
            orderDtlPO1.setBackhaulQty(0l); //已回运数量为0

            dao.update(oldPo, newPo);
            dao.update(orderDtlPO, orderDtlPO1);
            this.saveHistory(req, act, Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07, "验收驳回", orderId);
            act.setOutData("success", "驳回成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "验收驳回失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        } finally {
            try {
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "关闭失败,请联系管理员!");
            }
        }
    }
}
