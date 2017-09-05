package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartLocationDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartLoactionDefinePO;
import com.infodms.dms.po.TtPartLoactionHistoryPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;

/**
 * @author : luole CreateDate : 2013-4-3
 * @ClassName : PartLocation
 * @Description : 配件货位维护
 */
public class PartLocation extends BaseImport {
    public Logger logger = Logger.getLogger(PartLocation.class);
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    PartLocationDao dao = PartLocationDao.getInstance();
    private final String PART_LOCATION_INIT_URL = "/jsp/parts/baseManager/partsBaseManager/partLocation/partLocationInit.jsp"; // 配件货位维护初始化页面
    private final String PART_LOCATION_ADD_URL = "/jsp/parts/baseManager/partsBaseManager/partLocation/partLocationAdd.jsp"; // 新增初始化页面
    private final String SEL_PART_URL = "/jsp/parts/baseManager/partsBaseManager/selPartInit.jsp";                  // 新增初始化页面
    private final String SEL_WH_URL = "/jsp/parts/baseManager/partsBaseManager/selWhInit.jsp";                    // 新增初始化页面
    private final String PART_LOCATION_UPDATE_URL = "/jsp/parts/baseManager/partsBaseManager/partLocation/partLocationMod.jsp"; // 修改初始化页面
    private static final String INPUT_ERROR_URL = "/jsp/parts/baseManager/partsBaseManager/partLocation/inputError.jsp";//数据导入出错页面
    private static final String MAG_SEL_URL = "/jsp/parts/baseManager/partsBaseManager/partLocation/magSelectSingle.jsp";//库管员选择页面
    private final String LOCATION_INIT = "/jsp/parts/storageManager/partLocationMgr/selectLocation.jsp"; // 货位选择界面
    
    /**
     * <p>Description: 货位选择--跳转到货位选择窗口</p>
     */
    public void selectLocationInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));
//            if(!CommonUtils.isEmpty(partCname)){
                //partCname = new String(partCname.getBytes("iso8859-1"), "utf-8");
