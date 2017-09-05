<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
    String contextPath = request.getContextPath();
    String orgId = request.getParameter("orgId");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<title>配件货位关系维护修改</title>
<jsp:include page="${path}/common/jsp_head_new.jsp" />

<script type="text/javascript">
function checkData(){
	var locCode=document.getElementById("LOC_CODE");/**货位编码*/
	if(locCode.value==null || locCode.value==""){
		MyAlert("货位编码不能为空！");
		return  false;
	}else{
		var reg = /^[A-Z]+\-([A-Z]{1}[0-9]{1})+\-([0-9]{3})+$/;
 		if (!reg.test(locCode.value)) {
		 	MyAlert("请输入正确的货位编码格式！正确格式：A-A1-001");
			return false;
 		}
	}
	return true;
}
    //提交
    function modLocation() {
        if(checkData()==true){
    		MyConfirm("确认提交修改内容?",fmCommint,[]);
    	}
    }

    function fmCommint(){
    	btnDisable();
        makeNomalFormCall('<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/updatePartLocation.json', showResult, 'fm');
    }
    function showResult(json) {
    	btnEnable();
        if (json.error != null) {
            MyAlert(json.error);
        } else if (json.success == "success") {
            MyAlert("修改成功!");
            location.href = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/partLocationInit.do";
        } else {
            MyAlert("更新操作失败，请联系管理员!");
        }
    }
    //清空
    function clrTxt(txtId) {
        document.getElementById(txtId).value = "";
    }

    //选择配件
    function selPart() {
        OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/selPartInit.do', 700, 400);
    }
    function selWh() {
        OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/selWhInit.do', 700, 400);
    }
    //回调方法 
    function setPartList(id, code, name) {
        document.getElementById("PART_ID").value = id;
        document.getElementById("PART_CODE").value = code;
        document.getElementById("PART_NAME").value = name;
    }
    function setWhList(id, code) {
        document.getElementById("WH_ID").value = id;
        document.getElementById("WH_CODE").value = code;
    }

  	//选择库管员
    function sel() {
        OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/partManagerSelect.do', 700, 400);
    }
    
    function goBack() {
    	btnDisable();
        window.location.href = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/partLocationInit.do";
    }
</script>
</head>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 配件关系货位维护 &gt; 修改
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 配件货位关系
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">配件编码：</td>
							<td>${mapInfo.PART_OLDCODE }</td>
							<td class="right">配件名称：</td>
							<td>${mapInfo.PART_CNAME }</td>
							<td class="right">仓库编码：</td>
							<td>${mapInfo.WH_CODE }</td>
						</tr>
						<tr>
							<td class="right">货位编码：</td>
							<td>
								<input type="text" class="middle_txt" value="${po.locCode }" name="LOC_CODE" id="LOC_CODE" datatype="0,is_null,100" />
							</td>
							<!--      
            <td   align="right">货位名称：</td>
            <td><input type="text" class="middle_txt" value="${po.locName }" name="LOC_NAME" id="LOC_NAME"
                       datatype="0,is_null,100"/></td>  -->
							<td class="right">附属货位：</td>
							<td>
								<input type="text" class="middle_txt" name="SUB_LOC" value="${po.subLoc }" />
								<input type="hidden" name="LOC_ID" value="${po.locId}" id="LOC_ID" />
							</td>
							<td></td>
							<td></td>
							<!-- zhumingwei 2013-09-11 begin -->
							<%--             <c:if test="${salerFlag}" > --%>
							<!--             	<td align="right">库管员：</td> -->
							<!-- 		        <td> -->
							<!-- 			       	<select  name="whManId" id = "whManId" class="short_sel" > -->
							<!-- 			       		<option  value="">--请选择--</option> -->
							<%-- 					   	<c:forEach items="${salerList}" var="saler"> --%>
							<%-- 						  <c:choose>  --%>
							<%-- 							<c:when test="${curUserId eq saler.WHMAN_ID}"> --%>
							<%-- 							  <option selected="selected" value="${saler.WHMAN_ID}">${saler.WHMAN_NAME}</option> --%>
							<%-- 							</c:when> --%>
							<%-- 							<c:otherwise> --%>
							<%-- 							  <option  value="${saler.WHMAN_ID}">${saler.WHMAN_NAME}</option> --%>
							<%-- 							</c:otherwise> --%>
							<%-- 						  </c:choose> --%>
							<%-- 						</c:forEach> --%>
							<!-- 			      	</select> -->
							<!-- 		        </td> -->
							<%-- 		    </c:if> --%>
							<!-- zhumingwei 2013-09-11 end -->
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="modLocation()" class="u-button" />
								<input type="button" name="backBtn" id="backBtn" value="返 回" onclick="goBack()" class="u-button" />
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
	</div>
</body>
</html>
