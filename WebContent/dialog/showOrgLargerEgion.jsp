<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	String haveOwn = request.getParameter("haveOwn");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<script type="text/javascript">

	var index =1;
	var myPage;
	var url =  "<%=contextPath%>/sales/planmanage/RequirementForecast/OemRequireForecastSearch/OrgRequireFindAll.json";
	var title = null;
	var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'ORG_ID',renderer:myLink},
				{header: "序号", renderer:getIndexData, align:'center'},
				{header: "大区编码", dataIndex: 'ORG_CODE', align:'center'},
				{header: "大区名称", dataIndex: 'ORG_NAME', align:'center'}
		      ];

	function myLink(value,meta,rec){
		var data = rec.data;	
		return "<input type='checkbox'  name='pose_id' value='"+data.ORG_CODE+"' />";
    }
	
	function setCheckModel(){
		var reCode="";
		var materialCheckBoxs=document.getElementsByName("pose_id");
		if(!materialCheckBoxs)return;
		for(var i=0;i<materialCheckBoxs.length;i++)
		{
			if(materialCheckBoxs[i].checked)
			{
				if(reCode && reCode.length > 0)
					reCode += "," + materialCheckBoxs[i].value;
				else
					reCode = reCode+materialCheckBoxs[i].value;
		    }
		}
		if (parent.$('inIframe')) {					
			parentContainer.showInfo(reCode);
			parentContainer._hide();			
		}else {			
			parentContainer.showInfo(code);
			parentContainer._hide();
		}
	}
	
	
	function getIndexData(){
		return index++;
	}
	
	function findQuery(){
		index=1;
		__extQuery__(1);
	}
	
	function submit_(code,name){
		if (parent.$('inIframe')) {					
			parentContainer.showInfo(code,name);
			parentContainer._hide();			
		}else {			
			parentContainer.showInfo(code,name);
			parentContainer._hide();
		}
	}
	
</script>  

<title>需求预测查询</title>
</head>
<body onunload='javascript:destoryPrototype();' onLoad ='findQuery();' > 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  计划管理 &gt; 需求预测 &gt; 需求预测查询</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="haveOwn" name="haveOwn" value=<%=CommonUtils.checkNull(haveOwn)%>  />
		<table class="table_query" border="0">
			<tr>
				<td width="15%" class="tblopt" align="left"><div align="right">大区编译：</div></td>
				<td width="15%" >
      				<input type="text" id="orgCode" name="orgCode" class="middle_txt" size="20"   />
    			</td>
    			<td width="15%" class="tblopt" align="left"><div align="right">大区名称：</div></td>
				<td width="15%" >
      				<input type="text" id="orgName" name="orgName" class="middle_txt" size="20"   />
    			</td>
				<td class="table_query_3Col_input" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" class="normal_btn" onclick="findQuery();" value="查 询" id="queryBtn" /> 
					<input type="button" class="normal_btn" onclick="setCheckModel();" value="确认" id="queryBtn" /> 
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
	
</div>
  
</body>
</html>