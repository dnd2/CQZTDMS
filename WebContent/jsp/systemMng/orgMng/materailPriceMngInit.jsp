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
	var url = "<%=contextPath%>/sysmng/orgmng/MaterailPriceMng/materailPriceQuery.json";
	
	var title = null;
	var columns = [
				{header: "序号", align:'center', renderer:getIndex, width:'7%'},
				{id:'action',header: "操作", walign:'center',idth:70,dataIndex: 'PRICE_ID',renderer:myLink} ,
				{header: "价格代码", dataIndex: 'PRICE_CODE', align:'center'},
				{header: "价格描述", dataIndex: 'PRICE_DESC', align:'center'},
				{header: "生效时间", dataIndex: 'START_DATE', align:'center'},
				{header: "失效时间", dataIndex: 'END_DATE', align:'center'},
				{header: "创建日期", dataIndex: 'CREATE_DATE', align:'center'}
		      ];      

	function myLink(value,meta,record){ 
        	 return String.format(
        		 "<a href=\"#\" onclick='queryInvoice(\""+ value +"\")'>[修改]</a>");
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
	
	function wareHouseAdd(){
		 window.self.location = "<%=contextPath%>/sysmng/orgmng/MaterailPriceMng/materailPriceAdd.do";		 
	}
	
	function clearDate(){
		document.getElementById('priceCode').value = '' ;
		document.getElementById('pirceName').value = '' ;
		document.getElementById('startDate').value = '' ;
		document.getElementById('endDate').value = '' ;
		document.getElementById('createDate').value = '' ;
	}
	
</script>  


</head>
<body onload="__extQuery__(1);"> <!-- onunload="javascript:destoryPrototype();" -->
<div id="loader" style="position: absolute; z-index: 200; background-color: rgb(255, 204, 0); padding: 1px; top: 4px; left: 455px; display: none; background-position: initial initial; background-repeat: initial initial; "> 正在载入中... </div>

<title>车厂仓库维护</title>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif">&nbsp;当前位置：  系统管理 &gt; 组织管理&gt; 物料价格管理</div>
	<form id="fm" name="fm" method="post">

		<input type="hidden" name="dutyType" id="dutyType" value="${dutyType}"/>
		<div class="form-panel">
			<h2>查询条件</h2>
			<div class="form-body">	
		<table class="table_query" border="0">
			<tbody>
			<tr>
				<td width="12%" class="tblopt right">价格代码：</td>
				<td class="left" width="10%">
					<input type="text" class="middle_txt" id="priceCode" name="priceCode" value=""/>
				</td>
				
				<td width="12%" class="tblopt right">价格描述：</td>
				<td class="left" width="10%">
					<input type="text" class="middle_txt" id="pirceName" name="priceName" value=""/>
				</td>
				
				<td class="tblopt right">生效时间：</td>
				<td class="left">
      				<input name="startDate"  id="startDate" value="${date }" type="text" class="middle_txt"  style="width:100px" onFocus="WdatePicker({el:$dp.$('startDate'), maxDate:'#F{$dp.$D(\'payDate1\')}'})" />
		      			<input  id="payDate1"  type="hidden"   />
    			</td>
			</tr>
			<tr>
				<td width="12%" class="tblopt right">失效时间：</td>
				<td class="left">
      				<input name="endDate"  id="endDate" value="${date }" type="text" class="middle_txt"  style="width:100px" onFocus="WdatePicker({el:$dp.$('endDate'), maxDate:'#F{$dp.$D(\'payDate2\')}'})" />
		      			<input  id="payDate2"  type="hidden"   />
    			</td>
    			
				<td class="tblopt right" width="15%">创建日期：</td>
				<td class="left">
      				<input name="createDate"  id="createDate" value="${date }" type="text" class="middle_txt"  style="width:100px" onFocus="WdatePicker({el:$dp.$('createDate'), maxDate:'#F{$dp.$D(\'payDate3\')}'})" />
		      			<input  id="payDate3"  type="hidden"   />
    			</td>
			</tr>
			<tr>
				<td class="center" colspan="6">
					<input type="hidden" name=invoiveId id="invoiveId"/>
					<input type="button" class="u-button u-query"  value=" 查  询  " id="queryBtn" onclick="__extQuery__(1)"/> 
					<input type="button" class="u-button u-reset"  value="重 置" onclick="clearDate();"/>
					<input type="button" class="u-button u-submit"  value="新增" onclick="wareHouseAdd();"/>
				</td>
			</tr>
		</tbody>
	</table>
	</div>
	</div>
	 <!-- 查询条件 end -->
 	 <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end --> 
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
	
</div>

<div id="checkMsgDiv0" style="visibility: hidden; " class="tipdiv"> 
 <table width="120" height="28" border="0" cellpadding="0" cellspacing="0" id="checkMsgTable">    
  <tbody>
	 <tr>       
	 	<td valign="bottom"><img src="<%=contextPath%>/js/validate/alert_top.gif" width="120" height="6"></td>    
 	</tr>    
 	<tr>       
 		<td valign="top">          
 			<table width="120" border="0" cellpadding="0" cellspacing="0" id="checkMsgTable" style="font:9pt 宋体;">           
				<tbody>
					<tr>                 
						<td width="136" valign="top" id="checkMsgDiv0_msg" background="<%=contextPath%>/js/validate/alert_middle.gif" align="center" style="font:9pt 宋体;">test </td>
			         </tr>          
			     </tbody>
	        </table>      
	    </td>    
	</tr>    
	<tr>     
		<td height="10" valign="top">
			<img src="<%=contextPath%>/js/validate/alert_bottom.gif" width="120"/>
		</td>    
	</tr>  
  </tbody>
</table>
</div>
<div>
	<embed id="lingoes_plugin_object" type="application/lingoes-npruntime-capture-word-plugin" hidden="true" width="0" height="0">
</div>
</body>
</html>
