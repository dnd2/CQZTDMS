<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@page import="java.util.Arrays"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Map" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>经销商维护</title>
<script type="text/javascript">
function bohui(dealer_id) {
	var curPage = document.getElementById("curPage").value;
	 if (confirm("确认要驳回?")){
			parentContainer.bohui(dealer_id,curPage);
			_hide();
	 }
}
</script>
</head>
<body>
<form method="post" name = "fm"  id="fm">
<input type="hidden" name="curPage" id="curPage" value="${curPage}" />
<input id="user_id" name="user_id" type="hidden" value="${user_id}"/>
</form>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;经销商查看</div>

 <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <tr>
			<TH colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 经销商基础信息</TH>
    	  </tr>
    	  <tr>
    	  	<td align="right" width="20%">组织机构代码：</td>
    	  	<td width="30%">
    	  		${dMap.COMPANY_ZC_CODE }
    	  	</td>
            <td align="right" width="20%">经营类型：</td>
             <td align="left" width="30%">
             		${dMap.SHOP_TYPE}
             </td>
          </tr>
		  <tr>
		    <td align="right">经销商公司：</td>
		    <td align="left">
            	${dMap.COMPANY_NAME }
            </td>
		    <td align="right" width="20%">分销网点简称：</td>
		    <td align="left">
		    	${dMap.DEALER_SHORTNAME }
		    </td>
      </tr>
		  <tr>
		    <td align="right" width="20%">分销网点代码：</td>
		    <td align="left">
		    	${dMap.DEALER_CODE }
		    </td>
		    <td align="right" width="20%">分销网点全称：</td>
		    <td align="left">
		    	${dMap.DEALER_NAME }
		    </td>
	      </tr>
	       <tr>
		    <td align="right" width="20%">经销商等级：</td>
		    <td colspan="3" align="left">
                 <script type="text/javascript">
                     document.write(getItemValue("${dMap.DEALER_LEVEL}"));
                 </script>
		    </td>
	      </tr>
	        <tr>
		    <td align="right" width="20%">上级经销商：</td>
		    <td align="left">
			    ${dMap.PARENT_DEALER_NAME }
		    </td>
	      </tr>
           <tr>
                      	 <td align="right">大区：</td>
              <td align="left">
              		${dMap.ROOT_ORG_NAME}
              </td>
              <td align="right">省份：</td>
              <td align="left">
              		${dMap.PROVINCE_ID}
              </td>
             
      		</tr>
           <tr>
            <td align="right">城市：</td>
              <td align="left">
              		${dMap.CITY_ID}
              </td>
          	 <td align="right">区县：</td>
              <td align="left">
              		${dMap.COUNTIES}
              </td>
            
	      </tr><tr>
	       <td align="right">邮编：</td>
             <td align="left">
             	${dMap.ZIP_CODE}
             </td></tr>
	      <tr>
             <td align="right">经销企业注册资金：</td>
	          <td align="left" >
	          	${dMap.REGISTERED_CAPITAL}
	          </td>
	          <td align="right">&nbsp;</td>
	          <td align="left">&nbsp;</td>
      </tr>
      		<tr>
             <td align="right">企业注册地址：</td>
	          <td align="left"  colspan="3">${dMap.ADDRESS}</td>
      		</tr>
      		<tr>
             <td align="right">销售展厅地址：</td>
	          <td align="left" colspan="3">${dMap.COMPANY_ADDRESS}</td>
      		</tr>
           <tr>
             <td align="right">法人：</td>
             <td align="left">${dMap.LEGAL}</td>
             <td align="right">法人手机：</td>
             <td align="left">${dMap.LEGAL_TELPHONE}</td>
           </tr>
           <tr>
             <td align="right">法人办公室电话：</td>
             <td align="left">${dMap.LEGAL_PHONE}</td>
             <td align="right">法人邮箱：</td>
             <td align="left">${dMap.LEGAL_EMAIL}</td>
           </tr>
           <tr>
		    <td align="right">经销商状态：</td>
		    <td align="left" colspan="3">
				<script type="text/javascript">
                    document.write(getItemValue("${dMap.SERVICE_STATUS}"));
                </script>
		    </td>
	      </tr>
	      <tr>
	      	<td align="right">单位性质：</td>
	      	<td>
	      		<script type="text/javascript">
	      			document.write('${dMap.UNION_TYPE }');
	      		</script>
	      	</td>
	      </tr>
	      	<tr class="table_list_row1">
		 <tr>
			<TH colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif">附件</TH>
    	  </tr>
