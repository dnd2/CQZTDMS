package com.infodms.dms.actions.sysmng.notice.dealerNotice;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.DealerNoticeBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.menu.MenuDAO;
import com.infodms.dms.dao.notice.dealerNotice.DealerNoticeDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcFuncPO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.enums.CommonEnum;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;


/**
 * 消息提醒action
 * @author chenyu
 *
 */
public class DealerNoticeAction {
	public Logger logger = Logger.getLogger(DealerNoticeAction.class);
	private final String queryUrl = "/jsp/notice/dealerNotice/dealerNoticeList.jsp";
	
	/**
	 * 进入消息提醒页面
	 * 
	 * @author chenyub@yonyou.com
	 */
	public void dealerNoticeIndex(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			TcFuncPO po = new TcFuncPO();
			po.setIsFunc(1);
			act.setOutData("noticeMenuList", MenuDAO.getFuncsExtend(po,true));
			act.setOutData("noticeStateList", CommonEnum.NoticeHandleStateEnum.getNoticeHandleStateEnumList());
			act.setOutData("noticetypeList", CommonEnum.NoticeTypeEnum.getNoticeTypeEnumList());
			act.setForword(queryUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "消息提醒查询列表页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询消息提醒记录
	 * 
	 * @author chenyub@yonyou.com
	 */
	public void queryNotice(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			DealerNoticeDAO nmDAO = new DealerNoticeDAO();
			Integer pageSize = ActionUtil.getPageSize(request);
			Integer curPage = ActionUtil.getCurPage(request);
			DealerNoticeBean bean = new DealerNoticeBean();
			bean.setFuncId(ActionUtil.getParamValue(request, "menuId"));
			String state = ActionUtil.getParamValue(request, "dnHandlestate");
			if(StringUtils.isEmpty(state)){
				state = "0";
			}
			bean.setDnHandlestate(state);
			bean.setDnDealerid(logonUser.getDealerId());
			bean.setDnTarid(String.valueOf(logonUser.getUserId()));
			bean.setNmDealertype(ActionUtil.getParamValue(request, "nmNoticetype"));
			bean.setTargetUserId(bean.getDnTarid());
			bean.setTargetPositionId(String.valueOf(logonUser.getPoseId()));
			PageResult<Map<String, Object>> datas = nmDAO.queryNoticeModelMap(bean,pageSize,curPage);
			act.setOutData("ps", datas);
			if(null!=datas&&datas.getTotalRecords()>0){
				act.setOutData("noticeSize", datas.getTotalRecords());
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "查询消息提醒记录");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void updateNoticeState(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			DealerNoticeDAO nmDAO = new DealerNoticeDAO();
			String flag = "faiture";
			String id = ActionUtil.getParamValue(request, "noticeId");
			/*
			// 查看消息提醒类型和状态
			DealerNoticeBean bean = new DealerNoticeBean();
			bean.setId(id);
			PageResult<DealerNoticeBean> pr = nmDAO.queryNoticeModelObject(bean, 1, Constant.PAGE_SIZE_MAX);
			if(null!=pr&&CollectionUtils.isNotEmpty(pr.getRecords())){
				List<DealerNoticeBean> beans = pr.getRecords();
				if(beans.size()==1){
					bean = beans.get(0);
				}
			}
			*/
			nmDAO.updateNoticeState(id, "1", logonUser);
			act.setOutData("flag", flag);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "查询消息提醒记录");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
