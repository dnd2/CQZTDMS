/**
 *
 */
package com.infodms.dms.util;

import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.exception.BizException;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yu
 * @Description 配件通用编码相关
 */
public class OrderCodeManager {

    /**
     * @param cfconfig_id 表对应的常量值
     * @return 单据编码
     * @title 获取当天的指定表中指定编码字段的单据编码
     */

    public static String getOrderCode(Integer cfconfig_id) {

        List shortName = new ArrayList();
        shortName.add(cfconfig_id);
        shortName.add(0);
        shortName.add(0);
        String ret = new String();
        if (cfconfig_id != null || !"".equals(cfconfig_id)) {
            ret = POFactoryBuilder
                    .getInstance().callFunction("PKG_PART.f_getnext_ordercode", java.sql.Types.VARCHAR, shortName).toString();
        }
        if (ret == null)
            return "";
        if (ret instanceof String) {
            return String.valueOf(ret);
        } else {
            return "";
        }
    }

    /**
     * @param cfconfig_id 表对应的常量值
     * @return 单据编码
     * @title 获取当天的指定表中指定编码字段的单据编码
     */
    public static String getOrderCode(Integer cfconfig_id, String dealerId) {

        List shortName = new ArrayList();
        shortName.add(cfconfig_id);
        shortName.add(0);
        shortName.add(dealerId);
        String ret = new String();
        if (cfconfig_id != null || !"".equals(cfconfig_id)) {
            ret = POFactoryBuilder
                    .getInstance().callFunction("PKG_PART.f_getnext_ordercode", java.sql.Types.VARCHAR, shortName).toString();
        }
        if (ret == null)
            return "";
        if (ret instanceof String) {
            return String.valueOf(ret);
        } else {
            return "";
        }
    }
    
    public static String getOrderCode(Integer cfconfig_id,Long userId, String dealerId) {

        List shortName = new ArrayList();
        shortName.add(cfconfig_id);
        shortName.add(userId);
        shortName.add(dealerId);
        String ret = new String();
        if (cfconfig_id != null || !"".equals(cfconfig_id)) {
            ret = POFactoryBuilder
                    .getInstance().callFunction("PKG_PART.f_getnext_ordercode", java.sql.Types.VARCHAR, shortName).toString();
        }
        if (ret == null)
            return "";
        if (ret instanceof String) {
            return String.valueOf(ret);
        } else {
            return "";
        }
    }

    /**
     * @param cfconfig_id 表对应的常量值
     * @return 单据编码
     * @title 判断是否已经完成
     */
    public static Integer isOver(Integer cfconfig_id, String orderId) {

        List shortName = new ArrayList();
        shortName.add(cfconfig_id);
        shortName.add(orderId);
        String ret = new String();
        if (cfconfig_id != null || !"".equals(cfconfig_id)) {
            ret = POFactoryBuilder
                    .getInstance().callFunction("PKG_PART.F_ISOVER", java.sql.Types.VARCHAR, shortName).toString();
        }
        if (ret == null)
            return 0;
        if (ret instanceof String) {
            return Integer.valueOf(ret);
        } else {
            return 0;
        }
    }

