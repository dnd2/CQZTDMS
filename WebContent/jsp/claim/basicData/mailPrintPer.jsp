<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时单价设定</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;服务站信封打印</div>
<form name='fm' id='fm' method="post">
<table class="table_edit" >
	<tr>
 		<td width="10%" align="right">经销商代码：</td>
 		<td width="20%"><input type="text" class="middle_txt" name="dealer_code"/></td>
		<td width="10%" align="right">经销商名称：</td>
		<td width="20%"><input type="text" class="middle_txt" name="dealer_name"/></td>
	</tr>
	<tr>
 		<td colspan="4" align="center">
 			<input class="normal_btn" type="button" value="查询" onclick="__extQuery__(1);"/> 
 		</td>
	</tr>
</table>
<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

</form>
<script type="text/javascript">
	var myPage;
	var url = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/mailPrintPerQuery.json' ;
	
	var title = null ;

	var columns = [
	               {header:'序号',width:'8%',align:'center',renderer:getIndex},
	               {header:'经销商代码',width:'12%',align:'center',dataIndex:'DEALER_CODE'},
	               {header:'经销商名称',width:'14%',align:'center',dataIndex:'DEALER_NAME'},
	               {header: "打印",sortable: false,dataIndex: 'DEALER_ID',renderer:myLink ,align:'center'}
		           	];
		__extQuery__(1);
	function myLink(value,meta,record){
	var str = "";
	
	str+= "<a href=\"#\" onclick='mail(\""+value+"\")'>[信封]</a><a href=\"#\" onclick='ems(\""+value+"\")'>[EMS]</a>"
	    return String.format(str  );
	}
	function mail(id){
	var str = window.showModalDialog('<%=contextPath%>/jsp/claim/basicData/mailInputInfo.jsp',id,'dialogWidth=600px;dialogHeight=400px');
		if(str!="" && str !=undefined){
		MyConfirm("确认打印？",confirmAdd1,[id,str]);
	  }
	}
	function confirmAdd1(id,str){
	window.open('<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/mailPrintDo.do?type=1&id='+id+"&dept="+encodeURI(str.split("$")[0])+"&remark="+encodeURI(str.split("$")[1]),"信封打印", "height=1000, width=1000, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	}
	function confirmAdd0(id,str){
	window.showModalDialog("<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/mailPrintDo.do?type=2&id=" + id+"&dept="+encodeURI(str.split("$")[0])+"&remark="+encodeURI(str.split("$")[1]), "ems打印", 'edge: Raised; center: Yes; help: Yes; resizable: Yes; status: No;dialogHeight:540px;dialogWidth:850px');
		
	}
	function ems(id){
	var str = window.showModalDialog('<%=contextPath%>/jsp/claim/basicData/mailInputInfo.jsp',id,'dialogWidth=600px;dialogHeight=400px');
		if(str!="" && str !=undefined){
		MyConfirm("确认打印？",confirmAdd0,[id,str]);
	  }
	}
</script>
</body>
</html>
