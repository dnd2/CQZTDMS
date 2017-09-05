package com.infodms.dms.dao.parts.baseManager.partsBaseManager;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * <p>ClassName: PartHolidayDao</p>
 * <p>Description: 配件节假日维护查询</p>
 * <p>Author: MEpaper</p>
 * <p>Date: 2017年7月13日</p>
 */
@SuppressWarnings("rawtypes")
public class PartHolidayDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PartAddrDao.class);

    private static final PartHolidayDao dao = new PartHolidayDao();
    
    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    private PartHolidayDao() {

    }
    
    public static final PartHolidayDao getInstance() {
        return dao;
    }
    
    /**
     * <p>Description: 获取查询节假日列表的SQL字符串</p>
     * @param request
     * @param logonUser 当前登录用户信息
     * @return
     */
    private String getPartHolidayListSql(RequestWrapper request, AclUserBean logonUser) {
        // 节假日开始日期
        String holidayDate = CommonUtils.checkNull(request.getParamValue("HOLIDAY_DATE"));
        // 节假日开始日期
        String holidayStartDate = CommonUtils.checkNull(request.getParamValue("HOLIDAY_START_DATE"));
        // 节假日结束日期
        String holidayEndDate = CommonUtils.checkNull(request.getParamValue("HOLIDAY_END_DATE"));
        // 状态
        String status = CommonUtils.checkNull(request.getParamValue("STATUS"));
        
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT PH.HOLIDAY_ID, \n");
        sql.append("        TO_CHAR(PH.HOLIDAY_DATE, 'YYYY-MM-DD') HOLIDAY_DATE, \n");
        sql.append("        PH.STATUS, \n");
        sql.append("        PH.CREATE_BY, \n");
        sql.append("        PH.CREATE_DATE \n");
        sql.append("   FROM TT_PART_HOLIDAY PH \n");
        sql.append("  WHERE 1=1 \n ");
        
        if(StringUtils.isNotEmpty(holidayDate)){
            sql.append("    AND PH.HOLIDAY_DATE = TO_DATE('"+holidayDate+"', 'YYYY-MM-DD') \n ");
        }
        if(StringUtils.isNotEmpty(holidayStartDate)){
            sql.append("    AND PH.HOLIDAY_DATE >= TO_DATE('"+holidayStartDate+"', 'YYYY-MM-DD') \n ");
        }
        if(StringUtils.isNotEmpty(holidayEndDate)){
            sql.append("    AND PH.HOLIDAY_DATE <= TO_DATE('"+holidayEndDate+"', 'YYYY-MM-DD')+1-1/24/59/59 \n ");
        }
        if(StringUtils.isNotEmpty(status)){
            sql.append("    AND PH.STATUS = "+status+" \n ");
        }
        sql.append("  ORDER BY PH.HOLIDAY_DATE \n");
        return sql.toString();
    }

    /**
     * <p>Description: 分页查询节假日列表</p>
     * @param request 
     * @param logonUser 当前登录用户
     * @param curPage 页码
     * @param pageSize 分页条数
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageResult<Map<String, Object>> queryPartHolidayList(RequestWrapper request,
            AclUserBean logonUser, Integer curPage, Integer pageSize) {
        String sql = this.getPartHolidayListSql(request, logonUser);
        PageResult<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }
    
    /**
     * <p>Description: 不分页查询节假日列表</p>
     * @param request
     * @param logonUser 当前登录用户信息
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPartHolidayList(RequestWrapper request, AclUserBean logonUser){
        String sql = this.getPartHolidayListSql(request, logonUser);
        List<Map<String, Object>> list = dao.pageQuery(sql, null, getFunName());
        return list;
    }
    
    /**
     * <p>Description: 获取节假日数量</p>
     * @param paramMap 参数map
     * @return
     */
    public int getPartHolidayCount(Map<String, String> paramMap){
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT 1 \n");
        sql.append(" FROM TT_PART_HOLIDAY PH \n");
        sql.append("  WHERE 1=1 \n ");

        String holidayDate = paramMap.get("holidayDate");
        if(StringUtils.isNotEmpty(paramMap.get("holidayDate"))){
            sql.append("    AND PH.HOLIDAY_DATE = TO_DATE('"+holidayDate+"', 'YYYY-MM-DD') \n ");
        }
        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list.size();
    }
    
}
