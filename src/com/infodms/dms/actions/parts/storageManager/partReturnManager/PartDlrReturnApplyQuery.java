package com.infodms.dms.actions.parts.storageManager.partReturnManager;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.storageManager.partReturnManager.PartDlrReturnApplyDAO;
import com.infodms.dms.dao.parts.storageManager.partReturnManager.PartDlrReturnChkrDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartDlrInstockDtlPO;
import com.infodms.dms.po.TtPartDlrReturnDtlPO;
import com.infodms.dms.po.TtPartDlrReturnMainPO;
import com.infodms.dms.po.TtPartLoactionDefinePO;
import com.infodms.dms.po.TtPartOutstockMainPO;
import com.infodms.dms.po.TtPartSalesRelationPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import jxl.Workbook;
import jxl.write.Label;

/**
 * @author : luole
 *         CreateDate     : 2013-4-15
 * @ClassName : PartSalesTHApply
 * @Description : 销售退货申请
 */
@SuppressWarnings("unchecked")
public class PartDlrReturnApplyQuery {
    private static final Integer PRINT_SIZE = 10;
    public Logger logger = Logger.getLogger(PartDlrReturnApplyQuery.class);
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    PartDlrReturnApplyDAO dao = PartDlrReturnApplyDAO.getInstance();
    private final String PART_RETURN_APPLY_INIT_URL = "/jsp/parts/storageManager/partReturnManager/partDlrReturnApply/partDlrReturnApplyQuery.jsp";// 配件销售退货申请初始化页面
    private final String PART_RETURN_APPLYDETAIL_INIT_URL = "/jsp/parts/storageManager/partReturnManager/partDlrReturnApply/partDlrReturnApplyDetailQuery.jsp";// 配件销售退货申请明细页面
    private final String PART_RETURN_UPDATE_INIT_URL = "/jsp/parts/storageManager/partReturnManager/partDlrReturnApply/partDlrReturnApplyMod.jsp";// 配件销售退货申请修改页面
    private final String PART_RETURN_APPLY_ADD_URL = "/jsp/parts/storageManager/partReturnManager/partDlrReturnApply/partDlrReturnApplyAdd.jsp";// 配件销售退货申请新增页面
    private final String PART_DLRRETURN_PRINT_URL = "/jsp/parts/storageManager/partReturnManager/partDlrReturnApply/partDlrReturnPrint.jsp";// 配件销售退货打印页面
    private final String SEL_RETURN_URL = "/jsp/parts/storageManager/partReturnManager/partDlrReturnApply/selReturn.jsp";// 退货单位选择
    private final String SEL_PART_URL = "/jsp/parts/storageManager/partReturnManager/partDlrReturnApply/selPart.jsp";// 配件选择

