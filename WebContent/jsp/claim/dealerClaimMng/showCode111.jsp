<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<body>
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：代码选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
   <!--查询条件begin-->
    <table class="table_query" border="0" >
      <tr>
        <td class="table_query_2Col_label_5Letter"><div align="right"><span class="tabletitle">大系统：</span></div></td>
        <td align="left">
            <select name="CODE" id="CODE" onchange="queryCon(this.value)">
            	<option value="">-请选择-</option>
            	<c:forEach var="list" items="${list }">
            		<option value="${list.VRT_CODE }">${list.VRT_NAME }</option>
            	</c:forEach>
            </select>
        </td>
        <td class="table_query_2Col_label_5Letter">子系统：</td>
        <td align="left">
        	<select name="CODE_NAME" id="CODE_NAME">
        		<option value="">-请选择-</option>
            </select>
        </td>
        <td class="table_query_2Col_label_5Letter">顾客问题：</td>
        <td align="left">
            <input name="CUSTOMERS_PROBLEM" type="text" class="middle_txt" size="5" />
        </td>
      </tr>
      <tr>
      	<td colspan="6" align="center">
	      	<input name="button" type="button" onmousedown="isFlag();" class="normal_btn"  value="查询" />
	      	<input name="button" type="button" onclick="_hide()" class="normal_btn"  value="关闭" />
        </td>
      </tr>
    </table>
    <!--查询条件end-->
      <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >

	var myPage;
	var type='<%=request.getAttribute("type")%>';
	var labourCode = '<%=request.getAttribute("labourCode")%>';
	if(type=="5"){
		//code = "客户问题代码";
		name = "顾客问题(质量CCC代码)";
	}
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getChngCode111.json?type="+type+"&labourCode="+labourCode;
				
	var title = null;
	
	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'id',align:'center',renderer:mySelect},
				//{header: code, dataIndex: 'CCC_CODE', align:'center'},
				{header: name, dataIndex: 'CCC_VRT_VFG', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setCode(\""+record.data.CCC_VRT_VFG+"\",\""+record.data.CCC_VRT_VFG+"\")' />");
	}

	function setCode(v1,v2){
		 //调用父页面方法
		 if(v1==null||v1=="null"){
		 	v1 = "";
		 }
		 if(v2==null||v2=="null"){
		 	v2 = "";
		 }
		 if (parent.$('inIframe')) {
 		 parentContainer.setCode(v1,v2);
 		 } else {
		 parent.setCode(v1,v2);
		 }
 			//关闭弹出页面
 			parent._hide();
 		
	}
	function isFlag(){
		if($('CODE').value==''){
			MyAlert('请选择大系统查询!');
			return false;
		}else{
			if($('CODE_NAME').value==''){
				MyAlert('请选择子系统查询!');
				return false;
			}else{
				__extQuery__(1)
			}
		}
	}
	
	function queryCon(code){
		var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/queryCon.json?code="+code;
		makeNomalFormCall(url,showAuditValue,'fm','queryBtn');
	}

	//回调函数
	function showAuditValue(json){
		var list = json.list;
		var CODE_NAME = document.getElementById('CODE_NAME');
		//先清空下拉列表的数据
		var codeNameLen=CODE_NAME.options.length;
		if(codeNameLen>1){
			for(var i = 1 ; i < codeNameLen; i++){
				CODE_NAME.removeChild(CODE_NAME.options[1]);
			}
		}
		//再把查出来的值放进去
		for(var i = 0 ; i < list.length; i++){ 
			var _option = new Option(list[i].VFG_NAME,list[i].VFG_NAME);   
			CODE_NAME.options.add(_option);  
		}
	}

	//控制鼠标和键盘的操作
	window.document.onkeydown = function (){
		if(event.keyCode==13){
			isFlag();
		};
	} 
</script>
</body>
</html>
