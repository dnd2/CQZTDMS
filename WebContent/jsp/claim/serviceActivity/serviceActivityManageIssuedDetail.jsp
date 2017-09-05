<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.bean.TtAsActivityBean"%>
<%@page import="java.util.List"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动管理</title>
<% String contextPath = request.getContextPath(); %>
<% 
   TtAsActivityBean ActivityBean =(TtAsActivityBean)request.getAttribute("ActivityBean");//服务活动信息-明细
   List<TtAsActivityBean> ActivityBeanList=(List<TtAsActivityBean>)request.getAttribute("ActivityBeanList");//活动工时
   List<TtAsActivityBean> ActivityPartsList=(List<TtAsActivityBean>)request.getAttribute("ActivityPartsList");//活动配件
   List<TtAsActivityBean> ActivityNetItemList=(List<TtAsActivityBean>)request.getAttribute("ActivityNetItemList");//活动其它项目
   List<TtAsActivityBean> ActivityVhclMaterialGroupList=(List<TtAsActivityBean>)request.getAttribute("ActivityVhclMaterialGroupList");//活动其它项目
   List<TtAsActivityBean> ActivitygetActivityAgeList=(List<TtAsActivityBean>)request.getAttribute("ActivitygetActivityAgeList");//车龄定义列表
%>
<%
List<TtAsActivityBean> ActivityCharactorList=(List<TtAsActivityBean>)request.getAttribute("ActivityCharactorList");//车辆性质
%>
<script type="text/javascript">
//服务活动-车辆信息
function openCharactor(){
	OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/getActivityVehicleListInfo.do?activityId='+<%=ActivityBean.getActivityId()%>,800,500);
}
//索赔选择
function fix(show){
    if(document.fm.isFixfee.checked==true){
     show.style.display ="block";
     }else{
     show.style.display ="none";
     }
}
</script>
</head>

<body onLoad="fix(show)">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理理&gt;服务车辆信息及状态查询
	</div>
