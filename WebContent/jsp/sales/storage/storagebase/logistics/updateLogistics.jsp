<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();   
	Map<String, Object> map =(Map<String, Object>)request.getAttribute("complaintMap"); 
	TcPosePO posePO = (TcPosePO)request.getAttribute("vpose");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.po.TcPosePO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=7" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
	<script type="text/javascript" src="<%=contextPath%>/js/web/dealer_tree.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
<title> 物流商管理 </title>
</head>
<script type="text/javascript">
    var filecontextPath="<%=contextPath%>";
    var g_webAppName = '<%=(request.getContextPath())%>';   
	validateConfig.isOnBlur = false;
	var tree_url = "<%=contextPath%>/sysmng/sysposition/SysPosition/initConmpanyDeptTree.json";
	var poseSearch = "<%=contextPath%>/sales/storage/storagebase/LogisticsManage/logisticsInit.do";
	var getDataAuthUrl = "<%=contextPath%>/sysmng/sysposition/SysPosition/getDataAuth.json";
	var getFunsByRoleIdsUrl = "<%=contextPath%>/sysmng/sysposition/SysPosition/getFunsByRoleIds.json";
	var fun_tree_url = "<%=contextPath%>/sysmng/sysposition/SysPosition/initFunTree.json";
	var modfiPoseUrl = "<%=contextPath%>/sales/storage/storagebase/LogisticsManage/editLogistics.json";
	var getsysPoseValUrl = "<%=contextPath%>/sysmng/sysposition/SysPosition/getSysPoseVal.json";
	var dept_tree_url = "<%=contextPath%>/sysmng/usemng/SgmSysUser/initOrgTree.json";
	var drlurl = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/allDrlQuery.json?COMMAND=1";
	var org_tree_url = "<%=contextPath%>/sysmng/sysposition/SysPosition/initOrgTree.json";
	var getGjzwIdsUrl = "<%=contextPath%>/sysmng/sysposition/SysPosition/getGjzwByRoldIds.json";
	var roleFuncList;
    	var roleList;
	var poseFuncList;
	var poseFuncDataList;
	 function txtClr(value){
		 document.getElementById(value).value = "";
	 }
		 
