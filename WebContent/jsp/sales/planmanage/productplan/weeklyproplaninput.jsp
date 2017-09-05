<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contextPath=request.getContextPath();
List dateList=(List)request.getAttribute("dateList");
List dlist=(List)request.getAttribute("dlist");
int intweek=((Integer)request.getAttribute("intweek")).intValue();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 周度滚动计划录入 </title>
<script language="JavaScript" src="<%=contextPath %>/js/ut.js"></script>

<script language="JavaScript">
       function doInit(){
    	   changeFleet($('fm').areaId.value.split(',')[0]) ;
    	   __extQuery__(1);
       }
       
       function isSave(){
      	 MyConfirm("是否确认保存信息?",goSaveRs);
       }
       function goSaveRs(){
       	 var url ="<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/requireForecastSave.do";
       	 $('fm').action=url;
       	 $('fm').submit();
       }
</script>
</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>生产计划>周度滚动计划录入
	</div>
<form name="fm" method="post" id="fm">
<table class="table_query" id="subtab" width="95%" >
  <tr class="csstr">
    <td width="30%"> 请选择业务范围：
      <select name="areaId" id="areaId" onchange="changeFleet(this.value.split(',')[0])">
	       <c:forEach items="${areaBusList}" var="areaBusList" >
	       <c:if test="${myNewAreaId==areaBusList.AREA_ID}">
	       		<option selected="selected" value="${areaBusList.AREA_ID },${areaBusList.DEALER_ID }">${areaBusList.AREA_NAME }</option>
	       		</c:if>
	       	<c:if test="${myNewAreaId!=areaBusList.AREA_ID}">
	       		<option value="${areaBusList.AREA_ID },${areaBusList.DEALER_ID }">${areaBusList.AREA_NAME }</option>
	       	</c:if>
		   </c:forEach>
      </select>
    </td>
    <td width="30%"> 车系：
      <select name="mateGroup" id="mateGroup__">
	       
      </select>
    </td>
    <td align="left" width="16%">
    	<input type="button" id="queryBtn" class="cssbutton"  value="查询" onclick="__extQuery__(1);" />
    </td>
    <td align="right">
    	${curdate }&nbsp;${curWeek }
    </td>
  </tr>  
</table>
<!-- 查询条件 end -->   
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</div>
<p>&nbsp;</p>
</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	var url = "<%=contextPath %>/sales/planmanage/ProductPlan/WeeklyProPlanInput/weeklyProPlanInputSearch.json?myAreaId=${myNewAreaId}";
				
	var title = null;

	var columns = [
				{header: "配置代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "配置名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "月度计划生产总量", dataIndex: 'SUM_AMOUNT', align:'center'},
				{header: "本月已排产量", dataIndex: 'WEEK_AMOUNT', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'GROUP_ID',renderer:myLink ,align:'center'}
		      ];
	//修改和删除的超链接
	function myLink(value,meta,record){
		var sumAmount = record.data.SUM_AMOUNT ;
  		return String.format("<a href='#' onclick='inputPlan("+ value +","+ sumAmount + ")'>[计划录入]</a>");
	}	      
	function inputPlan(groupId,sumAmount){
       	 var url ="<%=contextPath%>/sales/planmanage/ProductPlan/WeeklyProPlanInput/weeklyProPlanInputSearchForSave.do?groupId="+groupId+"&myAreaId="+$('fm').areaId.value+"&sumAmount="+sumAmount;
       	 $('fm').action=url;
       	 $('fm').submit();
	}

	function changeFleet(value) {
		var mateGroupName = "" ;
		var mateGroupId = "" ;
		var i = 0;
		<c:forEach items="${mateGroup__2}" var="mateGroupList" >
			if(${mateGroupList.AREA_ID} == value) {
				mateGroupName =mateGroupName+'${mateGroupList.GROUP_NAME}' + "," ;
				mateGroupId = mateGroupId + '${mateGroupList.GROUP_ID}' + "," ;
				i++ ;
			}
		</c:forEach>
		if (i>0) {
			$('fm').mateGroup__.options.length=i+1;
			
			$('fm').mateGroup__.options[0].value='';
			$('fm').mateGroup__.options[0].text='-请选择-';
			for(var j=0; j<i; j++) {
				$('fm').mateGroup__.options[j+1].value=mateGroupId.split(',')[j];
				$('fm').mateGroup__.options[j+1].text=mateGroupName.split(',')[j];
			}
			var n=1;
			var m=0;
			<c:forEach items="${mateGroup__2}" var="mateGroupList" >
			if(${mateGroupList.AREA_ID} == value) {
				if(${mateGro == mateGroupList.GROUP_ID}) {
					m=n;
				}
				n++ ;
			}
			</c:forEach>
			$('fm').mateGroup__.options[m].selected = true ;
		} else {
			$('fm').mateGroup__.options.length=1;
			$('fm').mateGroup__.options[0].value='';
			$('fm').mateGroup__.options[0].text='-请选择-';
			$('fm').mateGroup__.options[0].selected = true ;
		}
	}
</script>	
</html>