    /**
     * <p>Description: 配件销售退货申请初始化页面</p>
     */
    public void partReturnApplyInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            if (logonUser.getDealerId() == null) {//车厂
                act.setOutData("createOrg", logonUser.getOrgId());
            } else {
                act.setOutData("createOrg", logonUser.getDealerId());
            }

            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(PART_RETURN_APPLY_INIT_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售退货申请初始化页面");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title :
     * @Description: 查询配件销售退货申请
     */
    public void queryPartDlrReturnInfo() {

        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            String returnCode = CommonUtils.checkNull(request.getParamValue("RETURN_CODE"));//退货单号
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//退货单位
            String createDate_c = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
            String endDate_c = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间
            String state = CommonUtils.checkNull(request.getParamValue("state"));//制单结束时间
            String isWo = CommonUtils.checkNull(request.getParamValue("is_wo"));//制单结束时间

            String flag = "0";//默认是车厂,0表示车厂,1表示供应中心,2表示一般服务商
            if (logonUser.getDealerId() != null) {//如果不是车厂
                flag = "1";
                TmDealerPO tmDealerPO = new TmDealerPO();
                tmDealerPO.setDealerId(CommonUtils.parseLong(logonUser.getDealerId()));
                tmDealerPO = (TmDealerPO) dao.select(tmDealerPO).get(0);
                if (tmDealerPO.getPdealerType().intValue() != Constant.PART_SALE_PRICE_DEALER_TYPE_01.intValue()) {//如果不是供应中心
                    flag = "2";
                }
            }
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("returnCode", returnCode);
            paramMap.put("dealerName", dealerName);
            paramMap.put("createDate_c", createDate_c);
            paramMap.put("endDate_c", endDate_c);
            paramMap.put("flag", flag);
            paramMap.put("state", state);
            paramMap.put("isWo", isWo);

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartDlrReturnList(paramMap, logonUser, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title :
     * @Description: 提交退货申请
     */
    public void submitReturnApply() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String state = "";
            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            TtPartDlrReturnMainPO mainPO = new TtPartDlrReturnMainPO();
            mainPO.setReturnId(CommonUtils.parseLong(returnId));
            mainPO = (TtPartDlrReturnMainPO) dao.select(mainPO).get(0);

            state = mainPO.getState() + "";

            if ((Constant.PART_DLR_RETURN_STATUS_01 + "").equals(state) || (Constant.PART_DLR_RETURN_STATUS_03 + "").equals(state)) {
                if (mainPO.getStockOut() != null && !mainPO.getStockOut().equals(0l)) {
                    List<Map<String, Object>> dtlList = dao.queryPartDlrReturnDetailList1(returnId, "");

                    if (null != dtlList && dtlList.size() > 0) {
                        String errorStr = "";
                        String normalPart = "";
                        String buyPart = "";
                        for (int i = 0; i < dtlList.size(); i++) {
                            Map<String, Object> dtlMap = dtlList.get(i);
                            String partOldcode = dtlList.get(i).get("PART_OLDCODE").toString();
                            int applyQty = Integer.parseInt(dtlList.get(i).get("APPLY_QTY").toString());
                            int normalQty = Integer.parseInt(dtlMap.get("NORMAL_QTY").toString());
                            int buyQty = Integer.parseInt(dtlMap.get("BUY_QTY").toString());
                            if(normalQty < applyQty){
                                normalPart += partOldcode + " ";                                
                            }
                            
                            int returnQty = Integer.parseInt(dtlMap.get("IN_RETURN_QTY").toString());
                            if(buyQty - returnQty < applyQty){
                                buyPart += partOldcode + " ";                                
                            }
                            
                        }
                        
                        if(!"".equals(normalPart)){
                            errorStr = "配件【" + normalPart + "】可用库存小于申请退货数量!";
                        }
                        if(!"".equals(buyPart)){
                            if(!"".equals(errorStr)){
                                errorStr = "\n";
                            }
                            errorStr = "配件【" + buyPart + "】采购数量与已退货数量的差小于申请退货数量!";
                        }
                        

                        if ("".equals(errorStr)) {
                            TtPartDlrReturnMainPO spo = new TtPartDlrReturnMainPO();//源po
                            spo.setReturnId(CommonUtils.parseLong(returnId));
                            TtPartDlrReturnMainPO po = new TtPartDlrReturnMainPO();//更新po
                            po.setApplyDate(new Date());
                            po.setApplyBy(logonUser.getUserId());
                            //提交之后更新申请单状态为审核中
                            po.setState(Constant.PART_DLR_RETURN_STATUS_02);
                            po.setVerifyLevel(Constant.PART_RETURN_CHK_LEVEL_01); // 审核等级
                            dao.update(spo, po);

                            /*---------------------修改经销商入库明细记录的退货数量----------------------*/
                            Map<String, String> paramMap = new HashMap<String, String>();
                            paramMap.put("returnId", returnId); // 入库单主记录id
                            paramMap.put("loginId", logonUser.getUserId().toString()); // 当前用户
                            paramMap.put("sumColunm", "APPLY_QTY"); // 求和的字段 
                            PartDlrReturnChkrDao chkrDao = PartDlrReturnChkrDao.getInstance();
                            chkrDao.updateInStrockReturnQty(paramMap);
                            /*---------------------修改经销商入库明细记录的退货数量----------------------*/
                            
                            // 冻结出库单开票
                            if(mainPO.getSoId() != null){
                                TtPartOutstockMainPO outstockPO = new TtPartOutstockMainPO();
                                outstockPO.setSoId(mainPO.getSoId());
                                outstockPO.setIsInv(Constant.IF_TYPE_NO);
                            }

                            //调用库存占用逻辑
                            List<Object> ins = new LinkedList<Object>();
                            ins.add(0, mainPO.getReturnId());
                            ins.add(1, Constant.PART_CODE_RELATION_17);
                            ins.add(2, 1);// 1:占用 0：释放占用
                            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);

                            act.setOutData("success", "提交成功!");
                        } else {
                            act.setOutData("success", errorStr);
                        }
                    } else {
                        act.setOutData("success", "提交异常，请联系管理员!");
                    }
                } else {
                    act.setOutData("success", "提交异常，请联系管理员!");
                }
            } else {
                act.setOutData("success", "退货申请状态已更新，请重新查询结果!");
            }

            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "销售退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-12-20
     * @Title : 撤回提交的申请
     */
    public void rebackReturnApply() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String orderState = "";
            int outQtys = 0;
            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            String sqlStr = "AND RD.RETURN_ID = '" + returnId + "'";
            List<Map<String, Object>> listCheck = dao.orderStateCheck(sqlStr);

            if (null != listCheck && listCheck.size() > 0) {
                outQtys = Integer.parseInt(listCheck.get(0).get("OUT_QTYS").toString());
            }

            if (outQtys == 0) {
                TtPartDlrReturnMainPO mainPO = new TtPartDlrReturnMainPO();
                mainPO.setReturnId(CommonUtils.parseLong(returnId));
                mainPO = (TtPartDlrReturnMainPO) dao.select(mainPO).get(0);

                TtPartDlrReturnMainPO spo = new TtPartDlrReturnMainPO();//源po
                spo.setReturnId(CommonUtils.parseLong(returnId));
                TtPartDlrReturnMainPO po = new TtPartDlrReturnMainPO();//更新po
                po.setUpdateDate(new Date());
                po.setUpdateBy(logonUser.getUserId());
                //撤回为已保存状态
                po.setState(Constant.PART_DLR_RETURN_STATUS_01);
                dao.update(spo, po);

                orderState = mainPO.getState() + "";

                if ((Constant.PART_DLR_RETURN_STATUS_02 + "").equals(orderState)) {
                    //调用库存释放逻辑
                    List<Object> ins = new LinkedList<Object>();
                    ins.add(0, mainPO.getReturnId());
                    ins.add(1, Constant.PART_CODE_RELATION_17);
                    ins.add(2, 0);// 1:占用 0：释放占用
                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
                } else if ((Constant.PART_DLR_RETURN_STATUS_05 + "").equals(orderState) || (Constant.PART_DLR_RETURN_STATUS_04 + "").equals(orderState)) {
                    //调用库存释放逻辑
                    List<Object> ins = new LinkedList<Object>();
                    ins.add(0, mainPO.getReturnId());
                    ins.add(1, Constant.PART_CODE_RELATION_28);
                    ins.add(2, 0);// 1:占用 0：释放占用
                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
                }

                act.setOutData("success", "撤回已提交申请成功!");
            } else {
                act.setOutData("success", "退货申请状态已更新，请重新查询结果!");
            }

            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "撤回已提交申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-12-20
     * @Title : 作废销售退货申请
     */
    public void disableReturnApply() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String state = "";
            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            TtPartDlrReturnMainPO mainPO = new TtPartDlrReturnMainPO();
            mainPO.setReturnId(CommonUtils.parseLong(returnId));
            mainPO = (TtPartDlrReturnMainPO) dao.select(mainPO).get(0);

            state = mainPO.getState() + "";

            if ((Constant.PART_DLR_RETURN_STATUS_01 + "").equals(state) || (Constant.PART_DLR_RETURN_STATUS_03 + "").equals(state)) {
                TtPartDlrReturnMainPO spo = new TtPartDlrReturnMainPO();//源po
                spo.setReturnId(CommonUtils.parseLong(returnId));
                TtPartDlrReturnMainPO po = new TtPartDlrReturnMainPO();//更新po
                po.setDisableDate(new Date());
                po.setDisableBy(logonUser.getUserId());
                //提交之后更新申请单状态为审核中
                po.setState(Constant.PART_DLR_RETURN_STATUS_07);
                dao.update(spo, po);

                act.setOutData("success", "退货申请作废成功!");
            } else {
                act.setOutData("success", "退货申请状态已更新，请重新查询结果!");
            }

            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "销售退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title :
     * @Description: 查询退货申请明细初始化, 转到查询页面
     */
    public void queryReturnApplyDetailInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String state = CommonUtils.checkNull(request.getParamValue("state"));
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));
            Map<String, Object> map = dao.getPartDlrReturnMainInfo(returnId);
            request.setAttribute("po", map);
            request.setAttribute("state", state);
            request.setAttribute("rstate", Constant.PART_DLR_RETURN_STATUS_03);
            act.setOutData("flag", flag == null ? "0" : flag);
            act.setForword(PART_RETURN_APPLYDETAIL_INIT_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售退货申请明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title :
     * @Description: 查询退货申请明细
     */
    public void queryPartDlrReturnDetail() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartDlrReturnDetailList(returnId, soCode, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售退货申请明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-15
     * @Title : 新增初始化
     * @Description: TODO
     */
    public void addInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //获取退货单号
            String returnCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_17);
            act.setOutData("returnCode", returnCode);
            //判断当前用户所在单位类型,如果是车厂或供应中心就可以替它的下级提出退货申请,并且供应中心还可以向车厂提出退货申请,如果是一般服务商,就只能为自己提出退货申请
            int flag = 0;//默认是车厂,0表示车厂,1表示供应中心,2表示一般服务商
            String orgId = "";//制单单位id
            String createOrgName = "";//制单单位名称
            TmDealerPO tmDealerPO = new TmDealerPO();
            TmOrgPO tmOrgPO = new TmOrgPO();
            if (logonUser.getDealerId() == null) {
                orgId = Constant.OEM_ACTIVITIES;
                tmOrgPO.setOrgId(CommonUtils.parseLong(orgId));
                tmOrgPO = (TmOrgPO) dao.select(tmOrgPO).get(0);
                createOrgName = tmOrgPO.getOrgName();
            } else {
                orgId = logonUser.getDealerId();
                tmDealerPO.setDealerId(CommonUtils.parseLong(orgId));
                tmDealerPO = (TmDealerPO) dao.select(tmDealerPO).get(0);
                createOrgName = tmDealerPO.getDealerName();
            }

            List<PO> childOrg = new ArrayList<PO>();//退货单位
            List<PO> parentOrg = new ArrayList<PO>();//销售单位
            TtPartSalesRelationPO po = new TtPartSalesRelationPO();
            if (logonUser.getDealerId() == null) {//如果是车厂
                po.setParentorgId(CommonUtils.parseLong(orgId));
                childOrg = dao.select(po);
                act.setOutData("dealerId", logonUser.getOrgId().toString());
            } else if (tmDealerPO.getPdealerType().intValue() == Constant.PART_SALE_PRICE_DEALER_TYPE_01.intValue()) {//如果是供应中心,它还可以向车厂提出退货申请
                List<Map<String, Object>> clist;
                List<Map<String, Object>> plist;
                List<TtPartSalesRelationPO> parentOrg1 = new ArrayList<TtPartSalesRelationPO>();//销售单位(没有包括供应中心自己)
                clist = dao.queryChildOrg(logonUser.getDealerId());//获取该供应中心的下级(包括它自己)
                plist = dao.queryParentOrg(logonUser.getDealerId());//获取该供应中心的上级
                for (int i = 0; i < clist.size(); i++) {
                    Map<String, Object> map = clist.get(i);
                    TtPartSalesRelationPO cpo = new TtPartSalesRelationPO();
                    cpo.setChildorgId(((BigDecimal) map.get("CHILDORG_ID")).longValue());
                    cpo.setChildorgCode((String) map.get("CHILDORG_CODE"));
                    cpo.setChildorgName((String) map.get("CHILDORG_NAME"));
                    childOrg.add(cpo);
                }
                for (int i = 0; i < plist.size(); i++) {
                    Map<String, Object> map = plist.get(i);
                    TtPartSalesRelationPO ppo = new TtPartSalesRelationPO();
                    ppo.setParentorgId(((BigDecimal) map.get("PARENTORG_ID")).longValue());
                    ppo.setParentorgCode((String) map.get("PARENTORG_CODE"));
                    ppo.setParentorgName((String) map.get("PARENTORG_NAME"));
                    parentOrg.add(ppo);
                    parentOrg1.add(ppo);
                }
                TtPartSalesRelationPO curPPo = new TtPartSalesRelationPO();//当前供应中心(它也可以作为销售单位)
                curPPo.setParentorgId(CommonUtils.parseLong(logonUser.getDealerId()));
                curPPo.setParentorgCode(logonUser.getDealerCode());
                curPPo.setParentorgName(tmDealerPO.getDealerShortname());
                parentOrg.add(curPPo);
                flag = 1;
                act.setOutData("dealerId", logonUser.getDealerId());
                act.setOutData("dealerCode", logonUser.getDealerCode());
                act.setOutData("dealerName", tmDealerPO.getDealerShortname());
                act.setOutData("parentOrg1", parentOrg1);
            } else {//如果是一般服务商
                flag = 2;
                po.setChildorgId(CommonUtils.parseLong(orgId));
                parentOrg = dao.select(po);
                TtPartWarehouseDefinePO wDefinePO = new TtPartWarehouseDefinePO();
                wDefinePO.setOrgId(Long.valueOf(orgId));
                if (dao.select(wDefinePO).size() > 0) {
                    wDefinePO = (TtPartWarehouseDefinePO) dao.select(wDefinePO).get(0);
                }
                act.setOutData("dealerId", logonUser.getDealerId());
                act.setOutData("dealerCode", logonUser.getDealerCode());
                act.setOutData("dealerName", tmDealerPO.getDealerShortname());
                act.setOutData("wDefinePO", wDefinePO);
            }


            //判断退货申请销售单号是否强制检查,为0表示不强制检查，为1表示强制检查
//            TmBusinessParaPO tmBusinessParaPO = new TmBusinessParaPO();
//            tmBusinessParaPO.setParaId(60021001);
//            tmBusinessParaPO = (TmBusinessParaPO) dao.select(tmBusinessParaPO).get(0);
//
//            String soPara = tmBusinessParaPO.getParaValue();


            act.setOutData("orgId", orgId);
            act.setOutData("createOrgName", createOrgName);
//            act.setOutData("soPara", soPara);
//            act.setOutData("soPara", 1);
            act.setOutData("childOrg", childOrg);
            act.setOutData("parentOrg", parentOrg);
            act.setOutData("tmDealerPO", tmDealerPO);
            act.setOutData("tmOrgPO", tmOrgPO);
            act.setOutData("flag", flag);
            act.setForword(PART_RETURN_APPLY_ADD_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.ADD_FAILURE_CODE, "配件销售退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-1
     * @Title :
     * @Description: 查询退货单位
     */
    public void queryChildOrg() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            String childOrgCode = CommonUtils.checkNull(request.getParamValue("childOrgCode"));
            String childOrgName = CommonUtils.checkNull(request.getParamValue("childOrgName"));

            int flag = 0;//默认是车厂,0表示车厂,1表示供应中心,2表示一般服务商
            String orgId = "";
            if (logonUser.getDealerId() != null) {//如果不是车厂
                orgId = logonUser.getDealerId();
                flag = 1;
                TmDealerPO tmDealerPO = new TmDealerPO();
                tmDealerPO.setDealerId(CommonUtils.parseLong(logonUser.getDealerId()));
                tmDealerPO = (TmDealerPO) dao.select(tmDealerPO).get(0);
                if (tmDealerPO.getPdealerType().intValue() != Constant.PART_SALE_PRICE_DEALER_TYPE_01.intValue()) {//如果不是供应中心
                    flag = 2;
                }
            } else {
                orgId = logonUser.getOrgId().toString();
            }

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryChildOrgList(orgId, flag, childOrgCode, childOrgName, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "退货单位");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-24
     * @Title :
     * @Description: 查询配件信息
     */
    public void queryPartInfo() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            String childorgId = CommonUtils.checkNull(request.getParamValue("childorgId"));//退货单位

            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            partCode = partCode.toUpperCase();
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            partOldCode = partOldCode.toUpperCase();
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));