<form method="post" name="fm">
<table width="95%" border="0"  class="table_edit">
    <tr>
      <td width="1%">&nbsp;</td>
      <td width="98%"></td>
      <td width="1%">&nbsp;</td>
    </tr>
    <tr>
      <td align="center" bgcolor="#EFEFEF">
        <table width="95%" align=center>
          <tr>
            <td align="right">活动编号：</td>
            <td><%=ActivityBean.getActivityCode()%></td> 
            <td align="right">活动名称：</td>
            <td><%=ActivityBean.getActivityName()%></td>             
          </tr>
          <tr>
            <td align="right">活动类型：</td>
            <td>
	             <script type='text/javascript'>
			       var activityType=getItemValue('<%=ActivityBean.getActivityType()%>');
			       document.write(activityType) ;
			     </script>  
            </td>
            <td align="right">处理方式：</td>
            <td>
	             <script type='text/javascript'>
			       var dealwith=getItemValue('<%=ActivityBean.getDealwith()%>');
			       document.write(dealwith) ;
			     </script> 
            </td>	         
          </tr>                   
          <tr>
            <td align="right">活动开始日期： </td>
            <td nowrap>
            	<%=ActivityBean.getStartdate()%>
           	</td>
            <td align="right">活动结束日期：</td>
            <td nowrap>
            	<%=ActivityBean.getEnddate()%>
 			</td>
          </tr>
          <tr>
            <td align="right">活动类别：</td>
            <td>
	             <script type='text/javascript'>
			       var activityKind=getItemValue('<%=ActivityBean.getActivityKind()%>');
			       document.write(activityKind) ;
			     </script> 
            </td>
            <td align="right">距活动结束日期几天上传总结：</td>
            <td>
            	<%=ActivityBean.getUploadPrePeriod()%>
            </td>	         
          </tr>
          <tr id="show">
            <td align="right">配件费用：</td>
            <td>
            	<%=ActivityBean.getPartFee()%>
            </td>
            <td align="right">工时费用：</td>
            <td>
            	<%=ActivityBean.getWorktimeFee()%>
            </td>	         
          </tr> 
          <tr>
          <td colspan="2" align="center">索赔：</td>
            <td><input type="checkBox" name="isClaim" value="<%=ActivityBean.getIsClaim()%>"
            <%if("0".equals(ActivityBean.getIsClaim())) {%>checked="checked"<%}%> disabled="disabled"
            />
            </td>
            <td colspan="2" align="center">索赔是否为固定费用：</td>
            <td><input type="checkBox" name="isFixfee"  disabled="disabled"  value="<%=ActivityBean.getIsFixfee()%>"
            <%if("1".equals(ActivityBean.getIsFixfee())) {%>checked="checked" <%}%>
            />
	            <script type="text/javascript">
		        		if(document.fm.isFixfee.checked==true){
		        			document.fm.isClaim.checked=true;
		        			show.style.display ="block";
		        		}else {
		        		 	show.style.display ="none";
		        		}
	            </script>
            </td>         
          </tr> 
          <tr>
		<td class="table_edit_2Col_label_7Letter">解决方案说明：</td>
		<td colspan="3" align="left">
			<textarea name="solution" id="solution" class="SearchInput" rows="8" cols="85" datatype="0,is_digit_letter_cn,200"><%=ActivityBean.getSolution()==null?"":ActivityBean.getSolution()%></textarea>
		</td>
	</tr>
	<tr>
		<td class="table_edit_2Col_label_7Letter">索赔申请指导：</td>
		<td colspan="3" align="left">
			<textarea name="claimGuide" id="claimGuide" class="SearchInput" rows="8" cols="85" datatype="0,is_digit_letter_cn,200"><%=ActivityBean.getClaimGuide()==null?"":ActivityBean.getClaimGuide()%></textarea>
		</td>
	</tr>
  </table>
  <br/>
  <table width="95%" border="0" class="table_list" style="border-bottom:1px solid #DAE0EE">
  	  <tr>
		  <th colspan="3" align="left">
			  <img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 活动工时
		  </th>
	  </tr>
      <tr>
	      <th><b>工时代码</b></th>
	      <th><b>工时名称</b></th>
	      <th><b>工时数</b></th>
	  </tr>
	 <c:forEach var="ActivityBeanList" items="${ActivityBeanList}">
		         <tr class="table_list_row1">
				      <td><c:out value="${ActivityBeanList.itemCode}"></c:out></td>
				      <td><c:out value="${ActivityBeanList.itemName}"></c:out></td>
				      <td><c:out value="${ActivityBeanList.normalLabor}"></c:out></td>        
	             </tr>
	 </c:forEach>
  </table>
  <br/>
  <table width="95%" border="0" class="table_list" style="border-bottom:1px solid #DAE0EE">
      <tr>
		 <th colspan="2" align="left">
		 	<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 活动配件
		 </th>
	 </tr>
      <tr>
	      <th><b>配件代码</b></th>
	      <th><b>配件名称</b></th>
	  </tr>
	   <c:forEach var="ActivityPartsList" items="${ActivityPartsList}">
			<tr class="table_list_row1">
				  <td><c:out value="${ActivityPartsList.partNo}"></c:out></td>
				  <td><c:out value="${ActivityPartsList.partName}"></c:out></td>
			</tr>
      </c:forEach>
  </table>
   <br/>
  <table width="95%" border="0" class="table_list" style="border-bottom:1px solid #DAE0EE">
  		<tr>
		  <th colspan="2" align="left">
		 	 <img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 活动其它项目
		  </th>
	   </tr>
      <tr>
          <th><b>其他项目代码</b></th>
	      <th><b>其他项目名称</b></th>
	  </tr>
	      <c:forEach var="ActivityNetItemList" items="${ActivityNetItemList}">
				<tr class="table_list_row1">
					  <td><c:out value="${ActivityNetItemList.itemCodes}"></c:out></td>
					  <td><c:out value="${ActivityNetItemList.itemDesc}"></c:out></td>
				</tr>
	      </c:forEach>
  </table>
  <br/>
  <table width="95%" border="0" class="table_list" style="border-bottom:1px solid #DAE0EE">
	   <tr>
		  <th colspan="2" align="left">
		 	 <img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 车型列表
		  </th>
	   </tr>
      <tr>
	      <th><b>车型代码</b></th>
	      <th><b>车型名称</b></th>
	  </tr>
	      <c:forEach var="ActivityVhclMaterialGroupList" items="${ActivityVhclMaterialGroupList}">
				<tr class="table_list_row1">
					  <td><c:out value="${ActivityVhclMaterialGroupList.groupCode}"></c:out></td>
					  <td><c:out value="${ActivityVhclMaterialGroupList.groupName}"></c:out></td>
				</tr>
	      </c:forEach>
  </table>
  <br/>
  <table width="95%" border="0" class="table_list" style="border-bottom:1px solid #DAE0EE">
      <tr>
		   <th colspan="6" align="left">
		   <img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 车龄定义列表
		   </th>
	   </tr>
      <tr>
	      <th><b>起始日期</b></th>
	      <th><b>截止日期</b></th>
	  </tr>
	     <c:forEach var="ActivitygetActivityAgeList" items="${ActivitygetActivityAgeList}">
			<tr class="table_list_row1">
			    <td><c:out value="${ActivitygetActivityAgeList.saleDateStart}"></c:out></td>
			  <td><c:out value="${ActivitygetActivityAgeList.saleDateEnd}"></c:out></td>
			</tr>
	      </c:forEach>
  </table>
  <br/>
  <table width=95% border=0 class="table_list" style="border-bottom:1px solid #DAE0EE">
     <tr>
	     <th colspan="3" align="left">
	    	 <img class="nav" src="<%=contextPath%>/img/subNav.gif" > 车辆性质
	     </th>
     </tr>
	  	  <c:forEach var="ActivityCharactorList" items="${ActivityCharactorList}">
					<tr class="table_list_row1">
						  <td>
						  	<c:out value="${ActivityCharactorList.codeDesc}"></c:out>
						  </td>
					</tr>
	      </c:forEach>
  </table>
  <br/>
  <table width="95%" border="0" align="center" cellpadding="4" cellSpacing="1">
	  <tr>
	  		<td>
		  		<input type="button" name="vehicleInfo" value="车辆信息" class="normal_btn" onclick="openCharactor();"/>
		  		<input type="button" name="bt_back" class="normal_btn" onclick="javascript:history.go(-1)" value="返回">
	  		</td>
	  </tr>
  </table>
  </td>
  </tr>
  </table>
</form>
</body>
</html>
