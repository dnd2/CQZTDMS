<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%String contextPath=request.getContextPath();%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="<%=contextPath%>/js/web/jquery-1.8.0.min.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 分配货位 </title>

</head>

<body onload="__extQuery__(1);">
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：配件管理>配件仓储管理>配件接收入库>配件接收入库>分配货位
	</div>

	<form name="fm" method="post" id="fm">
        <input type="hidden" name="distributeId" id="distributeId" value="${distributeId}"/>
	<table  width=100% border="0" cellpadding="1" cellspacing="1" class="table_list">
		<tr><th colspan="10" align="left"><img class="nav" src="<%=contextPath %>/img/subNav.gif" />待分配配件信息</th></tr>
		<tr bgcolor="#FFFFCC">
	     <td align="center">配件编码</td>
	     <td align="center">配件名称</td>
	     <!-- 
	     <td align="center">最小包装量</td>
	      -->
	     <td align="center">U9入库数量</td>
	     <td align="center">已入库数量</td>
	     <td align="center">待入库数量</td>
	     <td align="center">货位入库数量<font color="RED">*</font></td>
	     <td align="center">入库仓库<font color="RED">*</td>
	     <td align="center">入库货位<font color="RED">*</font></td>
	     <td align="center">备注</td>
	    </tr> 
	    <tr class="table_list_row1">
	     <td align="center">${map.PART_CODE }
			<input type="hidden" name="DISTRIBUTE_ID" value="${map.DISTRIBUTE_ID }"/>
			<input type="hidden" name="PART_ID" value="${map.PART_ID }"/>	
			<input type="hidden" name="PART_CODE" value="${map.PART_CODE }"/>
	     </td>
	     <td align="center">${map.PART_NAME }<input type="hidden" name="PART_NAME" value="${map.PART_NAME }"/></td>
	     <!-- 
	     <td>${map.OEM_MIN_PKG }<input type="hidden" name="OEM_MIN_PKG" value="${map.OEM_MIN_PKG }"/></td>
	      -->
	     <td>${map.ERP_STORAGE_NUM }<input type="hidden" name="ERP_STORAGE_NUM" value="${map.ERP_STORAGE_NUM }"/></td>
	     <td>${map.FINISH_STORAGE_NUM }<input type="hidden" name="FINISH_STORAGE_NUM" value="${map.FINISH_STORAGE_NUM }"/></td>
	     <td>${map.WAIT_STORAGE_NUM }<input type="hidden" id="wait_num" name="WAIT_STORAGE_NUM" value="${map.WAIT_STORAGE_NUM }"/></td>
	     <td><input name="PART_NUM" type="text" class="short_time_txt" id="PART_NUM" value="${map.WAIT_STORAGE_NUM }" style="background-color:#FF9"></td>
	     <td>
             <select name="WH_ID" id="WH_ID" class="short_sel">
                 <option value="">-请选择-</option>
                 <c:forEach items="${wareHouseList}" var="wareHouse">
                 	 <c:if test="${wareHouse.WH_ID==map.WH_ID}">
                     	<option selected="selected" value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                     </c:if>
                     <c:if test="${wareHouse.WH_ID!=map.WH_ID}">
                     	<option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                     </c:if>
                 </c:forEach>
             </select>
         </td>
	     <td>
   		 	<input name ="LOC_CODE_T" id="LOC_CODE_T" class="middle_txt" type="text" onchange="">
   		 	<input name="LOC_CODE" id="LOC_CODE" type="hidden" value="">
            <input class='mini_btn' type='button' value='...' onclick="codeChoice();"/>
	        <!-- 
	     	<c:if test="${LOCID==null}">
    		</c:if>
            <c:forEach items="${locationList}" var="obj">
            	<c:if test="${LOCID==obj.LOC_ID}">
	            	<input id="LOC_CODE_T" class="middle_txt" type="text" value="${obj.LOC_CODE}"/>
	    		 	<input name="LOC_CODE" id="LOC_CODE" type="hidden" value="${obj.LOC_ID},${obj.LOC_CODE},${obj.LOC_NAME}"/>
            	</c:if>
            </c:forEach>
	     	<select name="LOC_CODE" id="LOC_CODE" onclick="codeChoice();"style="width: 100px">
	     		 <c:if test="${locationList==null}">
	     		 	<option value="">--请选择</option>
	     		 </c:if>
	             <c:forEach items="${locationList}" var="obj">
	             	<c:if test="${LOCID==obj.LOC_ID}">
	             	<option selected="selected" value="${obj.LOC_ID},${obj.LOC_CODE},${obj.LOC_NAME}">${obj.LOC_CODE}</option>
	             	</c:if>
	             </c:forEach>
		     </select>
	         -->     
		  </td>
	      <td><input name="REMARKS" class="middle_txt" type="text" value="${map.REMARKS }"></td>
	   </tr>
	</table>
	<br>
	<table width=100% border="0" cellpadding="1" cellspacing="1" >
      <tr align="center">
      	<th  align=center>
			<input type="button" class="normal_btn" id="saveButton" onclick="editSave()"  value="分配"/>&nbsp;&nbsp;
			<input type="button" class="normal_btn" onclick="back();" id="goBack"  value="返回"/>
	   	</th>
	  </tr>
 	</table>
	<br>
	<table  width=100% border="0" cellpadding="1" cellspacing="1" class="table_list">
		<tr><th colspan="6" align="left"><img class="nav" src="<%=contextPath %>/img/subNav.gif" />已分配配件信息</th></tr>
	</table>
	<!-- 查询条件 end -->
	<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
	</form>
