package com.infodms.dms.dao.parts.baseManager.partsBaseManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : luole
 *         CreateDate     : 2013-4-2
 * @ClassName : PartWareHouseDao
 * @Description : 配件仓库维护DAO
 */
public class PartFixcodeDAO extends BaseDao<PO> {
    public static final Logger logger = Logger.getLogger(PartFixcodeDAO.class);
    private static final PartFixcodeDAO dao = new PartFixcodeDAO();

    private PartFixcodeDAO() {
    }

    public static final PartFixcodeDAO getInstance() {
        return dao;
    }

    /**
     * @param : @param conSql  条件 SQl
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-11
     * @Title : 配件变量查询
     * @Description: TODO
     */
    public PageResult<Map<String, Object>> fixcodePageQuery(String conSql, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from TT_PART_FIXCODE_DEFINE t where 1=1 \n");
        sql.append(conSql + "\n");
        sql.append("  ORDER BY t.fix_gouptype,t.sort_no");
        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    /**
     * @param : @param typeId
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-11
     * @Title : 根据大类型获取下级类型
     * @Description: TODO
     */
    public List<Map<String, Object>> getPartFixcodeListByTypeId(int typeId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select *  from TT_PART_FIXCODE_DEFINE t where  t.Fix_Gouptype = " + typeId + " order by to_number(t.sort_no)");
        return pageQuery(sql.toString(), null, getFunName());
    }

    public List<Map<String, Object>> getPartFixcodeListByTypeId(int typeId, String typeDesc) {
        StringBuffer sql = new StringBuffer();
        sql.append("select t.fix_value,t.fix_name  from TT_PART_FIXCODE_DEFINE t where  t.Fix_Gouptype = " + typeId + "  order by to_number(t.sort_no)");
        return pageQuery(sql.toString(), null, getFunName());
    }

    /**
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-11
     * @Title : 新增初始化页面的时候查询的类型
     * @Description: TODO
     */
    public List<Map<String, Object>> getPartFixcodeType() {
        StringBuffer sql = new StringBuffer();
        sql.append("select c.code_id, c.code_desc\n" +
                "  from tc_code c\n" +
                " where not exists (select *\n" +
                "          from tt_part_fixcode_define f\n" +
                "         where c.code_id = f.fix_gouptype and f.state=10011001)\n" +
                "   and c.type = 9225 and c.status=10011001");
        return pageQuery(sql.toString(), null, getFunName());
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }
}