//            }
            String partLocId = CommonUtils.checkNull(request.getParamValue("partLocId"));
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
            act.setOutData("partId", partId);
            act.setOutData("partLocId", partLocId);
            act.setOutData("partOldcode", partOldcode);
            act.setOutData("partCname", partCname);
            act.setOutData("whId", whId);
            act.setForword(this.LOCATION_INIT);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "货位选择--查询货位信息失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * <p>Description: 查询货位</p>
     */
    public void selectLocation() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String locCode = CommonUtils.checkNull(request.getParamValue("locCode"));
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
            System.out.println("whId:"+whId);
            Map<String, String> map = new HashMap<String, String>();
            map.put("whId", whId);
            map.put("locCode", locCode);
            map.put("partId", partId);

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
            PageResult<Map<String, Object>> ps = dao.queryLocation(map, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "货位选择--查询货位信息失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-3
     * @Title : 初始化
     */
    public void partLocationInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        long orgId = 0l;
        try {
            if (logonUser.getDealerId() == null) {
                orgId = logonUser.getOrgId();
            } else {
                orgId = new Long(logonUser.getDealerId());
            }
            List<Map<String, Object>> list = dao.getPartWareHouse(orgId);
            act.setOutData("list", list);
            act.setOutData("orgId", orgId);
            act.setForword(PART_LOCATION_INIT_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件货位维护查询页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-3
     * @Title : 新增初始化
     */
    public void partLocationAddInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PART_LOCATION_ADD_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件货位维护新增页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-3
     * @Title : 新增
     */
    public void savePartLocation() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
            	
                parentOrgId = Constant.OEM_ACTIVITIES;
            } else {
                parentOrgId = logonUser.getDealerId();
            }

            String partId = CommonUtils.checkNull(request.getParamValue("PART_ID")); // 配件ID
            String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME")); // 配件ID
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID")); // 仓库ID
            String whCode = CommonUtils.checkNull(request.getParamValue("WH_CODE")); // 配件CODE
            String locCode = CommonUtils.checkNull(request.getParamValue("LOC_CODE")).trim();// 货位CODE
            String locName = CommonUtils.checkNull(request.getParamValue("LOC_NAME")).trim();// 货位NAME
            String subLoc = request.getParamValue("SUB_LOC");
            boolean flag = true;
            String errorinfo = null;
            TtPartLoactionDefinePO po1 = new TtPartLoactionDefinePO();
            po1.setWhId(Long.parseLong(whId));
			po1.setLocCode(locCode);
			List<PO> areaList1 = dao.select(po1);
			if (areaList1.size() != 0) {
				flag = false;
				errorinfo = "货位编码重复,请重新输入!";
			}
			TtPartLoactionDefinePO po2 = new TtPartLoactionDefinePO();
			po2.setWhId(Long.parseLong(whId));
			po2.setLocName(locName);
			List<PO> areaList2 = dao.select(po2);
			if (areaList2.size() != 0) {
				flag = false;
				errorinfo = errorinfo + "  货位名称重复,请重新输入!";
			}
            TtPartLoactionDefinePO po3 = new TtPartLoactionDefinePO();
            po3.setOrgId(Long.parseLong(parentOrgId));
            po3.setWhId(Long.parseLong(whId));
            po3.setPartId(Long.parseLong(partId));
			  po3.setState(Constant.STATUS_ENABLE);
			  po3.setLocCode(locCode);
            List<PO> areaList3 = dao.select(po3);
            if (areaList3.size() != 0) {
                flag = false;
                errorinfo = " 配件[" + partName + "]货位["+locCode+"]已经存在，不能新增!";
            }
            if (flag) {
                TtPartLoactionDefinePO po = new TtPartLoactionDefinePO();
                po.setLocId(Long.parseLong(SequenceManager.getSequence("")));
                po.setPartId(Long.parseLong(partId));
                po.setWhId(Long.parseLong(whId));
                po.setLocCode(locCode);
                po.setLocName(locName);
                po.setCreateBy(logonUser.getUserId());
                po.setCreateDate(new Date());
                po.setSubLoc(subLoc);
                po.setState(Constant.STATUS_ENABLE);
                po.setStatus(Constant.IF_TYPE_YES);
//				OrgBean obl = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0);

                po.setOrgId(Long.parseLong(parentOrgId));

				dao.insert(po);
                act.setOutData("success", "success");
            } else {
                act.setOutData("error", errorinfo);
            }
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件货位新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-3
     * @Title : 查询
     */
    public void partLocationQuery() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.getResponse().setContentType("application/json");
            String partCode = request.getParamValue("PART_CODE"); // 件号
            String partOldcode = request.getParamValue("PART_OLDCODE"); // 配件编码
            String whId = request.getParamValue("WH_ID"); // 仓库ID
            String locCode = request.getParamValue("LOC_CODE"); // 货位编码
            String subCol = request.getParamValue("SUB_COL"); // 附属货位
            String searchType = request.getParamValue("searchType"); // 查询类型
            String partId = request.getParamValue("partId"); // 配件ID

            long orgId = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId();
            StringBuffer sql = new StringBuffer();
            if (!CommonUtils.isNullString(locCode)) {
                sql.append(" AND UPPER(L.LOC_CODE) LIKE '%" + locCode.trim().toUpperCase() + "%' ");
            }
            if (!CommonUtils.isNullString(subCol)) {
                sql.append(" AND L.SUB_LOC LIKE '%" + subCol.trim() + "%' ");
            }
            if (!CommonUtils.isNullString(partCode)) {
                sql.append(" AND  UPPER(D.PART_CODE) LIKE '%" + partCode.trim().toUpperCase() + "%' ");
            }
            if (!CommonUtils.isNullString(partOldcode)) {
                sql.append(" AND  UPPER(D.PART_OLDCODE)  LIKE '%" + partOldcode.trim().toUpperCase() + "%' ");
            }
            if (!CommonUtils.isNullString(whId)) {
                sql.append("  AND L.WH_ID  =  " + whId);
            }
            if (!CommonUtils.isNullString(partId)) {
                sql.append(" AND  D.PART_ID = '" + partId + "' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            if ("normal".equals(searchType)) {
                ps = dao.locPageQuery(sql.toString(), orgId, curPage, Constant.PAGE_SIZE);
            } else {
                ps = dao.locHistoryQuery(sql.toString(), orgId, curPage, Constant.PAGE_SIZE);
            }


            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件货位查询失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-3
     * @Title : 失效
     */
    public void partNotState() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String locId = CommonUtils.checkNull(request.getParamValue("Id")); // 仓库ID
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));
            // System.out.println(locId);
            TtPartLoactionDefinePO po = new TtPartLoactionDefinePO();
            po.setLocId(Long.parseLong(locId));
            po.setState(Constant.STATUS_ENABLE);
            TtPartLoactionDefinePO po1 = new TtPartLoactionDefinePO();
            po1.setState(Constant.STATUS_DISABLE);
            po1.setDisableBy(logonUser.getUserId());
            po1.setDeleteDate(new Date());
            dao.update(po, po1);
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "失效成功");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件仓库维护查询页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-3
     * @Title : 有效
     */
    public void partEnableState() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String locId = CommonUtils.checkNull(request.getParamValue("Id")); // 仓库IDcurPage
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));
            if ("".equals(curPage)) {
                curPage = "1";
            }
            TtPartLoactionDefinePO po = new TtPartLoactionDefinePO();
            po.setLocId(Long.parseLong(locId));
            po.setState(Constant.STATUS_DISABLE);
            TtPartLoactionDefinePO po1 = new TtPartLoactionDefinePO();
            po1.setState(Constant.STATUS_ENABLE);
            po1.setDisableBy(logonUser.getUserId());
            po1.setDeleteDate(new Date());
            dao.update(po, po1);
            act.setOutData("success", "设置有效成功");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件仓库维护查询页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void checkItem_qty() {
        String locId = CommonUtils.checkNull(request.getParamValue("LOC_ID")); // 仓库ID
        List<Map<String, Object>> list2 = dao.getItem_qty(locId);
        if (list2 != null && list2.size() > 0 && list2.get(0) != null) {
            act.setOutData("ITEM_QTY", list2.get(0).get("ITEM_QTY").toString());
        }
    }

    public void moveSeat() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String reloc_id = CommonUtils.checkNull(request.getParamValue("RELOC_ID")); //移库后实际货位ID
            String loc_code = CommonUtils.checkNull(request.getParamValue("LOC_CODE")); //移库货位编码
            String loc_name = CommonUtils.checkNull(request.getParamValue("LOC_NAME")); //移库后货位名称
            String locId = CommonUtils.checkNull(request.getParamValue("LOC_ID")); // 货位ID
            //1.判断配件库存是否为0，为0则不需要要移位
            List<Map<String, Object>> list2 = dao.getItem_qty(locId);
            if (list2 != null && list2.size() > 0 && list2.get(0) != null) {
                String str = list2.get(0).get("ITEM_QTY").toString();
                long itemQty = Long.parseLong(str);
                if (itemQty <= 0) {
                    act.setOutData("returnValue", 3);
                    return;
                }
            } else {
                act.setOutData("returnValue", 3);
                return;
            }
            TtPartLoactionDefinePO tldp = new TtPartLoactionDefinePO();
            tldp.setLocId(Long.parseLong(locId));
            //2.插入修改历史
            List<PO> tldpList = dao.select(tldp);
            tldp = (TtPartLoactionDefinePO) tldpList.get(0);
            TtPartLoactionHistoryPO lhPo = new TtPartLoactionHistoryPO();
            lhPo.setHsId(Long.parseLong(SequenceManager.getSequence("")));
            lhPo.setLocId(Long.parseLong(locId));
            lhPo.setLocCode(loc_code);
            lhPo.setOldLocCode(tldp.getLocCode());
            lhPo.setLocName(tldp.getLocName());
            lhPo.setOldLocName(tldp.getLocName());
            //lhPo.setSubLoc(subLoc);
            //lhPo.setOldSubLoc(lcList.get(0).get("SUB_LOC").toString());
            lhPo.setWhId(tldp.getWhId());
            lhPo.setOrgId(tldp.getOrgId());
            lhPo.setPartId(tldp.getPartId());
            lhPo.setWhmanId(tldp.getWhmanId());
            lhPo.setOldWhmanId(tldp.getWhmanId());
            lhPo.setCreateBy(logonUser.getUserId());
            lhPo.setCreateDate(new Date());
            dao.insert(lhPo);
            act.setOutData("returnValue", 1);
            //3.移位--更新位置信息
            TtPartLoactionDefinePO t1 = new TtPartLoactionDefinePO();
            t1.setRelocId(Long.parseLong(reloc_id));
            t1.setLocCode(loc_code);
            t1.setLocName(loc_name);
            TtPartLoactionDefinePO tldp2 = new TtPartLoactionDefinePO();
            tldp2.setLocId(Long.parseLong(locId));
            dao.update(tldp2, t1);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件移位");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-3
     * @Title : 修改
     */
    public void updatePartLocation() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.getResponse().setContentType("application/json");
            String locId = CommonUtils.checkNull(request.getParamValue("LOC_ID")); // 仓库ID
            String locCode = CommonUtils.checkNull(request.getParamValue("LOC_CODE"));// 货位CODE
            String locName = CommonUtils.checkNull(request.getParamValue("LOC_CODE"));// 货位NAME
            String subLoc = CommonUtils.checkNull(request.getParamValue("SUB_LOC"));//附属货位
            String whManId = CommonUtils.checkNull(request.getParamValue("whManId"));// 库管员ID