            String soPara = CommonUtils.checkNull(request.getParamValue("soPara"));//是否强制检查销售单号
            String inCode = CommonUtils.checkNull(request.getParamValue("inCode"));//销售单号
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//仓库id
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//仓库id
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//仓库id

            TtPartDefinePO po = new TtPartDefinePO();
            po.setPartCode(partCode);
            po.setPartOldcode(partOldCode);
            po.setPartCname(partCname);

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartInfoList(po, childorgId, soPara, inCode, whId, beginTime, endTime, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
            act.setOutData("inCode", inCode);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-24
     * @Title :
     * @Description: 保存申请
     */
    public void saveApply() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            TtPartDlrReturnMainPO ttDlrReturnMainPO = new TtPartDlrReturnMainPO();

            String[] partIds = request.getParamValues("cb");//获取所有选中的配件id
            String returnCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_17);//退货单号
            String inCode = CommonUtils.checkNull(request.getParamValue("inCode"));// 入库单号id
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//仓库
            String[] SALE_ORG = request.getParamValue("SALE_ORG").split(",");//销售单位信息

            //如果销售单号不为空,可以到出库单主表中查询订单类型,并获取销售日期
            if (!"".equals(inCode)) {
                String childorgId = CommonUtils.checkNull(request.getParamValue("childorgId"));
//                Map<String, Object> map = dao.getSoInfoByCode(soCode);//通过销售单号从出库单主表中获取信息
                Map<String, Object> map = dao.queryInCodeList(childorgId,SALE_ORG[0],inCode).get(0);//通过销售单号从出库单主表中获取信息
                
                ttDlrReturnMainPO.setSoId(((BigDecimal) map.get("SO_ID")).longValue());
                ttDlrReturnMainPO.setSoCode(map.get("SO_CODE").toString());
		    	ttDlrReturnMainPO.setOrderType(((BigDecimal)map.get("ORDER_TYPE")).intValue());
                ttDlrReturnMainPO.setSaleDate((Date) map.get("SALE_DATE"));
                ttDlrReturnMainPO.setInId(((BigDecimal) map.get("IN_ID")).longValue());
                ttDlrReturnMainPO.setInCode(inCode);
                ttDlrReturnMainPO.setInDate((Date) map.get("IN_DATE"));
            }
            String[] RETURN_DEALER = request.getParamValue("RETURN_DEALER").split(",");//退货单位信息
            String REMARK = request.getParamValue("REMARK");//退货原因

