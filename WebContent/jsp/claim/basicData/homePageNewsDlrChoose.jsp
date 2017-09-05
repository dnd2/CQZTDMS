<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	Long userId = (Long)request.getAttribute("logonUser");
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>首页新闻查询</title>
<style type="text/css">
	input[type='checkbox']{cursor: pointer;}
	.table_list_th{
		background-color:#9FF;
		z-index:10;
		/*定位，必定位*/
		position:relative;
		/*通过某种机制，时该执行，IE8不在支持，关键语句*/
		top:expression(0);
	}
	.table_list{height: 80px; overflow-y:scroll; }
</style>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  // 初始化时间控件
	}
	function orgChoose(obj) {
		var orgs = document.getElementsByName('org');
		if(obj.checked == true) {
			for(var i=0;i<orgs.length;i++) {
				if(orgs[i].lang == obj.value) {
					orgs[i].checked = true;
				}
			}
		}
		else
		{
			for(var i=0;i<orgs.length;i++) {
				if(orgs[i].lang == obj.value) {
					orgs[i].checked = false;
				}
			}
		}
		//__extQuery__(1);
	}
	function checkAll(){
		var obj = document.getElementById("org_big");
		var smallorg = document.getElementById("org_small");
		var bOrgObjs = document.getElementsByName('bOrg');
		if(obj.checked){
			smallorg.checked = true;
			for(var i=0;i<bOrgObjs.length;i++) {
				bOrgObjs[i].checked = true;
			}
		}else{
			smallorg.checked = false;
			for(var i=0;i<bOrgObjs.length;i++) {
				bOrgObjs[i].checked = false;
			}
		}
		checkSmallOrg();
	}

	function checkSmallOrg(){
		var smallorg = document.getElementById("org_small");
		var orgs = document.getElementsByName('org');
		if(smallorg.checked){
			for(var i=0;i<orgs.length;i++) {
				//smallorg.checked=true;
				orgs[i].checked = true;
			}
		}else{
			for(var i=0;i<orgs.length;i++) {
				//smallorg.checked=false;
				orgs[i].checked = false;
			}
		}
	}
	function chooseConfim() {
		var dealer = document.getElementsByName('dealer');
		var dealerArr = new Array();
		for(var i=0;i<dealer.length;i++) {
			if(dealer[i].checked == true) {
				var tempArr = dealer[i].value.split(",");
				dealerArr.push(tempArr);
			}
		}
		
		parentContainer.initUserList(dealerArr);
		_hide();
	}
</script>
</head>
<body>
<form name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 个人信息管理&gt;个人信息管理&gt;首页新闻</div>
  <input type="hidden" name="dealerType" id="dealerType" value="${dealerType }" />
   <table  class="table_query">
   		<tr>
   			<td><input type='checkbox' checked="checked" onclick="checkAll();" id='org_big' name="org_big"/>大区：</td>
   			<td>
   				<c:forEach items="${orgList }" var="list">
		       		<input type='checkbox' checked="checked" onclick="orgChoose(this)" name='bOrg' value="${list.ORG_ID }"/>${list.ORG_NAME }
		       </c:forEach>
   			</td>
   		</tr>
   		<tr>
   			<td width="80"><input name="org_small" id='org_small' onclick="checkSmallOrg();" type="checkbox" checked="checked" />省份：</td>
   			<td>
   				<table class="table_query">
	   				<tr>
		   				<c:forEach items="${proviceList }" var="list" varStatus="status">
				       		<c:choose>
				 				<c:when test="${status.last == true }">
				 					<td width='24'>
				 						<input name="org" type="checkbox" checked="checked" lang="${list.PARENT_ORG_ID }" value="${list.ORG_ID }"/>
				 					</td>
				 					<td colspan='${6-status.index%6 }'>
				 						${list.ORG_NAME }
				 					</td>
				 					</tr>
				 				</c:when>
				 				<c:when test="${(status.index+1) % 6 == 0 && (status.index+1) > 5}">
				 					<td width='24'><input name="org" type="checkbox" checked="checked" lang="${list.PARENT_ORG_ID }" value="${list.ORG_ID }"/></td>
				 					<td width='120'>${list.ORG_NAME }</td>
				 					</tr><tr>
				 				</c:when>
				 				<c:otherwise>
				 					<td width='24'><input name="org" type="checkbox" checked="checked" lang="${list.PARENT_ORG_ID }" value="${list.ORG_ID }"/></td>
				 					<td width='120'>${list.ORG_NAME }</td>
				 				</c:otherwise>
				 			</c:choose>
				       </c:forEach>
			       </tr>
		       </table>
   			</td>
   		</tr>
   		<tr>
   			<td colspan="2" class="center">
   				<input type="button" id="queryBtn" value="查 询" class="u-button" onclick="__extQuery__(1)"/>
   				&nbsp;&nbsp;
   				<input type="button" id="queryBtn" value="确 认" class="u-button" onclick="chooseConfim()"/>
   			</td>
   		</tr>
  </table>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/HomePageNews/dlrUserChooseQuery.json";
	var title = null;
	
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{
			header: "<input type='checkbox' onclick='selectAll(this,\"dealer\")'/>",dataIndex: 'DEALER_CODE',align:'center',
			renderer:function(value,mata,record){
				return "<input type='checkbox' name='dealer' id='dealer' value='"+record.data.DEALER_ID+","+record.data.DEALER_CODE+","+record.data.DEALER_NAME+","+getItemValue(record.data.DEALER_TYPE)+"'/>";
			}
		},
		{header: "经销商编码",sortable: false,dataIndex: 'DEALER_CODE',align:'center'},
		{header: "经销商类型",sortable: false,dataIndex: 'DEALER_TYPE',renderer:getItemValue},
		{header: "经销商名称",sortable: false,dataIndex: 'DEALER_NAME',align:'center',style:'text-align:left'}
      ];
</script>
</body>
</html>	