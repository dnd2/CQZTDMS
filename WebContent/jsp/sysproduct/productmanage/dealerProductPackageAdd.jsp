<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%String contextPath = request.getContextPath();%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商产品套餐维护新增</title>
<script type="text/javascript" >
	function doInit(){
		//__extQuery__(1);
	}
</script> 
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  系统管理 &gt; 产品维护 &gt; 经销商产品套餐维护 &gt; 新增</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_edit" align="center">
			<tr class="tabletitle">
				<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />业务范围信息</th>
			</tr>
			<tr>
				<td width="20%" align="right" nowrap="nowrap"><div align="right">套餐代码：</div></td>
				<td width="20%" align="left" nowrap="nowrap">
      				<input type="text" class="middle_txt" id="packageCode" name="packageCode"/><font color="red">*</font>
    			</td>
    			<td width="10%" align="right" nowrap="nowrap"><div align="right">套餐名称：</div></td>
    			<td width="30%" align="left" nowrap="nowrap">
      				<input type="text" class="middle_txt" name="packageName" id="packageName"/><font color="red">*</font>
    			</td>
			</tr>
			<tr>
			<td width="20%" align="right" nowrap="nowrap"><div align="right">省份：</div></td>
      		<td width="20%" align="left" nowrap="nowrap">
				<select class="short_sel" id="province" name="province">
	    			<option value=""><c:out value="--请选择--"/></option>
					<c:forEach var="province" items="${provinces}">
					<option value="${province.REGION_ID}" >				         						      
					<c:out value="${province.REGION_NAME}"/>
					</option>
				</c:forEach>	
			</select>
    		</td>
    		<td width="10%" align="right" nowrap="nowrap"><div align="right">状态：</div></td>
    		<td width="30%" align="left" nowrap="nowrap">
      			<script type="text/javascript">
		      		genSelBoxExp("status",<%=Constant.STATUS%>,"",false,"short_sel","","false",'');
		    	</script>
    		</td>
			</tr>
			<tr>
			<td width="20%" class="tblopt"></td>
      		<td  width="20%" align="left"></td>
    		<td width="40%" align="right" >
      			<input name="button3" type="button" class="long_btn" onclick="showMaterialGroup_Sel('groupCode','','true','')" value="新增物料组" />
      			<input type="hidden" id="area_id" name="area_id" value="" />
      			<input type="hidden" id="groupIds_" name="groupIds_" />
    		</td>
			</tr>
		</table>

	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
	<form  name="form1" id="form1">
		<table>
			<tr>
				<td>
					<input type="button" name="button1" class="normal_btn" onclick="add();" value="确定" /> 
				</td>
			</tr>
		</table>
	</form>
<script type="text/javascript" >
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];

	var myPage;
	var url = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/showAllGroupList.json?COMMAND=1";
	var title = null;
	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")' />全选", width:'3%',sortable: false,dataIndex: 'GROUP_ID',renderer:myCheckBox},
				{header: "物料代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'GROUP_NAME', align:'center'}
		      ];
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='groupIds' value='" + value + "'checked='check' />");
	}  

	function add(){
		var packageCode = document.getElementById("packageCode").value.replace(/(^\s*)|(\s*$)/g, "");
		var packageName = document.getElementById("packageName").value.replace(/(^\s*)|(\s*$)/g, "");
		var province = document.getElementById("province").value.replace(/(^\s*)|(\s*$)/g, "");
		if(!packageCode){
			MyAlert("请填写产品代码");
			document.getElementById("packageCode").focus();
			return;
		}
		if(!packageName){
			MyAlert("请产品名称");
			document.getElementById("packageName").focus();
			return;
		}
		if(!province){
			MyAlert("请填写省份");
			document.getElementById("province").focus();
			return;
		}
		
		var groupIds = document.getElementsByName("groupIds");
		var groupId="";
		for(var i=0;i<groupIds.length;i++){
			if(groupIds[i].checked){
				groupId = groupId+groupIds[i].value+",";
			}
		}
		if(!groupId){
			MyAlert("请选择物料信息!");
			return;
		}else{
			MyConfirm("是否提交?",addAction,[groupId]);
		}
	}

	function addAction(groupId){
		fm.action = "<%=contextPath%>/sysproduct/productmanage/MaterialGroupManage/addProductAction.do?groupId="+groupId;
		fm.submit();
	}

	function showGroupId(groupIds){
		var groupIds_show = document.getElementsByName("groupIds");
		var gIds = new Array();
		for(var i=0;i<groupIds_show.length;i++){
			gIds.push(groupIds_show[i].value);
		}
		document.getElementById("groupIds_").value = gIds.toString();
		document.getElementById("groupIds_").value=document.getElementById("groupIds_").value+","+groupIds;
		__extQuery__(1);
	}
 </script>    
</body>
</html>