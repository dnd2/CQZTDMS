<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%String contextPath = request.getContextPath();%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>旧件回运申请补录</title>
<script type="text/javascript">
	function doInit(){
	   loadcalendar();
	}
</script>
</head>
<body onload="doInit()">
 <form method="post" name ="fm" id="fm">
  <input type="hidden" name="RETURN_APPLY_ID" id="RETURN_APPLY_ID" value="${RETURN_APPLY_ID }" /><!-- 回运单申请ID -->
  <input type="hidden" name="COMMAND" id="COMMAND" value="1" />
  <table width="100%">
  <tr>
  	<td>
  	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理 &gt;索赔旧件管理 &gt;旧件回运申请补录</div>
  	</td>
  </tr>
  <tr>
  	<td>
  <table class="table_edit">
          <tr>
	         <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
          </tr>
          <tr>
          	<td align="right" nowrap="nowrap">货运单号：</td>
          	<td align="left">
          		<input type="text" id="SEND_NO" name="SEND_NO" value="" class="long_txt"  />
          	</td>
          	<td align="right" nowrap="nowrap">发运日期：</td>
       		<td align="left">
       		<input name="SEND_DATE" type="text" class="short_time_txt" datatype="0,is_null" value="" id="SEND_DATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'SEND_DATE', false);" />  	
       			</td>
          </tr>
          <tr>
	          <td align="right" nowrap="nowrap">货运联系人：</td>
	            <td align="left">
	            	<input type=text name="SEND_LINK_USER" class="middel_txt" value=""/>
	            </td>
	            <td align="right" nowrap="nowrap">联系人手机：</td>
	            <td align="left">
	               <input type="text" id="SEND_LINK_PHONE" datatype="1,is_phone,11" name="SEND_LINK_PHONE" class="middel_txt" value=""/>
	            </td>
          </tr>
  </table>
  </td></tr>
	<tr><td>	  
     <table class="table_list">
       <tr > 
         <td height="12" align="center">
          <input id="a11111" type="button" onclick="saveConfirm();" class="normal_btn" style="width=8%" value="确定"/>
           &nbsp;&nbsp;
          <input id="b11111" type="button" onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/>
         </td>
       </tr>
     </table>
      </td></tr>
  </table>
     <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
function backUp(){
	location.href="<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/returnOrder.do";
}
function saveConfirm(){
	if(submitForm(fm)==false){
		return;
	}
	MyConfirm("确定补录吗?",save,[]);
}
function save(){
	fm.action = "<%=contextPath%>/claim/oldPart/ClaimOldPartReturnApplyManager/oldpartReturnApplyTran.do";
	fm.submit();
}

function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
 var obj = document.getElementById("freight_type");
  if(obj){
   		obj.attachEvent('onchange',getTypeChangeStyleParam);//
   	}
   function getTypeChangeStyleParam() {
   		getTypeChangeStyle(obj.value);
   	}
   	function getTypeChangeStyle(obj) {
    	if(obj=='<%=Constant.OLD_RETURN_STATUS_03%>') {//自送
    		document.getElementById("que").style.display = 'none';
    		document.getElementById("que2").style.display = '';
    		document.getElementById("TRANSPORT_NO").setAttribute("readOnly","true")
    		document.getElementById("TRANSPORT_NO").value="0000";
    	}else {
    		document.getElementById("que").style.display = '';
    		document.getElementById("que2").style.display = 'none';
    		document.getElementById("TRANSPORT_NO").value='${poValue.transportNo }';
    		document.getElementById("TRANSPORT_NO").setAttribute("readOnly","")
    	}
    	}
</script>
</body>
</html>