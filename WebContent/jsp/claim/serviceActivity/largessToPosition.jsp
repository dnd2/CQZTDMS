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
        <td class="table_query_3Col_label_6Letter">操作</td> 
    </tr>  
    <c:forEach var="largess" items="${largess}" varStatus="vs">
	    <tr>            
	        <td class="table_query_3Col_label_6Letter" align="left">  
	        <c:if test="${is_add == 1}">
	        <script type='text/javascript'>
		       var activityType=getItemValue('${largess.giftName}');
		       document.write(activityType) ;
		     </script>
	        </c:if>
	        <c:if test="${is_add != 1}">
	        <script type='text/javascript'>
		       	genSelBoxExp("gift_name",<%=Constant.TAX_RATE_GIFT%>,"${largess.giftName}",false,"min_sel","","true",'');
  		    </script>
	        </c:if>
	        </td>            
	        <td class="table_query_3Col_label_6Letter" align="left" ><input <c:if test="${is_add == 1}">disabled='true'</c:if>  type="text" name="project_name" id="project_name" datatype="0,is_null,20" value="${largess.projectName}" /></td> 
	        <td class="table_query_3Col_label_6Letter" align="left" ><input <c:if test="${is_add == 1}">disabled='true'</c:if>  type="text" name="project_amount" id="project_amount" datatype="0,is_double" decimal="2" value="${largess.projectAmount}" /></td> 
	        <td class="table_query_3Col_label_6Letter" align="left" ><input <c:if test="${is_add == 1}">disabled='true'</c:if>  type="text" name="remark"  id="remark" datatype="0,is_null,200" value="${largess.remark}"  /></td> 
	        <td class="table_query_3Col_label_6Letter" align="left"><input <c:if test="${is_add == 1}">disabled='true'</c:if>  class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow(this);" /></td> 
	    </tr>  
     </c:forEach>
  </table>
  <br/>
<script type="text/javascript" >

	function addRow(tableId){
	    var addTable = document.getElementById(tableId);
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row1";
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
		insertRow.insertCell(4);
		addTable.rows[length].cells[0].align='left';	
		addTable.rows[length].cells[0].align='left';		
		addTable.rows[length].cells[0].align='left';	
		addTable.rows[length].cells[0].align='left';
		addTable.rows[length].cells[0].align='left';	
				
		addTable.rows[length].cells[0].innerHTML = genSelBoxExpPay("gift_name",<%=Constant.TAX_RATE_GIFT%>,"",false,"min_sel","","true","");
		addTable.rows[length].cells[1].innerHTML = '<input type="text" name="project_name" id="project_name" datatype="0,is_null,20" />'; 
		addTable.rows[length].cells[2].innerHTML = '<input type="text" name="project_amount" id="project_amount"  datatype="0,is_double" decimal="2" />'; 
		addTable.rows[length].cells[3].innerHTML = '<input type="text" name="remark"  id="remark" datatype="0,is_null,200"  />'; 
		addTable.rows[length].cells[4].innerHTML = '<input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow(this);" />'; 
		return addTable.rows[length];
	}
	
	
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
	function deleteRow(obj)
	{
		 var tabl=document.all['otherTableId'];
		 var index = obj.parentElement.parentElement.rowIndex;
		 tabl.deleteRow(index); 
		 countSeq();
	}
	
	function add()
	{
		if(!submitForm('fm')) {
			return false;
		}
		else
		{
			makeNomalFormCall('<%=request.getContextPath()%>/claim/serviceActivity/ServiceActivityManage/add_largess.json',updateBack,'fm','');
	    }
	}
	function updateBack(json){
	
		if (parent.$('inIframe')) 
		{
			parentDocument.getElementById('amounttt').value = json.num;
			
		}	
		  _hide();

	}
	
	
	
</script>
  <table align="center">
  <tr align="center">
  <td>
     <c:if test="${is_add == 1}">
    <input  class=normal_btn  onclick='_hide();' align="right" value=关闭 type=button name=button/>
  </c:if>
  <c:if test="${is_add != 1}">
  	<input  class=normal_btn onclick="addRow('otherTableId');" align="right" value=新增 type=button name=button/>
    <input  class=normal_btn onclick='add()' align="right" value=确定 type=button name=button/>
  </c:if>
  
  </td>
  </tr>
  </table>
</form>
</body>
</html>