package com.infodms.dms.actions.parts.baseManager.partExceptionQueryManger;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partExceptionQueryManger.PartExceptionQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartReplacedDefinePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

import jxl.Workbook;
import jxl.write.Label;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理设计变更维护业务
 * @Description:CHANADMS
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2013-4-2
 * @remark
 */
public class partExceptionQuery implements PTConstants {
    public Logger logger = Logger.getLogger(partExceptionQuery.class);

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 跳转至设计变更页面
     */
    public void partExceptionQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PART_EXCEPTION_QUERY_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "设计变更维护初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 跳转至新增设计页面
     */
    public void partExceptionQueryAddInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PART_EXCEPTION_QUERY_ADD);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "新增设计初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 跳转至修改设计页面
     */
    public void partExceptionQueryFormodInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();

        try {
            String replaceId = CommonUtils.checkNull(request.getParamValue("parms"));
            PartExceptionQueryDao dao = PartExceptionQueryDao.getInsttance();
            String sql = " AND T.REPLACE_ID = '" + replaceId + "'";
            List<Map<String, Object>> list = dao.queryReplacePart(sql);

            String partId = list.get(0).get("PART_ID").toString();// 配件ID
            String partOldcode = list.get(0).get("PART_OLDCODE").toString();// 变更前配件编码
            String rePartID = list.get(0).get("REPART_ID").toString();// 变更后配件ID
            String rePartOldcode = list.get(0).get("REPART_OLDCODE").toString();
            String rePartCode = list.get(0).get("REPART_CODE").toString();// 变更后件号
            String rePartCname = list.get(0).get("REPART_CNAME").toString();
            String type = CommonUtils.checkNull(list.get(0).get("TYPE"));
            String remark = "";
            if (null != list.get(0).get("REMARK")) {
                remark = list.get(0).get("REMARK").toString();
            }
            String hiddenValue = rePartCname + "&&" + rePartID + "&&" + rePartCode;
            act.setOutData("partId", partId);
            act.setOutData("replaceId", replaceId);
            act.setOutData("partOldcode", partOldcode);
            act.setOutData("rePartOldcode", rePartOldcode);
            act.setOutData("rePartID", rePartID);
            act.setOutData("hiddenValue", hiddenValue);
            act.setOutData("rePartCname", rePartCname);
            act.setOutData("type", type);
            act.setOutData("remark", remark);

            act.setForword(PART_EXCEPTION_QUERY_Mod);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "修改设计信息初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 使设计失效
     */
    public void celPartExceptionQuery() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String disabeParms = CommonUtils.checkNull(request.getParamValue("disabeParms"));
            String disabeParmsArr[] = disabeParms.split("@@");
            String partId = disabeParmsArr[0];// 配件ID
            String replaceId = disabeParmsArr[1];// 替代记录ID
            Long userId = logonUser.getUserId();// 当前用户ID
            PartExceptionQueryDao dao = PartExceptionQueryDao.getInsttance();

            // TtPartDefinePO
            TtPartDefinePO selPO = new TtPartDefinePO();
            TtPartDefinePO updatePO = new TtPartDefinePO();

            selPO.setPartId(Long.parseLong(partId));

            updatePO.setRepartCode("");
            updatePO.setIsReplaced(Constant.IF_TYPE_NO);
            updatePO.setRepartMby(userId);
            updatePO.setRepartMdate(new Date());

            dao.update(selPO, updatePO);

            // TtPartReplacedDefinePO
            TtPartReplacedDefinePO selRDPO = new TtPartReplacedDefinePO();
            TtPartReplacedDefinePO updateRDPO = new TtPartReplacedDefinePO();

            selRDPO.setPartId(Long.parseLong(partId));
            selRDPO.setReplaceId(Long.parseLong(replaceId));

            updateRDPO.setState(Constant.STATUS_DISABLE);
            updateRDPO.setUpdateBy(userId);
            updateRDPO.setUpdateDate(new Date());
            updateRDPO.setDisableBy(userId);
            updateRDPO.setDisableDate(new Date());

            dao.update(selRDPO, updateRDPO);

            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "失效选中的设计");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 使设计有效
     */
    public void enablePartExceptionQuery() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String enableParms = CommonUtils.checkNull(request.getParamValue("enableParms"));
            String enableParmsArr[] = enableParms.split("@@");
            String partId = enableParmsArr[0];// 配件ID
            String rePartOldcode = enableParmsArr[1];// 变更后配件编码
            String replaceId = enableParmsArr[2];// 替代记录ID

            Long userId = logonUser.getUserId();
            PartExceptionQueryDao dao = PartExceptionQueryDao.getInsttance();
            // TtPartDefinePO
            TtPartDefinePO selPO = new TtPartDefinePO();
            TtPartDefinePO updatePO = new TtPartDefinePO();

            selPO.setPartId(Long.parseLong(partId));

            updatePO.setRepartCode(rePartOldcode);
            updatePO.setIsReplaced(Constant.IF_TYPE_YES);
            updatePO.setRepartMby(userId);
            updatePO.setRepartMdate(new Date());

            dao.update(selPO, updatePO);

            // TtPartReplacedDefinePO
            TtPartReplacedDefinePO selRDPO = new TtPartReplacedDefinePO();
            TtPartReplacedDefinePO updateRDPO = new TtPartReplacedDefinePO();

            selRDPO.setPartId(Long.parseLong(partId));
            selRDPO.setReplaceId(Long.parseLong(replaceId));

            updateRDPO.setState(Constant.STATUS_ENABLE);
            updateRDPO.setUpdateBy(userId);
            updateRDPO.setUpdateDate(new Date());

            dao.update(selRDPO, updateRDPO);

            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "有效选中的设计");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 删除设计（暂时无启用）
     */
    public void delPartExceptionQuery() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));// 配件ID
            Long userId = logonUser.getUserId();
            PartExceptionQueryDao dao = PartExceptionQueryDao.getInsttance();
            // TtPartDefinePO
            TtPartDefinePO selPO = new TtPartDefinePO();
            TtPartDefinePO updatePO = new TtPartDefinePO();

            selPO.setPartId(Long.parseLong(partId));

			/*
             * updatePO.setRepartId(Long.parseLong("0"));
			 * updatePO.setRepartCode("");
			 */
            updatePO.setIsReplaced(Constant.IF_TYPE_NO);
            updatePO.setRepartMby(userId);
            updatePO.setRepartMdate(new Date());

            dao.update(selPO, updatePO);

            // TtPartReplacedDefinePO
            TtPartReplacedDefinePO selRDPO = new TtPartReplacedDefinePO();
            TtPartReplacedDefinePO updateRDPO = new TtPartReplacedDefinePO();

            selRDPO.setPartId(Long.parseLong(partId));
            selRDPO.setStatus(1);

            updateRDPO.setState(Constant.STATUS_DISABLE);
			/*
			 * updateRDPO.setRepartId(null); updateRDPO.setRepartCode(null);
			 * updateRDPO.setRepartCname(null);
			 * updateRDPO.setRepartOldcode(null);
			 */
            updateRDPO.setDeleteBy(userId);
            updateRDPO.setDeleteDate(new Date());
            updateRDPO.setStatus(0);

            dao.update(selRDPO, updateRDPO);

            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "删除选中的设计");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 按条件查询设计
     */
    public void partExceptionQuerySearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderID = CommonUtils.checkNull(request.getParamValue("ORDER_ID")); // 件号
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); // 配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME"));// 配件名称
            String stateValue = CommonUtils.checkNull(request.getParamValue("stateValue"));// 是否有效
            PartExceptionQueryDao dao = PartExceptionQueryDao.getInsttance();
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartException(orderID, partCode, partName, stateValue, Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询设计");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 配件编码选项查询(1)
     */
    public void partExceptionQuerySelect() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            act.getResponse().setContentType("application/json");
            String partOldId = CommonUtils.checkNull(request.getParamValue("partOldId"));// 替换件ID
            String reType = CommonUtils.checkNull(request.getParamValue("reType"));// 替换件ID
            if ("1".equals(request.getParamValue("query"))) { // 开始查询
                
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("partOldId", partOldId);
                paramMap.put("partOlcode", CommonUtils.checkNull(request.getParamValue("partolcode")));// 配件编码
                paramMap.put("partCName", CommonUtils.checkNull(request.getParamValue("partcname")));// 配件名称
                
                Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
                PartExceptionQueryDao dao = PartExceptionQueryDao.getInsttance();
                PageResult<Map<String, Object>> ps = dao.getPartOLCode(Constant.PAGE_SIZE, curPage, paramMap);
                act.setOutData("ps", ps);
            } else {
                act.setOutData("partOldId", partOldId);
                act.setOutData("reType", reType);
                act.setForword(PART_EXCEPTION_QUERY_SELECT);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件编码选项查询(1)");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 新增设计
     */
    @SuppressWarnings("unchecked")
    public void partExceptionQueryAdd() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String errorExist = null;
        PartExceptionQueryDao dao = PartExceptionQueryDao.getInsttance();
        try {
            RequestWrapper request = act.getRequest();

            String partID = CommonUtils.checkNull(request.getParamValue("PART_ID"));// 配件ID
            String partOldcode = request.getParamValue("PART_OLDCODE");// 配件编码
            String partCode = partOldcode;// 件号
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_DATA")) ;// 配件名称
            
            String rePartID = CommonUtils.checkNull(request.getParamValue("PART_ID2"));// 替代件ID
            String rePartOldcode = request.getParamValue("PART_OLDCODE2");// 替代件编码
            String rePartCode = rePartOldcode;// 替代件件号
            String rePartCname = CommonUtils.checkNull(request.getParamValue("PART_DATA2"));// 替代件名称
            String oldRepPartId = CommonUtils.checkNull(request.getParamValue("OLD_REP_PART_ID"));// 原替换件ID
            
            String type = CommonUtils.checkNull(request.getParamValue("TYPE"));// 替换类型

            Long userId = logonUser.getUserId();// 用户ID
            String remark = request.getParamValue("REMARK");// 备注

            boolean flag = false;
            if(rePartID.equals(oldRepPartId)){
                List existlist = dao.getExistPO(rePartID, partID, "");
                if (existlist != null && existlist.size() > 0) {
                    flag= true;
                }
                if(!flag){
                    existlist = dao.getExistPO(partID, rePartID, "");
                    if (existlist != null && existlist.size() > 0) {
                        flag= true;
                    }
                }
            }
            
            if (flag) {
                errorExist = partCode;
            } else {
                // TtPartDefinePO
                TtPartDefinePO selPO = new TtPartDefinePO();
                TtPartDefinePO updatePO = new TtPartDefinePO();

                selPO.setPartId(Long.parseLong(partID));
//				selPO.setPartCode(partCode);
//				selPO.setPartOldcode(partOldcode);
//				selPO.setPartCname(partCname);

                updatePO.setIsReplaced(Constant.PART_BASE_FLAG_YES);
                updatePO.setRepartId(Long.parseLong(rePartID));
                updatePO.setRepartCode(rePartOldcode);
                updatePO.setRepartDate(new Date());
                updatePO.setRepartBy(userId);
//				updatePO.setRemark(remark);

                dao.update(selPO, updatePO);

                // TtPartReplacedDefinePO
                TtPartReplacedDefinePO replacedDefinePO = new TtPartReplacedDefinePO();

                replacedDefinePO.setReplaceId(Long.parseLong(SequenceManager.getSequence("")));
                replacedDefinePO.setPartId(Long.parseLong(partID));
                replacedDefinePO.setPartCode(partCode);
                replacedDefinePO.setPartOldcode(partOldcode);
                replacedDefinePO.setPartCname(partCname);
                replacedDefinePO.setRepartId(Long.parseLong(rePartID));
                replacedDefinePO.setRepartCname(rePartCname);
                replacedDefinePO.setRepartOldcode(rePartOldcode);
                replacedDefinePO.setRepartCode(rePartCode);
                replacedDefinePO.setType(Integer.valueOf(type));
                replacedDefinePO.setRemark(remark);
                replacedDefinePO.setCreateDate(new Date());
                replacedDefinePO.setCreateBy(userId);
                replacedDefinePO.setUpdateDate(new Date());
                replacedDefinePO.setUpdateBy(userId);
                dao.insert(replacedDefinePO);
            }
            act.setOutData("errorExist", errorExist);// 配件替代件记录已创建
            act.setOutData("success", "true");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "监控设计变更维护");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 修改设计
     */
    // update
    @SuppressWarnings("unchecked")
    public void partExceptionQueryUpdate() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String errorExist = null;
        PartExceptionQueryDao dao = PartExceptionQueryDao.getInsttance();
        try {
            RequestWrapper request = act.getRequest();

            String partID = request.getParamValue("PART_ID");// 配件ID
            String partOldcode = request.getParamValue("PART_OLDCODE");// 配件编码
            String replaceID = request.getParamValue("REPLACE_ID");// 替代记录ID
            String oldRePartId = request.getParamValue("OLD_REP_PART_ID");// 原替代配件ID
            String type = CommonUtils.checkNull(request.getParamValue("TYPE"));// 替换类型
			/*
			 * String partDataArr[] = partData.split("&&"); String partCname =
			 * partDataArr[0].toString(); String partID =
			 * partDataArr[1].toString(); String partCode =
			 * partDataArr[2].toString();
			 */
            String rePartOldcode = request.getParamValue("PART_OLDCODE2");// 替代件编码
            String rePartCname = CommonUtils.checkNull(request.getParamValue("PART_DATA2"));// 替代件名称
            String rePartID = CommonUtils.checkNull(request.getParamValue("PART_ID2"));// 替代件ID
            String rePartCode = rePartOldcode; // 替代件件号
            Long userId = logonUser.getUserId();// 用户ID
            String remark = CommonUtils.checkNull(request.getParamValue("REMARK")).trim();// 备注
            

            boolean flag = false;
            List<TtPartReplacedDefinePO> existlist = dao.getExistPO(partID,rePartID, oldRePartId);
            if (existlist != null && existlist.size() > 0) {
                flag= true;
            }
//            if(!flag){
//                existlist = dao.getExistPO(partID, rePartID);
//                if (existlist != null && existlist.size() > 0) {
//                    flag= true;
//                }
//            }
            if(flag){
                errorExist = rePartOldcode;
            }else{
                // TtParfinePO
                TtPartDefinePO selPO = new TtPartDefinePO();
                TtPartDefinePO updatePO = new TtPartDefinePO();

                // TtPartReplacedDefinePO
                TtPartReplacedDefinePO selPPDPO = new TtPartReplacedDefinePO();
                TtPartReplacedDefinePO updatePPDPO = new TtPartReplacedDefinePO();

                selPPDPO.setPartId(Long.parseLong(partID));
                selPPDPO.setReplaceId(Long.parseLong(replaceID));
                
                if(!oldRePartId.equals(rePartID)){
                    // TtParfinePO
                    selPO.setPartOldcode(partOldcode);
                    selPO.setPartId(Long.parseLong(partID));
                    
                    updatePO.setRepartId(Long.parseLong(rePartID));
                    updatePO.setIsReplaced(Constant.IF_TYPE_YES);
                    updatePO.setRepartCode(rePartOldcode);
                    updatePO.setRepartMdate(new Date());
                    updatePO.setRepartMby(userId);
                    
                    // TtPartReplacedDefinePO
                    updatePPDPO.setRepartId(Long.parseLong(rePartID));
                    updatePPDPO.setRepartCode(rePartCode);
                    updatePPDPO.setRepartCname(rePartCname);
                    updatePPDPO.setRepartOldcode(rePartOldcode);

                    dao.update(selPO, updatePO);
                }
                
                updatePPDPO.setType(Integer.parseInt(type));
                updatePPDPO.setRemark(remark);
                updatePPDPO.setUpdateDate(new Date());
                updatePPDPO.setUpdateBy(userId);
    
    
                dao.update(selPPDPO, updatePPDPO);

            }
            act.setOutData("errorExist", errorExist);// 配件替代件记录已创建
            act.setOutData("success", "true");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "监控设计变更维护");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-3
     * @Title :下载设计
     */
    public void exportPartExceptionExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartExceptionQueryDao dao = PartExceptionQueryDao.getInsttance();
            String orderID = CommonUtils.checkNull(request.getParamValue("ORDER_ID")); // 件号
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); // 配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME"));// 配件名称
            String stateVal = CommonUtils.checkNull(request.getParamValue("stateValue"));// 是否有效
            String[] head = new String[9];
            head[0] = "配件编码";
            head[1] = "配件名称";
            head[2] = "件号";
            head[3] = "变更后编码";
            head[4] = "变更后名称";
            head[5] = "变更后件号";
            head[6] = "新增日期";
            head[7] = "修改日期";
            head[8] = "是否有效";
            List<Map<String, Object>> list = dao.queryAllPartException(orderID, partCode, partName, stateVal);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[9];
                        detail[2] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[0] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[1] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[5] = CommonUtils.checkNull(map.get("REPART_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("REPART_OLDCODE"));
                        detail[4] = CommonUtils.checkNull(map.get("REPART_CNAME"));
                        detail[6] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[7] = CommonUtils.checkNull(map.get("UPDATE_DATE"));
                        Integer stateValue = Integer.parseInt(CommonUtils.checkNull(map.get("STATE").toString()));
                        if (Constant.STATUS_ENABLE.equals(stateValue)) {
                            detail[8] = "有效";
                        } else {
                            detail[8] = "无效";
                        }
                        list1.add(detail);
                    }
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
     * @param : @param response
     * @param : @param request
     * @param : @param head
     * @param : @param list
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 文件导出为xls文件
     */
    public static Object exportEx(ResponseWrapper response, RequestWrapper request, String[] head, List<String[]> list) throws Exception {

        String name = "设计变更维护信息.xls";
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
}
