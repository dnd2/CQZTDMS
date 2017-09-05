<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
	TmOrgPO orgPO = (TmOrgPO)request.getAttribute("orgobj");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.po.TmOrgPO"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<link href="/CQZTDMS/style/dtree1.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/CQZTDMS/js/web/dept_tree.js"></script>
	<script type="text/javascript" src="/CQZTDMS/js/web/dtree.js"></script>
<!-- <link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/my-grid-pager.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dealer_tree.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dept_tree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/common.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/framecommon/HashMap.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/framecommon/default.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/dict.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script> -->
<title>公司组织维护</title>
<style>textarea.form-control{height: 80px;margin-left: 10px;}</style>
<script>
var filecontextPath="<%=contextPath%>";
var dept_tree_url = "<%=contextPath%>/sysmng/orgmng/SgmOrgMng/initOrgTree.json"; 
var url = "<%=contextPath%>/sysmng/orgmng/SgmOrgMng/getOrgBusiness.json";
var modify_url="<%=contextPath%>/sysmng/orgmng/SgmOrgMng/modfiSgmOrgMng.json";
var porgname = "<%=request.getAttribute("porgname")==null?"":request.getAttribute("porgname")%>";
validateConfig.isOnBlur = false;
</script>
</head>
<body onload="pageload()">
<div class="wbox">
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 组织管理&gt; 公司组织维护</div>
		<form id="fm" name="fm" method="post">
			<input id="DEPT_ID" name="DEPT_ID" type="hidden" value="<%=orgPO.getParentOrgId() %>"/>
			<input id="D_ID" name="D_ID" type="hidden" value="<%=orgPO.getOrgId() %>"/>
			<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
			<input id="DEPT_CODE" name="DEPT_CODE" type="hidden" value="<%=orgPO.getOrgCode() %>"/>
			<input id="THIS_DEPT_STAT" name="THIS_DEPT_STAT" type="hidden" value="<%=orgPO.getStatus() %>"/>
			<input id="areaIds" name="AREA_IDS" type="hidden" value=""/>
			  <div class="dtree" id="deptt"></div>
			<div class="form-panel">
				 <h2>公司组织维护</h2>
				 <div class="form-body">
					  <table class="table_query" border="0">
							<tr>
								<td class="table_query_2Col_label_4Letter right" nowrap="nowrap">部门代码：</td>
								<td class="table_query_4Col_input" nowrap="nowrap" class="table_query_2Col_input" id="pcode"><%=orgPO.getOrgCode() %>
								</td>
								<td class="table_query_2Col_label_4Letter right" nowrap="nowrap">部门名称：</td>
								<td class="table_query_2Col_input" nowrap="nowrap">
									<input name="DEPT_NNAME" id="DEPT_NNAME"  type="text" value="<%=orgPO.getOrgName() %>" class="middle_txt" datatype="0,is_null,30"/></td>
							</tr>
							<tr>
								<td class="table_query_2Col_label_4Letter right" nowrap="nowrap">上级部门：</td>
								<td class="table_query_2Col_input" nowrap="nowrap" id="sjbmm">
									<input name="DEPT_NAME" id="DEPT_NAME" onclick="dt.initDeptTree();"
										style="cursor: pointer;"
						type="text" class="middle_txt"  datatype="1,is_null,30" readonly="readonly" />
								</td>
								<td class="table_query_2Col_label_4Letter right" nowrap="nowrap">部门状态：</td>
								<td class="table_query_2Col_input" nowrap="nowrap">
									<script type="text/javascript"> genSelBox("DEPT_STAT",<%=Constant.STATUS%>,<%=orgPO.getStatus() %>,false,"u-select","");</script>
								</td>
							</tr>
							<tr>
								<td class="table_query_2Col_label_4Letter right" nowrap="nowrap">组织类型：</td>
								<td class="table_query_2Col_input" nowrap="nowrap">
								<select class="min_sel u-select" name="ORG_TYPE" id="ORG_TYPE">
									<option value="<%=Constant.DUTY_TYPE_COMPANY %>" <% if(orgPO.getDutyType().equals(Constant.DUTY_TYPE_COMPANY)) {%>selected="selected"<% } %>>公司</option>
									<option value="<%=Constant.DUTY_TYPE_LARGEREGION %>" <% if(orgPO.getDutyType().equals(Constant.DUTY_TYPE_LARGEREGION)) {%>selected="selected"<% } %>>大区</option>
									<option value="<%=Constant.DUTY_TYPE_SMALLREGION %>" <% if(orgPO.getDutyType().equals(Constant.DUTY_TYPE_SMALLREGION)) {%>selected="selected"<% } %>>小区</option>
								</select>
								</td>
								<td class="table_query_2Col_label_4Letter" nowrap="nowrap"></td>
								<td class="table_query_2Col_input" nowrap="nowrap">
								</td>
							</tr>
							<tr>
								<td class="table_query_2Col_label_4Letter right" nowrap="nowrap">备注：</td>
								<td class="table_query_2Col_input" nowrap="nowrap" colspan="3">
									<textarea name="DEPT_DESC" id="DEPT_DESC" class="form-control remark" value="<%=orgPO.getOrgDesc() == null ? "" : orgPO.getOrgDesc() %>" maxLength="100"></textarea>
									</td>
								</tr>
							<tr>
								<td class="center" colspan="4" align="center">
									<input name="button2" type="button" class="u-button" onclick="sub()" value="修 改"/>
									<input name="button" type="button" class="u-button u-cancel" onclick="toGoSgmOrgSearch()" value="取 消"/>
								</td>
							</tr>
						</table>
						<!--<jsp:include page="${ctx}/queryPage/orderHidden.html" />-->
						<table class='table_query' style=display:none>
							<tr>
								<td align="center" nowrap="nowrap">选择业务范围</td>
								<td align="center" nowrap="nowrap">业务范围代码</td>
								<td align="center" nowrap="nowrap">业务范围名称</td>
							</tr>
							<c:forEach items="${ps}" var="org">
								<tr>
									<td align="center" nowrap="nowrap">
											<input type='checkbox' name='ad' id="${ org.AREA_ID}"  value="${ org.AREA_ID}" checked="checked"/>
									
									</td>
									<td align="center" nowrap="nowrap">${org.AREA_CODE }</td>
									<td align="center" nowrap="nowrap">${org.AREA_NAME }</td>
								</tr>
							</c:forEach>
						</table>
					</form>
					<%-- <jsp:include page="${ctx}/queryPage/pageDiv.html" /> --%>
					<table class="table_query" border="0" id="table1" style="display:none"> 
						<tr>
							<td class="center" colspan="4" align="center">
								<input name="button3" type="button" class="u-button" onclick="checkAll()" value="全选"/>
								<input name="button4" type="button" class="u-button" onclick="checkNoAll()" value="全不选"/>
							</td>
						</tr>
					</table>
				 </div>
			</div>
