<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件直发条件设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript" type="text/javascript">
	function doInit(){
		__extQuery__(1);
	}
</script>
</head>
<body>
<form name='fm' id='fm'>
<input type="hidden" name="prvBrand" id="prvBrand" value="${brand}" />
<input type="hidden" name="defineIdMod" id="defineIdMod" value="${defineIdMod}" />
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 配件直发条件设置 &gt; 维护</div>
	<table class="table_query">
		<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 查询条件</th>
	    <tr>
	      <td width="10%" align="right" >供应商：</td>
	      <td width="20%"><input type="text" id="venderName"  name="venderName" class="long_txt" value="${venderName }" readonly="readonly" />
	        <input type="hidden"  name="venderId" id="venderId" value="${venderId }"/>
          </td>
          <td width="10%" align="right">品牌分类：</td>
	      <td width="20%" >
	      <input class="middle_txt" type="text" name="brand" id="brand" value="${brand}"/>
	      <font color="red">*</font>
	      </td>
	      <td width="10%" align="right">最小订货箱数：</td>
	      <td width="20%" >
	      <input class="middle_txt" type="text" name="criterion" id="criterion" onchange="dataTypeCheck(this)" value="${criterion}"/>
	      <font color="red">*</font>
	      </td>
      </tr>
      <tr>
    	<td  align="center" colspan="6" >
 <!-- 	  <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)"/>  -->
    	  <input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="saveData()"  class="normal_btn"/>
    	  <input type="button" name="addBtn" id="addBtn" value="新增配件" onclick="addNew()"  class="normal_btn"/>
	      <input type="button" name="backBtn" id="backBtn" value="返 回" onclick="goBack()"  class="normal_btn"/>
    	</td>
	  </tr>
	</table>
</div>
<!-- 查询条件 end -->
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
</form>
<script type="text/javascript" >
	var myPage;
	
	var url = "<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/partSTOSetSearch.json";
	
	var title = null;
	
	var columns = [
				{header: "序号", dataIndex: 'DEFT_ID', renderer:getIndex,align:'center'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style:'text-align: left;'},
				{header: "配件名称", dataIndex: 'PART_NAME', style:'text-align: left;'},
				{header: "件号", dataIndex: 'PART_CODE', style:'text-align: left;'},
				{header: "供应商名称", dataIndex: 'VENDER_NAME', align:'center'},
				{header: "最小包装数", dataIndex: 'MIN_PKG', align:'center',renderer:mimPkgText},
				{header: "是否有效", dataIndex: 'STATE', align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];
	     
	
	//设置超链接
	function myLink(value,meta,record)
	{
		var defineId = record.data.DEFT_ID;
		var state = record.data.STATE;
		var disableValue = <%=Constant.STATUS_DISABLE%>;
		if(disableValue == state){
			return String.format("<a href=\"#\" onclick='enableData(\""+defineId+"\")'>[有效]</a>");
	    } else {
	    	return String.format("<a href=\"#\" onclick='cel(\""+defineId+"\")'>[失效]</a>");
		}
			
	}

	//返回 text
	function mimPkgText(value,meta,record)
	{
		var defineId = record.data.DEFT_ID;
		var str = "<input type='hidden'  name='defineIds' value=\""+defineId+"\" /><input type='text' id='minPkg_"+defineId+"' name='minPkg_"+defineId+"' onchange='dataTypeCheck(this)' value=\""+value+"\" />"
		return String.format(str);
			
	}

	//数据验证
	function dataTypeCheck(obj)
	{
		var value = obj.value;
	    if (isNaN(value)) {
	        MyAlert("请输入数字!");
	        obj.value = "";
	        return;
	    }
	    var re = /^[1-9]+[0-9]*]*$/;
	    if (!re.test(obj.value)) {
	        MyAlert("请输入正整数!");
	        obj.value = "";
	        return;
	    }
	}

	//保存设置
	function saveData() {
		var brand = document.getElementById("brand").value;
    	var criterion = document.getElementById("criterion").value;
    	if("" == brand || null == brand)
    	{
    		MyAlert('请设置品牌分类!');
			return false;
    	}

    	if("" == criterion || null == criterion)
    	{
    		MyAlert('请设置最小订货箱数!');
			return false;
    	}
		if(confirm("确定保存设置?")){
			btnDisable();
	     	var url = '<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/updatePartSTOSet.json?curPage='+myPage.page;
	  		makeFormCall(url,showResult,'fm');
	    }
	}
	
	//设置失效：
	function cel(parms) {
		if(confirm("确定失效该数据?")){
			btnDisable();
	     	var url = '<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/celPartSTOSet.json?disabeParms='+parms+'&curPage='+myPage.page;
	  		makeFormCall(url,showResult,'fm');
	    }
	}
	
	//设置有效：
	function enableData(parms) {
		if(confirm("确定有效该数据?")){
			btnDisable();
	     	var url = '<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/enablePartSTOSet.json?enableParms='+parms+'&curPage='+myPage.page;
	  		makeFormCall(url,showResult,'fm');
	    }
	}
	
	function showResult(json) {
		btnEnable();
	    if (json.errorExist != null && json.errorExist.length > 0) {
	        MyAlert(json.errorExist);
	    } else if (json.success != null && json.success == "true") {
	    	MyAlert("操作成功!");
	    	document.getElementById("prvBrand").value = document.getElementById("brand").value;
	    	__extQuery__(json.curPage);          
	    } else {
	        MyAlert("操作失败，请联系管理员!");
	    }
	}

	//新增配件
	function addNew(){
		var defineIdMod = document.getElementById("defineIdMod").value;
		var url = '<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/queryPartsInit.do?defineIdMod='+defineIdMod;
		OpenHtmlWindow(url,700,500);
	}

	//返回
	function goBack(){
		btnDisable();
		location = '<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/partSTOSetInit.do' ;
	}

	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
</script>
</body>
</html>
