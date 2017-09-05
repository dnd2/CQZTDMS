<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.bean.AclUserBean"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
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
	var myPage;
	var url = "<%=contextPath%>/sysmng/orgmng/MaterailPriceMng/materailPriceSuccess.json";
	
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
    
	function clrTxt1(valueId) {
		document.getElementById(valueId).value = '' ;
		document.getElementById("dealerCode").value = '' ;
	}
	
    //开票日期内容清空
	function clrTxt(valueId) {
		document.getElementById(valueId).value = '' ;
	}
    //查看信息
    function queryInvoice(value){
    	location = '<%=contextPath%>/sysmng/orgmng/MaterailPriceMng/materailPriceQueryShow.do?PRICE_ID='+value;
    }
	//查询条件重置
	function requery() {
		
		document.getElementById('purchaseInvoiceNo').value = '' ;
		document.getElementById('billDate').value = '' ;
		document.getElementById('ticketDeptName').value = '' ;
		document.getElementById('saleOrderNo').value = '' ;
		document.getElementById('dealerCode').value = '' ;
	}
	
	
	function doSave(){
		var fm = document.getElementById("fm");
		fm.action = "<%=contextPath%>/sysmng/orgmng/MaterailPriceMng/materailPriceImportSave.do";
    	fm.submit();	
	}
	
</script>  


</head>
<body onunload="javascript:destoryPrototype();" onload="__extQuery__(1);">
<div id="loader" style="position: absolute; z-index: 200; background-color: rgb(255, 204, 0); padding: 1px; top: 4px; left: 455px; display: none; background-position: initial initial; background-repeat: initial initial; "> 正在载入中... </div>

<title>物料价格管理</title>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif">&nbsp;当前位置：  系统管理 &gt; 组织管理&gt; 物料价格管理</div>	
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end --> 
    
	 <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="PRICE_ID"  value="${PRICE_ID}"/>
		<table class="table_query" border="0">
			<tr>
				<td class="center" colspan="6">
					<input type="hidden" name=invoiveId id="invoiveId"/>
					<input type="button" class="u-button u-query"  value="确定" onclick="doSave();"/>
					<input type="button" class="u-button u-reset"  value="返回" onclick="history.back();"/>
				</td>
			</tr>
		</table>
	</form>
	
</div>
  
</body>
</html>
