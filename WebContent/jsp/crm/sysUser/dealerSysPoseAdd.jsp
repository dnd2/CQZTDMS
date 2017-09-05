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
	<meta http-equiv="X-UA-Compatible" content="IE=7" />
	<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/mootools.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/common.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dealer_tree.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dept_tree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/crm/sysUser/vpose.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/framecommon/default.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dtree.js"></script>
	<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/crm/sysUser/dealerPose.js"></script>
	
<title>角色维护</title>
<style>
.img {
	border: none
}
</style>
<script type="text/javascript">
    var filecontextPath="<%=contextPath%>";
    
	validateConfig.isOnBlur = false;
	var poseSearch = "<%=contextPath%>/crm/sysUser/DealerSysPose/doInit.do";
	var getDataAuthUrl = "<%=contextPath%>/sysmng/sysposition/SysPosition/getDataAuth.json";
	var getFunsByRoleIdsUrl = "<%=contextPath%>/sysmng/sysposition/SysPosition/getFunsByRoleIds.json";
	var getGjzwIdsUrl = "<%=contextPath%>/sysmng/sysposition/SysPosition/getGjzwByRoldIds.json";
	var fun_tree_url = "<%=contextPath%>/sysmng/sysposition/SysPosition/initFunTree.json";
	var addPoseUrl = "<%=contextPath%>/crm/sysUser/DealerSysPose/addPosition.json";
	var dept_tree_url = "<%=contextPath%>/sysmng/usemng/SgmSysUser/initOrgTree.json";
	var drlurl = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/allDrlQuery.json?COMMAND=1";
	var org_tree_url = "<%=contextPath%>/sysmng/sysposition/SysPosition/initOrgTree.json";
	var tree_url = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/initOrgTree.json";
	
	function selType(obj,sgmCode,dealerCode) {
		if(obj.value == sgmCode) {
			$('COMPANY_ID').value = "";
			$('COMPANY_NAME').value = "";
			$('jxs').setStyle("display","none");
		}else if(obj.value == dealerCode) {
			$('COMPANY_ID').value = "";
			$('COMPANY_NAME').value = "";
			$('jxs').setStyle("display","none");
		}
		
		if(obj.value == sgmCode) {
			$('DEPT_ID').value = "";
			$('DEPT_NAME').value = "";
			$('bm').setStyle("display","");
		}else if(obj.value == dealerCode) {
			$('DEPT_ID').value = "";
			$('DEPT_NAME').value = "";
			$('bm').setStyle("display","none");
		}
		
		changePoseSelectItem(document.getElementById("POSE_TYPE").value);
		//onChange2();
	}
	
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
	    	obj.options[0] = new Option('---请选择---','');
	        if (subcat[i][2] == arg) { 
	           obj.options[obj.length] = new Option(subcat[i][1], subcat[i][0]); 
	        }    
	        obj.options[0].selected=true;    
	    }
	    obj.options[1].selected=true;
	}
	function changeValue(){
		selType($("POSE_TYPE"),'<%=Constant.SYS_USER_SGM%>','<%=Constant.SYS_USER_DEALER%>');
	}
