<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- created by lishuai103@yahoo.com.cn 20100603 供应商查询 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商与供货方关系维护</title>

<script language="JavaScript">

	//初始化方法
	function doInit()
	{
		__extQuery__(1);
	}

</script>
</head>
<body>
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;基本信息查询&gt;经销商与供货方关系维护 
<form method="post" name ="fm" id="fm">
<input type="hidden" name="dcId" value="<c:out value="${dcInfo.DC_ID}"/>"/>
	<table class="table_edit">
     <tr>
      <td class="table_query_2Col_label_6Letter">供货方代码：</td>
      <td class="table_query_2Col_input">
      	<c:out value="${dcInfo.DC_CODE}"/>
      </td>
      <td class="table_query_2Col_label_6Letter">供货方名称：</td>
      <td>
      	<c:out value="${dcInfo.DC_NAME}"/>
      </td>
      <td class="table_query_2Col_label_2Letter">
      	<input type="button" name="BtnQuery" id="queryBtn"  value="返 回"  class="normal_btn" onClick="history.back();" >
      </td>
  	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<form name="form1">
   <table class="table_list" id="table1" >
  	  <tr>
  	  	<th align="center">
    		<input class="normal_btn" type="button" value="新增" onclick="addRelaction('<c:out value="${dcInfo.DC_ID}"/>')">&nbsp;
    		<input class="normal_btn" type="button" value="删除" onclick="deleteConfirm()">
       </th>
  	  </tr>
   </table>
  </form>
<script type="text/javascript" >

	var myPage;

	var url = "<%=contextPath%>/partsmanage/infoSearch/DealerSupplierInfo/updateRelaction.json?dcId="+"<c:out value="${dcInfo.DC_ID}"/>";
				
	var title = null;

	var columns = [
	{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"dealerIds\")' />", width:'8%',sortable: false,dataIndex: 'DEALER_ID',renderer:myCheckBox},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "经销商级别", dataIndex: 'DEALER_LEVEL', align:'center',renderer:getItemValue},
				{header: "所属大区", dataIndex: 'ORG_NAME', align:'center'}
		      ];
		    
//设置超链接  begin      
	
	//设置超链接
	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{
		return String.format("<input type='checkbox' name='dealerIds' value='" + value + "'/>");
	}
	
	//新增关系
	function addRelaction(value)
	{
		OpenHtmlWindow('<%=contextPath%>/partsmanage/infoSearch/DealerSupplierInfo/queryDealerInfoListInit.do?dcId='+value,800,500);
	}
	
	//删除与经销商关系
	function deleteConfirm()
	{
		var chk = document.getElementsByName("dealerIds");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{            
				cnt++;
			}
		}
        if(cnt==0)
        {
             MyAlert("请选择要删除经销商！");
             return;
        }
		MyConfirm("确认删除？",deleteDealerReation);
	}
	
	//删除与经销商关系
	function deleteDealerReation()
	{
		makeNomalFormCall("<%=contextPath%>/partsmanage/infoSearch/DealerSupplierInfo/delReaction.json",showReactionValue,'fm','queryBtn'); 
	}
	
	//回显方法
	function showReactionValue(json)
	{
		if(json.returnValue == '1')
		{
			MyAlert("删除成功！");
			__extQuery__(1);
		}else
		{
			MyAlert("删除失败！");
		}
	}
	
//设置超链接 end
	
</script>
</body>
</html>