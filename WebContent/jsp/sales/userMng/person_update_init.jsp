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
<title>机构人员修改查询</title>
<script type="text/javascript">
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/usermng/UserManage/personSelect.json";
	var title = null;
	var columns = [
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "姓名",dataIndex: 'NAME',align:'center'},
				{header: "性别",dataIndex: 'GENDER',align:'center',renderer:getItemValue},
				{header: "身份证号",dataIndex: 'ID_NO',align:'center'},
				{header: "电子邮件",dataIndex: 'EMAIL',align:'center'},
				{header: "联系电话", dataIndex: 'MOBILE', align:'center'},
				{header: "职位", dataIndex: 'POSITION', align:'center',renderer:getItemValue},
				{header: "入职日期", dataIndex: 'ENTRY_DATE', align:'center'},
				{header: "是否投资人", dataIndex: 'IS_INVESTOR', align:'center',renderer:getItemValue},
				{header: "所属银行", dataIndex: 'BANK', align:'center',renderer:getItemValue},
				{header: "职位状态", dataIndex: 'POSITION_STATUS', align:'center',renderer:getItemValue},
				//{header: "备注", dataIndex: 'REMARK', align:'center'},
				{header: "操作", dataIndex: 'PERSON_ID', align:'center',renderer:myLink}
		      ];
	function myLink(value,meta,record){
		return String.format("<a href='#' onclick='personUpdate("+record.data.PERSON_ID+")'>[人员修改]</a>/<a href='#' onclick='personIntegUpdate("+record.data.PERSON_ID+")'>[积分清零]</a>");
	}
	
	function personUpdate(person_id){
			window.location.href="<%=contextPath%>/sales/usermng/UserManage/personUpdateLoad.do?personId="+person_id;
	}
	function personIntegUpdate(person_id){
		if(confirm("确认积分清零？")){
			var urls="<%=contextPath%>/sales/usermng/UserManage/personIntegUpdate.json?personId="+person_id;
			makeFormCall(urls, integUpdateReturn, "fm") ;
		}
	}
	function integUpdateReturn(json){
		var subFlag = json.subFlag ;
		if(subFlag == 'success') {
			MyAlert("清零成功!") ;
		} else {
			MyAlert("清零失败!") ;
		}
	}
	function executeQuery(){
		url= "<%=contextPath%>/sales/usermng/UserManage/personSelect.json";
		__extQuery__(1);
	}
	function txtClr(value){
		$(value).value="";
	}
</script>
</head>

<body onload="executeQuery();">
<div class="wbox">
<div class="navigation"><img src="/dms/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 人员管理 &gt; 人员修改查询</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0">
		<tr>
			<td align="right">人员姓名：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="name" id="name" value=""  />
			</td>
			<td align="right">身份证号：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="idNO" id="idNO" value=""  />	
			</td>
		</tr>
		<tr>
          <td align="right">电子邮件：</td>
		  <td align="left">
		  	<input type="text" class="middle_txt" name="email" id="email" value=""  />
		  </td>
		  <td align="right">联系电话：</td>
		  <td align="left">
		  	<input type="text" class="middle_txt" name="mobile" id="mobile"  />
		  </td>
	  </tr>
		<tr>
			<td align="right">性别：</td>
			<td align="left">
				<script type="text/javascript">
	                genSelBoxExp("gender",1003,"",true,"mini_sel","","false",'');
	            </script>
	         </td>
	          <td align="right">是否投资人：</td>
			  <td align="left"><script type="text/javascript">
		                genSelBoxExp("isInvestor",9995,"",true,"mini_sel","","false",'');
		            </script>
		      </td>
		</tr>
		<tr>
			 <td align="right">职位：</td>
		  <td align="left">
		 		 <script type="text/javascript">
	                genSelBoxExp("position",9996,"",true,"mini_sel","","false",'');
	            </script> 
	       </td>
	       <td width="20%" class="tblopt"><div align="right">选择经销商：</div></td>
				<td width="39%" >
      			  <input name="dealerCode" type="text" id="dealerCode" class="middle_txt" value="" size="20" />
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
         <td align="right" style="display:none;"></td>
		  <td align="left" style="display:none;">
		 		<select name="status" >
					<option value="99991003" >审核通过</option>
				</select>
	       </td>
	       
	  </tr>
	  <tr>
		<td align="center" colspan="2">
			<input type="button" class="normal_btn" onclick="executeQuery();" value="查询" id="addSub" />
<!--			<input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />-->
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