</script>
</head>
<body onload="changeValue();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 团队管理 &gt;职位维护新增</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="FUNSH" id="FUNSH"/>
<input type="hidden" name="MYFUNS" id="MYFUNS"/>
<input id="DEALER_ID" name="DEALER_ID" type="hidden"/>
<input id="ROLE_IDS" name="ROLE_IDS" type="hidden" value=""/>
<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
<input id="curPaths" name="curPaths" value="<%=request.getContextPath() %>" type="hidden"/>
<table class="table_query" border="0" style="border:1px solid #DAE0EE">
	<tr style="display:none;"> 
	  <td class="table_query_2Col_label_6Letter" nowrap="nowrap" >职位类别：</td>
	  <td class="table_query_2Col_input" nowrap="nowrap">
			<script type="text/javascript"> genSelBox("POSE_TYPE",<%=Constant.SYS_USER%>,"${POSE_TYPE}",false,"","onchange='selType(this,<%=Constant.SYS_USER_SGM%>,<%=Constant.SYS_USER_DEALER%>)'");</script>
	  </td>
        <td class="table_query_2Col_label_6Letter" nowrap="nowrap">职位类型：</td>
	  <td class="table_query_2Col_input" nowrap="nowrap">
	  		<!--<select name="POSE_BUS_TYPE" onchange="selType(this,<%=Constant.POSE_BUS_TYPE_VS%>,<%=Constant.POSE_BUS_TYPE_VS%>);"></select>
	  		--><select name="POSE_BUS_TYPE"></select><font color="red">*</font>
	  </td>
	  <td class="table_query_2Col_label_4Letter" nowrap="nowrap">&nbsp;</td>
	  <td class="table_query_2Col_input" nowrap="nowrap">
        &nbsp;</td>
	</tr>
	<tr id="jxs" style="display: none;">
	  <td class="table_query_2Col_label_6Letter" nowrap="nowrap" id="corg">经销商公司：</td>
	  <td class="table_query_2Col_input" nowrap="nowrap">
        <!--
           <input class="middle_txt" id="DEALER_NAME" value="" onclick="showPan()" readonly="readonly" datatype="0,is_null,100" style="cursor: pointer;" name="DEALER_NAME" type="text"/>
        -->
           <input id="COMPANY_ID" name="COMPANY_ID" type="hidden"  />
           <input class="middle_txt" id="COMPANY_NAME"   name="COMPANY_NAME" type="text"  readonly="readonly"  datatype="0,is_null,100" />
		   <input class="mark_btn" type="button" value="&hellip;" onclick="SC('<%=contextPath %>')"/>
      </td>
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap"></td>
		<td class="table_query_2Col_input" nowrap="nowrap"></td>
	</tr>
	<tr  id="bm">
	  <td class="table_query_2Col_label_6Letter" nowrap="nowrap" id="corg">所属部门：</td>
	  <td class="table_query_2Col_input" nowrap="nowrap">
          <input class="middle_txt" id="DEPT_NAME" onblur="isCloseTreeDiv(event,this,'deptt')" readonly="readonly" datatype="0,is_null,100" onclick="showDEPT()" style="cursor: pointer;" name="DEPT_NAME" type="text"/>
      </td>
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap"></td>
		<td class="table_query_2Col_input" nowrap="nowrap"></td>
	</tr>
	<tr>
	<td class="table_query_2Col_label_6Letter" nowrap="nowrap">职位代码：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
          <input class="middle_txt" datatype="0,is_digit_letter,30" maxlength="30" type="text" id="POSE_CODE" name="POSE_CODE"/>
        </td> 
        <td class="table_query_2Col_label_4Letter" nowrap="nowrap">职位级别：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		<script type="text/javascript"> genSelBox("POSE_RANK",<%=Constant.DEALER_USER_LEVEL%>,"",false,"","");</script>
		</td>
        </tr>
	<tr>
	  <td class="table_query_2Col_label_6Letter" nowrap="nowrap">职位名称：<%request.getAttribute("list"); %></td>
	  <td class="table_query_2Col_input" nowrap="nowrap">
        <input class="middle_txt" datatype="0,is_null,30" maxlength="30" type="text" id="POSE_NAME" name="POSE_NAME"/>
      </td>
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap">状态：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
          <script type="text/javascript"> genSelBox("POSE_STATUS",<%=Constant.STATUS%>,"",false,"","");</script>
        </td>
	</tr>
	<tr>
	  <td class="table_query_2Col_label_6Letter" nowrap="nowrap">上级职位：</td>
	  <td class="table_query_2Col_input" nowrap="nowrap">
      <input id="par_pose_name" name="par_pose_name" type="text" value="" class="middle_txt" datatype="0,is_textarea,30" size="30"  readonly="readonly"/> 
			<input id="par_pose_id" name="par_pose_id" value="" type="hidden" class="middle_txt" /> 
			<input id="type" name="type" value="" type="hidden"/> 
			<input type="button" value="..." class="mini_btn" onclick="toPoseList();" />
			<input type="button" value="清空" class="normal_btn" onclick="clrTxt('par_pose_name');" />
      </td>
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap"></td>
		<td class="table_query_2Col_input" nowrap="nowrap">
        </td>
	</tr>
	<tr id="trgjtw" style="display:none;">
	  <td class="table_query_2Col_label_6Letter" nowrap="nowrap">业务范围：</td>
	  <td class="table_query_2Col_input" nowrap="nowrap">
