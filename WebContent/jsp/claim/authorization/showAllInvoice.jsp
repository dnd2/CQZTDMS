<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算单管理</title>
	<script type="text/javascript">
	   
	</script>
</head>
<body onload="__extQuery__(1)">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;开票单维护
         <span style="color: red;">请选择要开票的经销商</span>
         <form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td style="text-align: right;">经销商代码：</td>
		<td>
		 	<input type="text" class="middle_txt" id="dealerCode" maxlength="20" name="dealerCode"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
		<td style="text-align: right;">经销商名称：</td>
		<td>
		 	<input type="text" class="middle_txt" id="dealerName" maxlength="20" name="dealerName"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input class="u-button u-query" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
		</td>
	</tr>
</table>
</form>
</div>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/application/DealerNewKp/delaerKpAllQuery.json";
		var title = null;
		
		var columns = [
					{id:'action',header: "选择",sortable: false,dataIndex: 'DEALER_ID',renderer:Mycheck,align:'center'},
					{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "是否包含旧件",dataIndex: 'CLAIM_IS',align:'center'},
					{header: "开始时间",dataIndex: 'SDATE',align:'center'},
					{header: "结束时间",dataIndex: 'EDATE',align:'center'}
			      ];
		function Mycheck(value,meta,record){ 
			  return String.format("<input type='radio' name='rd' onclick='setSupplier(\""+record.data.DEALER_ID+"\",\""+record.data.SDATE+"\",\""+record.data.EDATE+"\",\""+record.data.ID+"\")' />");
		}
		function setSupplier(v1,v2,v3,v4){
			 //调用父页面方法
			 if(v1==null||v1=="null"){
			 	v1 = "";
			 }
			 if(v2==null||v2=="null"){
			 	v2 = "";
			 }
			 if(v3==null||v3=="null"){
			 	v3 = "";
			 }
			 if(v4=="null"||v4==null){
			 	v4 = "";
			 }
			 if (parent.$('inIframe')) {
		 			parentContainer.setSupplier(v1,v2,v3,v4);
	 		} else {
				parent.setSupplier(v1,v2,v3,v4);
			}
	 		//关闭弹出页面
	 		parent._hide();
		}
</script>
</body>
</html>