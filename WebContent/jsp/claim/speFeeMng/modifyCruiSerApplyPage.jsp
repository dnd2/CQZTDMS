<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@page import="com.infoservice.mvc.context.ActionContext" %>
<%@page import="com.infodms.dms.bean.AclUserBean" %>
<%@page import="com.infodms.dms.bean.CruiServiceDetailInfoBean"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>巡航服务线路申请</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   CruiServiceDetailInfoBean detailBean = (CruiServiceDetailInfoBean)request.getAttribute("detailBean");
%>
<script type="text/javascript">
function window.onload(){
	var retCode="<%=request.getAttribute("retCode")%>";
	if(retCode=="data_error"){
		_hide();
        MyDivAlert("查询巡航详细信息数据有误！");
    }
}
//修改信息
function save(){
	if(document.getElementById("crui_reason").value.length>60){
       MyAlert("巡航服务内容不能超过60字！");
       return;
	}
	if(!submitForm('fm')) {
		return false;
	}
	MyDivConfirm("确认修改？",ok,"");
}
function ok(){
	var submit_url="<%=contextPath%>/claim/speFeeMng/CruiServicePathApplyManager/modifyAndSaveCuriServiceInfo.json?ord_id="+<%=detailBean.getId()%>;
	makeNomalFormCall(submit_url,afterCall,'fm','createOrdBtn');
}
//回调函数处理
function afterCall(json){
	var retCode=json.retCode;
	if(retCode=="modify_success"){
	   MyDivConfirm("修改成功！",refreshParent,"");
	}else if(retCode=="modify_failure"){
       MyDivAlert("修改失败！");
	}
}
function refreshParent(){
	parent.window._hide();
	parentContainer.__extQuery__(1);
}
</script>
</head>

<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;特殊费用管理 &gt;巡航服务线路申请</div>
 <form method="post" name ="fm" id="fm">
	  <table class="table_edit">
	  <tr>
	    <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
	  </tr>
	    <tr>
	      <td class="table_edit_2Col_label_6Letter">经销商代码：</td>
	      <td align="left">
	        <%=detailBean.getDealer_code()%>
	      </td>
	      <td class="table_edit_2Col_label_6Letter">经销商名称：</td>
	      <td align="left">
	        <%=detailBean.getDealer_name()%>
	      </td>
	      <td class="table_edit_2Col_label_6Letter">建单日期：</td>
	      <td align="left">
	        <%=detailBean.getMake_date()%>
	      </td>
	    </tr>
	    <tr>
	       <td class="table_edit_2Col_label_6Letter">巡航目的地：</td>
	       <td align="left">
	         <input type="text" name="crui_aim" id="crui_aim"  datatype="0,is_letter_cn,15" class="short_txt" value="<%=detailBean.getCr_whither()%>"/>
	       </td>
	       <td class="table_edit_2Col_label_5Letter">巡航总里程：</td>
	       <td align="left">
	         <input type="text" name="crui_km" id="crui_km"  datatype="0,isMoney,10" class="short_txt" value="<%=detailBean.getCr_mileage()%>"/>km
	       </td>
	       <td class="table_edit_2Col_label_5Letter">巡航天数：</td>
	       <td align="left">
	         <input type="text" name="crui_days" id="crui_days"  datatype="0,isDigit,5" class="short_txt" value="<%=detailBean.getCr_day()%>"/>天
	       </td>
	     </tr>
	     <tr>
	       <td class="table_edit_2Col_label_6Letter">巡航负责人：</td>
	       <td align="left">
	         <input type="text" name="crui_man" id="crui_man"  datatype="0,is_letter_cn,10" class="short_txt" value="<%=detailBean.getCr_principal()%>"/>
	       </td>
	       <td class="table_edit_2Col_label_7Letter">巡航服务电话：</td>
	       <td align="left">
	         <input type="text" name="crui_phone" id="crui_phone" datatype="0,is_phone,13" class="short_txt" value="<%=detailBean.getCr_phone()%>"/>
	       </td>
	       <td class="table_edit_2Col_label_7Letter">巡航服务原因：</td>
	       <td align="left">
	      	 <textarea id="crui_reason" name="crui_reason" rows="4" cols="25" datatype="0,is_null,60"><%=detailBean.getCr_cause()%></textarea>
	       </td>
	     </tr>
	  </table>
	  <table class="table_list">
	    <tr> 
	      <th height="12" align=center>
	       <input type="button" onclick="save();" class="normal_btn" value="修改"/>
	        &nbsp;&nbsp;
	       <input type="button" onclick="_hide();parentContainer.__extQuery__(1);" class="normal_btn" style="width=8%" value="关闭"/></th>
	    </tr>
	  </table>   
	  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
	</form>
</body>
</html>