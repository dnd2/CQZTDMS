<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>销售主数据维护</title>
<%
	String contextPath = request.getContextPath();
%>
<script type="text/javascript">
	var plannerArray = new Array(); //创建一个新的计划员数组
    <c:forEach var= "list" items="${plannersList}" varStatus="sta"> //得到有数据的数组集合
    	plannerArray.push(['${list.USER_ID}&&${list.NAME}']);//得到数组的内容（实体bean)加入到新的数组里面
    </c:forEach>
	
    var purchaserArray = new Array(); //创建一个新的采购员数组
    <c:forEach var= "list" items="${purchasersList}" varStatus="sta"> //得到有数据的数组集合
    	purchaserArray.push(['${list.USER_ID}&&${list.NAME}']); //得到数组的内容（实体bean)加入到新的数组里面
    </c:forEach>
    
    function doInit(){
   		//loadcalendar();  //初始化时间控件
   		__extQuery__(1);
	}
</script>
</head>
<body onload="doInit()">
<form method="post" name="fm" id="fm" method="post" enctype="multipart/form-data">
<div class="wbox">
<input type="hidden" id="selPartId" name="selPartId" value=""/>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
基础信息管理 &gt; 配件基础信息维护 &gt; 销售主数据维护</div>
  <table class="table_query">
	<th colspan="6"><img class="nav"
		src="<%=contextPath%>/img/subNav.gif" /> 查询条件</th>
	<tr>
		<td width="10%" align="right">配件编码：</td>
		<td width="20%"><input class="middle_txt" type="text" name="PART_OLDCODE" /></td>
		<td width="10%" align="right">配件名称：</td>
		<td width="20%"><input class="middle_txt" type="text" name="PART_CNAME" /></td>
        <%--<td width="10%" align="right">包装发运方式：</td>
          <td width="20%" >
			<script type="text/javascript">
        	 		genSelBoxExp("PACK_STATE",<%=Constant.PART_PACK_STATE%>,"",true,"short_sel","","false",'');
        	 	</script> 
		</td>--%>
		<td width="10%"  align="right" style="display: none"></td>
		<td width="20%" style="display: none">
			<script type="text/javascript">
				genSelBoxExp("STATE",<%=Constant.STATUS%>,<%=Constant.STATUS_ENABLE%>,true,"short_sel","","false",'');
			</script>
		</td>

		<td width="10%"  align="right">是否特殊：</td>
		<td width="20%" >
			<script type="text/javascript">
				genSelBoxExp("IS_SPECIAL",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
			</script>
		</td>
	</tr>
	<tr>
		<td width="10%"  align="right">是否打码：</td>
		<td width="20%" >
		<script type="text/javascript">
		   	 genSelBoxExp("OF_FLAG",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
		</script>
		</td>

        <td width="10%"  align="right">是否精品：</td>
        <td width="20%" >
            <script type="text/javascript">
                genSelBoxExp("IS_DIRECT",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
            </script>
        </td>
        <td width="10%"  align="right">是否广宣：</td>
        <td width="20%" >
            <script type="text/javascript">
                genSelBoxExp("IS_GX",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
            </script>
        </td>
		<!-- 
		<td width="10%"  align="right">是否紧缺件：</td>
		<td width="20%" >
		<script type="text/javascript">
		   	 genSelBoxExp("IS_LACK",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
		</script>
		</td>
		 -->
	</tr>
	<tr>
		<!-- 
		<td width="10%"  align="right">是否计划：</td>
		<td width="20%"  >
		<script type="text/javascript">
		   	 genSelBoxExp("IS_PLAN",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
		</script>
		</td>
		 -->
		<!-- add by zhumingwei 2013-09-16 -->
		<!-- 

		 -->
		
	</tr>
	<tr>
		<td align="center" colspan="6">
		  <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
		  <input class="normal_btn" type="button" value="导 出" onclick="exportPartStockExcel()"/>
          <input class="normal_btn" type="button" value="批量导入" id="upload_button" name="button1"onclick="showUpload();">
		</td>
	</tr>
  </table>
  <div style="display:none ; heigeht: 5px" id="uploadDiv">
	<table  class="table_query">
	  <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 上传文件</th>
		<tr>
			<td colspan="2"><font color="red"> 
			&nbsp;&nbsp;<input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()" />
			文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font> 
			<input type="file" name="uploadFile" id="uploadFile" style="width: 250px" datatype="0,is_null,2000" value="" /> &nbsp; 
			<input type="button" id="upbtn" class="normal_btn" value="确 定" onclick="confirmUpload()" />
			</td>
		</tr>
	</table>
  </div>

  <!--分页 begin -->
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
</form>
<script type="text/javascript">
	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/PartBaseQuery/partSalesBaseQuery.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex,style:'text-align:left'},
                {id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style:'text-align:left'},
				{header: "配件名称", dataIndex: 'PART_CNAME',style:'text-align:left'},
                {header: "件号", dataIndex: 'PART_CODE', style:'text-align:left'},
				//{header: "包装发运方式", dataIndex: 'PACK_STATE', style:'text-align:center',renderer:getPackState},
				//{header: "是否计划", dataIndex: 'IS_PLAN', style:'text-align:center',renderer:planCheckBox},
				{header: "是否精品", dataIndex: 'IS_DIRECT', style:'text-align:center',renderer:directCheckBox},
				{header: "是否广宣", dataIndex: 'IS_GXP', style:'text-align:center',renderer:gxCheckBox},
				//{header: "是否紧缺件", dataIndex: 'IS_LACK', style:'text-align:center',renderer:lackCheckBox},
				{header: "是否打码", dataIndex: 'OF_FLAG', style:'text-align:center',renderer:ofFlagCheckBox},
				{header: "是否特殊", dataIndex: 'IS_SPECIAL', style:'text-align:center',renderer:isSpecialCheckBox},
				{header: "是否有效", dataIndex: 'STATE', style:'text-align:center',renderer:getItemValue}

		      ];
	     
	//设置超链接
	function myLink(value,meta,record)
	{
		var partID = record.data.PART_ID;
	 	var state = record.data.STATE;
		return String.format("<a href=\"#\" onclick='save(\""+partID+"\")'>[保存]</a>");
	}

	//保存
	function save(parms)
	{
		if(confirm("确定要保存?")){
			var objPlan = document.getElementById("planCheckBox_"+parms);
			var objDirect  = document.getElementById("isDirectCheckBox_"+parms);
			var objLack = document.getElementById("lackCheckBox_"+parms);
			//var packState = document.getElementById("PACK_STATE"+parms).value;
			var objOfFlag = document.getElementById("ofFlag_"+parms);
			var objRecv1 = document.getElementById("isSpecialCheckBox_"+parms);//add by zhumingwei 2013-09-16
			var objGxp = document.getElementById("isGxpCheckBox_"+parms);
			var checkedValue = <%=Constant.IF_TYPE_YES%>;
			var uncheckedValue = <%=Constant.IF_TYPE_NO%>;
			var isPlan = uncheckedValue;
			var isDirect = uncheckedValue;
			var ofFlag = uncheckedValue;
			var isLack = uncheckedValue;
			var isRecv1 = uncheckedValue;
			var isGxp = uncheckedValue;
			
			//if(objPlan.checked)
			//{
				//isPlan = checkedValue;
			//}
            if(objDirect.checked)
            {
                isDirect = checkedValue;
            }
			//if(objLack.checked)
			//{
				//isLack = checkedValue;
			//}

			if(objOfFlag.checked)
			{
				ofFlag = checkedValue;
			}

			if(objRecv1.checked)
			{
				isRecv1 = checkedValue;
			}
			
			if(objGxp.checked)
			{
				isGxp = checkedValue;
			}
			
			btnDisable();
   	     	var url = '<%=contextPath%>/parts/baseManager/PartBaseQuery/savePartSalesBase.json?partId='+parms+'&isPlan='+isPlan+'&isLack='+isLack+'&ofFlag='+ofFlag+'&isRecv1='+isRecv1+'&curPage='+myPage.page+'&isDirect='+isDirect+'&isGxp='+isGxp;
   	  		makeFormCall(url,showResult,'fm');
   	    }
	}

	//下载
	function exportPartStockExcel(){
		document.fm.action="<%=contextPath%>/parts/baseManager/PartBaseQuery/expPartSalesBaseExcel.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	//上传
	function uploadExcel(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/baseManager/PartBaseQuery/partSalesBaseUpload.do";
		fm.submit();
	}

	//上传检查和确认信息
	function confirmUpload()
	{
		if(fileVilidate())
		{
			MyConfirm("确定导入选择的文件?",uploadExcel,[]);
		}
		
	}

	function fileVilidate(){
		var importFileName = $("uploadFile").value;
		if(importFileName==""){
		    MyAlert("请选择导入文件!");
			return false;
		}
		var index = importFileName.lastIndexOf(".");
		var suffix = importFileName.substr(index+1,importFileName.length).toLowerCase();
		if(suffix != "xls" && suffix != "xlsx"){
		MyAlert("请选择Excel格式文件");
			return false;
		}
		return true;
	}
	
	function showUpload(){
		var uploadDiv = document.getElementById("uploadDiv");
		if(uploadDiv.style.display=="block" ){
			uploadDiv.style.display = "none";
		}else {
		uploadDiv.style.display = "block";
		}
	}

	//下载上传模板
	function exportExcelTemplate(){
		fm.action = "<%=contextPath%>/parts/baseManager/PartBaseQuery/exportPartSalesTemplate.do";
		fm.submit();
	}
	
    function showResult(json) {
    	btnEnable();
        if (json.errorExist != null && json.errorExist.length > 0) {
            MyAlert(json.errorExist);
        } else if (json.success != null && json.success == "true") {
        	MyAlert("保存成功!"); 
        	__extQuery__(json.curPage);
        } else {
            MyAlert("保存失败，请联系管理员!");
        }
    }	

    
  //生成下拉框
    function getPackState(value, meta, record) {
        var str = genSelBoxStrExp("PACK_STATE" + record.data.PART_ID, <%=Constant.PART_PACK_STATE%>, value, false, "", "", false, "");
        return str;
    }

	//是否计划
	function planCheckBox(value,meta,record)
	{
		var partID = record.data.PART_ID;
		var checkedValue = <%=Constant.IF_TYPE_YES%>;
		if(checkedValue == value)
		{
			return String.format("<input type='checkbox' id='planCheckBox_"+partID+"'  value=\""+value+"\" onclick = 'setValue()' checked />");
		}
		else
			return String.format("<input type='checkbox' id='planCheckBox_"+partID+"'  value=\""+value+"\" onclick = 'setValue()' />");
	}

	//是否紧缺件
	function lackCheckBox(value,meta,record)
	{
		var partID = record.data.PART_ID;
		var checkedValue = <%=Constant.IF_TYPE_YES%>;
		if(checkedValue == value)
		{
			return String.format("<input type='checkbox' id='lackCheckBox_"+partID+"' value=\""+value+"\" onclick = 'setValue()' checked />");
		}
		else
			return String.format("<input type='checkbox' id='lackCheckBox_"+partID+"' value=\""+value+"\" onclick = 'setValue()' />");
	}

	//是否流失关注件
	function ofFlagCheckBox(value,meta,record)
	{
		var partID = record.data.PART_ID;
		var checkedValue = <%=Constant.IF_TYPE_YES%>;
		if(checkedValue == value)
		{
			return String.format("<input type='checkbox' id='ofFlag_"+partID+"' value=\""+value+"\" onclick = 'setValue()' checked />");
		}
		else
			return String.format("<input type='checkbox' id='ofFlag_"+partID+"' value=\""+value+"\" onclick = 'setValue()' />");
	}

	//add by zhumingwei 2013-09-16
	//是否特殊配件
	function isSpecialCheckBox(value,meta,record){
		var partID = record.data.PART_ID;
		var checkedValue = <%=Constant.IF_TYPE_YES%>;
		if(checkedValue == value){
			return String.format("<input type='checkbox' id='isSpecialCheckBox_"+partID+"' value=\""+value+"\" onclick = 'setValue()' checked />");
		}
		else
			return String.format("<input type='checkbox' id='isSpecialCheckBox_"+partID+"' value=\""+value+"\" onclick = 'setValue()' />");
	}
	//add by zhumingwei 2013-09-16

	
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

    //是否直发
    function directCheckBox(value,meta,record){
        var partID = record.data.PART_ID;
        var checkedValue = <%=Constant.IF_TYPE_YES%>;
        if(checkedValue == value){
            return String.format("<input type='checkbox' id='isDirectCheckBox_"+partID+"' value=\""+value+"\" onclick = 'setValue()' checked />");
        }
        else{
            return String.format("<input type='checkbox' id='isDirectCheckBox_"+partID+"' value=\""+value+"\" onclick = 'setValue()' />");
        }

    }

    function gxCheckBox(value,meta,record){
		var partID = record.data.PART_ID;
		var checkedValue = <%=Constant.IF_TYPE_YES%>;
		if(checkedValue == value){
			return String.format("<input type='checkbox' id='isGxpCheckBox_"+partID+"' value=\""+value+"\" onclick = 'setValue()' checked />");
		}
		else
			return String.format("<input type='checkbox' id='isGxpCheckBox_"+partID+"' value=\""+value+"\" onclick = 'setValue()' />");
	}
  </script>
<!--页面列表 end -->
</body>
</html>