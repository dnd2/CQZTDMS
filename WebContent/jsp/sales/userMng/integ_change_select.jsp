<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>积分变动查询</title>
<script type="text/javascript">
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/usermng/IntegationManage/integChangeSelect.json";
	var title = null;
	var columns = [
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "姓名",dataIndex: 'NAME',align:'center'},
				{header: "身份证号",dataIndex: 'ID_NO',align:'center'},
				{header: "创建日期",dataIndex: 'CREATE_DATE',align:'center'},
				{header: "调整前积分", dataIndex: 'INTEG_BEFORE', align:'center'},
				{header: "调整积分", dataIndex: 'THIS_CHANGE_INTEG', align:'center'},
				{header: "调整后积分", dataIndex: 'INTEG_AFTER', align:'center'},
				{header: "积分类型", dataIndex: 'INTEG_TYPE', align:'center',renderer:getItemValue},
				{header: "变动类型", dataIndex: 'CHANGE_TYPE', align:'center',renderer:getItemValue}
				
		      ];
	function executeQuery(){
		url= "<%=contextPath%>/sales/usermng/IntegationManage/integChangeSelect.json";
		__extQuery__(1);
	}
	function txtClr(value){
		$(value).value="";
	}
</script>
</head>

<body>
<div class="wbox">
<div class="navigation"><img src="/dms/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 积分管理 &gt; 积分变动查询</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0">
		<tr>
			<td align="right">人员姓名：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="name" id="name"  />
			</td>
			<td align="right">身份证号：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="idNo" id="idNo"   />	
			</td>
		</tr>
		<tr>
			<c:if test="${dutyType!=10431005}">
				<td width="20%" class="tblopt"><div align="right">选择经销商：</div></td>
				<td width="39%" >
      			  <input name="dealerCode" type="text" id="dealerCode" class="middle_txt" datatype="0,is_textarea,30" value="" size="20" />
                  <c:if test="${dutyType==10431001}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431002}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431003}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431004}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer6('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>                    
                    <input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
    			</td>
    			</c:if>
    			<c:if test="${dutyType==10431005}">
    			<td></td>
    			<td></td>
    			</c:if>
    			<td align="right">职位：</td>
				  <td align="left">
				 		 <script type="text/javascript">
			                genSelBoxExp("position",9996,"",true,"mini_sel","","false",'');
			            </script> 
			       </td>
		</tr>
		<tr>
		<td align="right">职位状态：</td>
		  <td align="left">
		 		 <script type="text/javascript">
	                genSelBoxExp("positionStatus",9994,"",true,"mini_sel","","false",'');
	            </script> 
	      </td>
	       <td align="right">积分类型：</td>
	       <td align="left">
	       		<script type="text/javascript">
	                genSelBoxExp("integType",9998,"",true,"mini_sel","","true",'');
				</script> 
	       </td>
		</tr>
	  <tr>
			<td align="center" colspan="2">
				<input type="button" class="normal_btn" onclick="executeQuery();" value="查询" id="addSub" />
<!--				<input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />-->
			</td>
			<td align="right" colspan="2">
			<input name="pagesizes" id="pagesizes" value="10" datatype="0,is_digit,20" style="width:30px"/>
			</td>
		</tr>
	</table>
	<br />
	<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end --> 
	
	
</form>
</div>
</body>

</html>
