<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <% String contextPath = request.getContextPath(); %>
    <title>领用配件维护</title>
    <script language="javascript" type="text/javascript">
    var plannerArray = new Array(); //创建一个新的计划员数组
    <c:forEach var= "list" items="${plannersList}" varStatus="sta"> //得到有数据的数组集合
    	plannerArray.push(['${list.USER_ID}&&${list.NAME}']);//得到数组的内容（实体bean)加入到新的数组里面
    </c:forEach>
    
    function doInit() {
            //loadcalendar();  //初始化时间控件
            __extQuery__(1);
        }
    </script>
</head>
<body>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
	<input type="hidden" name="updPlanQty" id="updPlanQty" value="" >
	<input type="hidden" name="updRoom" id="updRoom" value="" >
	<input type="hidden" name="updWhman" id="updWhman" value="" >
	<input type="hidden" name="updPId" id="updPId" value="" >
    <div class="wbox">
        <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：
            基础数据管理 &gt; 计划相关参数维护 &gt; 领用配件维护
        </div>
        <table class="table_query">
            <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="10%" align="right">配件编码：</td>
                <td width="20%"><input class="middle_txt" type="text" maxlength="20"  name="partOldcode" id="partOldcode"/></td>
                <td width="10%" align="right">配件名称：</td>
                <td width="20%"><input class="middle_txt" type="text" maxlength="20"  name="partName" id="partName"/></td>
                <td width="10%" align="right">件号：</td>
                <td width="20%"><input class="middle_txt" type="text" maxlength="20"  name="partCode" id="partCode"/></td>
            </tr>
            <tr>
            	<td width="10%" align="right">库房(室)：</td>
                <td width="20%"><input class="middle_txt" type="text" maxlength="20"  name="room" id="room"/></td>
                <td width="10%" align="right">保管员：</td>
                <td width="20%"><input class="middle_txt" type="text" maxlength="20"  name="whMan" id="whMan"/></td>
            	<td width="10%"  align="right">是否有效：</td>
				<td width="20%" >
				<script type="text/javascript">
				   	 genSelBoxExp("STATE",<%=Constant.STATUS%>,",<%=Constant.STATUS_ENABLE%>",true,"short_sel","onchange=__extQuery__(1)","false",'');
				</script>
				</td>
            </tr>
            <tr>
            	<td width="10%" align="right">计划员：</td>
                <td width="20%">
	                <select  name="plannerId" id="plannerId" style="width:150px;" onchange="__extQuery__(1)">
			      		<option value="">-请选择-</option>
			  			<c:if test="${plannersList!=null}">
							<c:forEach items="${plannersList}" var="list">
							  <c:choose> 
								<c:when test="${currUserId eq list.USER_ID}">
								  <option selected="selected" value="${list.USER_ID }">${list.NAME }</option>
								</c:when>
								<c:otherwise>
								  <option value="${list.USER_ID }">${list.NAME }</option>
								</c:otherwise>
							  </c:choose>
							</c:forEach>
						</c:if>
			      	</select>
                </td>
                <td width="10%" align="right"></td>
                <td width="20%"></td>
            	<td width="10%"  align="right"></td>
				<td width="20%" >
				</td>
            </tr>
            <tr>
                <td align="center" colspan="6">
                    <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)"/>
                    <input class="normal_btn" type="button" value="导 出" onclick="exportResAllotExcel()"/>
                    <input class="normal_btn" type="button" value="批量导入" id="upload_button" name="button1"onclick="showUpload();">
                </td>
            </tr>
        </table>
    </div>
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

    <!-- 查询条件 end -->
    <!--分页 begin -->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <!--分页 end -->
