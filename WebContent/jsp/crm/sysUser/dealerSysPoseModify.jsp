<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	TcPosePO posePO = (TcPosePO)request.getAttribute("vpose");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.po.TcPosePO"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=7">
	<link href="<%=contextPath %>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/dtree1.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/mootools.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dept_tree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/framecommon/default.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/crm/sysUser/vpose.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dtree.js"></script>
	<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/crm/sysUser/dealerPose.js"></script>
<title>系统职位维护</title>
</head>

<script type="text/javascript">
	validateConfig.isOnBlur = false;
	var tree_url = "<%=contextPath%>/sysmng/sysposition/SysPosition/initConmpanyDeptTree.json";
	var poseSearch = "<%=contextPath%>/sysmng/sysposition/SysPosition/querySysPositionInit.do";
	var getDataAuthUrl = "<%=contextPath%>/sysmng/sysposition/SysPosition/getDataAuth.json";
	var getFunsByRoleIdsUrl = "<%=contextPath%>/sysmng/sysposition/SysPosition/getFunsByRoleIds.json";
	var fun_tree_url = "<%=contextPath%>/sysmng/sysposition/SysPosition/initFunTree.json";
	var modfiPoseUrl = "<%=contextPath%>/crm/sysUser/DealerSysPose/sysRolemodfi.json";
	var getsysPoseValUrl = "<%=contextPath%>/sysmng/sysposition/SysPosition/getSysPoseVal.json";
	var dept_tree_url = "<%=contextPath%>/sysmng/usemng/SgmSysUser/initOrgTree.json";
	var getGjzwIdsUrl = "<%=contextPath%>/sysmng/sysposition/SysPosition/getGjzwByRoldIds.json";
    
    var filecontextPath="<%=contextPath%>";

    var roleFuncList;
    var roleList;
	var poseFuncList;
	var poseFuncDataList;

	var tgjzw = "<%=request.getAttribute("gjzw") %>";
	
	var subcat = new Array();
	subcat[0] = new Array("<%=Constant.POSE_BUS_TYPE_SYS%>","系统管理(ALL)","<%=Constant.SYS_USER_SGM%>"); 	
	subcat[1] = new Array("<%=Constant.POSE_BUS_TYPE_VS%>","车厂销售管理","<%=Constant.SYS_USER_SGM%>"); 	
	subcat[2] = new Array("<%=Constant.POSE_BUS_TYPE_WR%>","车厂售后管理","<%=Constant.SYS_USER_SGM%>"); 	
	subcat[3] = new Array("<%=Constant.POSE_BUS_TYPE_DVS%>","经销商销售","<%=Constant.SYS_USER_DEALER%>"); 	
	subcat[4] = new Array("<%=Constant.POSE_BUS_TYPE_DWR%>","经销商售后","<%=Constant.SYS_USER_DEALER%>"); 	
	subcat[5] = new Array("<%=Constant.POSE_BUS_TYPE_JSZX%>","结算中心","<%=Constant.SYS_USER_DEALER%>"); 	
	
	function doInit(){
		changePoseSelectItem(document.getElementById("POSE_TYPE").value);
	}
	
	function changePoseSelectItem(arg){
		var obj = document.getElementById("POSE_BUS_TYPE");
		obj.length = 0; 
	    for (var i=0;i < subcat.length; i++) { 
	        if (subcat[i][2] == arg) { 
	           obj.options[obj.length] = new Option(subcat[i][1], subcat[i][0]); 
	           if(subcat[i][0] == '<%=posePO.getPoseBusType()%>'){
	           	  obj.options[obj.length-1].selected = "selected";
	           }
	        }        
	    }
	}
</script>