<!-- 		<td colspan="3"> -->
			<c:forEach items="${attachList}" var="attls">
			<tr>
<!-- 			<td>附件：</td> -->
			   <td colspan="4"> <a target="_blank" href="<%=request.getContextPath()%>/util/FileDownLoad/fileDownloadQuery.do?fjid=${attls.FJID}">${attls.FILENAME}</a> </td></tr>
			</c:forEach>
<!-- 		</td> -->
	</tr>
	      <tr id="zcxs">
			<td width="100%" colspan=4>
			<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1">
			<tr>
			<th colSpan=4 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"> <span id="up_name_00">整车销售信息</span></th>
			</tr>
             <tr >
               <td align="right" width="20%">总经理：</td>
               <td align="left" width="30%">${dMap.WEBMASTER_NAME}</td>
               <td width="20%" align="right">总经理办公电话：</td>
               <td align="left" width="30%">${dMap.WEBMASTER_PHONE}</td>
             </tr>
             <tr >
               <td align="right">总经理手机：</td>
               <td align="left">${dMap.WEBMASTER_TELPHONE}</td>
               <td align="right">总经理邮箱：</td>
               <td align="left">${dMap.WEBMASTER_EMAIL}</td>
             </tr>
             <tr >
               <td align="right">销售经理：</td>
               <td align="left">${dMap.MARKET_NAME}</td>
               <td align="right">销售经理办公电话：</td>
               <td align="left">${dMap.MARKET_PHONE}</td>
             </tr>
             <tr >
               <td align="right">销售经理手机：</td>
               <td align="left">${dMap.MARKET_TELPHONE}</td>
               <td align="right">销售经理邮箱：</td>
               <td align="left">${dMap.MARKET_EMAIL}</td>
             </tr>
             <tr >
               <td align="right">市场经理：</td>
               <td align="left">${dMap.MANAGER_NAME}</td>
               <td align="right">市场经理办公电话：</td>
               <td align="left">${dMap.MANAGER_PHONE}</td>
             </tr>
             <tr >
               <td align="right">市场经理手机：</td>
               <td align="left">${dMap.MANAGER_TELPHONE}</td>
               <td align="right">市场经理邮箱：</td>
               <td align="left">${dMap.MANAGER_EMAIL}</td>
             </tr>
             <tr >
               <td align="right">财务经理：</td>
               <td align="left">${dMap.FINANCE_MANAGER_NAME}</td>
               <td align="right">财务经理办公电话：</td>
               <td align="left">${dMap.FINANCE_MANAGER_PHONE}</td>
             </tr>
             <tr >
               <td align="right">财务经理手机：</td>
               <td align="left">${dMap.FINANCE_MANAGER_TELPHONE}</td>
               <td align="right">财务经理邮箱：</td>
               <td align="left">${dMap.FINANCE_MANAGER_EMAIL}</td>
             </tr>
             <tr >
               <td align="right">信息员：</td>
               <td align="left">${dMap.MESSAGER_NAME}</td>
               <td align="right">信息员办公电话：</td>
               <td align="left">${dMap.MESSAGER_PHONE}</td>
             </tr>
             <tr >
               <td align="right">信息员手机：</td>
               <td align="left">${dMap.MESSAGER_TELPHONE}</td>
               <td align="right">信息员QQ：</td>
               <td align="left">${dMap.MESSAGER_QQ}</td>
             </tr>
             <tr >
               <td align="right">信息员邮箱：</td>
               <td align="left">${dMap.MESSAGER_EMAIL}</td>
               <td align="right">销售热线：</td>
               <td align="left">${dMap.HOTLINE}</td>
             </tr>
           <tr >
             <td align="right">是否经营其他品牌：</td>
             <td align="left">
             	<script type="text/javascript">
					document.write(getItemValue("${dMap.IS_ACTING_BRAND}"));
				</script>
             </td>
             <td align="right">代理其它品牌名称：</td>
             <td align="left">
             	${dMap.ACTING_BRAND_NAME}
             </td>
           </tr>
                      <tr >
             <td align="right">是否具备自有服务网点：</td>
             <td align="left">
             	<script type="text/javascript">
					document.write("${dMap.HAVE_SERVICE}");
				</script>
             </td>
             <td align="right">服务站面积：</td>
             <td align="left">
             	${dMap.SERVICE_AREA}
             </td>
           </tr>
                      <tr >
             <td align="right">服务站地址：</td>
             <td align="left">
             	<script type="text/javascript">
					document.write("${dMap.SERVICE_ADDRESS}");
				</script>
             </td>
             <td align="right">24小时服务热线：</td>
             <td align="left">
             	${dMap.SERVICE_HOTLINE}
             </td>
           </tr>
           <tr >
             <td align="right">分销网点邮箱：</td>
             <td align="left">
             	<script type="text/javascript">
					document.write("${dMap.EMAIL}");
				</script>
             </td>
             <td align="right">最低库存：</td>
             <td align="left">
             	${dMap.MIN_STOCK}
             </td>
           </tr>
                                 <tr >
             <td align="right">北汽幻速专营面积：</td>
             <td align="left">
             	<script type="text/javascript">
					document.write("${dMap.OME_AREA}");
				</script>
             </td>
             <td align="right">北汽幻速专营销售人员数：</td>
             <td align="left">
             	${dMap.OME_PEOPLE_TOTAL}
             </td>
           </tr>
                                 <tr >
             <td align="right">全年任务量：</td>
             <td align="left">
             	<script type="text/javascript">
					document.write("${dMap.YEAR_PLAN}");
				</script>
             </td>
             <td align="right">代理区域：</td>
             <td align="left">
             	${proArea}
             </td>
           </tr>
           <tr >
             <td align="right">代理车型：</td>
             <td align="left">
             	<script type="text/javascript">
					document.write("${dMap.PROXY_VEHICLE_TYPE}");
				</script>
             </td>
           </tr>
           <c:if test="${dMap.DEALER_LEVEL == 10851002}">
           <tr>
    	  	<td colspan="4" width="100%">
    	  		<table width=100% border="0" align="center" cellpadding="1" cellspacing="1">
    	  			<tr>
    	  				<th colspan="4" align="left"><img class=nav src="<%=contextPath%>/img/subNav.gif"> <span id="up_name_00">其它信息</span></th>
    	  			</tr>
    	  			<tr>
    	  				<td align="right" width="20%">二级网络性质：</td>
    	  				<td align="left" width="30%">
    	  					${dMap.SECOND_LEVEL_NETWORK_NATURE }
        	  			</td>
    	  				<td align="right" width="20%">竞品品牌：</td>
    	  				<td align="left">${dMap.COMPETING_BRAND }</td>
    	  			</tr>
    	  			<tr>
    	  				<td align="right">与竞品行驶距离（米）：</td>
    	  				<td align="left">
    	  					${dMap.AND_COMPETING_RUN_DISTANCE }
        	  			</td>
    	  				<td align="right">月均销量：</td>
    	  				<td align="left">${dMap.MONTH_AVERAGE_SALES }</td>
    	  			</tr>
    	  			<tr>
    	  				<td align="right">市占率（%）：</td>
    	  				<td align="left">
    	  					${dMap.MARKET_OCCUPANCY }
        	  			</td>
    	  				<td align="right">门头长度：</td>
    	  				<td align="left">${dMap.DOORHEAD_LENGTH }</td>
    	  			</tr>
    	  			<tr>
    	  				<td align="right">是否具有销售门头：</td>
    	  				<td align="left">
    	  					${dMap.IS_HAVE_SALES_DOORHEAD }
        	  			</td>
    	  				<td align="right">是否具有销售形象墙：</td>
    	  				<td align="left">
    	  					${dMap.IS_HAVE_SALES_IMAGE_WALL }
    	  				</td>
    	  			</tr>
    	  			<tr>
    	  				<td align="right">代理区域人口数量：</td>
    	  				<td align="left">
    	  					${dMap.AGENT_ZONE_POPULATION_COUNT }
        	  			</td>
    	  				<td align="right">销售顾问（人员数量）：</td>
    	  				<td align="left">${dMap.SALES_CONSULTANT_COUNT }</td>
    	  			</tr>
    	  			<tr>
    	  				<td align="right">服务网点性质：</td>
    	  				<td align="left">
    	  					${dMap.SERVICE_NETWORK_NATURE }
        	  			</td>
    	  				<td align="right">维修资质：</td>
    	  				<td align="left">
    	  					${dMap.REPAIR_APTITUDE }
    	  				</td>
    	  			</tr>
    	  			<tr>
    	  				<td align="right">服务车间面积：</td>
    	  				<td align="left">
    	  					${dMap.SERVICE_WORKSHOP_AREA }
        	  			</td>
    	  				<td align="right">是否具有服务门头：</td>
    	  				<td align="left">
    	  					${dMap.IS_HAVE_SERVICE_DOORHEAD }
    	  				</td>
    	  			</tr>
    	  			<tr>
    	  				<td align="right">是否具有服务形象墙：</td>
    	  				<td align="left">
    	  					${dMap.IS_HAVE_SERVICE_IMAGE_WALL }
        	  			</td>
    	  				<td align="right">服务离销售网点距离：</td>
    	  				<td align="left">
    	  					${dMap.SERVICE_SALES_NETWORK_DISTANCE}
    	  				</td>
    	  			</tr>
    	  			<tr>
    	  				<td align="right">维修技术师最低配备 （人员数量）：</td>
    	  				<td align="left">
    	  					${dMap.REPAIR_ENGINEER_LOWEST_DEPLOY }
        	  			</td>
    	  				<td align="right">星级申报：</td>
    	  				<td align="left">
    	  					${dMap.LEVEL_REPORT }
    	  				</td>
    	  			</tr>
    	  		</table>
    	  	</td>
    	  </tr>
    	  </c:if>
           <c:if test="${isDealer=='no' }">
			<tr>
				<th colSpan=5 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"> <span id="up_name_00">操作:</span></th>
			</tr>
			<tr>
				<td align="center"><input type="button" value="驳回" name="cancelBtn" class="normal_btn" onclick="bohui('${dMap.DEALER_ID}');" />
				</td>
			</tr>
			</c:if>
				</table>
						<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1">
			<tr>
			<th colSpan=5 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"> <span id="up_name_00">操作流水</span></th>
			</tr>
			<tr>
			<td>序号</td>
			<td>状态</td>
			<td>操作人</td>
			<td>操作时间</td>
			<td>备注</td>
			</tr>
			<c:forEach items="${flowInfo}" varStatus="index" var="flowInfo">
			<tr><td>${index.index+1 }</td>
			<td>${flowInfo.STATUS }</td>
			<td>${flowInfo.USER_ID }</td>
			<td>${flowInfo.CREATE_DATE }</td>
			<td>${flowInfo.REMARK }</td></tr>
			</c:forEach>
			</table>
			</td>

    	  <tr>
    	   <tr id="kpxx" >
			<td width="100%" colspan=4>
</body>
</html>
