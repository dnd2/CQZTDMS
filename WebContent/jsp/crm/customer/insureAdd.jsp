<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">

</script>
<style type="text/css" >
 .mix_type{width:100px;}
 .min_type{width:176px;}
 .mini_type{width:198px;}
 .long_type{width:545px;}
 .xlong_type{width:305px}
 .nx_type{width:160px;}
</style>
<title>实销信息上报</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="loadcalendar();"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  潜客管理 &gt; 客户管理 &gt;保险公司添加 </div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<input type="hidden" id="ctmId" name="ctmId" value="${ctmId}"/>
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">保险公司：</div></td>
				<td width="39%" >
	                 <input type="hidden" id="enCompany" name="enCompany" value="" />
		      		<div id="ddtopmenubar1" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:173px;" rel="ddsubmenu1" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6051', loadEnCompany);" deftitle="--请选择--">
								--请选择--
								</a>
								<ul id="ddsubmenu1" class="ddsubmenustyle"></ul>
							</li>
						</ul> 	
    			</td>
    			<td width="20%" class="tblopt"><div align="right">保险时间：</div></td>
				<td width="39%" >
      			  <input name="enTime" type="text" id="enTime"  class="nx_type" readonly    />
        		  <input class="time_ico" type="button" onClick="showcalendar(event, 'enTime', false);" />
    			</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">险种：</div></td>
				<td width="39%" >
      				<input type="text" id="enMoney" name="enVar" class="min_type" size="20"   />
    			</td>
    			<td width="20%" class="tblopt"><div align="right">金额：</div></td>
				<td width="39%" >
      				<input type="text" id="enMoney" name="enMoney" class="min_type" size="20"   />
    			</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">备注：</div></td>
				<td width="39%" colspan="3" >
      				<textarea cols="81" rows="5" name=enRemark>
      					
      				</textarea>
    			</td>
			</tr>
			<tr>
				<td   colspan="4" align="center">
					<input type="button" class="normal_btn" onclick="addInsure();" value="添加" id="queryBtn" /> 
				</td>
			</tr>
		</table>
		
</form>
	
</div>
<script type="text/javascript">
	function submit_(pose_id,pose_name){
		if (parent.$('inIframe')) {
			_hide();
			parent.$('inIframe').contentWindow.showSalesManInfo(pose_id,pose_name);
		}else {
			parent._hide();
			parent.showSalesManInfo(pose_id,pose_name);
		}
	}
	function addInsure(){
		makeFormCall('<%=contextPath%>/crm/customer/CustomerManage/insureAdd.json', addResult, "fm") ;
	}
	//数据回写
	function addResult(json){
		_hide();
		parent.$('inIframe').contentWindow.insureTableAdd(json);
	}
	function loadEnCompany(obj){
		$("enCompany").value=obj.getAttribute("TREE_ID");
	}
</script> 
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar1", "topbar")</script>   
</body>
</html>