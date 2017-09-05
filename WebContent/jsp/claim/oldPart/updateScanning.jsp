<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>索赔申请上报</title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
    function doInit()
	{
	   loadcalendar();
	}
</script>
</head>
<body>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
<tr>
 <td align="right" nowrap="true">条码：</td>
<td>
<input type="text" id="barcodeNo" value="${barcodeNo}" readonly/></td>
 <td align="right" nowrap="true">索赔类型：</td>
            <td>
				<script type="text/javascript">
				    genSelBoxExp("CLAIM_TYPE",<%=Constant.OLDPART_DEDUCT_TYPE%>,${reasonCode},true,"short_sel","","true",'<%=Constant.OLDPART_DEDUCT_TYPE_05%>,<%=Constant.OLDPART_DEDUCT_TYPE_07%>,<%=Constant.OLDPART_DEDUCT_TYPE_08%>,<%=Constant.OLDPART_DEDUCT_TYPE_09%>');
				</script>
 		    </td> 	

</tr>

<tr>	
	<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="保存" onclick="confirm();"/>
		</td>
		
			<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="修改正常" onclick="confirm1()"/>
		</td>
		
			<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="返回" onclick="back()"/>
		</td>
</tr>
</table>
</form>
<script type="text/javascript">

function confirm(){
	MyConfirm("是否确认保存？",save,[]);
	}
function confirm1(){
	MyConfirm("是否确认保存为正常？",saveZC,[]);
	}
		function save(){

			var CLAIM_TYPE=document.getElementById("CLAIM_TYPE").value;
		
			var barcodeNo=document.getElementById("barcodeNo").value;
			fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/updateScanningSave.do?CLAIM_TYPE="+CLAIM_TYPE+"&barcodeNo="+barcodeNo;
		    fm.method="post";
		    fm.submit();
		    if(parent.${'inIframe'}){
				parent.window._hide();
			}else{
				window.close();
			}
			}
		
				
		function saveZC(){
			var barcodeNo=document.getElementById("barcodeNo").value;

			fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/updateScanningSaveZC.do?barcodeNo="+barcodeNo;
		    fm.method="post";
		    fm.submit();
		    if(parent.${'inIframe'}){
				parent.window._hide();
			}else{
				window.close();
			}
		}
		


		function back(){
			fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/queryScanning.do";
		    fm.method="post";
		    fm.submit();
			
			}
</script>
</body>
</html>