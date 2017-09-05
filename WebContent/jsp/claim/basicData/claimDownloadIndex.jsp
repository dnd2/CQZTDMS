<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>下发索赔工时</title>
</head>
<body>
<form name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;下发索赔工时</div>
   <table  class="table_list">
   	     <th><input type="checkbox" name="checkAll" onclick="selectAll(this,'wrgroupIds')"/></th>
     	 <th>车型组</th>
	       <c:forEach var="addlist" items="${ADDLIST}">
	       <tr class="table_list_row1">
	          <td> 
				<input type="checkbox" id="${addlist.WRGROUP_ID}" name="wrgroupIds" value="${addlist.WRGROUP_ID}"/>
	          </td>
	          <td>
	          <a href="#" onclick="laborDetail('${addlist.WRGROUP_ID}');"><c:out value="${addlist.WRGROUP_NAME}"></c:out></a>
	          </td>
	        </tr>
	    </c:forEach>
       
  </table>
  <br/>
  <table class="table_query">
		<tr>            
        	<td class="table_query_2Col_label_5Letter">经销商代码：</td>            
        	<td>
				<textarea rows="2" cols="53" id="dealerCode" name="dealerCode"></textarea>
		     	<input name="button1" type="button" class="mini_btn" style="cursor: pointer;"  onclick="showOrgDealer01('dealerCode','','true','',true);" value="..." />        
		     	<input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>
        	</td>   
       	</tr>
       	<tr>
             <td colspan="4" align="center">
				<input class="normal_btn" type="button" id="sendBtn" name="button1" value="下发"  onclick="subChecked();"/>
			</td>
       </tr>
	</table>

<%--       <tr>            --%>
<%--        <td class="table_query_2Col_label_5Letter">经销商代码：</td>            --%>
<%--        <td>--%>
<%--			<input class="middle_txt" id="dealerName"  name="dealerName" type="text" datatype="1,is_null,200"/>--%>
<%--			<textarea rows="2" cols="53" id="dealerName" name="dealerName" ></textarea>--%>
<%--            <input name="button1" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerName','','true','',true);" value="..." />        --%>
<%--            <input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>--%>
<%--        </td>--%>
<%--        <td class="table_query_2Col_label_5Letter">&nbsp;</td>--%>
<%--        <td>&nbsp;</td>     --%>
<%--       </tr>--%>
<%--       <tr>            --%>
<%--        <td class="table_query_2Col_label_5Letter">经销商名称：</td>--%>
<%--        <td><input type="text" name="DEALER_NAME" id="DEALER_NAME" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/></td>     --%>
<%--       </tr>       --%>
<%--	   <tr>--%>
<%--	   	<td colspan="4" align="center">注:如不选择经销商，则下发索赔工时数据到所有的经销商!</td>--%>
<%--	   </tr>--%>
	   
</form>  
<script type="text/javascript">
function subChecked() {
	var str="";
	var chk = document.getElementsByName("wrgroupIds");
	var l = chk.length;
	var cnt = 0;
	for(var i=0;i<l;i++){        
		if(chk[i].checked)
		{            
			str = chk[i].value+","+str; 
			cnt++;
		}
	}
	if(cnt==0){
        MyAlert("请选择要下发的车型组！");
        return;
    }else{
		var dealerCodes = document.getElementById("dealerCode").value;
		if (!dealerCodes) {
			MyAlert("经销商代码不能为空！");
			return;
		} else {
			sel(str);
		}
    }
}
//下发方法：
function sel(str){
	MyConfirm("是否确认下发？",send,[str]);
}  
//下发：
function send(str){
	document.getElementById("sendBtn").disabled = true;
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimDowLoadMain/claimDowLoadSend.json?wrgroupIds='+str,sendBack,'fm','');
}
//下发回调方法：
function sendBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlert("下发成功！");
		document.getElementById("sendBtn").disabled = false;
	} else {
		MyAlert("下发失败！请联系管理员！");
	}
}   
//下发方法：
function sendAll(){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/DownloadcodeMain/sendAll.json',sendAllBack,'fm','');
}
//下发后的回调方法：
function sendAllBack(json) {
	if(json.success != null && json.success == "true") {
	    if(json.returnValue != null && json.returnValue.length > 0){
			MyAlert("新增成功！但经销商："+json.returnValue+"原先已经维护了！");
	    }else {
	    	MyAlert("下发成功 !");
	    }
	} else {
		MyAlert("下发失败！请联系管理员！");
	}
}
//清除方法
 function clr() {
	document.getElementById('dealerCode').value = "";
  }
//车型组对应的索赔工时明细：
function laborDetail(val){
	OpenHtmlWindow('<%=contextPath%>/claim/basicData/ClaimDowLoadMain/claimLaborDetail.do?WRGROUP_ID='+val,900,500);
}  
</script>
</body>
</html>