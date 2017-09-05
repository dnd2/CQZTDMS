package com.infodms.dms.dao.notice.dealerNotice;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.DealerNoticeBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtDealernoticePO;
import com.infodms.dms.util.BeanUtils;
import com.infodms.dms.util.enums.CommonEnum;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


/**
 * 消息提醒模版DAO
 * @author chenyu
 *
 */
public class DealerNoticeDAO extends BaseDao<PO>{
	public Logger logger = Logger.getLogger(DealerNoticeDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	/**
	 * 自定义封装PO
	 */
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	/**
	 * 更新指定id的消息提醒的状态
	 * 
	 * @param id
	 *            指定id
	 * @param state
	 *            修改的状态,0||null:未处理;1:已处理
	 * @author chenyub@yonyou.com
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public void updateNoticeState(String id, String state, AclUserBean loginUser)
			throws IllegalAccessException, InvocationTargetException {
		if (null == id) {
			return;
		}
		if(null == state){
			state = "0";
		}
		TtDealernoticePO old = new TtDealernoticePO();
		old.setId(id);
		PageResult<TtDealernoticePO> beans = queryNoticeModelObject(old,Constant.PAGE_SIZE_MAX,1);
		if(null!=beans){
			List<TtDealernoticePO> datas = beans.getRecords();
			if(!CommonUtils.isNullList(datas)&&datas.size()==1){
				TtDealernoticePO bean = datas.get(0);
				bean.setDnHandlestate(state);
				bean.setDnHandleuser(loginUser.getUserId().toString());
				bean.setDnHandletime(new Date());
				factory.update(old, bean);
			}
		}
	}

	/**
	 * 查询noticePO集合
	 * @param noticePO
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @author chenyub@yonyou.com
	 */
	public PageResult<TtDealernoticePO> queryNoticeModelObject(
			TtDealernoticePO noticePO,int pageSize,int curPage) throws IllegalAccessException, InvocationTargetException {
		if(null==noticePO){
			noticePO = new TtDealernoticePO();
		}
		DealerNoticeBean noticeBean = new DealerNoticeBean();
		BeanUtils.copyProperties(noticePO, noticeBean);
		PageResult<Map<String, Object>> pageList = queryNoticeModelMap(noticeBean,pageSize,curPage);
		List<Map<String, Object>> nl = pageList.getRecords();
		List<TtDealernoticePO> list = new ArrayList<TtDealernoticePO>();
		for (int i=0;i<nl.size();i++) {
			Map<String, Object> map = nl.get(i);
			list.add(BeanUtils.map2Bean(map, new TtDealernoticePO()));
		}
		
		PageResult<TtDealernoticePO> reList = new PageResult<TtDealernoticePO>();
		reList.setCurPage(pageList.getCurPage());
		reList.setPageSize(pageList.getPageSize());
		reList.setTotalPages(pageList.getTotalPages());
		reList.setTotalRecords(pageList.getTotalRecords());
		reList.setRecords(list);
		return reList;
	}
	
	/**
	 * 查询noticePO集合
	 * @param noticeBean
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @author chenyub@yonyou.com
	 */
	public PageResult<DealerNoticeBean> queryNoticeModelObject(
			DealerNoticeBean noticeBean,int curPage,int pageSize) {
		PageResult<Map<String, Object>> pageList = queryNoticeModelMap(noticeBean,pageSize,curPage);
		List<Map<String, Object>> nl = pageList.getRecords();
		List<DealerNoticeBean> list = new ArrayList<DealerNoticeBean>();
		for (int i=0;i<nl.size();i++) {
			Map<String, Object> map = nl.get(i);
			list.add(BeanUtils.map2Bean(map, new DealerNoticeBean()));
		}
		
		PageResult<DealerNoticeBean> reList = new PageResult<DealerNoticeBean>();
		reList.setCurPage(pageList.getCurPage());
		reList.setPageSize(pageList.getPageSize());
		reList.setTotalPages(pageList.getTotalPages());
		reList.setTotalRecords(pageList.getTotalRecords());
		reList.setRecords(list);
		return reList;
	}
	/**
	 * 查询noticePO集合
	 * @param noticeBean
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @author chenyub@yonyou.com
	 */
	public PageResult<Map<String, Object>> queryNoticeModelMap(
			DealerNoticeBean noticeBean,int pageSize,int curPage) {
		StringBuffer buff = new StringBuffer();
		buff.append(" select tf.func_id,tf.func_name ");
		buff.append(" ,nm.nm_noticetype ");
		buff.append(" ,tf.func_code ");
		buff.append(" ,tc.code_desc ");
		buff.append(" ,t.* ");
		buff.append(" from tt_dealernotice t ");
		buff.append(" left join tm_noticemodel nm on nm.id=t.nm_id ");
		buff.append(" left join tc_func tf on tf.func_id=nm.nm_menuid ");
		buff.append(" left join tc_code tc on tc.code_id=t.dn_businessstate ");
		buff.append(" left join tc_pose tp on tp.pose_code = nm.nm_tarvalue ");
		buff.append(" left join tc_user tu on upper(tu.acnt) = upper(nm.nm_tarvalue) ");
		buff.append(" where 1=1 ");
		if(null!=noticeBean){
			// ID条件
			if(StringUtils.isNotEmpty(noticeBean.getId())){
				buff.append(" and t.id = '").append(noticeBean.getId()).append("' ");
			}
			// 菜单id条件
			if(StringUtils.isNotEmpty(noticeBean.getFuncId())){
				buff.append(" and tf.func_id = '").append(noticeBean.getFuncId()).append("' ");
			}
			// 处理状态条件
			if(StringUtils.isNotEmpty(noticeBean.getDnHandlestate())){
				buff.append(" and t.dn_handlestate = '").append(noticeBean.getDnHandlestate()).append("' ");
			}
			
			if(StringUtils.isNotEmpty(noticeBean.getNmDealertype())){
				buff.append(" and nm.nm_noticetype = '").append(noticeBean.getNmDealertype()).append("' ");
			}
			
			StringBuffer target = new StringBuffer();
			// 提醒目标条件 限定为个人的条件
			if(StringUtils.isNotEmpty(noticeBean.getDnTarid())){
				target.append(" and (nm.nm_tartype='").append(CommonEnum.TarTypeEnum.用户.getValue()).append("' ");
				target.append(" and t.dn_tarid='").append(noticeBean.getDnTarid()).append("' ");
			}
			// 提醒对象为经销商的
			if(StringUtils.isNotEmpty(noticeBean.getDnDealerid())){
				if(target.length()>0){
					target.append(" or ");
				} else {
					target.append(" and (");
				}
				target.append(" nm.nm_tartype='").append(CommonEnum.TarTypeEnum.用户.getValue()).append("' ");
				target.append(" and t.dn_tarid is null and t.dn_dealerid='").append(noticeBean.getDnDealerid()).append("' ");
				target.append(" or nm.nm_tartype='").append(CommonEnum.TarTypeEnum.经销商服务站.getValue()).append("' ");
				target.append(" and t.dn_dealerid='").append(noticeBean.getDnDealerid()).append("' ");
			}
			// 提醒对象为职位的
			if(StringUtils.isNotEmpty(noticeBean.getTargetPositionId())){
				if(target.length()>0){
					target.append(" or ");
				} else {
					target.append(" and (");
				}
				target.append(" nm.nm_tartype='").append(CommonEnum.TarTypeEnum.职位.getValue()).append("' ");
				target.append(" and tp.pose_id='").append(noticeBean.getTargetPositionId()).append("' ");
			}
			// 提醒对象为指定用户的
			if(StringUtils.isNotEmpty(noticeBean.getTargetUserId())){
				if(target.length()>0){
					target.append(" or ");
				} else {
					target.append(" and (");
				}
				target.append(" nm.nm_tartype='").append(CommonEnum.TarTypeEnum.指定用户.getValue()).append("' ");
				target.append(" and tu.user_id='").append(noticeBean.getTargetUserId()).append("' ");
			}
			if(target.length()>0){
				target.append(" ) ");
			}
			buff.append(target);
		}
		buff.append(" order by t.dn_businessstatetime");
		List<Object> params = new LinkedList<Object>();
		PageResult<Map<String, Object>> pageList = this.pageQuery(buff.toString(), params, getFunName(),pageSize,curPage);
		return pageList;
	}
	

}
