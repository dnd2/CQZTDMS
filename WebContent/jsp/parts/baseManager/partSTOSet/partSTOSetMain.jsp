<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>配件直发条件设置</title>
<script language="javascript" type="text/javascript">
	function doInit(){
			//loadcalendar();  //初始化时间控件
			__extQuery__(1);
	}
</script>
</head>
<body>
  <form method="post" name ="fm" id="fm">
    <input type="hidden" name="venderId" id="venderId" value="" />
    <input type="hidden" name="venderName" id="venderName" value="" />
    <input type="hidden" name="brand" id="brand" value="" />
    <input type="hidden" name="criterion" id="criterion" value="" />
    <input type="hidden" name="defineIdMod" id="defineIdMod" value="" />
	<div class="wbox">
		<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
			基础信息管理 &gt; 配件基信息据维护 &gt; 配件直发条件设置
		</div>
		<table class="table_query">
			<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 查询条件</th>
		    <tr>
		      <td width="10%" align="right">配件编码：</td>
		      <td width="20%" ><input class="middle_txt" type="text" name="partOldcode" id="partOldcode"/></td>
		      <td width="10%" align="right">配件名称：</td>
			  <td width="20%" ><input class="middle_txt" type="text" name="partName" id="partName"/></td>
              <td width="10%" align="right">件号：</td>
              <td width="20%" ><input class="middle_txt" type="text" name="partCode" id="partCode"/></td>
	       </tr>
	       <tr>
		      <td width="10%" align="right">供货商：</td>
		      <td width="20%" ><input class="middle_txt" type="text" name="vender_name" id="vender_name"/></td>
		      <td width="10%" align="right">品牌分类：</td>
		      <td width="20%" ><input class="middle_txt" type="text" name="brand_search" id="brand_search"/></td>
		      <td width="10%" align="right">是否有效：</td>
			  <td width="20%">
			    <script type="text/javascript">
			   	 genSelBoxExp("STATE",<%=Constant.STATUS%>,"<%=Constant.STATUS_ENABLE%>",true,"short_sel","","false",'');
			  </script>
		     </td>
	       </tr>
	       <tr>
	    	<td  align="center" colspan="6" >
	    	  <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)"/>
	    	  <input class="normal_btn" type="button" value="新 增" onclick="relationAdd()"/>
	    	  <input class="normal_btn" type="button" value="导 出" onclick="exportPartSTOExcel()"/>
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
				{header: "序号", dataIndex: 'DEFT_ID', renderer:getIndex, style: 'text-align:center'},
                {id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink , style: 'text-align:left'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
				{header: "配件名称", dataIndex: 'PART_NAME',  style: 'text-align:left'},
                {header: "件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
				{header: "供货商名称", dataIndex: 'VENDER_NAME',  style: 'text-align:left'},
				{header: "最小包装数", dataIndex: 'MIN_PKG',  style: 'text-align:center'},
				{header: "品牌分类", dataIndex: 'BRAND',  style: 'text-align:center'},
				{header: "最小订货箱数", dataIndex: 'CRITERION',  style: 'text-align:center'},
				{header: "是否有效", dataIndex: 'STATE',  style: 'text-align:center',renderer:getItemValue}

		      ];
	     
	
	//设置超链接
	function myLink(value,meta,record)
	{
		var defineId = record.data.DEFT_ID;
		var venderId = record.data.VENDER_ID;
		var venderName = record.data.VENDER_NAME;
		var brand = record.data.BRAND;
		var criterion = record.data.CRITERION;
		var state = record.data.STATE;
		var disableValue = <%=Constant.STATUS_DISABLE%>;
		if(disableValue == state){
			return String.format("<a href=\"#\" onclick='enableData(\""+defineId+"\")'>[有效]</a>");
        } else {
        	return String.format("<a href=\"#\" onclick='cel(\""+defineId+"\")'>[失效]</a>&nbsp;<a href=\"#\" onclick='formod(\""+venderId+"\",\""+venderName+"\",\""+brand+"\",\""+criterion+"\",\""+defineId+"\")'>[维护]</a>");
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
        	__extQuery__(json.curPage);          
        } else {
            MyAlert("操作失败，请联系管理员!");
        }
    }

    //维护
    function formod(venderId,venderName,brand, criterion,defineId)
    {
    	btnDisable();
    	document.getElementById("venderId").value = venderId;
    	document.getElementById("venderName").value = venderName;
    	document.getElementById("brand").value = brand;
    	document.getElementById("criterion").value = criterion;
    	document.getElementById("defineIdMod").value = defineId;
    	document.fm.action = "<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/partSTOSetFormodInit.do";
		document.fm.target="_self";
		document.fm.submit();
    }

  	//新增
	function relationAdd(){
		btnDisable();
		window.location.href ="<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/partSTOSetAddInit.do";
	}

	function exportPartSTOExcel()
	{
		document.fm.action = "<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/exportPartSTOExcel.do";
		document.fm.target="_self";
		document.fm.submit();
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