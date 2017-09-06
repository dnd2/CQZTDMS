<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); 
String claimId=request.getParameter("claimId");
String part_Id=request.getParameter("part_Id");
%>
<script language="JavaScript" >
</script>
<body onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation">
<img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：供应商选择 </div>
</div>
 <form  name="fm" id="fm" method="post">
<input class="middle_txt" id="claimId" name="claimId" value="<%=claimId%>" type="hidden"/>
<input class="middle_txt" id="part_Id" name="part_Id" value="<%=part_Id%>" type="hidden"/>
<input class="middle_txt" id="supplyCode" name="supplyCode" type="hidden"/>
<input class="middle_txt" id="supplyName" name="supplyName" type="hidden"/>
<!--查询条件begin-->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    <tr>
		<td width="12.5%" style="text-align: right;"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">供应商代码：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="maker_code" name="maker_code" value="" type="text"/>
      	</td>
        <td width="10%" style="text-align: right;" class="table_query_2Col_label_6Letter" nowrap="true">供应商名称：</td>
      	<td width="15%" nowrap="true">
      		<input name="maker_shotname" type="text" id="maker_shotname"  class="middle_txt"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="12.5%"></td>
	</tr>
  	<tr>
    	<td style="text-align: center;" colspan="8">
    		<input type="button" name="btnQuery" id="btnQuery" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;
    		<input name="button2" type="button" class="normal_btn" onclick="parent._hide();" value="关 闭" />
    	</td>
    </tr>
 </table>
 <!--查询条件end-->
 <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
 <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >
	var myPage;
	
	var url = "<%=contextPath%>/GoodClaimAction/querySupplierCode.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'MAKER_ID',renderer:mySelect,align:'center'},
				{header: "供应商代码", dataIndex: 'MAKER_CODE', align:'center'},
				{header: "供应商名称", dataIndex: 'MAKER_SHOTNAME', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setData(\""+record.data.MAKER_CODE+"\",\""+record.data.MAKER_SHOTNAME+"\")' />");
	}

	function setData(v1,v2){
		 //调用父页面方法
		 if(v2==null||v2=="null"){
		 	v2 = "";
		 }
		 if(v1==null||v1=="null"){
		 	v1 = "";
		 }
		 document.getElementById("supplyCode").value=v1;
		 document.getElementById("supplyName").value=v2;
		 var url="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/producerInfoSave.json";
			makeNomalFormCall(url,sureCommitBack,"fm");
	}
	function sureCommitBack(json){
		if(json.msg=="0"){			
			__parent().Back();
		 	//关闭弹出页面
		 	parent._hide();
		}else{
			MyAlert("操作失败！请与管理员联系！");
		}
	}
</script>
</body>
</html>
