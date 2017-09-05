<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>首保备件关系维护</title>
<script type="text/javascript" src="<%=contextPath%>/js/web/util/util.js"></script>
</head>
<body onload="__extQuery__(1);">
\<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" /> 
		&nbsp;当前位置： 售后服务管理 &gt;基础数据&gt;备件三包期维护
	</div>
	<%-- 查询条件 开始 --%>
	<form method="post" name="fm" id="fm" >
	<input id="aty" name="aty" type="hidden"/>
	<%-- 三包天数--%>
	<input id="wm" name="wm" type="hidden"/>
	<%-- 三包里程 --%>
	<input id="wg" name="wg" type="hidden"/>
	<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
	<div style="background-color: #F0F7F2; width: 100%;text-align: center;">
		<table class="table_query">
		<tr>
			<td class="right">备件代码：</td>
			<td class="left">
				<input type="text"  name="part_code"  id="part_code" class="middle_txt" />
			</td>
			<td class="right">备件名称：</td>
			<td class="left">
				<input type="text"  name="part_name"  id="part_name" class="middle_txt" />
			</td>	
		</tr>
		<tr>
			<td class="right">三包天数：</td>
			<td class="left">
				<input type="text"  name="wr_months"  id="wr_months" class="middle_txt" />
			</td>
			<td class="right">三包里程：</td>
			<td class="left">
				<input type="text"  name="wr_mileage"  id="wr_mileage" class="middle_txt" />
			</td>	
		</tr>
		<tr>
			<td colspan="4" class="center">
				<input class="u-button u-query" type="button" id="queryBtn" name="button1" value="查 询"  onclick="__extQuery__(1);"/>&nbsp;
				<input class="u-button u-submit" type="button" id="updateBtn" name="button1" value="修 改"  onclick="toUpdate();"/>
				<!-- <input class="normal_btn" type="button" id="deleteBtn" name="button1" value="删 除"  onclick="toDelete();"/>  -->
			</td>
		</tr>
		</table>
	</div>
	</div>
	</div>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" /></div>
  <!--分页 end --> 
</form>
</body>
<script type="text/javascript">
function queryInit()
{
		__extQuery__(1);
}

var myPage;
//查询路径
var url = "<%=contextPath%>/afterSales/base/SparePartsThreeBagAction/querySparePartsThreeBagInit.json";
		
var title = null;

var columns = [
			{header: "<input type='checkbox' id='idt' name='idt' onclick='selectAll();'/>",sortable: false,align:'center',dataIndex: 'PART_ID',renderer:myCheckBox},
			{header: "备件代码", dataIndex: 'PART_CODE', align:'center', style:"text-align: center;"},
			{header: "备件名称",dataIndex: 'PART_NAME',align:'center'},
			{header: "三包月份", dataIndex: 'WR_MONTHS', align:'center', style:"text-align: center;",renderer:myLink},
			{header: "三包里程/公里", dataIndex: 'WR_MILEAGE', align:'center', style:"text-align: center;",renderer:myLink1}
			];
      


	//设置超链接

	function myLink(value, meta, record) {
		var update_btn = "<input type='text' class='middle_txt' name='wm' id='wm"+record.data.PART_ID+"'  value='" + value + "'/>";
		return String.format(update_btn);
	}

	function myLink1(value, meta, record) {
		var update_btn = "<input type='text' class='middle_txt'  name='wg' id='wg"+record.data.PART_ID+"'  value='" + value + "'/>";
		return String.format(update_btn);
	}
	function myCheckBox(value, meta, record) {
		var check_btn = "<input type='checkbox' name='ids' id='ids'  value='" + value + "'/>";
		return String.format(check_btn);
	}
	//全选
	function selectAll() {
		var td1 = document.getElementsByName("ids");
		var th1 = document.getElementsByName("idt");
		var l = td1.length;
		for (var i = 0; i < l; i++) {
			if (th1[0].checked == true) {
				td1[i].checked = true;
			} else {
				td1[i].checked = false;
			}
		}
	}

	//修改
	function toUpdate() {
		var str = document.getElementsByName("ids");
		var objarray = str.length;
		var chestr = "";
		var cheMonth = "";
		var cheMileage = "";
		for (var i = 0; i < objarray; i++) {
			if (str[i].checked == true) {
				chestr += str[i].value + ",";
				var reg1=/^[1-9][0-9]{0,3}$/;
				if (document.getElementById("wm" + str[i].value).value == "") {
					MyAlert("三包天数不能为空！");
					return;
				}else if(!reg1.test(document.getElementById("wm" + str[i].value).value)){
					MyAlert("请正确填写三包天数！");
					document.getElementById("wm" + str[i].value).value == ""
					return;
				}
				var reg = /^[0-9]+.[0-9]{0,2}$/;
				if (document.getElementById("wg" + str[i].value).value == "") {
					MyAlert("三包里程不能为空！");
					return;
				}else if(!reg.test(document.getElementById("wg" + str[i].value).value)){
					MyAlert("请正确填写三包里程！");
					document.getElementById("wg" + str[i].value).value == "";
					return;
				}
				cheMonth += document.getElementById("wm" + str[i].value).value+ ",";
				cheMileage += document.getElementById("wg" + str[i].value).value+ ",";
			}
		}
		if (chestr == "") {
			MyAlert("请选择需修改的信息！");
			return;
		}
		document.getElementById("aty").value = chestr;
		document.getElementById("wm").value = cheMonth;
		document.getElementById("wg").value = cheMileage;
		makeNomalFormCall("<%=contextPath%>/afterSales/base/SparePartsThreeBagAction/updateSparePartsThreeBag.json",showUpdateValue,'fm','updateBtn'); 
	}


	function showUpdateValue(json) {
		if (json.succeed == "1") {
			MyAlert("修改成功！");
			queryInit();
		} else {
			MyAlert("修改失败");
		}
	}


</script>
</html>