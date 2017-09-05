<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); 
%>
<script language="JavaScript" >
</script>
<body>
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation">
<img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：配置选择 </div>
</div>
 <form  name="fm" id="fm" method="post">
<input class="middle_txt" id="partcode" name="partcode" value="${partcode }" type="hidden"/>
<input class="middle_txt" id="model_code" name="model_code" value="${model_code }" type="hidden"/>
<!--查询条件begin-->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    <tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">配置代码：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="package_code" name="package_code" value="" type="text"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">配置名称：</td>
      	<td width="15%" nowrap="true">
      		<input name="package_name" type="text" id="package_name"  class="middle_txt"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="12.5%"></td>
	</tr>
  	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="确认" class="normal_btn" onclick="is_ok();" />
    	</td>
    </tr>
 </table>
 <!--查询条件end-->
 <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
 <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >
	var myPage;
	var url = "<%=contextPath%>/MainTainAction/queryConfiguration.json?query=true";
				
	var title = null;

	var columns = [
			{id:'action',header: "<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"recesel\")' />全选",sortable: false,dataIndex: 'PACKAGE_CODE',renderer:checkBoxShow,align:'center'},
			{header: "配置代码", dataIndex: 'PACKAGE_CODE', align:'center'},
			{header: "配置名称", dataIndex: 'PACKAGE_NAME', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='check' name='rd' onclick='setMainWork(\""+record.data.PACKAGE_CODE+"\",\""+record.data.PACKAGE_NAME+"\")' />");
	}
	 function checkBoxShow(value,meta,record){
		  var str="";
		  str+="<input type='checkbox' id='recesel' name='recesel' value='" + record.data.PACKAGE_CODE + "' />";
			return String.format(str);
	}
	function is_ok(){
	 var code = getCheckedToStr('recesel');
	 var name="";
	 var url = "<%=contextPath%>/MainTainAction/queryConfigurationBycode.json?package_code="+code;
	 makeNomalFormCall(url,function show(json){
		 name= json.package_name;
	     setMainConfiguration(code,name);
	},'fm');
	}
	//得到选择的值,按照‘,’隔开
		function getCheckedToStr(name) {
			var str="";
			var chk = document.getElementsByName(name);
			if (chk==null){
				return "";
			}else {
			var l = chk.length;
			for(var i=0;i<l;i++){        
				if(chk[i].checked)
				{            
				str = chk[i].value+","+str; 
				}
			}
				return str.substring(0,str.length-1);
			}
		}
	function setMainConfiguration(v1,v2){
		 //调用父页面方法
		 if(v2==null||v2=="null"){
		 	v2 = "";
		 }
		 if(v1==null||v1=="null"){
		 	v1 = "";
		 }
 		if (parent.$('inIframe')) {
 			parentContainer.setConfiguration(v1,v2);
 		} else {
			parent.setConfiguration(v1,v2);
		}
	 	//关闭弹出页面
	 	parent._hide();
	}
	
</script>
</body>
</html>
