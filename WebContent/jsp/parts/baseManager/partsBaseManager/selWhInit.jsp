<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
    String contextPath = request.getContextPath();
%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<title>配件货位维护</title><script type="text/javascript">
var myPage;

var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/whQuery.json";
			
var title = null;

var columns = [
			{header:'选择',dataIndex:'WH_ID',renderer:myLink,align:'center',width:'4%'},
			{header: "仓库编码", dataIndex: 'WH_CODE', align:'center'},
			{header: "仓库名称", dataIndex: 'WH_NAME', align:'center'}
	      ];

//设置超链接  begin      

//设置超链接
function myLink(value,meta,record)
{
	return String.format('<input type="radio" name="code" value="'+record.data.PART_ID+'" onclick="getLabourData(\''+ record.data.WH_ID +'\',\''+ record.data.WH_CODE +'\')" />');
}
	function getLabourData(id,code){
       	var codes = document.getElementsByName("code");
       	for(var i=0;i<codes.length;i++){
       		if(codes[i].checked){
       		    if(!parentContainer.setWhList)
       				MyAlert('调用父页面setLaborList方法出现异常!');
       			else{
       				parentContainer.setWhList(id,code);  //从子层返回数据
       				_hide();
       			} 
       		}
       	}
       	
       }
   
$(function(){__extQuery__(1);});
</script>
</head>
<body>
	<form name="fm" id="fm">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 配件仓库选择
		</div>
		<div class="form-panel">

       	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>

			<div class="form-body">
				<table class="table_query">
					<tr>
						<td align="right">仓库编码：</td>
						<td>
							<input class="middle_txt" type="text" name="WH_CODE" />
						</td>
						<td align="right">仓库名称：</td>
						<td>
							<input class="middle_txt" type="text" name="WH_NAME" />
						</td>
						<td>
							<input class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);" />
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

</body>
</html>
