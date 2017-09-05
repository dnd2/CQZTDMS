<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();   
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=7" />
	<%--
	<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
	
	<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/mtcommon.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/common.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/dealer_tree.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/dept_tree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/vposeLogistics.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/framecommon/default.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/validate/validate.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/jslib/dialog_new.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
	<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/framecommon/DialogManager.js"></script>
	
	<script type="text/javascript" src="<%=contextPath%>/js/jslib/html-to-json.js" /></script>
	<script type="text/javascript" src="<%=contextPath%>/js/jslib/json2.js" /></script>
	<script type="text/javascript" src="<%=contextPath%>/js/jslib/my-grid-pager.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/framecommon/HashMap.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/sales/storage/commonUtil.js" ></script>
	 --%> 
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
	<script type="text/javascript" src="<%=contextPath%>/js/web/dealer_tree.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
<title> 物流商管理 </title>
<script type="text/javascript">
    var filecontextPath="<%=contextPath%>";
	var poseSearch = "<%=contextPath%>/sales/storage/storagebase/LogisticsManage/logisticsInit.do";
	var getFunsByRoleIdsUrl = "<%=contextPath%>/sysmng/sysposition/SysPosition/getFunsByRoleIds.json";
	var fun_tree_url = "<%=contextPath%>/sysmng/sysposition/SysPosition/initFunTree.json";
	var addPoseUrl = "<%=contextPath%>/sales/storage/storagebase/LogisticsManage/addLogistics.json";
	var dept_tree_url = "<%=contextPath%>/sysmng/usemng/SgmSysUser/initOrgTree.json";
	var drlurl = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/allDrlQuery.json?COMMAND=1";
	var org_tree_url = "<%=contextPath%>/sysmng/sysposition/SysPosition/initOrgTree.json";
	var tree_url = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/initOrgTree.json";
		 
		 function addPose(obj) {
			 makeNomalFormCall(addPoseUrl,function(json){
				 if(json.st == "poseCode_error") {
					MyAlert("职位代码重复，请重新输入!") ;
					return ;
				} else if(json.st == "poseName_error") {
					MyAlert("职位名称重复，请重新输入!") ;
					return ;
				}else if(json.st == "oemOrg_error") {
					MyAlert("车厂职位不存在，无法添加!") ;
					return ;
				}else if(json.st == "succeed") {
					//MyAlert('保存成功!');
					window.location = poseSearch;
				} else {
					MyAlert("保存失败!") ;
					return ;
				}
			 },'fm');
		 }
		function txtClr(value){
			 document.getElementById(value).value = "";
	 	}
