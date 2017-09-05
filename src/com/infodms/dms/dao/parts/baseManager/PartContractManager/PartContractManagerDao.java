package com.infodms.dms.dao.parts.baseManager.PartContractManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/5.
 */
@SuppressWarnings("unchecked")
public class PartContractManagerDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartContractManagerDao.class);
    private static final PartContractManagerDao dao = new PartContractManagerDao();

    private PartContractManagerDao() {
    }

    public static final PartContractManagerDao getInstance() {
        return dao;
    }

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }


    /**

     * @return :
     * @throws : LastDate    : 2013-4-10
     * @Title : 备件采购属性查询
     */
    public PageResult<Map<String, Object>> getContractQuery(String venderCode, String venderName, String ConNumber,
                                                          String  partCode,String partName, String partOldcode, String ConType, String sDate
                                                           ,String vtype ,String isOut,int pageSize, int curPage,String state,String isTemp) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT DISTINCT  TO_CHAR(CD.CONTRACT_EDATE,'yyyy-mm-dd')CONTRACT_EDATE,\n");
        sql.append("       TO_CHAR(CD.CONTRACT_SDATE,'yyyy-mm-dd')CONTRACT_SDATE,\n");
        sql.append("       CD.CONTRACT_NUMBER,\n");
        sql.append("       CD.VENDER_ID,\n");
        sql.append("       VD.VENDER_CODE,\n");
        sql.append("       VD.VENDER_NAME,\n");
        sql.append("       TC.NAME,\n");
        sql.append("       CD.REMARK,\n");
        sql.append("       DECODE(CD.CONTRACT_TYPE,"+Constant.CONTRACT_TYPE_01+",'基本合同',"+Constant.CONTRACT_TYPE_02+",'订单合同')CONTRACT_TYPE,\n");
