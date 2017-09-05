package com.infodms.dms.actions.customerRelationships.reviewmanage;

import java.util.HashMap;
import java.util.LinkedList;

import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.ReviewManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class ReviewSearch {

	
	private static Logger logger = Logger.getLogger(ReviewSearch.class);
	private static final String questionManageInit="/jsp/customerRelationships/reviewmanage/questionSearch.jsp";
	private static final String seeQuestionair="/jsp/customerRelationships/reviewmanage/seeQuestionair.jsp";
	/**
	 * 
	 * @Title      : 
	 * @Description: 问卷管理 初始页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-21
	 */
	public void questionManageInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			act.setForword(questionManageInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"问卷管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 问卷管理 页面查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-21
	 */
	public void questionairManageQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String QR_TYPE=CommonUtils.checkNull(request.getParamValue("QR_TYPE"));
			String checkSDate=CommonUtils.checkNull(request.getParamValue("checkSDate"));
			String checkEDate=CommonUtils.checkNull(request.getParamValue("checkEDate"));
			String QR_STATUS=CommonUtils.checkNull(request.getParamValue("QR_STATUS"));
			String QR_NAME=CommonUtils.checkNull(request.getParamValue("QR_NAME"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			PageResult<Map<String, Object>> ps = null;
			ps=dao.questionairManageQuery(Constant.PAGE_SIZE, curPage, QR_TYPE,checkSDate,checkEDate,QR_STATUS,QR_NAME);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"问卷管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}


	/**
	 * 
	 * @Title      : 浏览问卷
	 * @Description: TODO 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void seeQuestionair(){
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			//获得问卷ID
			String QR_ID=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			Map<String,LinkedList<Map<String,Object>>> result = new HashMap();
			//根据ID查询 问题及答案
			result=dao.questionairQuery(QR_ID);
			String questionhtml=generateQuestion3(result.get("questions"));
			act.setOutData("result", result);
			act.setOutData("questionhtml", questionhtml);
			act.setForword(seeQuestionair);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 浏览问卷");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
		
	}
	
	public String generateQuestion3(LinkedList<Map<String,Object>> questionIinfo)
	{
		StringBuilder qh = new StringBuilder();
		qh.append("");
		int i=1;
		for(Map<String, Object> q:questionIinfo)
		{
			
			qh.append(" <tr >");
			qh.append("<td  align=\"center\"><span >问题 :"+q.get("QD_NO")+"</span></td>");
			qh.append(" <td  align=\"left\"><span style=\"DISPLAY:inline-block; WIDTH: 90%\">"+q.get("QD_QUESTION")+"</span><br />");
			qh.append(" <table  border=\"0\"> <tbody> <tr>");
			if(Constant.QD_QUE_TYPE_1==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
				String[]radiocheck=q.get("QD_CHOICE").toString().split("\\|");
				for(int k=0;k<radiocheck.length;k++)
				{
					qh.append("<td><input id=\"q1"+q.get("QD_NO")+"\" type=\"radio\" value=\""+radiocheck[k]+"\" name=\"ql"+q.get("QD_NO")+"\"/><label>"+radiocheck[k]+"</label></td>");
				}
			}else if(Constant.QD_QUE_TYPE_2==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
				String[]radiocheck=q.get("QD_CHOICE").toString().split("\\|");
				for(int k=0;k<radiocheck.length;k++)
				{
					qh.append("<td><input id=\"q1"+q.get("QD_NO")+"\" type=\"checkbox\"  value=\""+radiocheck[k]+"\" name=\"ql"+q.get("QD_NO")+"\"/><label>"+radiocheck[k]+"</label></td>");
				}
			}else if(Constant.QD_QUE_TYPE_3==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
			
				int width=0;
				int hight=20;
				if(Constant.qd_txt_type_1==Integer.parseInt(q.get("QD_TXT_TYPE").toString()))
				{
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
				}else if(Constant.qd_txt_type_2==Integer.parseInt(q.get("QD_TXT_TYPE").toString())){
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
					hight=Integer.parseInt(q.get("ED_TXT_HIGHT").toString());
				}
				qh.append("<td><input id=\"q1"+q.get("QD_NO")+"\" type=\"text\"   value=\"\" name=\"ql"+q.get("QD_NO")+"\" style='WIDTH: "+width+"px; height:"+hight+"px;'/></td>");
			}else if(Constant.QD_QUE_TYPE_4==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
				int width=600;
				int hight=20;
				if(Constant.qd_txt_type_1==Integer.parseInt(q.get("QD_TXT_TYPE").toString()))
				{
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
				}else if(Constant.qd_txt_type_2==Integer.parseInt(q.get("QD_TXT_TYPE").toString())){
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
					hight=Integer.parseInt(q.get("ED_TXT_HIGHT").toString());
				}
				qh.append("<td><textarea id=\"q1"+q.get("QD_NO")+"\" type=\"text\"  name=\"ql"+q.get("QD_NO")+"\" style='width: "+width+"px; height:"+hight+"px;' /></textarea></td>");
			}else{
				qh.append("");
			}
			qh.append("</tr></tbody> </table></td>");
			qh.append("</tr>");
			i++;
		}
		return qh.toString();
	}
	
	
}