</script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>基础管理>物流商管理>物流商信息添加</div>

    <form method="post" name ="fm" id="fm">
    <input type="hidden" name="FUNSH" id="FUNSH"/>
	<input type="hidden" name="MYFUNS" id="MYFUNS"/>
	<input id="DEALER_ID" name="DEALER_ID" type="hidden"/>
	<input id="ROLE_IDS" name="ROLE_IDS" type="hidden" value=""/>
	<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
	
	<input type="hidden" name="DEPT_ID" id="DEPT_ID" value="" />
	<input type="hidden" id="orderCol2" name="orderCol2" value="" />
	<input type="hidden" id="order2" name="order2" value="" />
    <table  class="table_query" style="border:1px solid #DAE0EE">
    <tr> <th colspan="4"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息</th>
        </tr>
	    <tr>
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
				<input type="text" maxlength="20"  class="middle_txt" name="LOGI_CODE" id="LOGI_CODE" maxlength="20" datatype="0,is_null,20" />
			</td>
			<td width="10%" height="25" class="right">物流商简称：</td>
			<td align="left">
				<input type="text" maxlength="20"  class="middle_txt" name="LOGI_NAME" id="LOGI_NAME" maxlength="20" datatype="0,is_null,20" />
			</td>
		</tr>
		<tr>
			<td width="10%" height="25" class="right">物流商全称：</td>
			<td>
				<input type="text" maxlength="20"  class="middle_txt" name="LOGI_FULL_NAME" id="LOGI_FULL_NAME" maxlength="20" datatype="0,is_null,20"  />
			</td>
			<td width="10%" height="25" class="right">法人：</td>
			<td>
				<input type="text" maxlength="20"  class="middle_txt" name="CORPORATION" id="CORPORATION" maxlength="20" />
			</td>
		</tr>
		<tr>
			<td width="10%" height="25" class="right">联系人：</td>
			<td>
				<input type="text" maxlength="20"  class="middle_txt" name="CON_PER" id="CON_PER"  maxlength="20" />
			</td>
			<td width="10%" height="25" class="right">联系人电话：</td>
			<td>
				<input type="text" maxlength="20"  class="middle_txt" name="CON_TEL" id="CON_TEL" maxlength="20" onkeyup="phonecheck(this)" />
			</td>
		</tr>
		<tr>
			 <td class="right">状态：</td> 
			  <td align="left">
				 <label>
						<script type="text/javascript">
								genSelBoxExp("STATUS",<%=Constant.STATUS%>,"",false,"u-select","","false",'');
							</script>
					</label>
			  </td> 
			  <td width="10%" height="25" class="right">地址：</td>
			<td>
				<input type="text" maxlength="20"  class="middle_txt" name="ADDRESS" id="ADDRESS"  maxlength="30" />
			</td>
		</tr>
		<tr>
			 <td class="right">职位类别：</td> 
			 <td align="left">
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
				<input class="middle_txt" datatype="0,is_null,30" maxlength="30" type="text" maxlength="20"  id="POSE_NAME" name="POSE_NAME"/>
				<input type="hidden" id="chooseDlr" name="chooseDlr" value="<%=Constant.IF_TYPE_NO %>" checked="checked"/>
			 </td> 
			 <td width="10%" height="25" class="right">职位代码：</td>
			 <td>
				${POSECODE}
          		<input class="middle_txt" type="hidden"  value='${POSECODE}' datatype="0,is_digit_letter,30" maxlength="30" type="text" maxlength="20"  id="POSE_CODE" name="POSE_CODE"/>
			 </td>
		</tr>
		<tr>	
			<td width="10%" class="right">备注：</td>
			<td colspan="3">
				<textarea  cols="50" rows="3" name="REMARK" datatype="1,is_textarea,500"  id="REMARK" ></textarea>
			</td>
		</tr>
	</table>
	
	<br />
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
							<label class="u-checkbox">
								<input type="checkbox"  name="PROVICE"  value="${provice.ORG_ID}"/>
								<span></span>
							</label> <label class="u-label"><span>${provice.ORG_NAME}</span></label>
						</div>
						</c:forEach>
					</td>
				</tr>
			</table>
			<br />
</div>
</form>
<table width="100%">
	<tr>
		<td align="center">
			<!-- <input class="normal_btn" type="button" value="新增角色" id="mysub" onclick="addRole('<%=contextPath%>')"/> -->
			<input name="queryBtn" class="normal_btn" type="button" value="确&nbsp;定" id="queryBtn" onclick="check_addPose1(this);"/>
			<input class="u-button" type="button" value="返&nbsp;回" id="myfhh" onclick="window.history.go(-1)"/>
			<input type="hidden" id="old_roleIds" name="old_roleIds" />
		</td>
	</tr>
</table>	
<script type="text/javascript" >
	function back(){
		fm.action="<%=contextPath%>/sales/storage/storagebase/LogisticsManage/logisticsInit.do";
		fm.submit();
	}
	function check_addPose1(value){
		//MyAlert(document.getElementById("POSE_BUS_TYPE").value+"   "+document.getElementById("POSE_TYPE").value);
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
			if(!bo){
				MyAlert("请选择负责的区域!");
				return;
			}
			document.getElementById("hidProvice").value=hidProvice;//给文本框赋值
							
			addPose(value);
			disabledButton(["queryBtn","goBack"],true);
		}
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
</script>
</body>
</html>
