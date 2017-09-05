<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@page import="com.infoservice.mvc.context.ActionContext" %>
<%@page import="com.infodms.dms.bean.AclUserBean" %>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>巡航服务线路申请</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   String logonName = logonUser.getName();
%>
</head>
<body>
 <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;特殊费用管理 &gt;巡航服务线路申请</div>
 <form method="post" name ="fm" id="fm">
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <tr>
       <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">经销商代码：</td>
       <td><%=CommonUtils.checkNull(request.getAttribute("deal_code"))%></td>
       <td align="right">经销商名称：</td>
       <td><%=CommonUtils.checkNull(request.getAttribute("deal_name"))%></td>
       <td height="27" align="right" bgcolor="FFFFFF">省份：</td>
       <td><%=CommonUtils.checkNull(request.getAttribute("privince_name"))%></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">巡航目的地：</td>
       <td>
         <input type="text" name="crui_aim" id="crui_aim" value="" datatype="0,is_letter_cn,15" class="middle_txt"/>
       </td>
       <td align="right">巡航总里程：</td>
       <td>
         <input type="text" name="crui_km" id="crui_km" value="" datatype="0,isMoney,10" class="middle_txt"/>(km)
       </td>
       <td align="right">巡航天数：</td>
       <td>
         <input type="text" name="crui_days" id="crui_days" value="" datatype="0,isDigit,5" class="middle_txt"/>(天)
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">巡航负责人：</td>
       <td>
         <input type="text" name="crui_man" id="crui_man" value="" datatype="0,is_letter_cn,10" class="middle_txt"/>
       </td>
       <td align="right">巡航服务电话：</td>
       <td>
         <input type="text" name="crui_phone" id="crui_phone" value="" datatype="0,is_phone,13" class="middle_txt"/>
       </td>
       <td align="right">巡航服务原因：</td>
       <td>
         <textarea id="crui_reason" name="crui_reason" rows="4" cols="30" datatype="0,is_null,60"></textarea>
       </td>
     </tr>
  </table>
  <table class="table_list">
    <tr > 
      <th height="12" align=center>
       <input type="button" onclick="save();" class="normal_btn" style="width=8%" value="保存"/>
        &nbsp;&nbsp;
       <input type="button" onclick="askClose();" class="normal_btn" style="width=8%" value="关闭"/></th>
    </tr>
  </table>
  <!-- 资料显示区结束 -->
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
</body>
<script type="text/javascript">
//询问是否离开
function askClose(){
	MyDivConfirm("确认离开？",closeMe,"");
}
function closeMe(){
	parent.window._hide();
}
//保存信息
function save(){
	if(document.getElementById("crui_reason").value.length>60){
       MyAlert("巡航服务内容不能超过60字！");
       return;
	}
	if(!submitForm('fm')) {
		return false;
	}
	MyDivConfirm("确认保存？",ok,"");
}
function ok(){
	var submit_url="<%=contextPath%>/claim/speFeeMng/CruiServicePathApplyManager/saveNewCuriServiceInfo.json";
	makeNomalFormCall(submit_url,afterCall,'fm','createOrdBtn');
}
//回调函数处理
function afterCall(json){
	var retCode=json.retCode;
	if(retCode=="save_success"){
       //MyAlert("保存成功！");
       parent.window._hide();
       parentContainer.refreshPage();
	}else if(retCode=="save_failure"){
       MyDivAlert("保存失败！");
	}else if(retCode=="save_failure_001"){
       MyDivAlert("无法获得登陆参数，保存失败！");
	}
}
</script>
</html>