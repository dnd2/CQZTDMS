<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); 
int scan = (Integer)request.getAttribute("isScan");//取得后台传来的值得，用于判断在新增的时候,VIN输入框是否能够手动输入%>
<script type="text/javascript">
	/**
	以下方法是在VIN输入时进行判断，如果没有主机厂授权，无法手动输入VIN号。只能扫描进去
	*/
var arrvins = new Array();
function get(){
if(<%=scan%> ==0 ){
	var iKeyCode = window.event.keyCode;
	if(iKeyCode != 46 && iKeyCode != 8) {
	   var ddate = new Date();
	   arrvins[arrvins.length] = ddate.getTime();
	}
	}
	return true;
}	

	function add(){
	$('pin').value="";
		var vinNo = $('vinNo').value;
		  if(<%=scan%>==0){
	    	var time = arrvins[arrvins.length-1]-arrvins[0];
			arrvins = new Array();
			if(time > 500){
				document.getElementById("vinNo").value = "";
				MyAlert("你不能手动输入VIN!");
				return false;
			}
	    }
		if (vinNo==null||vinNo==''||vinNo=='null') {
			MyAlert('VIN不能为空！');
			return;
		}
		var vin = vinNo;
	if(vin.length ==18){
       vin = vin.substring(0,vin.length-1);
        var strAscii = new Array();//用于接收ASCII码
		var res = "";
		for(var i = 0 ; i < vin.length ; i++ ){
		strAscii[i] = parseInt(vin.charCodeAt(i))-1;//只能把字符串中的字符一个一个的解码
		res+=String.fromCharCode(strAscii[i]);  //完成后，转化为为字母或者数字
		}
		document.getElementById("vinNo").value=res;
		vin = res;
        }
		MyConfirm('此次查询将会被记录,确定查询?',addConfirm);
	}
	function addConfirm(){
	$("commitBtn").disabled = true;
		var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/pinSearch2.json';
		makeNomalFormCall(url,showPinNo,"fm");
	}
	function showPinNo(obj){
		var pNo = obj.pNo;
		if(pNo!=null&&pNo!=""){
		MyAlert("查询成功!点击确定将PIN码显示到界面.");
		$("commitBtn").disabled = false;
		$('pin').value=pNo;
		}else{
			$("commitBtn").disabled = false;
			$('pin').value="";
			MyAlert("没有找到该车的PIN码或者VIN不存在,请联系管理员");
		}
	}
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;车辆PIN查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">

    <tr>
       <td class="table_query_2Col_label_6Letter">VIN：</td>
      	<td><input name="vinNo" type="text" id="vinNo" <% if(scan ==0){%> 
							 onkeydown="get()" onpaste="return false"
				<%  } %> datatype="0,is_vin" class="middle_txt"/></td>
    </tr>
    	<tr>
      	<td class="table_query_2Col_label_6Letter">PIN码：</td>
      	<td><input name="pin" type="text" id="pin" readonly="readonly"  class="middle_txt"/>
      	<span style="color: red;">注：此处为PIN显示区域</span>
      	</td>
      
    </tr>
    <tr>
    	<td align="center" colspan="6">
    		<input type="button" name="BtnAdd" id="commitBtn"  value="查询"  class="normal_btn" onClick="add()" >
    	</td>
    </tr>
</table>
</body>
</html>