<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 团队管理 &gt;职位修改</div>
<form id="fm" name="fm">
<input id="curPaths" name="curPaths" value="<%=request.getContextPath() %>" type="hidden"/>
<input type="hidden" name="DEPT_ID" id="DEPT_ID" value="<%=posePO.getOrgId() %>"/>
<input type="hidden" name="POSE_CODE" id="POSE_CODE" value="<%=posePO.getPoseCode() %>"/>
<input type="hidden" name="POSE_ID" id="POSE_ID" value="<%=request.getAttribute("poseId") %>"/>
<input id="ROLE_IDS" name="ROLE_IDS" type="hidden" value=""/>
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden" value="${COMPANY_ID }" />
<input id="DEALER_ID" name="DEALER_ID" type="hidden" value="${dealerId }"/>
<input type="hidden" value="" id="GG" name="GG" />
<input id="POSE_TYPE" name="POSE_TYPE" type="hidden" value="<%=posePO.getPoseType() %>"/>
<input type="hidden" name="FUNSH" id="FUNSH"/>
<input type="hidden" name="MYFUNS" id="MYFUNS"/>
<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
<table class="table_query" border="0" style="border:1px solid #DAE0EE">
	<tr style="display:none;">
	  <td class="table_query_2Col_label_4Letter" nowrap="nowrap">职位类别：</td>
	  <td class="table_query_4Col_input" nowrap="nowrap" id="zwlb"></td>
	    <td class="table_query_2Col_label_4Letter" nowrap="nowrap">职位类型：</td>
	  <td class="table_query_4Col_input" nowrap="nowrap">
	  		<select name="POSE_BUS_TYPE" class="short_sel"></select>
	  </td>
	</tr>
	
		<tr>
		<%if(posePO.getPoseType().equals(Constant.SYS_USER_DEALER)) { %>
		
		  <td class="table_query_2Col_label_5Letter" nowrap="nowrap" id="corg" align="left">经销商公司：</td>
		  <td class="table_query_4Col_input" nowrap="nowrap" align="left">${COMPANY_NAME }</td>
			
	<%} %>
		<%if(posePO.getPoseType().equals(Constant.SYS_USER_SGM)) { %>
	
	  <td class="table_query_2Col_label_5Letter" nowrap="nowrap" id="cdept">所属部门：</td>
	  <td class="table_query_4Col_input" nowrap="nowrap">
        <input class="middle_txt" id="DEPT_NAME" value="${orgName }" onblur="isCloseTreeDiv(event,this,'deptt')" readonly="readonly" datatype="0,is_null,100" onclick="showDEPT()" style="cursor: pointer;" name="DEPT_NAME" type="text"/>
      </td>
      <%} %>
		<td class="table_query_2Col_label_4Letter"  align="left">职位代码：</td>
		<td class="table_query_4Col_input" align="left"><%=posePO.getPoseCode() %>
        </td>
	</tr>
	<tr>
	  <td class="table_query_2Col_label_5Letter" align="left" >职位名称：</td>
	  <td class="table_query_4Col_input"  align="left">
        <input class="middle_txt"  value="<%=posePO.getPoseName() %>" type="text" id="POSE_NAME" name="POSE_NAME"/>
      </td>
		<td class="table_query_2Col_label_4Letter" >状态：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
          <script type="text/javascript"> genSelBox("POSE_STATUS",<%=Constant.STATUS%>,<%=posePO.getPoseStatus() %>,false,"","");</script>
        </td>
	</tr>
	<tr>
	  <td class="table_query_2Col_label_5Letter" align="left">职位级别：</td>
	  <td class="table_query_4Col_input" align="left">
	  <script type="text/javascript"> genSelBox("POSE_RANK",<%=Constant.DEALER_USER_LEVEL%>,"<%=posePO.getPoseRank() %>",false,"","");</script>
      </td>
		<td class="table_query_2Col_label_4Letter" align="left">上级职位：</td>
		<td class="table_query_4Col_input" align="left">
			<input id="par_pose_name" name="par_pose_name" type="text" value="${par_pose_name}" class="middle_txt" datatype="0,is_textarea,30" size="30"  readonly="readonly"/> 
			<input id="par_pose_id" name="par_pose_id" value="<%=posePO.getParPoseId() %>" type="hidden" class="middle_txt" /> 
			<input id="type" name="type" value="" type="hidden"/> 
			<input type="button" value="..." class="mini_btn" onclick="toPoseList();" />
			<input type="button" value="清空" class="normal_btn" onclick="clrTxt('par_pose_name');" />
        </td>
	</tr>
	<tr id="trgjtw" style="display:none;">
	  <td class="table_query_2Col_label_4Letter"  style="display:none;">业务范围：</td>
	  <td class="table_query_2Col_input" style="display:none;">
      			<c:forEach items="${brandList}" var="brand">
				<c:forEach items="${relList}" var="rel">
				<c:if test="${rel.areaId == brand.areaId}">
				<c:set var="tmpBrand" value="${rel.areaId}"></c:set>
				</c:if>
				</c:forEach>
					<input type="checkbox" name="BRAND"   <c:if test="${brand.areaId == tmpBrand }">checked="checked" </c:if> value="${brand.areaId}"/>${brand.areaName}&nbsp;&nbsp;
				</c:forEach>
      </td>
		<td class="table_query_2Col_label_4Letter" style="display:none;"></td>
		<td class="table_query_2Col_input" style="display:none;" ></td>
	</tr>
