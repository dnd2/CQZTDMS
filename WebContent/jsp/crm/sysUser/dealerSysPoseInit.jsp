<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=7"/>
	<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
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
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/crm/sysUser/dealerPose.js"></script>
	
<title>系统职位维护</title>
<style>
.img {
	border: none
}
</style>
<script type="text/javascript">


   var filecontextPath="<%=contextPath%>";


</script>
</head>

<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 团队管理 &gt;经销商职位维护</div>
<form id="fm" name="fm"><input type="hidden" name="curPage" id="curPage"
	value="1" />
	<input type="hidden" id="orderCol" name="orderCol" value="" />
	<input type="hidden" id="tree_root_id" name="tree_root_id" value="" />
	<input id="DEPT_ID" name="DEPT_ID" type="hidden"/>
	<c:if test="${dutyType==10431005}"><input id="DEALER_ID" name="DEALER_ID" type="hidden" value="${DEALER_ID}"/></c:if>
	<c:if test="${dutyType!=10431005}"><input id="DEALER_ID" name="DEALER_ID" type="hidden" /></c:if>
	
	<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
	<input id="curPaths" name="curPaths" value="<%=request.getContextPath() %>" type="hidden"/>
	
<input type="hidden" id="order" name="order" value="" />
<table class="table_query" border="0">
	<tr>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap" style="display:none;">职位类型：</td>
		<td class="table_query_2Col_input" nowrap="nowrap" style="display:none">
		<script type="text/javascript"> genSelBox("POSE_TYPE",<%=Constant.SYS_USER%>,"10021002",false,"","onchange='selType(this,<%=Constant.SYS_USER_SGM%>,<%=Constant.SYS_USER_DEALER%>)'");</script>
		</td>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap" >职位级别：</td>
		<td class="table_query_2Col_input" nowrap="nowrap" >
		<script type="text/javascript"> genSelBox("POSE_RANK",<%=Constant.DEALER_USER_LEVEL%>,"",true,"","");</script>
		</td>
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap">状态：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
          <script type="text/javascript"> genSelBox("POSE_STATUS",<%=Constant.STATUS%>,"",true,"","");</script>
        </td>
	</tr>
	<tr id="zz" style="display: inline;">
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">上级职位：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		<input id="par_pose_name" name="par_pose_name" type="text" value="" class="middle_txt"  size="30"  readonly="readonly"/> 
			<input id="par_pose_id" name="par_pose_id" value="" type="hidden" class="middle_txt" /> 
			<input id="type" name="type" value="" type="hidden"/> 
			<input type="button" value="..." class="mini_btn" onclick="toPoseList();" />
			<input type="button" value="清空" class="normal_btn" onclick="clrTxt('par_pose_name');" />
		</td>
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap">职位名称：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<input name="POSE_NAME" maxlength="30" datatype="1,is_noquotation,30" id="POSE_NAME" type="text" class="middle_txt" value="${POSE_NAME}" />
		</td>
	</tr>
	<tr id="jxs" style="display: none;">
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">经销商公司：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
<!--		<input class="middle_txt" id="DEALER_NAME" datatype="0,is_null,300" onclick="showPan()" style="cursor: pointer;" name="DEALER_NAME" type="text"/>-->
		
			<input class="middle_txt" id="COMPANY_NAME" style="cursor: pointer;" name="COMPANY_NAME" type="text" disabled="disabled" />
			<input class="mark_btn" type="button" value="&hellip;" onclick="showCompany('<%=contextPath %>')"/>
			<input type="button" value="清空" class="normal_btn" onclick="clearCompanyId()"/>
		</td>
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap"></td>
		<td class="table_query_2Col_input" nowrap="nowrap"></td>
	</tr>
	<tr>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">职位代码：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		<input name="POSE_CODE" maxlength="30" datatype="1,is_noquotation,30" id="POSE_CODE" type="text" class="middle_txt" /></td>
		
	</tr>
	<tr style="display:none;">
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">角色代码：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		<input class="middle_txt" id="ROLE_NAME" name="ROLE_NAME" type="text"/>
		</td>
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap"></td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		</td>
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap"></td>
		<td class="table_query_2Col_input" nowrap="nowrap">
	</tr>
	<tr>
		<td colspan="6" class="table_query_4Col_input"
			style="text-align: center"><input name="queryBtn" type="button"
			class="normal_btn" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
			<input type="button"
			class="normal_btn" onclick="requery()" value="重 置"/> &nbsp; 
			<input
			name="button2" type="button" class="normal_btn"
			onclick="window.location.href='<%=contextPath%>/crm/sysUser/DealerSysPose/addSysPositionInit.do'" value="新 增" /></td>
	</tr>
</table>
</form>
<div id="_page" style="margin-top:15px;display:none;"></div>
<div id="myGrid" ></div>
<div id="myPage" class="pages"></div>

