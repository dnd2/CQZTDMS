<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerPO"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){

	}

	function doSave(){
		var upfile = document.getElementById("upfile").value;
		if(upfile==null||upfile==''){
			alert("未选取文件");
			return;
		}
		document.getElementById("re_Id").disabled=true;
		var fm1 = document.getElementById("fm1");
		fm1.action = "<%=contextPath%>/sysmng/orgmng/MaterailPriceMng/importExport.do";
    	fm1.submit();
	}
	
	function doSaveBack(json){
		history.back();
	}
	
	function downloadFile(){
		var fm = document.getElementById("fm");
		fm.action = "<%=contextPath%>/sysmng/orgmng/MaterailPriceMng/materailPriceDeatailExport.do";

    	fm.submit();	
	}
	
	var myPage;
	var url = "<%=contextPath%>/sysmng/orgmng/MaterailPriceMng/materailPriceQueryDeatailShow.json";
	
	var title = null;
	var columns = [
				{header: "序号", align:'center', renderer:getIndex, width:'7%'},
				{header: "物料编码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "销售价格", dataIndex: 'SALES_PRICE', align:'center'},
		      ];      

	function myLink(value,meta,record){ 
        	 return String.format(
        		 "<a href=\"#\" onclick='queryInvoice(\""+ value +"\")'>[操作]</a>");
    }
	//查看信息
    function queryInvoice(value){
    	location = '<%=contextPath%>/sysmng/orgmng/MaterailPriceMng/materailPriceQueryShow.do?PRICE_ID='+value;
    }
	
	function getBack(){
		location = '<%=contextPath%>/sysmng/orgmng/MaterailPriceMng/materailPriceInit.do';
	}
</script>

<title>物料价格管理</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="doInit();__extQuery__(1);">
<div class="wbox"  id="wbox" >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 组织管理 &gt; 物料价格管理</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="PRICE_ID" id="PRICE_ID" value="${PRICE_ID}"/>
<div id="customerInfoId">

<table class="table_query table_list" class="center" >
	<tr class="tabletitle">
		<th colspan="4">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				价格信息<br/>
		</th>
	</tr>
	<tr>
		<td width="15%" class=right>价格代码：</td>
		<td  class=left>${PRICE_CODE}<input type="hidden" id="priceCode" name="priceCode" value="1" /></td>
		<td width="15%" class=right>价格描述：</td>
		<td  class=left>${PRICE_DESC}</td> 
	</tr>
	<tr>
		<td class=right>生效时间：</td>
		<td class=left>${START_DATE}</td>
		<td class=right>失效时间：<input type="hidden" id="modelCode" name="modelCode" value="1" /></td>
		<td class=left>${END_DATE}<input type="hidden" id="model_name" name="model_name" value="1" /></td>
	</tr>
	<tr>
		<td class=right>创建日期：</td>
		<td class=left>${CREATE_DATE}<input type="hidden" value="1 " id="groupId"/></td>
	</tr>
		
</table>
</div>
</form>
<form id="fm1" name="fm1" method="post" enctype="multipart/form-data">
<input type="hidden" name="PRICE_ID"  value="${PRICE_ID}"/>
<table class="table_query table_list" id="submitTable">

	<tr> 
		<td class="table_query_label" colspan="2">
			1、点“<font color="#FF0000">浏览</font>”按钮，找到您所要导入的的价格目标文件,请确定文件的格式为“<strong>物料编码—物料名称—销售价格</strong>”：&nbsp;&nbsp;&nbsp;</td>
    </tr>
	<tr>
	    <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp; 
	      <input type="file" name="uploadFile" datatype="0,is_null,2000" id="upfile" value="" />
	    </td>
    </tr>
    <td class="table_query_label" width="30%">2、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。</td>
	<tr >
		<td class="center">
			<input type="button" id="re_Id" value="确定" class="u-button u-submit" onclick="doSave();" /> 
			 <input type="button" class="u-button u-submit"  name="downloadBtn" value="导入模板下载" onclick="downloadFile()" />
			<input type="button" value="返 回"  class="u-button u-reset"  onclick="getBack();" /> 
		</td>
	</tr>
</table>

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end --> 
    

</form>
</div>
</body>
</html>