</script>
<body><!--  onunload='javascript:destoryPrototype();' -->
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>储运基础数据> 物流商管理>物流商信息修改
	</div>

    <form method="post" name ="fm" id="fm">
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
   <table class="table_query" style="border:1px solid #DAE0EE">
    <input type="hidden" name="LOGI_ID" id="LOGI_ID" value="<c:out value="${complaintMap.LOGI_ID}"/>" />
    <tr> <th colspan="4"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息</th>
        </tr>
	    <tr id="trgjtw">
		  <td class="right">产地：</td> 
		  <td align="left" colspan="3">
			 <select name="YIELDLY" id="YIELDLY" class="u-select">
			 <option value="">-请选择-</option>
					<c:if test="${list!=null}">
						<c:forEach items="${list}" var="list">
							<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
						</c:forEach>
					</c:if>
		  		</select><span style="color: red">&nbsp;*</span>
			</td>  
	  </tr> 
		<tr>
			<td width="10%" height="25" class="right">物流商代码：</td>
			<td align="left">
				<input type="text" maxlength="20"  class="middle_txt" name="LOGI_CODE" id="LOGI_CODE" maxlength="20" datatype="0,is_null,20" value="<c:out value="${complaintMap.LOGI_CODE}"/>"/>
			</td>
			<td width="10%" height="25" class="right">物流商简称：</td>
			<td align="left">
				<input type="text" maxlength="20"  class="middle_txt" name="LOGI_NAME" id="LOGI_NAME" maxlength="20" datatype="0,is_null,20" value="<c:out value="${complaintMap.LOGI_NAME}"/>"/>
			</td>
		</tr>
		<tr>
			<td width="10%" height="25" class="right">物流商全称：</td>
			<td>
				<input type="text" maxlength="20"  class="middle_txt" name="LOGI_FULL_NAME" id="LOGI_FULL_NAME" maxlength="20" datatype="0,is_null,20"  value="<c:out value="${complaintMap.LOGI_FULL_NAME}"/>"/>
			</td>
			<td width="10%" height="25" class="right">法人：</td>
			<td>
				<input type="text" maxlength="20"  class="middle_txt" name="CORPORATION" id="CORPORATION" maxlength="20" value="<c:out value="${complaintMap.CORPORATION}"/>"/>
			</td>
		</tr>
		<tr>
			<td width="10%" height="25" class="right">联系人：</td>
			<td>
				<input type="text" maxlength="20"  class="middle_txt" name="CON_PER" id="CON_PER"  maxlength="20" value="<c:out value="${complaintMap.CON_PER}"/>"/>
			</td>
			<td width="10%" height="25" class="right">联系人电话：</td>
			<td>
				<input type="text" maxlength="20"  class="middle_txt" name="CON_TEL" id="CON_TEL" maxlength="20" onkeyup="phonecheck(this)" value="<c:out value="${complaintMap.CON_TEL}"/>"/>
			</td>
		</tr>
		<tr>
			 <td class="right">状态：</td> 
			  <td align="left">
				 <label>
						<script type="text/javascript">
								genSelBoxExp("STATUS",<%=Constant.STATUS%>,"<c:out value="${complaintMap.STATUS}"/>",false,"u-select","","false",'');
							</script>
					</label>
			  </td> 
			  <td width="10%" height="25" class="right">地址：</td>
			<td>
				<input type="text" maxlength="20"  class="middle_txt" name="ADDRESS" id="ADDRESS"  maxlength="30" value="<c:out value="${complaintMap.ADDRESS}"/>"/>
			</td>
		</tr>
		<tr>
			 <td class="right">职位类别：</td> 
			 <td align="left" id="zwlb">
				<select name="POSE_TYPE" id="POSE_TYPE" class="u-select">
			 		<option value="${poseType}">主机厂端</option>
				</select>
			 </td> 
			 <td width="10%" height="25" class="right">职位类型：</td>
			 <td>
				<select name="POSE_BUS_TYPE" id="POSE_BUS_TYPE" class="u-select">
			 		<option value="${poseBusType}">储运物流</option>
				</select>
			 </td>
		</tr>
		<tr>
			 <td class="right">职位名称：</td> 
			 <td align="left">
			 <input class="middle_txt"  value="<%=posePO.getPoseName() %>" type="text" maxlength="20"  id="POSE_NAME" name="POSE_NAME"/>
				<input type="hidden" id="chooseDlr" name="chooseDlr" value="<%=Constant.IF_TYPE_NO %>" onclick="showDlrDiv()" checked="checked"/>
			 </td> 
			 <td width="10%" height="25" class="right">职位代码：</td>
			 <td>
			<%=posePO.getPoseCode() %>
			 </td>
		</tr>
		<tr>	
			<td width="10%" class="right">备注：</td>
			<td colspan="3">
				<textarea  cols="50" rows="3" name="REMARK"  id="REMARK" maxlength="100"  ><c:out value="${complaintMap.REMARK}"/> </textarea>
			</td>
		</tr>
		<tr/>
	</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
