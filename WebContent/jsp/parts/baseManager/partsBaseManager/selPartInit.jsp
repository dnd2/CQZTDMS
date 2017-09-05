<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<title>配件货位维护</title>
<script type="text/javascript">
$(function(){__extQuery__(1)});
var myPage;

var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/partQuery.json";
			
var title = null;

var columns = [
			{header:'选择',dataIndex:'PART_ID',renderer:myLink,align:'center',width:'4%'},
			{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center'},
			{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'}
	      ];

//设置超链接  begin      

//设置超链接
function myLink(value,meta,record)
{
	return String.format('<input type="radio" name="code" value="'+record.data.PART_ID+'" onclick="getLabourData(\''+ record.data.PART_ID +'\',\''+ record.data.PART_OLDCODE +'\',\''+record.data.PART_CNAME+'\')" />');
}

function getLabourData(id,code, name){
	var codes = document.getElementsByName("code");
	for(var i=0;i<codes.length;i++){
		if(codes[i].checked){
		    if(!__parent().setPartList){
				MyAlert('调用父页面setLaborList方法出现异常!');
		    }else{
				__parent().setPartList(id,code,name);  //从子层返回数据
				_hide();
			} 
		}
	}
       	
}
</script>
</head>
<body>
	<div class="wbox">
		<form name="fm" id="fm">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 配件选择
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">配件编码：</td>
							<td>
								<input class="middle_txt" type="text" name="PART_CODE" />
							</td>
							<td class="right">配件名称：</td>
							<td>
								<input class="middle_txt" type="text" name="PART_NAME" />
							</td>
							<td>
								<input class="u-button u-query" type="button" value="查 询" onclick="__extQuery__(1);" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
		</form>
	</div>
</body>
</html>