</div>	

<script type="text/javascript">
var title = null;
var columns = [ {header: "选择业务范围",dataIndex: 'AREA_ID',width:"50px",renderer:myLink},
				{header: "业务范围代码",dataIndex: 'AREA_CODE',align:'center'},
				{header: "业务范围名称",dataIndex: 'AREA_NAME',align:'center'}
		      ];
function myLink(value,meta,rec){
	var relationId="";
	var data = rec.data;
	relationId=data.RELATION_ID;
	if(relationId&&(relationId+"").length>0)
	{
		return "<input type='checkbox' name='ad' id=\""+data.AREA_ID+"\"  value=\""+data.AREA_ID+"\"  checked />"
   }else
   {
   	
   		return "<input type='checkbox' name='ad' id=\""+data.AREA_ID+"\"  value=\""+data.AREA_ID+"\" />"
   }
   
}
function sub() {

	addString();
	sendAjax(modify_url, subBack,'fm') ;
}

function subBack(redata) {
	if(redata.st != null && redata.st == "succeed") {
		toGoSgmOrgSearch();
	}else if(redata.st != null && redata.st == "deptCode_error"){
		MyAlert(redata.st);
		$('#DEPT_CODE')[0].select();
	}else if(redata.st != null && redata.st == "deptName_error"){
		showError('ermsg','erdiv','DEPT_NNAME','部门名称重复,请重新输入!',170);
		$('#DEPT_NNAME')[0].select();
	} else {
		MyAlert("未知错误");
	}
}

function toGoSgmOrgSearch() {
	window.location = "<%=contextPath%>/sysmng/orgmng/SgmOrgMng/sgmOrgMngQueryInit.do";
}

function pageload() {
	if(<%=orgPO.getParentOrgId() %> == <%=orgPO.getOrgId() %>) {
		$('#sjbmm').html(porgname);
	} else {
		$('#DEPT_NAME')[0].value = porgname;
	}
	//__extQuery__(1);
	//makeNomalFormCall(url, callBack,'fm') ;
}
function checkAll()
{
  var ad=document.getElementsByName("ad");
  if(!ad) return;
  for(var i=0;i<ad.length;i++)
  {
	  ad[i].checked=true;
  }
}
function checkNoAll()
{
  var ad=document.getElementsByName("ad");
  if(!ad) return;
  for(var i=0;i<ad.length;i++)
  {
	  ad[i].checked=false;
  }
}
function addString()
{
  var s="";
  var ad=document.getElementsByName("ad");
  if(!ad) return;
  for(var i=0;i<ad.length;i++)
  {
	  if(ad[i].checked)
	  {
		if(s&&s.length>0)
		s+=","+ad[i].value;
		else
		s=ad[i].value;	 
	  }
  }
  document.getElementById("areaIds").value=s;
}
</script>
</body>
</html>
