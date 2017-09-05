package com.infodms.dms.actions.parts.baseManager.partSTOSet;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partPlannerWarehouseManager.partPlannerWarehouseDao;
import com.infodms.dms.dao.parts.baseManager.partSTOSet.partSTOSetDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartStoDefinePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理配件直发条件设置业务 (注：供货商从制造商表获取)
 * @Description:CHANADMS
 * @Copyright: Copyright (c) 2010
 * @Date: 2013-7-6
 * @remark
 */
public class partSTOSetAction {
    public Logger logger = Logger.getLogger(partSTOSetAction.class);
    private static final partSTOSetDao dao = partSTOSetDao.getInstance();

    private static final String PART_STO_SET_MAIN = "/jsp/parts/baseManager/partSTOSet/partSTOSetMain.jsp";//配件直发条件设置首页
    private static final String PART_STO_SET_MOD = "/jsp/parts/baseManager/partSTOSet/partSTOSetMod.jsp";//配件直发条件设置页面
    private static final String PART_SELECT_PAGE = "/jsp/parts/baseManager/partSTOSet/queryPartsForMod.jsp";//配件选择页面
    private static final String PART_STO_SET_ADD = "/jsp/parts/baseManager/partSTOSet/partSTOSetAdd.jsp";//配件直发条件设置ADD页面
    private static final String PART_ADD_PAGE = "/jsp/parts/baseManager/partSTOSet/queryPartsForAdd.jsp";//配件选择ADD页面
    private static final String VENDER_SELECT_PAGE = "/jsp/parts/baseManager/partSTOSet/venderForAdd.jsp";//供应商选择页面

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 跳转至配件直发条件设置页面
     */
    public void partSTOSetInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            act.setForword(PART_STO_SET_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件直发条件设置初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 按条件查询配件直发条件信息
     */
    public void partSTOSetSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode")); // 配件件号
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("partName")); // 配件名称
            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));// 供应商ID
            String venderName = CommonUtils.checkNull(request.getParamValue("vender_name")); // 供应商名称
            String brand = CommonUtils.checkNull(request.getParamValue("brand_search")); // 品牌分类
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));// 是否有效
            String brandMod = CommonUtils.checkNull(request.getParamValue("brand")); // 品牌分类 for Mod

            StringBuffer sql = new StringBuffer();

            if (null != partCode && !"".equals(partCode)) {
                sql.append(" AND UPPER(SD.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
            }

            if (null != partOldcode && !"".equals(partOldcode)) {
                sql.append(" AND UPPER(SD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
            }
            if (null != partName && !"".equals(partName)) {
                sql.append(" AND SD.PART_NAME LIKE '%" + partName + "%' ");
            }

            if (null != venderId && !"".equals(venderId)) {
                sql.append(" AND SD.VENDER_ID = '" + venderId + "' ");
            }

            if (null != venderName && !"".equals(venderName)) {
                sql.append(" AND SD.VENDER_NAME LIKE '%" + venderName + "%' ");
            }
            if (null != brand && !"".equals(brand)) {
                sql.append(" AND SD.BRAND LIKE '%" + brand + "%' ");
            }

            if (null != brandMod && !"".equals(brandMod)) {
                sql.append(" AND SD.BRAND = '" + brandMod + "' ");
            }

            if (null != state && !"".equals(state)) {
                sql.append(" AND SD.STATE = '" + state + "' ");
            }
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = dao.queryPartSTOSet(sql.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询人员类型信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-8
     * @Title : 失效配件直发条件设置
     */
    public void celPartSTOSet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String defineId = CommonUtils.checkNull(request.getParamValue("disabeParms"));// 关系ID
            Long userId = logonUser.getUserId();// 操作用户ID

            //TtPartStoDefinePO
            TtPartStoDefinePO selPO = new TtPartStoDefinePO();
            TtPartStoDefinePO updatePO = new TtPartStoDefinePO();

            selPO.setDeftId(Long.parseLong(defineId));

            updatePO.setUpdateBy(userId);
            updatePO.setUpdateDate(new Date());
            updatePO.setDisableBy(userId);
            updatePO.setDisableDate(new Date());
            updatePO.setState(Constant.STATUS_DISABLE);

            dao.update(selPO, updatePO);

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "失效配件直发条件设置失败 ");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 有效配件直发条件设置
     */
    public void enablePartSTOSet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String defineId = CommonUtils.checkNull(request.getParamValue("enableParms"));// 关系ID
            Long userId = logonUser.getUserId();// 操作用户ID
            partPlannerWarehouseDao dao = partPlannerWarehouseDao.getInstance();

            //TtPartStoDefinePO
            TtPartStoDefinePO selPO = new TtPartStoDefinePO();
            TtPartStoDefinePO updatePO = new TtPartStoDefinePO();

            selPO.setDeftId(Long.parseLong(defineId));

            updatePO.setUpdateBy(userId);
            updatePO.setUpdateDate(new Date());
            updatePO.setState(Constant.STATUS_ENABLE);

            dao.update(selPO, updatePO);

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "有效配件直发条件设置失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-6
     * @Title : 更新配件直发条件设置
     */
    public void updatePartSTOSet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            Long userId = logonUser.getUserId();// 操作用户ID
            Date date = new Date();
            String defineIds[] = request.getParamValues("defineIds");// 关系ID
            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));// 供应商ID
            String venderName = CommonUtils.checkNull(request.getParamValue("venderName")); // 供应商名称
            String prvBrand = CommonUtils.checkNull(request.getParamValue("prvBrand")); // 品牌分类
            String brand = CommonUtils.checkNull(request.getParamValue("brand")); // 品牌分类
            String criterion = CommonUtils.checkNull(request.getParamValue("criterion")); // 最小订货箱数

            //TtPartStoDefinePO
            TtPartStoDefinePO selPO = null;
            TtPartStoDefinePO updatePO = null;

            selPO = new TtPartStoDefinePO();
            updatePO = new TtPartStoDefinePO();

            selPO.setBrand(prvBrand);
            selPO.setVenderId(Long.parseLong(venderId));

            updatePO.setBrand(brand);
            updatePO.setCriterion(Integer.parseInt(criterion));
            updatePO.setUpdateBy(userId);
            updatePO.setUpdateDate(date);

            dao.update(selPO, updatePO);

            Thread.sleep(200);

            if (null != defineIds && defineIds.length > 0) {
                for (int i = 0; i < defineIds.length; i++) {
                    String deftId = defineIds[i];// 序列ID
                    String minPkg = request.getParamValue("minPkg_" + deftId);//最小包装数

                    selPO = new TtPartStoDefinePO();
                    updatePO = new TtPartStoDefinePO();

                    selPO.setDeftId(Long.parseLong(deftId));

                    updatePO.setMinPkg(Integer.parseInt(minPkg));

                    dao.update(selPO, updatePO);

                }
            }

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "更新配件人员类型关系");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 跳转至配件直发条件设置修改页面
     */
    public void partSTOSetFormodInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();

        try {
            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));// 供应商ID
            String venderName = CommonUtils.checkNull(request.getParamValue("venderName"));// 供应商名称
            String brand = CommonUtils.checkNull(request.getParamValue("brand"));// 品牌分类
            String criterion = CommonUtils.checkNull(request.getParamValue("criterion"));// 最小订货箱数
            String defineIdMod = CommonUtils.checkNull(request.getParamValue("defineIdMod"));// 最小订货箱数

            act.setOutData("venderId", venderId);
            act.setOutData("venderName", venderName);
            act.setOutData("brand", brand);
            act.setOutData("criterion", criterion);
            act.setOutData("defineIdMod", defineIdMod);
            act.setForword(PART_STO_SET_MOD);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件直发条件设置修改初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 跳转至配件直发条件设置新增页面
     */
    public void partSTOSetAddInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            act.setForword(PART_STO_SET_ADD);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件直发条件设置新增初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件信息弹出框 for Modify
     */
    public void queryPartsInit() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String defineIdMod = CommonUtils.checkNull(request.getParamValue("defineIdMod"));
            String venderId = "";
            String brand = "";
            String criterion = "";
            String venderName = "";

            String sqlStr = " AND SD.DEFT_ID = '" + defineIdMod + "'";
            List<Map<String, Object>> sdList = dao.queryPartSTOSetList(sqlStr);

            if (null != sdList && sdList.size() > 0) {
                venderId = sdList.get(0).get("VENDER_ID").toString();
                brand = sdList.get(0).get("BRAND").toString();
                criterion = sdList.get(0).get("CRITERION").toString();
                venderName = sdList.get(0).get("VENDER_NAME").toString();
            }

            act.setOutData("venderId", venderId);
            act.setOutData("brand", brand);
            act.setOutData("criterion", criterion);
            act.setOutData("venderName", venderName);
            act.setForword(PART_SELECT_PAGE);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件信息初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件信息弹出框 for Add
     */
    public void queryPartsForAddInit() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            act.setForword(PART_ADD_PAGE);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件信息初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-8
     * @Title : 获取供货单位
     */
    public void queryVendersForAddInit() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            act.setForword(VENDER_SELECT_PAGE);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件信息初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 返回配件信息
     */
    public void queryPartsDialog() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partolcode = CommonUtils.checkNull(request.getParamValue("partolcode")); // 配件编码
            String partcname = CommonUtils.checkNull(request.getParamValue("partcname")); // 配件名称

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            String oemId = Constant.OEM_ACTIVITIES;//主机厂ID
            PageResult<Map<String, Object>> ps = dao.getParts(partolcode, partcname,
                    13, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询人员信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 返回配件替换信息
     */
    public void queryReplacedPartsDialog() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partolcode = CommonUtils.checkNull(request.getParamValue("partolcode")); // 配件编码
            String partcname = CommonUtils.checkNull(request.getParamValue("partcname")); // 配件名称

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartException(partolcode, partcname, "", 13, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询人员信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-8
     * @Title : 获取供货商信息 (从制造商表 获取)
     */
    public void partVendersQuery() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String venderCode = CommonUtils.checkNull(request.getParamValue("VEND_CODE")); // 配件编码
            String venderName = CommonUtils.checkNull(request.getParamValue("VEND_NAME")); // 配件名称
            StringBuffer sql = new StringBuffer();
            if (null != venderCode && !"".equals(venderCode)) {
                sql.append(" AND UPPER(MD.MAKER_CODE) LIKE '%" + venderCode.toUpperCase() + "%' ");
            }

            if (null != venderName && !"".equals(venderName)) {
                sql.append(" AND MD.MAKER_NAME LIKE '%" + venderName + "%' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            String oemId = Constant.OEM_ACTIVITIES;//主机厂ID
            PageResult<Map<String, Object>> ps = dao.getVenders(sql.toString(), 13, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询供货商信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 新增人员类型关系
     */
    public void insertPartSTOSet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String errorExist = "";
        try {
            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));
            String brand = CommonUtils.checkNull(request.getParamValue("brand"));
            String criterion = CommonUtils.checkNull(request.getParamValue("criterion"));
            String venderName = CommonUtils.checkNull(request.getParamValue("venderName"));

            String parts = CommonUtils.checkNull(request.getParamValue("parts")); //新增配件

            Long userId = logonUser.getUserId();// 用户ID
            Date date = new Date();

            // TtPartStoDefinePO
            TtPartStoDefinePO insertPO = null;

            String partsArr[] = parts.split(",");
            for (int i = 0; i < partsArr.length; i++) {
                List existlist = dao.getExistPO(venderId, brand, partsArr[i].toString());
                if (existlist != null && existlist.size() > 0) {
                    TtPartStoDefinePO sdPo = (TtPartStoDefinePO) existlist.get(0);
                    errorExist += sdPo.getPartName() + " ";
                }

            }
            if ("".equals(errorExist)) {
                for (int i = 0; i < partsArr.length; i++) {
                    String partId = partsArr[i];
                    String sqlStr = " AND PD.PART_ID = '" + partId + "' ";
                    List<Map<String, Object>> partList = dao.getPartList(sqlStr);
                    String partCode = partList.get(0).get("PART_CODE").toString();
                    String partOldcode = partList.get(0).get("PART_OLDCODE").toString();
                    String partName = partList.get(0).get("PART_CNAME").toString();
                    String minPkg = CommonUtils.checkNull(request.getParamValue("minPkg_" + partId));
                    int minPkgInt = 1;
                    if (null != minPkg && !"".equals(minPkg)) {
                        minPkgInt = Integer.parseInt(minPkg);
                    }

                    insertPO = new TtPartStoDefinePO();
                    insertPO.setDeftId(Long.parseLong(SequenceManager.getSequence("")));
                    insertPO.setVenderId(Long.parseLong(venderId));
                    insertPO.setVenderName(venderName);
                    insertPO.setBrand(brand);
                    insertPO.setCriterion(Integer.parseInt(criterion));
                    insertPO.setPartId(Long.parseLong(partId));
                    insertPO.setPartCode(partCode);
                    insertPO.setPartOldcode(partOldcode);
                    insertPO.setPartName(partName);
                    insertPO.setMinPkg(minPkgInt);//默认 1
                    insertPO.setCreateBy(userId);
                    insertPO.setCreateDate(date);

                    dao.insert(insertPO);
                }
            }

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("errorExist", errorExist);// 关系记录存在
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "新增配件直发条件");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    public void exportPartSTOExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode")); // 配件件号
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("partName")); // 配件名称
            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));// 供应商ID
            String venderName = CommonUtils.checkNull(request.getParamValue("vender_name")); // 供应商名称
            String brand = CommonUtils.checkNull(request.getParamValue("brand_search")); // 品牌分类
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));// 是否有效

            StringBuffer sql = new StringBuffer();

            if (null != partCode && !"".equals(partCode)) {
                sql.append(" AND UPPER(SD.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
            }

            if (null != partOldcode && !"".equals(partOldcode)) {
                sql.append(" AND UPPERSD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
            }
            if (null != partName && !"".equals(partName)) {
                sql.append(" AND SD.PART_NAME LIKE '%" + partName + "%' ");
            }

            if (null != venderId && !"".equals(venderId)) {
                sql.append(" AND SD.VENDER_ID = '" + venderId + "' ");
            }

            if (null != venderName && !"".equals(venderName)) {
                sql.append(" AND SD.VENDER_NAME LIKE '%" + venderName + "%' ");
            }
            if (null != brand && !"".equals(brand)) {
                sql.append(" AND SD.BRAND LIKE '%" + brand + "%' ");
            }

            if (null != state && !"".equals(state)) {
                sql.append(" AND SD.STATE = '" + state + "' ");
            }

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "件号";
            head[2] = "配件编码";
            head[3] = "配件名称";
            head[4] = "供货商名称";
            head[5] = "最小包装数";
            head[6] = "品牌分类";
            head[7] = "最小订货箱数";
            head[8] = "是否有效";

            List<Map<String, Object>> list = dao.queryPartSTOSetList(sql.toString());

            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[3] = CommonUtils.checkNull(map.get("PART_NAME"));
                        detail[4] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("MIN_PKG"));
                        detail[6] = CommonUtils.checkNull(map.get("BRAND"));
                        detail[7] = CommonUtils.checkNull(map.get("CRITERION"));

                        int stateInt = Integer.parseInt(CommonUtils.checkNull(map.get("STATE")));
                        int stateEnable = Constant.STATUS_ENABLE;
                        if (stateEnable == stateInt) {
                            detail[8] = "有效";
                        } else {
                            detail[8] = "无效";
                        }

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件直发条件设置信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件库存盘点调整申请");
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