</table>
<br/>
<table class="table_query" id="role_list">
<tr class="table_query_th">
	<th colspan="5"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;<a href="#" onclick="show2()">角色列表</a></th>
</tr>
    <tr class="table_query_row2">
      	<td width="8%"  nowrap="nowrap" >序号</td>
      	<td width="35%"  nowrap="nowrap" >角色代码</td>
      	<td width="47%"  nowrap="nowrap" >角色名称</td>
		<td width="10%"  nowrap="nowrap" >操作</td>
    </tr>
</table>
<br/>
</form>
<table class="de_table_list" style="border-bottom:0px solid #DAE0EE" width="100%">
	<tr class="table_list_th">
		<td width="100%"><img class="nav" src="<%=contextPath %>/img/subNav.gif" />&nbsp;功能列表<font color="red">&nbsp;*</font></td>
	</tr>
	<tr id="row1" class="table_list_th" >
		<td width="100%" height="380" id="treetd" valign="top" style="padding:3px;line-height:1em;overflow:auto">
			<div class="dtree" id="funTree" style="border:1px solid #5E7692;width: 95%;height:350px; position: absolute;overflow:auto">
				<script type="text/javascript">
					var b;
				</script>
			</div>
		</td>
	</tr>
</table>
<br />
<table width="100%">
	<tr>
		<td align="center">
			<input class="normal_btn" type="button" value="新增角色" id="mysub" onclick="addRole('<%=contextPath%>')"/>
			<input class="normal_btn" type="button" value="保 存" id="myfh" onclick="modfiPose()"/>
			<input class="normal_btn" type="button" value="返 回" id="myfhh" onclick="ffhh();"/>
			<input type="hidden" id="old_roleIds" name="old_roleIds" />
	</tr>
</table>
    <div id="erdiv" style="position: absolute; top:-1000px; background: #FDFFCE; height: 17px; border:1px solid #FFBA43; display: none;">
<img style="margin-top: 1px; margin-left: 2px;" src="<%=contextPath%>/img/exclamation.gif" />
<span id="ermsg" style="color: red; position: absolute; margin-top: 1px;"></span></div>
<script type="text/javascript">
function modfiPoseBack(jsons) {
	if(jsons.st == "succeed") {
		$('fm').action= "<%=contextPath%>/crm/sysUser/DealerSysPose/doInit.do" ;
		$('fm').method = "POST";
		$('fm').submit();
	} else {
		MyAlert(jsons.st);
		
		return ;
	}
}
</script>
</body>
</html>
