<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>更改库区库位</title>
</head>
<script type="text/javascript" >

</script>
<body onload="__extQuery__(1);">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：旧件库区库位维护&gt;库区库位选择</div>
 <form method="post" name = "fm" id="fm">
    <table class="table_query" border="0" >
      <tr>
        <td width="20%" align="right">区域编码：</td>
        <td>
            <input name="CODE_OLD" type="text" class="middle_txt"/>
        </td>
         <td width="20%" align="right">区域名称：</td>
        <td>
            <input name="NAME_OLD" type="text" class="middle_txt"/>
            <input name="NAME_TYPE" id="nameType" type="hidden" class="middle_txt" value="${NAME_TYPE}"/>
        </td>
           </tr>
        <TR align="center">
        <td  colspan="4"> 
        	<input type="button" onclick="__extQuery__(1);" class="normal_btn"  value="查询" /> 
        	<input type="button" value="关闭" class="normal_btn" onclick="_hide();"/>
        </td>
        <td></td>
      </tr>
    </table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />  
</form>

<script type="text/javascript">
	var flag = true ;

	var url = "<%=contextPath%>/claim/basicData/ClaimVenderPrice/showReserQuery.json";
				
	var title = null;

	var columns = [
	           {header:'序号',align:'center',renderer:getIndex,width:'3%'},
	           {id:'action',header: "选择", width:'3%',align:'center',sortable: false,dataIndex: 'CODE_OLD',renderer:myCheckBox},
	           {header:'区域编码',dataIndex:'CODE_OLD',align:'center'},
	            {header:'区域名称',dataIndex:'NAME_OLD',align:'center'}
		   		]; 

	function myCheckBox(value,metaDate,record){
		var name = "'";
	    var obj =  ' <input type="radio" name="part" onclick="winClose('+name+record.data.CODE_OLD+name+','+name+record.data.NAME_OLD+name+');" />';
		return String.format(obj);
	}
	
	function winClose(CODE_OLD,NAME_OLD){
		
		var nameType = document.getElementById('nameType').value; 
			if(1 == nameType){
			parentDocument.getElementById('LOCAL_WAR_HOUSE').value = CODE_OLD;
			}else if(2 == nameType){
			parentDocument.getElementById('LOCAL_WAR_SHEL').value = CODE_OLD;
			}else{
			parentDocument.getElementById('LOCAL_WAR_LAYER').value = CODE_OLD;
			}
		_hide();
	}
</script>
</body>
</html>