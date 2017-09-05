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
		__extQuery__(1);
	}
</script> 
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  系统管理 &gt; 产品维护 &gt; 经销商产品套餐维护 &gt; 修改</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<input type="hidden" id="product_id" name="product_id" value="${map.PRODUCT_ID }" />
		<table class="table_query" border="0">
			<tr>
				<td align="right" class="tblopt">套餐代码：</td>
				<td>
					<input name="packageCode" value="${map.PACKAGE_CODE }" maxlength="30" datatype="1,is_noquotation,30" id="packageCode" type="text" class="middle_txt" />
					<input name="region_id" id="region_id" value="${map.REGION_ID }" type="hidden"/>
				</td>
				<td align="right" class="tblopt">套餐名称：</td>
				<td>
					<input name="packageName" value="${map.PACKAGE_NAME }" maxlength="30" datatype="1,is_noquotation,30" id="packageName" type="text" class="middle_txt" />
				</td>
    		</tr>
    		<tr>	
    			<td align="right" class="tblopt">状态：</td>
    			<td>
    				<script type="text/javascript">
						genSelBoxExp("status",<%=Constant.STATUS%>,"${map.STATUS }",false,"short_sel","","false",'');
					</script>
    			</td>
    			<td align="right" class="tblopt">省份：</td>
    			<td>
					<select class="short_sel" id="province" name="province">
	    			<option value=""><c:out value="--请选择--"/></option>
					<c:forEach var="province" items="${provinces}">
					<option value="${province.REGION_ID}" >				         						      
					<c:out value="${province.REGION_NAME}"/>
					</option>
					</c:forEach>	
					</select>
    			</td>
    		</tr>
    		<tr>
    			<td colspan="4">
    				<div align="center">
    					<input name="button2" type="button" class="normal_btn" onclick="statuc_Change();" value="确定" />
    					<input name="button3" type="button" class="long_btn" onclick="showMaterialGroup_Sel_con('groupCode','','true','')" value="新增物料组" />
    					<input type="hidden" id="area_id" name="area_id" value="${map.PRODUCT_ID }" />
      					<input type="hidden" id="groupIds_" name="groupIds_" />
    				</div>
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
					<input type="button" name="button1" class="normal_btn" onclick="deleteMaterialGroup();" value="删除" /> 
					<input type="button" name="button3" class="normal_btn" onclick="history.back();"  value="返回" />
				</td>
			</tr>
		</table>
	</form>
<script type="text/javascript" >

	var myPage;
	
	var url = "<%=contextPath%>/sysproduct/productmanage/MaterialGroupManage/getProdyctMaterialInfo.json?COMMAND=1";
	//var url = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/getBusinessAreaDetailInfo.json?COMMAND=1";
	
	var title = null;

	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"relationIds\")' />全选", width:'3%',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
				{header: "物料组代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "物料组名称", dataIndex: 'GROUP_NAME', align:'center'}
		      ];
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='relationIds' value='" + value + "' />");
	}  

	function deleteMaterialGroup(){
		var relationIds = document.getElementsByName("relationIds");
		var relationId="";
		for(var i=0;i<relationIds.length;i++){
			if(relationIds[i].checked){
				relationId = relationId+relationIds[i].value+",";
			}
		}
		if(!relationId){
			MyAlert("请选择物料信息!");
			return;
		}else{
			MyConfirm("是否提交?",deleteMaterialGroupAction,[relationId]);
		}
	}

	function deleteMaterialGroupAction(relationId){
		makeNomalFormCall('<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/deleteProductMaterial.json?relationId='+relationId,showResult,'fm');
	}

	function showResult(json){
		turnQuery();
	}
	
	function turnQuery() {
		 __extQuery__(1);
		
	}

	function toAddBusiness(){
	    OpenHtmlWindow('<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/toAddBusiness.do',700,480);
	}  
	  
	function reloadAction(){
		__extQuery__(1);
	}

	function statuc_Change(){
		var old_status = '${status}';
		var new_status = document.getElementById("status").value;

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
		MyConfirm("是否修改?",statuc_ChangeAction,[new_status]);
		//MyConfirm("是否修改?",statuc_ChangeAction,[new_status]);
	}
	function statuc_ChangeAction(new_status){
		fm.action = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/productChangeAction.do?new_status="+new_status;
		fm.submit();
	}
	
	var region_id=$('region_id').value;
    var po = document.getElementById("province").options ;
    for(var i = 0; i < po.length; i++) {
        if (po[i].value == region_id) {
        	po[i].selected  = true;
        }
    }	
 </script>    
</body>
</html>