            ttDlrReturnMainPO.setReturnId(CommonUtils.parseLong(SequenceManager.getSequence("")));
            ttDlrReturnMainPO.setReturnCode(returnCode);
            ttDlrReturnMainPO.setDealerId(CommonUtils.parseLong(RETURN_DEALER[0]));
            ttDlrReturnMainPO.setDealerCode(RETURN_DEALER[1]);
            ttDlrReturnMainPO.setDealerName(RETURN_DEALER[2]);
            ttDlrReturnMainPO.setSellerId(CommonUtils.parseLong(SALE_ORG[0]));
            ttDlrReturnMainPO.setSellerCode(SALE_ORG[1]);
            ttDlrReturnMainPO.setSellerName(SALE_ORG[2]);
            ttDlrReturnMainPO.setRemark(REMARK);
            ttDlrReturnMainPO.setCreateDate(new Date());
            ttDlrReturnMainPO.setCreateBy(logonUser.getUserId());
            ttDlrReturnMainPO.setCreateOrg(CommonUtils.parseLong(request.getParamValue("orgId")));
            ttDlrReturnMainPO.setCreateOrgname(request.getParamValue("orgName"));
            ttDlrReturnMainPO.setState(Constant.PART_DLR_RETURN_STATUS_01);//状态为正常

