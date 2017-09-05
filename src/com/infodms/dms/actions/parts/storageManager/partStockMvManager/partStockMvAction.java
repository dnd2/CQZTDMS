package com.infodms.dms.actions.parts.storageManager.partStockMvManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.tag.ExcelUtil;
import com.infodms.dms.dao.parts.storageManager.partStockMvManager.partStockMvDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartLoactionDefinePO;
import com.infodms.dms.po.TtPartRecordPO;
import com.infodms.dms.po.TtPartStockChgDtlPO;
import com.infodms.dms.po.TtPartStockChgMianPO;
import com.infodms.dms.po.TtPartStockChgRecordPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * <p>ClassName: partStockMvAction</p>
 * <p>Description: 车厂配件调拨移库</p>
 * <p>Author: MEpaper</p>
 * <p>Date: 2017年8月17日</p>
 */
@SuppressWarnings("unchecked")
public class partStockMvAction extends BaseImport {
    public Logger logger = Logger.getLogger(partStockMvAction.class);
    private static final partStockMvDao dao = partStockMvDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();

    //配件移库、调拨URL
    private static final String PART_MV_QUERY = "/jsp/parts/storageManager/partStockMvManager/partStockMvQueryMain.jsp";//查询主页面
    private static final String PART_MV_MAIN = "/jsp/parts/storageManager/partStockMvManager/partStockMvMain.jsp";//申请主页面
    private static final String PART_MV_VIEW = "/jsp/parts/storageManager/partStockMvManager/partStockMvView.jsp";//查看页面
    private static final String PART_MV_CHECK_MAIN = "/jsp/parts/storageManager/partStockMvManager/partStockMvCheckMain.jsp";//审核主页面
    private static final String PART_MV_OUTSTOCK_MAIN = "/jsp/parts/storageManager/partStockMvManager/partStockMvOutStockMain.jsp";//出库主页面
    private static final String PART_MV_INSTOCK_MAIN = "/jsp/parts/storageManager/partStockMvManager/partStockInStockMain.jsp";//入库主页面
    private static final String PART_MV_ADD = "/jsp/parts/storageManager/partStockMvManager/partStockMvAdd.jsp";///配件移库单申请新增页面
    private static final String PART_MV_MOD = "/jsp/parts/storageManager/partStockMvManager/partStockMvMod.jsp";///配件移库单申请修改页面
    private static final String PART_MV_CHECK = "/jsp/parts/storageManager/partStockMvManager/partStockMvCheck.jsp";///配件移库单审核页面
    private static final String PART_MV_OUTSTOCK = "/jsp/parts/storageManager/partStockMvManager/partStockMvOutStock.jsp";///配件移库单出库主页面
    private static final String PART_MV_INSTOCK = "/jsp/parts/storageManager/partStockMvManager/partStockMvInStock.jsp";///配件移库单入库主页面
    private static final String PART_MV_PRINT = "/jsp/parts/storageManager/partStockMvManager/partStockMvPrint.jsp";///配件移库单打印页面


    /**
     * 申请页面初始化
     */
    public void partMvQueryInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orgId = "";//父机构（销售单位）ID
            String orgCode = "";//父机构（销售单位）编码
            String orgName = ""; //制单单位
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                orgId = Constant.OEM_ACTIVITIES;
                orgCode = Constant.ORG_ROOT_CODE;
                orgName = dao.getMainCompanyName(orgId);
            } else {
                orgId = logonUser.getDealerId();
                orgCode = logonUser.getDealerCode();
                orgName = dao.getDealerName(orgId);
            }
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.ORG_ID = '" + orgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());

            act.setOutData("parentOrgId", orgId);
            act.setOutData("parentOrgCode", orgCode);
            act.setOutData("companyName", orgName);
            act.setOutData("WHList", WHList);
            act.setOutData("WHList2", WHList);
            act.setOutData("oemId", Constant.OEM_ACTIVITIES);

            act.setForword(PART_MV_QUERY);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件移库申请页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 申请页面初始化
     */
    public void partMvInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orgId = "";//父机构（销售单位）ID
            String orgCode = "";//父机构（销售单位）编码
            String orgName = ""; //制单单位
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                orgId = Constant.OEM_ACTIVITIES;
                orgCode = Constant.ORG_ROOT_CODE;
                orgName = dao.getMainCompanyName(orgId);
            } else {
                orgId = logonUser.getDealerId();
                orgCode = logonUser.getDealerCode();
                orgName = dao.getDealerName(orgId);
            }
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.ORG_ID = '" + orgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());

            act.setOutData("parentOrgId", orgId);
            act.setOutData("parentOrgCode", orgCode);
            act.setOutData("companyName", orgName);
            act.setOutData("WHList", WHList);
            act.setOutData("WHList2", WHList);
            act.setOutData("oemId", Constant.OEM_ACTIVITIES);

            act.setForword(PART_MV_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件移库申请页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 审核主页面
     */
    public void checkMvInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orgId = "";//父机构（销售单位）ID
            String orgCode = "";//父机构（销售单位）编码
            String orgName = ""; //制单单位
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                orgId = Constant.OEM_ACTIVITIES;
                orgCode = Constant.ORG_ROOT_CODE;
                orgName = dao.getMainCompanyName(orgId);
            } else {
                orgId = logonUser.getDealerId();
                orgCode = logonUser.getDealerCode();
                orgName = dao.getDealerName(orgId);
            }
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.ORG_ID = '" + orgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());

            act.setOutData("parentOrgId", orgId);
            act.setOutData("parentOrgCode", orgCode);
            act.setOutData("companyName", orgName);
            act.setOutData("WHList", WHList);
            act.setOutData("WHList2", WHList);
            act.setOutData("oemId", Constant.OEM_ACTIVITIES);

            act.setForword(PART_MV_CHECK_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件移库申请页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件移库查询
     */
    public void partMVSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartMvOrders(request, Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件移库查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    public void viewMVDtlInint() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
//            String chgId = CommonUtils.checkNull(request.getParamValue("chgId"));// 变更单ID
            String optionType = CommonUtils.checkNull(request.getParamValue("optionType"));// 操作类型
//            String pageType = CommonUtils.checkNull(request.getParamValue("pageType"));// 操作类型
            String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));// ID
            String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); //制单单位编码
            String orgName = CommonUtils.checkNull(request.getParamValue("orgName")); //制单单位名称

            StringBuffer sbStr = new StringBuffer();
            sbStr.append(" AND TM.ORG_ID = '" + orgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbStr.toString());
