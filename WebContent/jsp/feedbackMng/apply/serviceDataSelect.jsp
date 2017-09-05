<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增服务资料明细</title>
</head>
<script type="text/javascript" >

</script>
<body onload="__extQuery__(1);">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：服务资料审批表维护 &gt; 服务资料明细</div>
 <form method="post" name = "fm" id="fm">
    <table class="table_query" border="0" >
      <tr>
        <td class="table_query_2Col_input"><div align="right"><span class="tabletitle">服务资料名称：</span></div></td>
        <td class="table_query_3Col_input"><div align="left">
            <input name="dataname" type="text" class="middle_txt" size="15" />
        </div></td>
        <td class="table_query_2Col_input"><input name="buttonSearch" type="button" onclick="__extQuery__(1);" class="normal_btn"  value="查询" /></td>
      </tr>
    </table>
<!--  <table class="table_list" style="border-bottom:1px solid #DAE0EE">-->
<!--    表格名字-->
<!--    <thead>-->
<!--      <tr >-->
<!--        <th width="7%">选择 </th>-->
<!--        <th width="54%" class="tdgray">资料名称</th>-->
<!--        <th width="54%" class="tdgray">单价</th>-->
<!--        </tr>-->
<!--  </table>-->
      <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />  
</form>

<script type="text/javascript">
	var myPage;

	var url = "<%=contextPath%>/feedbackmng/apply/ServiceInfoApplyManager/queryServiceData.json?commind=1";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'DATA_ID',renderer:mySelect ,align:'center'},
				{header: "资料名称", dataIndex: 'DATA_NAME', align:'center'},
				{header: "单价", dataIndex: 'PRICE', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setNo(\""+record.data.DATA_ID+"\",\""+record.data.DATA_NAME+"\",\""+record.data.PRICE+"\")'/>");
	}
	
	function setNo(dataId,dataName,price){
	 //调用父页面方法
	 parentContainer.getSelectValue(dataId,dataName,price);
	 //关闭弹出页面
	 _hide();
	}
</script>
</body>
</html>