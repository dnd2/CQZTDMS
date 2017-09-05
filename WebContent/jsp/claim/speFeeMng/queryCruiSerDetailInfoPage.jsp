<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.infodms.dms.bean.CruiServiceDetailInfoBean"%>
<%@ page import="com.infodms.dms.bean.SpeFeeApproveLogListBean" %>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>巡航服务线路查询</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   String logonName = logonUser.getName();
   CruiServiceDetailInfoBean detailBean = (CruiServiceDetailInfoBean)request.getAttribute("detailBean");
   List<SpeFeeApproveLogListBean> logList = (LinkedList<SpeFeeApproveLogListBean>)request.getAttribute("logList");
%>
</head>
<script type="text/javascript">
function window.onload(){
	var retCode="<%=request.getAttribute("retCode")%>";
	if(retCode=="data_error"){
		_hide();
        MyDivAlert("查询巡航详细信息数据有误！");
    }
}
</script>
<BODY>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;特殊费用管理 &gt;巡航服务线路查询</div>
 <form method="post" name ="fm" id="fm">
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
    <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
    <tr bgcolor="F3F4F8">
      <td align="right">经销商代码：</td>
      <td>
        <%=detailBean.getDealer_code()%>
      </td>
      <td align="right">经销商名称：</td>
      <td>
        <%=detailBean.getDealer_name()%>
      </td>
      <td align="right">建单日期：</td>
      <td>
        <%=detailBean.getMake_date()%>
      </td>
    </tr>
    <tr bgcolor="F3F4F8">
       <td align="right">巡航目的地：</td>
       <td>
         <%=detailBean.getCr_whither()%>
       </td>
       <td align="right">巡航总里程：</td>
       <td>
         <%=detailBean.getCr_mileage()%>km
       </td>
       <td align="right">巡航天数：</td>
       <td>
         <%=detailBean.getCr_day()%>天
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">巡航负责人：</td>
       <td>
         <%=detailBean.getCr_principal()%>
       </td>
       <td align="right">巡航服务电话：</td>
       <td>
         <%=detailBean.getCr_phone()%>
       </td>
       <td align="right">巡航服务原因：</td>
       <td>
         <textarea id="crui_reason" name="crui_reason" rows="4" cols="30" disabled><%=detailBean.getCr_cause()%></textarea>
       </td>
     </tr>
  </table>
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <th colspan="12"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />审批明细
     </th>
     <tr bgcolor="F3F4F8">
       <th align="center">审批日期</th>
       <th align="center">审批人员</th>
       <th align="center">人员部门</th>
       <th align="center">审批状态</th>
       <th align="center">审批意见</th>
     </tr>
      <%
      if(logList!=null){
    	  for(int count=0;count<logList.size();count++){
    		  SpeFeeApproveLogListBean logInfo=new SpeFeeApproveLogListBean();
    		  logInfo=(SpeFeeApproveLogListBean)logList.get(count);
        	  
          %>
          <tr>
           <td>
             <%=CommonUtils.checkNull(logInfo.getAuditing_date())%>
           </td>
           <td>
             <%=CommonUtils.checkNull(logInfo.getUser_name())%>
           </td>
           <td>
             <%=CommonUtils.checkNull(logInfo.getDept_name())%>
           </td>
           <td>
             <%=CommonUtils.checkNull(logInfo.getAudit_status())%>
           </td>
           <td>
            <textarea rows="1" cols="30" readonly="readonly"><%=CommonUtils.checkNull(logInfo.getAuditing_opinion())%></textarea>
           </td>
          </tr>
          <%
    	  }
      }%>
       
  </table>
  <table class="table_list">
    <tr > 
      <td height="12" align=center>
       <input type="button" onclick="_hide();" class="normal_btn" style="width=8%" value="关闭"/>
      </td>
    </tr>
  </table>   
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
</BODY>
</html>
