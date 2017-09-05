<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增索赔员</title>
</head>
<script type="text/javascript" >

</script>
<body onload="__extQuery__(1);">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：供应商索赔维护&gt;索赔员选择</div>
 <form method="post" name = "fm" id="fm">
    <table class="table_query" border="0" >
      <tr>
        <td width="20%" align="right">索赔员账号：</td>
        <td>
            <input name="USER_CODE" type="text" class="middle_txt"/>
        </td>
         <td width="20%" align="right">索赔员姓名：</td>
        <td>
            <input name="USER_NAME" type="text" class="middle_txt"/>
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

	var url = "<%=contextPath%>/claim/basicData/ClaimVenderPrice/showUserQuery.json";
				
	var title = null;

	var columns = [
	           {header:'序号',align:'center',renderer:getIndex,width:'3%'},
	           {header:'选择',dataIndex:'USER_ID',renderer:myCheckBox,align:'center',width:'4%'},
	           {header:'人员账号',dataIndex:'ACNT',align:'center'},
	            {header:'人员姓名',dataIndex:'NAME',align:'center'}
		   		]; 

	function myCheckBox(value,meta,record){
		var ipt = '' ;
		ipt+='<input type="radio" name="code" onclick="winClose();" value="'+record.data.USER_ID+'">'+'<input type="hidden" name="Wcode" value="'+record.data.NAME+'">';
		return String.format(ipt);
	}
	
	function winClose(){
		var codes = document.getElementsByName('code');
		var Wcodes = document.getElementsByName('Wcode');
		var cs = [];
		var ws=[];
		var k = 0;
		var j=0;
		for(var i=0;i<codes.length;i++){
			if(codes[i].checked==true){
				cs[k++] = codes[i].value;
				ws[j++] = Wcodes[i].value;
				
			}
		}
		if(parentContainer.setUser==undefined)
			MyAlert('调用父页面setLaborList方法出现异常!');
		else{
			parentContainer.setUser(cs,ws);
			_hide();
		}
	}
</script>
</body>
</html>