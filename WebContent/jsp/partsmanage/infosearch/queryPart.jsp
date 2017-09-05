<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件信息</title>
</head>
<script type="text/javascript" >

</script>
<body onload="__extQuery__(1);">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理&gt;配件信息选择</div>
 <form method="post" name = "fm" id="fm">
    <table class="table_query" border="0" >
      <tr>
        <td width="20%" align="right">配件代码：</td>
        <td>
            <input name="PART_CODE" type="text" class="middle_txt"/>
        </td>
        <td class="table_query_2Col_input">
        	<input type="button" onclick="__extQuery__(1);" class="normal_btn"  value="查询" />
        	&nbsp;
        	<input type="button" value="确认" class="normal_btn" onclick="winClose();"/>
        	&nbsp;
        	<input type="button" value="关闭" class="normal_btn" onclick="_hide();"/>
        </td>
      </tr>
    </table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />  
</form>

<script type="text/javascript">
	var flag = true ;

	var url = "<%=contextPath%>/partsmanage/infoSearch/PartTypeSearch/queryPartDialog.json";
				
	var title = null;

	var columns = [
	           {header:'序号',align:'center',renderer:getIndex,width:'3%'},
	           {header:'全选',dataIndex:'partId',renderer:myCheckBox,align:'center',width:'4%'},
	           {header:'配件代码',dataIndex:'partCode',align:'center'},
	           {header:'配件名称',dataIndex:'partName',align:'center'}
		   		]; 
		      
	function myCheckBox(value,meta,record){
		return String.format("<input type='checkbox' name='rd' value='"+record.data.partId+"' value1='"+record.data.partCode+"' value2='"+record.data.partName+"' onclick='setMainPartCode(\""+record.data.partId+"\",\""+record.data.partCode+"\",\""+record.data.partName+"\")' />");
	}
	function setMainPartCode(v1,v2,v3){
		 //调用父页面方法
		var v1=v1;
		if(!v1) return;
		if(flag){
			for(var i=0;i<v1.length;i++){
				v[i].checked=true;
			}
			flag = false ;
		} else if(!flag){
			for(var i=0;i<v1.length;i++){
				v1[i].checked=false;
			}
			flag = true ;
		}
	}
	
	function winClose(){
		var codes = document.getElementsByName('rd');
		var cs = [];
		var pc = [];
		var pn = [];
		var k = 0;
		for(var i=0;i<codes.length;i++){
			if(codes[i].checked==true){
				cs[k] = codes[i].value;
				pc[k] = codes[i].value1;
				pn[k] = codes[i].value2;
				k++;
			}
		}
		if(parentContainer.setPartCode==undefined)
			MyAlert('调用父页面setLaborList方法出现异常!');
		else{
			parentContainer.setPartCode(cs,pc,pn);
			_hide();
		}
	}
</script>
</body>
</html>