package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @ClassName : WarningTimeDao
 * @Description : 预警时间规则维护DAO
 * @author : luole CreateDate : 2013-4-17
 */
public class WarningTimeDao extends BaseDao<PO> {
	private static WarningTimeDao dao = new WarningTimeDao();
	public static WarningTimeDao getInstance() {
		if (dao == null)
			dao = new WarningTimeDao();
		return dao;
	}
	/**
	 * @Title      : 获取权限列表
	 * @Description: TODO 
	 * @param      : @return      
	 * @return     :    
	 * @throws     : luole
	 * LastDate    : 2013-4-18
	 */
	public List<Map<String, Object>> getAuthinfo(String code,String oemId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select tawa.approval_level_code,\n");
		sql.append("                      tawa.approval_level_name,\n");
		sql.append("                      tawa.approval_level_tier\n");
		sql.append("                 from tt_as_wr_authinfo tawa\n");
		sql.append("                where tawa.approval_level_code <> 100\n");
		sql.append("                  and tawa.type = 0\n");
		sql.append("                  and tawa.oem_company_id = "+oemId+"\n");
		if(code!=null && !"".equals(code)){
			sql.append("              and tawa.APPROVAL_LEVEL_CODE in( "+code+")\n");
		}
		sql.append("                order by to_number(tawa.approval_level_code)");
		return pageQuery(sql.toString(), null, getFunName());
	}
	
	
	/**
	 * @Title      : 分布查询 
	 * @Description: TODO 
	 * @param      : @param consql
	 * @param      : @param curPage
	 * @param      : @param pageSize
	 * @param      : @return      
	 * @return     :    
	 * @throws     : luole
	 * LastDate    : 2013-4-18
	 */
	public PageResult<Map<String, Object>> warningTimeQuery(String consql,int curPage,int pageSize){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tt_as_warning_time wt where 1=1 \n");
		sql.append(consql);
		return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
