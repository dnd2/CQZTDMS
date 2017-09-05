package com.infodms.dms.dao.notice.noticeModel;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.NoticeModelBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmNoticemodelPO;
import com.infodms.dms.util.BeanUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


/**
 * 消息提醒模版DAO
 * @author chenyu
 *
 */
public class NoticeModelDAO extends BaseDao<PO>{
	public Logger logger = Logger.getLogger(NoticeModelDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	/**
	 * 自定义封装PO
	 */
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * 新增消息提醒模版
	 * @param noticeModel
	 * @author chenyub@yonyou.com
	 */
	public void insertNoticeModel(TmNoticemodelPO noticeModel){
		if (null == noticeModel) {
			return;
		}
		if(StringUtil.isNull(noticeModel.getId())){
			noticeModel.setId(getStringPK(noticeModel));
		}
		factory.insert(noticeModel);
	}
	
	
	/**
	 * 查询提醒模版
	 * @param noticeModel
	 * @return
	 * @author chenyub@yonyou.com
	 */
	public PageResult<NoticeModelBean> queryNoticeModel(
			TmNoticemodelPO noticeModel,int curPage,int pageSize) {
		StringBuffer buff = new StringBuffer();
		buff.append("select fun.func_name,fun.func_code,nm.* from tm_noticemodel nm ");
		buff.append(" left join tc_func fun on fun.func_id=nm.nm_menuid ");
		buff.append(" where 1=1 ");
		if(null!=noticeModel){
			if(StringUtils.isNotEmpty(noticeModel.getId())){
				buff.append(" and nm.id = '").append(noticeModel.getId()).append("' ");
			}
		}
		buff.append(" order by nm.nm_createtime desc");
		List<Object> params = new LinkedList<Object>();
		PageResult<Map<String, Object>> pageList = this.pageQuery(buff.toString(), params, getFunName(),pageSize,curPage);
		List<Map<String, Object>> nl = pageList.getRecords();
		List<NoticeModelBean> list = new ArrayList<NoticeModelBean>();
		for (int i=0;i<nl.size();i++) {
			Map<String, Object> map = nl.get(i);
			list.add(BeanUtils.map2Bean(map, new NoticeModelBean()));
		}
		
		PageResult<NoticeModelBean> reList = new PageResult<NoticeModelBean>();
		reList.setCurPage(pageList.getCurPage());
		reList.setPageSize(pageList.getPageSize());
		reList.setTotalPages(pageList.getTotalPages());
		reList.setTotalRecords(pageList.getTotalRecords());
		reList.setRecords(list);
		return reList;
	}

}
