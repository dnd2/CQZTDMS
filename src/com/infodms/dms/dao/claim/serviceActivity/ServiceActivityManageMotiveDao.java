package com.infodms.dms.dao.claim.serviceActivity;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.ttAsActivitySubjectBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TtAsActivitySubjectPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 活动主题DAO
 * @ClassName     : ServiceActivityManageMotiveDao 
 * @Description   : TODO 
 * @author        : Administrator
 * CreateDate     : 2013-4-3
 */
public class ServiceActivityManageMotiveDao extends BaseDao
{
	
	public static Logger logger = Logger.getLogger(ServiceActivityManageDao.class);
	private static final ServiceActivityManageMotiveDao dao = new ServiceActivityManageMotiveDao();
	public  static final ServiceActivityManageMotiveDao getInstance()
	{
		return dao;
	}
	
	@SuppressWarnings("unchecked")
	public  PageResult<Map<String, Object>>  getAllServiceActivityManageMotive(ttAsActivitySubjectBean ttAsActivityBean,int curPage,int pageSize)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM TT_AS_ACTIVITY_SUBJECT TT WHERE 1=1 AND TT.IS_DEL != 1");
	
		if(!"".equals(ttAsActivityBean.getSubjectNo())){
			sql.append(" AND UPPER(TT.SUBJECT_NO) like UPPER('%"+ttAsActivityBean.getSubjectNo()+"%')\n");
		}
		
		if(!"".equals(ttAsActivityBean.getActivityType()) &&!(null==ttAsActivityBean.getActivityType())){
			sql.append(" AND TT.ACTIVITY_TYPE = '"+ttAsActivityBean.getActivityType()+"'  \n");
		}
		
		if(!"".equals(ttAsActivityBean.getSubjectName())){
			sql.append(" AND UPPER(TT.SUBJECT_NAME) like UPPER('%"+ttAsActivityBean.getSubjectName()+"%')\n");
		}
		
		if(!"".equals(ttAsActivityBean.getSubjectStartDate()) &&!(null==ttAsActivityBean.getSubjectStartDate())){      //活动开始日期不为空
			sql.append(" AND TT.SUBJECT_START_DATE >=to_date('"+ttAsActivityBean.getSubjectStartDate()+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(ttAsActivityBean.getSubjectEndDate()) &&!(null==ttAsActivityBean.getSubjectEndDate())){         //活动结束日期不为空
			sql.append(" AND TT.SUBJECT_END_DATE  <= to_date('"+ttAsActivityBean.getSubjectEndDate()+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		// 添加按照主题编号倒叙排列 艾春9.11添加
		sql.append("order by TT.SUBJECT_NO desc");
		
		PageResult<Map<String, Object>> pageQuery = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		PageResult<Map<String, Object>> ps = pageQuery;
		return ps;
	}
	
	@SuppressWarnings("unchecked")
	public void serviceActivityManageDelete(String subject_id ,TtAsActivitySubjectPO ttAsActivitySubjectPO)
	{
		TtAsActivitySubjectPO ttAsActivitySubjectPO1 = new TtAsActivitySubjectPO();
		ttAsActivitySubjectPO1.setSubjectId(Long.parseLong(subject_id));
		dao.update(ttAsActivitySubjectPO1, ttAsActivitySubjectPO);
	}
	
	@SuppressWarnings({ "unchecked", "static-access" })
	public List<TcUserPO> selectUser()
	{
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT NAME,USER_ID FROM TC_USER TT WHERE TT.APPROVAL_LEVEL_CODE IS NOT NULL");
		@SuppressWarnings("unused")
		TcUserPO tcUserPO = new TcUserPO();
		List<TcUserPO> list = dao.select(TcUserPO.class, sql.toString(), params);
		return list;
	}
	public static void serviceActivityManageAdd(TtAsActivitySubjectPO ttActivityPO) {
		dao.insert(ttActivityPO);
    }
	@Override
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void serviceActivityManageUpdate(String subject_id ,TtAsActivitySubjectPO ttAsActivitySubjectPO)
	{
		TtAsActivitySubjectPO ttAsActivitySubjectPO1 = new TtAsActivitySubjectPO();
		ttAsActivitySubjectPO1.setSubjectId(Long.parseLong(subject_id));
		dao.update(ttAsActivitySubjectPO1, ttAsActivitySubjectPO);
	}

}
