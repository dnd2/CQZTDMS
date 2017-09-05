<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>坐席组管理</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body onload="__extQuery__(1)">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 基础设定 &gt;坐席组管理</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />坐席组管理</th>
			
			<tr>
				<td align="right" nowrap="true">组名称：</td>
				<td align="left" nowrap="true">
					<input type="text" id="seatsName" name="seatsName"/>
				</td>
			</tr>
	
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
					&nbsp;
          			<input name="addBtn" type="button" class="normal_btn" onclick="addSeatsTeam();" value="新增" />
          			&nbsp;
          			<input name="delBtn" type="button" class="normal_btn" onclick="delSeatsTeam();" value="删除" />
        		</td>
			</tr>
		</table>
		
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>
<script type="text/javascript">
	//坐席组新增
	function addSeatsTeam(){
		window.location.href='<%=contextPath%>/customerRelationships/baseSetting/seatsTeamSet/addOrUpdateSeatsTeamSet.do';
	}
	var delIds = new Array(); 
	//坐席组删除
	function delSeatsTeam(){
		var cnt = 0;
		var chk=document.getElementsByName("stIds");
		var l = chk.length;
		delIds.splice(0,delIds.length);
		for(var i=0;i<l;i++){
			if(chk[i].checked){
		       cnt++;
		       delIds.push(chk[i].value);
			}
		 }
		if(cnt==0){
		    MyAlert("请选择删除数据！");
		    return ;
		}else{
			MyConfirm("是否确认删除？",delsubmit,"");
		}
	}

	function delsubmit(){
		makeNomalFormCall('<%=contextPath%>/customerRelationships/baseSetting/seatsTeamSet/delSeatsTeamSetSubmit.json?ids='+delIds,delBack,'fm','');
	}
	//删除回调方法：
	function delBack(json) {
		if(json.success != null && json.success=='true'){
			document.getElementById("delBtn").disabled = true;
			MyAlertForFun("删除成功",sendPage);
		}else{
			MyAlert("删除失败！请联系管理员");
			document.getElementById("delBtn").disabled = false;
		}
	}
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/baseSetting/seatsTeamSet/querySeatsTeamSet.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"stIds\")'/>", width:'8%',sortable: false,dataIndex: 'STID',renderer:myCheckBox},
				{header: "组名称",dataIndex: 'STNAME',align:'center'},
				{header: "编号",dataIndex: 'STCODE',align:'center'},
				{header: "备注", dataIndex: 'STMEMO', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'STID',renderer:myLink}
		      ];
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='stIds' value='" + value + "' />");
	}
	function myLink(value,meta,record){
		return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='修改'/>");
	}
	function viewDetail(value){		
		window.location.href='<%=contextPath%>/customerRelationships/baseSetting/seatsTeamSet/addOrUpdateSeatsTeamSet.do?id='+value ;
	}
	//页面跳转：
	function sendPage(){
		window.location.href = "<%=contextPath%>/customerRelationships/baseSetting/seatsTeamSet/seatsTeamSetInit.do";
	}
</script>
</body>
</html>