    /**
     * @param configId 表对应的常量值
     * @return 单据编码
     * @throws SQLException
     * @title 获取当天的指定表中指定编码字段的单据编码
     */
    public static String getOrderCode(long cfcconfigId) throws SQLException {
        Connection cn = null;
        CallableStatement cs = null;
        String nextOrderCode = "";
        String strProc = "";
        try {
            cn = DBService.getInstance().getConnection();
            strProc = "{?=call PKG_PART.f_getnext_ordercode(?,?,?)}";
            cs = cn.prepareCall(strProc);
            cs.registerOutParameter(1, Types.CHAR);
            cs.setLong(2, cfcconfigId);
            cs.setLong(3, 0);
            cs.setLong(4, 0);
            cs.execute();
            nextOrderCode = cs.getString(1);
            POContext.endTxn(true);
            return nextOrderCode;
        } catch (SQLException se) {
            POContext.endTxn(false);
        	se.printStackTrace();
            throw new SQLException("OrderCodeManager.getNextOrderCode: " + se);
        } finally {
        	try {
        		if(cs!=null)
            		cs.close();
            	if(cn!=null)
            		cn.close();
                POContext.cleanTxn();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 查询制定表中的指点字段值(字符)是否存在
     *
     * @param tblName  表名称
     * @param codeName 表字段
     * @param code     表字段值(字符)
     * @return
     * @throws Exception
     */
    public static boolean isInsertDupNoExist(String tblName, String codeName,
                                             String code) throws Exception {
        Connection cn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean isDup = false;
        try {
            StringBuffer str = new StringBuffer();
            str.append(" SELECT 1 FROM ");
            str.append(tblName);
            str.append(" WHERE STATUS >0 AND ");
            str.append(codeName);
            str.append(" = ? ");
            cn = DBService.getInstance().getConnection();
            ps = cn.prepareStatement(str.toString());
            ps.setString(1, code);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    isDup = true;
                } else {
                    isDup = false;
                }
            }
            POContext.endTxn(true);
            return isDup;
        } catch (Exception e) {
        	POContext.endTxn(false);
            throw new Exception("OrderCodeManager.isInsertDupNoExist() " + e);
        } finally {
        	try {
        		if(rs!=null) rs.close();
            	if(ps!=null) ps.close();
            	if(cn!=null) cn.close();
                POContext.cleanTxn();
            } catch (Exception ex) {
            	ex.printStackTrace();
            }
        }
    }

    /**
     * 查询制定表中的指点字段值(数值)是否存在
     *
     * @param tblName  表名称
     * @param codeName 字段名称
     * @param id       字段值(数值)
     * @return
     * @throws Exception
     */
    public static boolean isInsertDupNoExist(String tblName, String codeName,
                                             long id) throws Exception {
        Connection cn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean isDup = false;
        try {
            StringBuffer str = new StringBuffer();
            str.append(" SELECT 1 FROM ");
            str.append(tblName);
            str.append(" WHERE STATUS >0 AND ");
            str.append(codeName);
            str.append(" = ? ");
            cn = DBService.getInstance().getConnection();
            ps = cn.prepareStatement(str.toString());
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    isDup = true;
                } else {
                    isDup = false;
                }
            }
            POContext.endTxn(true);
            return isDup;
        } catch (Exception e) {
        	POContext.endTxn(false);
            throw new Exception("OrderCodeManager.isInsertDupNoExist() " + e);
        } finally {
        	try {
        		if(rs!=null) rs.close();
            	if(ps!=null) ps.close();
            	if(cn!=null) cn.close();
                POContext.cleanTxn();
            } catch (Exception ex) {
            	ex.printStackTrace();
            }
        }
    }

    /**
     * 修改时查询制定数据中的制定字段值是否重复
     *
     * @param tblName  表名称
     * @param codeName 表编码字段
     * @param code     表编码值
     * @param idName   表序列字段
     * @param id       表序列值
     * @return
     * @throws Exception
     */
    public static boolean isModifyDupNoExist(String tblName, String codeName,
                                             String code, String idName, long id) throws Exception {
        Connection cn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean isDup = false;
        try {
            StringBuffer str = new StringBuffer();
            str.append(" SELECT 1 FROM ");
            str.append(tblName);
            str.append(" WHERE STATUS>0 AND ");
            str.append(idName);
            str.append(" <> ? ");
            str.append(" AND ");
            str.append(codeName);
            str.append(" = ?");
            cn = DBService.getInstance().getConnection();
            ps = cn.prepareStatement(str.toString());
            ps.setLong(1, id);
            ps.setString(2, code);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    isDup = true;
                } else {
                    isDup = false;
                }
            }
            POContext.endTxn(true);
            return isDup;
        } catch (Exception e) {
        	POContext.endTxn(false);
            throw new Exception("OrderCodeManager.isModifyDupNoExist() " + e);
        } finally {
        	try {
        		if(rs!=null) rs.close();
            	if(ps!=null) ps.close();
            	if(cn!=null) cn.close();
                POContext.cleanTxn();
            } catch (Exception ex) {
            	ex.printStackTrace();
            }
        }
    }

    /**
     * @param
     * @return locId
     * @title 获取配件货位ID
     */
    public static long getPartLocId(String orgId, String whId, String partId) {

        List shortName = new ArrayList();
        shortName.add(orgId);
        shortName.add(whId);
        shortName.add(partId);
        Long ret = Long.valueOf(POFactoryBuilder
                .getInstance().callFunction("PKG_PART.F_GETPARTLOCID", Types.DECIMAL, shortName).toString());

        if (ret == null)
            return 0l;
        if (ret instanceof Long) {
            return ret;
        } else {
            return 0l;
        }
    }

    public static String getSequence(String str) throws SQLException {
        Connection cn = null;
        CallableStatement cs = null;
        String nextOrderCode = "";
        String strProc = "";
        try {
            cn = DBService.getInstance().getConnection();
            strProc = "{?=call F_GETID()}";
            cs = cn.prepareCall(strProc);
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.execute();
            nextOrderCode = cs.getString(1);
            POContext.endTxn(true);
            return nextOrderCode;
        } catch (SQLException se) {
            POContext.endTxn(false);
            se.printStackTrace();
            throw new SQLException("OrderCodeManager.getSequence: " + se);
        } finally {
            try {
                if(cs!=null)
                    cs.close();
                if(cn!=null)
                    cn.close();
                POContext.cleanTxn();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * 获取结算单号
     *
     * @param venderCode 供应商编码
     * @return
     */
    public static String getBalCode(String venderCode) {

        List shortName = new ArrayList();
        shortName.add(venderCode);
        String ret = new String();
        if (venderCode != null || !"".equals(venderCode)) {
            ret = POFactoryBuilder.getInstance().callFunction(" f_get_balcode", java.sql.Types.VARCHAR, shortName).toString();
        }
        if (ret == null)
            return "";
        if (ret instanceof String) {
            return String.valueOf(ret);
        } else {
            return "";
        }
    }
}
