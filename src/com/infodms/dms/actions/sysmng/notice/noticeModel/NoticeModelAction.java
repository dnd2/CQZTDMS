package com.infodms.dms.actions.sysmng.notice.noticeModel;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.infodms.dms.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.NoticeModelBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.menu.MenuDAO;
import com.infodms.dms.dao.notice.noticeModel.NoticeModelDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcFuncPO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmNoticemodelPO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.BeanUtils;
import com.infodms.dms.util.enums.CommonEnum;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


public class NoticeModelAction {
	public Logger logger = Logger.getLogger(NoticeModelAction.class);
	private final String queryUrl = "/jsp/notice/noticeModel/noticeModelList.jsp";
	private final String toAddUrl = "/jsp/notice/noticeModel/noticeEdit.jsp";
	
	/**
	 * 进入消息提醒模板管理初始页面
	 * 
	 * @author chenyub@yonyou.com
	 */
	public void noticeModelIndex(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setRedirect(queryUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "消息提醒模版管理初始页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询消息提醒模版记录
	 * 
	 * @author chenyub@yonyou.com
	 */
	public void queryNoticeModel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			NoticeModelDAO nmDao = new NoticeModelDAO();
			PageResult<NoticeModelBean> datas = nmDao.queryNoticeModel(null,ActionUtil.getCurPage(request),
					ActionUtil.getPageSize(request));
			List<NoticeModelBean> records = datas.getRecords();
			if(!CommonUtils.isNullList(records)){
				for (NoticeModelBean bean : records) {
					TcUserPO user = new TcUserPO();
					if(null!=bean.getNmCreateuser()){
						user.setUserId(bean.getNmCreateuser());
						List<PO> poList = nmDao.select(user);
						if(!CommonUtils.isNullList(poList)){
							user = (TcUserPO) poList.get(0);
							bean.setNmCreateUserName(user.getName());
						}
					}
					if(null!=bean.getNmTartype()){
						CommonEnum.TarTypeEnum ttEnum = CommonEnum.TarTypeEnum.getTarTypeEnum(bean.getNmTartype());
						if(null!=ttEnum){
							bean.setNmTartypeDesc(ttEnum.name());
						}
					}
				}
			}
			ActionUtil.setCustomPageSizeFlag(act, true);
			act.setOutData("ps", datas);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "消息提醒模版管理记录");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 进到编辑页面
	 * 
	 * @author chenyub@yonyou.com
	 */
	public void toAddNotice(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String id = ActionUtil.getParamValue(request, "nmId");
			NoticeModelBean bean = null;
			if(!StringUtils.isEmpty(id)){
				TmNoticemodelPO po = new TmNoticemodelPO();
				po.setId(id);
				NoticeModelDAO nmDAO = new NoticeModelDAO();
				PageResult<NoticeModelBean> pr = nmDAO.queryNoticeModel(po, 1, ActionUtil.getCurPage(request));
				List<NoticeModelBean> list = pr.getRecords();
				if(!CommonUtils.isNullList(list)){
					bean = list.get(0);
					act.setOutData("nmBean", bean);
					act.setOutData("fieldList", getTableComments(bean.getNmMenuid()));
				}
			}
			act.setOutData("tarTypeList", CommonEnum.TarTypeEnum.getTarTypeEnumList());
			act.setOutData("noticeTypeList", CommonEnum.NoticeTypeEnum.getNoticeTypeEnumList());
			act.setOutData("relationList", CommonEnum.RelationsEnum.getRelationsEnumList());
			act.setForword(toAddUrl);
			act.setOutData("loginUser", logonUser);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "消息提醒模板管理进入新增");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询可提醒表
	 * 
	 * @author chenyub@yonyou.com
	 */
	public void getMenuList(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String isFunc = ActionUtil.getParamValue(request, "is_func");
			String funcId = ActionUtil.getParamValue(request, "queryFuncId");
			String funcName = ActionUtil.getParamValue(request, "funcName");
			List<Map<String, Object>> pageList = MenuDAO.getFuncList(isFunc,funcId,funcName,true);
			PageResult<Map<String, Object>> pageResult = new PageResult<Map<String,Object>>();
			pageResult.setCurPage(1);
			pageResult.setPageSize(Constant.PAGE_SIZE_MAX);
			pageResult.setRecords(pageList);
			pageResult.setTotalPages(1);
			pageResult.setTotalRecords(pageList.size());
			act.setForword(toAddUrl);
			act.setOutData("loginUser", logonUser);
			act.setOutData("ps", pageResult);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "消息提醒模板管理选择菜单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 查询指定表的字段信息
	 * 
	 * @author chenyub@yonyou.com
	 */
	public void queryTableField(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String menuId = ActionUtil.getParamValue(request, "selectMenuId");
			List<Map<String, Object>> tableComments = getTableComments(menuId);
			act.setOutData("comments", tableComments);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "消息提醒模板管理查询表字段");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 保存消息提醒模版
	 * 
	 * @author chenyub@yonyou.com
	 */
	public void saveNoticeModel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			TmNoticemodelPO bean = BeanUtils.getSubmitBean(request, new TmNoticemodelPO());
			NoticeModelDAO nmDao = new NoticeModelDAO();
			if(null!=bean){
				boolean isCreate = false;
				if(StringUtils.isEmpty(bean.getId())){
					bean.setId(UUID.randomUUID().toString().replaceAll("-", ""));
					isCreate = true;
				}
				if(StringUtils.isEmpty(bean.getNmModelstate())){
					bean.setNmModelstate("0");
				} else {
					bean.setNmModelstate("1");
				}
				if(isCreate){
					if(null==bean.getNmCreatetime()){
						bean.setNmCreatetime(new Date());
					}
					if(null==bean.getNmCreateuser()){
						bean.setNmCreateuser(logonUser.getUserId());
					}
					nmDao.insert(bean);
				}else {
					if(null==bean.getNmUpdatetime()){
						bean.setNmUpdatetime(new Date());
					}
					if(null==bean.getNmUpdateuser()){
						bean.setNmUpdateuser(logonUser.getUserId());
					}
					TmNoticemodelPO oldPO = new TmNoticemodelPO();
					oldPO.setId(bean.getId());
					nmDao.update(oldPO,bean);
				}
			}
			noticeModelIndex();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "消息提醒模板管理查询表字段");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询表的注释
	 * @param menuId
	 * @return
	 * @author chenyub@yonyou.com
	 */
	private List<Map<String, Object>> getTableComments(String menuId){
		TcFuncPO tcPO = new TcFuncPO();
		tcPO.setFuncId(NumberUtils.toLong(menuId));
		List<TcFuncPO> list = MenuDAO.getFuncsExtend(tcPO,false);
		if(!CommonUtils.isNullList(list)){
			return new LinkedList<Map<String,Object>>();
		} else if(list.size()>1){
			throw new RuntimeException("查询到多条数据,请精确查询!");
		}
		String tableName = list.get(0).getFuncTablename();
		List<Map<String, Object>> tableComments = MenuDAO.getTableInfo(tableName);
		return tableComments;
	}

}
