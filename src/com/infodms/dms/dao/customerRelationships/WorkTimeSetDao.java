package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtCrmWorktimePO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class WorkTimeSetDao extends BaseDao{

	private static final WorkTimeSetDao dao = new WorkTimeSetDao();
	
	public static final WorkTimeSetDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public PageResult<Map<String,Object>> queryWorkTimeSet(int pageSize,int curPage){
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.WT_ID              ID,\n" +
				"       C.CODE_DESC          AS WT_TYPE,\n" + 
				"       CASE WHEN NVL(T.WT_STA_ON_MINUTE,0) <10 THEN '0'||NVL(T.WT_STA_ON_MINUTE,0) ELSE to_char(NVL(T.WT_STA_ON_MINUTE,0)) END ||':'||\n" + 
				"       CASE WHEN NVL(T.WT_STA_OFF_MINUTE2,0) <10 THEN '0'||NVL(T.WT_STA_OFF_MINUTE2,0) ELSE TO_CHAR(NVL(T.WT_STA_OFF_MINUTE2,0)) END AS STA_TIME,\n" + 
				"       CASE WHEN NVL(T.WT_END_ON_MINUTE,0) <10 THEN '0'||NVL(T.WT_END_ON_MINUTE,0) ELSE to_char(NVL(T.WT_END_ON_MINUTE,0)) END ||':'||\n" + 
				"       CASE WHEN NVL(T.WT_END_OFF_MINUTE,0) <10 THEN '0'||NVL(T.WT_END_OFF_MINUTE,0) ELSE TO_CHAR(NVL(T.WT_END_OFF_MINUTE,0)) END AS END_TIME\n" + 
				"  FROM TT_CRM_WORKTIME T, TC_CODE C\n" + 
				" WHERE T.WT_TYPE = C.CODE_ID");
;
		
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}

	public TtCrmWorktimePO queryTtCrmWorktimePOById(TtCrmWorktimePO ttCrmWorktimePO) {
		List<TtCrmWorktimePO> lists = this.select(ttCrmWorktimePO);
		if(lists.size()>0) return lists.get(0);
		return null ;
	}

}
