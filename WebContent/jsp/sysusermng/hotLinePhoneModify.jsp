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
<title>经销商热线修改</title>
<script type="text/javascript">
</script>
</head>
<body onload="">
 <form method="post" name ="fm" id="fm">
  <input type="hidden" name="dealer_id" id="dealer_id" value="${listBean[0].DEALER_ID }" /><!-- 回运单申请ID -->
  <input type="hidden" name="COMMAND" id="COMMAND" value="1" />
  <table width="100%">
  <tr>
  	<td>
  	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：个人信息管理 &gt;服务站人员及联系方式 &gt;经销商热线修改</div>
  	</td>
  </tr>
  <tr>
  	<td>
  <table class="table_edit">
          <tr>
	         <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
          </tr>
          <tr>
          	<td align="right" nowrap="nowrap">服务站代码：</td>
          	<td align="left">
          		${listBean[0].DEALER_CODE }
          	</td>
          	<td align="right" nowrap="nowrap">服务站名称：</td>
       		<td align="left">
       		${listBean[0].DEALER_SHORTNAME }  	
       			</td>
          </tr>
          <tr>
	          <td align="right" nowrap="nowrap">服务站热线：</td>
	            <td align="left">
	            	<input type="text" id="HOT_LINE_PHONE" datatype="1,is_phone,11" name="HOT_LINE_PHONE" class="middel_txt" value="${listBean[0].HOT_LINE_PHONE }"/>
	            </td>
	            <td align="right" nowrap="nowrap">&nbsp;</td>
	            <td align="left">
	               &nbsp;
	            </td>
          </tr>
  </table>
  </td></tr>
	<tr><td>	  
     <table class="table_list">
       <tr > 
         <td height="12" align="center">
          <input id="a11111" type="button" onclick="saveConfirm();" class="normal_btn" style="width=8%" value="保存"/>
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
	MyConfirm("确定修改吗?",save,[]);
}
function save(){
	var fm = document.getElementById('fm');
	fm.action="<%=contextPath%>/sysusermng/sysuserinfo/SysPasswordManager/saveDealerHotLinePhone.do";
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