//			String minPkg = CommonUtils.checkNull(request.getParamValue("MIN_PKG"));// 最小包装量

            boolean flag = true;
            String errorinfo = null;
            /*TtPartLoactionDefinePO po4 = new TtPartLoactionDefinePO();
            po4.setLocId(Long.parseLong(locId));
			List<PO> areaList4 = dao.select(po4);
			po4 = (TtPartLoactionDefinePO) areaList4.get(0);
			TtPartLoactionDefinePO po1 = new TtPartLoactionDefinePO();
			po1.setWhId(po4.getWhId());
			po1.setLocCode(locCode);
			po1.setPartId(po4.getPartId());
			
			List<PO> areaList1 = dao.select(po1);
			if (areaList1.size() >1) {
				po1 = (TtPartLoactionDefinePO) areaList1.get(0);
				if (!locId.equals(po1.getLocId() + "")) {
					flag = false;
					errorinfo = "货位编码重复,请重新输入!";
				}
			}*/
			/*TtPartLoactionDefinePO po2 = new TtPartLoactionDefinePO();
			po2.setWhId(po4.getWhId());
			po2.setLocName(locName);
			po2.setPartId(po4.getPartId());
			List<PO> areaList2 = dao.select(po2);
			if (areaList2.size() > 1) {
				po2 = (TtPartLoactionDefinePO) areaList1.get(0);
				if (!locId.equals(po2.getLocId() + "")) {
					flag = false;
					errorinfo = errorinfo + "  货位名称重复,请重新输入!";
				}
			}*/
            if (flag) {
                Date date = new Date();
                String sqlStr = " AND L.LOC_ID = '" + locId + "'";

                List<Map<String, Object>> lcList = dao.locInfoList(sqlStr);

                //插入修改历史
                TtPartLoactionHistoryPO lhPo = new TtPartLoactionHistoryPO();

                lhPo.setHsId(Long.parseLong(SequenceManager.getSequence("")));
                lhPo.setLocId(Long.parseLong(locId));
                lhPo.setLocCode(locCode);
                if (null != lcList.get(0).get("LOC_CODE") && !"".equals(lcList.get(0).get("LOC_CODE"))) {
                    lhPo.setOldLocCode(lcList.get(0).get("LOC_CODE").toString());
                }
                lhPo.setLocName(locName);
                if (null != lcList.get(0).get("LOC_NAME") && !"".equals(lcList.get(0).get("LOC_NAME"))) {
                    lhPo.setOldLocName(lcList.get(0).get("LOC_NAME").toString());
                }
                lhPo.setSubLoc(subLoc);
                if (null != lcList.get(0).get("SUB_LOC") && !"".equals(lcList.get(0).get("SUB_LOC"))) {
                    lhPo.setOldSubLoc(lcList.get(0).get("SUB_LOC").toString());
                }
                lhPo.setWhId(Long.parseLong(lcList.get(0).get("WH_ID").toString()));
                lhPo.setOrgId(Long.parseLong(lcList.get(0).get("ORG_ID").toString()));
                lhPo.setPartId(Long.parseLong(lcList.get(0).get("PART_ID").toString()));
                if (null != whManId && !"".equals(whManId) && !"null".equals(whManId)) {
                    lhPo.setWhmanId(Long.parseLong(whManId));

                    if (null != lcList.get(0).get("WHMAN_ID") && !"".equals(lcList.get(0).get("WHMAN_ID"))) {
                        lhPo.setOldWhmanId(Long.parseLong(lcList.get(0).get("WHMAN_ID").toString()));
                    }
                }
                lhPo.setCreateBy(logonUser.getUserId());
                lhPo.setCreateDate(date);

                dao.insert(lhPo);

                TtPartLoactionDefinePO po3 = new TtPartLoactionDefinePO();
                po3.setLocId(Long.parseLong(locId));
                TtPartLoactionDefinePO po = new TtPartLoactionDefinePO();
                po.setLocCode(locCode);
                po.setLocName(locName);
                po.setSubLoc(subLoc);
//				po.setMinPkg(Long.parseLong(minPkg));
                if (null != whManId && !"".equals(whManId) && !"null".equals(whManId)) {
                    po.setWhmanId(Long.parseLong(whManId));
                }
                po.setUpdateBy(logonUser.getUserId());
                po.setUpdateDate(date);
                dao.update(po3, po);

                act.setOutData("success", "success");
            } else {
                act.setOutData("error", errorinfo);
            }
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件货位新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-3
     * @Title : 修改初始化页面
     */
    public void partTypeUpdateInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        boolean salerFlag = false;
        try {
            String id = CommonUtils.checkNull(request.getParamValue("Id"));
            String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));

            TtPartLoactionDefinePO po = new TtPartLoactionDefinePO();
            po.setLocId(Long.parseLong(id));
            List<PO> areaList = dao.select(po);
            if (areaList.size() != 0) {
                po = (TtPartLoactionDefinePO) areaList.get(0);
            }
            act.setOutData("po", po);
            Map<String, Object> map = dao.selInfo(po.getLocId());

            String str = " AND L.LOC_ID = '" + id + "' ";

            List<Map<String, Object>> whManList = dao.getUsers(str);
            Map<String, Object> whManMap = null;
            if (null != whManList && whManList.size() == 1) {
                whManMap = whManList.get(0);
            } else {
                whManMap = new HashMap<String, Object>() {{
                    put("USER_ID", "");
                    put("NAME", "");
                }};
            }

            //zhumingwei 2013-09-11 begin
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            if (dealerId.equals(Constant.OEM_ACTIVITIES)) {
                salerFlag = true;
            }
            List whmans = dao.queryWhmanInfo();
            act.setOutData("salerList", whmans);
            act.setOutData("salerFlag", salerFlag);
            act.setOutData("curUserId", logonUser.getUserId());
            //zhumingwei 2013-09-11 begin

            act.setOutData("mapInfo", map);
            act.setOutData("orgId", orgId);
            act.setOutData("whManMap", whManMap);
            act.setForword(PART_LOCATION_UPDATE_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件货位修改页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-3
     * @Title : 配件选择初始化
     */
    public void selPartInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(SEL_PART_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件货位维护新增页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-3
     * @Title : 配件选择
     */
    public void partQuery() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.getResponse().setContentType("application/json");
            String partCode = request.getParamValue("PART_CODE");
            String partName = request.getParamValue("PART_NAME");
            StringBuffer sql = new StringBuffer();
            if (!CommonUtils.isNullString(partCode)) {
                sql.append(" AND UPPER(TP.PART_OLDCODE) LIKE '%" + partCode.trim().toUpperCase() + "%' ");
            }
            if (!CommonUtils.isNullString(partName)) {
                sql.append(" AND TP.PART_CNAME LIKE '%" + partName.trim() + "%' ");
            }
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.selPartPageQuery(sql.toString(), curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件货位维护新增页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-3
     * @Title : 配件仓库选择初始化
     */
    public void selWhInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(SEL_WH_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件货位维护新增页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-3
     * @Title : 配件仓库选择
     */
    public void whQuery() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.getResponse().setContentType("application/json");
            String whCode = request.getParamValue("WH_CODE");
            String whName = request.getParamValue("WH_NAME");
            StringBuffer sql = new StringBuffer();
            if (!CommonUtils.isNullString(whCode)) {
                sql.append(" AND UPPER(TPWD.WH_CODE) LIKE '%" + whCode.trim().toUpperCase() + "%' ");
            }
            if (!CommonUtils.isNullString(whName)) {
                sql.append(" AND TPWD.WH_NAME LIKE '%" + whName + "%' ");
            }
            sql.append("  and tpwd.org_id=" + PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId());
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.selWhPageQuery(sql.toString(), curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件货位维护新增页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-24
     * @Title : 库管员信息
     */
    public void partManagerSelect() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer sb = new StringBuffer();
        try {
            RequestWrapper request = act.getRequest();
            act.getResponse().setContentType("application/json");
            if ("1".equals(request.getParamValue("query"))) { // 开始查询
                String managerName = request.getParamValue("managerName");// 用户名
                String userPost = "库管员";//人员类型：库管员

                if (Utility.testString(managerName)) {
                    sb.append(" and U.NAME LIKE '%" + managerName + "%' \n");
                }
                sb.append(" AND PF.FIX_NAME ='" + userPost + "' \n");
                Integer curPage = request.getParamValue("curPage") != null ? Integer
                        .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
                PageResult<Map<String, Object>> ps = dao.getManagerList(Constant.PAGE_SIZE, curPage, sb.toString());
                act.setOutData("ps", ps);
            } else {
                act.setForword(MAG_SEL_URL);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "返回库管员信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-6
     * @Title : 导出货位批量导入 EXECEL模板
     */
    public void exportExcelTemplate() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();
            String comp = logonUser.getOemCompanyId();

            List<List<Object>> list = new LinkedList<List<Object>>();
            //标题
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
            listHead.add("配件编码");
            listHead.add("货位编码 ");
            listHead.add("附属货位");

//            if (null == comp) {
//                listHead.add("库管员账号");
//            }

            list.add(listHead);

            // 导出的文件名
            String fileName = "货位批量更新模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "导出货位批量导入 EXECEL模板错误");
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
     * @param : @param locList
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 批量更新保存
     */
    public void savePartLocation(List<Map<String, String>> locList) {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String errors = "";
        try {
            if (null != locList && locList.size() > 0) {
                TtPartLoactionDefinePO selPo = null;
                TtPartLoactionDefinePO updPo = null;
                Long userId = logonUser.getUserId();
                Date date = new Date();
                int listSize = locList.size();
                for (int i = 0; i < listSize; i++) {
                    String partId = locList.get(i).get("partId"); // 配件ID
                    String partoldCode = locList.get(i).get("partoldCode"); // 配件编码
                    String whId = locList.get(i).get("whId"); // 仓库ID
                    String locCode = locList.get(i).get("locCode");// 货位CODE
                    String locName = locList.get(i).get("locName");// 货位NAME
                    String subLoc = locList.get(i).get("subLoc");//附属货位
                    String orgId = locList.get(i).get("orgId");//orgId
                    String whManId = locList.get(i).get("whManId");//库管员ID
//					String minPkg = locList.get(i).get("minPkg");//整包发运量

                    boolean flag = true;
                    String errorinfo = "";
                    if (!"".equals(partId)) {
                        TtPartLoactionDefinePO po3 = new TtPartLoactionDefinePO();
                        po3.setOrgId(Long.parseLong(orgId));
                        po3.setWhId(Long.parseLong(whId));
                        po3.setPartId(Long.parseLong(partId));
//						po3.setState(Constant.STATUS_ENABLE);
                        List<PO> areaList3 = dao.select(po3);
                        if (areaList3.size() != 1) {
                            flag = false;
                            errorinfo = " 配件编码[" + partoldCode + "]货位不存在，不能进行更新!<br/>";
                        }
                        if (flag) {

                            String sqlStr = " AND L.WH_ID = '" + whId + "' AND L.ORG_ID = '" + orgId + "' AND L.PART_ID = '" + partId + "'";

                            List<Map<String, Object>> lcList = dao.locInfoList(sqlStr);

                            TtPartLoactionHistoryPO lhPo = new TtPartLoactionHistoryPO();

                            lhPo.setHsId(Long.parseLong(SequenceManager.getSequence("")));

                            if (null != lcList.get(0).get("LOC_ID") && !"".equals(lcList.get(0).get("LOC_ID"))) {
                                lhPo.setLocId(Long.parseLong(lcList.get(0).get("LOC_ID").toString()));
                            }
                            lhPo.setLocCode(locCode);
                            if (null != lcList.get(0).get("LOC_CODE") && !"".equals(lcList.get(0).get("LOC_CODE"))) {
                                lhPo.setOldLocCode(lcList.get(0).get("LOC_CODE").toString());
                            }
                            lhPo.setLocName(locName);
                            if (null != lcList.get(0).get("LOC_NAME") && !"".equals(lcList.get(0).get("LOC_NAME"))) {
                                lhPo.setOldLocName(lcList.get(0).get("LOC_NAME").toString());
                            }
                            lhPo.setSubLoc(subLoc);
                            if (null != lcList.get(0).get("SUB_LOC") && !"".equals(lcList.get(0).get("SUB_LOC"))) {
                                lhPo.setOldSubLoc(lcList.get(0).get("SUB_LOC").toString());
                            }
                            lhPo.setWhId(Long.parseLong(lcList.get(0).get("WH_ID").toString()));
                            lhPo.setOrgId(Long.parseLong(lcList.get(0).get("ORG_ID").toString()));
                            lhPo.setPartId(Long.parseLong(lcList.get(0).get("PART_ID").toString()));
                            if (null != whManId && !"".equals(whManId) && !"null".equals(whManId)) {
                                lhPo.setWhmanId(Long.parseLong(whManId));

                                if (null != lcList.get(0).get("WHMAN_ID") && !"".equals(lcList.get(0).get("WHMAN_ID"))) {
                                    lhPo.setOldWhmanId(Long.parseLong(lcList.get(0).get("WHMAN_ID").toString()));
                                }
                            }
                            lhPo.setCreateBy(logonUser.getUserId());
                            lhPo.setCreateDate(date);

                            dao.insert(lhPo);

                            selPo = new TtPartLoactionDefinePO();
                            updPo = new TtPartLoactionDefinePO();

                            selPo.setWhId(Long.parseLong(whId));
                            selPo.setOrgId(Long.parseLong(orgId));
                            selPo.setPartId(Long.parseLong(partId));

                            updPo.setLocCode(locCode);
                            updPo.setLocName(locName);
                            updPo.setSubLoc(subLoc);
//							updPo.setMinPkg(Long.parseLong(minPkg));
                            updPo.setUpdateBy(userId);
                            updPo.setUpdateDate(date);
                            if (null != whManId && !"".equals(whManId)) {
                                updPo.setWhmanId(Long.parseLong(whManId));
                            }
                            updPo.setState(Constant.STATUS_ENABLE);

                            dao.update(selPo, updPo);
                        } else {
                            errors += errorinfo;
                        }
                    }
                }
            }
//			act.setOutData("success", "success");
//			act.setOutData("error", errors);
            partLocationInit();
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件货位新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-6
     * @Title : 配件货位 -> 导入文件
     */
    public void locImpUpload() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        try {
            String whId = CommonUtils.checkNull(req.getParamValue("WH_ID")); //仓库ID
            String parentOrgId = "";//父机构（销售单位）ID
            String roleType = "";
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                roleType = "oem";
                parentOrgId = Constant.OEM_ACTIVITIES;
            } else {
                roleType = "server";
                parentOrgId = logonUser.getDealerId();
            }
            List<Map<String, String>> errorInfo = null;
            String err = "";

            errorInfo = new ArrayList<Map<String, String>>();
            long maxSize = 1024 * 1024 * 5;
            int errNum = 0;
            if (null == comp) {
                errNum = insertIntoTmp(request, "uploadFile", 4, 3, maxSize);
            } else {
                errNum = insertIntoTmp(request, "uploadFile", 3, 3, maxSize);
            }


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
                act.setForword(INPUT_ERROR_URL);
            } else {
                List<Map> list = getMapList();
                List<Map<String, String>> voList = new ArrayList<Map<String, String>>();
                loadVoList(voList, list, errorInfo, parentOrgId, whId, roleType);
                if (errorInfo.size() > 0) {
                    act.setOutData("errorInfo", errorInfo);
                    act.setForword(INPUT_ERROR_URL);
                } else {
                    //保存
                    savePartLocation(voList);
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
     * LastDate    : 2013-4-12
     * @Title : 读取CELL
     */
    private void loadVoList(List<Map<String, String>> voList, List<Map> list, List<Map<String, String>> errorInfo, String parentOrgId, String whId, String roleType) {
        if (null == list) {
            list = new ArrayList();
        }
        String userPost = "库管员";
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
                    errormap.put("2", "配件编码");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                } else {
                    List<Map<String, Object>> partCheck = dao.checkOldCode(cells[0].getContents().trim().toUpperCase());
                    if (partCheck.size() == 1) {
                        partIdTmp = partCheck.get(0).get("PART_ID").toString();
                        tempmap.put("partId", partCheck.get(0).get("PART_ID").toString());
                        tempmap.put("partoldCode", partCheck.get(0).get("PART_OLDCODE").toString());
//						tempmap.put("minPkg", partCheck.get(0).get("MIN_PKG").toString());
                    } else {
                        partIdTmp = "";
						/*Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "配件编码【" + cells[0].getContents().trim()+"】 ");
						errormap.put("3", "不存在!");
						errorInfo.add(errormap);*/
                    }
                }

                if (cells.length < 2 || CommonUtils.isEmpty(cells[1].getContents())) {
					/*Map<String, String> errormap = new HashMap<String, String>();
					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
					errormap.put("2", "货位编码");
					errormap.put("3", "为空!");
					errorInfo.add(errormap);*/
                    tempmap.put("locName", "暂无");
                    tempmap.put("locCode", "暂无");
                } else {
                    String accTemp = cells[1].getContents().trim();
                    StringBuffer sb = new StringBuffer();
                    sb.append(" AND LD.WH_ID = '" + whId + "' ");
                    sb.append(" AND LD.ORG_ID = '" + parentOrgId + "' ");
//					sb.append(" AND LD.LOC_NAME = '" + accTemp + "' ");
                    sb.append(" AND LD.PART_ID = '" + partIdTmp + "' ");

                    List<Map<String, Object>> lcCheck = dao.checkLocCode(sb.toString());

                    if (lcCheck.size() == 1) {
                        tempmap.put("locName", accTemp);
                        tempmap.put("locCode", accTemp);
                    } else {
                        partIdTmp = "";
						/*Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "配件货位");
						errormap.put("3", "不存在!");
						errorInfo.add(errormap);*/
                    }
                }
                tempmap.put("subLoc", cells.length < 3 || null == cells[2].getContents() ? "" : cells[2].getContents().trim());
                if ("oem".equals(roleType)) {
                    if (cells.length < 4 || CommonUtils.isEmpty(cells[3].getContents())) {
						/*Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "库管员");
						errormap.put("3", "为空!");
						errorInfo.add(errormap);*/
                        tempmap.put("whManId", "");
                    } else {
                        String accTemp = cells[3].getContents().trim();

                        List<Map<String, Object>> userCheck = dao.getUsers(userPost, accTemp.trim().toUpperCase());

                        if (userCheck.size() == 1) {
                            tempmap.put("whManId", userCheck.get(0).get("USER_ID").toString());
                        } else {
                            partIdTmp = "";
							/*Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "库管员【"+ accTemp +"】");
							errormap.put("3", "无效!");
							errorInfo.add(errormap);*/
                        }
                    }
                }

                tempmap.put("whId", whId);
                tempmap.put("orgId", parentOrgId);
                if (!"".equals(partIdTmp)) {
                    voList.add(tempmap);
                }
            }
        }
    }

    public void exportPartLocExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
            } else {
                parentOrgId = logonUser.getDealerId();
            }
            //String partCode = request.getParamValue("PART_CODE"); // 件号
            String partOldcode = request.getParamValue("PART_OLDCODE"); // 配件编码
            String whId = request.getParamValue("WH_ID"); // 仓库ID
            String locCode = request.getParamValue("LOC_CODE"); // 货位编码
            String subCol = request.getParamValue("SUB_COL"); // 附属货位
            StringBuffer sql = new StringBuffer();
            if (!CommonUtils.isNullString(locCode)) {
                sql.append(" AND UPPER(L.LOC_CODE) LIKE '%" + locCode.trim().toUpperCase() + "%' ");
            }
            if (!CommonUtils.isNullString(subCol)) {
                sql.append(" AND UPPER(L.SUB_LOC) LIKE '%" + subCol.trim().toUpperCase() + "%' ");
            }
//            if (!CommonUtils.isNullString(partCode)) {
//                sql.append(" AND  UPPER(D.PART_CODE) LIKE '%" + partCode.trim().toUpperCase() + "%' ");
//            }
            if (!CommonUtils.isNullString(partOldcode)) {
                sql.append(" AND  UPPER(D.PART_OLDCODE)  LIKE '%" + partOldcode.trim().toUpperCase() + "%' ");
            }
            if (!CommonUtils.isNullString(whId)) {
                sql.append("  AND L.WH_ID  =  '" + whId + "' ");
            }
            if (!CommonUtils.isNullString(parentOrgId)) {
                sql.append("  AND L.ORG_ID  =  '" + parentOrgId + "' ");
            }

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "配件编码";
            head[2] = "配件名称";
            //head[3] = "件号";
            head[3] = "货位编码";
            head[4] = "附属货位";

            if (Constant.OEM_ACTIVITIES.equals(parentOrgId)) {
//                head[5] = "库管员";
//                head[6] = "包装尺寸";
//                head[7] = "最小包装量";
//                head[8] = "整包发运量";
//                head[9] = "修改日期";
//                head[10] = "仓库";
                head[5] = "包装尺寸";
                head[6] = "最小包装量";
                head[7] = "整包发运量";
                head[8] = "修改日期";
                head[9] = "仓库";
            } else {
                head[5] = "修改日期";
                head[6] = "仓库";
            }

            List<Map<String, Object>> list = dao.locInfoList(sql.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("PART_OLDCODE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("PART_CNAME"));
//                        detail[3] = CommonUtils.checkNull(map
//                                .get("PART_CODE"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("LOC_CODE"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("SUB_LOC"));

                        if (Constant.OEM_ACTIVITIES.equals(parentOrgId)) {
//                            detail[5] = CommonUtils.checkNull(map
//                                    .get("WH_MAN"));
                            detail[5] = CommonUtils.checkNull(map
                                    .get("PKG_SIZE"));
                            detail[6] = CommonUtils.checkNull(map
                                    .get("OEM_MIN_PKG"));
                            detail[7] = CommonUtils.checkNull(map
                                    .get("MIN_PKG"));
                            detail[8] = CommonUtils.checkNull(map
                                    .get("UPDATE_DATE"));
                            detail[9] = CommonUtils.checkNull(map
                                    .get("WH_NAME"));
                        } else {
                            detail[5] = CommonUtils.checkNull(map
                                    .get("UPDATE_DATE"));
                            detail[6] = CommonUtils.checkNull(map
                                    .get("WH_NAME"));
                        }

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件货位信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件货位信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-27
     * @Title : 货位变更导出
     */
    public void exportLcHsExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
            } else {
                parentOrgId = logonUser.getDealerId();
            }
            String partCode = request.getParamValue("PART_CODE"); // 件号
            String partOldcode = request.getParamValue("PART_OLDCODE"); // 配件编码
            String whId = request.getParamValue("WH_ID"); // 仓库ID
            String locCode = request.getParamValue("LOC_CODE"); // 货位编码
            String subCol = request.getParamValue("SUB_COL"); // 附属货位
            StringBuffer sql = new StringBuffer();
            if (!CommonUtils.isNullString(locCode)) {
                sql.append(" AND UPPER(L.LOC_CODE) LIKE '%" + locCode.trim().toUpperCase() + "%' ");
            }
            if (!CommonUtils.isNullString(subCol)) {
                sql.append(" AND UPPER(L.SUB_LOC) LIKE '%" + subCol.trim().toUpperCase() + "%' ");
            }
            if (!CommonUtils.isNullString(partCode)) {
                sql.append(" AND  UPPER(D.PART_CODE) LIKE '%" + partCode.trim().toUpperCase() + "%' ");
            }
            if (!CommonUtils.isNullString(partOldcode)) {
                sql.append(" AND  UPPER(D.PART_OLDCODE)  LIKE '%" + partOldcode.trim().toUpperCase() + "%' ");
            }
            if (!CommonUtils.isNullString(whId)) {
                sql.append("  AND L.WH_ID  =  '" + whId + "' ");
            }
            if (!CommonUtils.isNullString(parentOrgId)) {
                sql.append("  AND L.ORG_ID  =  '" + parentOrgId + "' ");
            }

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "配件编码";
            head[2] = "配件名称";
            head[3] = "货位编码(原)";
            head[4] = "货位编码(现)";
            head[5] = "附属货位(原)";
            head[6] = "附属货位(现)";
            if (Constant.OEM_ACTIVITIES.equals(parentOrgId)) {
                head[7] = "库管员(原)";
                head[8] = "库管员(现)";
                head[9] = "修改日期";
                head[10] = "仓库名称";
            } else {
                head[7] = "修改日期";
                head[8] = "仓库名称";
            }

            List<Map<String, Object>> list = dao.locHistoryList(sql.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("PART_OLDCODE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("PART_CNAME"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("OLD_LOC_CODE"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("LOC_CODE"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("OLD_SUB_LOC"));
                        detail[6] = CommonUtils.checkNull(map
                                .get("SUB_LOC"));
                        if (Constant.OEM_ACTIVITIES.equals(parentOrgId)) {
                            detail[7] = CommonUtils.checkNull(map
                                    .get("OLD_WH_MAN"));
                            detail[8] = CommonUtils.checkNull(map
                                    .get("WH_MAN"));
                            detail[9] = CommonUtils.checkNull(map
                                    .get("CREATE_DATE"));
                            detail[10] = CommonUtils.checkNull(map
                                    .get("WH_NAME"));
                        } else {
                            detail[7] = CommonUtils.checkNull(map
                                    .get("CREATE_DATE"));
                            detail[8] = CommonUtils.checkNull(map
                                    .get("WH_NAME"));
                        }

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件货位变更信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件货位信息");
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
