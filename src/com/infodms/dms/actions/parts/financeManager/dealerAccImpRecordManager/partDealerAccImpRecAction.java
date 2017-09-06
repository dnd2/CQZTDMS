package com.infodms.dms.actions.parts.financeManager.dealerAccImpRecordManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.financeManager.dealerAccImpRecordManager.partDealerAccImpRecDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtPartAccountImportHistoryPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理服务商资金导入历史记录查询
 * @Description:CHANADMS
 * @Date: 2013-4-16
 * @remark
 */
public class partDealerAccImpRecAction implements PTConstants {
    public Logger logger = Logger.getLogger(partDealerAccImpRecAction.class);
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    private static final partDealerAccImpRecDao dao = partDealerAccImpRecDao.getInstance();
    public static final String DEALER_ACC_IMP_REC_QUERY = "/jsp/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecordQuery.jsp";//资金导入记录查询页面
    private static final String DEALER_ACC_QUERY = "/jsp/parts/financeManager/dealerAccImpRecordManager/partDealerAccQuery.jsp";//资金导入记录查询页面
    private static final String DEALER_ACC_Add = "/jsp/parts/financeManager/dealerAccImpRecordManager/partDealerAccEdit.jsp";//资金导入记录查询页面
    private static final String DEALER_ACC_Add2 = "/jsp/parts/financeManager/dealerAccImpRecordManager/partDealerAccOEMEdit.jsp";//配件首批款维护页面
    private static final String DEALER_ACC_QUERY2 = "/jsp/parts/financeManager/dealerAccImpRecordManager/partDealerAccOEMQuery.jsp";//配件首批款查询

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-16
     * @Title : 跳转至服务商资金导入历史记录查询页面
     */
    public void partDealerAccImpRecInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码

            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;

            } else {
                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();

            }
            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setForword(DEALER_ACC_IMP_REC_QUERY);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "资金导入历史记录查询初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-16
     * @Title : 按条件查询资金导入记录信息
     */
    public void partDealerAccImpRecSearch() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 服务商编码
            String importType = CommonUtils.checkNull(request.getParamValue("importType")); // 导入类型
            String remark = CommonUtils.checkNull(request.getParamValue("remark")); // 备注(财务凭证)
            String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate"));// 开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate"));// 结束时间
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            String accountKind = CommonUtils.checkNull(request.getParamValue("accountKind"));// 资金类型
            String sqlStr = "";
            if (null != dealerCode && !"".equals(dealerCode)) {
                /*String dealerCodes[] = dealerCode.split(",");
                String dealerCodeStr = "";
	        	for(int i = 0; i < dealerCodes.length; i ++)
	        	{
	        		if(0 != i)
	        		{
	        			dealerCodeStr += ", '" + dealerCodes[i] + "' ";
	        		}else
	        		{
	        			dealerCodeStr = "'" + dealerCodes[i] + "' ";
	        		}
	        	}
				sqlStr += " AND PA.CHILDORG_CODE IN (" + dealerCodeStr + ") ";*/
                sqlStr += " AND PA.CHILDORG_CODE LIKE '%" + dealerCode + "%' ";
            }
            if (null != importType && !"".equals(importType)) {
                sqlStr += " AND PA.IMPORT_TYPE = '" + importType + "'";
            }
            if (null != remark && !"".equals(remark)) {
                sqlStr += " AND PA.REMARK LIKE '%" + remark + "%'";
            }
            if (null != startDate && !"".equals(startDate)) {
                sqlStr += " AND TO_CHAR(PA.CREATE_DATE,'yyyy-MM-dd') >= '" + startDate + "' ";
            }
            if (null != endDate && !"".equals(endDate)) {
                sqlStr += " AND TO_CHAR(PA.CREATE_DATE,'yyyy-MM-dd') <= '" + endDate + "' ";
            }
            if (null != accountKind && !accountKind.equals("")) {
                sqlStr += " AND PA.ACCOUNT_KIND  = '" + accountKind + "' ";
            }
            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sqlStr += " AND PA.PARENTORG_ID = '" + parentOrgId + "' ";
            }
            /*if(null != parentOrgCode && !"".equals(parentOrgCode))
            {
				sqlStr += " AND PA.PARENTORG_CODE = '" + parentOrgCode + "' ";
			}*/

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryDealerAccImpRec(Constant.PAGE_SIZE, curPage, sqlStr);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询资金导入记录信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件打款登记
     */
    public void partDealerAccPaymentinit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码

            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;

            } else {
                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();

            }
            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setForword(DEALER_ACC_QUERY);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件打款等级报错！");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @throws Exception
     */
    public void partDealerAccQueryAdd() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String flag = CommonUtils.checkNull(request.getParamValue("flag"));
        try {
            // 车厂银行账户信息
            String sql = "SELECT * FROM TT_SALES_FIN_BANK TSF WHERE STATUS = 10011001";
            List<Map<String, Object>> list = dao.pageQuery(sql, null, dao.getFunName());
            act.setOutData("bank", list);

            //等待后续优化，当前已够用
            if (!"".equals(flag) && null != flag) {
                act.setOutData("dk_date", CommonUtils.getDate());
                act.setForword(DEALER_ACC_Add2);
            } else {
                if (logonUser.getDealerId() != null) {
                    TmDealerPO dealerPO = new TmDealerPO();
                    dealerPO.setDealerId(Long.valueOf(logonUser.getDealerId()));
                    dealerPO = ((TmDealerPO) dao.select(dealerPO).get(0));

                    act.setOutData("dealerId", dealerPO.getDealerId());
                    act.setOutData("dealerCode", dealerPO.getDealerCode());
                    act.setOutData("dealerName", dealerPO.getDealerName());
                    act.setOutData("dk_date", CommonUtils.getDate());
                } else {
                    TmCompanyPO companyPO = new TmCompanyPO();
                    companyPO.setCompanyType(Integer.parseInt(Constant.COMPANY_TYPE_SGM));
                    companyPO = (TmCompanyPO) dao.select(companyPO).get(0);
                    act.setOutData("dealerId", Constant.OEM_ACTIVITIES);
                    act.setOutData("dealerCode", Constant.ORG_ROOT_CODE);
                    act.setOutData("dealerName", companyPO.getCompanyName());
                    act.setOutData("dk_date", CommonUtils.getDate());
                }
                act.setForword(DEALER_ACC_Add);
            }


        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件打款等级报错！");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 保存和修改方法
     */
    public void partDealerAccQuerySave() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String amount = CommonUtils.checkNull(request.getParamValue("amount"));//打款金额
            String remark = CommonUtils.checkNull(request.getParamValue("remark"));//打款金额
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));//服务商ID
            String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate"));//打款日期
            String pzNo = CommonUtils.checkNull(request.getParamValue("pz_no"));//凭证号
            String hid = CommonUtils.checkNull(request.getParamValue("hid"));//记录ID
            String jp = CommonUtils.checkNull(request.getParamValue("jp"));//打款类型，前端隐藏默认为配件款
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));//标志
            String bankId = CommonUtils.checkNull(request.getParamValue("bankId"));//银行Id

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            DecimalFormat decimalFormat = new DecimalFormat("#.00");

            TmDealerPO dealerPO = new TmDealerPO();

            TmCompanyPO companyPO = new TmCompanyPO();
            companyPO.setCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));
            companyPO = (TmCompanyPO) dao.select(companyPO).get(0);

            if ("".equals(hid)) {
                TtPartAccountImportHistoryPO importHistoryPO = new TtPartAccountImportHistoryPO();
                if (!"".equals(flag) && null != flag) {
                    dealerPO.setDealerId(Long.valueOf(dealerId));
                    dealerPO = (TmDealerPO) dao.select(dealerPO).get(0);
                    importHistoryPO.setHistroryId(Long.parseLong(SequenceManager.getSequence("")));
                    importHistoryPO.setChildorgId(Long.parseLong(dealerId));
                    importHistoryPO.setChildorgCode(dealerPO.getDealerCode());
                    importHistoryPO.setChildorgName(dealerPO.getDealerName());
                    importHistoryPO.setParentorgId(companyPO.getCompanyId());//主机厂
                    importHistoryPO.setParentorgCode(companyPO.getCompanyCode());
                    importHistoryPO.setParentorgName(companyPO.getCompanyName());
                    importHistoryPO.setAccountKind(Constant.FIXCODE_CURRENCY_01);
                    importHistoryPO.setImportType(-1);//导入类型
                    importHistoryPO.setAmount(Double.valueOf(decimalFormat.format(Double.parseDouble(amount))));//金额
                    importHistoryPO.setRemark(remark);
                    importHistoryPO.setPzNo(pzNo);
                    importHistoryPO.setCreateDate(new Date());
                    importHistoryPO.setDkDate(format.parse(checkSDate));
                    importHistoryPO.setCreateBy(logonUser.getUserId());
                    importHistoryPO.setState(Constant.STATUS_ENABLE);
                    importHistoryPO.setStatus(-1);//处理状态首批登记
                    importHistoryPO.setAccountPurpose(Constant.PART_ACCOUNT_PURPOSE_TYPE_01);
                } else {
                    dealerPO.setDealerId(Long.valueOf(logonUser.getDealerId()));
                    dealerPO = (TmDealerPO) dao.select(dealerPO).get(0);

                    importHistoryPO.setHistroryId(Long.parseLong(SequenceManager.getSequence("")));
                    importHistoryPO.setChildorgId(Long.parseLong(dealerId));
                    importHistoryPO.setChildorgCode(dealerPO.getDealerCode());
                    importHistoryPO.setChildorgName(dealerPO.getDealerName());
                    importHistoryPO.setParentorgId(companyPO.getCompanyId());//主机厂
                    importHistoryPO.setParentorgCode(companyPO.getCompanyCode());
                    importHistoryPO.setParentorgName(companyPO.getCompanyName());
                    importHistoryPO.setAccountKind(Constant.FIXCODE_CURRENCY_01);
                    importHistoryPO.setImportType(0);//导入类型发生额
                    importHistoryPO.setAmount(Double.valueOf(decimalFormat.format(Double.parseDouble(amount))));//订单金额
                    importHistoryPO.setRemark(remark);
                    importHistoryPO.setPzNo(pzNo);
                    importHistoryPO.setCreateDate(new Date());
                    importHistoryPO.setDkDate(format.parse(checkSDate));
                    importHistoryPO.setCreateBy(logonUser.getUserId());
                    importHistoryPO.setState(Constant.STATUS_ENABLE);
                    importHistoryPO.setStatus(0);//处理状态已保存
                    importHistoryPO.setSysStatus("0");//接口处理状态
                    if(!"".equals(bankId) && null != bankId) {
                        importHistoryPO.setBankId(Long.valueOf(bankId));
                    }
                    importHistoryPO.setAccountPurpose(Integer.valueOf(jp));
                }
                dao.insert(importHistoryPO);
            } else {
                TtPartAccountImportHistoryPO srcPo = new TtPartAccountImportHistoryPO();
                srcPo.setHistroryId(Long.parseLong(hid));

                TtPartAccountImportHistoryPO updatePo = new TtPartAccountImportHistoryPO();
                updatePo.setAmount(Double.valueOf(decimalFormat.format(Double.parseDouble(amount))));
                updatePo.setRemark(remark);
                updatePo.setDkDate(format.parse(checkSDate));
                updatePo.setUpdateBy(logonUser.getUserId());
                updatePo.setUpdateDate(new Date());
                if(!"".equals(bankId) && null != bankId) {
                    updatePo.setBankId(Long.valueOf(bankId));
                }
                if ("".equals(flag) || null == flag) {
                    updatePo.setPzNo(pzNo);
                    updatePo.setAccountPurpose(Integer.valueOf(jp));
                }
                dao.update(srcPo, updatePo);
            }

            act.setOutData("ACTION_RESULT", "1");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件打款登记报错");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void partDealerAccQueryQuery() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String remark = CommonUtils.checkNull(request.getParamValue("remark")); // 备注(财务凭证)
            String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate"));// 开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate"));// 结束时间
            String pzNo = CommonUtils.checkNull(request.getParamValue("pz_No"));// 结束时间
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));// 标志
            String sqlStr = "";

            if (null != pzNo && !"".equals(pzNo)) {
                sqlStr += " AND PA.pz_No LIKE '%" + pzNo + "%'";
            }
            if (null != remark && !"".equals(remark)) {
                sqlStr += " AND PA.REMARK LIKE '%" + remark + "%'";
            }
            if (null != remark && !"".equals(remark)) {
                sqlStr += " AND PA.REMARK LIKE '%" + remark + "%'";
            }
            if (null != startDate && !"".equals(startDate)) {
                sqlStr += " AND TO_CHAR(PA.CREATE_DATE,'yyyy-MM-dd') >= '" + startDate + "' ";
            }
            if (null != endDate && !"".equals(endDate)) {
                sqlStr += " AND TO_CHAR(PA.CREATE_DATE,'yyyy-MM-dd') <= '" + endDate + "' ";
            }

            if (!"".equals(flag) && null != flag) {
                sqlStr += "  AND PA.STATUS < 0 ";
            } else {
                sqlStr += "  AND PA.CHILDORG_ID =" + logonUser.getDealerId();
                sqlStr += "  AND PA.STATUS >= 0 ";
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryDealerAccImp(Constant.PAGE_SIZE, curPage, sqlStr);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询资金导入记录信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 修改操作
     */
    public void partDealerAccQueryEdit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String hid = CommonUtils.checkNull(request.getParamValue("hid"));//记录ID
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));//标志
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            TtPartAccountImportHistoryPO importHistoryPO = new TtPartAccountImportHistoryPO();
            importHistoryPO.setHistroryId(Long.parseLong(hid));
            importHistoryPO = ((TtPartAccountImportHistoryPO) dao.select(importHistoryPO).get(0));

            TmDealerPO dealerPO = new TmDealerPO();
            if (!"".equals(flag) && null != flag) {
                dealerPO.setDealerId(Long.valueOf(importHistoryPO.getChildorgId() + ""));
                dealerPO = ((TmDealerPO) dao.select(dealerPO).get(0));
            } else {
                dealerPO.setDealerId(Long.valueOf(logonUser.getDealerId()));
                dealerPO = ((TmDealerPO) dao.select(dealerPO).get(0));
            }

            // 车厂银行账户信息
            String sql = "SELECT * FROM TT_SALES_FIN_BANK TSF WHERE STATUS = 10011001";
            List<Map<String, Object>> list = dao.pageQuery(sql, null, dao.getFunName());
            act.setOutData("bank", list);
            act.setOutData("dealerId", dealerPO.getDealerId());
            act.setOutData("dealerCode", dealerPO.getDealerCode());
            act.setOutData("dealerName", dealerPO.getDealerName());
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("amount", importHistoryPO.getAmount());
            act.setOutData("remark", importHistoryPO.getRemark());
            act.setOutData("pz_no", importHistoryPO.getPzNo());
            act.setOutData("bank_id", importHistoryPO.getBankId());
            act.setOutData("dk_date", importHistoryPO.getDkDate() == null ? CommonUtils.getDate() : format.format(importHistoryPO.getDkDate()));
            act.setOutData("hid", hid);
            act.setOutData("jp", importHistoryPO.getAccountPurpose());
            if (!"".equals(flag) && null != flag) {
                act.setForword(DEALER_ACC_Add2);
            } else {
                act.setForword(DEALER_ACC_Add);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件打款登记报错");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 提交、作废逻辑
     */
    public void partDealerAccQueryMng() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String hid = CommonUtils.checkNull(request.getParamValue("hid"));//记录ID
            String opType = CommonUtils.checkNull(request.getParamValue("opType"));//操作类型

            TtPartAccountImportHistoryPO importHistoryPO = new TtPartAccountImportHistoryPO();
            importHistoryPO.setHistroryId(Long.parseLong(hid));

            //提交操作
            if (opType.equals("SUB")) {
//                importHistoryPO.setSysStatus("0");
//                importHistoryPO.setStatus(0);
                TtPartAccountImportHistoryPO updatePo = new TtPartAccountImportHistoryPO();
                updatePo.setSysStatus("1");//接口状态已提交
                updatePo.setStatus(1);//审核状态已提交
                updatePo.setSubBy(logonUser.getUserId());
                updatePo.setSubDate(new Date());
                dao.update(importHistoryPO, updatePo);
                act.setOutData("ACTION_RESULT", "提交成功");
            } else {
                dao.delete(importHistoryPO);
                act.setOutData("ACTION_RESULT", "作废成功");
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件打款登记报错");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件打款登记
     */
    public void partDealerAccOEMinit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码

            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;

            } else {
                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();

            }
            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setForword(DEALER_ACC_QUERY2);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件打款等级报错！");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}