</body>
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/getpartList.json?id="+${map.PART_ID};
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "仓库",dataIndex: 'WH_NAME',style: 'text-align: left;'},
				{header: "配件编码",dataIndex: 'PART_CODE',style: 'text-align: left;'},
				{header: "配件名称",dataIndex: 'PART_NAME',style: 'text-align: left;'},
				//{header: "最小包装量",dataIndex: 'OEM_MIN_PKG',style: 'text-align:center;'},
				{header: "数量",dataIndex: 'PART_NUM',style: 'text-align:center;'},
				{header: "货位",dataIndex: 'LOC_CODE',style: 'text-align:center;'},
				{header: "日期",dataIndex: 'STORAGE_DATE',style: 'text-align:center;'},
				{header: "操作",sortable: false, dataIndex: 'DISTRI_LOG_ID', align:'center',renderer:myLink}
		      ];
	function myLink(value,meta,record){
  		return String.format("<a href=\"#\" onclick='sel(\""+value+"\")'>[重新分配]</a>");
	}
	function sel(value){
        MyConfirm("确定要取消?",cancle,[value]);

	}
    function cancle(value){
        makeNomalFormCall("<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/redistributeInit.json?Id="+value,redistributeBack,'fm','saveButton');
    }
	function redistributeBack(json){
		if(json.returnValue == 3){
			MyAlert("当前货位上配件的库存可用数量只有"+json.normalQty+"个,已经不足,无法重新分配！");
		}else if(json.returnValue == 1){
            MyAlert("取消分配成功！");
			window.location.href='<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/distributeInit.do?Id='+${map.DISTRIBUTE_ID};
		}
	}	
	function back(){
		fm.action="<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/init.do";
		fm.submit();
	}
	//添加
	jQuery.noConflict();
	function editSave(){
		var locCode = jQuery("#LOC_CODE_T").val();
		if(locCode==""){MyAlert("请录入货位编码");return;}
		var whId = jQuery("#WH_ID").val();
		var url2 = "<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/checkSeatExist.json";
		var para = "LOC_CODE="+locCode+"&whId="+whId;
		makeCall(url2,forBack2,para);
	}
	function forBack2(json){
		if(json.returnValue == 1){
			if(!submitForm("fm")){
				return;
			}
            codeSet(json.LOC_ID,json.LOC_CODE,json.LOC_CODE);
			var partNum = jQuery("#PART_NUM").val();
			var waitNum = jQuery("#wait_num").val();
			if(Number(partNum)>Number(waitNum)){parent.MyAlert("入库数量大于待入库数量！");return;}
			if(jQuery("#LOC_CODE_T").val()==""){parent.MyAlert("请选择入库货位！");return;}
			if(jQuery("#WH_ID").val()==""){parent.MyAlert("请选择入库仓库！");return;}
			MyConfirm("确认分配！",save);
		}else{
			parent.MyAlert("该货位编码在库房【"+jQuery("#WH_ID").find("option:selected").text()
            +"】不存在，请录入正确的货位信息！");
		}
	}
	function save()
	{
		disabledButton(["saveButton","goBack"],true);
		makeNomalFormCall("<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/editSave.json",saveBack,'fm','saveButton');
	}
	
	function saveBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			window.location.href='<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/distributeInit.do?Id='+${map.DISTRIBUTE_ID};
		}else if(json.returnValue == 2){
			disabledButton(["saveButton","goBack"],false);
			MyAlert("货位分配数量出现异常，请尝试刷新该页面后重新分配！");
		}else{
			disabledButton(["saveButton","goBack"],true);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	function codeChoice(){
        var whId = jQuery("#WH_ID").val();
		OpenHtmlWindow(g_webAppName +"/parts/storageManager/partDistributeMgr/PartDistributeMgr/selectLocationInit.do?whId="+whId,700,400);
	}
	function codeSet(i,c,n){
		var v = i+","+c+","+n;
		jQuery("#LOC_CODE_T").val(c);
		jQuery("#LOC_CODE").val(v);
	}



</script>
</html>
