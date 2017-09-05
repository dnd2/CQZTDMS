<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商质保信息</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 参数设定 &gt; 经销商质保信息
		</div><br/>
		<form id="fm">
			<table class="table_list">
				<tr class="table_list_th">
					<input type="hidden" name="curPage" id="curPage" value="1"/>
					<input type="hidden" name="qaltAsrnId" id="qaltAsrnId" value=""/>
					<input type="hidden" name="queryBtn" id="queryBtn" value="" />
					<td nowrap="nowrap" align="center">
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;质保内容&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input class="normal_btn" type="button" value="新 增"  onclick="window.location.href='<%=contextPath %>/jsp/systemMng/paraConfig/dealerQaltAsrnInfoAdd.jsp'"/>
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		</form>
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</div>
<script type="text/javascript">

	var url = "<%=contextPath%>/sysmng/paramconfig/DealerQaltAsrnInfo/queryQaltAsrnInfo.json?command=1";

	//设置表格标题
	var title= null;

	var columns = [
					{header: "序号",width:'6%',  renderer:getIndex},
					{header: "质保代码", width:'25%', dataIndex: 'qaltAsrnCode',orderCol:"qalt_asrn_code"},
					{header: "质保名称", width:'35%',orderType:"pingyin", dataIndex: 'qaltAsrnName',orderCol:"qalt_asrn_name"},
					{id:'action',width:'20%',header: "操作", sortable: true,dataIndex: 'qaltAsrnId',renderer:myLink}
				  ];  

	//设置超链接
	function myLink(value){
        return String.format(
               "<a href=\"<%=contextPath%>/sysmng/paramconfig/DealerQaltAsrnInfo/getQaltAsrnInfo.do?qaltAsrnId="+value+"\">[修改]</a>&nbsp;&nbsp;"+
               "<a href='#' onClick='del("+value+");'>[删除]</a>");
    }
    function del(value){
    	$("qaltAsrnId").value = value;
       MyConfirm("是否确认删除质保内容?",delsub);
    }
    function delsub(){
    	makeNomalFormCall('<%=contextPath%>/sysmng/paramconfig/DealerQaltAsrnInfo/delQaltAsrnInfo.json',showResult,'fm');
    }
    function showResult(json){
		if(json.ACTION_RESULT == '1'){
			window.location.href = '<%=request.getContextPath()%>/sysmng/paramconfig/DealerQaltAsrnInfo/queryQaltAsrnInfo.do';
		}else if(json.ACTION_RESULT == '2'){
			MyAlert("删除失败！请确认删除的质保内容是否已经被使用！");
		}
	}
</script>
</body>
</html>