<!--      			<c:forEach items="${brandList}" var="brand">-->
<!--					<input type="checkbox"  name="BRAND"  value="${brand.areaId}"/>${brand.areaName}&nbsp;&nbsp;-->
<!--				</c:forEach>-->
<!--			yin-->
					<input type="checkbox"  name="BRAND" checked value="2012112619161228"/>${brand.areaName}&nbsp;&nbsp;
<!--			end-->
      </td>
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap"></td>
		<td class="table_query_2Col_input" nowrap="nowrap"></td>
	</tr>
</table>
<br/>
<table class="table_query" id="role_list">
<tr class="table_query_th">
	<th colspan="5"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;角色列表</th>
</tr>
    <tr class="table_query_row2">
      	<td width="8%"  nowrap="nowrap" >序号</td>
      	<td width="35%"  nowrap="nowrap" >角色代码</td>
      	<td width="47%"  nowrap="nowrap" >角色名称</td>
		<td width="10%"  nowrap="nowrap" >操作</td>
    </tr>
</table>
<br />
</form>

<table class="de_table_list" style="border-bottom:0px solid #DAE0EE"  width="100%">
	<tr class="table_list_th">
		<td  width="100%"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;功能列表<font color="red">&nbsp;*</font>&nbsp;<span id="msg" style="color: red; visibility: hidden;">请在&nbsp;<a style="cursor: pointer;" onclick="addRole('<%=contextPath%>')">这里</a>&nbsp;添加功能</span></td>
	</tr>
	<tr id="row1" class="table_list_th" >
		<td width="100%" height="280" id="treetd" valign="top" style="padding:3px;line-height:1em;">
			<div class="dtree" id="funTree">
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
			<input name="queryBtn" class="normal_btn" type="button" value="确&nbsp;定" id="myfh" onclick="check_addPose1(this);"/>
			<input class="normal_btn" type="button" value="返&nbsp;回" id="myfhh" onclick="toGoPositionSearch()"/>
			<input type="hidden" id="old_roleIds" name="old_roleIds" />
		</td>
	</tr>
</table>
    <div id="erdiv" style="position: absolute; top:-1000px; background: #FDFFCE; height: 17px; border:1px solid #FFBA43; display: none;">
<img style="margin-top: 1px; margin-left: 2px;" src="<%=contextPath%>/img/exclamation.gif" />
<span id="ermsg" style="color: red; position: absolute; margin-top: 1px;"></span></div>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
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
<script type="text/javascript" >
	function check_addPose1(value){
		if(submitForm('fm')) {
			if(document.getElementById("POSE_BUS_TYPE").value==''){
				MyAlert('职位类型不能为空！');
				
				return ;
			}
			
			var brand = document.getElementsByName("BRAND");
			var brand_flag = false;
			for(var i=0 ; i<brand.length ; i++){
				if(brand[i].checked){
					brand_flag = true;
					break;
				}
			}
			if(!brand_flag){
				MyAlert("请选择业务范围!");
				return;
			}
			var role_table = document.getElementById("role_list");
			var rowNum = role_table.rows.length;
			if(rowNum<=2){
				MyAlert("请新增角色!");
				return;
			}
			addPose1(value);
		}
	}
	function SC(path){ //公用模块代理商选择页面 
		OpenHtmlWindow(path+'/common/OrgMng/queryCompany2.do',800,450);
	}
   function checkArea(areas){ //如果新增的业务范围是已有的 将不再显示
     var arr =  document.getElementsByName('BRAND');
     var brr = areas.split(",");    
	    for (i=0;i<arr.length;i++) {      
	       for(j=0;j<brr.length;j++){
	          if(arr[i].value == brr[j]){
	            arr[i].disabled = true;
	          }
	       }	     
	    } 
   }
</script>
</form>
</body>
</html>
