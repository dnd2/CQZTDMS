<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<% String contextPath = request.getContextPath();%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>可变代码维护</title>
</head>

<body>
<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统业务参数维护 &gt; 可变代码维护
	</div>
	<form method="post" name="fm" id="fm">
		<div class="form-panel">
			<h2><img src="/CQZTDMS/jmstyle/img/search-ico.png" class="panel-query-title">可变代码维护</h2>
			<div class="form-body">
				<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
					<tr>
						<td class="table_query_2Col_label_6Letter">业务参数类型：</td>
						<td>
							<script type="text/javascript">
							genSelBoxExp("paraType",<%=Constant.PARA_TYPE%>,"",true,"short_sel u-select","","true",'');
							</script>
						</td>
						<td>&nbsp;&nbsp;&nbsp;</td>
						<td>
							<input name="searchBtn" type="button" class="u-button u-query" onclick="__extQuery__(1);" value="查询" />
							<input name="addBtn" type="button" class="u-button" onclick="addVarPara()" value="新增" />
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

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/VariableParaManage/queryVariablePara.json";
				
	var title = null;

	var columns = [
				{header: "参数代码", dataIndex: 'PARA_CODE', align:'center'},
				{header: "参数名称", dataIndex: 'PARA_NAME', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "是否需要下发", dataIndex: 'ISSUE', align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'PARA_ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//提交的超链接
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='updatePara(\""+ value +"\")'>[修改]</a>");
	}
	
	//修改的ACTION设置，修改可变代码信息
	function updatePara(value){
		fm.action = '<%=contextPath%>/sysbusinesparams/businesparamsmanage/VariableParaManage/updateVarParaInit.do?paraId=' + value;
	 	fm.submit();
	}
	
	
	//可变代码新增
	function addVarPara(){
	    var paraType = document.fm.paraType.value;
	    if(null==paraType||"".indexOf(paraType)==0){
	       MyAlert("请选择业务参数类型！");
	       return false;
	    }
		window.location.href='<%=contextPath%>/sysbusinesparams/businesparamsmanage/VariableParaManage/addVariableParaInit.do?paraType='+paraType;
	}

	function doCusChange() {}
//设置超链接 end
</script>
<!--页面列表 end -->
</body>
</html>