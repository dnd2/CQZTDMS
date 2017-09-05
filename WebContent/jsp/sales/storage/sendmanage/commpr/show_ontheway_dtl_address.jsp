<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ taglib prefix="c" uri="/jstl/cout" %>
<%@ taglib prefix="fmt" uri="/jstl/fmt" %>

<%String contextPath = request.getContextPath();%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>位置详情</title>
    <script type="text/javascript">
    function doInit(){
		  //初始化时间控件
	}
    function confirmUpdate() {
        var time = document.getElementById("time").value;
    	if(!time){
        	MyAlert("请选择日期!");
        	return;
    	}
    	var address = document.getElementById("address").value;
    	if(!address){
        	MyAlert("请填写位置!");
        	return;
    	}
    	
    	if(confirm("确定添加?")) editSubmitAction();
    }
    function editSubmitAction() {
        makeNomalFormCall('<%=contextPath%>/sales/storage/sendmanage/OnTheWayAction/saveAddAddress.json', showForwordValue, 'fm');
    }
    function showForwordValue() {
    	var ok=json.OK;
    	var billId=json.billId
    	if(ok==1){
        	MyAlert('保存成功!');
        	
        	parent._hide();//先关闭层
        	
        	parent.document.getElementById('inIframe').contentWindow.fromwordInit(billId);//然后跳转父页面的方法
    	}
    }
    
    function goback(){
    	parent._hide();
    }
    </script>
</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：储运管理>发运管理> 在途位置维护 &gt; 位置详情</div>
    <form id="fm" name="fm" method="post">
        <input type="hidden" name="billId" value="${billId}"/>
        <input type="hidden" name="dtl_ids" value="${dtl_ids}"/>
        <table class="table_list">
            <tr class="table_list_row2">
               <td align="center">序号</td>
               <td align="center">时间</td>
               <td align="center">位置</td>
               <td align="center">状态</td>
            </tr>
          <c:forEach items="${list }" var="var" varStatus="vc">
            <tr class="table_list_row${(vc.index+1)%2+1 }">
              <td align="center">${vc.index+1 }</td>
              <td align="center"><fmt:formatDate value="${var.ADDRESS_DATE }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
              <td align="center">${var.ADDRESS }</td>
              <td align="center">${var.IS_SUB_NAME }</td>
            </tr>
          </c:forEach>
        </table>
        <table width="100%" >
            <tr>
                <td class="table_query_4Col_input" style="text-align: center">
                    <input type="button" name="backBtn" id="backBtn" value="关闭" onclick="goback();" class="normal_btn"/>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
