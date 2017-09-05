package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtPartBillDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.context.POContext;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartDlrDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartDlrDao.class);

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    private static final PartDlrDao dao = new PartDlrDao();

    private PartDlrDao() {
    }

    public static final PartDlrDao getInstance() {
        return dao;
    }

    ActionContext act = ActionContext.getContext();
    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);


    /**
     * Function : 经销商查询
     *
     * @param : 经销商CODE
     * @param : 经销商名称
     * @param : 经销商等级
     * @param : 经销商状态
     * @param : 上级经销商代码
     * @param : 上级车厂组织
     * @param : 经销商类型
     * @param : 服务商类型
     * @param : 经销商公司ID
     * @param : 分页参数
     * @return : 经销商信息
     * @throws : Exception LastUpdate : 2010-7-5
     */
    public PageResult<Map<String, Object>> queryDealerInfo(String dealerClass, String dealerCode, String dealerName, String dealerLevel,
                                                           String status, String sJdealerCode, String orgCode, String dealerType, String companyId, String oemCompanyId, String province,
                                                           String pdealerType, int curPage, int pageSize) throws Exception {
        StringBuffer sql = new StringBuffer();
        /*
         * sql.append("SELECT D.DEALER_ID,\n");
		 * sql.append("       D.DEALER_CODE,\n");
		 * sql.append("       D.DEALER_SHORTNAME,\n");
		 * sql.append("       D.DEALER_TYPE,\n");
		 * sql.append("       D.STATUS,\n");
		 * sql.append("       D.DEALER_LEVEL,\n"); sql.append(
		 * "       DECODE(TD1.DEALER_SHORTNAME, NULL, '', TD1.DEALER_SHORTNAME) AS SHANGJINAME,\n"
		 * ); if(String.valueOf(Constant.DEALER_LEVEL_01).equals(dealerLevel)) {
		 * sql.append("      D.SHANGJIORGNAME,\n"); }else {
		 * sql.append("        '' AS SHANGJIORGNAME,\n"); }
		 * sql.append("       D.COMPANY_SHORTNAME\n");
		 * sql.append("  FROM (SELECT TD.DEALER_ID,\n");
		 * sql.append("               TD.DEALER_CODE,\n");
		 * sql.append("               TD.DEALER_SHORTNAME,\n");
		 * sql.append("               TD.DEALER_TYPE,\n");
		 * sql.append("               TD.STATUS,\n");
		 * sql.append("               TD.DEALER_LEVEL,\n");
		 * if(String.valueOf(Constant.DEALER_LEVEL_01).equals(dealerLevel)) {
		 * sql.append("               TMO.ORG_NAME AS SHANGJIORGNAME,\n"); }
		 * sql.append("               TD.PARENT_DEALER_D,\n");
		 * sql.append("               TC.COMPANY_SHORTNAME\n");
		 * if(String.valueOf(Constant.DEALER_LEVEL_01).equals(dealerLevel)) {
		 * sql.append(
		 * "           FROM TM_DEALER TD, TM_COMPANY TC,TM_ORG TMO,TM_DEALER_ORG_RELATION TDOR\n"
		 * ); }else {
		 * sql.append("           FROM TM_DEALER TD, TM_COMPANY TC \n"); }
		 * sql.append("         WHERE TD.COMPANY_ID = TC.COMPANY_ID\n");
		 * if(!"".equals(dealerCode)&&dealerCode!=null) {
		 * sql.append("           AND TD.DEALER_CODE LIKE '%"
		 * +dealerCode+"%'\n"); } if(!"".equals(dealerName)&&dealerName!=null) {
		 * sql
		 * .append("           AND TD.DEALER_SHORTNAME LIKE '%"+dealerName+"%'\n"
		 * ); } if(!"".equals(status)&&status!=null) {
		 * sql.append("           AND TD.STATUS = "+status+"\n"); }
		 * if(!"".equals(dealerType)&&dealerType!=null) {
		 * sql.append("           AND TD.DEALER_TYPE = "+dealerType+"\n"); }
		 * if(String.valueOf(Constant.DEALER_LEVEL_01).equals(dealerLevel)) {
		 * sql.append("           AND TD.DEALER_ID = TDOR.DEALER_ID\n");
		 * sql.append("           AND TDOR.ORG_ID = TMO.ORG_ID\n");
		 * sql.append("           AND TD.DEALER_LEVEL = "+dealerLevel+"\n");
		 * if(!"".equals(orgCode)&&orgCode!=null) { sql.append(
		 * "     AND EXISTS (SELECT 1 FROM TM_DEALER_ORG_RELATION TDOR,TM_ORG TMO WHERE TDOR.ORG_ID=TMO.ORG_ID AND TMO.ORG_CODE='"
		 * +orgCode+"' AND TDOR.DEALER_ID=TD.DEALER_ID)\n"); } }else {
		 * sql.append("           AND TD.DEALER_LEVEL = "+dealerLevel+"\n");
		 * if(!"".equals(sJdealerCode)&&sJdealerCode!=null) { sql.append(
		 * "           AND TD.PARENT_DEALER_D = (SELECT DEALER_ID FROM TM_DEALER WHERE DEALER_CODE ='"
		 * +sJdealerCode+"')\n"); } } if(!"".equals(companyId)&&companyId!=null)
		 * { sql.append("           AND TD.COMPANY_ID = "+companyId+"\n"); }
		 * if(oemCompanyId!=null&&!"".equals(oemCompanyId)) {
		 * sql.append("           AND TD.OEM_COMPANY_ID = "+oemCompanyId+"\n");
		 * } if(dealerClass!=null&&dealerClass!=""){
		 * sql.append("          AND TD.DEALER_CLASS='"+dealerClass+"'\n"); }
		 * sql.append("           AND TD.COMPANY_ID = TC.COMPANY_ID)D\n");
		 * sql.append
		 * ("  LEFT JOIN TM_DEALER TD1 ON D.PARENT_DEALER_D = TD1.DEALER_ID\n");
		 * sql.append("  ORDER BY D.DEALER_ID ASC\n");
		 */

        sql.append("SELECT DISTINCT D.DEALER_ID,\n");
        sql.append("       D.DEALER_CODE,\n");
        sql.append("       D.DEALER_SHORTNAME,\n");
        sql.append("       D.DEALER_TYPE,\n");
        sql.append("       D.PDEALER_TYPE,\n");
        sql.append("       D.STATUS,\n");
        sql.append("       D.DEALER_LEVEL,\n");
        sql.append("       DECODE(TD1.DEALER_SHORTNAME, NULL, '', TD1.DEALER_SHORTNAME) AS SHANGJINAME,\n");
        sql.append("       D.SHANGJIORGNAME,\n");
        sql.append("       D.COMPANY_SHORTNAME,\n");
        sql.append("       TO_CHAR(D.CREATE_DATE,'yyyy-MM-dd') AS CREATEDATE,\n");
        sql.append("       TO_CHAR(D.UPDATE_DATE,'yyyy-MM-dd') AS UPDATEDATE,\n");
        sql.append("       DECODE(E.NAME, NULL, '', E.NAME) AS CREATEPER,\n");
        sql.append("       DECODE(F.NAME, NULL, '', F.NAME) AS UPDATEPER\n");
        sql.append("  FROM (SELECT TD.DEALER_ID,\n");
        sql.append("               TD.DEALER_CODE,\n");
        sql.append("               TD.DEALER_SHORTNAME,\n");
        sql.append("               TD.DEALER_TYPE,\n");
        sql.append("               TD.STATUS,\n");
        sql.append("               TD.DEALER_LEVEL,\n");
        sql.append("               TD.CREATE_BY,\n");
        sql.append("               TD.UPDATE_BY,\n");
        sql.append("               TD.CREATE_DATE,\n");
        sql.append("               TD.UPDATE_DATE,\n");
        sql.append("               TMO.ORG_NAME AS SHANGJIORGNAME,\n");
        sql.append("               TD.PDEALER_TYPE,\n");
        sql.append("               TD.PARENT_DEALER_D,\n");
        sql.append("               TC.COMPANY_SHORTNAME\n");
        sql.append("           FROM TM_COMPANY TC,TM_ORG TMO,TM_DEALER_ORG_RELATION TDOR,TM_DEALER TD, TM_DEALER_BUSINESS_AREA TDBA\n");
        sql.append("         	WHERE TD.COMPANY_ID = TC.COMPANY_ID\n");
        sql.append("         	  AND TD.DEALER_ID = TDBA.DEALER_ID(+)\n");
        if (dealerCode != null && !"".equals(dealerCode)) {
            sql.append("           AND UPPER(TD.DEALER_CODE) LIKE '%" + dealerCode.trim().toUpperCase() + "%'\n");
        }
        if (dealerName != null && !"".equals(dealerName)) {
            sql.append("           AND TD.DEALER_SHORTNAME LIKE '%" + dealerName + "%'\n");
        }
        if (status != null && !"".equals(status)) {
            sql.append("           AND TD.STATUS = " + status + "\n");
        }
        if (dealerType != null && !"".equals(dealerType)) {
            sql.append("           AND TD.DEALER_TYPE = " + dealerType + "\n");
        }
        sql.append("           AND F_GET_PID(TD.DEALER_ID) = TDOR.DEALER_ID \n");
        sql.append("           AND TDOR.ORG_ID = TMO.ORG_ID\n");
        if (dealerLevel != null && !"".equals(dealerLevel)) {
            sql.append("           AND TD.DEALER_LEVEL = " + dealerLevel + "\n");
        }
        if (orgCode != null && !"".equals(orgCode)) {
            sql.append("     AND EXISTS (SELECT 1 FROM TM_DEALER_ORG_RELATION TDOR,TM_ORG TMO WHERE TDOR.ORG_ID=TMO.ORG_ID AND TMO.ORG_CODE='"
                    + orgCode + "' AND (TDOR.DEALER_ID=TD.DEALER_ID OR TDOR.DEALER_ID=TD.PARENT_DEALER_D))\n");
        }

        if (sJdealerCode != null && !"".equals(sJdealerCode)) {
            sql.append("           AND TD.PARENT_DEALER_D = (SELECT DEALER_ID FROM TM_DEALER WHERE DEALER_CODE ='" + sJdealerCode + "')\n");
        }
        if (companyId != null && !"".equals(companyId)) {
            sql.append("           AND TD.COMPANY_ID = " + companyId + "\n");
        }
        if (oemCompanyId != null && !"".equals(oemCompanyId)) {
            sql.append("           AND TD.OEM_COMPANY_ID = " + oemCompanyId + "\n");
        }
        if (dealerClass != null && dealerClass != "") {
            sql.append("          AND TD.DEALER_CLASS='" + dealerClass + "'\n");
        }
        if (province != null && province != "") {
            sql.append("          AND TD.PROVINCE_ID='" + province + "'\n");
        }
        if (pdealerType != null && !"".equals(pdealerType)) {
            sql.append("           AND TD.PDEALER_TYPE = " + pdealerType + "\n");
        }

        sql.append("           AND TD.COMPANY_ID = TC.COMPANY_ID)D\n");
        sql.append("  LEFT JOIN TM_DEALER TD1 ON D.PARENT_DEALER_D = TD1.DEALER_ID\n");
        sql.append("  LEFT JOIN TC_USER E ON D.CREATE_BY=E.USER_ID\n");
        sql.append("  LEFT JOIN TC_USER F ON D.UPDATE_BY=F.USER_ID\n");

        sql.append("  ORDER BY D.STATUS ASC, D.DEALER_ID ASC\n");
        PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return rs;
    }

    // 修改服务商类型
    public void modifyDealerInfo(Map<String, Object> map) {
        String serviceType = (String) map.get("serviceType");
        String serviceCode = (String) map.get("serviceCode");
        String sql = "update tm_dealer t set t.pdealer_type = " + serviceType + " where t.dealer_code = '" + serviceCode + "'";
        System.out.println(sql + "修改服务商类型=======================修改服务商类型");
        dao.update(sql, null);
    }

    // 根据经销商编号查询看是否已经存在该開票信息,存在修改,不存在新增
    public List<Map<String, Object>> modifyBillInfo(Map<String, Object> map) {
        String dealerId = (String) map.get("dealerId");

        String sql = "select * from tt_part_bill_define t where t.dealer_id = '" + dealerId + "'";
        String serviceCode = (String) map.get("serviceCode");
        String serviceName = (String) map.get("serviceName");
        String erpCode = (String) map.get("erpCode");
        String taxesNo = (String) map.get("taxesNo");
        String BANK = (String) map.get("BANK");
        String INVOICE_ACCOUNT = (String) map.get("INVOICE_ACCOUNT");
        String INVOICE_ADD = (String) map.get("INVOICE_ADD");
        String INVOICE_PHONE = (String) map.get("INVOICE_PHONE");

        List<Map<String, Object>> list = super.pageQuery(sql.toString(), null, super.getFunName());
        if (list.size() == 0) {

            TtPartBillDefinePO po = new TtPartBillDefinePO();
            po.setBillId(Long.valueOf(SequenceManager.getSequence("")));
            po.setDealerId(new Long(dealerId));
            po.setAccount(INVOICE_ACCOUNT);
            po.setAddr(INVOICE_ADD);
            po.setBank(BANK);
            po.setCreateBy(logonUser.getUserId());
            po.setDeleteBy(logonUser.getUserId());
            po.setUpdateBy(logonUser.getUserId());
            po.setDisableBy(logonUser.getUserId());
            Date currTime = new Date(System.currentTimeMillis());
            po.setCreateDate(currTime);
            po.setUpdateDate(currTime);
            po.setDeleteDate(currTime);
            po.setDisableDate(currTime);
            po.setDealerCode(serviceCode);
            po.setDealerName(serviceName);
            po.setTaxNo(taxesNo);
            po.setTaxName(erpCode);
            po.setTel(INVOICE_PHONE);
            po.setStatus(Constant.STATUS);
            po.setState(Constant.STATUS_ENABLE);

            dao.insert(po);

            POContext.endTxn(true);
        } else {
            StringBuffer sql2 = new StringBuffer();
            sql2.append(" update tt_part_bill_define t set t.tax_name='" + erpCode + "',");
            sql2.append(" tax_no='" + taxesNo + "',");
            sql2.append(" bank='" + BANK + "',");
            sql2.append(" account='" + INVOICE_ACCOUNT + "',");
            sql2.append(" addr='" + INVOICE_ADD + "',");
            sql2.append(" tel='" + INVOICE_PHONE + "'");
            sql2.append(" where t.dealer_id=" + dealerId);

            dao.update(sql2.toString(), null);
        }
        return list;
    }

    // 查询开票信息
    public Map<String, Object> getInvoiceInfo(String dealerId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T.TAX_NAME,\n");
        sql.append("       T.TAX_NO,\n");
        sql.append("       T.BANK,\n");
        sql.append("       T.ACCOUNT,\n");
        sql.append("       T.ADDR,\n");
        sql.append("       T.MAIL_ADDR,\n");
        sql.append("       T.TEL,\n");
        sql.append("       T.INV_TYPE\n");
        sql.append("  FROM tt_part_bill_define T\n");
        sql.append(" WHERE T.DEALER_ID = " + dealerId);

        return pageQueryMap(sql.toString(), null, getFunName());
    }

    public List<Map<String, Object>> dealerInfoDownload(Map<String, String> map) {
        String dealerClass = map.get("dealerClass");
        String dealerCode = map.get("dealerCode");
        String dealerName = map.get("dealerName");
        String dealerLevel = map.get("dealerLevel");
        String status = map.get("status");
        String sJdealerCode = map.get("sJdealerCode");
        String orgCode = map.get("orgCode");
        String dealerType = map.get("dealerType");
        String companyId = map.get("companyId");
        String oemCompanyId = map.get("oemCompanyId");
        String province = map.get("province");

        StringBuffer sql = new StringBuffer("\n");

        sql.append("SELECT D.REGION_NAME,\n");
        sql.append("       D.DEALER_ID,\n");
        sql.append("       D.DEALER_CODE,\n");
        sql.append("       D.DEALER_SHORTNAME,\n");
        sql.append("       TCC6.Code_Desc DEALER_CLASS,\n");
        sql.append("/*     D.DEALER_LEVEL,*/\n");
        sql.append("/*     D.DEALER_TYPE,*/\n");
        sql.append("/*      D.PARENT_DEALER_D,*/\n");
        sql.append("	TD1.DEALER_CODE PARENT_DEALER_CODE,\n");
        sql.append("       TCC1.CODE_DESC TYPE_DESC,\n");
        sql.append("       D.TAXES_NO,\n");
        sql.append("       TCC4.Code_Desc IMAGE_LEVEL,\n");
        sql.append("       TCC5.Code_Desc MAIN_RESOURCES,\n");
        sql.append("       D.STATUS,\n");
        sql.append("       TCC2.CODE_DESC STATUS_DESC,\n");
        sql.append("       TCC3.CODE_DESC LEVEL_DESC,\n");
        sql.append("       DECODE(TD1.DEALER_SHORTNAME, NULL, '', TD1.DEALER_SHORTNAME) AS SHANGJINAME,\n");
        sql.append("       D.SHANGJIORGNAME,\n");
        sql.append("       D.COMPANY_SHORTNAME,\n");
        sql.append("       D.PDEALER_TYPE,\n");
        sql.append("       TO_CHAR(D.CREATE_DATE, 'yyyy-MM-dd') AS CREATEDATE,\n");
        sql.append("       TO_CHAR(D.UPDATE_DATE, 'yyyy-MM-dd') AS UPDATEDATE,\n");
        sql.append("       DECODE(E.NAME, NULL, '', E.NAME) AS CREATEPER,\n");
        sql.append("       DECODE(F.NAME, NULL, '', F.NAME) AS UPDATEPER\n");
        sql.append("  FROM (SELECT TR.REGION_NAME,\n");
        sql.append("               TD.DEALER_ID,\n");
        sql.append("               TD.DEALER_ID,\n");
        sql.append("               TD.DEALER_CODE,\n");
        sql.append("               TD.DEALER_SHORTNAME,\n");
        sql.append("               TD.DEALER_CLASS,\n");
        sql.append("               TD.DEALER_TYPE,\n");
        sql.append("               TD.TAXES_NO,\n");
        sql.append("               TD.STATUS,\n");
        sql.append("               TD.DEALER_LEVEL,\n");
        sql.append("               TD.IMAGE_LEVEL,\n");
        sql.append("               TD.MAIN_RESOURCES,\n");
        sql.append("               TD.CREATE_BY,\n");
        sql.append("               TD.UPDATE_BY,\n");
        sql.append("               TD.CREATE_DATE,\n");
        sql.append("               TD.UPDATE_DATE,\n");
        sql.append("               TMO.ORG_NAME AS SHANGJIORGNAME,\n");
        sql.append("               TD.PARENT_DEALER_D,\n");
        sql.append("               TC.COMPANY_SHORTNAME\n");
        sql.append("          FROM TM_COMPANY             TC,\n");
        sql.append("               TM_ORG                 TMO,\n");
        sql.append("               TM_DEALER_ORG_RELATION TDOR,\n");
        sql.append("               TM_DEALER              TD,\n");
        sql.append("               Tm_Region              TR\n");
        sql.append("         WHERE TD.COMPANY_ID = TC.COMPANY_ID\n");

        if (dealerCode != null && !"".equals(dealerCode)) {
            sql.append("           AND TD.DEALER_CODE LIKE '%" + dealerCode + "%'\n");
        }
        if (dealerName != null && !"".equals(dealerName)) {
            sql.append("           AND TD.DEALER_SHORTNAME LIKE '%" + dealerName + "%'\n");
        }
        if (status != null && !"".equals(status)) {
            sql.append("           AND TD.STATUS = " + status + "\n");
        }
        if (dealerType != null && !"".equals(dealerType)) {
            sql.append("           AND TD.DEALER_TYPE = " + dealerType + "\n");
        }
        sql.append("           AND F_GET_PID(TD.DEALER_ID) = TDOR.DEALER_ID \n");
        sql.append("           AND TDOR.ORG_ID = TMO.ORG_ID\n");
        if (dealerLevel != null && !"".equals(dealerLevel)) {
            sql.append("           AND TD.DEALER_LEVEL = " + dealerLevel + "\n");
        }
        if (orgCode != null && !"".equals(orgCode)) {
            sql.append("     AND EXISTS (SELECT 1 FROM TM_DEALER_ORG_RELATION TDOR,TM_ORG TMO WHERE TDOR.ORG_ID=TMO.ORG_ID AND TMO.ORG_CODE='"
                    + orgCode + "' AND (TDOR.DEALER_ID=TD.DEALER_ID OR TDOR.DEALER_ID=TD.PARENT_DEALER_D))\n");
        }

        if (sJdealerCode != null && !"".equals(sJdealerCode)) {
            sql.append("           AND TD.PARENT_DEALER_D = (SELECT DEALER_ID FROM TM_DEALER WHERE DEALER_CODE ='" + sJdealerCode + "')\n");
        }
        if (companyId != null && !"".equals(companyId)) {
            sql.append("           AND TD.COMPANY_ID = " + companyId + "\n");
        }
        if (oemCompanyId != null && !"".equals(oemCompanyId)) {
            sql.append("           AND TD.OEM_COMPANY_ID = " + oemCompanyId + "\n");
        }
        if (dealerClass != null && dealerClass != "") {
            sql.append("          AND TD.DEALER_CLASS='" + dealerClass + "'\n");
        }
        if (province != null && province != "") {
            sql.append("          AND TD.PROVINCE_ID='" + province + "'\n");
        }
        sql.append("		   and TR.REGION_CODE = td.province_id\n");
        sql.append("           AND TD.COMPANY_ID = TC.COMPANY_ID)D\n");
        sql.append("LEFT JOIN TM_DEALER TD1 ON D.PARENT_DEALER_D = TD1.DEALER_ID\n");
        sql.append(" LEFT JOIN TC_USER E ON D.CREATE_BY = E.USER_ID\n");
        sql.append(" LEFT JOIN TC_USER F ON D.UPDATE_BY = F.USER_ID\n");
        sql.append(" LEFT JOIN TC_CODE TCC1 ON D.DEALER_TYPE = TCC1.CODE_ID\n");
        sql.append(" LEFT JOIN TC_CODE TCC2 ON D.STATUS = TCC2.CODE_ID\n");
        sql.append(" LEFT JOIN TC_CODE TCC3 ON D.DEALER_LEVEL = TCC3.CODE_ID\n");
        sql.append(" LEFT JOIN TC_CODE TCC4 ON D.IMAGE_LEVEL = TCC4.CODE_ID\n");
        sql.append(" LEFT JOIN TC_CODE TCC5 ON D.MAIN_RESOURCES = TCC5.CODE_ID\n");
        sql.append(" LEFT JOIN TC_CODE TCC6 ON D.DEALER_CLASS = TCC6.CODE_ID\n");
        sql.append("ORDER BY D.STATUS ASC, D.DEALER_ID ASC\n");

        return super.pageQuery(sql.toString(), null, super.getFunName());
    }

    /**
     * 新增经销商时查询经销商的公司的组织Id
     *
     * @param companyId 公司Id
     * @return
     * @throws Exception
     */
    public List<TmOrgPO> getOrgInfo(Long companyId) throws Exception {

        String query = " SELECT TMO.ORG_ID FROM TM_ORG TMO WHERE TMO.COMPANY_ID = " + companyId;

        logger.debug("SQL+++++++++++++++++++++++: " + query);
        return factory.select(query, null, new DAOCallback<TmOrgPO>() {
            public TmOrgPO wrapper(ResultSet rs, int idx) {
                TmOrgPO bean = new TmOrgPO();
                try {
                    bean.setOrgId(rs.getLong("ORG_ID"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return bean;
            }
        });
    }

    /**
     * 根据经销商Id查询经销商详情
     *
     * @param dealerId
     * @return
     */
    public Map<String, Object> getDealerInfobyId(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT D.*,\n");
        sql.append("       TD.DEALER_ID AS SJDEALERID,\n");
        sql.append("       TD.DEALER_CODE AS SJDEALERCODE,\n");
        sql.append("       DECODE(D.DEALER_LEVEL,\n");
        sql.append("              10851001,\n");
        sql.append("              (SELECT TMO.ORG_ID\n");
        sql.append("                 FROM TM_ORG TMO, TM_DEALER_ORG_RELATION TDOR\n");
        sql.append("                WHERE TMO.ORG_ID = TDOR.ORG_ID\n");
        sql.append("                  AND TDOR.DEALER_ID = D.DEALER_ID),\n");
        sql.append("              '') AS ORG_ID,\n");
        sql.append("       DECODE(D.DEALER_LEVEL,\n");
        sql.append("              10851001,\n");
        sql.append("              (SELECT TMO.ORG_CODE\n");
        sql.append("                 FROM TM_ORG TMO, TM_DEALER_ORG_RELATION TDOR\n");
        sql.append("                WHERE TMO.ORG_ID = TDOR.ORG_ID\n");
        sql.append("                  AND TDOR.DEALER_ID = D.DEALER_ID),\n");
        sql.append("              '') AS ORG_CODE\n");
        sql.append("  FROM (SELECT TD.DEALER_ID,\n");
        sql.append("               TD.COMPANY_ID,\n");
        sql.append("               TD.DEALER_TYPE,\n");
        sql.append("               TD.DEALER_CODE,\n");
        sql.append("               TD.DEALER_NAME,\n");
        sql.append("               TD.DEALER_CLASS,\n");
        sql.append("               TD.STATUS,\n");
        sql.append("               TD.DEALER_LEVEL,\n");
        sql.append("               TD.PARENT_DEALER_D,\n");
        sql.append("               TD.DEALER_ORG_ID,\n");
        sql.append("               TD.PROVINCE_ID,\n");
        sql.append("               TD.CITY_ID,\n");
        sql.append("               TD.ZIP_CODE,\n");
        sql.append("               TD.ADDRESS,\n");
        sql.append("               TD.PHONE,\n");
        sql.append("               TD.FAX_NO,\n");
        sql.append("               TD.LINK_MAN,\n");
        sql.append("               TD.EMAIL,\n");
        sql.append("               TD.REMARK,\n");
        sql.append("               TD.DEALER_SHORTNAME,\n");
        sql.append("               TD.OEM_COMPANY_ID,\n");
        sql.append("               TD.PRICE_ID,\n");
        sql.append("               TC.COMPANY_SHORTNAME,\n");
        sql.append("               TD.ERP_CODE,\n");
        sql.append("               TD.TAXES_NO,td.dealer_labour_type,\n");
        sql.append("               TD.IS_DQV,\n");
        sql.append("               TD.BALANCE_LEVEL,\n");
        sql.append("               TO_CHAR(TD.CREATE_DATE,'YYYYMM') CREATE_DATE,\n");
        sql.append("               TD.INVOICE_LEVEL,TD.COUNTIES,TD.TOWNSHIP,TD.LEGAL,TD.WEBMASTER_PHONE,TD.DUTY_PHONE,TD.BANK,TD.MAIN_RESOURCES,TD.ADMIN_LEVEL,TD.IMAGE_LEVEL,TD.TAX_LEVEL,TD.INVOICE_ACCOUNT,TD.INVOICE_ADD,TD.INVOICE_PHONE,TD.INVOICE_POST_ADD,TD.PDEALER_TYPE\n");
        sql.append("          FROM TM_DEALER TD, TM_COMPANY TC\n");
        sql.append("         WHERE TD.COMPANY_ID = TC.COMPANY_ID\n");
        sql.append("           AND TD.DEALER_ID = " + dealerId + ") D\n");
        sql.append("  LEFT JOIN TM_DEALER TD ON D.PARENT_DEALER_D = TD.DEALER_ID\n");
        logger.debug("SQL+++++++++++++++++++++++: " + sql.toString());
        Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
        return map;
    }

    /**
     * 根据地址Id查询经销商地址详情
     *
     * @param id
     * @return
     */
    public List<Map<String, Object>> getAddressfobyId(String id) {
        StringBuffer sql = new StringBuffer();
        sql.append("select tt_part_addr_define.dealer_id,tt_part_addr_define.dealer_code,tt_part_addr_define.dealer_name,addr,linkman,tel,post_code,station,fax,tt_part_addr_define.remark,tt_part_addr_define.status from tt_part_addr_define,TM_DEALER TD WHERE tt_part_addr_define.Dealer_Id=TD.DEALER_ID  and tt_part_addr_define.ADDR_ID ='"
                + id + "'");
        // sql.append("select * from tt_part_addr_define td  where td.addr_id ='"
        // + id + "' ");
        logger.debug("SQL+++++++++++++++++++++++: " + sql.toString());
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public void chkAddressSame() {
        RequestWrapper request = new ActionContext().getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        // String flagStr = null ;
        String addressId = CommonUtils.checkNull(request.getParamValue("addressId"));
        String address = CommonUtils.checkNull(request.getParamValue("address")).trim();
        // address = getAddDet(province, city, area) + address ;
        String dealerId = logonUser.getDealerId();
        Map<String, String> map = new HashMap<String, String>();
        map.put("dealerId", dealerId);
        map.put("address", address);
        // 新增
        if (null != addressId && !"".equals(addressId)) {
            map.put("addressId", addressId);
        }
        List<Map<String, Object>> list = dao.chkSame(map);
        if (null != list && list.size() > 0) {
            list.get(0).get("");
        }
        // act.setOutData("flagStr", flagStr) ;
    }

    public List<Map<String, Object>> chkSame(Map<String, String> map) {
        String addressId = map.get("addressId");
        String address = map.get("address");
        String dealerId = map.get("dealerId");

        StringBuffer sql = new StringBuffer("\n");

        sql.append("select tva.addr_id,tva.addr,tva.dealer_id \n");
        sql.append("  from tt_part_addr_define tva \n");
        sql.append(" where 1 = 1\n");
        // sql.append("   and tva.b_area_id = tba.area_id(+)\n");
        sql.append("   and tva.addr = '").append(address).append("'\n");
        sql.append("   and tva.dealer_id = ").append(dealerId).append("\n");
        // if(!CommonUtils.isNullString(addressId)) {
        // sql.append("   and tva.id <> ").append(addressId).append("\n");
        // }
        if (null != addressId && !"".equals(addressId)) {
            sql.append("   and tva.addr_id <> ").append(addressId).append("\n");
        }
        return super.pageQuery(sql.toString(), null, super.getFunName());
    }

    public static void main(String[] args) {

        // dao.getAddressfobyId("2013042819355926");
    }

    public int setDownDealerPrice(String erpCode) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE t_dealer_account_details_tmp TDADT SET TDADT.REMARK='',TDADT.EXPORT_STATUS=0\n");
        sql.append("WHERE TDADT.REMARK LIKE '%XXX%' AND TDADT.EXPORT_STATUS=3  AND TDADT.DEALER_ERP_ORG_CODE='" + erpCode + "'");
        return factory.update(sql.toString(), null);

    }

    /**
     * 查询经销商业务范围
     *
     * @param dealerId 经销商Id
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getDealerBusinessArea(String dealerId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TDBA.RELATION_ID,\n");
        sql.append("       TDBA.DEALER_ID,\n");
        sql.append("       TDBA.AREA_ID,\n");
        sql.append("       TBA.AREA_CODE,\n");
        sql.append("       TBA.AREA_NAME\n");
        sql.append("  FROM TM_DEALER_BUSINESS_AREA TDBA, TM_BUSINESS_AREA TBA\n");
        sql.append(" WHERE TDBA.AREA_ID = TBA.AREA_ID\n");
        sql.append("   AND TDBA.DEALER_ID = " + dealerId + "\n");
        logger.debug("SQL+++++++++++++++++++++++: " + sql.toString());
        return pageQuery(sql.toString(), null, getFunName());
    }

    /**
     * 查询经销商地址
     *
     * @param dealerId 经销商Id
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getAddress(String dealerId) throws Exception {

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T.ADDR_ID,\n");
        sql.append("       T.ADDR,\n");
        sql.append("       T.LINKMAN,\n");
        sql.append("       T.TEL,\n");
        sql.append("       T.STATUS,T.STATE\n");
        sql.append("  FROM tt_part_addr_define t\n");
        sql.append(" WHERE T.DEALER_ID = " + dealerId);
        logger.debug("SQL+++++++++++++++++++++++查询经销商地址: " + sql.toString());
        return pageQuery(sql.toString(), null, getFunName());
    }

    /*
     * 通过经销商ID查询其下级经销商
     * @dealerIds 经销商ID
     */
    public List<Map<String, Object>> getLowDelaerInfo(String dealerIds) {
        StringBuffer sql = new StringBuffer("");

        sql.append("select td2.dealer_name, td2.dealer_id,td2.parent_dealer_d\n");
        sql.append("  from tm_dealer td1, tm_dealer td2\n");
        sql.append(" where td2.parent_dealer_d = td1.dealer_id\n");
        sql.append(" and td1.dealer_id in (" + dealerIds + ")\n");
        sql.append(" and td1.status = " + Constant.STATUS_ENABLE + "\n");
        sql.append(" and td2.status = " + Constant.STATUS_ENABLE + "\n");
        sql.append(" order by td2.dealer_name\n");

        return dao.pageQuery(sql.toString(), null, getFunName());
    }

    /*
     * 经销商查询
     * @dealerId 本级经销商Id
     * @dealerIds 上级经销商Id
     * @dealerLevel 需查询经销商级别
     * @dealerType 经销商类型
     */
    public List<Map<String, Object>> getDel(String dealerId, String dealerIds, String dealerLevel, String dealerType) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT  TD.DEALER_NAME, \n");
        sql.append("		TD.DEALER_CODE, \n");
        sql.append("		TD.DEALER_ID \n");
        sql.append("FROM	TM_DEALER TD \n");
        sql.append("WHERE	TD.STATUS = " + Constant.STATUS_ENABLE + " \n");

        if (!"".equals(dealerId)) {
            sql.append("	AND	TD.DEALER_ID IN (" + dealerId + ") \n");
        }
        if (!"".equals(dealerLevel)) {
            sql.append("	AND	TD.DEALER_LEVEL = " + dealerLevel.trim() + " \n");
        }
        if (!"".equals(dealerIds)) {
            sql.append("	AND	TD.PARENT_DEALER_D IN (" + dealerIds + ") \n");
        }
        if (!"".equals(dealerType)) {
            sql.append("	AND	TD.DEALER_TYPE = " + dealerType.trim() + " \n");
        }

        return dao.pageQuery(sql.toString(), null, getFunName());
    }


    /**
     * 获取当前经销商的下级经销商
     *
     * @param dealerId
     * @return
     */
    public List<Map<String, Object>> getDel(String dealerId) {
        StringBuffer sql = new StringBuffer("\n");

        sql.append("SELECT TMD2.DEALER_ID, TMD2.DEALER_NAME\n");
        sql.append("  FROM TM_DEALER TMD1, TM_DEALER TMD2\n");
        sql.append(" WHERE TMD1.DEALER_ID = TMD2.PARENT_DEALER_D\n");
        sql.append("   AND TMD1.STATUS = " + Constant.STATUS_ENABLE + "\n");
        sql.append("   AND TMD2.STATUS = " + Constant.STATUS_ENABLE + "\n");
        sql.append("   AND TMD1.DEALER_ID IN (" + dealerId + ")\n");

        return dao.pageQuery(sql.toString(), null, getFunName());
    }

    /**
     * 获取当前经销商的下级经销商
     *
     * @param dealerId
     * @return
     */
    public String getDel__(String dealerId) {
        StringBuffer sql = new StringBuffer("\n");

        sql.append("SELECT TMD2.DEALER_ID, TMD2.DEALER_NAME\n");
        sql.append("  FROM TM_DEALER TMD1, TM_DEALER TMD2\n");
        sql.append(" WHERE TMD1.DEALER_ID = TMD2.PARENT_DEALER_D\n");
        sql.append("   AND TMD1.STATUS = " + Constant.STATUS_ENABLE + "\n");
        sql.append("   AND TMD2.STATUS = " + Constant.STATUS_ENABLE + "\n");
        sql.append("   AND TMD1.DEALER_ID IN (" + dealerId + ")\n");

        List<Map<String, Object>> dealerList = dao.pageQuery(sql.toString(), null, getFunName());

        StringBuffer dealer = new StringBuffer("");
        if (null != dealerList && dealerList.size() > 0) {
            int len = dealerList.size();

            for (int i = 0; i < len; i++) {
                if ("".equals(dealer.toString())) {
                    dealer.append(dealerList.get(i).get("DEALER_ID"));
                } else {
                    dealer.append(",").append(dealerList.get(i).get("DEALER_ID"));
                }
            }
        }

        return dealer.toString();
    }

    /**
     * 通过公司id获取该公司下的所有经销商
     *
     * @param comId
     * @return
     */
    public List<Map<String, Object>> getDelList(String comId) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT TMD.DEALER_ID, TMD.DEALER_CODE, TMD.DEALER_NAME\n");
        sql.append("FROM TM_DEALER TMD\n");
        sql.append("WHERE TMD.COMPANY_ID = " + comId + "\n");

        return dao.pageQuery(sql.toString(), null, getFunName());
    }


    /**
     * 获得当前经销商所属区域
     *
     * @param delId
     * @return
     */
    public Long getOrgId(String delId) {
        Long orgId = new Long("1");
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT TMDOR.root_org_id org_id, TMDOR.root_org_code org_code, TMDOR.root_org_name org_name, TMDOR.DEALER_ID\n");
        sql.append("  FROM vw_org_dealer TMDOR\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND TMDOR.DEALER_ID IN (" + delId + ")\n");

        List<Map<String, Object>> listOrg = dao.pageQuery(sql.toString(), null, getFunName());

        if (null != listOrg && listOrg.size() > 0) {
            orgId = Long.parseLong(listOrg.get(0).get("ORG_ID").toString());
        }

        return orgId;
    }


    public PageResult<Map<String, Object>> queryAuthDealerChange(String code, String status, String startDate, String endDate, int curPage,
                                                                 int pageSize) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * from tm_dealer_change a where 1=1\n");
        if (Utility.testString(code)) {
            sql.append("and a.dealer_num like '" + code + "' \n");
        }
        if (Utility.testString(status)) {
            sql.append("and a.status = " + status + " \n");
        }
        if (Utility.testString(startDate)) {
            sql.append("AND A.dealer_time >= TO_DATE('" + startDate + "','YYYY-MM-DD HH24:MI:SS')\n");
        }
        if (Utility.testString(endDate)) {
            sql.append("AND A.dealer_time <= TO_DATE('" + endDate + "','YYYY-MM-DD HH24:MI:SS')\n");
        }
        PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return rs;
    }

    public List<Map<String, Object>> selectDealerInfo(long dealerId) {
        StringBuffer sql = new StringBuffer("");
        sql.append("select b.root_org_name,b.root_org_code,c.company_shortname,a.*,(select ee.dealer_name from  tm_dealer ee where ee.dealer_id=a.parent_dealer_d) as parent_name from tm_dealer a,vw_org_dealer_service b,TM_COMPANY c where a.dealer_id=b.dealer_id and a.company_id=c.company_id and a.dealer_id="
                + dealerId + "\n");
        return dao.pageQuery(sql.toString(), null, getFunName());
    }

    public List<Map<String, Object>> selectDealerUpdateAuthing(String dealerId, String yilyle) {
        String sql = "select a.*,u.namE from TT_AS_DEALER_TYPE_AUTHITEM a,tc_user u   where a.dealer_id=" + dealerId + " and a.yilie=" + yilyle
                + " and a.create_by=u.user_id order by a.create_date  ";

        return dao.pageQuery(sql.toString(), null, getFunName());
    }

    public Map<String, Object> getDlrClass(String dealerId) {
        StringBuffer sql = new StringBuffer("\n");

        sql.append("select tmd.dealer_class from tm_dealer tmd where tmd.dealer_id = ").append(dealerId).append(" ;");

        return super.pageQueryMap(sql.toString(), null, super.getFunName());
    }

}
