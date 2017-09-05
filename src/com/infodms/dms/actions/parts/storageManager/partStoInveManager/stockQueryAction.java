package com.infodms.dms.actions.parts.storageManager.partStoInveManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.storageManager.partStoInveManager.stockInventoryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartOnwayPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理配件库存查询业务
 * @Date: 2013-5-4
 * @remark
 */
public class stockQueryAction extends BaseImport {
    public Logger logger = Logger.getLogger(stockQueryAction.class);
    private static final stockInventoryDao dao = stockInventoryDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    private static final int PART_STOCK_ZC = 1;//正常
    private static final int PART_STATE_ZC = 2;//正常封存
    private static final int PART_STATE_PY = 3;//盘盈封存
    private static final int PART_STATE_PK = 4;//盘亏封存

    //配件库存查询
    private static final String PART_STOCK_QUERY_MAIN = "/jsp/parts/storageManager/partStoInveManager/partStockQuery/partStoQueryMain.jsp";//配件库存查询首页
    private static final String PART_PD_STOCK_DETAIL = "/jsp/parts/storageManager/partStoInveManager/partStockQuery/partPDStockDetail.jsp";//配件盘点封存详情
    private static final String PART_ZC_STOCK_DETAIL = "/jsp/parts/storageManager/partStoInveManager/partStockQuery/partZCStockDetail.jsp";//配件正常封存详情
    private static final String PART_IN_STOCK_DETAIL = "/jsp/parts/storageManager/partStoInveManager/partStockQuery/partINStockDetail.jsp";//配件入库详情
    private static final String PART_IO_STOCK_DETAIL = "/jsp/parts/storageManager/partStoInveManager/partStockQuery/partIOStockDetail.jsp";//配件出入库详情
    private static final String PART_OUT_STOCK_DETAIL = "/jsp/parts/storageManager/partStoInveManager/partStockQuery/partOUTStockDetail.jsp";//配件出库详情
    private static final String PART_ZY_STOCK_DETAIL = "/jsp/parts/storageManager/partStoInveManager/partStockQuery/partZYStockDetail.jsp";//配件占用详情
    private static final String PART_LOC_DETAIL = "/jsp/parts/storageManager/partStoInveManager/partStockQuery/partLocDetail.jsp";//配件货位详情
    private static final String PART_ZT_STOCK_DETAIL = "/jsp/parts/storageManager/partStoInveManager/partStockQuery/partZTStockDetail.jsp";//配件在途详情

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-5-4
     * @Title : 跳转至配件库存查询页面
     */
    public void stockQueryInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            StringBuffer sbString = new StringBuffer();

            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            String companyName = ""; //制单单位
            String partsAmount = "0.00";
            String itemQty = "0";

            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;
                companyName = dao.getMainCompanyName(parentOrgId);
                List<Map<String, Object>> oemAmtList = dao.getOEMPartsAmount();
                if (null != oemAmtList && oemAmtList.size() > 0) {
                    partsAmount = oemAmtList.get(0).get("PARTS_AMOUNT").toString();
                    itemQty = oemAmtList.get(0).get("ITEM_QTY").toString();
                }
            } else {
                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();
                companyName = dao.getDealerName(parentOrgId);

                List<Map<String, Object>> dlrAmtList = dao.getDLRPartsAmount(parentOrgId);
                if (null != dlrAmtList && dlrAmtList.size() > 0) {
                    partsAmount = dlrAmtList.get(0).get("PARTS_AMOUNT").toString();
                    itemQty = dlrAmtList.get(0).get("ITEM_QTY").toString();
                }
            }
            sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());

            PageResult<Map<String, Object>> pssum = dao.showPartGroupAmount(
                    parentOrgId, "", Constant.PAGE_SIZE, 1);

            if(pssum.getRecords() != null && pssum.getRecords().size()>0){
                act.setOutData("qtyCnt",pssum.getRecords().get(0).get("GROUP_COUNT"));
                act.setOutData("qtySum",pssum.getRecords().get(0).get("GROUP_AMOUNT"));
            }

            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("oemOrgId", Constant.OEM_ACTIVITIES);
            act.setOutData("companyName", companyName);
            act.setOutData("WHList", WHList);
            act.setOutData("partsAmount", partsAmount);
            act.setOutData("itemQty", itemQty);
            act.setForword(PART_STOCK_QUERY_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件库存查询页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-10-23
     * @Title : 货位明细
     */
    public void showLCDetInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));// 配件ID
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));// 仓库ID

            act.setOutData("partId", partId);
            act.setOutData("whId", whId);

            act.setForword(PART_LOC_DETAIL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "盘点封存详情初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 查询配件库存信息
     */
    public void stockQuerySearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode")); // 配件件号
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname")); // 配件名称
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
            String searchType = CommonUtils.checkNull(request.getParamValue("searchType")); // 查询类型
            String isLocked = CommonUtils.checkNull(request.getParamValue("isLocked")); // 是否锁定
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            String state = CommonUtils.checkNull(request.getParamValue("STATE")); // 是否有效
            String inType = CommonUtils.checkNull(request.getParamValue("inType")); // 在库类型
            String locCode = CommonUtils.checkNull(request.getParamValue("locCode")); //货位编码
            String subCode = CommonUtils.checkNull(request.getParamValue("subCode")); //附属货位
            String sub = CommonUtils.checkNull(request.getParamValue("sub")); //是否有附属货位
            String flag = CommonUtils.checkNull(request.getParamValue("flag")); //订货点标志
            String showZero = CommonUtils.checkNull(request.getParamValue("showZero")); //是否显示零库存

            StringBuffer sbString = new StringBuffer();

            if ("specific".equalsIgnoreCase(searchType)) {
                String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 服务商编码
                String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 服务商名称

                if (null != dealerCode && !"".equals(dealerCode)) {
                    sbString.append(" AND UPPER(DL.DEALER_CODE) LIKE '%" + dealerCode.toUpperCase() + "%' ");
                }
                if (null != dealerName && !"".equals(dealerName)) {
                    sbString.append(" AND DL.DEALER_NAME LIKE '%" + dealerName + "%' ");
                }
                sbString.append(" AND TD.ORG_ID != '" + Constant.OEM_ACTIVITIES + "' ");
            } else {
                if (null != parentOrgId && !"".equals(parentOrgId)) {
                    sbString.append(" AND TD.ORG_ID = '" + parentOrgId + "' ");
                }
            }
            if (null != partCode && !"".equals(partCode)) {
                sbString.append(" AND UPPER(TD.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
            }
            if (null != partCname && !"".equals(partCname)) {
                sbString.append(" AND TD.PART_CNAME LIKE '%" + partCname + "%' ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbString.append(" AND UPPER(TD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
            }
            if (null != locCode && !"".equals(locCode)) {
                sbString.append(" AND UPPER(TD.loc_code) LIKE '%" + locCode.toUpperCase() + "%' ");
            }
            if (null != subCode && !"".equals(subCode)) {
                sbString.append(" AND UPPER(TD.sub_loc) LIKE '%" + subCode.toUpperCase() + "%' ");
            }
            if (null != sub && !"".equals(sub)) {
                if ("1".equals(sub)) {
                    sbString.append(" AND TD.sub_loc is not null ");
                } else {
                    sbString.append(" AND TD.sub_loc is  null ");
                }
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND TD.WH_ID = '" + whId + "' ");
            }
            if (null != isLocked && !"".equals(isLocked)) {
                sbString.append(" AND TD.IS_LOCKED = '" + isLocked + "' ");
            }
            if (null != state && !"".equals(state)) {
                sbString.append(" AND TD.PDSTATE = '" + state + "' ");
            }

            if (null != inType && !"".equals(inType)) {
                if ("1".equals(inType)) {
                    sbString.append(" AND (TD.BOOKED_QTY + TD.ZCFC_QTY + TD.PKFC_QTY) > '0' ");
                }
                /*else if("2".equals(inType))
                {
					sbString.append(" AND TD.PKFC_QTY > '0' ");
				}
				else if("3".equals(inType))
				{
					sbString.append(" AND TD.PDFC_QTY > '0' ");
				}
				else if("4".equals(inType))
				{
					sbString.append(" AND TD.BOOKED_QTY > '0' ");
				}
				else if("5".equals(inType))
				{
                    //modify by yuan 20130917
                    sbString.append("AND EXISTS (SELECT 1\n");
                    sbString.append("         FROM VW_PART_OEM_BO B\n");
                    sbString.append("        WHERE TD.PART_ID = B.PART_ID\n");
                    sbString.append("          AND TD.WH_ID = B.WH_ID)\n");
                }*/
            }
            if (!"".equals(flag)) {
                sbString.append("  AND EXISTS\n");
                sbString.append("(SELECT 1\n");
                sbString.append("         FROM TT_PART_PLAN_DEFINE D\n");
                sbString.append("        WHERE D.PART_ID = TD.PART_ID\n");
                sbString.append("          AND D.WH_ID = TD.WH_ID\n");
                //sbString.append("          AND TD.NORMAL_QTY < (D.SAFETY_STOCK + D.AVG_QTY * 30 / 2))\n");
                sbString.append("          AND TD.NORMAL_QTY < (D.AVG_QTY * 30 *1.5))\n");//低于月均销量1.5倍预警
            }
            if (!"".equals(showZero) && null != showZero) {
                if ("1".equals(showZero)) {

                } else if ("2".equals(showZero)) {
                    sbString.append(" AND TD.ITEM_QTY > 0");
                } else {
                    sbString.append(" AND TD.ITEM_QTY = 0");
                }

            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.showPartStockBase(
                    sbString.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件库存信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :配件库存查询  -汇总查询
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 汇总查询配件库存信息
     */
    public void stockQuerySearchAll() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode")); // 配件件号
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname")); // 配件名称
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
            String searchType = CommonUtils.checkNull(request.getParamValue("searchType")); // 查询类型
            String isLocked = CommonUtils.checkNull(request.getParamValue("isLocked")); // 是否锁定
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            String state = CommonUtils.checkNull(request.getParamValue("STATE")); // 是否有效
            String inType = CommonUtils.checkNull(request.getParamValue("inType")); // 在库类型
            String flag = CommonUtils.checkNull(request.getParamValue("flag")); //订货点标志
            String showZero = CommonUtils.checkNull(request.getParamValue("showZero")); //是否显示零库存
            String jj = CommonUtils.checkNull(request.getParamValue("jj")); //可用库存小于警戒库存

            StringBuffer sbString = new StringBuffer();

            if ("specific".equalsIgnoreCase(searchType)) {
                String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 服务商编码
                String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 服务商名称

                if (null != dealerCode && !"".equals(dealerCode)) {
                    sbString.append(" AND UPPER(DL.DEALER_CODE) LIKE '%" + dealerCode.toUpperCase() + "%' ");
                }
                if (null != dealerName && !"".equals(dealerName)) {
                    sbString.append(" AND DL.DEALER_NAME LIKE '%" + dealerName + "%' ");
                }
                sbString.append(" AND TD.ORG_ID != '" + Constant.OEM_ACTIVITIES + "' ");
            } else {
                if (null != parentOrgId && !"".equals(parentOrgId)) {
                    sbString.append(" AND TD.ORG_ID = '" + parentOrgId + "' ");
                }
            }
            if (null != partCode && !"".equals(partCode)) {
                sbString.append(" AND UPPER(TD.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
            }
            if (null != partCname && !"".equals(partCname)) {
                sbString.append(" AND TD.PART_CNAME LIKE '%" + partCname + "%' ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbString.append(" AND UPPER(TD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
            }

            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND TD.WH_ID = '" + whId + "' ");
            }
            if (null != isLocked && !"".equals(isLocked)) {
                sbString.append(" AND TD.IS_LOCKED = '" + isLocked + "' ");
            }
            if (null != state && !"".equals(state)) {
                sbString.append(" AND TD.PDSTATE = '" + state + "' ");
            }

            if (null != inType && !"".equals(inType)) {
                if ("1".equals(inType)) {
                    sbString.append(" AND (TD.BOOKED_QTY + TD.ZCFC_QTY + TD.PKFC_QTY) > '0' ");
                }
            }
            if (!"".equals(flag)) {
                sbString.append("  AND EXISTS\n");
                sbString.append("(SELECT 1\n");
                sbString.append("         FROM TT_PART_PLAN_DEFINE D\n");
                sbString.append("        WHERE D.PART_ID = TD.PART_ID\n");
                sbString.append("          AND D.WH_ID = TD.WH_ID\n");
                sbString.append("          AND TD.NORMAL_QTY < (D.AVG_QTY * 30 *1.5))\n");//低于月均销量1.5倍预警
            }
            if (!"".equals(showZero) && null != showZero) {
                if ("1".equals(showZero)) {

                } else if ("2".equals(showZero)) {
                    sbString.append(" AND TD.ITEM_QTY > 0");
                } else {
                    sbString.append(" AND TD.ITEM_QTY = 0");
                }

            }
            //可用库存小于警戒库存
            if("1".equals(jj)){
                sbString.append("HAVING nvl(SUM(TD.NORMAL_QTY),0) < nvl(SUM(TD.HALFY_QTY),0)\n");
            }
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.showPartStockBaseAll(
                    sbString.toString(), Constant.PAGE_SIZE, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件库存信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-24
     * @Title : 汇总查询
     */
    public void stockGroupQuery() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            String groupType = CommonUtils.checkNull(request.getParamValue("groupType"));// 汇总类型

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.showPartGroupAmount(
                    parentOrgId, groupType, Constant.PAGE_SIZE_MAX, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件库存信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-29
     * @Title : 配件详情查看初始化
     */
    public void showPDDetInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));// 配件ID
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));// 仓库ID
            String locId = CommonUtils.checkNull(request.getParamValue("locId"));// 仓库ID
            String viewPage = CommonUtils.checkNull(request.getParamValue("viewPage"));// 跳转页面
            String sDate = CommonUtils.checkNull(request.getParamValue("sDate"));// 开始日期
            String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));// 结束日期
            String sumFlag = CommonUtils.checkNull(request.getParamValue("sumFlag"));// 汇总标志
            String rcFlag = CommonUtils.checkNull(request.getParamValue("rcFlag"));// 出入库台账标准
            act.setOutData("partId", partId);
            act.setOutData("whId", whId);
            act.setOutData("locId", locId);
            act.setOutData("viewPage", viewPage);
            act.setOutData("sumFlag", sumFlag);
            act.setOutData("rcFlag", rcFlag);
            if (!"".equals(sDate) && null != sDate) {
                act.setOutData("sDate", CommonUtils.getNextDay(sDate, 0));
            }
            if (!"".equals(eDate) && null != eDate) {
                act.setOutData("eDate", eDate);
            }
            if ("pdPage".equalsIgnoreCase(viewPage)) {
                act.setForword(PART_PD_STOCK_DETAIL);
            } else if ("zcPage".equalsIgnoreCase(viewPage)) {
                act.setForword(PART_ZC_STOCK_DETAIL);
            } else if ("inPage".equalsIgnoreCase(viewPage)) {
                act.setForword(PART_IN_STOCK_DETAIL);
            } else if ("outPage".equalsIgnoreCase(viewPage)) {
                act.setForword(PART_OUT_STOCK_DETAIL);
            } else if ("zyPage".equalsIgnoreCase(viewPage)) {
                act.setOutData("fcFlag","0");
                act.setForword(PART_ZY_STOCK_DETAIL);
            } else if ("zTPage".equalsIgnoreCase(viewPage)) {
                act.setForword(PART_ZT_STOCK_DETAIL);
            } else if ("rcPage".equalsIgnoreCase(viewPage)) {
                act.setForword(PART_IO_STOCK_DETAIL);
            }else if ("fcPage".equalsIgnoreCase(viewPage)) {
                act.setOutData("fcFlag","1");
                act.setForword(PART_ZY_STOCK_DETAIL);
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "盘点封存详情初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-29
     * @Title : 盘点封存详情查询
     */
    public void showPDDetSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId")); // 配件ID
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
            String fcName = CommonUtils.checkNull(request.getParamValue("fcName")); // 封存人
            String remark = CommonUtils.checkNull(request.getParamValue("remark")); // 备注
//			String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 封存开始时间
//			String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 封存截止时间

            String showType = CommonUtils.checkNull(request.getParamValue("showType")); // 显示类型

            StringBuffer sbString = new StringBuffer();
            if (null != partId && !"".equals(partId)) {
                sbString.append(" AND SD.PART_ID = '" + partId + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND SM.WH_ID = '" + whId + "' ");
            }
            if (null != fcName && !"".equals(fcName)) {
                sbString.append(" AND U.NAME LIKE '%" + fcName + "%' ");
            }
            if (null != remark && !"".equals(remark)) {
                sbString.append(" AND UPPER(SD.REMARK) LIKE '%" + remark + "%' ");
            }
            /*if(null != checkSDate && !"".equals(checkSDate))
            {
				sbString.append(" AND TO_CHAR(SD.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
			}
			if(null != checkEDate && !"".equals(checkEDate))
			{
				sbString.append(" AND TO_CHAR(SD.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
			}*/

            String sqlStr = "";

            if ("pdfc".equalsIgnoreCase(showType)) {
                sqlStr = " AND SD.CHANGE_REASON IN ('" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02 + "', '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03 + "') AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 + "' ";
            } else if ("zcfc".equalsIgnoreCase(showType)) {
                sqlStr = " AND SD.CHANGE_REASON IN ('" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01 + "', " +
                        "'" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01 + "', '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_04 + "', " +
                        "'" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_05 + "', '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_06 + "', '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_07 + "') AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 + "' ";
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.showPartPDStockDT(
                    sbString.toString(), sqlStr, 12, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件盘点封存详细信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-18
     * @Title : 占用详情
     */
    public void showPartZYDetSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String OrgId = "";//父机构（销售单位）ID

            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                OrgId = Constant.OEM_ACTIVITIES;
            } else {
                OrgId = logonUser.getDealerId();
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.showPartZYStockDT(
                    request, OrgId, 12, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件盘点封存详细信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :配件出入库明细查询
     * @return :
     * @throws : LastDate    : 2013-7-11
     * @Title : 配件出入库明细查询
     */
    public void showInOrOutDetSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId")); // 配件ID
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
            String locId = CommonUtils.checkNull(request.getParamValue("locId")); // 仓库ID

            String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 封存开始时间
            String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 封存截止时间

            String showType = CommonUtils.checkNull(request.getParamValue("showType")); // 显示类型
            String sumFlag = CommonUtils.checkNull(request.getParamValue("sumFlag")); // 汇总标志
            String rcFlag = CommonUtils.checkNull(request.getParamValue("rcFlag")); // 出入库台账标志
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode")); // 业务单号

            PageResult<Map<String, Object>> ps = null;
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

            StringBuffer sbString = new StringBuffer();
            if (null != locId && !"".equals(locId)) {
                sbString.append(" AND R.LOC_ID =" + locId);
            }
            if (null != partId && !"".equals(partId)) {
                sbString.append(" AND R.PART_ID = '" + partId + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND R.WH_ID = '" + whId + "' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(R.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(R.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }

//			sbString.append(" AND R.PART_STATE = '"+ PART_STOCK_ZC +"' "); //正常

            if ("inDetail".equalsIgnoreCase(showType)) {
                String checkCode = CommonUtils.checkNull(request.getParamValue("checkCode")); // 验货单号
                String inCode = CommonUtils.checkNull(request.getParamValue("inCode")); // 入库单号
                if (sumFlag.equals("1")) {
                    sbString.append(" AND IN_TYPE not LIKE '%移位%'");
                }
//				sbString.append(" AND R.ADD_FLAG = 1 "); //入库

                ps = dao.showPartInStockDT(sbString.toString(), checkCode, inCode, 12, curPage);
            } else if ("outDetail".equalsIgnoreCase(showType)) {
                String soCode = CommonUtils.checkNull(request.getParamValue("soCode")); // 销售单号
                String outCode = CommonUtils.checkNull(request.getParamValue("outCode")); // 出库单号
                String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 出库单号
                if (sumFlag.equals("1")) {
                    sbString.append(" AND OUT_TYPE not LIKE '%移位%'");
                }
//				sbString.append(" AND R.ADD_FLAG = 2 "); //出库

                ps = dao.showPartOutStockDT(sbString.toString(), soCode, outCode, dealerName, 12, curPage);
            } else if ("rcDetail".equalsIgnoreCase(showType)) {
                String soCode = CommonUtils.checkNull(request.getParamValue("soCode")); // 销售单号
                String outCode = CommonUtils.checkNull(request.getParamValue("outCode")); // 出库单号
                String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 服务商名称

                ps = dao.showPartRCDtl(sbString.toString(), orderCode, outCode, dealerName, 12, curPage);
            }

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件出入库详细信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-10-14
     * @Title : 入库/出库信息统计
     */
    public void countInOrOutQuery() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId")); // 配件ID
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
            String locId = CommonUtils.checkNull(request.getParamValue("locId")); // 仓库ID

            String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 封存开始时间
            String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 封存截止时间

            String showType = CommonUtils.checkNull(request.getParamValue("showType")); // 显示类型
            String sumFlag = CommonUtils.checkNull(request.getParamValue("sumFlag")); // 显示类型
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode")); // 业务单号

            List<Map<String, Object>> list = null;

            StringBuffer sbString = new StringBuffer();
            if (null != locId && !"".equals(locId)) {
                sbString.append(" AND R.LOC_ID =" + locId);
            }
            if (null != partId && !"".equals(partId)) {
                sbString.append(" AND R.PART_ID = '" + partId + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND R.WH_ID = '" + whId + "' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(R.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(R.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }

//			sbString.append(" AND R.PART_STATE = '"+ PART_STOCK_ZC +"' "); //正常

            if ("inDetail".equalsIgnoreCase(showType)) {
                String checkCode = CommonUtils.checkNull(request.getParamValue("checkCode")); // 验货单号
                String inCode = CommonUtils.checkNull(request.getParamValue("inCode")); // 入库单号
                if (sumFlag.equals("1")) {
                    sbString.append(" AND IN_TYPE not LIKE '%移位%'");
                }

//				sbString.append(" AND R.ADD_FLAG = 1 "); //入库

                list = dao.countPartInStockDT(sbString.toString(), checkCode, inCode);

                if (null != list && list.size() > 0) {
                    int temCount = Integer.parseInt(list.get(0).get("DET_COUNT").toString());
                    if (temCount > 0) {
                        act.setOutData("detailCount", list.get(0).get("DET_COUNT"));
                        act.setOutData("inCount", list.get(0).get("IN_QTY"));
                    } else {
                        act.setOutData("detailCount", "0");
                        act.setOutData("inCount", "0");
                    }
                } else {
                    act.setOutData("detailCount", "0");
                    act.setOutData("inCount", "0");
                }
            } else if ("outDetail".equalsIgnoreCase(showType)) {
                String soCode = CommonUtils.checkNull(request.getParamValue("soCode")); // 销售单号
                String outCode = CommonUtils.checkNull(request.getParamValue("outCode")); // 出库单号
                if (sumFlag.equals("1")) {
                    sbString.append(" AND OUT_TYPE not LIKE '%移位%'");
                }

                list = dao.countPartOutStockDT(sbString.toString(), soCode, outCode);

                if (null != list && list.size() > 0) {
                    int temCount = Integer.parseInt(list.get(0).get("DET_COUNT").toString());
                    if (temCount > 0) {
                        act.setOutData("detailCount", list.get(0).get("DET_COUNT"));
                        act.setOutData("outCount", list.get(0).get("OUT_OTY"));
                    } else {
                        act.setOutData("detailCount", "0");
                        act.setOutData("outCount", "0");
                    }

                } else {
                    act.setOutData("detailCount", "0");
                    act.setOutData("outCount", "0");
                }
            } else if ("rcDetail".equalsIgnoreCase(showType)) {
                list = dao.countPartIOSum(sbString.toString(), orderCode, null);
                if (null != list && list.size() > 0) {
                    act.setOutData("IN_QTY", list.get(0).get("IN_QTY") == null ? 0 : list.get(0).get("IN_QTY"));
                    act.setOutData("OUT_QTY", list.get(0).get("OUT_QTY") == null ? 0 : list.get(0).get("OUT_QTY"));
                } else {
                    act.setOutData("IN_QTY", "0");
                    act.setOutData("OUT_QTY", "0");
                }
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件出入库详细信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-5-4
     * @Title :导出配件库存信息
     */
    public void exportPartStockStatusExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode")); // 配件件号
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname")); // 配件名称
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
            String searchType = CommonUtils.checkNull(request.getParamValue("searchType")); // 查询类型
            String isLocked = CommonUtils.checkNull(request.getParamValue("isLocked")); // 是否锁定
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            String state = CommonUtils.checkNull(request.getParamValue("STATE")); // 是否有效
            String inType = CommonUtils.checkNull(request.getParamValue("inType")); // 在库类型
            String locCode = CommonUtils.checkNull(request.getParamValue("locCode")); //货位编码
            String subCode = CommonUtils.checkNull(request.getParamValue("subCode")); //附属货位
            String sub = CommonUtils.checkNull(request.getParamValue("sub")); //是否有附属货位
            String showZero = CommonUtils.checkNull(request.getParamValue("showZero")); //是否显示零库存

            StringBuffer sbString = new StringBuffer();
            if ("specific".equalsIgnoreCase(searchType)) {
                String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 服务商编码
                String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 服务商名称

                if (null != dealerCode && !"".equals(dealerCode)) {
                    sbString.append(" AND UPPER(DL.DEALER_CODE) LIKE '%" + dealerCode.trim().toUpperCase() + "%' ");
                }
                if (null != dealerName && !"".equals(dealerName)) {
                    sbString.append(" AND DL.DEALER_NAME LIKE '%" + dealerName + "%' ");
                }
                sbString.append(" AND TD.ORG_ID != '" + Constant.OEM_ACTIVITIES + "' ");
            } else {
                if (null != parentOrgId && !"".equals(parentOrgId)) {
                    sbString.append(" AND TD.ORG_ID = '" + parentOrgId + "' ");
                }
            }
            if (null != partCode && !"".equals(partCode)) {
                sbString.append(" AND UPPER(TD.PART_CODE) LIKE '%" + partCode.trim().toUpperCase() + "%' ");
            }
            if (null != partCname && !"".equals(partCname)) {
                sbString.append(" AND TD.PART_CNAME LIKE '%" + partCname + "%' ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbString.append(" AND UPPER(TD.PART_OLDCODE) LIKE '%" + partOldcode.trim().toUpperCase() + "%' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND TD.WH_ID = '" + whId + "' ");
            }
            if (null != isLocked && !"".equals(isLocked)) {
                sbString.append(" AND TD.IS_LOCKED = '" + isLocked + "' ");
            }
            if (null != state && !"".equals(state)) {
                sbString.append(" AND TD.PDSTATE = '" + state + "' ");
            }
            if (null != locCode && !"".equals(locCode)) {
                sbString.append(" AND UPPER(TD.loc_code) LIKE '%" + locCode.toUpperCase() + "%' ");
            }
            if (null != subCode && !"".equals(subCode)) {
                sbString.append(" AND UPPER(TD.sub_loc) LIKE '%" + subCode.toUpperCase() + "%' ");
            }
            if (null != sub && !"".equals(sub)) {
                if ("1".equals(sub)) {
                    sbString.append(" AND TD.sub_loc is not null ");
                } else {
                    sbString.append(" AND TD.sub_loc is  null ");
                }
            }
            if (null != inType && !"".equals(inType)) {
                if ("1".equals(inType)) {
                    sbString.append(" AND (TD.BOOKED_QTY + TD.ZCFC_QTY + TD.PKFC_QTY) > '0' ");
                }
            }
            if (!"".equals(showZero) && null != showZero) {
                if ("1".equals(showZero)) {

                } else if ("2".equals(showZero)) {
                    sbString.append(" AND TD.ITEM_QTY > 0");
                } else {
                    sbString.append(" AND TD.ITEM_QTY = 0");
                }

            }
            if (null != partOldcode && !"".equals(partOldcode)) {
//                sbString.append(" ORDER BY TD.PART_OLDCODE ");
            }

            String[] head = new String[25];

            if (!"specific".equalsIgnoreCase(searchType)) {
                head[0] = "序号";
                head[1] = "仓库";
                head[2] = "配件编码";
                head[3] = "配件名称";
//				head[4] = "件号";
                head[4] = "最小包装量";
                head[5] = "单位";
                head[6] = "货位";
//				head[8] = "附属货位";
                head[7] = "可用库存";
                head[8] = "占用库存";
//				head[11] = "正常封存";
//				head[12] = "盘亏封存";
                head[9] = "账面库存";
//				head[14] = "盘盈封存";
                head[10] = "在途数量";
//				head[14] = "销售BO数量";
                /*if(Constant.OEM_ACTIVITIES.equals(parentOrgId))
                {
					head[12] = "计划价(元)";
				}
				else
				{
					head[12] = "采购价(元)";
				}
				*/
                head[11] = "是否锁定";
                head[12] = "是否有效";
                head[13] = "配件备注";
            } else {
                head[0] = "序号";
                head[1] = "服务商编码";
                head[2] = "服务商名称";
                head[3] = "仓库";
                head[4] = "配件编码";
                head[5] = "配件名称";
//				head[6] = "件号";
                head[6] = "最小包装量";
                head[7] = "单位";
                head[8] = "货位";
//				head[10] = "附属货位";
                head[9] = "可用库存";
                head[10] = "占用库存";
//				head[12] = "正常封存";
//				head[13] = "盘亏封存";
                head[11] = "账面库存";
//				head[15] = "盘盈封存";
//				head[16] = "销售BO数量";
//				head[13] = "采购价(元)";
                head[12] = "是否锁定";
                head[13] = "是否有效";
            }

            List<Map<String, Object>> list = dao.showPartStockBase(sbString.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[25];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        if (!"specific".equalsIgnoreCase(searchType)) {
                            detail[1] = CommonUtils.checkNull(map
                                    .get("WH_NAME"));
                            detail[2] = CommonUtils
                                    .checkNull(map.get("PART_OLDCODE"));
                            detail[3] = CommonUtils.checkNull(map
                                    .get("PART_CNAME"));
//							detail[4] = CommonUtils.checkNull(map
//									.get("PART_CODE"));
                            detail[4] = CommonUtils.checkNull(map
                                    .get("OEM_MIN_PKG"));
                            detail[5] = CommonUtils.checkNull(map
                                    .get("UNIT"));
                            detail[6] = CommonUtils.checkNull(map
                                    .get("LOC_NAME"));
//                            detail[8] = CommonUtils.checkNull(map
//									.get("SUB_LOC"));
                            detail[7] = CommonUtils.checkNull(map
                                    .get("NORMAL_QTY"));
                            detail[8] = CommonUtils.checkNull(map
                                    .get("BOOKED_QTY_NEW"));
//							detail[11] = CommonUtils.checkNull(map
//									.get("ZCFC_QTY"));
//							detail[12] = CommonUtils.checkNull(map
//									.get("PKFC_QTY"));
                            detail[9] = CommonUtils.checkNull(map
                                    .get("ITEM_QTY"));
//							detail[14] = CommonUtils.checkNull(map
//									.get("PDFC_QTY"));
                            detail[10] = CommonUtils.checkNull(map
                                    .get("ZT_QTY"));
                            /*detail[12] = CommonUtils.checkNull(map
                                    .get("PRICE"));*/

                            if ("1".equals(CommonUtils.checkNull(map.get("IS_LOCKED")))) {
                                detail[11] = "是";
                            } else {
                                detail[11] = "否";
                            }


                            if ((Constant.STATUS_ENABLE + "").equals(map.get("PDSTATE").toString())) {
                                detail[12] = "有效";
                            } else {
                                detail[12] = "无效";
                            }
                            detail[13] = CommonUtils.checkNull(map
                                    .get("REMARK"));
                        } else {
                            detail[1] = CommonUtils.checkNull(map
                                    .get("DEALER_CODE"));
                            detail[2] = CommonUtils
                                    .checkNull(map.get("DEALER_NAME"));
                            detail[3] = CommonUtils.checkNull(map
                                    .get("WH_NAME"));
                            detail[4] = CommonUtils
                                    .checkNull(map.get("PART_OLDCODE"));
                            detail[5] = CommonUtils.checkNull(map
                                    .get("PART_CNAME"));
//							detail[6] = CommonUtils.checkNull(map
//									.get("PART_CODE"));
                            detail[6] = CommonUtils.checkNull(map
                                    .get("OEM_MIN_PKG"));
                            detail[7] = CommonUtils.checkNull(map
                                    .get("UNIT"));
                            detail[8] = CommonUtils.checkNull(map
                                    .get("LOC_NAME"));
//                            detail[10] = CommonUtils.checkNull(map
//                                    .get("SUB_LOC"));
                            detail[9] = CommonUtils.checkNull(map
                                    .get("NORMAL_QTY"));
                            detail[10] = CommonUtils.checkNull(map
                                    .get("BOOKED_QTY_NEW"));
//							detail[13] = CommonUtils.checkNull(map
//									.get("ZCFC_QTY"));
//							detail[14] = CommonUtils.checkNull(map
//									.get("PKFC_QTY"));
                            detail[11] = CommonUtils.checkNull(map
                                    .get("ITEM_QTY"));
//							detail[16] = CommonUtils.checkNull(map
//									.get("PDFC_QTY"));
//							detail[17] = CommonUtils.checkNull(map
//									.get("BO_QTY"));
                            /*detail[13] = CommonUtils.checkNull(map
                                    .get("PRICE"));*/

                            if ("1".equals(CommonUtils.checkNull(map.get("IS_LOCKED")))) {
                                detail[12] = "是";
                            } else {
                                detail[12] = "否";
                            }
                            if ((Constant.STATUS_ENABLE + "").equals(map.get("PDSTATE").toString())) {
                                detail[13] = "有效";
                            } else {
                                detail[13] = "无效";
                            }
                        }

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件库存信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件库存信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void exportPartStockStatusExcelAll() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode")); // 配件件号
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname")); // 配件名称
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
            String searchType = CommonUtils.checkNull(request.getParamValue("searchType")); // 查询类型
            String isLocked = CommonUtils.checkNull(request.getParamValue("isLocked")); // 是否锁定
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            String state = CommonUtils.checkNull(request.getParamValue("STATE")); // 是否有效
            String inType = CommonUtils.checkNull(request.getParamValue("inType")); // 在库类型
            String showZero = CommonUtils.checkNull(request.getParamValue("showZero")); // 是否显示零库存
            String jj = CommonUtils.checkNull(request.getParamValue("jj")); // 可以库存小于警戒库存

            StringBuffer sbString = new StringBuffer();
            if ("specific".equalsIgnoreCase(searchType)) {
                String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 服务商编码
                String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 服务商名称

                if (null != dealerCode && !"".equals(dealerCode)) {
                    sbString.append(" AND UPPER(DL.DEALER_CODE) LIKE '%" + dealerCode.trim().toUpperCase() + "%' ");
                }
                if (null != dealerName && !"".equals(dealerName)) {
                    sbString.append(" AND DL.DEALER_NAME LIKE '%" + dealerName + "%' ");
                }
                sbString.append(" AND TD.ORG_ID != '" + Constant.OEM_ACTIVITIES + "' ");
            } else {
                if (null != parentOrgId && !"".equals(parentOrgId)) {
                    sbString.append(" AND TD.ORG_ID = '" + parentOrgId + "' ");
                }
            }
            if (null != partCode && !"".equals(partCode)) {
                sbString.append(" AND UPPER(TD.PART_CODE) LIKE '%" + partCode.trim().toUpperCase() + "%' ");
            }
            if (null != partCname && !"".equals(partCname)) {
                sbString.append(" AND TD.PART_CNAME LIKE '%" + partCname + "%' ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbString.append(" AND UPPER(TD.PART_OLDCODE) LIKE '%" + partOldcode.trim().toUpperCase() + "%' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND TD.WH_ID = '" + whId + "' ");
            }
            if (null != isLocked && !"".equals(isLocked)) {
                sbString.append(" AND TD.IS_LOCKED = '" + isLocked + "' ");
            }
            if (null != state && !"".equals(state)) {
                sbString.append(" AND TD.PDSTATE = '" + state + "' ");
            }
            if (null != inType && !"".equals(inType)) {
                if ("1".equals(inType)) {
                    sbString.append(" AND (TD.BOOKED_QTY + TD.ZCFC_QTY + TD.PKFC_QTY) > '0' ");
                }
            }
            if (!"".equals(showZero) && null != showZero) {
                if ("1".equals(showZero)) {

                } else if ("2".equals(showZero)) {
                    sbString.append(" AND TD.ITEM_QTY > 0");
                } else {
                    sbString.append(" AND TD.ITEM_QTY = 0");
                }

            }
            //可用库存小于警戒库存
            if("1".equals(jj)){
                sbString.append("HAVING nvl(SUM(TD.NORMAL_QTY),0) < nvl(SUM(TD.HALFY_QTY),0)\n");
            }
            String[] head = new String[25];

            if (!"specific".equalsIgnoreCase(searchType)) {
                head[0] = "序号";
                head[1] = "仓库";
                head[2] = "配件编码";
                head[3] = "配件名称";
//				head[4] = "件号";
                head[4] = "最小包装量";
                head[5] = "单位";
                head[6] = "可用库存";
                head[7] = "占用库存";
                head[8] = "封存库存";
                head[9] = "账面库存";
                head[10] = "在途数量";
                if (Constant.OEM_ACTIVITIES.equals(parentOrgId)) {
                    head[11] = "警戒库存";
                    head[12] = "安全库存";
                    head[13] = "最大库存";
                    head[14] = "是否锁定";
                    head[15] = "是否有效";
                    head[16] = "配件备注";
                } else {
                    head[11] = "是否锁定";
                    head[12] = "是否有效";
                    head[13] = "配件备注";
                }
            } else {
                head[0] = "序号";
                head[1] = "服务商编码";
                head[2] = "服务商名称";
                head[3] = "仓库";
                head[4] = "配件编码";
                head[5] = "配件名称";
//				head[6] = "件号";
                head[6] = "最小包装量";
                head[7] = "单位";
                head[8] = "可用库存";
                head[9] = "占用库存";
                head[10] = "账面库存";
                head[11] = "采购价(元)";
                head[12] = "是否锁定";
                head[13] = "是否有效";
            }

            List<Map<String, Object>> list = dao.showPartStockBaseAll(sbString.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[25];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        if (!"specific".equalsIgnoreCase(searchType)) {
                            detail[1] = CommonUtils.checkNull(map
                                    .get("WH_NAME"));
                            detail[2] = CommonUtils
                                    .checkNull(map.get("PART_OLDCODE"));
                            detail[3] = CommonUtils.checkNull(map
                                    .get("PART_CNAME"));
//							detail[4] = CommonUtils.checkNull(map
//									.get("PART_CODE"));
                            detail[4] = CommonUtils.checkNull(map
                                    .get("OEM_MIN_PKG"));
                            detail[5] = CommonUtils.checkNull(map
                                    .get("UNIT"));
                            detail[6] = CommonUtils.checkNull(map
                                    .get("NORMAL_QTY"));
                            detail[7] = CommonUtils.checkNull(map
                                    .get("BOOKED_QTY_NEW"));
                            detail[8] = CommonUtils.checkNull(map
                                    .get("FC_QTY"));
                            detail[9] = CommonUtils.checkNull(map
                                    .get("ITEM_QTY"));
                            detail[10] = CommonUtils.checkNull(map
                                    .get("ZT_QTY"));
                            if (Constant.OEM_ACTIVITIES.equals(parentOrgId)) {
                                detail[11] = CommonUtils.checkNull(map.get("HALFY_QTY"));
                                detail[12] = CommonUtils.checkNull(map.get("SAFETY_QTY"));
                                detail[13] = CommonUtils.checkNull(map.get("MAX_QTY"));
                                if ("1".equals(CommonUtils.checkNull(map.get("IS_LOCKED")))) {
                                    detail[14] = "是";
                                } else {
                                    detail[14] = "否";
                                }


                                if ((Constant.STATUS_ENABLE + "").equals(map.get("PDSTATE").toString())) {
                                    detail[15] = "有效";
                                } else {
                                    detail[15] = "无效";
                                }
                                detail[16] = CommonUtils.checkNull(map
                                        .get("REMARK"));
                            } else {
                                if ("1".equals(CommonUtils.checkNull(map.get("IS_LOCKED")))) {
                                    detail[11] = "是";
                                } else {
                                    detail[11] = "否";
                                }


                                if ((Constant.STATUS_ENABLE + "").equals(map.get("PDSTATE").toString())) {
                                    detail[12] = "有效";
                                } else {
                                    detail[12] = "无效";
                                }
                                detail[13] = CommonUtils.checkNull(map
                                        .get("REMARK"));
                            }
                           /* detail[10] = CommonUtils.checkNull(map
                                    .get("PRICE"));*/
                        } else {
                            detail[1] = CommonUtils.checkNull(map
                                    .get("DEALER_CODE"));
                            detail[2] = CommonUtils
                                    .checkNull(map.get("DEALER_NAME"));
                            detail[3] = CommonUtils.checkNull(map
                                    .get("WH_NAME"));
                            detail[4] = CommonUtils
                                    .checkNull(map.get("PART_OLDCODE"));
                            detail[5] = CommonUtils.checkNull(map
                                    .get("PART_CNAME"));
//							detail[6] = CommonUtils.checkNull(map
//									.get("PART_CODE"));
                            detail[6] = CommonUtils.checkNull(map
                                    .get("OEM_MIN_PKG"));
                            detail[7] = CommonUtils.checkNull(map
                                    .get("UNIT"));
                            detail[8] = CommonUtils.checkNull(map
                                    .get("NORMAL_QTY"));
                            detail[9] = CommonUtils.checkNull(map
                                    .get("BOOKED_QTY_NEW"));
                            detail[10] = CommonUtils.checkNull(map
                                    .get("ITEM_QTY"));
                            detail[11] = CommonUtils.checkNull(map
                                    .get("PRICE"));

                            if ("1".equals(CommonUtils.checkNull(map.get("IS_LOCKED")))) {
                                detail[12] = "是";
                            } else {
                                detail[12] = "否";
                            }
                            if ((Constant.STATUS_ENABLE + "").equals(map.get("PDSTATE").toString())) {
                                detail[13] = "有效";
                            } else {
                                detail[13] = "无效";
                            }
                        }

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件库存信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件库存信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void expPartStockAmountExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            String groupType = CommonUtils.checkNull(request.getParamValue("groupType"));// 汇总类型

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "服务商代码";
            head[2] = "服务商名称";
            head[3] = "库房";
            head[4] = "总账面库存";
            head[5] = "总在库含税金额(账面库存*服务商价格)";

            List<Map<String, Object>> list = dao.showPartGroupAmount(
                    parentOrgId, groupType, Constant.PAGE_SIZE_MAX, 1).getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("WH_NAME"));
                        detail[4] = CommonUtils.checkNull(map.get("GROUP_COUNT")).replace(",", "");
                        detail[5] = CommonUtils.checkNull(map.get("GROUP_AMOUNT")).replace(",", "");
                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件在库资金汇总查询";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件库存信息");
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
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i].replace(",", ""))));
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
     * ：zhumingwei 2013-09-17
     *
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-9-17
     * @Title : 配件入库详情
     */
    public void exportPartStockDetailExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId")); // 配件ID
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
            String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 封存开始时间
            String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 封存截止时间
            String checkCode = CommonUtils.checkNull(request.getParamValue("checkCode")); // 验货单号
            String inCode = CommonUtils.checkNull(request.getParamValue("inCode")); // 入库单号
            String sumFlag = CommonUtils.checkNull(request.getParamValue("sumFlag")); //汇总标志

            StringBuffer sbString = new StringBuffer();
            if (null != partId && !"".equals(partId)) {
                sbString.append(" AND R.PART_ID = '" + partId + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND R.WH_ID = '" + whId + "' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(R.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(R.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
            if (sumFlag.equals("1")) {
                sbString.append(" AND IN_TYPE not LIKE '%移位%'");
            }
//			sbString.append(" AND R.PART_STATE = '"+ PART_STOCK_ZC +"' "); //正常
//			sbString.append(" AND R.ADD_FLAG = 1 "); //入库

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "验货单号";
            head[2] = "入库单号";
            head[3] = "入库类型";
            head[4] = "供应商";
            head[5] = "配件编码";
            head[6] = "配件名称";
            //head[7] = "件号";
            head[7] = "入库数量";
            head[8] = "货位";
            head[9] = "单价(元)";
            head[10] = "金额(元)";
            head[11] = "入库时间";
            head[12] = "入库人";

            List<Map<String, Object>> list = dao.showDetailExcel(sbString.toString(), checkCode, inCode);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("CHECK_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("IN_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("IN_TYPE"));
                        detail[4] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[6] = CommonUtils.checkNull(map.get("PART_NAME"));
                        //detail[7] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[7] = CommonUtils.checkNull(map.get("PART_NUM"));
                        detail[8] = CommonUtils.checkNull(map.get("LOC_NAME"));
                        detail[9] = CommonUtils.checkNull(map.get("BUY_PRICE"));
                        detail[10] = CommonUtils.checkNull(map.get("IN_AMOUNT"));
                        detail[11] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[12] = CommonUtils.checkNull(map.get("NAME"));

						/*if("1".equals(CommonUtils.checkNull(map.get("IS_LOCKED"))))
						{
							detail[16] = "是";
						}
						else
						{
							detail[16] = "否";
						}
						if((Constant.STATUS_ENABLE + "").equals(map.get("STATE").toString()))
						{
							detail[17] = "有效";
						}
						else
						{
							detail[17] = "无效";
						}*/
                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件入库明细信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(), request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件入库明细信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     *
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-9-17
     * @Title : 配件出库详情
     */
    public void exportPartSoCodeDetailExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId")); // 配件ID
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
            String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 封存开始时间
            String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 封存截止时间
            String soCode = CommonUtils.checkNull(request.getParamValue("soCode")); // 销售单号
            String outCode = CommonUtils.checkNull(request.getParamValue("outCode")); // 出库单号
            String sumFlag = CommonUtils.checkNull(request.getParamValue("sumFlag")); // 汇总标志

            StringBuffer sbString = new StringBuffer();
            if (null != partId && !"".equals(partId)) {
                sbString.append(" AND R.PART_ID = '" + partId + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND R.WH_ID = '" + whId + "' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(R.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(R.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
            if (sumFlag.equals("1")) {
                sbString.append(" AND OUT_TYPE not LIKE '%移位%'");
            }
//			sbString.append(" AND R.PART_STATE = '"+ PART_STOCK_ZC +"' "); //正常
//			sbString.append(" AND R.ADD_FLAG = 2 "); //出库

            String[] head = new String[14];
            head[0] = "序号";
            head[1] = "销售单号";
            head[2] = "出库单号";
            head[3] = "出库类型";
            head[4] = "订货单位";
            head[5] = "配件编码";
            head[6] = "配件名称";
            //head[7] = "件号";
            head[7] = "出库数量";
            head[8] = "货位";
            head[9] = "单价(元)";
            head[10] = "金额(元)";
            head[11] = "出库时间";

            List<Map<String, Object>> list = dao.showSoCodeDetailExcel(sbString.toString(), soCode, outCode);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[14];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("SO_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("OUT_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("OUT_TYPE"));
                        detail[4] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[6] = CommonUtils.checkNull(map.get("PART_NAME"));
                        //detail[7] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[7] = CommonUtils.checkNull(map.get("PART_NUM"));
                        detail[8] = CommonUtils.checkNull(map.get("LOC_NAME"));
                        detail[9] = CommonUtils.checkNull(map.get("SALE_PRICE"));
                        detail[10] = CommonUtils.checkNull(map.get("SALE_AMOUNT"));
                        detail[11] = CommonUtils.checkNull(map.get("CREATE_DATE"));
						
						/*if("1".equals(CommonUtils.checkNull(map.get("IS_LOCKED"))))
						{
							detail[16] = "是";
						}
						else
						{
							detail[16] = "否";
						}
						if((Constant.STATUS_ENABLE + "").equals(map.get("STATE").toString()))
						{
							detail[17] = "有效";
						}
						else
						{
							detail[17] = "无效";
						}*/
                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件出库明细信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(), request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件出库明细信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-18
     * @Title : 占用详情
     */
    public void showPartZTDetSearch() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String OrgId = "";//父机构（销售单位）ID

            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                OrgId = Constant.OEM_ACTIVITIES;
            } else {
                OrgId = logonUser.getDealerId();
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.showPartZTStockDT(
                    request, OrgId, 12, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件在途详细信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @Title : 在途 -> 导入
     * @author : wucl
     * LastDate    : 2014-3-27
     */
    public void onwayImportExcel() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        FileObject uploadFile = request.getParamObject("uploadFile");//获取导入文件
        if (uploadFile == null) {//文件为空报空指针异常
            return;
        }
        String fileName = uploadFile.getFileName();//获取文件名
        fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());//截取文件名
        ByteArrayInputStream inputStream = new ByteArrayInputStream(uploadFile.getContent());//获取文件数据

        Workbook wb = null;
        try {
            String orgId = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId() + "";
            wb = Workbook.getWorkbook(inputStream);
            inputStream.reset();
            Sheet[] sheets = wb.getSheets();
            Sheet sheet = sheets[0];
            int rowNum = sheet.getRows();
            for (int j = 1; j < rowNum; j++) {
                Cell[] cells = sheet.getRow(j);
                String partCode = cells[0].getContents().trim();
                String onwayNum = cells[1].getContents().trim();

                List<Map<String, Object>> pdList = dao.selectData(partCode, orgId);
                if (pdList != null && pdList.size() > 0 && pdList.get(0) != null) {
                    Map<String, Object> map = pdList.get(0);
                    String partId = map.get("PART_ID").toString();
                    String whId = map.get("WH_ID").toString();

                    TtPartOnwayPO tpop = new TtPartOnwayPO();
                    tpop.setPartId(Long.valueOf(partId));
                    List<PO> poList = dao.select(tpop);
                    if (poList != null && poList.size() > 0 && poList.get(0) != null) {
                        tpop = (TtPartOnwayPO) poList.get(0);
                    } else {
                        tpop = null;
                    }
                    if (tpop == null) {//之前没有在途的配件，插入数据
                        tpop = new TtPartOnwayPO();
                        String onwayId = SequenceManager.getSequence(null);
                        tpop.setOnwayId(Long.valueOf(onwayId));
                        tpop.setOnQty(Long.valueOf(onwayNum));
                        tpop.setPartId(Long.valueOf(partId));
                        tpop.setWhId(Long.valueOf(whId));
                        tpop.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
                        tpop.setCreateBy(logonUser.getUserId());
                        tpop.setStatus(Integer.valueOf(1));
                        tpop.setState(Constant.STATUS_ENABLE);
                        dao.insert(tpop);
                    } else {//之前已经存在在途的配件，更新在途配件数量
                        TtPartOnwayPO tpop1 = new TtPartOnwayPO();
                        Long updateNum = Long.valueOf(onwayNum) + tpop.getOnQty();
                        tpop1.setOnQty(updateNum);
                        dao.update(tpop, tpop1);
                    }
                }
            }
            this.stockQueryInit();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "在途 -> 批量导入数据失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @author wucl
     * @Title : 在途导入模板
     * LastDate    : 2014-3-7
     */
    public void onwayExcelTemplate() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();
            List<List<Object>> list = new LinkedList<List<Object>>();
            List<Object> listHead = new LinkedList<Object>();
            listHead.add("配件编码");
            listHead.add("在途数量");
            list.add(listHead);
            String fileName = "在途导入模板.xls";
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "在途导入模板 EXECEL模板错误");
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
     * ：zhumingwei 2013-09-17
     *
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-9-17
     * @Title : 配件入库详情
     */
    public void exportPartINDetailExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId")); // 配件ID
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
            String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 封存开始时间
            String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 封存截止时间
            String checkCode = CommonUtils.checkNull(request.getParamValue("checkCode")); // 验货单号
            String inCode = CommonUtils.checkNull(request.getParamValue("inCode")); // 入库单号
            String sumFlag = CommonUtils.checkNull(request.getParamValue("sumFlag")); //汇总标志
            String soCode = CommonUtils.checkNull(request.getParamValue("soCode")); // 销售单号
            String outCode = CommonUtils.checkNull(request.getParamValue("outCode")); // 出库单号
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode")); // 业务单号
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 服务商名称

            StringBuffer sbString = new StringBuffer();
            if (null != partId && !"".equals(partId)) {
                sbString.append(" AND R.PART_ID = '" + partId + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND R.WH_ID = '" + whId + "' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(R.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(R.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
            if (sumFlag.equals("1")) {
                sbString.append(" AND IN_TYPE not LIKE '%移位%'");
            }

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "日期";
            head[2] = "类型";
            head[3] = "单号";
            head[4] = "服务站编码";
            head[5] = "服务站名称";
            head[6] = "入库数量";
            head[7] = "出库数量";
            head[8] = "结存数量";

            PageResult<Map<String, Object>> ps = dao.showPartRCDtl(sbString.toString(), orderCode, outCode, dealerName, 99999, 1);
            List list1 = new ArrayList();
            if (ps != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[2] = CommonUtils.checkNull(map.get("IN_TYPE"));
                        detail[3] = CommonUtils.checkNull(map.get("CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[5] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[6] = CommonUtils.checkNull(map.get("IN_QTY"));
                        detail[7] = CommonUtils.checkNull(map.get("OUT_QTY"));
                        detail[8] = CommonUtils.checkNull(map.get("ITEM_QTY"));
                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件出入库明细";
            this.exportEx(fileName, ActionContext.getContext().getResponse(), request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件出入库明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}