            if (!"".equals(whId)) {//如果选择了仓库,那么出库库房就是该仓库
                //如果选择了仓库,就要验证库存
                for (int i = 0; i < partIds.length; i++) {
                    Map<String, Object> map = dao.queryNormalQty(CommonUtils.parseLong(whId), CommonUtils.parseLong(partIds[i]), CommonUtils.parseLong(RETURN_DEALER[0]));
                    if (map != null) {
                        long normalQty = ((BigDecimal) map.get("NORMAL_QTY")).longValue();//可用数量
                        String partCname = request.getParamValue("PART_CNAME" + partIds[i]);//配件名称
                        long applyQty = CommonUtils.parseLong(request.getParamValue("APPLY_QTY" + partIds[i]));//申请退货数量
                        if (normalQty < applyQty) {
                            act.setOutData("error", "配件【" + partCname + "】的退货数量不能大于当前库存数量:" + normalQty);
                            return;
                        }
                    } else {
                        String partCname = request.getParamValue("PART_CNAME" + partIds[i]);
                        act.setOutData("error", "配件【" + partCname + "】的库存数量为0,不能退货,请重新选择!");
                        return;
                    }
                }
                ttDlrReturnMainPO.setStockOut(CommonUtils.parseLong(whId));
            }

            //保存退货申请主信息
            dao.insert(ttDlrReturnMainPO);
//		    List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
//		    if(soCode!=null&&!"".equals(soCode)){//有销售单号的时候,就查询出库明细表
//		    	list = dao.getSoDtlInfoByPartId(soCode,partIds);
//		    }
            for (int i = 0; i < partIds.length; i++) {
                TtPartDlrReturnDtlPO ttDlrReturnDtlPO = new TtPartDlrReturnDtlPO();
                ttDlrReturnDtlPO.setDtlId(CommonUtils.parseLong(SequenceManager.getSequence("")));
//		    	if(soCode!=null&&!"".equals(soCode)){
//		    		Map map = list.get(i);
//		    		if(map.get("SLINE_ID")!=null){
//		    			ttDlrReturnDtlPO.setSlineId(((BigDecimal)map.get("SLINE_ID")).longValue());
//		    		}
//		    		if(map.get("LINE_NO")!=null){
//		    			ttDlrReturnDtlPO.setLineNo(((BigDecimal)map.get("LINE_NO")).longValue());
//		    		}
//		    		if(map.get("STOCK_QTY")!=null){
//		    			ttDlrReturnDtlPO.setStockQty(((BigDecimal)map.get("STOCK_QTY")).longValue());
//		    		}
//		    		if(map.get("OUTSTOCK_QTY")!=null){
//		    			ttDlrReturnDtlPO.setSalesQty(((BigDecimal)map.get("OUTSTOCK_QTY")).longValue());
//		    		}
//		    		if(map.get("SALE_PRICE")!=null){
//		    			ttDlrReturnDtlPO.setBuyPrice(((BigDecimal)map.get("SALE_PRICE")).doubleValue());
//		    		}
//		    	}
                
                Long partId = CommonUtils.parseLong(partIds[i]);
                TtPartLoactionDefinePO locPo = new TtPartLoactionDefinePO();
                locPo.setPartId(partId);
                locPo.setOrgId(Long.parseLong(logonUser.getDealerId()));
                locPo.setWhId(Long.parseLong(whId));
                locPo = (TtPartLoactionDefinePO) dao.select(locPo).get(0);
                
                ttDlrReturnDtlPO.setReturnId(ttDlrReturnMainPO.getReturnId());
                ttDlrReturnDtlPO.setPartId(CommonUtils.parseLong(partIds[i]));
                ttDlrReturnDtlPO.setPartCode(request.getParamValue("PART_CODE" + partIds[i]));
                ttDlrReturnDtlPO.setPartOldcode(request.getParamValue("PART_OLDCODE" + partIds[i]));
                ttDlrReturnDtlPO.setPartCname(request.getParamValue("PART_CNAME" + partIds[i]));
                ttDlrReturnDtlPO.setUnit(request.getParamValue("UNIT" + partIds[i]));
                ttDlrReturnDtlPO.setStockQty(CommonUtils.parseLong(request.getParamValue("NORMAL_QTY" + partIds[i])));
                ttDlrReturnDtlPO.setSalesQty(CommonUtils.parseLong(request.getParamValue("BUY_QTY" + partIds[i])));
                ttDlrReturnDtlPO.setApplyQty(CommonUtils.parseLong(request.getParamValue("APPLY_QTY" + partIds[i])));
                ttDlrReturnDtlPO.setBuyPrice(CommonUtils.parseDouble(request.getParamValue("BUY_PRICE" + partIds[i])));
                ttDlrReturnDtlPO.setOutlocId(locPo.getLocId());
                ttDlrReturnDtlPO.setRemark(request.getParamValue("REMARK" + partIds[i]));
                ttDlrReturnDtlPO.setCreateDate(new Date());
                ttDlrReturnDtlPO.setCreateBy(logonUser.getUserId());

                dao.insert(ttDlrReturnDtlPO);//保存退货申请明细
            }

