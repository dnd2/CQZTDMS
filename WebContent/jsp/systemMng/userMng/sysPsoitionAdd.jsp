<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
	<script type="text/javascript" src="<%=contextPath%>/js/web/dealer_tree.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
	<title>角色维护</title>
	<script type="text/javascript">
	    var filecontextPath = "<%=contextPath%>";
	    
		var poseSearch = "<%=contextPath%>/sysmng/sysposition/SysPosition/querySysPositionInit.do";
		var getFunsByRoleIdsUrl = "<%=contextPath%>/sysmng/sysposition/SysPosition/getFunsByRoleIds.json";
		var fun_tree_url = "<%=contextPath%>/sysmng/sysposition/SysPosition/initFunTree.json";
		var addPoseUrl = "<%=contextPath%>/sysmng/sysposition/SysPosition/addPosition.json";
		var dept_tree_url = "<%=contextPath%>/sysmng/usemng/SgmSysUser/initOrgTree.json?unorglevel=3";

		var thisFunId = null; // 用户当前选择的功能
		var funsh = new HashMap();
		var backcount = -1;
		
		function isVev(fId) {
			for(var n=0; n<myfuns.length; n++) {
				if((myfuns[n]+'').indexOf(fId+'') == 0) {
					return true;
				}
			}
			return false;	
		}
		
		// 添加角色
		function addRole(path) {
			backcount --;
			var role_ids = document.getElementsByName("ROLE_ID");
			var old_roleIds="";
			if(role_ids){
				for(var i=0; i<role_ids.length;i++){
					old_roleIds = old_roleIds + role_ids[i].value;
					if(i != role_ids.length-1){
						old_roleIds = old_roleIds+",";
					}
				}
				document.getElementById("old_roleIds").value=old_roleIds;
			}
			OpenHtmlWindow(path+"/sysmng/sysposition/SysPosition/addSysPositionRoleInit.do?poseType="+$('#POSE_TYPE').val(),800,600,'角色选择');
		}
		
		function showFun(roles) {
			if(roles != null && roles != "" && roles.length >0) {
				sendAjax(getFunsByRoleIdsUrl+"?roleIds="+roles,showFunBack,'fm');
			}
		}
		
		function showFunBack(funss) {
			myfuns = null;
			myfuns = new Array();
			
			myfun = funss["funs"];
			for(var n=0; n<myfun.length; n++) {
				myfuns.push(myfun[n].funcId);
			}
			showFunsTree();
			delPalRow();
			var myRole = new Array();
			myRole = funss["roelList"];
			for(var n=0; n<myRole.length; n++) {
				addPlanRow(myRole[n].roleId,myRole[n].roleName,myRole[n].roleDesc,n);
			}
		}
		
		function delPalRow() {
			var addTable = $('#role_list')[0];
			var rowss = addTable.rows.length;
			for(var dels = 2; rowss > dels; rowss--) { 
				addTable.deleteRow(rowss-1);
			}
		}
		
		function deleteRow(row){
			var addTable = $('#role_list')[0];
			var rows = addTable.rows;
			addTable.deleteRow(row);
			for(var i = row;i<addTable.rows.length;i++){
				addTable.rows[i].cells[3].innerHTML = "<td class=table_info_input nowrap=nowrap align='center'><a href='javascript:void(0)' target='_self' onclick='deleteRow("+i+");'>[删除]</a></td>";
			}
			var roleIds = document.getElementsByName('ROLE_ID');
			var temp = new Array();
			for(var i=0; i<roleIds.length; i++) {
				temp.push(roleIds[i].value);
			}
			showFun(temp.toString());
		}
		
		function showFunsTree() {
			b = null;
			b = new dTree('b','funTree','false','false','true'); 
			b.config.closeSameLevel=false;
			b.config.myfun="setDataAuth";
			b.config.folderLinks=false; 
			sendAjax(fun_tree_url+"?tree_root_id=",createFunTree,'fm');
			b.closeAll();
		}
		
		function setDataAuth(id) {
			var fid = b.aNodes[id].id;
			thisFunId = fid;
			setSeleClear();
			if(funsh.get(fid) == null) { //第一次
			} else {
				setSele(fid);
			}
		}
		
		function setSeleClear() {
			var aa = document.getElementsByName("c1");
			for(var v=0; v<aa.length; v++) {
				aa[v].checked = false;
			}
		}
		
		function setSele(fid) {
			var arrobj = funsh.get(fid);
			var aa = document.getElementsByName("c1");
			for(var n=0; n<arrobj.length; n++) {
				for(var v=0; v<aa.length; v++) {
					if(aa[v].value == arrobj[n]) {
						aa[v].checked = true;
					}
				}
			}
		}

		function createFunTree(reobj) {
			var funlistobj = reobj["funlist"];
			var funcCode,parFuncId,funcId,funcName;
			for(var i=0; i<funlistobj.length; i++) {
				parFuncId = funlistobj[i].parFuncId;
				funcId = funlistobj[i].funcId;
				funcName = funlistobj[i].funcName;
				funcCode = funlistobj[i].funcCode;
				if(parFuncId == 0) { //系统根节点
					b.add(funcId,"-1",funcName);
				} else if(funcId.length<=10 && isVev(funcId)){
					b.add(funcId,parFuncId,funcName,funcCode);
				}
			}
			b.draw();
			b.openAll();
		}
		
		function addPlanRow(roleId,roleCode,roleName,n)
		{	
			var addTable = $('#role_list')[0];
			var rows = addTable.rows;
			var length = rows.length;
			
			var insertRow = addTable.insertRow(length);
			if(n == 0)
				insertRow.className = "table_list_row2";
			else
			    insertRow.className = "table_list_row1";
			insertRow.id = roleId;
			insertRow.insertCell(0);
			insertRow.insertCell(1);
			insertRow.insertCell(2);
			insertRow.insertCell(3);
			addTable.rows[length].cells[0].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'>"+(length-1)+"</td>";
			addTable.rows[length].cells[1].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'><input type='hidden' value='"+roleId+"' name='ROLE_ID'/>"+roleCode+"</td>";
			addTable.rows[length].cells[2].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'>"+roleName+"</td>";
			addTable.rows[length].cells[3].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'><a href='javascript:void(0)' target='_self' onclick='deleteRow("+(length)+");'>[删除]</a></td>";
		}

		// 职位类型
		var subcat = new Array();
		subcat[0] = new Array("<%=Constant.POSE_BUS_TYPE_SYS%>","系统管理(ALL)","<%=Constant.SYS_USER_SGM%>"); 	
		subcat[1] = new Array("<%=Constant.POSE_BUS_TYPE_VS%>","车厂销售管理","<%=Constant.SYS_USER_SGM%>"); 	
		subcat[2] = new Array("<%=Constant.POSE_BUS_TYPE_WR%>","车厂售后管理","<%=Constant.SYS_USER_SGM%>"); 	
		subcat[3] = new Array("<%=Constant.POSE_BUS_TYPE_DVS%>","经销商销售","<%=Constant.SYS_USER_DEALER%>"); 	
		subcat[4] = new Array("<%=Constant.POSE_BUS_TYPE_DWR%>","经销商售后","<%=Constant.SYS_USER_DEALER%>"); 	
		subcat[5] = new Array("<%=Constant.POSE_BUS_TYPE_WL%>","储运物流","<%=Constant.SYS_USER_SGM%>"); 
	
		var mybody, myfuns, addDeptNodeId, deptTreew, deptTreeh, depttree;
	 	var subStr = "funlist";
	 
	 	// 页面初始化
		 $(function($){
			 $("#deptt").addClass("dtree");
			 $("#deptt").css({ "z-index": "3000", "position": "absolute", "border": "1px solid #5E7692", "display": "none",
				 "width": "220px", "height": "333px", "padding": "1px", "orphans": "0", "background": "#F5F5F5", "overflow": "auto" });
			 $("#DEPT_NAME").css({"width":"200px"});
			 $("#COMPANY_NAME").css({"width":"200px"});
			 depttree = new dTree('depttree','deptt','false','false','true');
			 changePoseSelectItem($("#POSE_TYPE").val()); // 根据职位类别动态初始化职位类型
			 
			 $(document).on("click",function(event){
				 var x = event.pageX;
				 var y = event.pageY;
				 var dxl = $('#DEPT_NAME').position().left;
				 var dxr = $('#DEPT_NAME').position().left + $('#deptt').width();
				 var dyt = $('#DEPT_NAME').position().top - 30;
				 var dyb = $('#DEPT_NAME').position().top - 30 + $('#deptt').height();
				 // $("#test").html(x + " : " + dxl + " " + dxr  +" | " + y + " : " + dyt + " " + dyb + " 结果：" + ((dxl < x && x < dxr) && (dyt < y && y < dyb)));
				 if((dxl < x && x < dxr) && (dyt < y && y < dyb)) {
					 return;
				 } else {
					 $("#deptt").hide();
				 }
			 });
		 });
		 
		 function addPose(obj) {
			 makeNomalFormCall(addPoseUrl,function(json){
				 if(json.st == "poseCode_error") {
					MyAlert("职位代码重复，请重新输入!") ;
					return ;
				} else if(json.st == "poseName_error") {
					MyAlert("职位名称重复，请重新输入!") ;
					return ;
				} else {
					MyAlert('保存成功!');
					window.location = poseSearch;
				}
			 },'fm');
		 }
		 
		 
		// 根据职位类别动态初始化职位类型
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
		}
	 
		// 部门树加载对象
		 var dt = {
			 initDeptTree : function(){
				 $('#tree_root_id').val("");
				 $('#deptt').css({"top" : $('#DEPT_NAME').offset().top + 30, "left" : $('#DEPT_NAME').offset().left, "opacity" : 0.8});
				 depttree.config.closeSameLevel=false;
				 depttree.config.myfun="dt.deptpos";
				 depttree.config.folderLinks=true;
				 depttree.delegate=function (id){
					addDeptNodeId = depttree.aNodes[id].id;
				    var nodeID = depttree.aNodes[id].id;
				    $('#tree_root_id').val(nodeID);
				    sendAjax(dept_tree_url,dt.createDeptNode,'fm');
				};
				sendAjax(dept_tree_url,dt.createDeptTree,'fm');
				depttree.draw();
				depttree.closeAll();
				$("#deptt").show();
			 },
			 deptpos : function(id) {
				var orgid = depttree.aNodes[id].id;
				var orgname = depttree.aNodes[id].name;
				$('#DEPT_ID').val(orgid);
				$('#DEPT_NAME').val(orgname);
				$("#deptt").hide();
			 },
			 createDeptTree : function(json) {
				var orglistobj = json[subStr];
				var orgId,parentOrgId,orgName,orgCode,orgId;

				for(var i=0; i<orglistobj.length; i++) {
					orgId = orglistobj[i].orgId;
					orgName = orglistobj[i].orgName;
					orgCode = orglistobj[i].orgCode;
					parentOrgId = orglistobj[i].parentOrgId;

					if(parentOrgId == -1) {
						depttree.add(orgId,"-1",orgName,orgCode);
					} else {
						depttree.add(orgId,parentOrgId,orgName,orgCode);
					}
				}
				depttree.draw();
			 }
		 };
		 
		// 职位类别变更联动
		function selType(obj,sgmCode,dealerCode) {
			if(obj.value == sgmCode) {
				$('#COMPANY_ID').val("");
				$('#COMPANY_NAME').val("");
				$('#jxs').hide();
			}else if(obj.value == dealerCode) {
				$('#COMPANY_ID').val("");
				$('#COMPANY_NAME').val("");
				$('#jxs').show();
			}
			
			if(obj.value == sgmCode) {
				$('#DEPT_ID').val("");
				$('#DEPT_NAME').val("");
				$('#bm').show();
				var choose = document.getElementsByName("chooseDlr");
				for(var i = 0 ; i < choose.length ; i++){
					if(choose[i].checked){
						if(choose[i].value == <%=Constant.IF_TYPE_YES%>){
							document.getElementById("myDlrDiv").style.display = 'block';
							document.getElementById("myCityDiv").style.display = 'none';
							
						}
						if(choose[i].value == <%=Constant.IF_TYPE_NO%>){
							document.getElementById("myDlrDiv").style.display = 'none';
							document.getElementById("myCityDiv").style.display = 'block';
						}
					}
				}
			}else if(obj.value == dealerCode) {
				$('#DEPT_ID').val("");
				$('#DEPT_NAME').val("");
				$('#bm').hide();
				$('#myDlrDiv').hide();
				$('#myCityDiv').hide();
			}
			
			changePoseSelectItem($("#POSE_TYPE").val());
		}
		
	function changePoseType(value){
		if(value=='<%=Constant.POSE_BUS_TYPE_WL%>'){
			document.getElementById('WlCompany1').style.display='';
			document.getElementById('WlCompany2').style.display='';
		}else{
			document.getElementById('WlCompany1').style.display='none';
			document.getElementById('WlCompany2').style.display='none';
		}
	}
	
	function showDlrDiv(){
		var choose = document.getElementsByName("chooseDlr");
		for(var i = 0 ; i < choose.length ; i++){
			if(choose[i].checked){
				if(choose[i].value == <%=Constant.IF_TYPE_YES%>){
					document.getElementById("myDlrDiv").style.display = 'block';

					document.getElementById("myCityDiv").style.display = 'none';
				}
				if(choose[i].value == <%=Constant.IF_TYPE_NO%>){
					document.getElementById("myDlrDiv").style.display = 'none';

					document.getElementById("myCityDiv").style.display = 'block';
				}
			}
		}
	}
	
	function txtClr(value){
		 document.getElementById(value).value = "";
	 }
	 
	 function chooseDealer() {
	 	var dealerType = document.getElementById("POSE_BUS_TYPE").value;
	 	if(dealerType == '') {
	 		MyAlert('请先选择职位类型!'); return;
	 	}
	 	if(dealerType == '<%=Constant.POSE_BUS_TYPE_DVS %>') {
	 		showOrgDealer('outDealerCode','COMPANY_ID','false', '', 'true','','<%=Constant.DEALER_TYPE_DVS %>','COMPANY_NAME');	
	 	} else if(dealerType == '<%=Constant.POSE_BUS_TYPE_DWR %>') {
	 		showOrgDealer('outDealerCode','COMPANY_ID','false', '', 'true','','<%=Constant.DEALER_TYPE_DWR %>','COMPANY_NAME');
	 	}
	 }
	 
	<%-- 
	// 暂时不用 
	function getModel(v2,v1,v3)
	{
		var dealerIds = document.getElementsByName("dealerIds");
		var attr1 = v1.split(",");
		var attr2 = v2.split(",");
		var attr3 = v3.split(",");
		if(attr1.length > 0&&v1.length>0){
			var mytable = document.getElementById("dealer_list");
			for(var i = 0;i<attr1.length;i++){
				var flag = true;
				if(dealerIds.length>0){
					for(var j = 0 ; j< dealerIds.length;j++){
						var dealerId = dealerIds[j].value;
						if(dealerId == attr2[i]){
							flag = false;
							break;
						}
					}
				}
				if(flag){
					var newTr = mytable.insertRow();
					var newTd1 = newTr.insertCell();
					var newTd2 = newTr.insertCell();
					var newTd3 = newTr.insertCell();
					var newTd4 = newTr.insertCell();
					newTr.className = "table_list_row1";
					newTr.align = 'center';
					newTd1.innerHTML = mytable.rows.length-2;
					newTd2.innerText = attr1[i];
					newTd3.innerText = attr3[i];
					newTd4.innerHTML = "<input type='hidden' id='dealerIds' name='dealerIds' value='"+attr2[i]+"'>"+'<input type=button value="删除" class="u-button" name="remain" onclick="delItem(this)">';
				}
			}
		}
	}
	
	// 暂时不用 删除一行
	function delItem(obj)
	{
		var mytable = document.getElementById("dealer_list");
		var rowIndex = obj.parentElement.parentElement.rowIndex; 
		mytable.deleteRow(rowIndex);
		//重新排列序号
		 for(i=rowIndex;i<mytable.rows.length;i++){
			 if(i >= 2){
				 mytable.rows[i].cells[0].innerHTML = i-1;
			 }
		 }
		 
	}
	
	function choosedDealerClear(){
		 var mytable = document.getElementById("dealer_list");
		 var sumRow = mytable.rows.length - 1;
		 for(var i = sumRow ; i > 1 ; i--){
				 if(i > 1){
					 mytable.deleteRow(i);
				 }
		 }
	 }
	
	// 暂时不用
	function showOrgDealer_2(inputCode ,inputId ,isMulti ,orgId, isAllLevel, isAllArea,isDealerType,inputName)
	{
		if(!inputCode){ inputCode = null;}
		if(!inputId){ inputId = null;}
		if(!isMulti){ isMulti = null;}
		if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
		if(!isAllLevel){ isAllLevel = null;}
		if(!isAllArea){ isAllArea = null;}
		if(!isDealerType){ isDealerType = null;}
		if(!inputName){ inputName = null;}
		var org_id=null;
		
		var poreBusType = document.getElementById("POSE_BUS_TYPE").value;
		if(poreBusType == <%=Constant.POSE_BUS_TYPE_VS%>){
			isDealerType = <%=Constant.DEALER_TYPE_DVS%>;
		}
		if(poreBusType == <%=Constant.POSE_BUS_TYPE_WR%>){
			isDealerType = <%=Constant.DEALER_TYPE_DWR%>;
		}
		OpenHtmlWindow(g_webAppName+'/dialog/showOrgDealer.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+org_id+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea+"&isDealerType="+isDealerType+"&inputName="+inputName,730,390);
	}
	 --%>
</script>
<script type="text/javascript" >
    var boot ;
    // 检查职位提报数据是否合格
	function check_addPose(value){
		boot = value;
		if(submitForm('fm')) {
			if(document.getElementById("POSE_BUS_TYPE").value==''){
				MyAlert('职位类型不能为空！');
				return ;
			}else{
				if(document.getElementById("POSE_BUS_TYPE").value=='<%=Constant.POSE_BUS_TYPE_WL%>'){
						if(document.getElementById("logiCompany").value==''){
						MyAlert('物流公司不能为空！');
						
						return ;
					}
				}
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
				MyAlert("请选择产地!");
				return;
			}
			var role_table = document.getElementById("role_list");
			var rowNum = role_table.rows.length;
			if(rowNum<=2){
				MyAlert("请新增角色!");
				return;
			}
			var POSE_TYPE = document.getElementById("POSE_TYPE");
			if(POSE_TYPE.value == <%=Constant.SYS_USER_SGM%>){
				var chooseDlr = document.getElementsByName("chooseDlr");
				if(chooseDlr.length>0){
					for(var i = 0;i<chooseDlr.length;i++){
						if(chooseDlr[i].checked){
							if(chooseDlr[i].value == <%=Constant.IF_TYPE_YES%>){
								var dealerIds =	document.getElementsByName("dealerIds");
								if(dealerIds == null || dealerIds.length == 0){
									MyAlert("请选择经销商!");
									return;
								}
							}else{//否的时候（ranjian add）
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
							}
						}
					}
				}
			}
	   		addPose(value);
		}
	}
	function jude_result(json)
	{
		if(json.retType == "YES")
		{
		  addPose(boot);
		}else
		{
			MyAlert(json.retType);
		}
	}
	//公用模块代理商选择页面 
	function SC(path){ 
		OpenHtmlWindow(path+'/common/OrgMng/queryCompany2.do',800,450,'经销商公司选择');
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
   
   function chkPoseType(obj) {
	   var addTable = $('#role_list')[0];
		var rowss = addTable.rows.length;
		if(rowss > 2) {
			MyAlert("请删除已选择角色，再变更职位类别！") ;
			obj.blur() ;
			return false;
		} 
   }
</script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 用户管理 &gt;职位维护</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="FUNSH" id="FUNSH"/>
		<input type="hidden" name="MYFUNS" id="MYFUNS"/>
		<input id="DEALER_ID" name="DEALER_ID" type="hidden"/>
		<input id="ROLE_IDS" name="ROLE_IDS" type="hidden" value=""/>
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		<div class="dtree" id="deptt"></div>
		<table class="table_query" style="border:1px solid #DAE0EE">
			<tr>
				<td class="right">职位类别：</td>
				<td>
					<script type="text/javascript"> 
						genSelBox("POSE_TYPE",<%=Constant.SYS_USER%>,"",false,"","onclick='chkPoseType(this)' onchange='selType(this,<%=Constant.SYS_USER_SGM%>,<%=Constant.SYS_USER_DEALER%>) ;'");
					</script>
				</td>
				<td class="right">职位类型：</td>
				<td>
					<select class="u-select" name="POSE_BUS_TYPE" id="POSE_BUS_TYPE" onchange="changePoseType(this.value)"></select><font color="red">*</font>
				</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr id="jxs" style="display: none;">
		    	<td class="right">经销商：</td>
				<td >
	   				<input name="outDealerCode" type="hidden" id="outDealerCode" class="middle_txt" value=""  />
	   				<input name="COMPANY_NAME" type="text" id="COMPANY_NAME" class="middle_txt" onclick="chooseDealer()" />
	   				<input name="COMPANY_ID" type="hidden" id="COMPANY_ID" class="middle_txt" value="" />
	                <input type="button" class="u-button" onclick="txtClr('outDealerCode');txtClr('COMPANY_ID');txtClr('COMPANY_NAME');" value="清 空" id="clrBtn" /> 
		    	</td>
			</tr>
			<tr>
				<td id="WlCompany1" style="display:none" class="right">物流公司：</td>
				<td style="display:none" id="WlCompany2">
					<select name="logiCompany" id="logiCompany" class="u-select">
						<option value="">--请选择--</option>
						<c:forEach items="${logiList }" var="list">
						<option value="${list.LOGI_ID }">${list.LOGI_NAME }</option>
						</c:forEach>
				  	</select><font color="red">*</font>
				 </td>
				 <td></td>
				 <td></td>
			</tr>
			<tr id="bm">
				<td id="corg" class="right">所属部门：</td>
			  	<td>
			  		<input type="hidden" name="DEPT_ID" id="DEPT_ID" value="" />
		          	<input class="middle_txt" id="DEPT_NAME" readonly="readonly" datatype="0,is_null,100"  onclick="dt.initDeptTree()" name="DEPT_NAME" type="text"/>
		      	</td>
				<td class="right"></td>
				<td>
					<span style="display:none;">
					选择经销商：
					<label class="u-radiobox">
						<input type="radio" id="chooseDlr" name="chooseDlr" value="<%=Constant.IF_TYPE_YES %>" onclick="showDlrDiv()"/><span></span>
					</label> 
					<label class="u-label"><span>是</span></label>
					
					<label class="u-radiobox">
						<input type="radio" id="chooseDlr" name="chooseDlr" value="<%=Constant.IF_TYPE_NO %>" onclick="showDlrDiv()" checked="checked"/><span></span>
					</label> 
					<label class="u-label"><span>否</span></label>
					</span>
				</td>
			</tr>
			<tr>
			<td class="right">职位代码：</td>
				<td>
		          <input class="middle_txt" datatype="0,is_digit_letter,30" maxlength="30" type="text" id="POSE_CODE" name="POSE_CODE"/>
		        </td> 
		        <td></td>
				<td></td>
		        </tr>
			<tr>
				<td class="right">
			  		职位名称：<%request.getAttribute("list");  %>
			  	</td>
			  	<td>
		        	<input class="middle_txt" datatype="0,is_null,30" maxlength="30" type="text" id="POSE_NAME" name="POSE_NAME"/>
		     	</td>
				<td class="right">状态：</td>
				<td>
		        	<script type="text/javascript"> 
		          		genSelBox("POSE_STATUS",<%=Constant.STATUS%>,"",false,"","");
		          	</script>
		        </td>
			</tr>
			<tr id="trgjtw">
				<td class="right">产地：</td>
			  	<td>
	      			<c:forEach items="${brandList}" var="brand">
	      				<label class="u-checkbox">
							<input type="checkbox"  name="BRAND"  value="${brand.areaId}"/>
							<span></span>
						</label>
						<label class="u-label">
							<span>${brand.areaName}</span>
						</label>
					</c:forEach>
		      	</td>
				<td></td>
				<td></td>
			</tr>
		</table>
		<br/>
		<div id="myDlrDiv" name="myDlrDiv" style="display: none;">
			<table class="table_query" id="dealer_list">
				<tr class="table_query_th">
					<th colspan="5">
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;经销商列表
						<input type="button" class="u-button" value="增加" onclick="showOrgDealer_2('', '', 'true', '', 'true')"/> 
					</th>
				</tr>
				<tr class="table_query_row2" align="center">
					<td width="8%"  nowrap="nowrap">序号</td>
					<td width="35%" nowrap="nowrap">经销商代码</td>
					<td width="47%" nowrap="nowrap">经销商名称</td>
					<td width="10%" nowrap="nowrap">操作</td>
				</tr>
			</table>
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
								<input type="checkbox"  name="PROVICE"  value="${provice.orgId}"/>
								<span></span>
							</label> <label class="u-label"><span>${provice.orgName}</span></label>
						</div>
						</c:forEach>
					</td>
				</tr>
			</table>
		</div>
		<table class="table_query" id="role_list">
			<tr>
				<th colspan="5" style="border-bottom: 2px solid #ccc;"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;角色列表</th>
			</tr>
		    <tr>
		      	<td width="8%"  nowrap="nowrap" >序号</td>
		      	<td width="35%"  nowrap="nowrap" >角色代码</td>
		      	<td width="47%"  nowrap="nowrap" >角色名称</td>
				<td width="10%"  nowrap="nowrap" >操作</td>
		    </tr>
		</table>
		<br />
		<table  style="border-bottom:0px solid #DAE0EE"  width="100%">
			<tr class="table_list_th">
				<td  width="100%">
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;
					功能列表<font color="red">&nbsp;*</font>&nbsp;
					<span id="msg" style="color: red; visibility: hidden;">
						请在&nbsp;<a style="cursor: pointer;" onclick="addRole('<%=contextPath%>')">这里</a>&nbsp;添加功能
					</span>
				</td>
			</tr>
			<tr id="row1">
				<td width="100%" id="treetd" valign="top" style="padding:3px;line-height:1em;">
					<div class="dtree" id="funTree" style="height: 150px; overflow-y: scroll;">
						<script type="text/javascript">
							var b;
						</script>
					</div>
				</td>
			</tr>
			<tr>
				<td align="center">
					<input class="u-button" type="button" value="新增角色" id="mysub" onclick="addRole('<%=contextPath%>')"/>
					<input name="queryBtn" class="u-button" type="button" value="确&nbsp;定" id="myfh" onclick="check_addPose(this);"/>
					<input class="u-button" type="button" value="返&nbsp;回" id="myfhh" onclick="window.history.go(-1)"/>
					<input type="hidden" id="old_roleIds" name="old_roleIds" />
				</td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>
