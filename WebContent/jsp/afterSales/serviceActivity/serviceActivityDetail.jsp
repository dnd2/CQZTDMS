<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>服务活动</title>
<script type="text/javascript">
var myPage;

var url ="<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/getActivityVin.json?activityId=${info.ACTIVITY_ID }";
			
var title = null;
var columns = [
            {header : "序号",align : 'center',renderer : getIndex}, 
			{header: "VIN", dataIndex: 'VIN', align:'center'},
            {header: "活动次数", dataIndex: 'ACTIVITY_NUMBER', align:'center'},
            {header: "已完成次数", dataIndex: 'ACTIVITY_COMPLETED', align:'center'}
	      ];
<%-- function backInit(){
			window.location.href = "<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/serviceActivityPageInit.do";;
		} --%>
	$(document).ready(function(){__extQuery__(1);});
</script>

</head>
<body >
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务&gt;维修管理&gt;服务活动管理查看</div>
<form method="post" name="fm" id="fm">
<input type="hidden" name="id" id="activityId" value="${info.ACTIVITY_ID }"/>
<div class="form-panel">
		<h2><img src="<%=contextPath%>/img/nav.gif"/>服务活动信息</h2>
			<div class="form-body">
<table  class="table_query">
	<tr >
		<td class="right">活动名称：</td>
		<td >
			${info.ACTIVITY_NAME }
		</td>
		
		<td class="right">活动编号：</td>
		<td >
			${info.ACTIVITY_CODE }
		  </td>
		<td class="right">活动日期：</td>
		<td >
		<c:if test="${info.ACTIVITY_STRATE_DATE != null}">
		${info.ACTIVITY_STRATE_DATE }至${info.ACTIVITY_END_DATE }
		</c:if>
		</td>
	</tr>
	<tr >
		<td class="right">里程设置：</td>
		<td >
		<c:if test="${info.ACTIVITY_STRATE_MILEAGE != null}">
			${info.ACTIVITY_STRATE_MILEAGE }至${info.ACTIVITY_END_MILEAGE }
		</c:if>
			
		</td>
		<td class="right">车型：</td>
		<td >
		<span><input type="text"  style='border-left:0px;border-top:0px;border-right:0px;border-bottom:1px;background-color:#F0F7F2;color:#5c7693 ;width:60px;' readonly="readonly" onmouseover="this.title=this.value" value="${modelCode}"/></span>
		</td>
		<td class="right">实销日期：</td>
		<td >
		<c:if test="${info.ACTIVITY_SALES_STRATE_DATE != null}">
			${info.ACTIVITY_SALES_STRATE_DATE }至${info.ACTIVITY_SALES_END_DATE }
		</c:if>
		</td>
	</tr>
	<tr <c:if test="${isflag=='0'}">style="display:none;"</c:if>>
		<td class="right">折扣率：</td>
		<td colspan="4">
			${info.ACTIVITY_DISCOUNT }%
		</td>
	</tr>
	<tr  <c:if test="${info.ACTIVITY_TYPE=='96281001'||info.ACTIVITY_TYPE=='96281003'}">style="display:none;"</c:if>>
		<td class="right">折扣率：</td>
		<td  >
			${info.ACTIVITY_DISCOUNT }%
		</td>
		<td class="right">保养次数：</td>
		<td  >
			${info.MAINTAIN_NUMBER }
		</td>
		<td class="right">保养金额：</td>
		<td  >
			${info.MAINTAIN_MONEY }
		</td>
	</tr>
	<tr <c:if test="${info.ACTIVITY_TYPE=='96281001'||info.ACTIVITY_TYPE=='96281002'}">style="display:none;"</c:if>>
		<td class="right">折扣率：</td>
		<td   >
		${info.ACTIVITY_DISCOUNT }"
		</td>
		</td>
		<td class="right">检测金额：</td>
		<td  >
		${info.DETECTION_MONEY }
		</td>
	</tr>
	</table>
	</div>
	</div>
	<div <c:if test="${isflag=='0'}">style="display:none;"</c:if>>
	<div class="form-panel">
		<h2><img src="<%=contextPath%>/img/nav.gif"/>申请内容</h2>
			<div class="form-body">
	<table class="table_query"  id="REMARKS_ID" > 
    <tr>
					<td class="right" >故障描述：
					</td>
					<td  colspan="2">
						<textarea name='trouble_desc' maxlength="500" 	id='trouble_desc' rows='3' cols='32' disabled="disabled">${info.FAULT_DESCRIPTION }</textarea>
					</td>
					<td class="right" >故障原因：
					</td>
					<td colspan="2" >
						<textarea name='trouble_reason' id='trouble_reason' maxlength="500"  rows='3' cols='32' disabled="disabled" >${info.FAULT_PROBLEM }</textarea>
					</td>
				</tr>
				<tr>
				<td class="right" >维修措施：
					</td>
					<td colspan="2" >
					<textarea name='maintenance_measures' id='maintenance_measures' maxlength="500"  rows='3' cols='32' disabled="disabled">${info.MAINTENANCE_MEASURES }</textarea>
				</td>
				<td></td>
				<td colspan="2"></td>
				</tr>
          </table>
          </div>
          </div>
          </div>
          <div class="form-panel">
		<h2><img src="<%=contextPath%>/img/nav.gif"/>附件信息</h2>
			<div class="form-body">
	<table class="table_list"   id="file">
			<tr >
					<th class="center"> 附件名称 </th>
				    <th class="center"> 操作</th>
				</tr>
				<c:forEach items="${fsList}" var="list" varStatus="st">
			      	<tr  >
		        	<td class="center">
		        	${list.filename }
		        	</td>
		        	<td class="center">
		        	<a href="<%=contextPath%>/util/FileDownLoad/fileDownloadQuery.do?fjid=${list.fjid}" >&nbsp;下载</a>
		        	</td>
	      		</tr>
      </c:forEach>
	</table>
	</div>
	</div>
	<div class="form-panel">
		<h2><img src="<%=contextPath%>/img/nav.gif"/>下发经销商</h2>
			<div class="form-body">
	<table  class="table_list" id="REMARKS_ID" > 
     <tr id="tr" >
        <th class="center" colspan="1" nowrap="nowrap">行号</td>
        <th class="center" colspan="2" nowrap="nowrap">经销商代码</td>
        <th class="center" colspan="3"  nowrap="nowrap">经销商名称</td>
      </tr>
   
      <c:forEach items="${activityDealer }" var="list" varStatus="st">
	      <tr id="tr${list.DEALER_ID }" >
      		<td class="center" colspan="1" nowrap="nowrap" class = "dealerNum">${st.index + 1}</td>
        	<td class="center" colspan="2" nowrap="nowrap">${list.DEALER_CODE }</td>
        	<td class="center" colspan="3" nowrap="nowrap">${list.DEALER_NAME }</td>
        </tr>
      </c:forEach>
     </table>
     </div>
     </div>
     <!--子表信息-->
     <div <c:if test="${isflag=='0'}">style="display:none;"</c:if>>
     <div class="form-panel">
		<h2><img src="<%=contextPath%>/img/nav.gif"/>维修工时</h2>
			<div class="form-body">
		<table id="itemTableId"  class="table_list" >
				<tr class="center" >
					<th> 工时代码 </th>
				    <th> 工时名称</th>
				    <th> 工时数</th>
				</tr>
				<c:forEach items="${activityLabour }" var="list" varStatus="st">
			      	<tr  >
		        	<td class="center" nowrap="nowrap">
		        	${list.HOURS_CODE }
		        	</td>
		        	<td class="center"  nowrap="nowrap">
		        	${list.HOURS_NAME }
		        	</td>
		        	<td class="center"  nowrap="nowrap">
		        	${list.APPLY_HOURS_COUNT }
		        	</td>
	      </tr>
      </c:forEach>
			</table>
			</div>
			</div>
			<div class="form-panel">
		<h2><img src="<%=contextPath%>/img/nav.gif"/>维修配件</h2>
			<div class="form-body">
			<table id="itemTableId"  class="table_list">
				<tr class="center" >
				    <th> 配件代码 </th>
				    <th> 配件名称</th>
				    <th> 数量 </th>
				    <th> 关联工时 </th>
				    <th> 是否主因件 </th>
				    <th> 关联主因件 </th>
				    <td> 配件使用类型 </td>
				</tr>
				<c:forEach items="${activityPart }" var="list" varStatus="st">
			      <tr  >
		        	<td class="center"  nowrap="nowrap">
		       		${list.PART_CODE }
		        	</td>
		        	<td class="center"  nowrap="nowrap">
		        	${list.PART_NAME }
		        	</td>
		        	<td class="center"  nowrap="nowrap">
		        	${list.APPLY_PART_COUNT }
		        	</td>
		        	<td class="center"  nowrap="nowrap">
		        	${list.HOURS_NAME }
		        	</td>
		        	<td class="center"  nowrap="nowrap">
						<c:if test="${list.IS_MAIN==10041001}">
						是
						</c:if>
						<c:if test="${list.IS_MAIN==10041002}">
						否
						</c:if>
		        	</td>
		        	<td class="center"  nowrap="nowrap">
		        	<c:if test="${list.PART_MAIN_ID!=-1}">
						${list.PART_MAIN_NAME }
					</c:if>
		        	</td>
		        	<td class="center"  nowrap="nowrap">
		        	${list.PARTUSER }
		        	</td>
	      </tr>
      </c:forEach>
      </table>
      </div>
      </div>
      </div>
      <div <c:if test="${pst=='0'}">style="display:none;"</c:if>>
      <div class="form-panel">
		<h2><img src="<%=contextPath%>/img/nav.gif"/>VIN信息</h2>
			<div class="form-body">
      <table class="table_query" >
    	<tr >
    	<td class="right">VIN：</td>
		<td >
      		<input name="vin" type="text" id="vin"  class="middle_txt"/>
		</td>
       	<td  >
  	    	<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="u-button u-query" onClick="__extQuery__(1);" >
      	</td>
  	</tr>
	</table>
	</div>
	</div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</div>
	<table class="table_query">
		<tr>
		<td	class="center">
				<input  class="u-button u-cancel" type="button" name="backBtn"  id="backBtn" value="关闭" onclick="_hide() ;"/>
		</td>
		</tr>
	</table>
</form>
</div>
</body>
</html>