            act.setOutData("success", "保存成功!");

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 获取退货单位类型
     *
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-9
     * @Title :
     * @Description: TODO
     */
    public void getPDealerType() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            int flag = 0;
            String str_id = request.getParamValue("dealerId");//服务商id
            TmDealerPO po = new TmDealerPO();
            po.setDealerId(CommonUtils.parseLong(str_id));
            po = (TmDealerPO) dao.select(po).get(0);
            if (Constant.PART_SALE_PRICE_DEALER_TYPE_01.equals(po.getPdealerType())) {//如果是供应中心
                flag = 1;
            }
            List<TtPartWarehouseDefinePO> list = dao.getPartWareHouseList(str_id);//获取机构的库房信息
            act.setOutData("wareHouses", list);
            act.setOutData("flag", flag);

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "退货单位类型获取失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-11
     * @Title :
     * @Description: 查询销售单号
     */
    public void querySoCode() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            String childorgId = CommonUtils.checkNull(request.getParamValue("childorgId"));
            String saleOrgId = CommonUtils.checkNull(request.getParamValue("saleOrgId"));
            String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//销售开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//销售结束时间

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.querySoCodeList(childorgId, saleOrgId, soCode, partOldCode,
                    beginTime, endTime, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售单号");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-18
     * @Title :
     * @Description: 修改退货申请信息初始化
     */
    public void updateRInfoInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            Map<String, Object> map = dao.getPartDlrReturnMainInfo(returnId);
            String inCode = (String) map.get("IN_CODE");
            List<Map<String, Object>> list = dao.queryPartDlrReturnDetailList1(returnId, inCode);
            request.setAttribute("po", map);
            request.setAttribute("list", list);
            act.setOutData("returnId", returnId);
            act.setForword(PART_RETURN_UPDATE_INIT_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.UPDATE_FAILURE_CODE, "配件销售退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-18
     * @Title :
     * @Description: 修改退货申请
     */
    public void updateApply() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String[] partIds = request.getParamValues("cb");//获取所有选中的配件id
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//仓库
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String childorgId = CommonUtils.checkNull(request.getParamValue("childorgId")); // 经销商id
            String REMARK = request.getParamValue("REMARK");//退货原因
            

            TtPartDlrReturnMainPO spo = new TtPartDlrReturnMainPO();
            spo.setReturnId(CommonUtils.parseLong(returnId));
            TtPartDlrReturnMainPO po = new TtPartDlrReturnMainPO();
            po.setRemark(REMARK);

            if (!"".equals(whId)) {//如果选择了仓库,那么出库库房就是该仓库
                //如果选择了仓库,就要验证库存
                for (int i = 0; i < partIds.length; i++) {
                    Map<String, Object> map = dao.queryNormalQty(CommonUtils.parseLong(whId), CommonUtils.parseLong(partIds[i]), CommonUtils.parseLong(childorgId));
                    if (map != null) {
                        long normalQty = ((BigDecimal) map.get("NORMAL_QTY")).longValue();//可用数量
                        String partCname = request.getParamValue("PART_CNAME" + partIds[i]);//配件名称
                        long applyQty = CommonUtils.parseLong(request.getParamValue("APPLY_QTY" + partIds[i]));//申请退货数量
                        if (normalQty < applyQty) {
                            act.setOutData("error", "配件【" + partCname + "】的退货数量不能大于当前库存数量:" + normalQty);
                            return;
                        }
                    } else {
                        String partCname = request.getParamValue("PART_CNAME" + partIds[i]);
                        act.setOutData("error", "配件【" + partCname + "】的库存数量为0,不能退货,请重新选择!");
                        return;
                    }
                }
            }

//            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//            if (soCode != null && !"".equals(soCode)) {//有销售单号的时候,就查询出库明细表
//            list = dao.queryInStockDtlList(childorgId, saleOrgId, soCode, partIds);
//            }
            List<TtPartDlrReturnDtlPO> dlrPoList = new ArrayList<TtPartDlrReturnDtlPO>();
            for (int i = 0; i < partIds.length; i++) {
                TtPartDlrReturnDtlPO ttDlrReturnDtlPO = new TtPartDlrReturnDtlPO();
                ttDlrReturnDtlPO.setDtlId(CommonUtils.parseLong(SequenceManager.getSequence("")));
//                    if (soCode != null && !"".equals(soCode)) {
//                        Map<String, Object> map = list.get(i);
//                        if (map.get("SLINE_ID") != null) {
//                            ttDlrReturnDtlPO.setSlineId(((BigDecimal) map.get("SLINE_ID")).longValue());
//                        }
//                        if (map.get("LINE_NO") != null) {
//                            ttDlrReturnDtlPO.setLineNo(((BigDecimal) map.get("LINE_NO")).longValue());
//                        }
//                        if (map.get("STOCK_QTY") != null) {
//                            ttDlrReturnDtlPO.setStockQty(((BigDecimal) map.get("STOCK_QTY")).longValue());
//                        }
//                        if (map.get("OUTSTOCK_QTY") != null) {
//                            ttDlrReturnDtlPO.setSalesQty(((BigDecimal) map.get("OUTSTOCK_QTY")).longValue());
//                        }
//                        if (map.get("SALE_PRICE") != null) {
//                            ttDlrReturnDtlPO.setBuyPrice(((BigDecimal) map.get("SALE_PRICE")).doubleValue());
//                        }
//                    }

                Long partId = CommonUtils.parseLong(partIds[i]);
                TtPartLoactionDefinePO locPo = new TtPartLoactionDefinePO();
                locPo.setPartId(partId);
                locPo.setOrgId(Long.parseLong(logonUser.getDealerId()));
                locPo.setWhId(Long.parseLong(whId));
                locPo = (TtPartLoactionDefinePO) dao.select(locPo).get(0);
                
                ttDlrReturnDtlPO.setReturnId(spo.getReturnId());
                ttDlrReturnDtlPO.setPartId(CommonUtils.parseLong(partIds[i]));
                ttDlrReturnDtlPO.setPartCode(request.getParamValue("PART_CODE" + partIds[i]));
                ttDlrReturnDtlPO.setPartOldcode(request.getParamValue("PART_OLDCODE" + partIds[i]));
                ttDlrReturnDtlPO.setPartCname(request.getParamValue("PART_CNAME" + partIds[i]));
                ttDlrReturnDtlPO.setUnit(request.getParamValue("UNIT" + partIds[i]));
                ttDlrReturnDtlPO.setStockQty(CommonUtils.parseLong(request.getParamValue("NORMAL_QTY" + partIds[i])));
                ttDlrReturnDtlPO.setSalesQty(CommonUtils.parseLong(request.getParamValue("BUY_QTY" + partIds[i])));
                ttDlrReturnDtlPO.setApplyQty(CommonUtils.parseLong(request.getParamValue("APPLY_QTY" + partIds[i])));
                ttDlrReturnDtlPO.setBuyPrice(CommonUtils.parseDouble(request.getParamValue("BUY_PRICE" + partIds[i])));
                ttDlrReturnDtlPO.setOutlocId(locPo.getLocId());
                ttDlrReturnDtlPO.setRemark(request.getParamValue("REMARK" + partIds[i]));
                ttDlrReturnDtlPO.setCreateDate(new Date());
                ttDlrReturnDtlPO.setCreateBy(logonUser.getUserId());

                dao.insert(ttDlrReturnDtlPO);//保存退货申请明细
                
                dlrPoList.add(ttDlrReturnDtlPO);
            }
            dao.update(spo, po);
            
            TtPartDlrReturnDtlPO delDtlPo = new TtPartDlrReturnDtlPO();
            delDtlPo.setReturnId(spo.getReturnId());
            dao.delete(delDtlPo);
            
            dao.insert(dlrPoList);
            act.setOutData("success", "保存成功!");

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-18
     * @Title :
     * @Description: 删除退货明细
     */
    public void deleteDtl() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String dtlId = CommonUtils.checkNull(request.getParamValue("dtlId"));//明细id
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));//
            TtPartDlrReturnDtlPO spo = new TtPartDlrReturnDtlPO();
            spo.setDtlId(CommonUtils.parseLong(dtlId));
            dao.delete(spo);
            act.setOutData("returnId", returnId);
            act.setOutData("success", "删除成功!");
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.DELETE_FAILURE_CODE, "销售退货明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-9-9
     * @Title :
     * @Description: 销售退货打印
     */
    public void opPrintHtml() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {

            String returnId = CommonUtils.checkNull(request
                    .getParamValue("returnId"));

            Map<String, Object> map = dao.getPartDlrReturnMainInfo(returnId);

            List<Map<String, Object>> detailList = dao.getPartDlrReturnDtl(returnId);

            List allList = new ArrayList();

            for (int i = 0; i < detailList.size(); ) {
                List subList = detailList.subList(i,
                        i + PRINT_SIZE > detailList.size() ? detailList.size()
                                : i + PRINT_SIZE);
                i = i + PRINT_SIZE;
                allList.add(subList);
            }

            map.put("curDate", DateUtil.getDateStr(new Date(), 1));

            int allQty = 0;
            double amount = 0;
            double famount = 0;
            for (Map<String, Object> m : detailList) {
                allQty += ((BigDecimal) m.get("RETURN_QTY")).intValue();
                amount = Arith.add(amount, ((BigDecimal) m.get("BUY_AMOUNT"))
                        .doubleValue());
            }

            BigDecimal b = new BigDecimal(amount);
            famount = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            DecimalFormat df = new DecimalFormat("#.00");
            String samonut = df.format(famount);
            map.put("allQty", allQty);
            map.put("amount", samonut);
            dataMap.put("mainMap", map);
            dataMap.put("detailList", detailList);
            act.setOutData("allList", allList);
            act.setOutData("dataMap", dataMap);
            act.setForword(PART_DLRRETURN_PRINT_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "销售退货单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-25
     * @Title :
     * @Description: 下载退货申请
     */
    public void exportReturnApplyExcel() {

        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        int flag = 0;//默认是车厂,0表示车厂,1表示供应中心,2表示一般服务商
        if (logonUser.getDealerId() != null) {
            flag = 1;
            TmDealerPO tmDealerPO = new TmDealerPO();
            tmDealerPO.setDealerId(CommonUtils.parseLong(logonUser.getDealerId()));
            tmDealerPO = (TmDealerPO) dao.select(tmDealerPO).get(0);
            if (tmDealerPO.getPdealerType().intValue() != Constant.PART_SALE_PRICE_DEALER_TYPE_01.intValue()) {//如果不是供应中心
                flag = 2;
            }
        }
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[10];
            head[0] = "退货单号";
            head[1] = "退货单位";
            head[2] = "制单单位";
            head[3] = "制单人";
            head[4] = "制单日期";
            head[5] = "退货原因";
            head[6] = "状态";
            head[7] = "提交日期";
            List<Map<String, Object>> list = dao.getReturnApplyInfo(request, flag, logonUser);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[8];
                        detail[0] = CommonUtils.checkNull(map.get("RETURN_CODE"));
                        detail[1] = CommonUtils.checkNull(map
                                .get("DEALER_NAME"));
                        detail[2] = CommonUtils
                                .checkNull(map.get("CREATE_DEALER"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("CREATE_NAME"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("CREATE_DATE"));
                        detail[5] = CommonUtils.checkNull(map.get("REMARK"));
                        detail[6] = CommonUtils.checkNull(map.get("CODE_DESC"));
                        detail[7] = CommonUtils.checkNull(map.get("APPLY_DATE"));
                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1);
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
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
                        "文件下载错误");
            }
            String creator = request.getParamValue("creator");//当前用户id
            act.setOutData("createOrg", creator);
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_RETURN_APPLY_INIT_URL);
        }
    }


    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "销售退货申请.xls";
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
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title :
     * @Description: 查询配件销售退货申请
     */
    public void queryPartDlrReturnBackInfo() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String returnCode = CommonUtils.checkNull(request.getParamValue("RETURN_CODE"));//退货单号
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//退货单位
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间
            int flag = 0;//默认是车厂,0表示车厂,1表示供应中心,2表示一般服务商
            if (logonUser.getDealerId() != null) {//如果不是车厂
                flag = 1;
                TmDealerPO tmDealerPO = new TmDealerPO();
                tmDealerPO.setDealerId(CommonUtils.parseLong(logonUser.getDealerId()));
                tmDealerPO = (TmDealerPO) dao.select(tmDealerPO).get(0);
                if (tmDealerPO.getPdealerType().intValue() != Constant.PART_SALE_PRICE_DEALER_TYPE_01.intValue()) {//如果不是供应中心
                    flag = 2;
                }
            }
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartDlrReturnBackList(returnCode, dealerName, startDate, endDate, flag, logonUser, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }
}
