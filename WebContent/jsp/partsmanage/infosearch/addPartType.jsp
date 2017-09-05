<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件大类维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;基本信息查询&gt;配件大类信息维护</div>
<form name='fm' id='fm' method="post">
<table class="table_edit" >
    <tr>
      <td class="table_query_2Col_label_6Letter">配件大类代码：</td>
      <td><input name="PARTTYPE_CODE" type="text" id="PARTTYPE_CODE"  class="middle_txt" datatype="0,is_null,30"/></td>
      <td class="table_query_2Col_label_6Letter">配件大类名称：</td>
      <td><input name="PARTTYPE_NAME" type="text" id="PARTTYPE_NAME"  class="middle_txt" datatype="0,is_null,100"/></td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_6Letter">是否需回运：</td>
      <td><select name="IS_RETURN">
          <option value="<%=Constant.IS_NEED_RETURN%>">是</option>
          <option value="<%=Constant.IS_UNNEED_RETURN%>">否</option>
      </select>
      </td>
      <td class="table_query_2Col_label_6Letter">是否大件：</td>
       <td><select name="IS_MAX">
          <option value="<%=Constant.IS_NEED_RETURN%>">是</option>
          <option value="<%=Constant.IS_UNNEED_RETURN%>">否</option>
      </select>
      </td>
    </tr>
</table>
<br />

<table class="table_list" id="add_tab">
	<tr class="table_list_row1">
		<td with=40%>配件代码</td>
		<td with=40%>配件名称</td>
		<td><input type="button" value="选择配件" class="long_btn" onclick="showPart();"/></td>
	</tr>
</table>
<br />

<table class="table_edit">
	<tr>
 		<td colspan="2" align="center">
 			<input class="normal_btn" type="button" value="确定" onclick="sureSave();"/> 
 			&nbsp;&nbsp;
 			<input class="normal_btn" type="button" onclick="goBack();" value="返回"/>
 		</td>
	</tr>
</table>
</form>
<script type="text/javascript">

	function showPart(){
		var url = '<%=contextPath%>/partsmanage/infoSearch/PartTypeSearch/queryPart.do' ;
		OpenHtmlWindow(url,700,500);
	}
	function setPartCode(partid,partcode,partname){
		var tab = $('add_tab');
		for(var i = 0 ;i<partcode.length;i++){
			var idx = tab.rows.length ;
			var insert_row = tab.insertRow(idx);
			if(idx%2==0)
				insert_row.className = 'table_list_row2';
			else 
				insert_row.className = 'table_list_row1' ;
			insert_row.insertCell(0);
			insert_row.insertCell(1);
			insert_row.insertCell(2);
			var cur_row = tab.rows[idx];
			cur_row.cells[0].innerHTML = partcode[i]+'<input type="hidden" name="part_id" value='+partid[i]+'>' ;
			cur_row.cells[1].innerHTML = partname[i]+'<input type="hidden" name="part_name" value='+partname[i]+'>' ;
			cur_row.cells[2].innerHTML = '<input type="button" class="normal_btn" value="删除" onclick="delRow(this);"/>' ;
		}
	}
	function delRow(obj){
		var tab = $('add_tab');
		var idx = obj.parentElement.parentElement.rowIndex;
		tab.deleteRow(idx);
	}
	function sureSave(){
		$('fm').action = '<%=contextPath%>/partsmanage/infoSearch/PartTypeSearch/addPartType.do' ;

		if($('add_tab').rows.length==1){
			MyAlert('配件信息未维护!');
			return ;
		}
		MyConfirm('此操作将更改已存在的数据,确定操作?',$('fm').submit);
	}
	function goBack(){
		location = '<%=contextPath%>/partsmanage/infoSearch/PartTypeSearch/partTypeEditInit.do' ;
	}
</script>
</body>
</html>