</div>
<div id="myCityDiv">
			<table class="table_query">
				<tr>
					<th style="border-bottom: 2px solid #ccc;">
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;管理区域
						<input type="hidden" id="hidProvice" name="hidProvice"  value=""/>
					 </th>
				</tr>
				<tr>
					<td>
						<div style="width:150px; height: 25px; float: left;">
							<label class="u-checkbox">
								<input type="checkbox" id="PROVICES"  name="PROVICES"  value="" onclick="checkAll()"/>
								<span></span>
							</label> <label class="u-label"><span>全国</span></label>
						</div>
						
						<c:forEach items="${proviceList}" var="provice" >
							<div style="width:150px; height: 25px; float: left;">
								<label class="u-checkbox"><input type="checkbox"  name="PROVICE"  value="${provice.ORG_ID}"
								<c:forEach items="${checkProviceList}" var="checkProvice" >
									<c:if test="${provice.ORG_ID==checkProvice.regionId}">
										checked="checked"
									</c:if>
								</c:forEach>
								/><span></span>
							</label><label class="u-label"><span>${provice.ORG_NAME}</span></label>
							</div>
						</c:forEach>
					</td>
				</tr>
			</table>
			<br/>
</div>
</form>
<table width="100%">
	<tr>
		<td align="center">
			<input name="queryBtn" class="normal_btn" type="button" value="确&nbsp;定" id="myfh" onclick="check_addPose1(this)"/>
			<input class="u-button" type="button" value="返&nbsp;回" id="myfhh" onclick="window.history.go(-1)"/>
			<input type="hidden" id="old_roleIds" name="old_roleIds" />
		</td>
	</tr>
</table>	
<script type="text/javascript" >
function doInit(){
	
	var obj = document.getElementById("YIELDLY");
	obj.value = <%=map.get("AREA_ID")%>;
}
function back(){
	fm.action="<%=contextPath%>/sales/storage/storagebase/LogisticsManage/logisticsInit.do";
	fm.submit();
}
//全选
function checkAll(){
		var pro =  document.getElementById('PROVICES');
		 var arr =  document.getElementsByName('PROVICE');
		if(pro.checked){
			for (i=0;i<arr.length;i++) {      
				arr[i].checked=true;    
			} 
		}else{
			for (i=0;i<arr.length;i++) {      
				arr[i].checked=false;    
			}
		}
	}
function check_addPose1(value){
	
	if(submitForm('fm')) {
		var yieldly=document.getElementById("YIELDLY");//产地
		var logiCode=document.getElementById("LOGI_CODE");//物流商代码
		var logiName=document.getElementById("LOGI_NAME");//物流商简称
		var logiFullName=document.getElementById("LOGI_FULL_NAME");//物流商全称
		var corporation=document.getElementById("CORPORATION");//法人
		var conPer=document.getElementById("CON_PER");//联系人
		var conTel=document.getElementById("CON_TEL");//联系人电话
		var status=document.getElementById("STATUS");//状态
		var address=document.getElementById("ADDRESS");//地址
		if(yieldly.value==null || yieldly.value==""){
			MyAlert("产地不能为空！");
			return;
		}
		if(logiCode.value==null || logiCode.value==""){
			MyAlert("物流商代码不能为空！");
			return;
		}
		if(logiName.value==null || logiName.value==""){
			MyAlert("物流商简称不能为空！");
			return;
		}
		if(logiFullName.value==null || logiFullName.value==""){
			MyAlert("物流商全称不能为空！");
			return;
		}
		var arr =document.getElementsByName("PROVICE");
		var bo=false;
		var hidProvice="";
		for (i=0;i<arr.length;i++) {   
			if(arr[i].checked){
				bo=true;
				hidProvice+=arr[i].value+",";
			}
		} 
		//if(!bo){
		//	MyAlert("请选择负责的区域!");
		//	return;
		//}
		document.getElementById("hidProvice").value=hidProvice;//给文本框赋值
						
		modfiPose(value);
		disabledButton(["queryBtn","goBack"],true);
	}
}
function modfiPose() {
	makeNomalFormCall(modfiPoseUrl,function(json){
		if(json.st == "succeed") {
			fm.action = "<%=contextPath%>/sales/storage/storagebase/LogisticsManage/logisticsInit.do" ;
			fm.method = "POST";
			fm.submit() ;
		} else {
			MyAlert(json.st);
			return ;
		}
	},'fm');
}
</script>
</body>
</html>