//            LaborListSummaryReportDao laborListSummaryReportDao = LaborListSummaryReportDao.getInstance();

            Map<String, Object> map = dao.queryPartMvOrders(request, Constant.PAGE_SIZE_MAX, 1).getRecords().get(0);

            act.setOutData("map", map);
            act.setOutData("orgId", orgId);
            act.setOutData("orgCode", orgCode);
            act.setOutData("orgName", orgName);
            act.setOutData("oemId", Constant.OEM_ACTIVITIES);
            act.setOutData("WHList", WHList);
            act.setOutData("WHList2", WHList);
            if ("view".equalsIgnoreCase(optionType)) {
                //查看页面
                Map<String, Object> main = dao.queryPartMvOrders(request, Constant.PAGE_SIZE_MAX, 1).getRecords().get(0);
                act.setOutData("main", main);
                act.setForword(PART_MV_VIEW);
            } else if ("modify".equalsIgnoreCase(optionType)) {
                Map<String, Object> main = dao.queryPartMvOrders(request, Constant.PAGE_SIZE_MAX, 1).getRecords().get(0);
                List<Map<String, Object>> list = dao.queryMvDeatil(request, Constant.PAGE_SIZE_MAX, 1).getRecords();
                act.setOutData("main", main);
                act.setOutData("list", list);
                act.setOutData("oemOrgId", Constant.OEM_ACTIVITIES);
                act.setForword(PART_MV_MOD);
            } else if ("check".equalsIgnoreCase(optionType)) {
                Map<String, Object> main = dao.queryPartMvOrders(request, Constant.PAGE_SIZE_MAX, 1).getRecords().get(0);
                List<Map<String, Object>> list = dao.queryMvDeatil(request, Constant.PAGE_SIZE_MAX, 1).getRecords();
                act.setOutData("main", main);
                act.setOutData("list", list);
                act.setOutData("oemOrgId", Constant.OEM_ACTIVITIES);
                act.setForword(PART_MV_CHECK);
            } else if ("outStock".equalsIgnoreCase(optionType)) {
                Map<String, Object> main = dao.queryPartMvOrders(request, Constant.PAGE_SIZE_MAX, 1).getRecords().get(0);
                List<Map<String, Object>> list = dao.queryMvDeatil(request, Constant.PAGE_SIZE_MAX, 1).getRecords();
                act.setOutData("main", main);
                act.setOutData("list", list);
                act.setOutData("oemOrgId", Constant.OEM_ACTIVITIES);
                act.setForword(PART_MV_OUTSTOCK);
            } else if ("inStock".equalsIgnoreCase(optionType)) {
                Map<String, Object> main = dao.queryPartMvOrders(request, Constant.PAGE_SIZE_MAX, 1).getRecords().get(0);
                List<Map<String, Object>> list = dao.queryInStockDetial(request);
                act.setOutData("main", main);
                act.setOutData("list", list);
                act.setOutData("oemOrgId", Constant.OEM_ACTIVITIES);
                act.setForword(PART_MV_INSTOCK);
            } else {
                Map<String, Object> main = dao.queryPartMvOrders(request, Constant.PAGE_SIZE_MAX, 1).getRecords().get(0);
                List<Map<String, Object>> list = dao.queryMvPartsPageList(request, Constant.PAGE_SIZE_MAX, 1).getRecords();
                Long partSum = 0l;
                for (int i = 0; i < list.size(); i++) {
                    partSum += Long.valueOf(list.get(i).get("APPLY_QTY").toString());
                }
                act.setOutData("main", main);
                act.setOutData("list", list);

                act.setOutData("partSum", partSum);
                act.setOutData("Date", CommonUtils.getFullDate());
                act.setOutData("listSize", (10 - list.size()));
                //打印页面
                act.setForword(PART_MV_PRINT);
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 新增移库申请新增初始化
     */
    public void partMvAddInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
//        RequestWrapper request = act.getRequest();
        try {
            String orgId = "";//当前用户所属机构ID
            String orgCode = "";//当前用户所属机构编码
            String orgName = ""; //当前用户所属机构

            //取当前机构信息
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                orgId = Constant.OEM_ACTIVITIES;
                orgCode = Constant.ORG_ROOT_CODE;
                orgName = dao.getMainCompanyName(orgId);
                StringBuffer sb = new StringBuffer();
                sb.append(" AND TM.ORG_ID = '" + orgId + "' ");
            } else {
                orgId = logonUser.getDealerId();
                orgCode = logonUser.getDealerCode();
                orgName = dao.getDealerName(orgName);
            }
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.ORG_ID = '" + orgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());
            String changeCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_57);//获取制单单号

            act.setOutData("orgId", orgId);
            act.setOutData("orgCode", orgCode);
            act.setOutData("changeCode", changeCode);
            act.setOutData("userName", logonUser.getName());
            act.setOutData("orgName", orgName);
            act.setOutData("WHList", WHList);
            act.setOutData("WHList2", WHList);
            act.setOutData("toDay", CommonUtils.getDate());
            act.setOutData("oemId", Constant.OEM_ACTIVITIES);

            act.setForword(PART_MV_ADD);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "移库新增页面初始化");
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
        RequestWrapper req = act.getRequest();
        try {

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.showPartStockBase(
                    req, Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "明细查询失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 提交移库申请
     */
    public void submitMVOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        String error = "";
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            String chgId = CommonUtils.checkNull(req.getParamValue("chgId")); //移库ID
            if ("".equals(curPage)) {
                curPage = "1";
            }

            if ("".equals(error)) {
                TtPartStockChgMianPO chgMianPO = new TtPartStockChgMianPO();
                chgMianPO.setChgId(Long.valueOf(chgId));
                chgMianPO.setState(Constant.PART_MV_STATUS_01);

                TtPartStockChgMianPO chgMianPO1 = new TtPartStockChgMianPO();
                chgMianPO1.setState(Constant.PART_MV_STATUS_02);
                chgMianPO1.setSubmitBy(logonUser.getUserId());
                chgMianPO1.setSubmitDate(new Date());
                chgMianPO1.setCheckRemark("");
                dao.update(chgMianPO, chgMianPO1);
                act.setOutData("success", "success");
            } else {
                act.setOutData("erorr", error);// 返回错误信息
//                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, error);
            }

            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "提交移库信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 移库单作废
     */
    public void deleteMVorder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        String error = "";
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            String chgId = CommonUtils.checkNull(req.getParamValue("chgId")); //移库ID
            boolean qhjDbFlag = false; //切换调拨标识
            if ("".equals(curPage)) {
                curPage = "1";
            }

            TtPartStockChgMianPO chgMianPO = new TtPartStockChgMianPO();
            chgMianPO.setChgId(Long.valueOf(chgId));
            chgMianPO.setState(Constant.PART_MV_STATUS_01);
            TtPartStockChgMianPO selMianPO = (TtPartStockChgMianPO) dao.select(chgMianPO).get(0);

            String whId = selMianPO.getWhId() + "";
//            String toWhId = selMianPO.getTowhId() + "";
            String flag = selMianPO.getFlag() + "";

            //切换件调拨入库旧件库时为切换件调拨标识
            if ("2014092298805416".equals(whId) && "1".equals(flag)) {
                qhjDbFlag = true;
            }

            //释放锁定的资源 start
            if (!qhjDbFlag) {
                List<Object> ins2 = new LinkedList<Object>();
                ins2.add(0, chgId);
                ins2.add(1, Constant.PART_CODE_RELATION_57);
                ins2.add(2, 0);// 1:占用 0：释放占用
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins2, null);
            } else {
                //切换件旧件资源释放逻辑
                TtPartStockChgDtlPO selPo = new TtPartStockChgDtlPO();
                selPo.setChgId(Long.valueOf(chgId));
                List<TtPartStockChgDtlPO> dataPos = dao.select(selPo);
                for (TtPartStockChgDtlPO dataPo : dataPos) {
                    dao.updateASdMvState("1", null, "1", dataPo.getPartOldcode(), "3", dataPo.getApplyQty() + "", chgId);
                }
            }
            //end
            if ("".equals(error)) {
                //更新主表为无效
                TtPartStockChgMianPO srcMianPO = new TtPartStockChgMianPO();
                srcMianPO.setChgId(Long.valueOf(chgId));
                srcMianPO.setState(Constant.PART_MV_STATUS_01);

                TtPartStockChgMianPO upMianPo = new TtPartStockChgMianPO();
                upMianPo.setState(Constant.PART_MV_STATUS_06);

                dao.update(srcMianPO, upMianPo);

                act.setOutData("success", "success");
            } else {
                act.setOutData("erorr", error);// 返回错误信息
                throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, error);
            }

            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, error);
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 驳回申请
     */
    public void rejectMVorder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
