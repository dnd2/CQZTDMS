<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>下发失效模式</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基本数据&gt;下发失效模式</div>
<form id="fm" name="fm" method="post">
   <table class="table_query">
          <tr>
            <td  width="10%" align="right" nowrap="nowrap">下发范围选择：</td>
            <td width="60%">
				<select name="select" id="select">
					<option value="0">新增失效模式下发给所有经销商</option>
					<option value="1">所有失效模式下发给选定经销商</option>
				</select>
            </td>
          </tr>
          <tr>
	 		<td width="10%" align="right">经销商代码：</td>
	 		<td width="60%">
	 			<textarea cols="50" rows="2" id="dealer_code" name="dealer_code"></textarea>&nbsp;
	 			<input type="button" value="..." class="mini_btn" onclick="showOrgDealer('dealer_code','','true','','true');"/>&nbsp;
	 			<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
	 		</td>
			</tr>
		   <tr>    
			   <td colspan="2" align="center">
	            <input class="normal_btn" type="button" name="button1" value="下发"  onclick="downFailure()"/>
	            <input name="back" type="button" class="normal_btn" value="返回" onclick="onBack();"/>
	           </td>
           </tr>
       
  </table>
<br/>

</form>
<script type="text/javascript">
	function downFailure(){
		var value= document.getElementById("select").value;
		var url = "<%=request.getContextPath()%>/claim/basicData/FailureMaintainMain/downFailMode.json"
		if(value==0){
			url=url+"?value=0"
		}else{
			var codes = document.getElementById("dealer_code").value;
			if(codes==null || codes==""){
				MyAlert("请选择经销商代码");
				return;
			}else{
				url=url+"?value=1";
				//MyAlert(codes);
			}
		}
		makeNomalFormCall(url,showResult,'fm');
	}
	function showResult(json){
		var msg=json.msg;
		MyAlert(msg);
	}
	function wrapOut(){
		document.getElementById("dealer_code").value="";
	}
</script>
</body>
</html>