//        if (null != isOut && !isOut.equals("")) {
//            if(isOut.equals(Constant.IF_TYPE_NO.toString())){
//                sql.append(" '否' IS_OUT,");
//            }else{
//                sql.append(" '是' IS_OUT,");
//            }
//        }
        sql.append("       CASE WHEN SYSDATE BETWEEN CD.CONTRACT_SDATE AND CD.CONTRACT_EDATE THEN '否'  ELSE '是' END IS_OUT,");
        sql.append("       DECODE(CD.STATE,"+Constant.STATUS_ENABLE+",'有效',"+Constant.STATUS_DISABLE+",'无效') STATE,\n");
        sql.append("       DECODE(CD.ISTEMP,"+Constant.IF_TYPE_YES+",'是',"+Constant.IF_TYPE_NO+",'否')ISTEMP\n");
        sql.append("  FROM TT_PART_CONTRACT_DEFINE CD,\n");
        sql.append("   TT_PART_VENDER_DEFINE VD,\n");
        sql.append("   TC_USER TC\n");
        sql.append(" WHERE VD.VENDER_ID = CD.VENDER_ID");
        sql.append(" AND CD.CREATE_BY = TC.USER_ID(+)");
        sql.append(" AND CD.STATUS=1");
        List<String> list =new ArrayList<String>();
        if (null != ConNumber && !ConNumber.equals("")) {
            sql.append(" AND UPPER(CD.CONTRACT_NUMBER) LIKE UPPER(?) ");
            list.add("%"+ConNumber+"%");
        }
        if (null != venderCode && !venderCode.equals("")) {
            sql.append(" AND UPPER(VD.VENDER_CODE) LIKE UPPER(?) ");
            list.add("%"+venderCode+"%");
        }
        if (null != vtype && !vtype.equals("")) {
            sql.append(" AND VD.VENDER_TYPE = ? ");
            list.add(""+vtype+"");
        }
        if (null != venderName && !venderName.equals("")) {
            sql.append(" AND VD.VENDER_NAME LIKE UPPER(?)");
            list.add("%"+venderName+"%");
        }
        if (null != ConType && !ConType.equals("")) {
            sql.append(" AND CD.CONTRACT_TYPE=?");
            list.add(ConType);
        }
        if (null != state && !state.equals("")) {
            sql.append(" AND CD.STATE=?");
            list.add(state);
        }
        if (!"".equals(sDate)) {
            sql.append(" and  to_date(? ,'yyyy-mm-dd') between CD.CONTRACT_SDATE and CD.CONTRACT_EDATE ");
            list.add( ""+sDate+"" );
        }
        if (null != isOut && !isOut.equals("")) {
            if(isOut.equals(Constant.IF_TYPE_NO.toString())){
                sql.append(" AND sysdate  between CD.CONTRACT_SDATE and CD.CONTRACT_EDATE");
            }else{
                sql.append(" AND (CD.CONTRACT_SDATE > sysdate or CD.CONTRACT_EDATE < sysdate)");
            }
        }
        if (null != isTemp && !("").equals(isTemp)) {
            sql.append(" AND CD.ISTEMP=?");
            list.add(isTemp);
        }
        sql.append("  ORDER　BY CD.CONTRACT_NUMBER");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(),list,getFunName(), pageSize, curPage);
        return ps;
    }

    /**

     * @return :
     * @throws : LastDate    : 2013-4-10
     * @Title : 备件采购属性查询
     */
    public PageResult<Map<String, Object>> getJBContractQuery(String venderCode, String venderName, String ConNumber,
                                                            String  partCode,String partName, String partOldcode, String ConType, String sDate
            ,String vtype ,String isOut,int pageSize, int curPage,String State,String isTemp) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT DISTINCT  TO_CHAR(CD.CONTRACT_EDATE,'yyyy-mm-dd')CONTRACT_EDATE,\n");
        sql.append("       TO_CHAR(CD.CONTRACT_SDATE,'yyyy-mm-dd')CONTRACT_SDATE,\n");
        sql.append("       CD.CONTRACT_NUMBER,\n");
        sql.append("       CD.VENDER_ID,\n");
        sql.append("       VD.VENDER_CODE,\n");
        sql.append("       VD.VENDER_NAME,\n");
        sql.append("       TC.NAME,\n");
        sql.append("       CD.REMARK,\n");
        sql.append("       DECODE(CD.CONTRACT_TYPE,"+Constant.CONTRACT_TYPE_01+",'基本合同',"+Constant.CONTRACT_TYPE_02+",'订单合同')CONTRACT_TYPE,\n");
        if (null != isOut && !isOut.equals("")) {
            if(isOut.equals(Constant.IF_TYPE_NO.toString())){
                sql.append(" '否' IS_OUT,");
            }else{
                sql.append(" '是' IS_OUT,");
            }
        }
        sql.append("       DECODE(CD.STATE,"+Constant.STATUS_ENABLE+",'有效',"+Constant.STATUS_DISABLE+",'无效')STATE,\n");
        sql.append("       DECODE(CD.ISTEMP,"+Constant.IF_TYPE_YES+",'是',"+Constant.IF_TYPE_NO+",'否')ISTEMP\n");
        sql.append("  FROM TT_PART_CONTRACT_DEFINE CD,\n");
        sql.append("   TT_PART_VENDER_DEFINE VD,\n");
        sql.append("   TC_USER TC\n");
        sql.append(" WHERE VD.VENDER_ID = CD.VENDER_ID");
        sql.append(" AND CD.CREATE_BY = TC.USER_ID(+)");
        sql.append(" AND CD.CONTRACT_TYPE="+Constant.CONTRACT_TYPE_01+"");
        List<String> list =new ArrayList<String>();
        if (null != ConNumber && !ConNumber.equals("")) {
            sql.append(" AND UPPER(CD.CONTRACT_NUMBER) LIKE UPPER(?) ");
            list.add("%"+ConNumber+"%");
        }
        if (null != venderCode && !venderCode.equals("")) {
            sql.append(" AND UPPER(VD.VENDER_CODE) LIKE UPPER(?) ");
            list.add("%"+venderCode+"%");
        }
        if (null != vtype && !vtype.equals("")) {
            sql.append(" AND VD.VENDER_TYPE = ? ");
            list.add(""+vtype+"");
        }
        if (null != venderName && !venderName.equals("")) {
            sql.append(" AND VD.VENDER_NAME LIKE UPPER(?)");
            list.add("%"+venderName+"%");
        }
        if (null != ConType && !ConType.equals("")) {
            sql.append(" AND CD.CONTRACT_TYPE=?");
            list.add(ConType);
        }
        if(StringUtil.notNull(State)){
            sql.append(" AND CD.STATE=?");
            list.add(State);
        }
        if (!"".equals(sDate)) {
            sql.append(" and  to_date(? ,'yyyy-mm-dd') between CD.CONTRACT_SDATE and CD.CONTRACT_EDATE ");
            list.add( ""+sDate+"" );
        }
        if (null != isOut && !isOut.equals("")) {
            if(isOut.equals(Constant.IF_TYPE_NO.toString())){
                sql.append(" AND sysdate  between CD.CONTRACT_SDATE and CD.CONTRACT_EDATE");
            }else{
                sql.append(" AND (CD.CONTRACT_SDATE > sysdate or CD.CONTRACT_EDATE < sysdate)");
            }
        }
        if(StringUtil.notNull(isTemp)){
            sql.append(" AND CD.ISTEMP=?");
            list.add(isTemp);
        }
        sql.append("  ORDER　BY CD.CONTRACT_NUMBER");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(),list,getFunName(), pageSize, curPage);
        return ps;
    }
    public PageResult<Map<String, Object>> getOTContractQuery(RequestWrapper request,int pageSize, int curPage) {
        String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE")); //供应商编码
        String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));//供应商名称
        String ConNumber = CommonUtils.checkNull(request.getParamValue("CONTRACT_NUMBER")); //合同号
        String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //备件件号
        String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME"));//备件名称
        String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); //备件编码
        String ConType = CommonUtils.checkNull(request.getParamValue("CONTRACT_TYPE")); //合同类型
        String sDate = CommonUtils.checkNull(request.getParamValue("STARTDATE")); //合同开始时间
        String vType = CommonUtils.checkNull(request.getParamValue("VENDER_TYPE")); //供应商类型
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT D.*,\n" );
        sql.append("       V.VENDER_CODE,\n" );
        sql.append("       V.VENDER_NAME,\n" );
        sql.append("       P.PART_OLDCODE,\n" );
        sql.append("       P.PART_CODE,\n" );
        sql.append("       P.PART_CNAME\n" );
        sql.append("  FROM TT_PART_CONTRACT_DEFINE D,\n" );
        sql.append("       (SELECT DISTINCT MAX(CONTRACT_EDATE) CONTRACT_EDATE,\n" );
        sql.append("                        PART_ID,\n" );
        sql.append("                        VENDER_ID\n" );
        sql.append("          FROM TT_PART_CONTRACT_DEFINE C\n" );
        sql.append("         WHERE C.STATE = 10011001\n" );
        sql.append("         GROUP BY PART_ID, VENDER_ID) C,\n" );
        sql.append("       TT_PART_VENDER_DEFINE V,\n" );
        sql.append("       TT_PART_DEFINE P\n" );
        sql.append(" WHERE C.PART_ID = D.PART_ID\n" );
        sql.append("   AND D.VENDER_ID = V.VENDER_ID\n" );
        sql.append("   AND P.PART_ID = D.PART_ID\n" );
        sql.append("   AND C.VENDER_ID = D.VENDER_ID\n" );
        sql.append("   AND D.CONTRACT_EDATE = C.CONTRACT_EDATE\n" );
        sql.append("   AND MONTHS_BETWEEN(TRUNC(D.CONTRACT_EDATE, 'mm'), TRUNC(SYSDATE, 'mm')) <= 3\n" );
        sql.append("   AND MONTHS_BETWEEN(TRUNC(D.CONTRACT_EDATE, 'mm'), TRUNC(SYSDATE, 'mm')) > 0");

        List<String> list =new ArrayList<String>();
        if (null != ConNumber && !ConNumber.equals("")) {
            sql.append(" AND UPPER(D.CONTRACT_NUMBER) LIKE UPPER(?) ");
            list.add("%"+ConNumber+"%");
        }
        if (null != venderCode && !venderCode.equals("")) {
            sql.append(" AND UPPER(V.VENDER_CODE) LIKE UPPER(?) ");
            list.add("%"+venderCode+"%");
        }
        if (null != partCode && !partCode.equals("")) {
            sql.append(" AND UPPER(P.PART_CODE) LIKE UPPER(?) ");
            list.add("%"+partCode+"%");
        }
        if (null != partName && !partName.equals("")) {
            sql.append(" AND UPPER(P.PART_CNAME) LIKE UPPER(?) ");
            list.add("%"+partName+"%");
        }
        if (null != partOldcode && !partOldcode.equals("")) {
            sql.append(" AND UPPER(P.PART_OLDCODE) LIKE UPPER(?) ");
            list.add("%"+partOldcode+"%");
        }
        if (null != vType && !vType.equals("")) {
            sql.append(" AND V.VENDER_TYPE = ? ");
            list.add(""+vType+"");
        }
        if (null != venderName && !venderName.equals("")) {
            sql.append(" AND V.VENDER_NAME LIKE UPPER(?)");
            list.add("%"+venderName+"%");
        }
        if (null != ConType && !ConType.equals("")) {
            sql.append(" AND D.CONTRACT_TYPE=?");
            list.add(ConType);
        }
        if (!"".equals(sDate)) {
            sql.append(" and  to_date(? ,'yyyy-mm-dd') between D.CONTRACT_SDATE and CD.CONTRACT_EDATE ");
            list.add( ""+sDate+"" );
        }
        sql.append("  ORDER　BY D.CONTRACT_NUMBER");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(),list,getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> getNoSignContract(RequestWrapper request,int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE")); //供应商编码
        String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));//供应商名称
        String vType = CommonUtils.checkNull(request.getParamValue("VENDER_TYPE")); //供应商类型
        sql.append("SELECT DISTINCT (T.VENDER_ID)V, D.*\n" );
        sql.append("  FROM TT_PART_OEM_PO T, TT_PART_VENDER_DEFINE D\n" );
        sql.append(" WHERE EXISTS (SELECT * FROM TT_PART_PO_IN D WHERE D.PO_ID = T.PO_ID)\n" );
        sql.append("   AND EXISTS\n" );
        sql.append(" (SELECT * FROM TT_PART_PLAN_SCROLL_DEL LD WHERE LD.ID = T.PLINE_ID)\n" );
        sql.append("   AND MONTHS_BETWEEN(TRUNC(SYSDATE, 'mm'), TRUNC(T.CREATE_DATE, 'mm')) <= 12\n" );
        sql.append("   AND MONTHS_BETWEEN(TRUNC(SYSDATE, 'mm'), TRUNC(T.CREATE_DATE, 'mm')) > 0\n" );
        sql.append("   AND NOT EXISTS (SELECT 1\n" );
        sql.append("          FROM TT_PART_CONTRACT_DEFINE F\n" );
        sql.append("         WHERE T.VENDER_ID = F.VENDER_ID)\n" );
        sql.append("   AND T.VENDER_ID = D.VENDER_ID");
        List<String> list =new ArrayList<String>();
        if (null != vType && !vType.equals("")) {
            sql.append(" AND D.VENDER_TYPE = ? ");
            list.add(""+vType+"");
        }
        if (null != venderName && !venderName.equals("")) {
            sql.append(" AND D.VENDER_NAME LIKE UPPER(?)");
            list.add("%"+venderName+"%");
        }
        if (null != venderCode && !venderCode.equals("")) {
            sql.append(" AND UPPER(D.VENDER_CODE) LIKE UPPER(?) ");
            list.add("%"+venderCode+"%");
        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(),list,getFunName(), pageSize, curPage);
        return ps;
    }


    /**

     * @return :
     * @throws : LastDate    : 2013-4-10
     * @Title : 备件采购属性查询
     */
    public PageResult<Map<String, Object>> regetContractQuery(String venderCode, String venderName, String ConNumber,
                                                            String  partCode,String partName, String partOldcode, String ConType, String sDate, String eDate
            ,String vendeType,String isOut,int pageSize, int curPage,String  state) {
        StringBuffer sql = new StringBuffer("");
//        Date  date=new Date();
        sql.append("SELECT TO_CHAR(CD.CONTRACT_EDATE,'yyyy-mm-dd')CONTRACT_EDATE,\n");
        sql.append("       TO_CHAR(CD.CONTRACT_SDATE,'yyyy-mm-dd')CONTRACT_SDATE,\n");
        sql.append("       CD.CONTRACT_NUMBER,\n");
        sql.append("       CD.CONTRACT_PRICE,\n");
        sql.append("       CD.VENDER_ID,\n");
        sql.append("       CD.PART_ID,\n");
        sql.append("       CD.DEF_ID,\n");
        sql.append("       PD.PART_CODE,\n");
        sql.append("       PD.PART_CNAME,\n");
        sql.append("       PD.PART_OLDCODE,\n");
        sql.append("       VD.VENDER_CODE,\n");
        sql.append("       VD.VENDER_NAME,\n");
        sql.append("       TC.NAME,\n");
        sql.append("       DECODE(CD.CONTRACT_TYPE,"+Constant.CONTRACT_TYPE_01+",'基本合同',"+Constant.CONTRACT_TYPE_02+",'订单合同')CONTRACT_TYPE,\n");
        if (null != isOut && !isOut.equals("")) {
            if(isOut.equals(Constant.IF_TYPE_NO.toString())){
                sql.append(" '否' IS_OUT,");
            }else{
                sql.append(" '是' IS_OUT,");
            }
        }
        sql.append("       DECODE(CD.STATE,"+Constant.STATUS_ENABLE+",'有效',"+Constant.STATUS_DISABLE+",'无效')STATE\n");
        sql.append("  FROM TT_PART_CONTRACT_DEFINE CD,\n");
        sql.append("       TT_PART_DEFINE          PD,\n");
        sql.append("       TT_PART_VENDER_DEFINE   VD,\n");
        sql.append("   TC_USER TC\n");
        sql.append(" WHERE CD.PART_ID = PD.PART_ID\n");
        sql.append("   AND VD.VENDER_ID = CD.VENDER_ID");
        sql.append(" AND CD.CREATE_BY = TC.USER_ID(+)");
        sql.append(" AND CD.STATUS = 1 ");


        List<String> list =new ArrayList<String>();
        if (null != venderCode && !venderCode.equals("")) {
            sql.append(" AND UPPER(VD.VENDER_CODE) LIKE UPPER(?) ");
            list.add("%"+venderCode+"%");
        }
        if (null != venderName && !venderName.equals("")) {
            sql.append(" AND VD.VENDER_NAME  LIKE UPPER(?)");
            list.add("%"+venderName+"%");
        }
        if (null != vendeType && !vendeType.equals("")) {
            sql.append(" AND VD.VENDER_TYPE LIKE UPPER(?)");
            list.add(""+vendeType+"");
        }
        if (null != ConNumber && !ConNumber.equals("")) {
            sql.append(" AND UPPER(CD.CONTRACT_NUMBER) LIKE UPPER(?)");
            list.add("%"+ConNumber+"%");
        }
        if (null != partCode && !partCode.equals("")) {
            sql.append(" AND UPPER(PD.PART_CODE) LIKE UPPER(?)");
            list.add("%"+partCode+"%");
        }
        if (null != partName && !partName.equals("")) {
            sql.append(" AND PD.PART_CNAME LIKE UPPER(?)");
            list.add("%"+partName+"%");
        }
        if (null != partOldcode && !partOldcode.equals("")) {
            sql.append(" AND UPPER(PD.PART_OLDCODE) LIKE UPPER(?)");
            list.add("%"+partOldcode+"%");
        }
        if (null != ConType && !ConType.equals("")) {
            sql.append(" AND CD.CONTRACT_TYPE=?");
            list.add(ConType);
        }
        if (null != state && !state.equals("")) {
            sql.append(" AND CD.STATE=?");
            list.add(state);
        }
        if (!"".equals(sDate)) {
            sql.append(" and  to_date(? ,'yyyy-mm-dd') between CD.CONTRACT_SDATE and CD.CONTRACT_EDATE ");
            list.add( ""+sDate+"" );
        }
//        if (!"".equals(eDate)) {
//            sql.append(" and CD.CONTRACT_EDATE<= to_date(?,'YYYY/MM/dd')\n");
//            list.add(""+eDate+"");
//        }
        if (null != isOut && !isOut.equals("")) {
            if(isOut.equals(Constant.IF_TYPE_NO.toString())){
                sql.append(" AND sysdate  between CD.CONTRACT_SDATE and CD.CONTRACT_EDATE");
            }else{
                sql.append(" AND (CD.CONTRACT_SDATE > sysdate or CD.CONTRACT_EDATE < sysdate)");
            }
        }

        sql.append("  ORDER　BY CD.DEF_ID");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(),list,getFunName(), pageSize, curPage);
        return ps;
    }


    /**

     * @return :
     * @throws : LastDate    : 2013-4-10
     * @Title : 备件采购属性查询
     */
    public PageResult<Map<String, Object>> regetDDContractQuery(String venderCode, String venderName, String ConNumber,
                                                              String  partCode,String partName, String partOldcode, String ConType, String sDate, String eDate
            ,String vendeType,String isOut,int pageSize, int curPage,String state,String isTemp) {
        StringBuffer sql = new StringBuffer("");
//        Date  date=new Date();
        sql.append("SELECT TO_CHAR(CD.CONTRACT_EDATE,'yyyy-mm-dd')CONTRACT_EDATE,\n");
        sql.append("       TO_CHAR(CD.CONTRACT_SDATE,'yyyy-mm-dd')CONTRACT_SDATE,\n");
        sql.append("       CD.CONTRACT_NUMBER,\n");
        sql.append("       CD.CONTRACT_PRICE,\n");
        sql.append("       CD.VENDER_ID,\n");
        sql.append("       CD.PART_ID,\n");
        sql.append("       CD.DEF_ID,\n");
        sql.append("       PD.PART_CODE,\n");
        sql.append("       PD.PART_CNAME,\n");
        sql.append("       PD.PART_OLDCODE,\n");
        sql.append("       VD.VENDER_CODE,\n");
        sql.append("       VD.VENDER_NAME,\n");
        sql.append("       TC.NAME,\n");
        sql.append("       DECODE(CD.CONTRACT_TYPE,"+Constant.CONTRACT_TYPE_01+",'基本合同',"+Constant.CONTRACT_TYPE_02+",'订单合同')CONTRACT_TYPE,\n");
        if (null != isOut && !isOut.equals("")) {
            if(isOut.equals(Constant.IF_TYPE_NO.toString())){
                sql.append(" '否' IS_OUT,");
            }else{
                sql.append(" '是' IS_OUT,");
            }
        }
        sql.append("       DECODE(CD.STATE,"+Constant.STATUS_ENABLE+",'有效',"+Constant.STATUS_DISABLE+",'无效')STATE,\n");
        sql.append("       DECODE(CD.ISTEMP,"+Constant.IF_TYPE_YES+",'是',"+Constant.IF_TYPE_NO+",'否')ISTEMP\n");
        sql.append("  FROM TT_PART_CONTRACT_DEFINE CD,\n");
        sql.append("       TT_PART_DEFINE          PD,\n");
        sql.append("       TT_PART_VENDER_DEFINE   VD,\n");
        sql.append("   TC_USER TC\n");
        sql.append(" WHERE CD.PART_ID = PD.PART_ID\n");
        sql.append("   AND VD.VENDER_ID = CD.VENDER_ID");
        sql.append(" AND CD.CREATE_BY = TC.USER_ID(+)");
        sql.append(" AND CD.CONTRACT_TYPE ="+Constant.CONTRACT_TYPE_02+"");


        List<String> list =new ArrayList<String>();
        if (null != venderCode && !venderCode.equals("")) {
            sql.append(" AND UPPER(VD.VENDER_CODE) LIKE UPPER(?) ");
            list.add("%"+venderCode+"%");
        }
        if (null != venderName && !venderName.equals("")) {
            sql.append(" AND VD.VENDER_NAME LIKE UPPER(?)");
            list.add("%"+venderName+"%");
        }

        if (null != vendeType && !vendeType.equals("")) {
            sql.append(" AND VD.VENDER_TYPE LIKE UPPER(?)");
            list.add(""+vendeType+"");
        }
        if (null != ConNumber && !ConNumber.equals("")) {
            sql.append(" AND UPPER(CD.CONTRACT_NUMBER) LIKE UPPER(?)");
            list.add("%"+ConNumber+"%");
        }
        if (null != partCode && !partCode.equals("")) {
            sql.append(" AND UPPER(PD.PART_CODE) LIKE UPPER(?)");
            list.add("%"+partCode+"%");
        }
        if (null != partName && !partName.equals("")) {
            sql.append(" AND PD.PART_CNAME LIKE UPPER(?)");
            list.add("%"+partName+"%");
        }
        if(StringUtil.notNull(state)){
            sql.append(" AND CD.STATE = ?");
            list.add(state);
        }
        if (null != partOldcode && !partOldcode.equals("")) {
            sql.append(" AND UPPER(PD.PART_OLDCODE) LIKE UPPER(?)");
            list.add("%"+partOldcode+"%");
        }
        if (null != ConType && !ConType.equals("")) {
            sql.append(" AND CD.CONTRACT_TYPE=?");
            list.add(ConType);
        }
        if (!"".equals(sDate)) {
            sql.append(" and  to_date(? ,'yyyy-mm-dd') between CD.CONTRACT_SDATE and CD.CONTRACT_EDATE ");
            list.add( ""+sDate+"" );
        }
//        if (!"".equals(eDate)) {
//            sql.append(" and CD.CONTRACT_EDATE<= to_date(?,'YYYY/MM/dd')\n");
//            list.add(""+eDate+"");
//        }
        if (null != isOut && !isOut.equals("")) {
            if(isOut.equals(Constant.IF_TYPE_NO.toString())){
                sql.append(" AND sysdate  between CD.CONTRACT_SDATE and CD.CONTRACT_EDATE");
            }else{
                sql.append(" AND (CD.CONTRACT_SDATE > sysdate or CD.CONTRACT_EDATE < sysdate)");
            }
        }
        if(StringUtil.notNull(isTemp)){
            sql.append(" AND CD.ISTEMP = ?");
            list.add(isTemp);
        }
        sql.append("  ORDER　BY CD.DEF_ID");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(),list,getFunName(), pageSize, curPage);
        return ps;
    }

    /**

     * @return :
     * @throws : LastDate    : 2013-4-10
     * @Title : 备件采购属性查询
     */
    public PageResult<Map<String, Object>> getContractQueryHis(String venderCode, String venderName, String ConNumber,
                                                            String  partCode,String partName, String partOldcode, String ConType, String sDate, String eDate
            ,int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT TO_CHAR(CD.CONTRACT_EDATE,'yyyy-mm-dd')CONTRACT_EDATE,\n");
        sql.append("       TO_CHAR(CD.CONTRACT_SDATE,'yyyy-mm-dd')CONTRACT_SDATE,\n");
        sql.append("       CD.CONTRACT_NUMBER,\n");
        sql.append("       CD.CONTRACT_PRICE,\n");
        sql.append("       CD.VENDER_ID,\n");
        sql.append("       CD.UPDATE_DATE,\n");
        sql.append("       CD.PART_ID,\n");
        sql.append("       CD.DEF_ID,\n");
        sql.append("       PD.PART_CODE,\n");
        sql.append("       PD.PART_CNAME,\n");
        sql.append("       PD.PART_OLDCODE,\n");
        sql.append("       TC.NAME,\n");
        sql.append("       VD.VENDER_CODE,\n");
        sql.append("       VD.VENDER_NAME,\n");
        sql.append("       DECODE(CD.CONTRACT_TYPE,"+Constant.CONTRACT_TYPE_01+",'基本合同',"+Constant.CONTRACT_TYPE_02+",'订单合同')CONTRACT_TYPE,\n");
        sql.append("       DECODE(CD.STATE,"+Constant.STATUS_ENABLE+",'有效',"+Constant.STATUS_DISABLE+",'无效')STATE\n");
        sql.append("  FROM TT_PART_CONTRACT_DEFINE_LOG CD,\n");
        sql.append("       TT_PART_DEFINE          PD,\n");
        sql.append("       TC_USER          TC,\n");
        sql.append("       TT_PART_VENDER_DEFINE   VD\n");
        sql.append(" WHERE CD.PART_ID = PD.PART_ID(+)\n");
        sql.append("   AND VD.VENDER_ID = CD.VENDER_ID");
        sql.append("   AND CD.UPDATE_BY=TC.USER_ID(+)");

        List<String> list =new ArrayList<String>();
        if (null != venderCode && !venderCode.equals("")) {
            sql.append(" AND UPPER(VD.VENDER_CODE) LIKE UPPER(?) ");
            list.add("%"+venderCode+"%");
        }
        if (null != venderName && !venderName.equals("")) {
            sql.append(" AND VD.VENDER_NAME LIKE UPPER(?)");
            list.add("%"+venderName+"%");
        }
        if (null != ConNumber && !ConNumber.equals("")) {
            sql.append(" AND UPPER(CD.CONTRACT_NUMBER) LIKE UPPER(?)");
            list.add("%"+ConNumber+"%");
        }
        if (null != partCode && !partCode.equals("")) {
            sql.append(" AND UPPER(PD.PART_CODE) LIKE UPPER(?)");
            list.add("%"+partCode+"%");
        }
        if (null != partName && !partName.equals("")) {
            sql.append(" AND PD.PART_CNAME LIKE UPPER(?)");
            list.add("%"+partName+"%");
        }
        if (null != partOldcode && !partOldcode.equals("")) {
            sql.append(" AND UPPER(PD.PART_OLDCODE) LIKE UPPER(?)");
            list.add("%"+partOldcode+"%");
        }
        if (null != ConType && !ConType.equals("")) {
            sql.append(" AND CD.CONTRACT_TYPE=?");
            list.add(ConType);
        }
        if (!"".equals(sDate)) {
            sql.append(" and  to_date(? ,'yyyy-mm-dd') between CD.CONTRACT_SDATE and CD.CONTRACT_EDATE ");
            list.add( ""+sDate+"" );
        }
//        if (!"".equals(eDate)) {
//            sql.append(" and CD.CONTRACT_EDATE<= to_date(?,'YYYY/MM/dd')\n");
//            list.add(""+eDate+"");
//        }
        sql.append("  ORDER　BY CD.DEF_ID");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(),list,getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> queryPartException(String partolcode, String partname,int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT PD.PART_ID,PD.PART_CODE, PD.PART_CNAME, PD.PART_OLDCODE\n");
        sql.append("  FROM TT_PART_DEFINE PD\n");
        sql.append(" WHERE 1=1\n");
        List<String> list=new ArrayList<String>();
        if (null != partolcode && !partolcode.equals("")) {
            sql.append(" AND UPPER(PD.PART_OLDCODE) LIKE UPPER(?)");
            list.add("%"+partolcode+"%");
        }
        if (null != partname && !partname.equals("")) {
            sql.append(" AND UPPER(PD.PART_CNAME) LIKE UPPER(?)");
            list.add("%"+partname+"%");
        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), list, getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> queryPartVender(String contractType,String venderType, String venderCode,int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("WITH CXX AS\n");
        sql.append(" (SELECT D1.VENDER_ID,\n");
        sql.append("         D1.CONTRACT_NUMBER,\n");
        sql.append("         D1.CONTRACT_SDATE,\n");
        sql.append("         D1.CONTRACT_EDATE\n");
        sql.append("    FROM TT_PART_CONTRACT_DEFINE D1\n");
        sql.append("   WHERE (D1.VENDER_ID, D1.CONTRACT_SDATE) IN\n");
        sql.append("         (SELECT D.VENDER_ID, MAX(D.CONTRACT_SDATE) CONTRACT_SDATE\n");
        sql.append("            FROM TT_PART_CONTRACT_DEFINE D\n");
        sql.append("           WHERE D.STATUS = 1\n");
        sql.append("             AND D.STATE = 10011001\n");
//        sql.append("             AND D.CONTRACT_TYPE = 97071001\n");
        List<String> list=new ArrayList<String>();
        if (null != contractType && !contractType.equals("")) {
            sql.append(" AND D.CONTRACT_TYPE = ?");
            list.add(contractType);
        }
        sql.append("             AND SYSDATE NOT BETWEEN D.CONTRACT_SDATE AND D.CONTRACT_EDATE\n");
        sql.append("           GROUP BY D.VENDER_ID))\n");
        sql.append("SELECT DISTINCT VD.VENDER_ID,\n");
        sql.append("                VD.VENDER_CODE,\n");
        sql.append("                VD.VENDER_NAME,\n");
        sql.append("                X.CONTRACT_NUMBER,\n");
        sql.append("                X.CONTRACT_SDATE,\n");
        sql.append("                X.CONTRACT_EDATE\n");
        sql.append("  FROM TT_PART_DEFINE        D,\n");
        sql.append("       TT_PART_BUY_PRICE     BP,\n");
        sql.append("       TT_PART_VENDER_DEFINE VD,\n");
        sql.append("       CXX                   X\n");
        sql.append(" WHERE D.PART_ID = BP.PART_ID\n");
        sql.append("   AND BP.VENDER_ID = VD.VENDER_ID\n");
        sql.append("   AND VD.VENDER_ID = X.VENDER_ID(+)\n");
        sql.append("   AND D.PRODUCE_FAC = 92811001 --客服采购\n");
        sql.append("   AND D.IN_PLAN = 10041001 --是计划\n");
        sql.append("      --AND VD.VENDER_ID <> 82001 --客服采购 否股份\n");
        sql.append("   AND D.STATUS = 1\n");
        sql.append("   AND BP.STATUS = 1\n");
        sql.append("   AND VD.STATUS = 1\n");
        sql.append("   AND D.STATE = 10011001\n");
        sql.append("   AND BP.STATE = 10011001\n");
        sql.append("   AND VD.STATE = 10011001\n");
        if (null != venderCode && !venderCode.equals("")) {
            sql.append(" AND UPPER(VD.VENDER_NAME) LIKE UPPER(?)");
            list.add("%"+venderCode+"%");
        }

        if (null != venderType && !venderType.equals("")) {
            sql.append(" AND VD.VENDER_TYPE = ?");
            list.add(""+venderType+"");
        }
        sql.append("   AND NOT EXISTS\n");
        sql.append(" (SELECT 1\n");
        sql.append("          FROM TT_PART_CONTRACT_DEFINE D\n");
        sql.append("         WHERE D.STATUS = 1\n");
        sql.append("           AND D.STATE = 10011001\n");
        //sql.append("           AND D.CONTRACT_TYPE = 97071001\n");
        if (null != contractType && !contractType.equals("")) {
            sql.append(" AND D.CONTRACT_TYPE = ?");
            list.add(contractType);
        }
        sql.append("           AND BP.VENDER_ID = D.VENDER_ID\n");
        sql.append("           AND SYSDATE BETWEEN D.CONTRACT_SDATE AND D.CONTRACT_EDATE)");


        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), list, getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> queryPartVender(String contractType,String partName,String planName,String buyName,String venderType, String venderCode,int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("WITH CXX AS\n");
        sql.append(" (SELECT D1.VENDER_ID,\n");
        sql.append("         D1.CONTRACT_NUMBER,\n");
        sql.append("         D1.CONTRACT_SDATE,\n");
        sql.append("         D1.CONTRACT_EDATE\n");
        sql.append("    FROM TT_PART_CONTRACT_DEFINE D1\n");
        sql.append("   WHERE (D1.VENDER_ID, D1.CONTRACT_SDATE) IN\n");
        sql.append("         (SELECT D.VENDER_ID, MAX(D.CONTRACT_SDATE) CONTRACT_SDATE\n");
        sql.append("            FROM TT_PART_CONTRACT_DEFINE D\n");
        sql.append("           WHERE D.STATUS = 1\n");
        sql.append("             AND D.STATE = 10011001\n");
//        sql.append("             AND D.CONTRACT_TYPE = 97071001\n");
        List<String> list=new ArrayList<String>();
        if (null != contractType && !contractType.equals("")) {
            sql.append(" AND D.CONTRACT_TYPE = ?");
            list.add(contractType);
        }
        sql.append("             AND SYSDATE NOT BETWEEN D.CONTRACT_SDATE AND D.CONTRACT_EDATE\n");
        sql.append("           GROUP BY D.VENDER_ID))\n");
        sql.append("SELECT DISTINCT VD.VENDER_ID,\n");
        sql.append("                VD.VENDER_CODE,\n");
        sql.append("                VD.VENDER_NAME,\n");
        sql.append("                X.CONTRACT_NUMBER,\n");
        sql.append("                X.CONTRACT_SDATE,\n");
        sql.append("                X.CONTRACT_EDATE,\n");
        sql.append("                D.PART_OLDCODE,\n");
        sql.append("                D.PART_CODE,\n");
        sql.append("                D.PART_CNAME,\n");
        sql.append("                US.NAME PLANNER_NAME,\n");
        sql.append("                UE.NAME BUY_NAME\n");
        sql.append("  FROM TT_PART_DEFINE        D,\n");
        sql.append("       TT_PART_BUY_PRICE     BP,\n");
        sql.append("       TT_PART_VENDER_DEFINE VD,\n");
        sql.append("       TC_USER US,\n");
        sql.append("       TC_USER UE,\n");
        sql.append("       CXX                   X\n");
        sql.append(" WHERE D.PART_ID = BP.PART_ID\n");
        sql.append("   AND BP.VENDER_ID = VD.VENDER_ID\n");
        sql.append("   AND US.USER_ID = D.PLANER_ID\n");
        sql.append("   AND UE.USER_ID = D.BUYER_ID\n");
        sql.append("   AND VD.VENDER_ID = X.VENDER_ID(+)\n");
        sql.append("   AND D.PRODUCE_FAC = 92811001 --客服采购\n");
        sql.append("   AND D.IS_PLAN = 10041001 --是计划\n");
        sql.append("      --AND VD.VENDER_ID <> 82001 --客服采购 否股份\n");
        sql.append("   AND D.STATUS = 1\n");
        sql.append("   AND BP.STATUS = 1\n");
        sql.append("   AND VD.STATUS = 1\n");
        sql.append("   AND D.STATE = 10011001\n");
        sql.append("   AND BP.STATE = 10011001\n");
        sql.append("   AND VD.STATE = 10011001\n");
        if (null != venderCode && !venderCode.equals("")) {
            sql.append(" AND UPPER(VD.VENDER_NAME) LIKE UPPER(?)");
            list.add("%"+venderCode+"%");
        }
        if (null != planName && !planName.equals("")) {
            sql.append(" AND UPPER(US.NAME) LIKE UPPER(?)");
            list.add("%"+planName+"%");
        }
        if (null != buyName && !buyName.equals("")) {
            sql.append(" AND UPPER(UE.NAME) LIKE UPPER(?)");
            list.add("%"+buyName+"%");
        }
        if (null != partName && !partName.equals("")) {
            sql.append(" AND UPPER(D.PART_CNAME) LIKE UPPER(?)");
            list.add("%"+partName+"%");
        }
        if (null != venderType && !venderType.equals("")) {
            sql.append(" AND VD.VENDER_TYPE = ?");
            list.add(""+venderType+"");
        }
        sql.append("   AND NOT EXISTS\n");
        sql.append(" (SELECT 1\n");
        sql.append("          FROM TT_PART_CONTRACT_DEFINE D\n");
        sql.append("         WHERE D.STATUS = 1\n");
        sql.append("           AND D.STATE = 10011001\n");
        //sql.append("           AND D.CONTRACT_TYPE = 97071001\n");
        if (null != contractType && !contractType.equals("")) {
            sql.append(" AND D.CONTRACT_TYPE = ?");
            list.add(contractType);
        }
        sql.append("           AND BP.VENDER_ID = D.VENDER_ID\n");
        sql.append("           AND SYSDATE BETWEEN D.CONTRACT_SDATE AND D.CONTRACT_EDATE)");


        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), list, getFunName(), pageSize, curPage);
        return ps;
    }
    /**
     * @param : @param delCode
     * @param : @return
     * @return :
     * LastDate    : 2013-5-3
     * @Title : 验证供应商编码是否存在 并返回备件ID、Name
     * @Description:
     */
    public List<Map<String, Object>> checkVenderCode(String oldCode) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT TD.VENDER_ID,TD.VENDER_CODE, TD.VENDER_NAME FROM TT_PART_VENDER_DEFINE TD " +
                " WHERE UPPER(TD.VENDER_CODE) = '" + oldCode + "' ";
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }


    public Map<String, Object> getContractList(String oldCode,String contractType) {
        Map<String, Object> list = null;
        StringBuffer sql = new StringBuffer("");
        if(contractType.equals(Constant.CONTRACT_TYPE_02)){
            sql.append("SELECT TO_CHAR(CD.CONTRACT_EDATE,'yyyy-mm-dd')CONTRACT_EDATE,\n");
            sql.append("       TO_CHAR(CD.CONTRACT_SDATE,'yyyy-mm-dd')CONTRACT_SDATE,\n");
            sql.append("       CD.CONTRACT_NUMBER,\n");
            sql.append("       CD.CONTRACT_PRICE,\n");
            sql.append("       CD.VENDER_ID,\n");
            sql.append("       CD.PART_ID,\n");
            sql.append("       CD.DEF_ID,\n");
            sql.append("       PD.PART_CODE,\n");
            sql.append("       PD.PART_CNAME,\n");
            sql.append("       PD.PART_OLDCODE,\n");
            sql.append("       VD.VENDER_CODE,\n");
            sql.append("       VD.VENDER_NAME,\n");
            sql.append("       CD.CONTRACT_TYPE,\n");
            sql.append("       CD.REMARK,\n");
            sql.append("       CD.STATE,\n");
            sql.append("       CD.ISTEMP\n");
            sql.append("  FROM TT_PART_CONTRACT_DEFINE CD,\n");
            sql.append("       TT_PART_DEFINE          PD,\n");
            sql.append("       TT_PART_VENDER_DEFINE   VD\n");
            sql.append(" WHERE CD.PART_ID = PD.PART_ID\n");
            sql.append("   AND VD.VENDER_ID = CD.VENDER_ID\n");
            sql.append("   AND CD.CONTRACT_NUMBER=?");
        }else{
            sql.append("SELECT TO_CHAR(CD.CONTRACT_EDATE,'yyyy-mm-dd')CONTRACT_EDATE,\n");
            sql.append("       TO_CHAR(CD.CONTRACT_SDATE,'yyyy-mm-dd')CONTRACT_SDATE,\n");
            sql.append("       CD.CONTRACT_NUMBER,\n");
            sql.append("       CD.VENDER_ID,\n");
            sql.append("       CD.DEF_ID,\n");
            sql.append("       VD.VENDER_CODE,\n");
            sql.append("       VD.VENDER_NAME,\n");
            sql.append("       CD.CONTRACT_TYPE,\n");
            sql.append("       CD.REMARK,\n");
            sql.append("       CD.STATE,\n");
            sql.append("       CD.ISTEMP\n");
            sql.append("  FROM TT_PART_CONTRACT_DEFINE CD,\n");
            sql.append("       TT_PART_VENDER_DEFINE   VD\n");
            sql.append(" WHERE VD.VENDER_ID = CD.VENDER_ID\n");
            sql.append("   AND CD.CONTRACT_NUMBER=?");
        }
        List<String> list1 =new ArrayList<String>();
        list1.add(oldCode);
        list =pageQueryMap(sql.toString(), list1, getFunName());
        return list;
    }


    public PageResult<Map<String, Object>> getContractQueryInit(String connum,int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT PD.PART_ID,PD.PART_CODE, PD.PART_CNAME, PD.PART_OLDCODE,CD.CONTRACT_PRICE,CD.DEF_ID,cd.ISTEMP\n");
        sql.append("  FROM TT_PART_DEFINE PD,TT_PART_CONTRACT_DEFINE CD\n");
        sql.append(" WHERE PD.PART_ID = CD.PART_ID\n");
        sql.append("   AND CD.CONTRACT_NUMBER = ?");
        List<String> list=new ArrayList<String>();
        list.add(connum);
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), list, getFunName(), pageSize, curPage);
        return ps;
    }


    public PageResult<Map<String, Object>> queryPartVender3(String contractType,int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("select tp.Part_Oldcode, tp.part_cname, tp.part_code, us.name p_name, ue.name b_name \n");
        sql.append("  from tt_part_define        tp,\n");
        sql.append("       tt_part_buy_price     bp,\n");
        sql.append("       tt_part_vender_define vd,\n");
        sql.append("       tc_user               us,\n");
        sql.append("       tc_user               ue\n");
        sql.append(" where tp.part_id = bp.part_id\n");
        sql.append("   and vd.vender_id = bp.vender_id\n");
        sql.append("   and us.user_id = tp.planer_id\n");
        sql.append("   and ue.user_id = tp.buyer_id\n");
        sql.append("   and vd.vender_id =?");
        List<String> list=new ArrayList<String>();
        list.add(contractType);
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), list, getFunName(), pageSize, curPage);
        return ps;
    }

    public List<Map<String, Object>> getContractById(String partId,String venderId,String contractType,String contractNum) {
        StringBuffer query = new StringBuffer("");
        query.append("SELECT *\n");
        query.append("  FROM TT_PART_CONTRACT_DEFINE CD\n");
        query.append(" WHERE CD.CONTRACT_NUMBER <> ?\n");
        query.append("   AND CD.PART_ID = ?\n");
        query.append("   AND CD.VENDER_ID = ?\n");
        query.append("   AND CD.STATE="+Constant.STATUS_ENABLE+"");
        query.append("   AND CD.CONTRACT_TYPE = ?");
        List<String> list=new ArrayList<String>();
        list.add(contractNum);
        list.add(partId);
        list.add(venderId);
        list.add(contractType);
        List<Map<String, Object>> ps = pageQuery(query.toString(), list, getFunName());
        return ps;
    }
    /**
     * @param : @param ContractNum
     * @param : @return
     * @return :
     * LastDate    : 2017-3-16
     * @Title : 验证不同的供应商不能添加同一个合同号
     * @Description:
     */
    public List<Map<String, Object>> checkVender(String ContractNum,Long venderId) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
		sql.append("SELECT *\n") ; 
		sql.append("  FROM TT_PART_CONTRACT_DEFINE\n") ;  
		sql.append(" WHERE CONTRACT_NUMBER ='"+ContractNum+"'\n") ;  
		sql.append("   AND VENDER_ID <>"+venderId);

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

}