//        String errorExist = "";
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            String chgId = CommonUtils.checkNull(req.getParamValue("chgId")); //制单ID
            String rejectRemark = CommonUtils.checkNull(req.getParamValue("rejectRemark")); //驳回原因
            if ("".equals(curPage)) {
                curPage = "1";
            }

            TtPartStockChgMianPO chgMianPO = new TtPartStockChgMianPO();
            chgMianPO.setChgId(Long.valueOf(chgId));
            chgMianPO.setState(Constant.PART_MV_STATUS_02);

            TtPartStockChgMianPO chgMianPO1 = new TtPartStockChgMianPO();
            chgMianPO1.setState(Constant.PART_MV_STATUS_01);
            chgMianPO1.setCheckRemark(rejectRemark);
            dao.update(chgMianPO, chgMianPO1);

            act.setOutData("success", "success");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "驳回移库单信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 新增保存
     */
    public void saveMVOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        String error = "";
        try {
            String chgCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_57);//移库单号
            String orgId = request.getParamValue("orgId");//制单单位id
            String orgCode = request.getParamValue("orgCode");//单位编码
            String orgName = request.getParamValue("orgName");//单位名称
            String remark = request.getParamValue("remark");//移库原因
            String whId = request.getParamValue("whId");//移出库房
            String toWhId = request.getParamValue("toWhId");//移入库房
            String flag = request.getParamValue("flag");//正常 0:配件库存，1：售后切换件库存
            String[] partIdArr = req.getParamValues("cb");
            boolean qhjDbFlag = false; //切换调拨标识

            //切换件调拨入库旧件库时为切换件调拨标识
            if ("2014092298805416".equals(whId) && "1".equals(flag)) {
                qhjDbFlag = true;
            }
            List<Map<String, Object>> partList = null;
            List<TtPartStockChgDtlPO> dtlPOArrayList = new ArrayList<TtPartStockChgDtlPO>();
            Long chgId = Long.parseLong(SequenceManager.getSequence(""));
            //库存校验
            if (null != partIdArr) {
                TtPartStockChgDtlPO chgDtlPO = null;
                for (int i = 0; i < partIdArr.length; i++) {
                    String partIdLocId = partIdArr[i];
                    String partId = partIdLocId.substring(0, partIdLocId.indexOf("_RNUM"));
                    String partOldcode = CommonUtils.checkNull(req.getParamValue("PART_OLDCODE" + partIdLocId));   //配件编码
                    String partCname = CommonUtils.checkNull(req.getParamValue("PART_CNAME" + partIdLocId));   //配件名称
                    String partCode = CommonUtils.checkNull(req.getParamValue("PART_CODE" + partIdLocId));   //配件件号
                    
                    String loc = CommonUtils.checkNull(req.getParamValue("LOC_" + partIdLocId));  
                    String[] locArr = loc.split(",");
                    String locId = locArr[1];   //配件货位ID
                    String batchNo = locArr[4];   //批次号
                    String venderId = CommonUtils.checkNull(req.getParamValue("venderId_" + partIdLocId)); //供应商id
                    venderId = Constant.PART_RECORD_VENDER_ID;
                    
                    Integer applyQty = Integer.valueOf(CommonUtils.checkNull(req.getParamValue("APPLY_QTY" + partIdLocId)));   //申请移库数量
                    String lineRemark = CommonUtils.checkNull(req.getParamValue("REMARK" + partIdLocId));   //行备注

                    partList = dao.getPartStockInfos(partId, orgId, whId, locId, batchNo);
                    Integer normalQty = Integer.parseInt(partList.get(0).get("NORMAL_QTY").toString());//可用库存
                    if (normalQty < applyQty) {
                        error += "第" + (i + 1) + "行配件：" + partOldcode + "移库数量不能大于可用库存数：" + normalQty + " !<br/>";
                    } else {
                        chgDtlPO = new TtPartStockChgDtlPO();
                        chgDtlPO.setDtlId(Long.valueOf(SequenceManager.getSequence("")));
                        chgDtlPO.setChgId(chgId);
                        chgDtlPO.setPartId(Long.valueOf(partId));
                        chgDtlPO.setPartCode(partCode);
                        chgDtlPO.setPartOldcode(partOldcode);
                        chgDtlPO.setPartCname(partCname);
                        chgDtlPO.setUnit("件");
                        chgDtlPO.setApplyQty(Long.valueOf(applyQty));
                        chgDtlPO.setRemark(lineRemark);
                        chgDtlPO.setCreateBy(logonUser.getUserId());
                        chgDtlPO.setCreateDate(new Date());
                        chgDtlPO.setVer(1);
                        chgDtlPO.setWhId(Long.valueOf(whId));
                        chgDtlPO.setLocId(Long.valueOf(locId));
                        chgDtlPO.setBatId(Long.parseLong(batchNo));
                        chgDtlPO.setVenderId(Long.parseLong(venderId));
                        dtlPOArrayList.add(chgDtlPO);
                    }
                }
            }

            if ("".equals(error)) {
                //保存主表
                TtPartStockChgMianPO inserRMPo = new TtPartStockChgMianPO();
                inserRMPo.setChgId(Long.valueOf(chgId));
                inserRMPo.setChgCode(chgCode);
                inserRMPo.setOrgId(Long.valueOf(orgId));
                inserRMPo.setOrgCode(orgCode);
                inserRMPo.setOrgName(orgName);
                inserRMPo.setRemark(remark);
                inserRMPo.setCreateDate(new Date());
                inserRMPo.setCreateBy(logonUser.getUserId());
                inserRMPo.setVer(1);
                inserRMPo.setState(Constant.PART_MV_STATUS_01);
                inserRMPo.setWhId(Long.valueOf(whId));
                inserRMPo.setTowhId(Long.valueOf(toWhId));
                inserRMPo.setFlag(Integer.valueOf(flag)); //是否装车切换件标识
                dao.insert(inserRMPo);
                
                //保存明细
                dao.insert(dtlPOArrayList);

                //资源锁定逻辑 start
                if (!qhjDbFlag) {
                    List<Object> ins = new LinkedList<Object>();
                    ins.add(0, chgId);
                    ins.add(1, Constant.PART_CODE_RELATION_57);
                    ins.add(2, 1);// 1:占用 0：释放占用
                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
                } else {
                    for (TtPartStockChgDtlPO dataPo : dtlPOArrayList) {
                        //切换件旧件资源锁定逻辑
                        dao.updateASdMvState("3", chgId + "", "1", dataPo.getPartOldcode(), "1", dataPo.getApplyQty() + "", null);
                    }
                }
                //end
                act.setOutData("success", "保存成功!");
            } else {
                act.setOutData("error", error);// 返回错误信息
                throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, error);
            }
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存新增移库单信息出错，请联系管理员");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("errorExist", "保存失败!");
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-11
     * @Title : 修改订单
     */
    public void modSaveMvOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        String error = "";
        try {
            String chgCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_57);//移库单号
            String orgId = request.getParamValue("orgId");//制单单位id
            String orgCode = request.getParamValue("orgCode");//单位编码
            String orgName = request.getParamValue("orgName");//单位名称
            String remark = request.getParamValue("remark");//移库原因
            String whId = request.getParamValue("whId");//移出库房
            String toWhId = request.getParamValue("toWhId");//移入库房
            Long chgId = Long.parseLong(request.getParamValue("CHG_ID"));//移库ID
            String flag = request.getParamValue("flag");//装车切换件标识
            String[] partIdArr = req.getParamValues("cb");

            //先释放先前的移库单上数据start
            TtPartStockChgMianPO checkMainPO = new TtPartStockChgMianPO();
            checkMainPO.setChgId(chgId);
            checkMainPO = (TtPartStockChgMianPO) dao.select(checkMainPO).get(0);

            //释放锁定的资源 start
            if ("0".equals(checkMainPO.getFlag() + "")) {
                List<Object> ins2 = new LinkedList<Object>();
                ins2.add(0, chgId);
                ins2.add(1, Constant.PART_CODE_RELATION_57);
                ins2.add(2, 0);// 1:占用 0：释放占用
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins2, null);
            } else {
                //切换件旧件资源释放逻辑
                TtPartStockChgDtlPO selPo = new TtPartStockChgDtlPO();
                selPo.setChgId(Long.valueOf(chgId));
                List<TtPartStockChgDtlPO> dataPos = dao.select(selPo);
                for (TtPartStockChgDtlPO dataPo : dataPos) {
                    dao.updateASdMvState("1", null, "1", dataPo.getPartOldcode(), "3", dataPo.getApplyQty() + "", chgId + "");
                }
            }
            //释放end
            boolean qhjDbFlag = false; //切换调拨标识

            //开始处理修改后数据
            //切换件调拨入库旧件库时为切换件调拨标识
            if ("2014092298805416".equals(whId) && "1".equals(flag)) {
                qhjDbFlag = true;
            }

            List<Map<String, Object>> partList = null;
            List<TtPartStockChgDtlPO> dtlPOArrayList = new ArrayList<TtPartStockChgDtlPO>();
            //库存校验
            if (null != partIdArr) {
                TtPartStockChgDtlPO chgDtlPO = null;
                for (int i = 0; i < partIdArr.length; i++) {
                    String partIdLocId = partIdArr[i];
                    String partId = partIdLocId.substring(0, partIdLocId.indexOf("_RNUM"));
                    String partOldcode = CommonUtils.checkNull(req.getParamValue("PART_OLDCODE" + partIdLocId));   //配件编码
                    String partCname = CommonUtils.checkNull(req.getParamValue("PART_CNAME" + partIdLocId));   //配件名称
                    String partCode = CommonUtils.checkNull(req.getParamValue("PART_CODE" + partIdLocId));   //配件件号
                    
                    String loc = CommonUtils.checkNull(req.getParamValue("LOC_" + partIdLocId));  
                    String[] locArr = loc.split(",");
                    String locId = locArr[1];   //配件货位ID
                    String batchNo = locArr[4];   //批次号
                    String venderId = CommonUtils.checkNull(req.getParamValue("venderId_" + partIdLocId)); //供应商id
                    venderId = Constant.PART_RECORD_VENDER_ID;
                    
                    Integer applyQty = Integer.valueOf(CommonUtils.checkNull(req.getParamValue("APPLY_QTY" + partIdLocId)));   //申请移库数量
                    String lineRemark = CommonUtils.checkNull(req.getParamValue("REMARK" + partIdLocId));   //行备注

                    partList = dao.getPartStockInfos(partId, orgId, whId, locId, batchNo);
                    Integer normalQty = Integer.parseInt(partList.get(0).get("NORMAL_QTY").toString());//可用库存
                    if (normalQty < applyQty) {
                        error += "第" + (i + 1) + "行配件：" + partOldcode + "移库数量不能大于可用库存数：" + normalQty + " <br/>!";
                    } else {
                        chgDtlPO = new TtPartStockChgDtlPO();
                        chgDtlPO.setDtlId(Long.valueOf(SequenceManager.getSequence("")));
                        chgDtlPO.setChgId(chgId);
                        chgDtlPO.setPartId(Long.valueOf(partId));
                        chgDtlPO.setPartCode(partCode);
                        chgDtlPO.setPartOldcode(partOldcode);
                        chgDtlPO.setPartCname(partCname);
                        chgDtlPO.setUnit("件");
                        chgDtlPO.setApplyQty(Long.valueOf(applyQty));
                        chgDtlPO.setRemark(lineRemark);
                        chgDtlPO.setCreateBy(logonUser.getUserId());
                        chgDtlPO.setCreateDate(new Date());
                        chgDtlPO.setVer(1);
                        chgDtlPO.setWhId(Long.valueOf(whId));
                        chgDtlPO.setLocId(Long.valueOf(locId));
                        chgDtlPO.setBatId(Long.parseLong(batchNo));
                        chgDtlPO.setVenderId(Long.parseLong(venderId));
                        dtlPOArrayList.add(chgDtlPO);
                    }
                }
            }

            if ("".equals(error)) {
                //更新主表
                TtPartStockChgMianPO srcMianPO = new TtPartStockChgMianPO();
                srcMianPO.setChgId(Long.valueOf(chgId));

                TtPartStockChgMianPO updateMianPO = new TtPartStockChgMianPO();
                updateMianPO.setChgCode(chgCode);
                updateMianPO.setOrgId(Long.valueOf(orgId));
                updateMianPO.setOrgCode(orgCode);
                updateMianPO.setOrgName(orgName);
                updateMianPO.setRemark(remark);
                updateMianPO.setUpdateDate(new Date());
                updateMianPO.setUpdateBy(logonUser.getUserId());
                updateMianPO.setVer(1);
                updateMianPO.setState(Constant.PART_MV_STATUS_01);
                updateMianPO.setWhId(Long.valueOf(whId));
                updateMianPO.setTowhId(Long.valueOf(toWhId));

                dao.update(srcMianPO, updateMianPO);
                
                //先清空明细
                TtPartStockChgDtlPO chgDtlPO = new TtPartStockChgDtlPO();
                chgDtlPO.setChgId(Long.valueOf(chgId));
                dao.delete(chgDtlPO);
                //保存明细
                dao.insert(dtlPOArrayList);

                //重新资源锁定逻辑 start
                if (!qhjDbFlag) {
                    List<Object> ins = new LinkedList<Object>();
                    ins.add(0, chgId);
                    ins.add(1, Constant.PART_CODE_RELATION_57);
                    ins.add(2, 1);// 1:占用 0：释放占用
                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
                } else {
                    for (TtPartStockChgDtlPO dataPo : dtlPOArrayList) {
                        //切换件旧件资源锁定逻辑
                        dao.updateASdMvState("3", chgId + "", "1", dataPo.getPartOldcode(), "1", dataPo.getApplyQty() + "", null);
                    }
                }
                //资源锁定end
                act.setOutData("success", "success");
            } else {
                throw new BizException(act, ErrorCodeConstant.UPDATE_FAILURE_CODE, error);
            }
            //修改后处理end
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, error);
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("errorExist", "修改失败!");
        }
    }


    /**
     * 配件移库明细查询
     */
    public void partMVDetailQuery() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryMvPartsPageList(
                    request, Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件移库明细查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


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
                    .getParamValue("whId")); // 截止时间
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
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
            String[] head = new String[9];
            head[0] = "序号";
            head[1] = "制单单号";
            head[2] = "类型";
            head[3] = "制单单位";
            head[4] = "仓库";
            head[5] = "制单人";
            head[6] = "制单日期";
            head[7] = "制单金额(元)";
            head[8] = "状态";
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
                       /* if (orderType1 == oderType) {
                            detail[2] = "移库";
                        } else if (orderType2 == oderType) {
                            detail[2] = "领用";
                        }*/
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
                        int orderState = Integer.parseInt(CommonUtils.checkNull(map.get("STATE")));
                       /* if (orderState1 == orderState) {
                            detail[8] = "已保存";
                        } else if (orderState2 == orderState) {
                            detail[8] = "已提交";
                        } else if (orderState3 == orderState) {
                            detail[8] = "已完成";
                        } else if (orderState4 == orderState) {
                            detail[8] = "已通过";
                        } else if (orderState5 == orderState) {
                            detail[8] = "已驳回";
                        } else if (orderState6 == orderState) {
                            detail[8] = "已作废";
                        }*/

                        list1.add(detail);
                    }
                }
            }
            String fileName = "";
           /* if ((orderType1 + "").equals(orderType + "")) {
                fileName = "配件移库单信息";
            } else {
                fileName = "配件领用单信息";
            }*/

            ExcelUtil.toExceUtil(ActionContext.getContext().getResponse(),
                    request, head, list1, fileName);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件移库/领用单");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * 审核移库申请
     */
    public void checkMvOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        String error = "";
        boolean qhjDbFlag = false; //切换调拨标识
        try {
            String checkFlag = CommonUtils.checkNull(req.getParamValue("checkFlag")); //审核标识
            String checkRemark = CommonUtils.checkNull(req.getParamValue("checkRemark")); //移库ID
            Long chgId = Long.parseLong(request.getParamValue("CHG_ID"));//移库ID

            String orgId = request.getParamValue("orgId");//制单单位id
            String whId = request.getParamValue("whId");//移出库房
            String toWhId = request.getParamValue("toWhId");//移入库房
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            //先做资源释放 start
            TtPartStockChgMianPO chgMianPO = new TtPartStockChgMianPO();
            chgMianPO.setChgId(chgId);
            chgMianPO = (TtPartStockChgMianPO) dao.select(chgMianPO).get(0);

            List<Map<String, Object>> partList = null;
            //释放锁定的资源 start
            if ("0".equals(chgMianPO.getFlag() + "")) {
                List ins2 = new LinkedList<Object>();
                ins2.add(0, chgId);
                ins2.add(1, Constant.PART_CODE_RELATION_57);
                ins2.add(2, 0);// 1:占用 0：释放占用
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins2, null);
            } else {
                //切换件旧件资源释放逻辑
                TtPartStockChgDtlPO selPo = new TtPartStockChgDtlPO();
                selPo.setChgId(Long.valueOf(chgId));
                List<TtPartStockChgDtlPO> dataPos = dao.select(selPo);
                for (TtPartStockChgDtlPO dataPo : dataPos) {
                    dao.updateASdMvState("1", null, "1", dataPo.getPartOldcode(), "3", dataPo.getApplyQty() + "", chgId + "");
                }
            }
            //end
            //先做资源释放end
            //重新开始资源占用start
            if ("2014092298805416".equals(whId) && "1".equals(chgMianPO.getFlag() + "")) {
                qhjDbFlag = true;
            }
            String[] partIdArr = req.getParamValues("cb");
            List<TtPartStockChgDtlPO> dtlPOArrayList = new ArrayList<TtPartStockChgDtlPO>();
            //库存校验
            if (null != partIdArr) {
                TtPartStockChgDtlPO chgDtlPO = null;
                for (int i = 0; i < partIdArr.length; i++) {
                    String partIdLocId = partIdArr[i];
                    String partId = partIdLocId.split("_")[0];
                    String partOldcode = CommonUtils.checkNull(req.getParamValue("PART_OLDCODE" + partIdLocId));   //配件编码
                    String locId = CommonUtils.checkNull(req.getParamValue("LOC_ID" + partIdLocId));   //配件货位ID
                    String batchNo = CommonUtils.checkNull(req.getParamValue("BATCH_NO" + partIdLocId));   //配件货位ID
                    Integer checkQty = Integer.valueOf(CommonUtils.checkNull(req.getParamValue("CHECK_QTY" + partIdLocId)));   //审核移库数量

                    partList = dao.getPartStockInfos(partId, orgId, whId, locId, batchNo);
                    Integer normalQty = Integer.parseInt(partList.get(0).get("NORMAL_QTY").toString());//可用库存
                    if (normalQty < checkQty) {
                        error += "第" + (i + 1) + "行配件：" + partOldcode + "审核数量不能大于可用库存数：" + normalQty + " <br/>!";
                    } else {
                        chgDtlPO = new TtPartStockChgDtlPO();
                        chgDtlPO.setChgId(chgId);
                        chgDtlPO.setPartId(Long.valueOf(partId));
                        chgDtlPO.setLocId(Long.parseLong(locId));
                        chgDtlPO.setCheckQty(Long.valueOf(checkQty));
                        dtlPOArrayList.add(chgDtlPO);
                    }
                }
            }

            if ("".equals(error) && dtlPOArrayList.size() > 0) {
                //更新审核数量
                if ("1".equals(checkFlag)) {
                    TtPartStockChgDtlPO srcDtlPO = null;
                    TtPartStockChgDtlPO updateDtlPO = null;
                    for (TtPartStockChgDtlPO dataDtlPO : dtlPOArrayList) {
                        srcDtlPO = new TtPartStockChgDtlPO();
                        updateDtlPO = new TtPartStockChgDtlPO();

                        srcDtlPO.setChgId(Long.valueOf(chgId));
                        srcDtlPO.setPartId(dataDtlPO.getPartId());
                        srcDtlPO.setLocId(dataDtlPO.getLocId());
                        updateDtlPO.setCheckQty(dataDtlPO.getCheckQty());

                        dao.update(srcDtlPO, updateDtlPO);
                    }
                }
                //更新审核状态
                TtPartStockChgMianPO selPo = new TtPartStockChgMianPO();
                TtPartStockChgMianPO updPo = new TtPartStockChgMianPO();

                selPo.setChgId(chgId);
                selPo.setState(Constant.PART_MV_STATUS_02);
                //1通过 2驳回
                if ("1".equals(checkFlag)) {
                    updPo.setState(Constant.PART_MV_STATUS_03);
                } else {
                    updPo.setState(Constant.PART_MV_STATUS_01);
                }
                updPo.setCheckRemark(checkRemark);

                dao.update(selPo, updPo);

                //重新资源锁定逻辑 start
                if (!qhjDbFlag) {
                    List<Object> ins = new LinkedList<Object>();
                    ins.add(0, chgId);
                    ins.add(1, Constant.PART_CODE_RELATION_58);
                    ins.add(2, 1);// 1:占用 0：释放占用
                    //dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
                } else {
                    //切换件旧件资源占用逻辑
                    TtPartStockChgDtlPO selDtlPo = new TtPartStockChgDtlPO();
                    selDtlPo.setChgId(Long.valueOf(chgId));
                    List<TtPartStockChgDtlPO> dataPos = dao.select(selDtlPo);
                    for (TtPartStockChgDtlPO dataPo : dataPos) {
                       dao.updateASdMvState("3", chgId + "", "1", dataPo.getPartOldcode(), "1", dataPo.getCheckQty() + "", null);
                    }
                }
                //资源占用end
                act.setOutData("success", "success");
                act.setOutData("curPage", curPage);
            } else {
                throw new BizException(act, ErrorCodeConstant.UPDATE_FAILURE_CODE, error);
            }

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, error);
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 出库主页面
     */
    public void outStockMvInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orgId = "";//父机构（销售单位）ID
            String orgCode = "";//父机构（销售单位）编码
            String orgName = ""; //制单单位
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                orgId = Constant.OEM_ACTIVITIES;
                orgCode = Constant.ORG_ROOT_CODE;
                orgName = dao.getMainCompanyName(orgId);
            } else {
                orgId = logonUser.getDealerId();
                orgCode = logonUser.getDealerCode();
                orgName = dao.getDealerName(orgId);
            }
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.ORG_ID = '" + orgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());

            act.setOutData("parentOrgId", orgId);
            act.setOutData("parentOrgCode", orgCode);
            act.setOutData("companyName", orgName);
            act.setOutData("WHList", WHList);
            act.setOutData("WHList2", WHList);
            act.setOutData("oemId", Constant.OEM_ACTIVITIES);

            act.setForword(PART_MV_OUTSTOCK_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件移库出库页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 移库出库
     */
    public void outStockMvOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        String error = "";
        boolean qhjDbFlag = false; //装车件移库标识
        try {
            String orgId = request.getParamValue("orgId");//制单单位id
            String orgCode = request.getParamValue("orgCode");//单位编码
            String orgName = request.getParamValue("orgName");//单位名称
            String whId = request.getParamValue("whId");//移出库房
            String whCode = request.getParamValue("whCode");//移出库房编码
            String whName = request.getParamValue("whName");//移出库房名称
            String toWhId = request.getParamValue("toWhId");//移入库房
            Long chgId = Long.parseLong(request.getParamValue("CHG_ID"));//移库ID
            String chgCode = CommonUtils.checkNull(request.getParamValue("CHG_CODE"));//移库编码
            String[] partIdArr = req.getParamValues("cb");

            TtPartStockChgMianPO chgMianPO = new TtPartStockChgMianPO();
            chgMianPO.setChgId(chgId);
            chgMianPO = (TtPartStockChgMianPO) dao.select(chgMianPO).get(0);

            //装车件移库标识
            if ("2014092298805416".equals(whId) && "1".equals(chgMianPO.getFlag() + "")) {
                qhjDbFlag = true;
            }

            List<Map<String, Object>> partList = null;
            List<TtPartStockChgDtlPO> dtlPOArrayList = new ArrayList<TtPartStockChgDtlPO>();

            List<TtPartRecordPO> recordPOs = new ArrayList<TtPartRecordPO>();
            List<TtPartStockChgRecordPO> chgRecordPOs = new ArrayList<TtPartStockChgRecordPO>();
            //库存校验
            if (null != partIdArr) {
                TtPartStockChgDtlPO chgDtlPO = null;
                TtPartRecordPO recordPO = null;
                TtPartStockChgRecordPO chgRecordPO = null;
                for (int i = 0; i < partIdArr.length; i++) {
                    String partIdLocId = partIdArr[i];
                    String partId = partIdLocId.split("_")[0];
                    String partOldcode = CommonUtils.checkNull(req.getParamValue("PART_OLDCODE" + partIdLocId));   //配件编码
                    String partCname = CommonUtils.checkNull(req.getParamValue("PART_CNAME" + partIdLocId));   //配件名称
                    String partCode = CommonUtils.checkNull(req.getParamValue("PART_CODE" + partIdLocId));   //配件件号
                    String locId = CommonUtils.checkNull(req.getParamValue("LOC_ID" + partIdLocId));   //配件货位ID
                    String batchNo = CommonUtils.checkNull(req.getParamValue("BATCH_NO" + partIdLocId));   //批次号
                    Integer outQty = Integer.valueOf(CommonUtils.checkNull(req.getParamValue("OUT_QTY" + partIdLocId)));   //申请移库数量

                    partList = dao.getPartStockInfos(partId, orgId, whId, locId, batchNo);
                    Integer itemQty = Integer.parseInt(partList.get(0).get("ITEM_QTY").toString());//可用库存
                    Integer bookedQty = Integer.parseInt(partList.get(0).get("BOOKED_QTY").toString());//可用库存
                    String locCode = CommonUtils.checkNull(partList.get(0).get("LOC_CODE").toString());//货位编码
//                    String venderId = CommonUtils.checkNull(partList.get(0).get("STOCK_VENDER_ID").toString()); // 配件供应商id
//                    venderId = Constant.PART_RECORD_VENDER_ID;
                    if (itemQty < outQty || bookedQty < outQty) {
                        error += "第" + (i + 1) + "行配件：" + partOldcode + "出库数量不能大于库存数：" + itemQty + " !";
                    } else {
                        chgDtlPO = new TtPartStockChgDtlPO();
                        recordPO = new TtPartRecordPO();
                        chgRecordPO = new TtPartStockChgRecordPO();
//
                        Long recId = Long.parseLong(SequenceManager.getSequence(""));
                        recordPO.setRecordId(recId);
                        recordPO.setOrderCode(chgCode); // 业务编码
                        recordPO.setAddFlag(2);//出库标识
                        recordPO.setPartId(Long.valueOf(partId));
                        recordPO.setPartCode(partCode);
                        recordPO.setPartOldcode(partOldcode);
                        recordPO.setPartName(partCname);
                        recordPO.setPartBatch(batchNo);
                        recordPO.setVenderId(Long.parseLong(Constant.PART_RECORD_VENDER_ID));
                        recordPO.setPartNum(Long.valueOf(outQty));//出库数量
                        recordPO.setConfigId(Long.parseLong(Constant.PART_CODE_RELATION_57 + ""));
                        recordPO.setOrderId(chgId);//移库单ID
                        recordPO.setOrgId(Long.parseLong(orgId));
                        recordPO.setOrgCode(orgCode);
                        recordPO.setOrgName(orgName);
                        recordPO.setWhId(Long.parseLong(whId));
                        recordPO.setWhCode(whCode);
                        recordPO.setWhName(whName);
                        
                        recordPO.setLocId(Long.valueOf(locId));
                        recordPO.setLocCode(locCode);
                        recordPO.setLocName(locCode);
                        recordPO.setOptDate(new Date());
                        recordPO.setCreateDate(new Date());
                        recordPO.setPersonId(logonUser.getUserId());
                        recordPO.setPersonName(logonUser.getName());
                        recordPO.setPartState(1);//配件状态
                        recordPOs.add(recordPO);

                        chgRecordPO.setRecId(recId);
                        chgRecordPO.setConfigId(Constant.PART_CODE_RELATION_57);//移库出库
                        chgRecordPO.setPartId(Long.valueOf(partId));
                        chgRecordPO.setPartCname(partCname);
                        chgRecordPO.setPartOldcode(partOldcode);
                        chgRecordPO.setPartCode(partCode);
                        chgRecordPO.setPartNum(Long.valueOf(outQty));
                        chgRecordPO.setLocId(Long.valueOf(locId));
                        chgRecordPO.setLocCode(locCode);
                        chgRecordPO.setTopartId(Long.valueOf(partId));
                        chgRecordPO.setTopartCname(partCname);
                        chgRecordPO.setTopartCode(partCode);
                        chgRecordPO.setTopartOldcode(partOldcode);
                        chgRecordPO.setTopartNum(0l);//未入库
                        chgRecordPO.setCreateDate(new Date());
                        chgRecordPO.setCreateBy(logonUser.getUserId());
                        chgRecordPO.setOrgId(Long.valueOf(orgId));
                        chgRecordPO.setWhId(Long.valueOf(whId));
                        chgRecordPO.setTowhId(Long.valueOf(toWhId));
                        chgRecordPOs.add(chgRecordPO);
                        chgRecordPO.setRecCode(chgCode);

                        chgDtlPO.setChgId(chgId);
                        chgDtlPO.setPartId(Long.valueOf(partId));
                        chgDtlPO.setLocId(Long.parseLong(locId));
                        chgDtlPO.setOutQty(Long.valueOf(outQty));
                        dtlPOArrayList.add(chgDtlPO);
                    }
                }
            }

            if ("".equals(error) && dtlPOArrayList.size() > 0) {
                //写出入库记录
                dao.insert(recordPOs);
                //写移库出库记录
                dao.insert(chgRecordPOs);
                //更新出库明细
                TtPartStockChgDtlPO srcDtlPO = null;
                TtPartStockChgDtlPO upDateDtlPO = null;
                for (TtPartStockChgDtlPO stockChgDtlPO : dtlPOArrayList) {
                    srcDtlPO = new TtPartStockChgDtlPO();
                    upDateDtlPO = new TtPartStockChgDtlPO();

                    srcDtlPO.setChgId(Long.valueOf(chgId));
                    srcDtlPO.setPartId(stockChgDtlPO.getPartId());
                    srcDtlPO.setLocId(stockChgDtlPO.getLocId());
                    upDateDtlPO.setOutQty(stockChgDtlPO.getOutQty());
                    //装车切换件调拨入库时旧件库级联更新为已入库数量
                    if (qhjDbFlag && "2014122507332491".equals(toWhId)) {
                        upDateDtlPO.setInQty(stockChgDtlPO.getOutQty());
                    }

                    dao.update(srcDtlPO, upDateDtlPO);
                }
                //更新主表状态为已出库
                TtPartStockChgMianPO srcMianPO = new TtPartStockChgMianPO();
                srcMianPO.setChgId(Long.valueOf(chgId));
                srcMianPO.setState(Constant.PART_MV_STATUS_03);

                TtPartStockChgMianPO updateMianPO = new TtPartStockChgMianPO();
                //装车切换件调拨入库旧件库级联更新为已入库
                if (qhjDbFlag && "2014122507332491".equals(toWhId)) {
                    updateMianPO.setState(Constant.PART_MV_STATUS_05);
                } else {
                    updateMianPO.setState(Constant.PART_MV_STATUS_04);
                }

                dao.update(srcMianPO, updateMianPO);

                //调用出入库逻辑
                if (!qhjDbFlag) {
                    List<Object> ins = new LinkedList<Object>();
                    ins.add(0, chgId);
                    ins.add(1, Constant.PART_CODE_RELATION_57);
                    ins.add(2, 1);//是否有占用标识，1有0否
                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
                } else {
                    //切换件旧件出库
                    TtPartStockChgDtlPO selPo = new TtPartStockChgDtlPO();
                    selPo.setChgId(Long.valueOf(chgId));
                    List<TtPartStockChgDtlPO> dataPos = dao.select(selPo);
                    for (TtPartStockChgDtlPO dataPo : dataPos) {
                        dao.updateASdMvState("2", chgId + "", "1", dataPo.getPartOldcode(), "3", dataPo.getCheckQty() + "", chgId + "");
                    }
                }

                act.setOutData("success", "success");
            } else {
                throw new BizException(act, ErrorCodeConstant.UPDATE_FAILURE_CODE, error);
            }

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, error);
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 出库主页面
     */
    public void inStockMvInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orgId = "";//父机构（销售单位）ID
            String orgCode = "";//父机构（销售单位）编码
            String orgName = ""; //制单单位
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                orgId = Constant.OEM_ACTIVITIES;
                orgCode = Constant.ORG_ROOT_CODE;
                orgName = dao.getMainCompanyName(orgId);
            } else {
                orgId = logonUser.getDealerId();
                orgCode = logonUser.getDealerCode();
                orgName = dao.getDealerName(orgId);
            }
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.ORG_ID = '" + orgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());

            act.setOutData("parentOrgId", orgId);
            act.setOutData("parentOrgCode", orgCode);
            act.setOutData("companyName", orgName);
            act.setOutData("WHList", WHList);
            act.setOutData("WHList2", WHList);
            act.setOutData("oemId", Constant.OEM_ACTIVITIES);

            act.setForword(PART_MV_INSTOCK_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件移库入库页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 移库入库
     */
    public void inStockMvOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        String error = "";
        try {
            Long chgId = Long.parseLong(request.getParamValue("CHG_ID"));//移库ID
            String chgCode = CommonUtils.checkNull(request.getParamValue("CHG_CODE"));//移库编码
            String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));//制单单位id
            String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));//单位编码
            String orgName = CommonUtils.checkNull(request.getParamValue("orgName"));//单位名称
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));//移出库房
            String toWhId = CommonUtils.checkNull(request.getParamValue("toWhId"));//移入库房
            String toWhCode = CommonUtils.checkNull(request.getParamValue("toWhCode"));//移入库房
            String toWhName = CommonUtils.checkNull(request.getParamValue("toWhName"));//移入库房
            String[] partLocIdArr = req.getParamValues("cb");

            List<TtPartStockChgDtlPO> dtlPOArrayList = new ArrayList<TtPartStockChgDtlPO>();
            List<TtPartRecordPO> recordPOs = new ArrayList<TtPartRecordPO>();
            List<TtPartStockChgRecordPO> chgRecordPOs = new ArrayList<TtPartStockChgRecordPO>();
            //数量校验
            if (null != partLocIdArr) {
                TtPartStockChgDtlPO chgDtlPO = null;
                TtPartRecordPO recordPO = null;
                TtPartStockChgRecordPO chgRecordPO = null;
                for (int i = 0; i < partLocIdArr.length; i++) {
                    String partLocId = partLocIdArr[i];
                    String partId = partLocId.substring(0, partLocId.indexOf("_RNUM"));
                    String partOldcode = CommonUtils.checkNull(req.getParamValue("PART_OLDCODE" + partLocId));   //配件编码
                    String partCname = CommonUtils.checkNull(req.getParamValue("PART_CNAME" + partLocId));   //配件名称
                    String partCode = CommonUtils.checkNull(req.getParamValue("PART_CODE" + partLocId));   //配件件号
                    String sjLocId = CommonUtils.checkNull(req.getParamValue("LOC_ID_" + partLocId));   //配件实际货位ID
                    String sjLocCode = CommonUtils.checkNull(req.getParamValue("LOC_CODE_" + partLocId));   //配件货位ID
                    String outLocId = CommonUtils.checkNull(req.getParamValue("OUT_LOC_ID_" + partLocId));   //配件货位ID
                    String batchNo = CommonUtils.checkNull(req.getParamValue("BATCH_NO" + partLocId));   //批次号
                    
//                    List<Map<String, Object>> partList = dao.getPartStockInfos(partId, orgId, whId, outLocId, batchNo);
//                    String venderId = CommonUtils.checkNull(partList.get(0).get("STOCK_VENDER_ID").toString()); // 配件供应商id
//                    venderId = Constant.PART_RECORD_VENDER_ID;
                    
                    Long inQty = Long.valueOf(CommonUtils.checkNull(req.getParamValue("IN_QTY" + partLocId)));   //出库货位id
                    Long locId = null;

                    TtPartStockChgDtlPO dtlPO = new TtPartStockChgDtlPO();
                    dtlPO.setChgId(chgId);
                    dtlPO.setPartId(Long.valueOf(partId));
                    dtlPO = (TtPartStockChgDtlPO) dao.select(dtlPO).get(0);
                    //待入库数量
                    Long remQty = dtlPO.getOutQty() - dtlPO.getInQty();
                    if (remQty < inQty) {
                        error += "第" + (i + 1) + "行配件：" + partOldcode + "入库数量不能大于待入库数量：" + (remQty) + " <br/>!";
                    } else {
                        chgDtlPO = new TtPartStockChgDtlPO();
                        recordPO = new TtPartRecordPO();
                        chgRecordPO = new TtPartStockChgRecordPO();

                        //货位判断，有则使用无则插入
                        TtPartLoactionDefinePO definePO = new TtPartLoactionDefinePO();
                        definePO.setPartId(Long.valueOf(partId));
                        definePO.setWhId(Long.valueOf(toWhId));
                        definePO.setStatus(1);
                        definePO.setState(Constant.STATUS_ENABLE);
                        if (logonUser.getDealerId() == null) {
                            definePO.setRelocId(Long.valueOf(sjLocId.split(",")[0]));
                        }
                        List<TtPartLoactionDefinePO> ll = dao.select(definePO);

                        if (ll.size() == 0) {
                            locId = CommonUtils.parseLong(SequenceManager.getSequence(""));
                            //无货位则为该配件插入一条货位信息
                            definePO.setLocId(locId);
                            if (logonUser.getDealerId() == null) {
                                definePO.setLocCode(sjLocCode);
                                definePO.setLocName(sjLocCode);
                                definePO.setRelocId(Long.valueOf(sjLocId.split(",")[0]));
                            } else {
                                definePO.setLocCode("暂无");
                                definePO.setLocName("暂无");
                            }
                            definePO.setOrgId(logonUser.getOrgId());
                            definePO.setCreateDate(new Date());
                            definePO.setCreateBy(-1l);
                            dao.insert(definePO);

                        } else {
                            locId = ll.get(0).getLocId();
                        }

                        
                        Long recId = Long.parseLong(SequenceManager.getSequence(""));
                        recordPO.setRecordId(recId);
                        recordPO.setOrderCode(chgCode); // 入库业务编码
                        recordPO.setAddFlag(1);//入库标识
                        recordPO.setPartId(Long.valueOf(partId));
                        recordPO.setPartCode(partCode);
                        recordPO.setPartOldcode(partOldcode);
                        recordPO.setPartName(partCname);
                        
                        recordPO.setPartBatch(batchNo);
//                        recordPO.setVenderId(Long.parseLong(venderId));
                        recordPO.setVenderId(Long.parseLong(Constant.PART_RECORD_VENDER_ID));
                        
                        recordPO.setPartNum(Long.valueOf(inQty));//入库数量
                        recordPO.setConfigId(Long.parseLong(Constant.PART_CODE_RELATION_57 + ""));
                        recordPO.setOrderId(chgId);//移库单ID
                        recordPO.setOrgId(Long.parseLong(orgId));
                        recordPO.setOrgCode(orgCode);
                        recordPO.setOrgName(orgName);
                        recordPO.setWhId(Long.parseLong(toWhId));
                        recordPO.setWhCode(toWhCode);
                        recordPO.setWhName(toWhName);
                        recordPO.setLocId(Long.valueOf(locId));
                        recordPO.setLocCode(sjLocCode);
                        recordPO.setLocName(sjLocCode);
                        recordPO.setOptDate(new Date());
                        recordPO.setCreateDate(new Date());
                        recordPO.setPersonId(logonUser.getUserId());
                        recordPO.setPersonName(logonUser.getName());
                        recordPO.setPartState(1);//配件状态
                        recordPOs.add(recordPO);


                        chgRecordPO.setRecId(Long.parseLong(SequenceManager.getSequence("")));
                        chgRecordPO.setConfigId(Constant.PART_CODE_RELATION_58);//移库入库
                        chgRecordPO.setPartId(Long.valueOf(partId));
                        chgRecordPO.setPartCname(partCname);
                        chgRecordPO.setPartOldcode(partOldcode);
                        chgRecordPO.setPartCode(partCode);
                        chgRecordPO.setPartNum(0l);
                       /* chgRecordPO.setLocId(Long.valueOf(locId));
                        chgRecordPO.setLocCode(sjLocCode);*/
                        chgRecordPO.setTopartId(Long.valueOf(partId));
                        chgRecordPO.setTopartCname(partCname);
                        chgRecordPO.setTopartCode(partCode);
                        chgRecordPO.setTopartOldcode(partOldcode);
                        chgRecordPO.setTopartNum(Long.valueOf(inQty));//入库
                        chgRecordPO.setTolocId(locId);
                        chgRecordPO.setTolocCode(sjLocCode);
                        chgRecordPO.setCreateDate(new Date());
                        chgRecordPO.setCreateBy(logonUser.getUserId());
                        chgRecordPO.setOrgId(Long.valueOf(orgId));
                        chgRecordPO.setWhId(Long.valueOf(whId));
                        chgRecordPO.setTowhId(Long.valueOf(toWhId));
                        chgRecordPO.setRecCode(chgCode);
                        chgRecordPOs.add(chgRecordPO);

                        chgDtlPO.setChgId(chgId);
                        chgDtlPO.setPartId(Long.valueOf(partId));
                        chgDtlPO.setLocId(Long.valueOf(outLocId));
                        chgDtlPO.setInQty(Long.valueOf(inQty));
                        chgDtlPO.setTowhId(Long.parseLong(toWhId));
                        chgDtlPO.setTolocId(locId);
                        dtlPOArrayList.add(chgDtlPO);
                    }
                }
            }

            if ("".equals(error) && dtlPOArrayList.size() > 0) {
                //写出入库记录
                dao.insert(recordPOs);
                //写移库入库记录
                dao.insert(chgRecordPOs);
                //更新出库明细
                TtPartStockChgDtlPO srcDtlPO = null;
                TtPartStockChgDtlPO upDateDtlPO = null;
                for (TtPartStockChgDtlPO stockChgDtlPO : dtlPOArrayList) {
                    srcDtlPO = new TtPartStockChgDtlPO();
                    upDateDtlPO = new TtPartStockChgDtlPO();

                    srcDtlPO.setChgId(Long.valueOf(chgId));
                    srcDtlPO.setPartId(stockChgDtlPO.getPartId());
                    Long inedQty = ((TtPartStockChgDtlPO) dao.select(srcDtlPO).get(0)).getInQty();
                    upDateDtlPO.setInQty(stockChgDtlPO.getInQty() + inedQty);
                    upDateDtlPO.setTowhId(stockChgDtlPO.getTowhId());
                    upDateDtlPO.setTolocId(stockChgDtlPO.getTolocId());
                    
                    dao.update(srcDtlPO, upDateDtlPO);
                }
                //更新主表状态为已入库
                dao.updateMvState(chgId.toString());

                //调用出入库逻辑
                List<Object> ins = new LinkedList<Object>();
                ins.add(0, chgId);
                ins.add(1, Constant.PART_CODE_RELATION_57);
                ins.add(2, 0);//是否有占用标识，1有0否
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);

                act.setOutData("success", "success");
            } else {
                throw new BizException(act, ErrorCodeConstant.UPDATE_FAILURE_CODE, error);
            }
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, e.getMessage());
            act.setException(e1);
        }
    }
    

    public void checkSeatExist() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String loc_code = CommonUtils.checkNull(request.getParamValue("LOC_CODE"));
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
            String whName = CommonUtils.checkNull(request.getParamValue("whName"));
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));
            String partLocId = CommonUtils.checkNull(request.getParamValue("partLocId"));
            TtPartLoactionDefinePO plp = new TtPartLoactionDefinePO();//货位表
            plp.setLocCode(loc_code);
            plp.setWhId(Long.valueOf(whId));
            plp.setPartId(Long.parseLong(partId));
            plp.setState(Constant.STATUS_ENABLE);
            plp.setStatus(Constant.IF_TYPE_YES);
            List<TtPartLoactionDefinePO> list = (List<TtPartLoactionDefinePO>) dao.select(plp);
            if (list != null && list.size() > 0 && list.get(0) != null) {
                plp = (TtPartLoactionDefinePO) list.get(0);
                act.setOutData("LOC_ID", plp.getLocId());
                act.setOutData("LOC_CODE", plp.getLocCode());
                act.setOutData("LOC_NAME", plp.getLocName());
                act.setOutData("returnValue", 1);//该货位编码存在
            } else {
                act.setOutData("returnValue", 2);//该货位编码不存在
                act.setOutData("whName", whName);
                act.setOutData("LOC_CODE", plp.getLocCode());
            }
            
            act.setOutData("partLocId", partLocId);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "判断货位编码是否存在出现异常");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}
