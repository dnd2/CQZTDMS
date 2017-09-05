<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
	String curPage = (String)request.getAttribute("curPage");
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
    <title>发运信息补录-修改</title>
</head>
<body>
<div class="wbox">
    <div class="navigation">
    	<img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置：配件管理&gt;总部销售管理&gt;发运信息补录&gt;修改
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <div class="form-panel">
     <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>信息补录</h2>
     <div class="form-body">
        <table class="table_query">
            <input type="hidden" name="TRANS_ID" value="${tranx.transId}">
            <input type="hidden" name="OUT_ID" value="${tranx.outId}">
            <tr>
            	<td class="right">订单单号：</td>
            	<td width="24%">${tranx.orderCode}</td>
            	<td class="right">订单类型：</td>
            	<td width="24%">
            		<script type="text/javascript">
                        genSelBoxExp("orderType", '<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>', "${tranx.orderType}", false, "u-select", 'disabled', "false", '');
                    </script>
            	</td>
            	<td class="right">销售单号：</td>
            	<td width="24%">${tranx.soCode}</td>
            </tr>
            <tr>
            	<td class="right">发运单号：</td>
                <td width="24%">
                	<input type="text" readonly="readonly" name="TRANS_CODE" class="middle_txt" value="${tranx.transCode}">
               	</td>
               	<td class="right">发运方式：</td>
                <td width="24%">
                	<select id="TRANS_TYPE" name="TRANS_TYPE" class="u-select" >
                		<option value="">--请选择--</option>
                			<c:forEach items="${tranTypeList}" var="list">
	                			<c:choose>
								       <c:when test="${list.TV_ID==tranx.transType || list.TV_NAME==tranx.transType}">
								           <option selected="selected" value="${list.TV_ID}">${list.TV_NAME }</option>
								       </c:when>
								       <c:otherwise>
								             <option value="${list.TV_ID}">${list.TV_NAME }</option>
								       </c:otherwise>
								</c:choose>
                			</c:forEach>
                	</select>
               	</td>
               	<td class="right">承运物流：</td>
                <td width="24%">
                	<select id="TRANSPORT_ID" name="TRANSPORT_ID" class="u-select">
                		<option value="">--请选择--</option>
                		<c:forEach items="${wuliuList}" var="list">
                			<c:if test="${tranx.transportOrg==list.LOGI_CODE || tranx.transportOrg==list.LOGI_FULL_NAME}">
                				<option selected="selected" value="${list.LOGI_CODE}">${list.LOGI_FULL_NAME}</option>
                			</c:if>
                			<c:if test="${tranx.transportOrg!=list.LOGI_CODE && tranx.transportOrg!=list.LOGI_FULL_NAME}">
                				<option value="${list.LOGI_CODE}">${list.LOGI_FULL_NAME}</option>
                			</c:if>
                		</c:forEach>
                	</select>
               	</td>
            </tr>
            <tr>
            	<td class="right">物流单号：</td>
                <td width="24%">
                	<input class="middle_txt" type="text" id="WULIU_CODE" name="WULIU_CODE"  value="${tranx.wuliuCode }" maxlength="50"/>
               	</td>
               	<td class="right">运费：</td>
                <td width="24%">
                	<input class="middle_txt" type="text" id="freightAmount" name="freightAmount"  value="${tranx.acAmount }" maxlength="50"/>
                </td>
            </tr>
            <tr>
            	<%-- <td class="right">装箱数量：</td>
                <td width="24%">
                	<input class="middle_txt" type="text" id="transBoxNum" name="transBoxNum"  value="${tranx.transBoxNum }" maxlength="4"/>
               	</td> --%>
            	<td class="right">发运信息补录备注：</td>
                <td width="24%" colspan="3">
                	<input class="middle_txt" type="text" id="transRemark" name="transRemark"  value="${tranx.remark2 }" maxlength="500"/>
               	</td>
            </tr>
            <tr>
                <td colspan="6" class="center">
                	<input class="normal_btn" type="button" value="保存" onclick="saveTransInfoMakeUp()"/>&nbsp;
                    <input class="normal_btn" type="button" value="返回" onclick="goback()"/> 
                </td>
            </tr>
        </table>
	  </div>
	  </div>  
    </form>
    <script type="text/javascript">
    	//返回
    	function goback(){
    		var urlkey = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/toTransInfoMakeUpInit.do?transCode=${transCode}&orderCode=${orderCode}&state=${state}';
    		location = urlkey;
    	}
    	//保存
    	function saveTransInfoMakeUp(){
    		var ys=document.getElementById("freightAmount").value;
    		if ($("#TRANS_TYPE")[0].value == "") {
                MyAlert("发运方式不能为空，请选择发运方式!");
                return;
            }
            if ($("#TRANSPORT_ID")[0].value == "") {
                MyAlert("承运物流不能为空，请选择承运物流!");
                return;
            }
            var reg = /^\d+(\.\d{1,2})?$/;
            if (!reg.test(ys)) {
                  MyAlert("发运金额金额不能为空，金额只能输入整数或两位小数!");
                  return;
            }
    		MyConfirm('确定保存？',function(){
    			var urlkey = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/saveTransInfoMakeUp.json';
    			makeNomalFormCall(urlkey,getResult,'fm');
            });
    	}
    	
    	//操作结果
        function getResult(json){
        	var success = json.success;
        	if(success!='' && success!=null && success!='null' && success!='undefined'){
        		MyAlert(success);
        		goback();
        	}else{
        		MyAlert(json.error);
        	}
        }
    </script>
</div>
</body>
</html>