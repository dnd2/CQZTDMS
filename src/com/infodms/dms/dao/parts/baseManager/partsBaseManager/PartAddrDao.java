package com.infodms.dms.dao.parts.baseManager.partsBaseManager;

import com.ibm.disthubmq.impl.formats.Multi.VMNumbering.numbers;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-4-10
 * @ClassName : PartAddrDao
 * @Description :
 */
public class PartAddrDao extends BaseDao {


    public static Logger logger = Logger.getLogger(PartAddrDao.class);

    private static final PartAddrDao dao = new PartAddrDao();

    private PartAddrDao() {

    }

    public static final PartAddrDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> queryPartAddrList(RequestWrapper request,
                                                             Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        StringBuffer sql = new StringBuffer("");
        String dealerName = CommonUtils.checkNull(request
                .getParamValue("DEALER_NAME"));//   name
        String dealerCode = CommonUtils.checkNull(request
                .getParamValue("DEALER_CODE")).toUpperCase();//  code
        try {

            sql.append("SELECT T.ADDR_ID,\n");
            sql.append("       T.DEALER_ID,\n");
            sql.append("       T.DEALER_CODE,\n");
            sql.append("       T.DEALER_NAME,\n");
            sql.append("       T.ADDR,\n");
            sql.append("       T.LINKMAN,\n");
            sql.append("       T.TEL,\n");
            sql.append("       T.POST_CODE,\n");
            sql.append("       T.STATION,\n");
            sql.append("       T.FAX,\n");
            sql.append("       T.REMARK,\n");
            sql.append("       T.CREATE_DATE,\n");
            sql.append("       T.UPDATE_DATE,\n");
            sql.append("       T.STATE,\n");
            sql.append("       T.LINKMAN2,\n");
            sql.append("       T.TEL2,\n");
            sql.append("       T.IS_DEFAULT_ADDR,\n");
            sql.append("       (SELECT R.REGION_NAME\n");
            sql.append("          FROM TM_REGION R\n");
            sql.append("         WHERE R.REGION_CODE = T.PROVINCE_ID) PROVINCE_ID,\n");
            sql.append("       (SELECT R.REGION_NAME\n");
            sql.append("          FROM TM_REGION R\n");
            sql.append("         WHERE R.REGION_CODE = T.CITY_ID) CITY_ID,\n");
            sql.append("       (SELECT R.REGION_NAME\n");
            sql.append("          FROM TM_REGION R\n");
            sql.append("         WHERE R.REGION_CODE = T.COUNTIES) COUNTIES\n");
            sql.append("  FROM TT_PART_ADDR_DEFINE T\n");
            sql.append(" WHERE T.STATUS = 1 ");
            if (!dealerName.equals("") && dealerName != null) {
                sql.append("AND T.DEALER_NAME LIKE '%").append(dealerName).append("%'\n");
            }
            if (!dealerCode.equals("") && dealerCode != null) {
                sql.append("AND T.DEALER_CODE LIKE '%").append(dealerCode).append("%'\n");
            }
            sql.append("ORDER BY T.CREATE_DATE DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public Map<String, Object> getPartAddrDetail(String addrId) throws Exception {
        Map<String, Object> map;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.ADDR_ID,T.DEALER_ID,T.DEALER_CODE,T.DEALER_NAME,T.ADDR,T.LINKMAN,T.TEL,T.POST_CODE,");
            sql.append("       T.STATION,T.FAX,T.REMARK,T.LINKMAN2,T.TEL2,T.PROVINCE_ID,T.CITY_ID,T.COUNTIES, T.IS_DEFAULT_ADDR");
            sql.append(" FROM TT_PART_ADDR_DEFINE T WHERE T.ADDR_ID = ").append(addrId);
            map = pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return map;
    }
    
    /**
     * <p>Description: 根据参数查询经销商地址</p>
     * @param paramMap 查询参数（dealerId：服务站ID，isDeafaultAddr：默认地址标志, notExistsAddrId:不包含的地址id ）
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getDealerPartAddrList(Map<String, String> paramMap) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT T.ADDR_ID, T.DEALER_ID, T.DEALER_CODE, T.DEALER_NAME \n");
        sql.append("   FROM TT_PART_ADDR_DEFINE T \n");
        sql.append("  WHERE DEALER_ID = "+paramMap.get("dealerId")+" \n");
        if(StringUtils.isNotEmpty(paramMap.get("isDefaultAddr"))){
            sql.append(" AND T.IS_DEFAULT_ADDR = "+paramMap.get("isDefaultAddr")+" \n");
        }
        if(StringUtils.isNotEmpty(paramMap.get("notExistsAddrId"))){
            sql.append(" AND T.ADDR_ID <> "+paramMap.get("notExistsAddrId")+" \n");
        }
        List<Map<String, Object>> addrList = pageQuery(sql.toString(), null, getFunName());
        return addrList;
    }

    /**
     * <p>Description: 根据经销商取消默认发运地址</p>
     * @param paramMap（user：当前登录用户，dealerId：经销商ID，notInAddId：不包含的地址id ）
     */
    @SuppressWarnings("unchecked")
    public void updatePartDefaultAddr(Map<String, String> paramMap){
        StringBuffer sql = new StringBuffer();
        sql.append("  \n");
        sql.append(" UPDATE TT_PART_ADDR_DEFINE \n");
        sql.append("    SET IS_DEFAULT_ADDR = "+Constant.IF_TYPE_NO+", \n");
        sql.append("        UPDATE_BY = "+paramMap.get("user")+", \n");
        sql.append("        UPDATE_DATE = SYSDATE \n");
        sql.append("  WHERE DEALER_ID = "+paramMap.get("dealerId")+" \n");
        sql.append("    AND IS_DEFAULT_ADDR = "+Constant.IF_TYPE_YES+" \n");
        if(StringUtils.isNotEmpty(paramMap.get("notInAddId"))){
            sql.append("    AND ADD_ID <> "+paramMap.get("notInAddId")+" \n");
        }
        dao.update(sql.toString(), null);
    }
    
    public List<Map<String, Object>> queryPartAddr(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {

            String dealerName = CommonUtils.checkNull(request
                    .getParamValue("DEALER_NAME"));// 采购单位名称
            String dealerCode = CommonUtils.checkNull(request
                    .getParamValue("DEALER_CODE"));// 采购单位名称
            StringBuilder sql = new StringBuilder("");
            sql.append("SELECT T.DEALER_CODE,T.DEALER_NAME,T.ADDR,T.LINKMAN,T.TEL,T.POST_CODE,T.STATION,T.FAX");
            sql.append(" FROM TT_PART_ADDR_DEFINE T WHERE 1=1 ");
            if (!"".equals(dealerName) && dealerName != null) {
                sql.append(" AND T.DEALER_NAME like '%").append(dealerName).append("%'");
            }
            if (!"".equals(dealerCode) && dealerCode != null) {
                sql.append(" AND T.DEALER_CODE like '%").append(dealerCode).append("%'");
            }
            sql.append(" ORDER BY T.CREATE_DATE DESC");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;
    }

}
