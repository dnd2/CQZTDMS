<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">

</script>
<style type="text/css" >
 .mix_type{width:100px;}
 .min_type{width:176px;}
 .mini_type{width:198px;}
 .long_type{width:545px;}
 .xlong_type{width:305px}
</style>
<title>实销信息上报</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="loadcalendar();"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  潜客管理 &gt; 客户管理 &gt;保险公司修改 </div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="ctmId" name="ctmId" value="${ctmId}"/>
		<input type="hidden" id="decorationId" name="docId" value="${tp.decorationId}"/>
		<table class="table_query" border="0">
		<tr>
				<td width="20%" class="tblopt"><div align="right">精品项目：</div></td>
				<td width="39%" >
      				<input type="text" id="exProject" name="exProject" class="min_type" size="20" value="${tp.exproject}"/>
    			</td>
    			<td width="20%" class="tblopt"><div align="right">精品名称：</div></td>
				<td width="39%" >
      				 <input name="exName" type="text" id="exName"  class="min_type"  value="${tp.exname}"  />
    			</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">精品单价：</div></td>
				<td width="39%" >
      				<input type="text" id="exPrice" name="exPrice" class="min_type" size="20" value="${tp.price}" onchange="setExMoney();"/>
    			</td>
    			<td width="20%" class="tblopt"><div align="right">精品数量：</div></td>
				<td width="39%" >
      				<input type="text" id="amount" name="amount" class="min_type" size="20" value="${tp.amount}" onchange="setExMoney();"/>
    			</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">精品金额：</div></td>
				<td width="39%" >
      				<input type="text" id="exMoney" name="exMoney" class="min_type" size="20" value="${tp.money}" readonly/>
    			</td>
				<td width="20%" class="tblopt"><div align="right">赠送或购买：</div></td>
				<td width="39%" colspan="3" >
      				<input type="text" id="giveOrBuy" name="giveOrBuy" class="min_type" size="20" value="${tp.giveorbuy}"/>
    			</td>
			</tr>
			<tr>
				<td class="table_query_3Col_input" colspan="4" align="center">
					<center>
					<input type="button" class="normal_btn" onclick="updateDoc();" value="修 &nbsp; 改" id="queryBtn" /> 
					</center>
				</td>
			</tr>
		</table>
		
</form>
	
</div>
<script type="text/javascript">

	function updateDoc(){
		makeFormCall('<%=contextPath%>/crm/customer/CustomerManage/docUpdate.json', addResult, "fm") ;
	}
	//数据回写
	function addResult(json){
		_hide();
		parent.$('inIframe').contentWindow.docTableAdd(json);
	}
	function setExMoney(){
		var amount=$("amount").value;
		var exPrice=$("exPrice").value;
		
		if(exPrice==null||""==exPrice){
			exPrice="1.00";
			$("exPrice").value="1.00";
		}
		if(amount==null||""==amount){
			amount=1;
			$("amount").value="1";
		}
		amount=Number(amount);
		exPrice=Number(exPrice);
		var flagAmount=isNaN(amount);
		var flagExPrice=isNaN(exPrice);
		if(!flagAmount&&!flagExPrice){
		 	exPrice=Math.round(exPrice*100);
			$("exMoney").value=exPrice*amount/100;
		}
	}
</script>    
</body>
</html>