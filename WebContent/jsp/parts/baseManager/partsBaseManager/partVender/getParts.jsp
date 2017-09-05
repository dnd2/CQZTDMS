<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>获取配件信息</title>
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartAndVenderManager/getPartInfos.json";
	
	var title = null;
	var columns = [
	            {header: "序号",width:'5%',  renderer:getIndex},
	            {header: '选择', dataIndex: 'PART_ID' , width:'5%',  align:'center', renderer:setLink},
	            {header: "编号", dataIndex: 'PART_OLDCODE', align:'center'},
	            {header: "名称", dataIndex: 'PART_CNAME', align:'center'},
	            {header: "件号", dataIndex: 'PART_CODE', align:'center'},
	            {header: "名称(英)", dataIndex: 'PART_ENAME', align:'center'}
	      ];

	//设置超链接  begin
	//设置单选按钮
	function setLink(value, meta, record){
		var id = record.data.PART_ID;
		var oldcode = record.data.PART_OLDCODE;
		var name = record.data.PART_CNAME;
		var code = record.data.PART_CODE;
		var ename = record.data.PART_ENAME;
		var str = '<input type="radio" name="rad" onclick="ckeckRadio(\''+code+'\',\''+name+'\')" />';
		if('<%=request.getParameter("flag")%>'=='partAndVender'){
			str = '<input type="radio" name="rad" onclick="ckeckRadio2(\''+id+'\',\''+oldcode+'\',\''+name+'\',\''+code+'\')" />';
		}
		if('<%=request.getParameter("flag")%>'=='partInternational'){
			str = '<input type="radio" name="rad" onclick="ckeckRadio3(\''+id+'\',\''+oldcode+'\',\''+name+'\',\''+code+'\',\''+ename+'\')" />';
		}
		return str;
		
	}
	//选择单选按钮
	function ckeckRadio(code,name){
		_hide();//隐藏选择
	}
	//选择单选按钮
	function ckeckRadio2(id,oldcode,name,code){
		__parent().setPartInfoke(id,oldcode,name,code);
		_hide();//隐藏选择
	}
	
	//选择单选按钮
	function ckeckRadio3(id,oldcode,name,code,ename){
		__parent().setPartInfoke(id,oldcode,name,code,ename);
		_hide();//隐藏选择
	}
	
	$(function(){__extQuery__(1);});
</script>

</head>
<body>
<div class="wbox">
<div class="navigation">
	<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：&gt;&nbsp;获取配件
</div>
<form method="post" name ="fm" id="fm" enctype="multipart/form-data">
    <div class="form-panel">
        <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
        <div class="form-body">
			<table class="table_query">
		        <tr>
			         <td class="right">配件编码：</td>
		             <td>
		                 <input class="middle_txt" type="text" name="PART_OLDCODE" value=""/>
		             </td>
		             <td class="right">配件名称：</td>
		             <td>
		                <input class="middle_txt" type="text"  name="PART_CNAME" value=""/>
		             </td>
		             <td class="right">配件件号：</td>
		             <td>
		                <input class="middle_txt" type="text"  name="PART_CODE" value=""/>
		             </td>
		        </tr>
			    <tr>
			    	<td  class="center" colspan="6" >
			    	  <input class="u-button u-query" type="button" value="查 询" onclick="__extQuery__(1)"/>
			    	  <input class="u-button u-submit" type="button" value="关闭" onclick="_hide()">
			       </td>    
			   </tr>
			</table>
		</div>
	</div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>
</body>
</html>