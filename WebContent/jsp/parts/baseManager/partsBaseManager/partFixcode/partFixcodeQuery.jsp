<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件变量查询</title>

<script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartFixcodeQuery/partFixcodeQuery.json";
				
	var title = null;

	var columns = [
					{header: "序号", align:'center', renderer:getIndex,width:'7%'},
					{header: "变量类型", dataIndex: 'FIX_GROUPNAME', align:'center'},
					{header: "变量名称", dataIndex: 'FIX_NAME', style: 'text-align: center;'},
//					{header: "变量值", dataIndex: 'FIX_VALUE', align:'center'},
					{header: "是否有效", dataIndex:'STATE', align:'center', renderer: getItemValue},
					{id:'action',header: "操作",sortable: false,dataIndex: 'FIX_ID',renderer:myLink ,align:'center'}
		      ];

//设置超链接  begin      
	
	//设置超链接
	function myLink(value,meta,record)
	{    
        var state = record.data.STATE;
        if(state=='<%=Constant.STATUS_DISABLE %>'){
            return String.format("<a href=\"#\" onclick='valid(\""+value+"\")'>[有效]</a>");
        }	    
  		return String.format("<a href=\"#\" onclick='sel(\""+value+"\")'>[维护]</a><a href=\"#\" onclick='cel(\""+value+"\")'>[失效]</a>");
	}
	
	//打开制造商信息修改页面
	function sel(value)
	{
		window.location.href='<%=contextPath%>/parts/baseManager/partsBaseManager/PartFixcodeQuery/partFixcodeModInit.do?fixId='+value;
	}
	//失效
	function cel(value){
	    MyConfirm("确定要失效?",celAction,[value]);
	}
	
	//有效
	function valid(value){
	     MyConfirm("确定要有效?",validAction,[value]);
	}
	
	function celAction(value){
	    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartFixcodeQuery/partNotState.json?Id='+value+'&curPage='+myPage.page;
	   makeNomalFormCall(url,handleCel,'fm');
	}
	
	function validAction(value){
	    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartFixcodeQuery/partEnableState.json?Id='+value+'&curPage='+myPage.page;
	    makeNomalFormCall(url,handleCel,'fm');
	}
	
	function handleCel(jsonObj) {
	  if(jsonObj!=null){
	     var success = jsonObj.success;
	     MyAlert(success);
	      __extQuery__(jsonObj.curPage);
	  }
   }
	function partAdd(){
		 window.location.href = '<%=contextPath %>/parts/baseManager/partsBaseManager/PartFixcodeQuery/partFixcodeAddInit.do';
	}
</script>
</head>
<body onload="__extQuery__(1);"> <!-- onunload='javascript:destoryPrototype()' loadcalendar(); -->
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
		配件管理 &gt;  基础信息管理 &gt; 配件基础信息维护 &gt; 配件变量维护
</div>
<form method="post" name ="fm" id="fm">
	<div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
		<div class="form-body">
			<table class="table_query">
				<tr>
				<td class="right" width="10%" align="right" >变量类型：</td>
				<td width="20%">
					<script type="text/javascript">
						genSelBoxExp("FIXCODETYPE",<%=Constant.FIXCODE_TYPE%>,"-1",true,"short_sel u-select",'',"false",'');
						</script>
				</td>
				<td class="right" width="10%" align="right">变量名称：</td>
				<td width="20%"  ><input class="middle_txt" type="text" name="FIXNAME" /></td>
				<td class="right" width="10%" align="right">是否有效：</td>
				<td width="20%" >
						<script type="text/javascript">
						genSelBoxExp("STATE",<%=Constant.STATUS%>,"<%=Constant.STATUS_ENABLE %>",true,"short_sel u-select",'',"false",'');
						</script>
				</td>
			</tr>
			<tr>
				<td class="formbtn-aln" align="center" colspan="6">
					<input class="u-button" type="button" value="查 询" onClick="__extQuery__(1)"/>
					<input class="u-button" type="button" value="新 增" onclick="partAdd();"/>
				</td>
			</tr>
			</table>
		</div>
	</div>
	
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

</div>
</body>
</html>