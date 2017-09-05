<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件仓库维护-查询</title>
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartWareHouse/partWareHouseQuery.json";
	var title = null;
	var columns = [
	            {header: "序号", align: 'center', renderer: getIndex, width: '7%'},
                {id: 'action', header: "操作", sortable: false, dataIndex: 'WH_ID', renderer: myLink, align: 'center'},
		        {header: "仓库编码", dataIndex: 'WH_CODE',  style: 'text-align:left'},
		        {header: "仓库名称", dataIndex: 'WH_NAME',  style: 'text-align:left'},
		        {header: "仓库类型", dataIndex: 'WH_TYPE',  style: 'text-align:left', renderer: getItemValue},
		        {header: "联系人", dataIndex: 'LINKMAN',  style: 'text-align:left'},
		        {header: "联系电话", dataIndex: 'TEL',  style: 'text-align:left'},
		        {header: "修改日期", dataIndex: 'UPDATE_DATE', align: 'center'},
		        {header: "是否有效", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
		      ];
//设置超链接  begin      
	
	//设置超链接
	function myLink(value,meta,record){    
        var state = record.data.STATE;
        if(state=='<%=Constant.STATUS_DISABLE %>'){
            return String.format("<a href=\"#\" onclick='valid(\""+value+"\")'>[有效]</a>");
        }	    
  		return String.format("<a href=\"#\" onclick='sel(\""+value+"\")'>[修改]</a>&nbsp;<a href=\"#\" onclick='cel(\""+value+"\")'>[失效]</a>");
	}
	
	//仓库修改页面
	function sel(value)
	{
		btnDisable();
		window.location.href = '<%=contextPath %>/parts/baseManager/partsBaseManager/PartWareHouse/partTypeUpdateInit.do?Id=' + value;
	}
	//失效
	function cel(value){
	    MyConfirm("确定要失效?",celAction,[value]);
	}
	function celAction(value){
		btnDisable();
	    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartWareHouse/partNotState.json?Id='+value+'&curPage='+myPage.page;
	    makeNomalFormCall(url,handleCel,'fm');
	}
	//有效
	function valid(value){
	     MyConfirm("确定要有效?",validAction,[value]);
	}
	function validAction(value){
		btnDisable();
	    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartWareHouse/partStateEnable.json?Id='+value+'&curPage='+myPage.page;
	    makeNomalFormCall(url,handleCel,'fm');
	}
	
	function handleCel(jsonObj) {
		btnEnable();
	    if(jsonObj!=null){
	       var success = jsonObj.success;
	       MyAlert(success);
	       __extQuery__(jsonObj.curPage);
	    }
   }
	function partAdd(){
		btnDisable();
		window.location.href = '<%=contextPath %>/parts/baseManager/partsBaseManager/PartWareHouse/partWareHouseAddInit.do';
	}
	$(document).ready(function(){
		__extQuery__(1);
	});
</script>
</head>
<body> <!-- onunload='javascript:destoryPrototype()' -->
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
		配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件仓库维护
</div>
<form method="post" name ="fm" id="fm">
<div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	<table class="table_query">
	    <tr>
	      <td class="right" >仓库编码：</td>
	      <td  ><input class="middle_txt" type="text" name="WH_CODE"/></td>
	      <td class="right"  >仓库名称：</td>
	      <td  ><input class="middle_txt" type="text" name="WH_NAME"/></td>
	      <td class="right" >仓库类型：</td>
	      <td >
	         <script type="text/javascript">
		       genSelBoxExp("partsWareHouseType",<%=Constant.PARTS_WAREHOUSE_TYPE %>,"",true,"u-select","","false",'');
		     </script>
	     </td>
	      <td class="right" >是否有效：</td>
	      <td>
	         <script type="text/javascript">
		       genSelBoxExp("STATE",<%=Constant.STATUS %>,"<%=Constant.STATUS_ENABLE %>",true,"u-select","","false",'');
		     </script>
	     </td>
      </tr>
      <tr>
		 <td class="center" colspan="8">
		  <input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onClick="__extQuery__(1)"/>
		  &nbsp;&nbsp;
		  <input class="u-button" type="button" value="新 增" onclick="partAdd();"/>
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