</form>
<script type="text/javascript">
    var myPage;

    var url = "<%=contextPath%>/parts/baseManager/partsPlanManager/partRecvSetAction/partRecvSetSearch.json";

    var title = null;

    var columns = [
        {header: "序号", dataIndex: 'PLAN_ID', renderer: getIndex,  style: 'text-align:left'},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'PART_ID', renderer: myLink,  style: 'text-align:left'},
        {header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
        {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
        {header: "件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
        {header: "可用库存", dataIndex: 'NORMAL_QTY'},
        {header: "在途数量", dataIndex: 'ORDER_QTY'},
        {header: "安全库存", dataIndex: 'SAFETY_STOCK'},
        {header: "计划数量", dataIndex: 'PLAN_QTY', renderer: getPlanQtyText},
        {header: "库房(室)", dataIndex: 'ROOM',  style: 'text-align:center', renderer: getRoomText},
        {header: "保管员", dataIndex: 'WHMAN',  style: 'text-align:center', renderer: getKeeperText},
        {header: "计划员", dataIndex: 'P_NAME',style:'text-align:left',renderer:returnPlanerSelect},
        {header: "是否有效", dataIndex: 'STATE',  style: 'text-align:center', renderer: getItemValue}

    ];


    //设置超链接
    function myLink(value, meta, record) {
        var defineId = record.data.DEF_ID;
        var state = record.data.STATE;
        var disableValue = <%=Constant.STATUS_DISABLE%>;
        var enableValue = <%=Constant.STATUS_ENABLE%>;
        if (disableValue == state) {
            return String.format("<a href=\"#\" onclick='enableData(\"" + defineId + "\")'>[有效]</a>");
        } else if (enableValue == state) {
            return String.format("<a href=\"#\" onclick='updateData(\"" + value + "\",\"" + defineId + "\")'>[保存]</a>&nbsp;<a href=\"#\" onclick='cel(\"" + defineId + "\")'>[失效]</a>");
        } else {
            return String.format("<a href=\"#\" onclick='updateData(\"" + value + "\",\"" + defineId + "\")'>[保存]</a>");
        }

//        return String.format("<a href=\"#\" onclick='updateData(\"" + value + "\",\"" + defineId + "\")'>[保存]</a>");

    }

    function getPlanQtyText(value, meta, record) {
    	var partID = record.data.PART_ID;
        return String.format("<input  style='width: 60px' type='text' id='planQty_" + partID + "' value=\"" + value + "\" onchange='dataTypeCheck(this)' />");
    }

    function getRoomText(value, meta, record) {
        var partID = record.data.PART_ID;
        return String.format("<input  style='width: 60px' type='text' id='room_" + partID + "' value=\"" + value + "\" />");
    }

    function getKeeperText(value, meta, record) {
        var partID = record.data.PART_ID;
        return String.format("<input  style='width: 60px' type='text' id='whMan_" + partID + "' value=\"" + value + "\" />");
    }

  //设置计划员下拉框
	function returnPlanerSelect(value,meta,record)
	{
		var pId = record.data.PLANNER_ID;
		var partID = record.data.PART_ID;
		var str = "<select  style='width: 60px' size = '1' id = 'PlanerSelect_"+partID+"' onmouseover='addPlannerList(\"PlanerSelect_"+partID+"\")' onclick='addPlannerList(\"PlanerSelect_"+partID+"\")'><option value='"+pId+"'>"+value+"</option>";
		str = str + "</select>";
		return String.format(str);
	}

	function addPlannerList(parms)
	{
		var obj = document.getElementById(parms);
		if(obj.options.children.length < 2)
		{
			var strTemp;
			var strsTemp= new Array();
			for(var i = 0; i < plannerArray.length; i ++)
			{
				strTemp = plannerArray[i].toString();
				 //定义一数组
				strsTemp = strTemp.split("&&"); //字符分割     
				var uID = strsTemp[0];
				var uName = strsTemp[1];
				if(uID != obj.options.children[0].value)
				{
					obj.options.add(new Option(uName,uID));
				}
			}
		}
		
	}

  	//数据验证
    function dataTypeCheck(obj) {
        var value = obj.value;
        if (isNaN(value)) {
            MyAlert("请输入数字!");
            obj.value = "";
            return;
        }
        var re = /^([0]|[1-9]+[0-9]*)$/;
        if (!re.test(obj.value)) {
            MyAlert("请输入正整数!");
            obj.value = "";
            return;
        }
  	}

    //设置失效：
    function cel(parms) {
        if (confirm("确定失效该数据?")) {
            btnDisable();
            var url = '<%=contextPath%>/parts/baseManager/partsPlanManager/partRecvSetAction/celPartRecvSet.json?disabeParms=' + parms + '&curPage=' + myPage.page;
            makeFormCall(url, showResult, 'fm');
        }
    }

    //设置有效：
    function enableData(parms) {
        if (confirm("确定有效该数据?")) {
            btnDisable();
            var url = '<%=contextPath%>/parts/baseManager/partsPlanManager/partRecvSetAction/enablePartRecvSet.json?enableParms=' + parms + '&curPage=' + myPage.page;
            makeFormCall(url, showResult, 'fm');
        }
    }

    //保存
    function updateData(partId, defineId) {
        var planQty = document.getElementById("planQty_" + partId).value;
        var room = document.getElementById("room_" + partId).value;
        var whMan = document.getElementById("whMan_" + partId).value;
        var planerId = document.getElementById("PlanerSelect_"+partId).value;

        if ("" == planQty) {
            MyAlert("请设置计划数量!");
            return;
        }
        
        if ("" == room) {
            MyAlert("请设置库房（室）!");
            return;
        }

        if ("" == whMan) {
            MyAlert("请设置保管员!");
            return;
        }

//        if ("null" == planerId || "" == planerId) {
//            MyAlert("请设置计划员!");
//            return;
//        }

        if (confirm("确定保存设置?")) {
            btnDisable();
            document.getElementById("updPlanQty").value = planQty;
            document.getElementById("updRoom").value = room;
            document.getElementById("updWhman").value = whMan;
            document.getElementById("updPId").value = planerId;
            var url = '<%=contextPath%>/parts/baseManager/partsPlanManager/partRecvSetAction/updatePartRecvSet.json?defId=' + defineId + '&partId=' + partId + '&curPage=' + myPage.page;
            makeFormCall(url, showResult, 'fm');
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

  	//下载上传模板
	function exportExcelTemplate(){
		fm.action = "<%=contextPath%>/parts/baseManager/partsPlanManager/partRecvSetAction/exportExcelTemplate.do";
		fm.submit();
	}

    //导出
    function exportResAllotExcel() {
        document.fm.action = "<%=contextPath%>/parts/baseManager/partsPlanManager/partRecvSetAction/exportPartRecvSet.do";
        document.fm.target = "_self";
        document.fm.submit();
    }

  	//上传
	function uploadExcel(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/baseManager/partsPlanManager/partRecvSetAction/partRecvUpload.do";
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

    //失效按钮
    function btnDisable() {

        $$('input[type="button"]').each(function (button) {
            button.disabled = true;
        });

    }

    //有效按钮
    function btnEnable() {

        $$('input[type="button"]').each(function (button) {
            button.disabled = "";
        });

    }
</script>
</body>
</html>