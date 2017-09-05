<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	Long userId = (Long)request.getAttribute("logonUser");
	
%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>首页新闻查询</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<form name='fm' id='fm' method="post">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;售后部位&gt;配置管理</div>
  <table id="otherTableId" width="100%" class="tab_edit">
  <tr><td><input type="hidden"  id="largess_type" name ="largess_type" value="${largess_type}"/><input type="hidden"  id="activityId" name ="activityId" value="${activityId}"/> </td>
  </tr>
   <tr>                     
        <td class="table_query_3Col_label_6Letter">项目类别</td> 
        <td class="table_query_3Col_label_6Letter">项目名称</td> 
        <td class="table_query_3Col_label_6Letter">项目金额</td> 
        <td class="table_query_3Col_label_6Letter">描述</td> 
    </tr>  
    <c:forEach var="largess" items="${largess}" varStatus="vs">
	    <tr>            
	        <td class="table_query_3Col_label_6Letter" align="left">  
	        <script type='text/javascript'>
		       var activityType=getItemValue('${largess.giftName}');
		       document.write(activityType) ;
		     </script>
	        </td>            
	        <td class="table_query_3Col_label_6Letter" align="left" >${largess.projectName}</td> 
	        <td class="table_query_3Col_label_6Letter" align="left" >${largess.projectAmount} </td> 
	        <td class="table_query_3Col_label_6Letter" align="left" >${largess.remark}  </td> 
	    </tr>  
     </c:forEach>
  </table>
  <br/>
<script type="text/javascript" >

	
	
	function genSelBoxExpPay(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
		var str = "";
		var arr;
		if(expStr.indexOf(",")>0)
			arr = expStr.split(",");
		else {
			expStr = expStr+",";
			arr = expStr.split(",");
		}
		str += "<select id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
		if(nullFlag && nullFlag == "true"){
			str += " datatype='0,0,0' ";
		}
		// end
		str += " onChange=doCusChange(this.value);> ";
		if(setAll){
			str += genDefaultOpt();
		}
		for(var i=0;i<codeData.length;i++){
			var flag = true;
			for(var j=0;j<arr.length;j++){
				if(codeData[i].codeId == arr[j]){
					flag = false;
				}
			}
			if(codeData[i].type == type && flag){
				str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '"+codeData[i].codeDesc+"' >" + codeData[i].codeDesc + "</option>";
			}
		}
		str += "</select>";
		return str;
	}
	
</script>
  <table align="center">
  <tr align="center">
  <td>
  	<input  class=normal_btn  onclick="_hide();" align="right" value=关闭 type=button name=button/>
  
  </td>
  </tr>
  </table>
</form>
</body>
</html>