<form id="fm2" name="fm2">
<input type="hidden" name="curPage2" id="curPage2" value="1" />
<input type="hidden" name="DEPT_ID" id="DEPT_ID" value="" />
<input type="hidden" id="orderCol2" name="orderCol2" value="" />
<input type="hidden" id="order2" name="order2" value="" />
<div id='pan' style="z-index: 3000;position:absolute;border:1px solid #5E7692;background: #FFFFFF; width: 715px;height: 379px;">
	<div id='myquery' style="z-index: 3001;position:absolute;border:1px solid #5E7692;width: 715px;height: 30px;">
		<table class="table_info" border="0" style="height: 30px;" width="100%">
		<tr>
				<td class="table_query_3Col_label_5Letter" nowrap="nowrap">经销商代码：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<input class="middle_txt" id="DRLCODE" datatype="1,is_noquotation,30" name="DRLCODE" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
				</td>
				<td class="table_query_3Col_label_5Letter" nowrap="nowrap">经销商简称：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<input class="middle_txt" id="DELSNAME" datatype="1,is_noquotation,30" name="DELSNAME" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
				</td>
				<td class="table_query_3Col_input" nowrap="nowrap"><input class="normal_btn" type="button" value="查 询" id="queryBtn2" onclick="getDrl(1)"/>
				<input class="normal_btn" type="button" value="重 置" onclick="requery2()"/></td>
			</tr>
		</table>
	</div>
	<div id='dtree' class="dtree" style="z-index: 3000;position: absolute;overflow:auto;border:1px solid #5E7692;width: 213px;height: 349px;">
        <script type="text/javascript">
        a = new dTree('a','dtree','false','false','true');
        </script>
    </div>
    <div id="drlv" style="z-index: 3000;position:absolute;border:1px solid #5E7692;width: 501px;height: 349px;  overflow-y: auto; overflow-x:hidden;">
    	<br />
    	<table width="100%">
    		<tr>
    			<td>
    				<div id="_page2" style="display:none;"></div>
					<div id="myGrid2" ></div>
					<div id="myPage2" class="pages"></div>
    			</td>
    		</tr>
    	</table>
    </div>
</div>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
</body>
</html>
<script>
validateConfig.isOnBlur = false;

var drlurl = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/allDrlQuery.json?COMMAND=1";
var tree_url = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/initOrgTree.json";
var dept_tree_url = "<%=contextPath%>/sysmng/orgmng/SgmOrgMng/initOrgTree.json";

function selType(obj,sgmCode,dealerCode) {
	if(obj.value == sgmCode) {
		//$('COMPANY_ID').value = "";
		//$('COMPANY_NAME').value = "";
		//$('DEPT_ID').value = "";
		//$('DEPT_NAME').value = "";
		//$('jxs').setStyle("display","none");
		//$('zz').setStyle("display","none");
	}else if(obj.value == dealerCode) {
		//$('COMPANY_ID').value = "";
		//$('COMPANY_NAME').value = "";
		//$('DEPT_ID').value = "";
		//$('DEPT_NAME').value = "";
		//$('jxs').setStyle("display","none");
		//$('zz').setStyle("display","none");
	}
	$('jxs').setStyle("display","none");
	$('zz').setStyle("display","none");
	//onChange2();
}
    var  GOLB_SPLIT_HIDE_HREF = false;
var myPage;
function __extQuery__(page){
	
	entThisPage = page;
	//if($('POSE_TYPE').value == <%=Constant.SYS_USER_DEALER%>) {
	//	$('jxs').setStyle("display","");
	//}

	//if($('POSE_TYPE').value == <%=Constant.SYS_USER_DEALER%>) {
		//var compnayName = document.getElementById("COMPANY_NAME");
		//if(compnayName.value == ''){
			//MyAlert("经销商公司不能为空");
			//return false;
			//return true;
		//}			
	//}
	
	$("queryBtn").disabled = "disabled";
	showMask();
	
	submitForm('fm') ? sendAjax(url+(url.lastIndexOf("?") == -1?"?":"&")+"curPage="+page,callBack,'fm') : ($("queryBtn").disabled = "",removeMask());
}
	var url = "<%=contextPath%>/crm/sysUser/DealerSysPose/sysPositionQuery.json?COMMAND=1";
	
	var title = null;
	
	var columns = [
					{header: "序号", align:'center', renderer:getIndex,width:'7%'},
					{header: "职位代码", dataIndex: 'POSE_CODE', orderCol:"POSE_CODE", align:'center'},
					{header: "职位名称", dataIndex: 'POSE_NAME', align:'center'},			
					{header: "职位类别", dataIndex: 'POSE_TYPE', align:'center',renderer:getItemValue},
					{header: "状态", dataIndex: 'POSE_STATUS', orderCol:"POSE_STATUS", align:'center',renderer:getItemValue},
					{header: "最后操作时间", dataIndex: 'UPDATE_DATE', align:'center'},
					{header: "最后操作人", dataIndex: 'UPDATE_NAME', align:'center'},					
					{id:'action',header: "操作", width:70,sortable: false,dataIndex: 'POSE_ID',renderer:myLink}
			      ];
    
   function myLink(value,metadata){
       return String.format(
              "<a href=\"<%=contextPath%>/crm/sysUser/DealerSysPose/viewSysRoleInit.do?poseId="
					+ value + "\">[查看]</a>");
	}

  
	function requery() {
		$('POSE_CODE').value="";
		$('POSE_NAME').value="";
		$('COMPANY_NAME').value="";
		$('COMPANY_ID').value="";
	}

	function requery2() {
		$('DRLCODE').value="";
		$('DELSNAME').value="";
		$('DEPT_ID').value="";
	 }
	 //经销商公司数据清空
	 function clearCompanyId(){
	 	$('COMPANY_NAME').value="";
	 	$('COMPANY_ID').value="";
	 }
	
</script>