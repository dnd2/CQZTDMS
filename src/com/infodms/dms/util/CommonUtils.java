package com.infodms.dms.util;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.usermng.UserManageDao;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TPcContactPointPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.dms.actions.crmphone.push.SendPushService;
import com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage.FleetInfoAppDao;
import com.infodms.dms.po.TPcLeadsAllotPO;
import com.infodms.dms.po.TPcLeadsPO;
import com.infodms.dms.po.TPcRemindPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmMaterialPricePO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmVhclMaterialGroupRPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TrRoleFuncPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.po.TPcCompanyGroupPO;
import com.infodms.dms.po.TPcFollowPO;
import com.infodms.dms.po.TPcInvitePO;
import com.infodms.dms.po.TPcInviteShopPO;
import com.infodms.dms.po.TPcCustomerPO;
import com.infodms.dms.po.TPcIntentVehiclePO;
import com.infodms.dms.po.TPcOrderDetailPO;
import com.infodms.dms.po.TPcOrderPO;
import com.infodms.eai.po.TrRolePosePO;
import com.infoservice.filestore.FileStore;
import com.infoservice.filestore.FileStoreException;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.callbackimpl.POCallBack;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    public static final int FIRST_TEN_DAYS = 1;
    public static final int MIDDLE_TEN_DAYS = 2;
    public static final int LAST_TEN_DAYS = 3;
    public static final String HTML_TOKEN_KEY = "HTML_TOKEN";
    public static final String SESSION_TOKEN_KEY = "token";
    private static final int DEF_DIV_SCALE = 2;
    // ------------------- percentage format handlers -------------------
    static DecimalFormat percentageDecimalFormat = new DecimalFormat("##0.##%");

    // private static ActionContext act = ActionContext.getContext();
    static DecimalFormat percentageIntFormat = new DecimalFormat("##0");
    // ------------------- quantity format handlers -------------------
    static DecimalFormat quantityDecimalFormat = new DecimalFormat("#,##0.###");
    private static POFactory factory = POFactoryBuilder.getInstance();
    // ------------------- price format handlers -------------------
    private static DecimalFormat priceDecimalFormat = new DecimalFormat("#,##0.00");

    private CommonUtils() {

    }

    public static boolean IsOEMCompanyType(Integer ctype) {
        boolean ret = false;
        if (ctype == 10041001) {
            ret = true;
        }
        return ret;
    }

    /**
     * Formats a Double representing a price into a string
     *
     * @param price The price Double to be formatted
     * @return A String with the formatted price
     */
    public static String formatPrice(Double price) {
        if (price == null)
            return "";
        return formatPrice(price.doubleValue());
    }

    /**
     * Formats a double representing a price into a string
     *
     * @param price The price double to be formatted
     * @return A String with the formatted price
     */
    public static String formatPrice(double price) {

        return priceDecimalFormat.format(price);
    }

    // ------------------------------------------- ȡ��

    /**
     * Formats a Double representing a percentage into a string
     *
     * @param percentage The percentage Double to be formatted
     * @return A String with the formatted percentage
     */
    public static String formatPercentage(Double percentage) {
        if (percentage == null)
            return "";
        return formatPercentage(percentage.doubleValue());
    }

    /**
     * Formats a double representing a percentage into a string
     *
     * @param percentage The percentage double to be formatted
     * @return A String with the formatted percentage
     */
    public static String formatPercentage(double percentage) {
        return percentageDecimalFormat.format(percentage);
    }

    /**
     * Formats a Double representing a percentage into a string
     *
     * @param percentage The percentage Double to be formatted
     * @return A String with the formatted percentage
     */
    public static String formatIntPercentage(Double percentage) {
        if (percentage == null)
            return "";
        return formatIntPercentage(percentage.doubleValue());
    }

    /**
     * Formats a double representing a percentage into a string
     *
     * @param percentage The percentage double to be formatted
     * @return A String with the formatted percentage
     */
    public static String formatIntPercentage(double percentage) {
        return percentageIntFormat.format(percentage);
    }

    /**
     * Formats an Long representing a quantity into a string
     *
     * @param quantity The quantity Long to be formatted
     * @return A String with the formatted quantity
     */
    public static String formatQuantity(Long quantity) {
        if (quantity == null)
            return "";
        else
            return formatQuantity(quantity.doubleValue());
    }

    /**
     * Formats an int representing a quantity into a string
     *
     * @param quantity The quantity long to be formatted
     * @return A String with the formatted quantity
     */
    public static String formatQuantity(long quantity) {
        return formatQuantity((double) quantity);
    }

    /**
     * Formats an Integer representing a quantity into a string
     *
     * @param quantity The quantity Integer to be formatted
     * @return A String with the formatted quantity
     */
    public static String formatQuantity(Integer quantity) {
        if (quantity == null)
            return "";
        else
            return formatQuantity(quantity.doubleValue());
    }

    /**
     * Formats an int representing a quantity into a string
     *
     * @param quantity The quantity int to be formatted
     * @return A String with the formatted quantity
     */
    public static String formatQuantity(int quantity) {
        return formatQuantity((double) quantity);
    }

    /**
     * Formats a Float representing a quantity into a string
     *
     * @param quantity The quantity Float to be formatted
     * @return A String with the formatted quantity
     */
    public static String formatQuantity(Float quantity) {
        if (quantity == null)
            return "";
        else
            return formatQuantity(quantity.doubleValue());
    }

    /**
     * Formats a float representing a quantity into a string
     *
     * @param quantity The quantity float to be formatted
     * @return A String with the formatted quantity
     */
    public static String formatQuantity(float quantity) {
        return formatQuantity((double) quantity);
    }

    /**
     * Formats an Double representing a quantity into a string
     *
     * @param quantity The quantity Double to be formatted
     * @return A String with the formatted quantity
     */
    public static String formatQuantity(Double quantity) {
        if (quantity == null)
            return "";
        else
            return formatQuantity(quantity.doubleValue());
    }

    /**
     * Formats an double representing a quantity into a string
     *
     * @param quantity The quantity double to be formatted
     * @return A String with the formatted quantity
     */
    public static String formatQuantity(double quantity) {
        return quantityDecimalFormat.format(quantity);
    }

    /**
     * precision math addition operation
     *
     * @param double param1 and double param2 to addition operation
     * @return A double value
     */
    public static double add(double param1, double param2) {
        BigDecimal x1 = new BigDecimal(Double.toString(param1));
        BigDecimal x2 = new BigDecimal(Double.toString(param2));
        return x1.add(x2).doubleValue();
    }

    /**
     * precision math addition operation
     *
     * @param String param1 and String param2 to addition operation
     * @return A double value
     */
    public static double add(String param1, String param2) {
        param1 = makeNull(param1);
        param2 = makeNull(param2);
        BigDecimal x1 = new BigDecimal(param1);
        BigDecimal x2 = new BigDecimal(param2);
        return x1.add(x2).doubleValue();
    }

    /**
     * precision math addition operation
     *
     * @param list param1 to addition operation
     * @return A double value
     */
    public static double add(List param) {
        String n;
        BigDecimal x1;
        BigDecimal x2 = new BigDecimal("0");
        if (param != null && param.size() > 0) {
            Iterator iter = param.iterator();
            while (iter.hasNext()) {
                n = (String) iter.next();
                n = makeNull(n);
                x1 = new BigDecimal(n);
                x2 = x2.add(x1);
            }
        }
        return x2.doubleValue();
    }

    /**
     * precision math addition operation
     *
     * @param list param1 to addition operation
     * @return A double value
     */
    public static double sum(List param) {
        Double n;
        BigDecimal x1;
        BigDecimal x2 = new BigDecimal("0");
        if (param != null && param.size() > 0) {
            Iterator iter = param.iterator();
            while (iter.hasNext()) {
                n = (Double) iter.next();
                if (n != null) {
                    x1 = new BigDecimal(n.toString());
                    x2 = x2.add(x1);
                }

            }
        }
        return x2.doubleValue();
    }

    /**
     * precision math subtration operation
     *
     * @param double param1 and double param2 to subtration operation
     * @return A double value
     */
    public static double sub(double param1, double param2) {
        BigDecimal x1 = new BigDecimal(Double.toString(param1));
        BigDecimal x2 = new BigDecimal(Double.toString(param2));
        return x1.subtract(x2).doubleValue();
    }

    /**
     * precision math subtration operation
     *
     * @param String param1 and String param2 to subtration operation
     * @return A double value
     */
    public static double sub(String param1, String param2) {
        param1 = makeNull(param1);
        param2 = makeNull(param2);
        BigDecimal x1 = new BigDecimal(param1);
        BigDecimal x2 = new BigDecimal(param2);
        return x1.subtract(x2).doubleValue();
    }

    /**
     * precision math multiplication operation
     *
     * @param double param1 and double param2 to multiplication operation
     * @return A double value
     */
    public static double multiply(double param1, double param2) {
        BigDecimal x1 = new BigDecimal(Double.toString(param1));
        BigDecimal x2 = new BigDecimal(Double.toString(param2));
        return x1.multiply(x2).doubleValue();
    }

    /**
     * precision math multiplication operation
     *
     * @param String param1 and double String to multiplication operation
     * @return A double value
     */
    public static double multiply(String param1, String param2) {
        param1 = makeNull(param1);
        param2 = makeNull(param2);
        BigDecimal x1 = new BigDecimal(param1);
        BigDecimal x2 = new BigDecimal(param2);
        BigDecimal p = new BigDecimal("10000");
        x1 = x1.multiply(p);
        x1 = x1.multiply(x2);
        x1 = x1.divide(p, 2, BigDecimal.ROUND_HALF_UP);
        return x1.doubleValue();
    }

    public static double div(double param1, double param2) {

        return div(param1, param2, DEF_DIV_SCALE);

    }

    public static double div(String param1, String param2) {
        param1 = makeNull(param1);
        param2 = makeNull(param2);
        return div(param1, param2, DEF_DIV_SCALE);

    }

    /**
     * precision math divide operation
     *
     * @param double param1 and double param2 to divide operation
     * @param scale  decimal digits
     * @return A double value
     */
    public static double div(double param1, double param2, int scale) {

        if (scale < 0) {

            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal x1 = new BigDecimal(Double.toString(param1));
        BigDecimal x2 = new BigDecimal(Double.toString(param2));
        return x1.divide(x2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * precision math divide operation
     *
     * @param String param1 and String param2 to divide operation
     * @param scale  decimal digits
     * @return A double value
     */
    public static double div(String param1, String param2, int scale) {

        if (scale < 0) {

            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal x1 = new BigDecimal(param1);
        BigDecimal x2 = new BigDecimal(param2);
        return x1.divide(x2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * precision math round operation
     *
     * @param double param to round operation
     * @param scale  decimal digits
     * @return A double value
     */
    public static double round(double param1, int scale) {

        if (scale < 0) {

            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(param1));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    ;

    /**
     * precision math round operation
     *
     * @param double param to round operation
     * @param scale  decimal digits
     * @return A double value
     */
    public static double round(String param1, int scale) {
        param1 = makeNull(param1);
        if (scale < 0) {

            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(param1);
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    ;

    /**
     * calculate tax
     *
     * @param x1
     * @param rate
     * @return
     */
    public static double calculateTax(String x1, String rate) {
        x1 = makeNull(x1);
        rate = makeNull(rate);
        BigDecimal t = new BigDecimal(x1);
        BigDecimal r = new BigDecimal(rate);
        BigDecimal c = new BigDecimal("1");
        BigDecimal p = new BigDecimal("10000");
        c = r.add(c);
        t = t.multiply(p);
        t = t.divide(c, BigDecimal.ROUND_HALF_UP);
        t = t.multiply(r);
        t = t.divide(p, 2, BigDecimal.ROUND_HALF_UP);
        return t.doubleValue();
    }

    public static double toDouble(String s) {
        s = makeNull(s);
        return Double.valueOf(s).doubleValue();
    }

    /**
     * conversion A String number to Double
     *
     * @param param1
     * @return
     */
    public static Double parseDouble(String s) {
        s = makeNull(s);
        return Double.parseDouble(s);
    }

    /**
     * conversion A String number with bigDecimal doubleValue
     *
     * @param s
     * @return
     */
    public static Double parseDoubleB(String s) {
        s = makeNull(s);
        BigDecimal x = new BigDecimal(s);
        return new Double(x.doubleValue());
    }

    /**
     * conversion A String number to float value
     *
     * @param s
     * @return
     */
    public static Float parseFloat(String s) {
        s = makeNull(s);
        return Float.valueOf(s);
    }

    /**
     * conversion A String number to long value
     *
     * @param s
     * @return
     */
    public static Long parseLong(String s) {
        return Long.valueOf(s);
    }

    /**
     * conversion A String number to integer value
     *
     * @param s
     * @return
     */
    public static Integer parseInteger(String s) {
        s = makeNull(s);
        return Integer.valueOf(s);
    }

    /**
     * conversion A String number to date type value
     *
     * @param s
     * @return
     */
    public static Date parseDate(String date) {
        DateFormat df = DateFormat.getDateInstance();
        try {
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ------------------- null string handlers -------------------

    /**
     * Checks to see if the passed Object is null, if it is returns an empty but
     * non-null string, otherwise calls toString() on the object
     *
     * @param obj1 The passed Object
     * @return The toString() of the passed Object if not null, otherwise an
     * empty non-null String
     */
    public static String makeString(Object obj1) {
        if (obj1 != null)
            return obj1.toString();
        else
            return "";
    }

    /**
     * Checks to see if the passed string is <code>null</code> string
     *
     * @param a sring s
     * @return an empty non-null String
     */
    public static String fixedNullString(String s) {
        if (s != null && s.length() > 0) {
            if ("null".equalsIgnoreCase(s.trim())) {
                return "";
            }
            return s.trim();
        } else {
            return "";
        }

    }

    public static String fixedUIString(Object o) {
        if (o != null) {
            if ("null".equalsIgnoreCase(o.toString())) {
                return "";
            }
            return o.toString();
        } else {
            return "";
        }
    }

    /**
     * Checks to see if the passed string is null, if it is returns an empty but
     * non-null string.
     *
     * @param string1 The passed String
     * @return The passed String if not null, otherwise an empty non-null String
     */
    public static String checkNull(String string1) {
        if (string1 != null)
            return string1;
        else
            return "";
    }

    public static String checkNullEx(String string1) {
        if (string1 != null)
            return string1;
        else
            return "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    }

    public static String checkNullEx(Object obj) {
        if (obj != null)
            return obj.toString();
        else
            return "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    }

    /**
     * Checks to see if the passed string is null, if it is returns an empty but
     * non-null string.
     *
     * @param string1 The passed String
     * @return The passed String if not null, otherwise an empty non-null String
     */
    public static String checkNull(Object obj) {
        if (obj != null)
            return obj.toString();
        else
            return "";
    }

    /**
     * Checks to see if the passed string array is null.
     *
     * @param s The passed String array
     * @return if the passed String array if not null then return true.
     */
    public static boolean paramCheck(String[] s) {
        if (s != null && s.length > 0)
            return true;
        else
            return false;
    }

    /**
     * Checks to see if the passed string array is empty "".
     *
     * @param s The passed String array
     * @return if the passed String array if not null then return true.
     */
    public static boolean paramCheck(String s) {
        if (s != null && !"".equals(s) && !"0".equals(s) && !"0.00".equals(s))
            return true;
        else
            return false;
    }

    /**
     * Checks to see if the passed string is a empty string <code>""</code> or a
     * null string.
     *
     * @param s The passed String
     * @return if the passed String if not A <code>""</code> String then return
     * true.
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * Checks to see if the passed string is null, if it is returns an empty but
     * non-null string.
     *
     * @param string1 The passed String
     * @return The passed String if not null, otherwise an empty non-null String
     */
    public static String makeNull(String string1) {
        if (string1 != null && string1.length() > 0)
            return string1;
        else
            return "";
    }

    /**
     * Checks to see if the passed string is non-null, if it is returns an empty
     * but non-null string.
     *
     * @param string1 The passed String
     * @return The passed String if not null, otherwise an empty non-null String
     */
    public static String trim(String string1) {
        if (string1 != null)
            return string1.trim();
        else
            return "";
    }

    /**
     * Replaces all occurances of oldString in mainString with newString
     *
     * @param mainString The original string
     * @param oldString  The string to replace
     * @param newString  The string to insert in place of the old
     * @return mainString with all occurances of oldString replaced by newString
     */
    public static String replaceString(String mainString, String oldString, String newString) {
        if (mainString == null) {
            return null;
        }
        if (oldString == null || oldString.length() == 0) {
            return mainString;
        }
        if (newString == null) {
            newString = "";
        }

        int i = mainString.lastIndexOf(oldString);

        if (i < 0)
            return mainString;

        StringBuffer mainSb = new StringBuffer(mainString);

        while (i >= 0) {
            mainSb.replace(i, i + oldString.length(), newString);
            i = mainString.lastIndexOf(oldString, i - 1);
        }
        return mainSb.toString();
    }

    /**
     * Creates a single string from a List of strings seperated by a delimiter.
     *
     * @param list  a list of strings to join
     * @param delim the delimiter character(s) to use. (null value will join with
     *              no delimiter)
     * @return a String of all values in the list seperated by the delimiter
     */
    public static String join(List list, String delim) {
        if (list == null || list.size() < 1)
            return null;
        StringBuffer buf = new StringBuffer();
        Iterator i = list.iterator();

        while (i.hasNext()) {
            buf.append((String) i.next());
            if (i.hasNext())
                buf.append(delim);
        }
        return buf.toString();
    }

    /**
     * Splits a String on a delimiter into a List of Strings.
     *
     * @param str   the String to split
     * @param delim the delimiter character(s) to join on (null will split on
     *              whitespace)
     * @return a list of Strings
     */
    public static List split(String str, String delim) {
        List splitList = null;
        StringTokenizer st = null;

        if (str == null)
            return splitList;

        if (delim != null)
            st = new StringTokenizer(str, delim);
        else
            st = new StringTokenizer(str);

        if (st != null && st.hasMoreTokens()) {
            splitList = new ArrayList();

            while (st.hasMoreTokens())
                splitList.add(st.nextToken());
        }
        return splitList;
    }

    /**
     * Format array of string to A PL/SQL String
     *
     * @param str
     * @return A formate string with PL/SQL NOT IN syntax
     */
    public static String linkStr(String[] str) {
        String temp = "(";
        for (int i = 0; i < str.length; i++) {
            if (i == 0) {
                temp = temp + "'" + str[i] + "'";
            } else {
                temp = temp + "," + "'" + str[i] + "'";
            }
        }
        temp = temp + ")";
        return temp;
    }

    /**
     * 将字符串格式成sql形式
     *
     * @param str
     * @param delim
     * @return
     */
    public static String strToSql(String str, String delim) {
        StringTokenizer st = null;

        if (str == null)
            return str;

        if (delim != null)
            st = new StringTokenizer(str, delim);
        else
            st = new StringTokenizer(str);

        String temp = "(";
        if (st != null && st.hasMoreTokens()) {
            while (st.hasMoreTokens()) {
                temp = temp + "'" + st.nextToken() + "',";
            }
        }
        if (temp.length() > 1) {
            temp = temp.substring(0, temp.length() - 1) + ")";
        } else {
            temp = "";
        }
        return temp;
    }

    /**
     * 将数字字符串格式成sql形式
     *
     * @param str
     * @param delim
     * @return
     */
    public static String numberToSql(String str, String delim) {
        StringTokenizer st = null;

        if (str == null)
            return str;

        if (delim != null)
            st = new StringTokenizer(str, delim);
        else
            st = new StringTokenizer(str);

        String temp = "(";
        if (st != null && st.hasMoreTokens()) {
            while (st.hasMoreTokens()) {
                temp = temp + st.nextToken() + ",";
            }
        }
        if (temp.length() > 1) {
            temp = temp.substring(0, temp.length() - 1) + ")";
        } else {
            temp = "";
        }
        return temp;
    }

    /**
     * Formate a Integer num to a string id
     *
     * @param id
     * @return A String line number
     */
    public static String getLineId(int id) {
        String sid = "";
        if (id >= 0 && id <= 9) {
            sid = "0" + Integer.toString(id);
        } else {
            sid = Integer.toString(id);
        }
        return sid;
    }

    /**
     * parse a double "string"
     *
     * @param id
     * @return A string id
     */
    public static String checkId(String id) {
        String sid = id;
        int i = -1;
        if (id != null && id.length() > 0) {
            i = id.indexOf(".");
            if (i != -1) {
                sid = id.substring(0, i);
                return sid;
            }
        }
        return sid;
    }

    /**
     * count the days with compare the firstDate date and the last date
     *
     * @param firstDate
     * @param lastDate
     * @return A double days
     */
    public static double compareDateToDays(Date firstDate, Date lastDate) {
        if (firstDate == null || lastDate == null)
            throw new IllegalArgumentException("the paramater is null date.");
        long timeColon1 = firstDate.getTime();
        long timeColon2 = lastDate.getTime();
        long tmpCal = timeColon2 - timeColon1;
        long mm = 24 * 60 * 60 * 1000;
        double days = (double) (tmpCal / mm);
        return Math.abs(days);
    }

    /**
     * 比较两个时间，取消绝对值判断
     *
     * @param firstDate
     * @param lastDate
     * @return
     */
    public static double compareDateToDaysWithNoAbs(Date firstDate, Date lastDate) {
        if (firstDate == null || lastDate == null)
            throw new IllegalArgumentException("the paramater is null date.");
        long timeColon1 = firstDate.getTime();
        long timeColon2 = lastDate.getTime();
        long tmpCal = timeColon2 - timeColon1;
        long mm = 24 * 60 * 60 * 1000;
        double days = (double) (tmpCal / mm);
        return days;
    }

    public static boolean checkIsNullStr(String nullStr) {
        boolean ret = false;
        if (nullStr != null && nullStr.length() > 0) {
            if (nullStr.equals("null") || "".equals(nullStr.trim())) {
                ret = true;
            }
        } else {
            ret = true;
        }
        return ret;
    }

    /**
     * whether the part_no is user defined part number
     *
     * @param partNo
     * @return true or false
     */
    public static boolean isUserDefinedPartNo(String partNo) {
        boolean ret = false;
        String marker = null;
        if (partNo != null && !"".equals(partNo)) {
            if (partNo.length() > 3) {
                marker = partNo.substring(0, 3);
                if (marker.equalsIgnoreCase("WZD") || marker.equalsIgnoreCase("DZD")) {
                    ret = true;
                }
            }
        }
        return ret;
    }

    public static String fixedFormatMoney(String m) {
        String tmp = "";
        if (m != null && m.length() > 0) {
            String[] t = m.split(",");
            for (int i = 0; i < t.length; i++) {
                tmp += t[i];
            }
            System.out.println(tmp);
        } else {
            tmp = "0.00";
        }
        return tmp;
    }

    /**
     * 判断是否为合法的TimeStamp格式字符串
     *
     * @param s
     * @return
     */
    public static boolean isValidTimestamp(String s) {
        SimpleDateFormat df = null;
        try {
            df = new SimpleDateFormat("yyyy-MM-dd");
            df.parse(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidDate(String s) {
        SimpleDateFormat df = null;
        try {
            df = new SimpleDateFormat("yyyy");
            df.parse(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Date parseDate(String dateString, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date result = null;
        try {
            result = df.parse(dateString);
        } catch (Exception e) {
        }
        return result;
    }

    public static Date parseDateTime(String dateString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // DateFormat df = DateFormat.getDateTimeInstance();
        Date result = null;
        try {
            result = df.parse(dateString);
        } catch (Exception e) {
        }
        return result;
    }

    public static Date parseDTime(String dateString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // DateFormat df = DateFormat.getDateTimeInstance();
        Date result = null;
        try {
            result = df.parse(dateString);
        } catch (Exception e) {
        }
        return result;
    }

    public static Date parseHMDate(String dateString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // DateFormat df = DateFormat.getDateTimeInstance();
        Date result = null;
        try {
            result = df.parse(dateString);
        } catch (Exception e) {
        }
        return result;
    }

    public static String printDate(Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.format(date);
        } else {
            return "";
        }

    }

    public static String printDate(String format, Date date) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);

    }

    public static String printDateTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);

    }

    public static String getYearMonth(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        return df.format(date);

    }

    public static long getIntervalDays(Date from, Date to) {
        return (to.getTime() - from.getTime()) / (1000 * 60 * 60 * 24);
    }

    public static String getOriginalFileName(String fileName) {
        int dotPlace = fileName.lastIndexOf(".");
        String firstPart = "";
        String secondPart = "";
        if (dotPlace == -1) {
            return fileName;
        } else {
            firstPart = fileName.substring(0, dotPlace);
            secondPart = fileName.substring(dotPlace + 1, fileName.length());
        }
        dotPlace = firstPart.lastIndexOf(".");
        if (dotPlace == -1) {
            return secondPart;
        } else {
            return firstPart.substring(0, dotPlace + 1) + secondPart;
        }
    }

    public static boolean isNullString(String s) {
        return null == s || s.equals("");
    }

    /**
     * Tests if this date is before the specified date.
     *
     * @param firstDate a date.
     * @param when      a date.
     * @return return true or false
     * @throws NullPointerException if <code>firstDate or when</code> is null.
     */
    public static boolean dateBefore(Date firstDate, Date when) throws Exception {
        if (firstDate == null || when == null) {
            throw new Exception("the date param can not be empty!");
        }
        return firstDate.before(when);
    }

    /**
     * Tests if this date is after the specified date.
     *
     * @param firstDate a date.
     * @param when      a date.
     * @return return true or false
     * @throws NullPointerException if <code>firstDate or when</code> is null.
     */
    public static boolean dateAfter(Date firstDate, Date when) throws Exception {
        if (firstDate == null || when == null) {
            throw new Exception("the date param can not be empty!");
        }
        return firstDate.after(when);
    }

    /**
     * 判断第一日期（firstDate）减去第二个日期（secondDate）的差 是否 大于 多少天（days）
     *
     * @param firstDate  Date - 第一日期
     * @param secondDate Date - 第二个日期
     * @param days       double - 天数
     * @return int 1表示大于，0表示等于，-1表示小于
     */
    public static int compareDate(Date firstDate, Date secondDate, double days) throws Exception {
        if (firstDate == null || secondDate == null) {
            throw new Exception("日期不能为空");
        }
        long lFirst = firstDate.getTime();
        long lSecond = secondDate.getTime();
        long tmp = lFirst - lSecond;
        long lDays = (long) (days * 24 * 60 * 60 * 1000);
        if (tmp > lDays) {
            return 1;
        } else {
            if (tmp == lDays) {
                return 0;
            } else {
                return -1;
            }
        }

    }

    /**
     * 根据传入日期来判断该日期是上旬、中旬还是下旬
     *
     * @param date
     * @return
     * @throws IllegalArgumentException
     */
    public static int get10DaysSpanType(Date date) throws IllegalArgumentException {
        int returnValue = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        if (dayOfMonth >= 1 && dayOfMonth <= 10)
            returnValue = FIRST_TEN_DAYS;
        else if (dayOfMonth > 10 && dayOfMonth <= 20)
            returnValue = MIDDLE_TEN_DAYS;
        else if (dayOfMonth > 20 && dayOfMonth <= 31)
            returnValue = LAST_TEN_DAYS;
        else
            throw new IllegalArgumentException("the paramater is not a valid date.");

        return returnValue;
    }

    /**
     * 根据传入日期来判断该日期是上旬、中旬还是下旬
     *
     * @param dateStr
     * @return
     * @throws IllegalArgumentException
     */
    public static int get10DaysSpanType(String dateStr) throws IllegalArgumentException {
        DateFormat format = DateFormat.getDateInstance();
        Date date;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("the paramater is not a valid date string.");
        }
        return get10DaysSpanType(date);
    }

    /**
     * 取得时间在月份中对应的天数
     *
     * @param date
     * @return
     */
    public static int getMonthDays(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 将从fromDate 到 toDate 之间（包括 fromDate 和 toDate)的时间放到一个list中返回
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public static List getDaysList(Date fromDate, Date toDate) {
        ArrayList list = new ArrayList();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        while (fromDate.before(toDate)) {
            list.add(fromDate);
            cal.add(Calendar.DATE, 1);
            fromDate = cal.getTime();
        }
        list.add(toDate);
        return list;
    }

    /**
     * 如果日期是一个月份的第10天、第20天或最后一天，则返回true，否则返回false
     *
     * @param date
     * @return
     */
    public static boolean isLastTenDays(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.getActualMaximum(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DATE))
            return true;
        if (cal.get(Calendar.DATE) == 10)
            return true;
        if (cal.get(Calendar.DATE) == 20)
            return true;
        return false;
    }

    public static Date currentDateTime() {
        return Calendar.getInstance().getTime();
    }

    public static Date getLastDateOfMonth(Date dateTime) {
        Calendar cal1 = Calendar.getInstance();

        cal1.setTime(dateTime);
        Calendar cal2 = new GregorianCalendar(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.getActualMaximum((Calendar.DAY_OF_MONTH)));

        return cal2.getTime();
    }

    /**
     * 方法描述：获取本月的第一天
     *
     * @param dateTime
     * @return
     * @author wangsongwei
     */
    public static Date getFirstDateOfMonth(Date dateTime) {
        Calendar cal1 = Calendar.getInstance();

        cal1.setTime(dateTime);
        Calendar cal2 = new GregorianCalendar(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.getActualMinimum((Calendar.DAY_OF_MONTH)));

        return cal2.getTime();
    }

    public static int getDayOfWeek(Date dateTime) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);

        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @param currentDate
     * @param day
     * @return Date
     * @description 日期增加天数
     */
    public static Date addDay(Date currentDate, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * @param currentDate
     * @param hour
     * @return Date
     * @description 日期时间增加小时
     */
    public static Date addHour(Date currentDate, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.HOUR, hour);
        return calendar.getTime();
    }

    /**
     * 判断是否是负数
     *
     * @param n
     * @return
     */
    public static boolean isMinus(Double n) {
        boolean result = false;
        if (n != null && n < 0) {
            result = true;
        }
        return result;
    }

    /**
     * ȡ���取数字绝对值
     *
     * @param n
     * @return
     */
    public static Double abs(Double n) {
        if (n != null && n < 0) {
            return Math.abs(n.doubleValue());
        } else if (n != null && n > 0) {
            return n;
        } else {
            return new Double(0.0d);
        }
    }

    /**
     * 显式转换为负长整型值
     *
     * @param id
     * @return
     */
    public static Long toMinus(Long id) {
        String key;
        Long l = 0L;
        if (id == null) {
            throw new IllegalArgumentException("is not a long number");
        }
        key = "-" + id.toString();
        l = new Long(key);
        return l;
    }

    /**
     * 显式转换为负浮点数
     *
     * @param id
     * @return
     */
    public static Double toMinus(Double id) {
        String key;
        Double l = 0.0;
        if (id == null) {
            throw new IllegalArgumentException("is not a double number");
        }
        key = "-" + id.toString();
        l = new Double(key);
        return l;
    }

    /**
     * 判断字符串是否是数字
     *
     * @param s
     * @return
     */
    public static boolean isNumber(String s) {
        boolean ret = true;
        Pattern pattern = Pattern.compile("[0-9+./+-/]");
        char[] ss = s.toCharArray();
        for (int i = 0; i < ss.length; i++) {
            Matcher isNum = pattern.matcher(String.valueOf(ss[i]));
            if (!isNum.find()) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    /**
     * 处理Double类型数据的四舍五入以及小数位精确到第N位
     *
     * @param d          需要处理的double类型的数据
     * @param decimalNum 需要精确的小数位数
     */
    public static String printDouble(double d, int decimalNum) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(decimalNum);
        numberFormat.setGroupingUsed(false);

        return numberFormat.format(d);
    }

    public static void main(String[] args) {
        // test case
        System.out.println(CommonUtils.isNumber("-9"));
    }

    /**
     * 格式化金钱数位XXX,XXX.00
     *
     * @param num          需要格式化的金钱数
     * @param decimalNum   小数精确位数
     * @param defaultValue 当需要格式化数位空时，默认返回值,null 则返回0
     * @return String
     */
    public static String currencyFormat(Double num, int decimalNum, String defaultValue) {

        String fmRes = "0";

        if (num == null) {
            if (defaultValue != null)
                fmRes = defaultValue;
            return fmRes;
        } else if (num == 0) {
            return fmRes;
        }
        NumberFormat nf = NumberFormat.getInstance(Locale.CHINA);
        nf.setMinimumFractionDigits(decimalNum);
        nf.setMaximumFractionDigits(decimalNum);

        fmRes = nf.format(num);

        return fmRes;
    }

    /**
     * 格式化金钱数位XXX,XXX.00
     *
     * @param num          需要格式化的金钱数
     * @param decimalNum   小数精确位数
     * @param defaultValue 当需要格式化数位空时，默认返回值,null 则返回0
     * @return String
     */
    public static String currencyFormat(Float num, int decimalNum, String defaultValue) {

        String fmRes = "0";

        if (num == null) {
            if (defaultValue != null)
                fmRes = defaultValue;
            return fmRes;
        } else if (num == 0) {
            return fmRes;
        }
        NumberFormat nf = NumberFormat.getInstance(Locale.CHINA);
        nf.setMinimumFractionDigits(decimalNum);
        nf.setMaximumFractionDigits(decimalNum);

        fmRes = nf.format(num);

        return fmRes;
    }

    public static Float floatValue(Double d) {
        if (d != null) {
            return (Float) d.floatValue();
        }
        return null;
    }

    public static Double doubleValue(Float f) {
        if (f != null) {
            return (Double) f.doubleValue();
        }
        return null;
    }

    public static String valueOf(Integer integer) {
        if (integer != null) {
            return String.valueOf(integer);
        } else {
            return null;
        }
    }

    public static String formatString(String str) {
        String rv = "";
        if (str != null) {
            if (str.contains(";")) {
                rv = str.replace(";", "<br>");
            } else {
                rv = str;
            }
        }
        return rv;
    }

    /**
     * 将字符串格式成sql like 的形式
     *
     * @param str
     * @param delim
     * @param column
     * @return
     */
    public static String strToSqlLike(String str, String delim, String column) {
        String[] strArr = null;
        String strLike = null;
        String strNull = " 1 = 1";
        // 如果需要处理的字符串为空，返回
        if (str == null || str.trim().length() == 0) {
            return strNull;
        }
        if (delim == null || delim.trim().length() == 0) {
            return strNull;
        } else {
            strArr = str.split(delim);
        }
        if (column == null || column.trim().length() == 0) {
            return strNull;
        }
        StringBuffer sb = new StringBuffer("(");
        // 循环生成字符串
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].trim().length() > 0) {
                sb.append(column);
                sb.append(" like '%");
                sb.append(strArr[i].trim().toUpperCase());
                sb.append("%' or ");
            }
        }
        if (sb.length() > 1) {
            return sb.append(")").toString().replaceAll(" or \\)", ")");
        } else {
            return strNull;
        }
    }

    /**
     * 将字符串格式成sql like 的形式
     *
     * @param str
     * @param delim
     * @param column
     * @return
     */
    public static boolean isInnerNetwork(String ip) {
        String allowIpRegex = "(127[.]0[.]0[.]1)|(localhost)|" + "(10[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3})|"
                + "(172[.]((1[6-9])|(2\\d)|(3[01]))[.]\\d{1,3}[.]\\d{1,3})|" + "(192[.]168[.]\\d{1,3}[.]\\d{1,3})";
        // Pattern pattern = Pattern.compile(allowIpRegex);
        return Pattern.matches(allowIpRegex, ip);
    }

    /**
     * 查询出所有有效的品牌信息
     *
     * @return
     */
    public static List<TmBrandPO> findAllBrands() {
        TmBrandPO po = new TmBrandPO();
        // 查询有效的品牌
        po.setStatus(Constant.STATUS_ENABLE);
        return factory.select(po);
    }

    // modify by xiayanpeng begin 查询出所有有效的业务范围信息
    public static List<TmBusinessAreaPO> findAllBusinessAreaPO(Long companyId) {
        TmBusinessAreaPO po = new TmBusinessAreaPO();
        // 查询有效的品牌
        po.setStatus(Constant.STATUS_ENABLE);
        // po.setCompanyId(companyId);
        return factory.select(po);
    }

    /**
     * 查询省市
     *
     * @return 省市列表
     */
    public static List<TmOrgPO> findAllProvicePO() {
        TmOrgPO po = new TmOrgPO();
        po.setDutyType(Constant.DUTY_TYPE_SMALLREGION);
        po.setStatus(Constant.STATUS_ENABLE);
        return factory.select(po);
    }

    /**
     * 根据职位ID查询选中的省份
     * param poseId职位Id
     *
     * @return 省市列表
     */
    public static List<TrPoseRegionPO> findCeckProvicePO(Long poseId) {
        TrPoseRegionPO po = new TrPoseRegionPO();
        po.setPoseId(poseId);
        return factory.select(po);
    }
    // modify by xiayapeng end

    /**
     * 根据品牌ID查询出指定的有效的品牌信息
     *
     * @param brandIds : 以String的形式传递多个或单个品牌ID
     * @return
     */
    public static List<TmBrandPO> findAllBrands(String brandIds) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT T.BRAND_ID,T.BRAND_NAME,T.ENGLISH_NAME,T.BRAND_CODE,T.REMARK,T.BUSINESS_TYPE,T.BILL_TOTAL,");
        sb.append("T.STATUS,T.CREATE_DATE,T.UPDATE_DATE,T.CREATE_BY,T.UPDATE_BY,T.OLD_MODEL_SIFT FROM TM_BRAND T");
        sb.append(" WHERE T.STATUS = ?");
        sb.append(" AND T.BRAND_ID IN (" + brandIds + ")");
        sb.append(" ORDER BY T.BRAND_NAME ");

        List<Object> params = new ArrayList<Object>();
        params.add(Constant.STATUS_ENABLE);

        return factory.select(sb.toString(), params, new DAOCallback<TmBrandPO>() {
            public TmBrandPO wrapper(ResultSet rs, int idx) {
                TmBrandPO po = new TmBrandPO();
                try {
                    po.setBrandId(rs.getLong("BRAND_ID"));
                    po.setBrandName(rs.getString("BRAND_NAME"));
                    po.setEnglishName(rs.getString("ENGLISH_NAME"));
                    po.setBrandCode(rs.getString("BRAND_CODE"));
                    po.setRemark(rs.getString("REMARK"));
                    po.setBusinessType(rs.getString("BUSINESS_TYPE"));
                    po.setBillTotal(rs.getString("BILL_TOTAL"));
                    po.setStatus(rs.getInt("STATUS"));
                    po.setCreateDate(rs.getTimestamp("CREATE_DATE"));
                    po.setUpdateDate(rs.getTimestamp("UPDATE_DATE"));
                    po.setCreateBy(rs.getLong("CREATE_BY"));
                    po.setUpdateBy(rs.getLong("UPDATE_BY"));
                    po.setOldModelSift(rs.getInt("OLD_MODEL_SIFT"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return po;
            }
        });
    }

    /**
     * 根据组织ID查询用户信息
     *
     * @param userId
     * @param orgId
     * @return
     */
    public static PageResult<Map<String, Object>> getUserInfoByOrgID(Long orgId) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT tu.user_id, tu.acnt, tu.NAME, tu.email \n");
        sql.append("  FROM tc_user tu, tr_user_pose rp, tc_pose tp \n");
        sql.append(" WHERE tu.user_id = rp.user_id \n");
        sql.append("   AND rp.pose_id = tp.pose_id \n");
        if (orgId != null)
            sql.append("   AND tp.org_id = " + orgId + " \n");
        sql.append("   AND tu.user_status = " + Constant.STATUS_ENABLE + " \n");
        return factory.pageQuery(sql.toString(), null, new DAOCallback<Map<String, Object>>() {
            public Map<String, Object> wrapper(ResultSet rs, int idx) {
                return PersisUtil.getMap("CommonUtils.getUserInfoByOrgID", rs);
            }
        }, 9999, 1);
    }

    /**
     * 根据组织关系ID查出所有有效的品牌
     *
     * @return
     */
    public static List<TmBrandPO> findAllBrands(Long orgId) {

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT T.* FROM TM_BRAND T,TM_ORG_BUSINESS_AREA O ");
        sb.append(" WHERE T.BRAND_ID = O.BRAND_ID ");
        sb.append(" AND T.STATUS = ? ");
        sb.append(" AND O.ORG_ID = ? ");
        sb.append(" ORDER BY T.BRAND_NAME ");

        List<Object> params = new ArrayList<Object>();
        params.add(Constant.STATUS_ENABLE);
        params.add(orgId);

        return factory.select(sb.toString(), params, new DAOCallback<TmBrandPO>() {
            public TmBrandPO wrapper(ResultSet rs, int idx) {
                TmBrandPO po = new TmBrandPO();
                try {
                    po.setBrandId(rs.getLong("BRAND_ID"));
                    po.setBrandName(rs.getString("BRAND_NAME"));
                    po.setEnglishName(rs.getString("ENGLISH_NAME"));
                    po.setBrandCode(rs.getString("BRAND_CODE"));
                    po.setRemark(rs.getString("REMARK"));
                    po.setBusinessType(rs.getString("BUSINESS_TYPE"));
                    po.setBillTotal(rs.getString("BILL_TOTAL"));
                    po.setStatus(rs.getInt("STATUS"));
                    po.setCreateDate(rs.getTimestamp("CREATE_DATE"));
                    po.setUpdateDate(rs.getTimestamp("UPDATE_DATE"));
                    po.setCreateBy(rs.getLong("CREATE_BY"));
                    po.setUpdateBy(rs.getLong("UPDATE_BY"));
                    po.setOldModelSift(rs.getInt("OLD_MODEL_SIFT"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return po;
            }
        });
    }

    /**
     * 根据组织关系ID查出所有有效的品牌
     *
     * @return
     */
    public static List<TmBrandPO> findAllBrandsForActivity(Long orgId, String brandIds) {

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT T.* FROM TM_BRAND T,TM_ORG_BUSINESS_AREA O ");
        sb.append(" WHERE T.BRAND_ID = O.BRAND_ID ");
        sb.append(" AND T.STATUS = ? ");
        sb.append(" AND O.ORG_ID = ? ");
        sb.append(" AND T.BRAND_ID IN (");
        sb.append(brandIds);
        sb.append(")");
        sb.append(" ORDER BY T.BRAND_NAME ");

        List<Object> params = new ArrayList<Object>();
        params.add(Constant.STATUS_ENABLE);
        params.add(orgId);

        return factory.select(sb.toString(), params, new DAOCallback<TmBrandPO>() {
            public TmBrandPO wrapper(ResultSet rs, int idx) {
                TmBrandPO po = new TmBrandPO();
                try {
                    po.setBrandId(rs.getLong("BRAND_ID"));
                    po.setBrandName(rs.getString("BRAND_NAME"));
                    po.setEnglishName(rs.getString("ENGLISH_NAME"));
                    po.setBrandCode(rs.getString("BRAND_CODE"));
                    po.setRemark(rs.getString("REMARK"));
                    po.setBusinessType(rs.getString("BUSINESS_TYPE"));
                    po.setBillTotal(rs.getString("BILL_TOTAL"));
                    po.setStatus(rs.getInt("STATUS"));
                    po.setCreateDate(rs.getTimestamp("CREATE_DATE"));
                    po.setUpdateDate(rs.getTimestamp("UPDATE_DATE"));
                    po.setCreateBy(rs.getLong("CREATE_BY"));
                    po.setUpdateBy(rs.getLong("UPDATE_BY"));
                    po.setOldModelSift(rs.getInt("OLD_MODEL_SIFT"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return po;
            }
        });
    }

    /**
     * 根据组织关系ID查出所有有效的品牌
     *
     * @return
     */
    public static List<TmOrgPO> findAgentAndDistribution(Long orgId) {

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT O.ORG_ID,O.ORG_NAME FROM TM_ORG O ");
        sb.append(" WHERE O.ORG_ID = ");
        sb.append(orgId);
        sb.append(" UNION ");
        sb.append("SELECT O.ORG_ID,O.ORG_NAME FROM TM_ORG O ");
        sb.append(" WHERE O.PARENT_ORG_ID = ");
        sb.append(orgId);

        List<Object> params = new ArrayList<Object>();

        return factory.select(sb.toString(), params, new DAOCallback<TmOrgPO>() {
            public TmOrgPO wrapper(ResultSet rs, int idx) {
                TmOrgPO po = new TmOrgPO();
                try {
                    po.setOrgId(rs.getLong("ORG_ID"));
                    po.setOrgName(rs.getString("ORG_NAME"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return po;
            }
        });
    }

    public static List<TcCodePO> findTcCodeByType(Integer type) {

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT CODE_ID,CODE_DESC,TYPE_NAME FROM TC_CODE ");
        sb.append(" WHERE TYPE = ");
        sb.append(type);

        List<Object> params = new ArrayList<Object>();

        return factory.select(sb.toString(), params, new DAOCallback<TcCodePO>() {
            public TcCodePO wrapper(ResultSet rs, int idx) {
                TcCodePO po = new TcCodePO();
                try {
                    po.setCodeId(rs.getString("CODE_ID"));
                    po.setCodeDesc(rs.getString("CODE_DESC"));
                    po.setTypeName(rs.getString("TYPE_NAME"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return po;
            }
        });
    }

    /**
     * BY 代理商ID 查询出指定代理商信息
     *
     * @param dealerId 代理商ID : dealerId
     * @return
     */
    public static TmDealerPO getDealerInfoByDealerId(Long dealerId) {
        if (null != dealerId && !"".equals(dealerId)) {
            TmDealerPO po = new TmDealerPO();
            po.setDealerId(dealerId);
            List<TmDealerPO> list = factory.select(po);
            if (null != list && list.size() > 0) {
                po = list.get(0);
            }
            return po;
        } else {
            return null;
        }
    }

    /**
     * 根据组织关系ID查出对应的经销商
     *
     * @return
     */
    public static TmDealerPO findAllDealers(Long orgId) {

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT T.* FROM TM_DEALER T,TM_DEALER_ORG_RELATION D ");
        sb.append(" WHERE T.DEALER_ID = D.DEALER_ID ");
        sb.append(" AND T.STATUS = ? ");
        sb.append(" AND D.ORG_ID = ? ");
        sb.append(" ORDER BY T.DEALER_ID ");

        List<Object> params = new ArrayList<Object>();
        params.add(Constant.STATUS_ENABLE);
        params.add(orgId);

        List<TmDealerPO> list = factory.select(sb.toString(), params, new DAOCallback<TmDealerPO>() {
            public TmDealerPO wrapper(ResultSet rs, int idx) {
                TmDealerPO po = new TmDealerPO();
                try {
                    po.setDealerId(rs.getLong("DEALER_ID"));
                    po.setCompanyId(rs.getLong("COMPANY_ID"));
                    po.setDealerType(rs.getInt("DEALER_TYPE"));
                    po.setDealerCode(rs.getString("DEALER_CODE"));
                    po.setDealerName(rs.getString("DEALER_NAME"));
                    po.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
                    po.setStatus(rs.getInt("STATUS"));
                    // po.setProvinceId(rs.getInt("PROVINCE_ID"));
                    // po.setCityId(rs.getInt("CITY_ID"));
                    po.setZipCode(rs.getString("ZIP_CODE"));
                    po.setAddress(rs.getString("ADDRESS"));
                    po.setPhone(rs.getString("PHONE"));
                    po.setFaxNo(rs.getString("FAX_NO"));
                    po.setLinkMan(rs.getString("LINK_MAN"));
                    // po.setTaxAccounts(rs.getString("TAX_ACCOUNTS"));
                    // po.setReciveAccount(rs.getString("RECIVE_ACCOUNT"));
                    po.setBeginBank(rs.getString("BEGIN_BANK"));
                    // po.setComLevel(rs.getInt("COM_LEVEL"));
                    // po.setLevelType(rs.getInt("LEVEL_TYPE"));
                    po.setRemark(rs.getString("REMARK"));
                    // po.setQadCode(rs.getString("QAD_CODE"));
                    // po.setAgentCode(rs.getString("AGENT_CODE"));
                    po.setCreateDate(rs.getTimestamp("CREATE_DATE"));
                    po.setUpdateDate(rs.getTimestamp("UPDATE_DATE"));
                    po.setCreateBy(rs.getLong("CREATE_BY"));
                    po.setUpdateBy(rs.getLong("UPDATE_BY"));
                    // po.setSaleArea(rs.getString("SALE_AREA"));
                    // po.setStockSet(rs.getString("STOCK_SET"));
                    // po.setIsMonopoly(rs.getInt("IS_MONOPOLY"));
                    // po.setAgType(rs.getInt("AG_TYPE"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return po;
            }
        });

        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据父组织ID，查询分销商信息
     *
     * @param parentOrgId
     * @return
     */
    public static List<TmOrgPO> getFxOrgIDByParentId(Long parentOrgId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT org.org_id, org.org_code, org.org_name, org.org_type, org.company_id \n");
        sql.append("  FROM tm_org org, tm_dealer dea, tm_dealer_org_relation dor \n");
        sql.append(" WHERE org.org_id = dor.org_id \n");
        sql.append("   AND dea.dealer_id = dor.dealer_id \n");
        // modified by andy.ten@tom.com 去掉分销
        // sql.append("   AND org.org_type = "+Constant.ORG_TYPE_DISTRIBUTION+" \n");
        sql.append("   AND org.status = " + Constant.STATUS_ENABLE + " \n");
        sql.append("   AND dea.status = " + Constant.STATUS_ENABLE + " \n");
        sql.append("   AND org.parent_org_id = " + parentOrgId + " \n");

        return factory.select(sql.toString(), null, new DAOCallback<TmOrgPO>() {
            public TmOrgPO wrapper(ResultSet rs, int idx) {
                TmOrgPO po = new TmOrgPO();
                try {
                    po.setOrgId(rs.getLong("org_id"));
                    po.setOrgCode(rs.getString("org_code"));
                    po.setOrgName(rs.getString("org_name"));
                    po.setOrgType(rs.getInt("org_type"));
                    po.setCompanyId(rs.getLong("company_id"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return po;
            }
        });
    }

    /**
     * 查询出所有有效的颜色信息
     *
     * @param type
     * @return
     */
    public static List<TmColorPO> findAllColors() {
        TmColorPO po = new TmColorPO();
        po.setStatus(Constant.STATUS_ENABLE);
        return factory.select(po);
    }

    /**
     * 查询出有效的公共参数类型
     *
     * @param TcCodePO
     * @return
     */
    public static List<TcCodePO> findAllPublicParamCodes(TcCodePO po) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT DISTINCT T.TYPE,T.TYPE_NAME FROM TC_CODE T WHERE 1=1 ");

        List<Object> params = new ArrayList<Object>();
        if (null != po.getStatus() && !"".equals(po.getStatus())) {
            // 根据传递的参数查询：有效无效
            params.add(po.getStatus());
            sb.append(" AND T.STATUS = ? ");
        } else {
            // 默认查询有效数据
            params.add(Constant.STATUS_ENABLE);
            sb.append(" AND T.STATUS = ? ");
        }
        // 查询是否可以修改
        if (null != po.getCanModify() && !"".equals(po.getCanModify())) {
            params.add(po.getCanModify());
            sb.append(" AND T.CAN_MODIFY = ? ");
        }
        sb.append(" ORDER BY T.TYPE_NAME");

        return factory.select(sb.toString(), params, new DAOCallback<TcCodePO>() {
            public TcCodePO wrapper(ResultSet rs, int idx) {
                TcCodePO po = new TcCodePO();
                try {
                    po.setType(rs.getString("TYPE"));
                    po.setTypeName(rs.getString("TYPE_NAME"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return po;
            }
        });
    }

    /**
     * 根据业务ID，业务类型查询所有相关附件
     *
     * @param id        业务ID
     * @param tableName 业务类型即表名
     * @return create by lwj 2009-12-22
     */
    public static List<TmAttachmentPO> getAttachmentList(Long id, String tableName) {
        TmAttachmentPO po = new TmAttachmentPO();
        po.setBusinessId(id);
        po.setAttachmentType(tableName);
        return factory.select(po);
    }

    /**
     * 根据附件ID删除附件
     *
     * @param id
     * @return
     * @throws FileStoreException
     */
    public static int delAttachment(Long id, String fileId) throws FileStoreException {
        FileStore fs = FileStore.getInstance();
        fs.delete(fileId);
        TmAttachmentPO po = new TmAttachmentPO();
        po.setAttachmentId(id);
        return factory.delete(po);
    }

    /**
     * 根据业务参数类型代码取得业务参数列表
     *
     * @param typeCode
     * @return
     */
    public static List<TmBusinessParaPO> getBussinessParaByType(String typeCode) {
        TmBusinessParaPO po = new TmBusinessParaPO();
        po.setTypeCode(Integer.valueOf(typeCode));
        return factory.select(po);
    }

    /**
     * 根据业务参数代码返回参数值
     *
     * @param paraId
     * @return
     */
    public static String getBussinessParaByCode(String paraId) {
        TmBusinessParaPO po = new TmBusinessParaPO();
        po.setParaId(Integer.valueOf(paraId));
        List<TmBusinessParaPO> list = factory.select(po);
        if (list != null && list.size() > 0) {
            po = list.get(0);
            return po.getParaValue();
        } else {
            return "";
        }
    }

    /**
     * 根据ORGID返回收车地点
     *
     * @param orgId
     * @return
     */
    public static List<TmReceiveAddressPO> getAddressByOrgId(Long orgId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT t.address_id, t.TAKECAR_SITE_NAME receive_address \n");
        sql.append("  FROM tm_receive_address t, tm_dealer_org_relation dor, tm_org org \n");
        sql.append(" WHERE t.dealer_id = dor.dealer_id \n");
        sql.append("   AND dor.org_id = org.org_id \n");
        sql.append("   AND org.org_id = " + orgId + " \n");

        return factory.select(sql.toString(), null, new DAOCallback<TmReceiveAddressPO>() {
            public TmReceiveAddressPO wrapper(ResultSet rs, int idx) {
                TmReceiveAddressPO po = new TmReceiveAddressPO();
                try {
                    po.setAddressId(rs.getLong("address_id"));
                    po.setReceiveAddress(rs.getString("receive_address"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return po;
            }
        });
    }

    /**
     * 根据规则返回编号
     *
     * @param dateType 日期格式
     * @param orgId    如果需要DEALERCODE则传ORGID
     * @param sclass   根据品牌得到的头字母类似T/Q/P
     * @param flag     流水号前的分隔符
     * @param num      流水号初始数
     * @return create by lwj
     */
    public static String getNoByRules(String sclass, Long orgId, String dateType, String flag, int num) {
        StringBuffer returnNo = new StringBuffer("");
        StringBuffer date = new StringBuffer("");
        String year = new String("");
        String month = new String("");
        String day = new String("");
        SvoGetnoPO spo = new SvoGetnoPO();
        TmDealerPO dpo = null;

        spo.setSclass(sclass);
        if (orgId != null) {
            dpo = CommonUtils.findAllDealers(orgId);
            spo.setDealerCode(dpo.getDealerCode());
        }
        if (dateType != null && !dateType.equals("")) {
            Date aDate = new Date(System.currentTimeMillis());
            year = DateUtil.getYearByDate(aDate);
            month = DateUtil.getMonthByDate(aDate);
            day = DateUtil.getDayByDate(aDate);
            if (dateType.equals("YYYY"))
                date.append(year);
            else if (dateType.equals("YY"))
                date.append(year.substring(2, 4));
            else if (dateType.equals("YYYYMM"))
                date.append(year).append(month);
            else if (dateType.equals("YYMM"))
                date.append(year.substring(2, 4)).append(month);
            else if (dateType.equals("YYYYMMDD"))
                date.append(year).append(month).append(day);
            else if (dateType.equals("YYMMDD"))
                date.append(year.substring(2, 4)).append(month).append(day);
            spo.setStime(date.toString());
        }
        // 查询SVO编号表中当前年月的流水号，FOR UPDATE防止并发
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT     nums \n");
        sql.append("      FROM svo_getno \n");
        sql.append("     WHERE sclass = '" + sclass + "' AND stime = '" + spo.getStime() + "'  \n");
        if (orgId != null)
            sql.append("	   AND dealer_code = '" + spo.getDealerCode() + "' \n");
        sql.append("FOR UPDATE \n");
        List<Integer> list = factory.select(sql.toString(), null, new DAOCallback<Integer>() {
            public Integer wrapper(ResultSet rs, int idx) {
                Integer num = new Integer(0);
                try {
                    num = rs.getInt("NUMS");
                    return num;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DAOException(e);
                }
            }
        });
        if (list != null && list.size() > 0) {
            // 如果存在则更新流水号
            num = ((Integer) list.get(0)).intValue() + 1;
            SvoGetnoPO con = new SvoGetnoPO();
            con.setNums(num);
            factory.update(spo, con);
        } else {
            // 没有则新增
            spo.setNums(Integer.valueOf(num));
            factory.insert(spo);
        }
        returnNo.append(sclass);
        if (orgId != null)
            returnNo.append(dpo.getDealerCode().trim());
        returnNo.append(date.toString());
        if (flag != null)
            returnNo.append(flag);
        returnNo.append(num);
        return returnNo.toString();
    }

    /**
     * 查询大区列表
     *
     * @return
     */
    public static List<TmOrgPO> getTeamList(Integer orgType) {
        TmOrgPO opo = new TmOrgPO();
        opo.setOrgType(orgType);
        opo.setStatus(Constant.STATUS_ENABLE);
        return factory.select(opo);
    }

    /**
     * 通过 CODEID 取得详细信息
     *
     * @param codeId
     * @return
     */
    public static TcCodePO findTcCodeDetailByCodeId(Integer codeId) {
        TcCodePO po = new TcCodePO();
        po.setCodeId(codeId.toString());
        List<TcCodePO> list = factory.select(po);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static Object getDataFromMap(Map<String, Object> map, String key) {

        Object resultObj = null;
        if (map != null && key != null && map.containsKey(key)) {
            resultObj = CommonUtils.checkNull(map.get(key));
        } else {
            resultObj = new String("");
        }
        return resultObj;
    }

    /**
     * 根据职位查询对应用户拥有的产地权限
     *
     * @param poseId 职位ID
     * @return String 产地CODE组成的字符串 格式如：yieldlyCode1+","+yieldlyCode2+... 如果没有则返回
     * -1
     */
    @SuppressWarnings("unchecked")
    public static String findYieldlyByPoseId(Long poseId) {

        String yieldlys = "";

        if (poseId == null)
            return "-1";

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT A.*\n");
        sql.append("  FROM TM_BUSINESS_AREA A, TM_POSE_BUSINESS_AREA B\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND A.AREA_ID = B.AREA_ID\n");
        sql.append("   AND B.POSE_ID = ?");

        List<Object> paramList = new ArrayList<Object>();
        paramList.add(poseId);

        List<TmBusinessAreaPO> businessList = factory.select(sql.toString(), paramList, new POCallBack(factory, TmBusinessAreaPO.class));
        if (businessList != null && businessList.size() > 0) {
            for (TmBusinessAreaPO tmBusinessAreaPO : businessList) {
                if (tmBusinessAreaPO.getAreaId() != null)
                    yieldlys = yieldlys + tmBusinessAreaPO.getAreaId() + ",";
            }
        }

        if (!"".equals(yieldlys) && yieldlys.length() > 0)
            yieldlys = yieldlys.substring(0, yieldlys.length() - 1);

        return yieldlys;
    }

    /**
     * 通过车厂公司判断当前登录系统，若返回值为0表示登录系统为微车系统，1表示登录系统为轿车系统
     *
     * @param oemCom 当前系统的所对应的车厂公司id
     * @return String
     */
    public static Integer getNowSys(Long oemCom) {
        Integer flag = null; // 0表示微车系统，1表示轿车系统
        if (Constant.OEM_COM_SVC.equals(oemCom.toString())) {
            flag = 0;
        } else {
            flag = 1;
        }

        return flag;
    }

    /**
     * List 空校验
     *
     * @param list
     * @return boolean
     */
    public static boolean isNullList(List<?> list) {
        if (null == list || list.size() <= 0) {
            return true;
        }

        return false;
    }

    /**
     * Map 空校验
     *
     * @param map
     * @return boolean
     */
    public static boolean isNullMap(Map<?, ?> map) {
        if (null == map || map.size() <= 0) {
            return true;
        }

        return false;
    }

    /**
     * *** add by liuxh 20101229 增加区分公司方法 *******
     */
    @SuppressWarnings("unchecked")
    public static String getCurCompanyCode() throws Exception {
        String companyCode = Constant.COMPANY_CODE_CVS;
        String sql = "SELECT COMPANY_CODE FROM COMPANY_JC_CVS";
        List list = factory.select(sql, null, new DAOCallback<String>() {
            public String wrapper(ResultSet rs, int idx) {
                try {
                    return rs.getString("COMPANY_CODE");
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }
        });
        if (list.size() > 0) {
            companyCode = (String) list.get(0);
        }
        if (!companyCode.equals(Constant.COMPANY_CODE_CVS) && !companyCode.equals(Constant.COMPANY_CODE_JC)) {
            throw new BizException("错误的参数设置 companyCode:" + companyCode);
        }
        return companyCode;
    }

    /****** add by liuxh 20101229 增加区分公司方法 ********/

    /**
     * ************************* ranjian add start **************************
     */
    public static String checkNullNum(String string1) {
        if (string1 != null && string1 != "" && !string1.equals("null"))
            return string1;
        else
            return "0";
    }

    /**************************** end ***************************/

    /**
     * 销售取号方法
     */

    public static String getBusNo(String busType, long areaId) throws Exception {
        String DNO = "";
        try {
            if (busType.equals(Constant.NOCRT_TOTAL_PRO_NO) || busType.equals(Constant.NOCRT_TOTAL_PRO_NO2)) { //生产订单号生成12位
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
                String dateStr2 = sdf2.format(new Date());
                List par1 = new ArrayList();
                String curNo = "";
                //Calendar ca = Calendar.getInstance();
                //System.out.println("33333333:"+String.valueOf(ca.get(Calendar.MONTH)));
                String sql = "SELECT SW_SEQ FROM TM_SWIFT_PRODUCT_NO WHERE SW_MONTH=? FOR UPDATE";
                par1.add(Long.valueOf(dateStr2));
                List<Long> list = factory.select(sql, par1, new DAOCallback<Long>() {
                    public Long wrapper(ResultSet rs, int idx) {
                        try {
                            return rs.getLong("SW_SEQ");
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0L;
                        }
                    }
                });
                if (list.size() <= 0) {// 第一次获取SEQ
                    TmSwiftProductNoPO po = new TmSwiftProductNoPO();
                    po.setSwMonth(Long.valueOf(dateStr2));
                    po.setSwSeq(2L);
                    po.setCreateDate(new Date());
                    po.setUpdateDate(new Date());
                    factory.insert(po);
                    curNo = "1";
                } else if (list.size() > 1) {
                    throw new RuntimeException("流水号设置重复,请检查.");
                } else {
                    List par2 = new ArrayList();
                    curNo = String.valueOf(Long.parseLong(list.get(0).toString()));
                    String sqlUpdate = "UPDATE TM_SWIFT_PRODUCT_NO SET SW_SEQ=? WHERE SW_MONTH=? ";
                    par2.add((Long.parseLong(curNo) + 1));
                    par2.add(Long.valueOf(dateStr2));
                    factory.update(sqlUpdate, par2);
                }
                if (busType.equals(Constant.NOCRT_TOTAL_PRO_NO2)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
                    String dateStr = sdf.format(new Date());
                    String zoneStr = "";
                    if (curNo.length() < 6) {
                        for (int i = 0; i < 6 - curNo.length(); i++) {
                            zoneStr += "0";
                        }
                    }
                    DNO = "TD" + dateStr + zoneStr + curNo;
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                    String dateStr = sdf.format(new Date());
                    String zoneStr = "";
                    if (curNo.length() < 6) {
                        for (int i = 0; i < 6 - curNo.length(); i++) {
                            zoneStr += "0";
                        }
                    }
                    DNO = dateStr + zoneStr + curNo;
                }
            } else {
                String curNo = "";
                Calendar ca = Calendar.getInstance();
                List par3 = new ArrayList();
                String sql = "SELECT SW_SEQ FROM TM_SWIFT_NO WHERE SW_TYPE=? AND SW_YEAR=? FOR UPDATE";
                par3.add(busType);
                par3.add(String.valueOf(ca.get(Calendar.YEAR)));
                List<Long> list = factory.select(sql, par3, new DAOCallback<Long>() {
                    public Long wrapper(ResultSet rs, int idx) {
                        try {
                            return rs.getLong("SW_SEQ");
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0L;
                        }
                    }
                });
                if (list.size() <= 0) {// 第一次获取SEQ
                    TmSwiftNoPO tsn = new TmSwiftNoPO();
                    tsn.setSwType(busType);
                    tsn.setSwYear(ca.get(Calendar.YEAR));
                    tsn.setSwSeq(2L);
                    //tsn.setAreaId(areaId);
                    // tsn.setCreateBy(logonUser.getUserId());
                    tsn.setCreateDate(new Date());
                    // tsn.setUpdateBy(logonUser.getUserId());
                    tsn.setUpdateDate(new Date());
                    factory.insert(tsn);
                    curNo = "1";
                } else if (list.size() > 1) {
                    throw new RuntimeException("流水号设置重复,请检查.");
                } else {
                    List par4 = new ArrayList();
                    curNo = String.valueOf(Long.parseLong(list.get(0).toString()));
                    String sqlUpdate = "UPDATE TM_SWIFT_NO SET SW_SEQ=? WHERE SW_TYPE=? AND SW_YEAR=? ";
                    par4.add((Long.parseLong(curNo) + 1));
                    par4.add(busType);
                    par4.add(ca.get(Calendar.YEAR));
                    factory.update(sqlUpdate, par4);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String dateStr = sdf.format(new Date());
                String zoneStr = "";
                if (curNo.length() < 6) {
                    for (int i = 0; i < 6 - curNo.length(); i++) {
                        zoneStr += "0";
                    }
                }
                if (busType.equals(Constant.NOCRT_BO_ORDER_NO)) {
                    DNO = "BO" + dateStr + zoneStr + curNo;
                } else if (busType.equals(Constant.NOCRT_ORDER_NO)) {// 订单号
                    DNO = "DD" + dateStr + zoneStr + curNo;
                } else if (busType.equals(Constant.NOCRT_BOARD_NO)) {// 组板号
                    DNO = "ZB" + dateStr + zoneStr + curNo;
                } else if (busType.equals(Constant.NOCRT_BOARD_MY_NO)) {// 组板号（自提单）
                    DNO = "ZT" + dateStr + zoneStr + curNo;
                } else if (busType.equals(Constant.NOCRT_PRO_NO)) {// 承诺单号
                    DNO = "CL" + dateStr + zoneStr + curNo;
                } else if (busType.equals(Constant.NOCRT_OUTSTORE_NO)) {// 出库单号
                    DNO = "CK" + dateStr + zoneStr + curNo;
                } else if (busType.equals(Constant.NOCRT_CUSORDER_NO)) {// 订做车订单
                    DNO = "DZ" + dateStr + zoneStr + curNo;
                } else if (busType.equals(Constant.NOCRT_PLAN_NO)) {//生产计划
                    DNO = "JH" + dateStr + zoneStr + curNo;
                } else if (busType.equals(Constant.NOCRT_TOTAL_PRO_NO)) {//生产订单
                    DNO = dateStr + zoneStr + curNo;
                } else if (busType.equals(Constant.NOCRT_SEND_ORDER_NO)) {//发运单号
                    DNO = "FY" + dateStr + zoneStr + curNo;
                } else if (busType.equals(Constant.NOCRT_DKH_NO)) {//大客户单号
                    DNO = dateStr + zoneStr + curNo;
                } else if (busType.equals(Constant.NOCRT_ASS_NO)) {//分派号
                    DNO = "FP" + dateStr + zoneStr + curNo;
                } else if (busType.equals(Constant.FIN_NO)) {//经销商现金打款流水号
                    SimpleDateFormat sdfLs = new SimpleDateFormat("yyyyMM");
                    String ym = sdfLs.format(new Date());
                    DNO = ym + zoneStr + curNo;
                }else if (busType.equals(Constant.NOCRT_DB_ORDER_NO)) {//调拨单号
                    DNO = "DB" + dateStr + zoneStr + curNo;
               }else if (busType.equals(Constant.NOCRT_JS_APPLY_NO)) {//结算申请单号
                    DNO = "JS" + dateStr + zoneStr + curNo;
               } else if (busType.equals(Constant.NOCRT_SWZC_NO)) {//发运单号
                    DNO = "ZC" + dateStr + zoneStr + curNo;
                } else {
                    throw new RuntimeException("传入参数错误,请检查.");
                }
            }

        } catch (Exception e) {
            throw e;
        }
        return DNO;
    }

    public static TmBusinessAreaPO getName(String id) {
        TmBusinessAreaPO p = new TmBusinessAreaPO();
        p.setAreaId(Long.valueOf(id));
        List<TmBusinessAreaPO> list = factory.select(p);
        if (list.size() > 0 && list != null) {
            p = list.get(0);
        } else {
            p = null;
        }
        return p;
    }

    /**
     * 配件可变参数查询
     *
     * @param type
     * @return
     * @throws Exception
     */
    public static List<TtPartFixcodeDefinePO> getPartUnitList(Integer type) throws Exception {
        try {
            TtPartFixcodeDefinePO po = new TtPartFixcodeDefinePO();
            po.setFixGouptype(type);
            po.setState(Constant.STATUS_ENABLE);
            List list = factory.select(po);
            return list;
        } catch (Exception e) {
            throw e;
        }
    }
    /**************************** end ***************************/

    /**
     * 获取库位码
     */

    public static String getSitCode(List<Object> params) throws Exception {
        String DNO = "";
        if (params.size() > 0) {
            for (int i = 0; i < params.size(); i++) {
                DNO += params.get(i) + "-";
            }
            DNO = formatSitCode(params.get(0)) + "-" + formatSitCode(params.get(1)) + "-" + formatSitCode(params.get(2));
        }
        if (DNO != "") {
            DNO.substring(0, DNO.length() - 1);
        }
        return DNO;
    }

    public static String formatSitCode(Object obj) {
        String code = obj.toString();
        if (obj != null) {
            if (code.length() < 2) {
                code = "0" + obj;
            }
        }
        return code;
    }

    //字符串拼接
    public static String getStrToArr(String[] arr, String reg) {
        String ret = "";
        if (arr != null && arr.length > 0) {
            for (int i = 0; i < arr.length; i++) {
                ret += arr[i] + reg;
            }
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret;
    }

    /**
     * 获取当前时区当前时间
     *
     * @param date
     * @return
     */
    public static Date getTZDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = null;
        try {
            date = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取指定日期后或前日期
     *
     * @param date
     * @return
     */
    public static Date getTZDate(Date date, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date2 = null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, c.get(Calendar.DATE) + day);
        try {
            date2 = sdf.parse(sdf.format(c.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date2;
    }

    /**
     * 获取当月第一天
     *
     * @return
     */
    public static String getMonthLastDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);
        c.roll(Calendar.DATE, -1);
        Date endTime = c.getTime();
        //String eTime = sdf.format(endTime) + " 23:59:59";
        return sdf.format(endTime);

    }

    /**
     * 获取当月最后一天
     *
     * @return
     */
    public static String getMonthFirstDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date beginTime = c.getTime();
        //String sTime = c.format(beginTime) + " 00:00:00";
        return sdf.format(beginTime);
    }

    /**
     * 获取前几个月第一天
     *
     * @param pM
     * @return
     */
    public static String getPreviousXMonthFirst(int m) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, m);// 减一个月，变为下月的1号
        // lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天

        return sdf.format(lastDate.getTime());
    }

    /**
     * 获取当前时间
     *
     * @param date
     * @return
     */
    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(new Date());
    }

    /**
     * 获取上月同一天日期
     *
     * @param date
     * @return
     */
    public static String getBefore(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        long millis = getBefore(calendar);
        calendar.setTimeInMillis(millis);

        return sdf.format(calendar.getTime());
    }

    /**
     * @param c
     * @return
     */
    private static long getBefore(Calendar c) {
        int month = (c.get(Calendar.MONTH) + 1);
        int day = c.get(Calendar.DATE);
        long monthMillis = 0;

        switch (month) {
            // 其它月份都减去31天
            // 10月31对应9月30
            // 11月30对应10对30
            case 1:
            case 2:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 11:
            case 10:
            case 12:
                monthMillis = calculateMillis(31);
                break;
            default:
                int febDay = 28;
                if (isLeapYear(c)) {
                    febDay = 29;
                }
                // 大于2月的天数，则减去当前天数
                if (day > febDay) {
                    monthMillis = calculateMillis(day);
                } else {
                    // 否则减去2月的天数
                    monthMillis = calculateMillis(febDay);
                }
                break;
        }
        System.out.println(monthMillis);
        return (c.getTimeInMillis() - monthMillis);
    }

    private static long calculateMillis(int month) {
        return month * (long) 24 * 60 * 60 * 1000;
    }

    private static boolean isLeapYear(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        return (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0));
    }

    public static String getStr4SpLen(String str, int len) {
        int strLength = str.length();
        String resultStr = "";
        if ("".equals(str)) {
            resultStr = "无";
        } else {
            if (strLength >= len) {
                resultStr = str.substring(0, len - 1);
            } else {
                resultStr = str;
            }
        }

        return resultStr;
    }

    /**
     * 判断车架号不能为空
     *
     * @param vin
     */
    public static void jugeVinNull(String vin) throws Exception {
        if (null == vin || vin.trim().equals("") || vin.trim().equalsIgnoreCase("null") || vin.trim().length() < 17) {
            throw new Exception("车架号不能为空!");
        }
    }

    /**
     * 查询职位下面的服务商信息
     *
     * @param mark      服务商ID
     * @param logonUser 职位ID
     * @return
     */
    public static String getOrgDealerLimitSqlByPose(String mark, AclUserBean logonUser) {
        StringBuffer sql = new StringBuffer();
        sql.append("AND EXISTS (SELECT 1\n");
        sql.append("         FROM TR_POSE_REGION_DLR_SERVICE DS\n");
        sql.append("        WHERE 1=1 \n");
        if (!"".equals(mark) && null != mark) {
            sql.append("      AND DS.DEALER_ID = " + mark + ".DEALER_ID\n");
        }
        sql.append("          AND DS.POSE_ID = " + logonUser.getPoseId() + ")\n");
        return sql.toString();
    }

    /**
     * 默认值
     *
     * @param string1
     * @param String2
     * @return
     */
    public static String checkNull4Default(String string1, String String2) {
        if (string1 != null)
            return string1;
        else if (String2 != null) {
            return String2;
        } else
            return "";
    }

    /**
     * 通过 CODEID 从内存中获取详细信息
     *
     * @param codeId
     * @return
     */
 /*   public static String getMemCodeDesc(String codeId) {
        if (CodeDict.getDictMap2().get(codeId) != null) {
            return CodeDict.dictMap2.get(codeId);
        } else {
            return "";
        }
    }*/

    /**
     * 通过 CODEID 取得详细信息
     *
     * @param codeId
     * @return
     */
    public static String getCodeDesc(String codeId) {
        TcCodePO po = new TcCodePO();
        po.setCodeId(codeId.trim());
        List<TcCodePO> list = factory.select(po);
        if (list.size() > 0) {
            return (list.get(0)).getCodeDesc();
        } else {
            return "";
        }
    }

    /**
     * @param strDate 指定日期
     * @param day     增加天数
     * @return
     * @throws Exception
     */
    public static String getNextDay(String strDate, int day) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dd = format.parse(strDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dd);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return format.format(calendar.getTime());
    }

    public static String[] array_unique(String[] a) {
        // array_unique  
        List<String> list = new LinkedList<String>();
        for (int i = 0; i < a.length; i++) {
            if (!list.contains(a[i])) {
                list.add(a[i]);
            }
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * 配件分网品种过滤SQL
     *
     * @param dealerId
     * @param preFix
     * @return
     */
    public static String bindPartSQL(String dealerId, String preFix) {
        StringBuffer sql = new StringBuffer();
        sql.append(" \n");
        if ("1".equals(CommonDAO.getPara(60041002 + ""))) {
            sql.append("  AND EXISTS\n");
            sql.append("(SELECT 1\n");
            sql.append("         FROM TT_PART_DEALER_CARTYPE DC, TT_PART_BRAND_CARTYPE BD\n");
            sql.append("        WHERE DC.CAR_TYPE = BD.CAR_TYPE\n");
            sql.append("          AND BD.PART_ID = " + preFix + ".PART_ID\n");
            sql.append("          AND DC.DEALER_ID = '" + dealerId + "')\n");
        }
        return sql.toString();
    }

    /**
     * 获取当前时间
     *
     * @param date
     * @return
     */
    public static String getFullDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(new Date());
    }


    /**
     * List to Map
     *
     * @param list
     * @return
     */
    public static Map<String, Object> listMap2Map(List<Map<String, Object>> list) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> tmp = list.get(i);
            Set set = tmp.entrySet();
            ArrayList arr = new ArrayList();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                arr.add(entry.getKey());
            }
            map.put(tmp.get(arr.toArray()[0]).toString(), tmp.get(arr.toArray()[1]));
        }

        return map;
    }
    
    /**
     * 生成32位UUID
     * @return
     * @author chenyub@yonyou.com
     */
    public synchronized static String getUUID(){
    	return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    public static String getSplitStringForIn(String codes){
    	String str="";
    	str="'"+codes.replaceAll(",", "','")+"'";
    	return str;
    }
    
    /**
	 * 判断是否为销售经理登陆
	 * true 表示是，false表示否
	 * @param city
	 * @return
	 */
	public static boolean judgeMgrLogin(String userId) {
		boolean flag= false;
		BaseDao dao=new  UserManageDao();
		TcUserPO tc=new TcUserPO();
		tc.setUserId(Long.parseLong(userId));
		tc=(TcUserPO) dao.select(tc).get(0);
		if(tc.getPoseRank().equals(Constant.DEALER_USER_LEVEL_02) || tc.getPoseRank()==Constant.DEALER_USER_LEVEL_02.longValue()){
			flag=true;
		}
		return flag;
	}

	/**
     * 增加接触点信息
     * pointWay:接触类型;pointContant:接触内容;customerId:客户Id;adviser;经销商ID:dealerId
     * @return
     */
	public static void addContackPoint(Integer pointWay,String pointContant,String customerId,String adviser,String dealerId) {
		try {
			String pointId = SequenceManager.getSequence("");
			BaseDao dao=new  UserManageDao();
			TPcContactPointPO pointPo = new TPcContactPointPO();
			pointPo.setPointId(Long.parseLong(pointId));
			pointPo.setPointDate(new Date());
			pointPo.setPointWay(pointWay.longValue());
			pointPo.setPointContent(pointContant);
			pointPo.setCreateDate(new Date());
			pointPo.setStatus(Constant.STATUS_ENABLE.longValue());
			pointPo.setCtmId(Long.parseLong(customerId));
			pointPo.setAdviser(adviser);
			pointPo.setDealerId(dealerId);
			dao.insert(pointPo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     * 新增提醒信息
     * remindType:提醒类型;beRemindId:被提醒ID;customerId:客户ID;dealerId:分派经销商;adviser:分派顾问;remindDate:提醒时间;remindNum:提醒数量
     * @return
     */
	public static void addRemindInfo(String remindType,String beRemindId,String customerId,String dealerId,String adviser,String remindDate,String remindNum) {
		try {
			BaseDao dao=new  UserManageDao();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String newRemindId = SequenceManager.getSequence("");
			TPcRemindPO remindPo = new TPcRemindPO();
			remindPo.setRemindId(Long.parseLong(newRemindId));
			if(customerId!=null && !"".equals(customerId)) {
				remindPo.setCustomerId(Long.parseLong(customerId));
			}
			if(dealerId!=null && !"".equals(dealerId)) {
				remindPo.setDealerId(dealerId);
			}
			if(adviser!=null && !"".equals(adviser)) {
				remindPo.setAdviser(adviser);
			}
			if(remindNum!=null && !"".equals(remindNum)) {
				remindPo.setRemindNum(Integer.parseInt(remindNum));
			}
			remindPo.setRemindType(remindType);
			remindPo.setRemindStatus(Constant.TASK_STATUS_01);
			remindPo.setRemindDate(sdf.parse(remindDate));
			remindPo.setBeremindId(Long.parseLong(beRemindId));
			remindPo.setCreateDate(new Date());
			dao.insert(remindPo);
			//手机推送消息
			//获取提醒类型名称
			TcCodePO tc=new TcCodePO();
			tc.setCodeId(remindType);
			tc=(TcCodePO) dao.select(tc).get(0);
			String remindTypeName =tc.getCodeDesc();
			
			//HashMap hm = new HashMap();
        	//hm.put("strpushtype", "100");
        	//hm.put("strkey", "100");
        	//SendPushService.sendNotification("您新增了一条"+remindTypeName+",待处理!",adviser,hm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//修改分配表是否重复字段
	public static void updateIfRepeat(String allot_id){
			TPcLeadsAllotPO tpla=new TPcLeadsAllotPO();
			tpla.setLeadsAllotId(new Long(allot_id));
			TPcLeadsAllotPO tpla1=new TPcLeadsAllotPO();
			tpla1.setIsRepeat(new Long(Constant.IF_TYPE_YES));
			BaseDao dao=new  UserManageDao();
			dao.update(tpla, tpla1);
			
		
	}
	
	//修改线索的状态为重复线索
	public static void updateLeadStatus(String leads_code){
		BaseDao dao=new  UserManageDao();
		TPcLeadsPO tp =new TPcLeadsPO();
		tp.setLeadsCode(new Long(leads_code));
		TPcLeadsPO tp1 =new TPcLeadsPO();
		tp1.setLeadsStatus(new Long(Constant.LEADS_STATUS_05));
		dao.update(tp, tp1);
	}
	
	/**
     *为对象产生大客户代码
     * @param po
     * @return
     */
    public static void getFleetCode(TmFleetPO po){
    	try {
    		//生成大客户代码
    		String fleet_code="S";
    		Calendar c=Calendar.getInstance();
    		int day=c.get(Calendar.DAY_OF_MONTH);
    		int year=c.get(Calendar.YEAR);
    		int month=c.get(Calendar.MONTH);
    		String dayString="";
    		String monthString="";
    		if(day<9){
    			dayString="0"+day;
    		}else{
    			dayString=day+"";
    		}
    		if((month+1)<10){
    			monthString="0"+(month+1);
    		}else{
    			monthString=""+(month+1);
    		}
    		String currentDate=""+year+monthString+dayString;
    		fleet_code+=currentDate;
    		String current_date=year+"-"+monthString+"-"+dayString;
    		FleetInfoAppDao fdao = new FleetInfoAppDao();
    		int counts=fdao.getDealerFleetCount(po.getDlrCompanyId().toString(), current_date);
    		String flow_number="";
    		if(counts<10){
    			flow_number="0"+(counts+1);
    		}else{
    			flow_number=(counts+1)+"";
    		}
    		fleet_code+=flow_number;
    		String dealer_code="";
    		UserManageDao udao=new UserManageDao();
    		TmDealerPO tdpo=new TmDealerPO();
    		tdpo.setCompanyId(po.getDlrCompanyId());
    		tdpo=(TmDealerPO) udao.select(tdpo).get(0);
    		 dealer_code=tdpo.getDealerCode();
    		 fleet_code+=dealer_code;
    		String lastChar="";
    		if(Constant.FLEET_TYPE_14.equals(po.getFleetType().toString())){
    			lastChar+="A";
    		}else if(Constant.FLEET_TYPE_15.equals(po.getFleetType().toString())){
    			lastChar+="D";
    		}else if(Constant.FLEET_TYPE_16.equals(po.getFleetType().toString())){
    			lastChar+="E";
    		}else if(Constant.FLEET_TYPE_17.equals(po.getFleetType().toString())){
    			lastChar+="F";
    		}else if(Constant.FLEET_TYPE_18.equals(po.getFleetType().toString())){
    			lastChar+="C";
    		}else if(Constant.FLEET_TYPE_19.equals(po.getFleetType().toString())){
    			lastChar+="B";
    		}
    		fleet_code+=lastChar;
    		TmFleetPO po1=new TmFleetPO();
    		po1.setFleetId(po.getFleetId());
    		TmFleetPO po2=new TmFleetPO();
    		po2.setFleetCode(fleet_code);
    		fdao.update(po1, po2);
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 通过经销商代码获取company
     * @param dealerCode
     * @return
     */
    public static TmCompanyPO getCompanyInfo(String dealerCode){
    	TmCompanyPO tc=new TmCompanyPO();
    	TmDealerPO td=new TmDealerPO();
    	td.setDealerCode(dealerCode);
    	UserManageDao udao=new UserManageDao();
    	td=(TmDealerPO) udao.select(td).get(0);
    	tc.setCompanyId(td.getCompanyId());
    	tc=(TmCompanyPO) udao.select(tc).get(0);
    	return tc;
    	
    }
    
    /**
     * 查询经销商合同是否过期 如果过期了返回0 如果没有到期返回1
     */
    public static Map<String,Object> getIsConstracExpire(String dealer_code){
    	Map<String,Object> m=null;
    	if(dealer_code==null||"".equals(dealer_code)){
    		m=new HashMap<String,Object>();
    		m.put("CONTRACT_DATE", "0000-00-00");
    		m.put("COUNT", 0);
    		return m;
    	}
    	try {
    		BaseDao<PO> dao=new UserManageDao();
        	StringBuilder sql= new StringBuilder();
        	//获取是否到期
        	sql.append("SELECT COUNT(1) COUNT\n" );
        	sql.append("  FROM CUX_CALM_CONTRACT_V T\n" );
        	sql.append(" WHERE T.MANTURITY_DATE >= SYSDATE-1\n" );
        	sql.append("   AND ORG_CODE = '"+dealer_code+"'");
        	m=dao.pageQueryMap(sql.toString(), null, "com.infodms.dms.util.commonUtils.getIsConstracExpire");
        	int count=Integer.parseInt(m.get("COUNT").toString());
        	//查询到期日
        	StringBuilder sql1= new StringBuilder();
        	sql1.append("SELECT T.MANTURITY_DATE CONTRACT_DATE\n" );
        	sql1.append("  FROM CUX_CALM_CONTRACT_V T\n" );
        	sql1.append(" WHERE \n" );
        	sql1.append("    ORG_CODE = '"+dealer_code+"'");
        	Map<String,Object> m1=dao.pageQueryMap(sql1.toString(), null, "com.infodms.dms.util.commonUtils.getIsConstracExpire");
        	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
         	String contractDate="";
        	//有数据并且没有到期
        	if(m1!=null&&m1.get("CONTRACT_DATE")!=null&&count>0){
             	contractDate=sdf.format(sdf.parse(m1.get("CONTRACT_DATE").toString()));
             	m.put("CONTRACT_DATE", contractDate);
             	m.put("COUNT", 1);
             //有数据但是已经到期
        	}else if(m1!=null&&m1.get("CONTRACT_DATE")!=null&&count==0){
        		m=new HashMap<String,Object>();
        		contractDate=sdf.format(sdf.parse(m1.get("CONTRACT_DATE").toString()));
             	m.put("CONTRACT_DATE", contractDate);
            	m.put("COUNT", 0);
        	}else{
        		m.put("CONTRACT_DATE", "    ");
        		m.put("COUNT", 0);
        	}
        	return m;
		} catch (Exception e) {
			m=new HashMap<String,Object>();
			m.put("CONTRACT_DATE", "1111-11-11");
			return m;
		}
    }
    
    /**
	 * 获取账户数量（根据dealerid和typecode）
	 * @param fundTypeId
	 * @param dealerId
	 * 查询不到0 异常返回-1
	 * @return
	 */
	public static  Map<String,Object> getAmounts(String dealerId, String type_code) throws Exception{
		Map<String,Object> m=new HashMap<String ,Object>();
		BaseDao<PO> dao=new UserManageDao();
		StringBuffer sql = null;
		TmDealerPO tdpo=new TmDealerPO();
		tdpo.setDealerId(dealerId==null?null:new Long(dealerId));
		tdpo=(TmDealerPO) dao.select(tdpo).get(0);
		String dealer_code =tdpo.getDealerCode();
		try {
			 sql= new StringBuffer();
			sql.append("SELECT COUNT(1) COUNTS\n" );
			sql.append("  FROM CUX_DMS_ACOUNT_V@DMS2EBS2 CDAV\n" );
			sql.append(" WHERE CDAV.DEALER_CODE = '"+dealer_code+"'\n" );
			sql.append("   AND ACCOUNT_CODE = '"+type_code+"'");
			m = dao.pageQueryMap(sql.toString(), null, " com.infodms.dms.util.getAmounts");
		} catch (Exception e) {
			m.put("COUNTS", -1);
		}
		return m;
	}
	
	/**
	 * @param category 物料编码 party_number 经销商代码
	 * @return 整车销售价格
	 */
	public static TmMaterialPricePO getMaterialPirce(String material_code,String party_number) throws Exception{
			if(material_code==null||party_number==null){
				return null;
			}
			if("".equals(material_code)||"".equals(party_number)){
				return null;
			}
			UserManageDao udao=new UserManageDao();
			TmMaterialPricePO tmpp=new TmMaterialPricePO();
			//根据material_code 获取价格
			StringBuilder sql0= new StringBuilder();
			sql0.append("SELECT LIST_HEADER_ID,\n" );
			sql0.append("       CATEGORIES,\n" );
			sql0.append("       OPERAND,\n" );
			sql0.append("       PARTY_NUMBER,\n" );
			sql0.append("       PARTY_NAME\n" );
			sql0.append("  FROM tm_material_price T\n" );
			sql0.append(" WHERE CATEGORIES = '"+material_code+"'\n" );
			sql0.append("   AND PARTY_NUMBER = '"+party_number+"'");
			Map<String,Object> priceMap0=udao.pageQueryMap(sql0.toString(), null, udao.getFunName());
			if(priceMap0!=null){
				tmpp.setCategories(priceMap0.get("CATEGORIES").toString());
				tmpp.setListHeaderId(new BigDecimal(priceMap0.get("LIST_HEADER_ID").toString()));
				tmpp.setPartyName(priceMap0.get("PARTY_NAME").toString());
				tmpp.setOperand(new BigDecimal(priceMap0.get("OPERAND").toString()));
				tmpp.setPartyNumber(new BigDecimal(priceMap0.get("PARTY_NUMBER").toString()));
				return tmpp;
			}
			//end
			
			String category="";
			category=CommonUtils.getGroupCodeByMaterialCode(material_code);
			
			//查询条件
		
			StringBuilder sql= new StringBuilder();
			sql.append("SELECT LIST_HEADER_ID,\n" );
			sql.append("       CATEGORIES,\n" );
			sql.append("       OPERAND,\n" );
			sql.append("       PARTY_NUMBER,\n" );
			sql.append("       PARTY_NAME\n" );
			sql.append("  FROM tm_material_price T\n" );
			sql.append(" WHERE CATEGORIES = '"+category+"'\n" );
			sql.append("   AND PARTY_NUMBER = '"+party_number+"'");
			Map<String,Object> priceMap=udao.pageQueryMap(sql.toString(), null, udao.getFunName());
			if(priceMap!=null){
				tmpp.setCategories(priceMap.get("CATEGORIES").toString());
				tmpp.setListHeaderId(new BigDecimal(priceMap.get("LIST_HEADER_ID").toString()));
				tmpp.setPartyName(priceMap.get("PARTY_NAME").toString());
				tmpp.setOperand(new BigDecimal(priceMap.get("OPERAND").toString()));
				tmpp.setPartyNumber(new BigDecimal(priceMap.get("PARTY_NUMBER").toString()));
			}
			//如果价格有值得话取值，没有返回空
			if(tmpp.getOperand()!=null){
				return tmpp;
			}else{
				return null;
			}
	}
	
	public static String getGroupCodeByMaterialCode(String material_code){
		String group_code="";
		BaseDao dao=new  UserManageDao();
		TmVhclMaterialPO tvmp=new TmVhclMaterialPO();
		tvmp.setMaterialCode(material_code);
		tvmp=(TmVhclMaterialPO) dao.select(tvmp).get(0);
		TmVhclMaterialGroupRPO tmgr=new TmVhclMaterialGroupRPO();
		tmgr.setMaterialId(tvmp.getMaterialId());
		tmgr=(TmVhclMaterialGroupRPO) dao.select(tmgr).get(0);
		TmVhclMaterialGroupPO tvmg=new TmVhclMaterialGroupPO();
		tvmg.setGroupId(tmgr.getGroupId());
		tvmg=(TmVhclMaterialGroupPO) dao.select(tvmg).get(0);
		group_code=tvmg.getGroupCode();
		return group_code;
		
	}
	
	/**
	 * @param category 物料编码 party_number 经销商代码
	 * @return 销售价格列表
	 */
	public static PageResult<Map<String, Object>> getMaterialPirceList(String category,String party_number,int pageSize,int curPage) throws Exception{
		PageResult<Map<String, Object>> priceList=null;
		//查询条件
		UserManageDao udao=new UserManageDao();
		try {
			StringBuilder sql= new StringBuilder();
			sql.append("SELECT T.LIST_HEADER_ID,\n" );
			sql.append("       T.CATEGORIES,\n" );
			sql.append("       T.OPERAND,\n" );
			sql.append("       T.PARTY_NUMBER,\n" );
			sql.append("       T.PARTY_NAME,\n" );
			sql.append("       TVMG.GROUP_NAME\n" );
			sql.append("  FROM CUX_DMS_MATERIAL_PRICE_V@DMS2EBS2 T,TM_VHCL_MATERIAL_GROUP TVMG \n" );
			sql.append("  WHERE TVMG.group_code=T.CATEGORIES\n" );
			if(null!=category&&!"".equals(category)){
				sql.append(" AND  CATEGORIES = '"+category+"'\n" );
			}
			if(null!=party_number&&!"".equals(party_number)){
				sql.append("   AND PARTY_NUMBER = '"+party_number+"'");
			}
			priceList=udao.pageQuery(sql.toString(), null, udao.getFunName(),pageSize,curPage);
			
		} catch (Exception e) {
			//如果查询erp的数据异常就调用本地的数据
			try {
				StringBuilder sql= new StringBuilder();
				sql.append("SELECT T.LIST_HEADER_ID,\n" );
				sql.append("       T.CATEGORIES,\n" );
				sql.append("       T.OPERAND,\n" );
				sql.append("       T.PARTY_NUMBER,\n" );
				sql.append("       T.PARTY_NAME,\n" );
				sql.append("       TVMG.GROUP_NAME\n" );
				sql.append("  FROM TM_MATERIAL_PRICE T,TM_VHCL_MATERIAL_GROUP TVMG\n" );
				sql.append("  WHERE TVMG.group_code=T.CATEGORIES\n" );
				if(null!=category&&!"".equals(category)){
					sql.append(" AND  T.CATEGORIES = '"+category+"'\n" );
				}
				if(null!=party_number&&!"".equals(party_number)){
					sql.append("   AND T.PARTY_NUMBER = "+party_number+"'");
				}
				
				priceList=udao.pageQuery(sql.toString(), null, udao.getFunName(),pageSize,curPage);
			} catch (Exception e1) {
				throw new BizException("整车销售价格查询异常");
			}
		}
		return priceList;
	}
	
	/**
	 * 判断单子的状态
	 * @param reqId
	 * @param status
	 * @return
	 */
	public static boolean judgeReqBillStatus(String reqId,int status) {
		BaseDao dao=new  UserManageDao();
		TtVsDlvryReqPO tvdrp=new TtVsDlvryReqPO();
		tvdrp.setReqId(new Long(reqId));
		 tvdrp= (TtVsDlvryReqPO) dao.select(tvdrp).get(0);
		if(!(tvdrp.getReqStatus().intValue()==status)){
			return true;
		}
		return false;
	}
	
	public static List<Map<String,Object>> getNeedRemarkAcntId(){
		List<Map<String,Object>> list=null;
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TVAT.TYPE_ID\n" );
		sql.append("  FROM TT_VS_ACCOUNT_TYPE TVAT\n" );
		sql.append(" WHERE TVAT.TYPE_CODE NOT IN ('2001')\n" );
		sql.append("   AND TVAT.STATUS = 10011001");
		FleetInfoAppDao dao = new FleetInfoAppDao();
		list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * 获取当前用户所有的功能
	 * @param reqId
	 * @param status
	 * @return
	 */
	public static String judgeUserHasFunc(AclUserBean logonUser) {
		String funcStr="";
		BaseDao dao=new  UserManageDao();
		//获取pose_id
		Long poseId=	logonUser.getPoseId();
		TrRolePosePO trp=new TrRolePosePO();
		trp.setPoseId(poseId);
		trp=(TrRolePosePO) dao.select(trp).get(0);
		TrRoleFuncPO trf=new TrRoleFuncPO();
		trf.setRoleId(trp.getRoleId());
		trf.setOpType(1);
		List<PO> list=dao.select(trf);
		for(int k=0;k<list.size();k++){
			trf=(TrRoleFuncPO) list.get(k);
			funcStr+=trf.getFuncId()+",";
		}
		return funcStr;
	}
	
	//根据经销商代码获取经销商id
	public static String getDealerCode(String dealerId){
		String dealerCode=null;
		BaseDao dao=new  UserManageDao();
		TmDealerPO td=new TmDealerPO();
		td.setDealerId(new Long(dealerId));
		int size=dao.select(td).size();
		if(size>0){
			td=(TmDealerPO) dao.select(td).get(0);
			dealerCode=td.getDealerCode();
		}
		return dealerCode;
	}
	
	//根据经销商代码获取经销商id
	public static Long getDealerId(String dealer_code){
		BaseDao dao=new  UserManageDao();
		Long dealerId=null;
		TmDealerPO td=new TmDealerPO();
		td.setDealerCode(dealer_code);
		int size=dao.select(td).size();
		if(size>0){
			td=(TmDealerPO) dao.select(td).get(0);
			dealerId=td.getDealerId();
		}
		return dealerId;
	}
	
	/**
	 * 获取账户余额
	 * @param fundTypeId
	 * @param dealerId
	 * 查询不到返回空
	 * @return
	 */
	public static  Map<String,Object> getAvailableAmount(String fundTypeId, String dealerId) throws Exception{
		Map<String,Object> m=new HashMap<String ,Object>();
		BaseDao<PO> dao=new UserManageDao();
		StringBuffer sql = null;
		TmDealerPO tdpo=new TmDealerPO();
		tdpo.setDealerId(dealerId==null?null:new Long(dealerId));
		tdpo=(TmDealerPO) dao.select(tdpo).get(0);
		String dealer_code =tdpo.getDealerCode();
		try {
			sql = new StringBuffer();
			sql.append("SELECT TAA.ACCOUNT_ID ACCOUNT_ID, TAA.BALANCE_AMOUNT AVAILABLE_AMOUNT\n");
			sql.append("  FROM TT_VS_ACCOUNT_TYPE  TSA,TT_VS_ACCOUNT TAA\n");
			sql.append("  WHERE TSA.TYPE_ID = TAA.ACCOUNT_TYPE_ID ");
			sql.append("  and  TSA.STATUS=10011001 ");
			sql.append("  AND TAA.DEALER_ID = '" + dealerId + "'\n");
			if(fundTypeId!=null&&!"".equals(fundTypeId)){
				sql.append("   AND TSA.TYPE_ID = " + fundTypeId);
			}
			
			m = dao.pageQueryMap(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getAvailableAmount");
			
		} catch (Exception e) {
			m=null;
//			sql = new StringBuffer();
//			sql.append("SELECT TSA.TYPE_ID ACCOUNT_ID, TAA.BALANCE_AMOUNT AVAILABLE_AMOUNT\n");
//			sql.append("  FROM TT_VS_ACCOUNT_TYPE TSA,TM_ACCOUNT_AMOUNT TAA\n");
//			sql.append("  WHERE TSA.TYPE_CODE = TAA.ACCOUNT_CODE ");
//			sql.append("  AND TAA.DEALER_CODE = '" + dealer_code + "'\n");
//			sql.append("   AND TSA.TYPE_ID = " + fundTypeId);
//			m = dao.pageQueryMap(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getAvailableAmount");
		}
		return m;
	}
	
	/**
     * 根据经销商id生成webBlog单号
     * @param dealerId
     */
    public static String getWebNo(String dealerId) throws Exception{
    	String str="WB";
    	Calendar c=Calendar.getInstance();
		int day=c.get(Calendar.DAY_OF_MONTH);
		int year=c.get(Calendar.YEAR);
		int month=c.get(Calendar.MONTH);
		String dayString="";
		String monthString="";
		if(day<9){
			dayString="0"+day;
		}else{
			dayString=day+"";
		}
		if((month+1)<10){
			monthString="0"+(month+1);
		}else{
			monthString=""+(month+1);
		}
		UserManageDao dao = UserManageDao.getInstance();
		TmDealerPO tdPO=new TmDealerPO();
		tdPO.setDealerId(new Long(dealerId));
		tdPO=(TmDealerPO) dao.select(tdPO).get(0);
		str+=tdPO.getDealerCode();
		String currentDate=""+year+monthString+dayString;
		str+=currentDate;
		String current_date=year+"-"+monthString+"-"+dayString;
		int counts=getDealerDayWebCount(dealerId, current_date);
		String flow_number="";
		if(counts<10){
			flow_number="0"+(counts+1);
		}else{
			flow_number=(counts+1)+"";
		}
		str+=flow_number;
		return str;
    	
    }
    
    /**
	 * 获取当天经销商审核的大客户数
	 * @return
	 * @throws Exception
	 */
	public static int  getDealerDayWebCount(String dealer_id,String current_date) throws Exception{
		int i=0;
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT COUNT(*) COUNTS \n" );
		sql.append("  FROM TT_VS_BLOG TF\n" );
		sql.append(" WHERE TF.DEALER_ID = "+dealer_id+"\n" );
		sql.append(" AND TF.BLOG_NO IS NOT NULL\n");
		sql.append("   AND ((TF.CREATE_DATE < SYSDATE\n" );
		sql.append("   AND TF.CREATE_DATE >\n" );
		sql.append("       TO_DATE('"+current_date+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss'))");
		sql.append("   OR( TF.CREATE_DATE >\n" );
		sql.append("       TO_DATE('"+current_date+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')))");
		FleetInfoAppDao dao = new FleetInfoAppDao();
		Map<String,Object> map=dao.pageQueryMap(sql.toString(), null, dao.getFunName());
		i=Integer.parseInt(map.get("COUNTS").toString());
		return i;
	}
    
    public static List<Map<String,Object>> seachDealerInfo(Long deaId){
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT DISTINCT TD.DEALER_ID,\n" );
    	sql.append("                TD.DEALER_LEVEL,\n" );
    	sql.append("                TD.DEALER_SHORTNAME,\n" );
    	sql.append("                TD.ADDRESS,\n" );
    	sql.append("                TD.ZIP_CODE,\n" );
    	sql.append("                TD.LINK_MAN,\n" );
    	sql.append("                TD.PHONE,\n" );
    	sql.append("                TD.DEALER_CODE\n" );
    	sql.append("  FROM TM_DEALER TD, TC_USER TU\n" );
    	sql.append(" WHERE 1 = 1\n" );
    	sql.append("   AND (TD.PARENT_DEALER_D =\n" );
    	sql.append("       (SELECT TD.PARENT_DEALER_D\n" );
    	sql.append("           FROM TM_DEALER TD\n" );
    	sql.append("          WHERE TD.DEALER_ID = "+deaId+") OR\n" );
    	sql.append("       TD.DEALER_ID =\n" );
    	sql.append("       (SELECT TD.PARENT_DEALER_D\n" );
    	sql.append("           FROM TM_DEALER TD\n" );
    	sql.append("          WHERE TD.DEALER_ID = "+deaId+"))\n" );
    	sql.append("   AND TD.COMPANY_ID = TU.COMPANY_ID\n" );
    	sql.append("   AND TD.DEALER_ID != "+deaId+"\n" );
    	sql.append(" ORDER BY DEALER_LEVEL");

		BaseDao<PO> dao=new UserManageDao();
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
    
    /**
     * 获取经销商的省系的组织id
     * @param dealerId
     * @return
     */
    public static String getDealerOrgId(String dealerId){
    	StringBuilder sql= new StringBuilder();
    	sql.append("SELECT TR.ORG_ID ORG_ID\n" );
    	sql.append("          FROM TM_DEALER TD, TM_ORG TR, TM_DEALER_ORG_RELATION TDOR\n" );
    	sql.append("         WHERE TD.DEALER_ID = TDOR.DEALER_ID\n" );
    	sql.append("           AND TDOR.ORG_ID = TR.ORG_ID\n" );
    	sql.append("           AND TD.DEALER_ID =\n" );
    	sql.append("               DECODE((SELECT TDS.DEALER_LEVEL\n" );
    	sql.append("                        FROM TM_DEALER TDS\n" );
    	sql.append("                       WHERE TDS.DEALER_ID = "+dealerId+"),\n" );
    	sql.append("                      10851001,\n" );
    	sql.append("                      (SELECT TDS.DEALER_ID\n" );
    	sql.append("                         FROM TM_DEALER TDS\n" );
    	sql.append("                        WHERE TDS.DEALER_ID = "+dealerId+"),\n");
    	sql.append("                      10851002,\n" );
    	sql.append("                      (SELECT TDS.PARENT_DEALER_D\n" );
    	sql.append("                         FROM TM_DEALER TDS\n" );
    	sql.append("                        WHERE TDS.DEALER_ID = "+dealerId+"))");
    	BaseDao<PO> dao=new UserManageDao();
    	Map<String,Object> m=dao.pageQueryMap(sql.toString(), null, "com.infodms.dms.util.commonUtils.getDealerOrgId");
    	String orgId=m.get("ORG_ID").toString();
    	return orgId;

    }
    
  //获取集团经销商的下级经销商
  	public static String getGroupDealerCodes(String dealer_id){
  		String dealerCodes="";
  		TPcCompanyGroupPO tpg=new TPcCompanyGroupPO();
  		tpg.setParDealerId(new Long(dealer_id));
  		BaseDao dao=new  UserManageDao();
  		if(dao.select(tpg).size()<=0){
  			return "-1";
  		}
  		int size=dao.select(tpg).size();
  		for(int k=0;k<size;k++){
  			TPcCompanyGroupPO temp=new TPcCompanyGroupPO();
  			temp=(TPcCompanyGroupPO) dao.select(tpg).get(0);
  			if(k==(size-1)){
  				dealerCodes+=temp.getDealerCodes();
  			}else{
  				dealerCodes+=temp.getDealerCodes()+",";
  			}
  		}
  		return dealerCodes;
  	}
  	/**
	 * 判断是否为顾问登陆
	 * true 表示是，false表示否
	 * @param city
	 * @return
	 */
	public static boolean judgeAdviserLogin(String userId) {
		boolean flag= false;
		BaseDao dao=new  UserManageDao();
		TcUserPO tc=new TcUserPO();
		tc.setUserId(Long.parseLong(userId));
		tc=(TcUserPO) dao.select(tc).get(0);
		if(tc.getPoseRank().equals(Constant.DEALER_USER_LEVEL_04) || tc.getPoseRank()==Constant.DEALER_USER_LEVEL_04.longValue()){
			flag=true;
		}
		return flag;
	}
	/**
	 * 判断是否为主管登陆
	 * true 表示是，false表示否
	 * @param city
	 * @return
	 */
	public static boolean judgeDirectorLogin(String userId) {
		boolean flag= false;
		BaseDao dao=new  UserManageDao();
		TcUserPO tc=new TcUserPO();
		tc.setUserId(Long.parseLong(userId));
		tc=(TcUserPO) dao.select(tc).get(0);
		if(tc.getPoseRank().equals(Constant.DEALER_USER_LEVEL_03) || tc.getPoseRank()==Constant.DEALER_USER_LEVEL_03.longValue()){
			flag=true;
		}
		return flag;
	}
	/**
	 * 判断是否为DCRC登陆
	 * true 表示是，false表示否
	 * @param city
	 * @return
	 */
	public static boolean judgeDcrcLogin(String userId) {
		boolean flag= false;
		BaseDao dao=new  UserManageDao();
		TcUserPO tc=new TcUserPO();
		tc.setUserId(Long.parseLong(userId));
		tc=(TcUserPO) dao.select(tc).get(0);
		if(tc.getPoseRank().equals(Constant.DEALER_USER_LEVEL_05) || tc.getPoseRank()==Constant.DEALER_USER_LEVEL_05.longValue()){
			flag=true;
		}
		return flag;
	}
	/**
	 * 获取主管下属分组的所有顾问
	 * @return
	 */
	public static String getAdviserByDirector(String userId) {
		String adviser = "";
		BaseDao dao=new  UserManageDao();
		TcUserPO tc=new TcUserPO();
		tc.setParUserId(Long.parseLong(userId));
		List<PO> po = dao.select(tc);
		for(int i=0;i<po.size();i++) {
			TcUserPO userPo = (TcUserPO)po.get(i);
			if(i<po.size()-1) {
				adviser = adviser+userPo.getUserId().toString()+",";
			} else {
				adviser = adviser+userPo.getUserId().toString();
			}
		}
		return adviser;
	}
	/**
	 * 判断一个客户是否可以做订单
	 * @param map
	 * @return
	 * true 表示可以做订单
	 * false 表示不可以做订单
	 */
	public static boolean judgeIfOrder(Map<String,String> map){
		//得到当前的手机号码
		String telephone=map.get("telephone");
		//得到当前的经销商
		String dealerId=map.get("dealerId");
		
		boolean flag=false;
		BaseDao dao=new  UserManageDao();
		StringBuilder sql= new StringBuilder();
		sql.append("select count(1) counts\n" );
		sql.append("  from t_pc_leads tpl, t_pc_leads_allot tpla\n" );
		sql.append(" where tpl.leads_code = tpla.leads_code\n" );
		sql.append("   and tpl.telephone = '"+telephone+"'\n" );
		sql.append("   and tpla.dealer_id = '"+dealerId+"'\n" );
		sql.append("   and tpl.jc_way in ('60021001', '60021003', '60021004','60021008')");
		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		int counts=Integer.parseInt(list.get(0).get("COUNTS").toString());
		if(counts>0){
			flag=true;
		}
		return flag;
	}
	//PC手机同时处理同一条数据
	public static int getTheSameData4(String taskId){
		BaseDao dao=new  UserManageDao();
		TPcFollowPO po = new TPcFollowPO();
		po.setFollowId(Long.parseLong(taskId));
		if(dao.select(po).size()>0) {
			po = (TPcFollowPO)dao.select(po).get(0);
			int i = po.getIfHandle();
			return i;
		} else {
			return 0;
		}
	}
	
	/**
     * 标记提醒信息为已完成
     * beRemindId:被提醒ID
     * @return
     */
	public static void setRemindDone(String beRemindId,String remindType) {
		try {
			BaseDao dao=new  UserManageDao();
			TPcRemindPO oldRemindPo = new TPcRemindPO();
			oldRemindPo.setBeremindId(Long.parseLong(beRemindId));
			oldRemindPo.setRemindType(remindType);
			TPcRemindPO newRemindPo = new TPcRemindPO();
			newRemindPo.setBeremindId(Long.parseLong(beRemindId));
			newRemindPo.setRemindStatus(Constant.TASK_STATUS_02);
			newRemindPo.setRemindType(remindType);
			dao.update(oldRemindPo,newRemindPo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//PC手机同时处理同一条数据
	public static int getTheSameData2(String inviteId){
		BaseDao dao=new  UserManageDao();
		TPcInvitePO po = new TPcInvitePO();
		po.setInviteId(Long.parseLong(inviteId));
		if(dao.select(po).size()>0) {
			po = (TPcInvitePO)dao.select(po).get(0);
			int i = po.getIfHandle();
			return i;
		} else {
			return 0;
		}
	}
	//PC手机同时处理同一条数据
	public static int getTheSameData3(String inviteShopId){
		BaseDao dao=new  UserManageDao();
		TPcInviteShopPO po = new TPcInviteShopPO();
		po.setInviteShopId(Long.parseLong(inviteShopId));
		if(dao.select(po).size()>0) {
			po = (TPcInviteShopPO)dao.select(po).get(0);
			int i = po.getIfHandle();
			return i;
		} else {
			return 0;
		}
	}
	/**
	 * 判断经销商为一级还是二级
	 * true 表示一级，false表示二级
	 * @param city
	 * @return
	 */
	public static boolean judgeDealerLevel(String dealerId) {
		boolean flag= false;
		BaseDao dao=new  UserManageDao();
		
		TmDealerPO po = new TmDealerPO();
		po.setDealerId(Long.parseLong(dealerId));
		po=(TmDealerPO) dao.select(po).get(0);
		if(po.getParentDealerD()==-1||"-1".equals(po.getParentDealerD())){
			flag=true;
		}
		return flag;
	}
	
	//PC手机同时处理同一条数据
	public static int getTheSameData(String leadsCode){
		BaseDao dao=new  UserManageDao();
		TPcLeadsPO po = new TPcLeadsPO();
		po.setLeadsCode(Long.parseLong(leadsCode));
		if(dao.select(po).size()>0) {
			po = (TPcLeadsPO)dao.select(po).get(0);
			int i = po.getIfHandle();
			return i;
		} else {
			return 0;
		}
	}
	/**
	 * 获取当前经销商对应的有效顾问
	 * @param map
	 * @return
	 */
	public static List<Map<String, Object>> queryUser(Map<String, String> map) {
		String dealerId=map.get("dealerId");
		String userId=map.get("userId");
		StringBuilder sql= new StringBuilder();
		sql.append("select distinct tu.name,tu.user_id\n" );
		sql.append("  from tc_user tu, tm_dealer td\n" );
		sql.append(" where tu.company_id = td.company_id\n" );
		sql.append("   and td.status ="+Constant.STATUS_ENABLE+" \n" );
		sql.append("   and tu.user_status = "+Constant.STATUS_ENABLE+"\n" );
		sql.append("   and tu.pose_rank="+Constant.DEALER_USER_LEVEL_04);
		if(dealerId!=null&&!"".equals(dealerId)){
			sql.append("   and td.dealer_id="+dealerId);
			if(userId!=null&&!"".equals(userId)){
				sql.append("  start with tu.user_id="+userId);
				sql.append(  "CONNECT BY PRIOR tu.user_id=tu.par_user_id");
			}
		
		}
		
		BaseDao dao=new  UserManageDao();
		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	//获取当前登录的人的职位级别
	public static String getPoseRank(AclUserBean logonUser){
		String poseRank="";
		BaseDao dao=new  UserManageDao();
		if(logonUser.getDealerId()!=null&&!"".equals(logonUser.getDealerId())){
			TcUserPO tc=new TcUserPO();
			tc.setUserId(logonUser.getUserId());
			tc=(TcUserPO) dao.select(tc).get(0);
			poseRank=tc.getPoseRank().toString();
		}
		return poseRank;
	}
	/**
	 * 获取字典下级的数量
	 * @param city
	 * @return
	 */
	public static int getNextLevelCount(String type,String tableType,String dealerId) {
			int flag= 0;
			BaseDao dao=new  UserManageDao();
			if("2".equals(tableType)){
				StringBuilder sql= new StringBuilder();
				sql.append("select count(1) counts from tc_code tc where tc.type="+type+" and tc.dealer_id is not null");
				List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
				flag=Integer.parseInt(list.get(0).get("COUNTS").toString());
			}else{
				StringBuilder sql= new StringBuilder();
				sql.append("select count(1) counts from tc_code tc where tc.type="+type+" and tc.dealer_id is  null");
				List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
				flag=Integer.parseInt(list.get(0).get("COUNTS").toString());
			}
			
		
		return flag;
	}
	public static List<Map<String, Object>> queryDataList(Map<String, String> map) {
		String codeId=map.get("codeId");
		String dealerId=map.get("dealerId");
		StringBuilder sql= new StringBuilder();
		sql.append("select td.code_id AS TREE_ID,\n" );
		sql.append("       td.type AS PARENT_ID,\n" );
		sql.append("       td.code_desc AS TREE_NAME,\n" );
		sql.append("       NVL( td.code_level,1) AS TREE_LEVEL,\n" );
		sql.append("       (select count(1) from tc_code tc where tc.type = td.code_id) AS NEXT_COUNT\n" );
		sql.append("  from tc_code td\n" );
		sql.append(" where td.code_id != '"+codeId+"'\n" );
		sql.append("   and td.status = '10011001'\n" );
		sql.append("and (td.dealer_id="+dealerId+" or td.dealer_id is null)");
		sql.append(" START WITH TD.code_id = '"+codeId+"'" );
		sql.append("CONNECT BY PRIOR TD.code_id = TD.type");
		sql.append(" order by td.num ");
		BaseDao dao=new  UserManageDao();
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	/**
	 * 加载省市区的数据
	 * @param map
	 * @return
	 */
	public static List<Map<String, Object>> queryRegionDataList(Map<String, String> map) {
		StringBuilder sql= new StringBuilder();
		sql.append("select tr.region_id,\n" );
		sql.append("       tr.region_code,\n" );
		sql.append("       tr.region_name,\n" );
		sql.append("       tr.parent_id,\n" );
		sql.append("       tr.region_type,\n" );
		sql.append("       tpr.region_code par_code\n" );
		sql.append("  from t_pc_region tr,t_pc_region tpr\n" );
		sql.append(" where tr.status = 10011001\n" );
		sql.append(" and tpr.region_id(+)=tr.parent_id\n" );
		sql.append(" order by tr.region_code");
		BaseDao dao=new  UserManageDao();
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	/**
	 * 加载省市区的数据
	 * @param map
	 * @return
	 */
	public static List<Map<String, Object>> queryRegionDataList1(Map<String, String> map) {
		StringBuilder sql= new StringBuilder();
		sql.append("select tr.region_id,\n" );
		sql.append("       tr.region_code,\n" );
		sql.append("       tr.region_name,\n" );
		sql.append("       tr.parent_id,\n" );
		sql.append("       tr.region_type,\n" );
		sql.append("       tpr.region_code par_code\n" );
		sql.append("  from tm_region tr,tm_region tpr\n" );
		sql.append(" where tr.status = 10011001\n" );
		sql.append(" and tpr.region_id(+)=tr.parent_id\n" );
		sql.append(" order by tr.region_code");
		BaseDao dao=new  UserManageDao();
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	public static List<Map<String, Object>> queryAllDataList(Map<String, String> map) {
		String codeId=map.get("codeId");
		StringBuilder sql= new StringBuilder();
		sql.append("select td.code_id AS TREE_ID,\n" );
		sql.append("       td.type AS PARENT_ID,\n" );
		sql.append("       td.code_desc AS TREE_NAME,\n" );
		sql.append("       NVL( td.code_level,1) AS TREE_LEVEL,\n" );
		sql.append("       (select count(1) from tc_code tc where tc.type = td.code_id) AS NEXT_COUNT\n" );
		sql.append("  from tc_code td\n" );
		sql.append(" where 1=1\n" );
		sql.append("   and td.status = '10011001'\n" );
		sql.append(" START WITH TD.code_id = '"+0+"'" );
		sql.append("CONNECT BY PRIOR TD.code_id = TD.type");
		sql.append(" order by td.type,td.code_id ");
		BaseDao dao=new  UserManageDao();
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	/**
	 * 重复交车之后主管退车是否修改车辆状态为10321003经销商在库 判断交车表
	 * @param map
	 * @return
	 *  int 表示是否修改车辆状态
	 * 
	 */
	public static int getCarBackDelvy(String vehicle_id, String delv_detail_id){
		
		boolean flag=false;
		BaseDao dao=new  UserManageDao();
		StringBuilder sql= new StringBuilder();
	
        sql.append(" select nvl(count(*),0) CAR_BACK from t_pc_delvy tpd where tpd.delv_detail_id<>'"+delv_detail_id+"' and tpd.vehicle_id='"+vehicle_id+"' and tpd.delivery_status<>'60571004' ");
		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		int carDelvy=0;
		if(list.size()>0){
			carDelvy=Integer.parseInt(list.get(0).get("CAR_BACK").toString());
		}
		return carDelvy;
	}
	/**
	 * 重复交车之后主管退车是否修改车辆状态为10321003经销商在库 判断实销表
	 * @param map
	 * @return
	 *  int 表示是否修改车辆状态
	 * 
	 */
	public static int getCarBackSales(String vehicle_id ){
		
		boolean flag=false;
		BaseDao dao=new  UserManageDao();
		StringBuilder sql= new StringBuilder();
	
        sql.append(" select nvl(count(*),0) CAR_SALES from tt_dealer_actual_sales tdas where tdas.vehicle_id='"+vehicle_id+"'  and tdas.is_return='10041002' ");
		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		int carSales=0;
		if(list.size()>0){
			carSales=Integer.parseInt(list.get(0).get("CAR_SALES").toString());
		}
		return carSales;
	}
	/**
     * 判断潜客订单是否完成交车
     * orderId 订单id
     * @return
     */
	public static boolean getOrderIfFinish(String orderId) {
		boolean flag=false;
		try {
			BaseDao dao=new  UserManageDao();
			StringBuilder sql= new StringBuilder();
			sql.append("select count(1) counts\n" );
			sql.append("  from t_pc_order tpo, t_pc_order_detail tpod\n" );
			sql.append(" where tpo.order_id = tpod.order_id\n" );
			sql.append("   and tpod.num != tpod.delivery_number\n" );
			sql.append("   and tpo.order_id = "+orderId);
			sql.append("   and tpod.task_status in(60171002 ,60171001)");
			List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
			int i=Integer.parseInt(list.get(0).get("COUNTS").toString());
			if(i==0){
				flag=true;
			}
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
	}
	/**
	 * 清零客户到店次数
	 * @param map
	 * @return
	 *  int 表示到店的次数
	 * 
	 */
	public static void clearComeCount(Long customerId){
		
		BaseDao dao=new  UserManageDao();
		TPcCustomerPO tpc1=new TPcCustomerPO();
		tpc1.setCustomerId(customerId);
		TPcCustomerPO tpc2=new TPcCustomerPO();
		tpc2.setComeCount(new Long(0));
		dao.update(tpc1, tpc2);
		
	
	}
	//判断是否是进口车
	public static boolean judgeIfForeign(String series_id){
		boolean  flag=false;
		BaseDao dao=new  UserManageDao();
		TPcIntentVehiclePO tpv=new TPcIntentVehiclePO();
		tpv.setSeriesId(new Long(series_id));
		tpv=(TPcIntentVehiclePO) dao.select(tpv).get(0);
		if(Constant.IF_TYPE_YES.toString().equals(tpv.getIsForeign().toString())){
			flag=true;
		}
		return flag;
	}
	//删除新增订单审核和生成的订单不对应的数据
	public static void delOrderAuditAmount(String orderId,String orderDetailId) throws SQLException
	{
		if(orderId==null||"".equals(orderId)) return;
		StringBuffer sql = new StringBuffer("");
		sql.append("DELETE FROM  t_pc_order_detail tpod");
		sql.append(" WHERE tpod.order_id='"+orderId+"' ");
		if(orderDetailId!=null&&!"".equals(orderDetailId)){
			sql.append(" AND tpod.order_detail_id <>'"+orderDetailId+"' ");
		}
		factory.delete(sql.toString(), null);
	}
	/**
	 * 查询销售经理审核时订单明细表中车辆的总台数
	 * @param map
	 * @return
	 */
	public static  int getOrderDetailNum( String orderId){
		boolean flag=false;
		BaseDao dao=new  UserManageDao();
		StringBuilder sql= new StringBuilder();
		sql.append("select nvl(sum(tpod.num),0) num  from t_pc_order_detail tpod where tpod.order_id='"+orderId+"' and tpod.task_status<>'60171003' \n" );
		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		int dNum=0;
		if(list.size()>0){
			dNum=Integer.parseInt(list.get(0).get("NUM").toString());
		}
		return dNum;
	}
	/**
	 * 查询销售经理审核时订单明细审核表中车辆的总台数
	 * @param map
	 * @return
	 */
	public static  int getOrderDetailAuditNum( String orderId){
		boolean flag=false;
		BaseDao dao=new  UserManageDao();
		StringBuilder sql= new StringBuilder();
		sql.append("select nvl(sum(tpoda.num),0) num  from t_pc_order_detail_audit tpoda where tpoda.order_id='"+orderId+"' and tpoda.status<>'10011002' \n" );
		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		int aNum=0;
		if(list.size()>0){
			aNum=Integer.parseInt(list.get(0).get("NUM").toString());
		}
		return aNum;
	}
	/**
	 * 判断是否为总经理登陆
	 * true 表示是，false表示否
	 * @param city
	 * @return
	 */
	public static boolean judgeTotalMgrAllLogin(String userId) {
		boolean flag= false;
		BaseDao dao=new  UserManageDao();
		TcUserPO tc=new TcUserPO();
		tc.setUserId(Long.parseLong(userId));
		tc=(TcUserPO) dao.select(tc).get(0);
		if(tc.getPoseRank().equals(Constant.DEALER_USER_LEVEL_01) || tc.getPoseRank()==Constant.DEALER_USER_LEVEL_01.longValue()){
			flag=true;
		}
		return flag;
	}
	public static String getPercent(Object[] obj){  
		Double allCount=new Double(obj[1].toString());
        Double intimeCount=new Double(obj[0].toString());
        Double intimeRate=intimeCount/allCount;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2); //设置小数点保留几位
		   return nf.format(intimeRate);  
	}
	
	/**
	 * 增加客户到店次数
	 * @param map
	 * @return
	 *  int 表示到店的次数
	 * 
	 */
	public static void addComeCount(Long customerId){
		
		BaseDao dao=new  UserManageDao();
		//获取到店次数
		TPcCustomerPO tpc0=new TPcCustomerPO();
		tpc0.setCustomerId(customerId);
		tpc0=(TPcCustomerPO) dao.select(tpc0).get(0);
		
		Long comeCount=tpc0.getComeCount();
		long count=0;
		if(comeCount!=null){
			count=comeCount.longValue()+1;
		}else{
			count=1;
		}
		TPcCustomerPO tpc1=new TPcCustomerPO();
		tpc1.setCustomerId(customerId);
		TPcCustomerPO tpc2=new TPcCustomerPO();
		tpc2.setComeCount(new Long(count));
		dao.update(tpc1, tpc2);
	
	}
	
	/**
	 * 判断一个客户是否可以做订单 (当天必须有客流的情况)
	 * @param map
	 * @return
	 * true 表示可以做订单
	 * false 表示不可以做订单
	 */
	public static boolean judgeIfOrderDate(Map<String,String> map){
		//得到当前的手机号码
		String telephone=map.get("telephone");
		//得到当前的经销商
		String dealerId=map.get("dealerId");
		
		Date d=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String dateStr=sdf.format(d);
		boolean flag=false;
		BaseDao dao=new  UserManageDao();
		StringBuilder sql= new StringBuilder();
		sql.append("select count(1) counts\n" );
		sql.append("  from t_pc_leads tpl, t_pc_leads_allot tpla\n" );
		sql.append(" where tpl.leads_code = tpla.leads_code\n" );
		sql.append("   and tpl.telephone = '"+telephone+"'\n" );
		sql.append("   and tpla.dealer_id = '"+dealerId+"'\n" );
		sql.append("  and tpl.LEADS_ORIGIN=60151011");
		sql.append("  and tpl.create_date<=to_date('"+dateStr+" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
		sql.append("  and tpl.create_date>=to_date('"+dateStr+" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		//sql.append("   and tpl.jc_way in ('60021001', '60021003', '60021004')");
		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		int counts=Integer.parseInt(list.get(0).get("COUNTS").toString());
		if(counts>0){
			flag=true;
		}
		return flag;
	}
	
	/**
	 * 判断一个订单是否可以做交车 
	 * @param map
	 * @return
	 * true 表示可以做交车
	 * false 表示不可以做交车
	 */
	public static boolean judgeIfAbleDelvy(Map<String,String> map){
		boolean flag=false;
		BaseDao dao=new  UserManageDao();
		String orderDetailId=map.get("orderDetailId");
		TPcOrderDetailPO tpod=new TPcOrderDetailPO();
		tpod.setOrderDetailId(new Long(orderDetailId));
		tpod=(TPcOrderDetailPO) dao.select(tpod).get(0);
		TPcOrderPO tpo=new TPcOrderPO();
		tpo.setOrderId(tpod.getOrderId());
		tpo=(TPcOrderPO) dao.select(tpo).get(0);
		if(Constant.TPC_ORDER_STATUS_01.toString().equals(tpo.getOrderStatus().toString())||Constant.TPC_ORDER_STATUS_02.toString().equals(tpo.getOrderStatus().toString())){
			if("60171001".equals(tpod.getTaskStatus().toString())){
				flag=true;
			}
		}
		return flag;
	}
	
	/**
     * 根据职位ID查询选中的省份对应的所有城市
     * param poseId职位Id
     *
     * @return 省市列表
     */
    public static List<TmRegionPO> findCeckCityPO(Long poseId) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append("SELECT DISTINCT TR.REGION_ID,TR.REGION_NAME,TR.ORG_ID\n");
        sbSql.append("     FROM TM_REGION TR,\n");
        sbSql.append("          TR_POSE_REGION TPR\n");
        sbSql.append("    WHERE TR.ORG_ID = TPR.REGION_ID AND TR.REGION_TYPE=?\n");
        sbSql.append("      AND TPR.POSE_ID = ?\n"); 
        sbSql.append(" ORDER BY TR.ORG_ID\n");
        List<Object> params = new ArrayList<Object>();
        params.add(Constant.REGION_TYPE_03);
        params.add(poseId);
        return factory.select(sbSql.toString(), params, new DAOCallback<TmRegionPO>() {
        	public TmRegionPO wrapper(ResultSet rs, int idx){
        		TmRegionPO po = new TmRegionPO();
        		try {
					po.setRegionId(rs.getLong("REGION_ID"));
					po.setRegionName(rs.getString("REGION_NAME"));
					po.setOrgId(rs.getLong("ORG_ID"));
				} catch (Exception e) {
					e.printStackTrace();
				}
        		return po;
        	}
		});
        
    }
    
    public static boolean checkIsNullObject(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            if (obj.toString().trim().equals("") || obj.toString().trim().equals("null") || obj.toString().trim().equals("NULL")) {
                return true;
            }
        }
        return false;
    }
}





