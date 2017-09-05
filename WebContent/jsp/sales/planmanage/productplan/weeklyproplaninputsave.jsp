<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.po.TmDateSetPO"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contextPath=request.getContextPath();
	List dlist=(List)request.getAttribute("dlist");
	String n7Date=(String)request.getAttribute("n7Date");
	List list=(List)request.getAttribute("list");
	int intweek=0;
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 周度滚动计划录入 </title>
<script language="JavaScript" src="<%=contextPath %>/js/ut.js"></script>

<script language="JavaScript">
       function doInit(){
       }
       
       function isSave(){
       	  if(submitForm('fm')){
      	 	MyConfirm("是否确认保存信息?",goSaveRs);
      	  }
       }
       function goSaveRs(){
       	 var url ="<%=contextPath%>/sales/planmanage/ProductPlan/WeeklyProPlanInput/weeklyProPlanInputSave.do";
       	 $('fm').action=url;
       	 $('fm').submit();
       	$('fm').disabled = true ;
       	$('fm').btn__.disabled = true ;
       }
</script>
</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>生产计划>周度滚动计划录入
	</div>
<table class="table_list" id="subtab" >
  <tr class="csstr">
    <td align="left">
    	${curdate }&nbsp;${curWeek }
    </td>
    <td>
    	月度计划生产总量:${sumAmount }
    </td>
  </tr>  
</table>
</div>
<%
if(null!=list&&null!=dlist&&list.size()>0&&dlist.size()>0){
%>
<form name="fm" method="post" id="fm">
<input type="hidden" value="${myAreaId}" name="myAreaId"></input>
<input type="hidden" name="groupId" value="${groupId }" />
 <input type="hidden" name="dateSet" value="${dateSet }" />
<table class="table_list" id="infotab" >
	 <tr  class="table_list_th">
	    <th ><div align="center">配置名称</div></th>
	    <th ><div align="center">物料代码</div></th>
        <th ><div align="center">颜色</div></th>
        <th >
            <p align="center">本月</p>
            <p align="center">已安排</p></th>
         <%
     		for(int i=0;i<dlist.size();i++){
     			TmDateSetPO po=(TmDateSetPO)dlist.get(i);
     			String s=po.getSetDate();
     			String d=s.substring(s.length()-4,s.length()-2)+"."+s.substring(s.length()-2,s.length());
     			if(d.equals(n7Date)){
     				intweek=i;
     			}
	     %>
		    <th ><div align="center"><%=d %></div></th>
		 <%
	     	}
		 %>
        <th >
            <p align="center">本周</p>
            <p align="center">已安排</p></th>
        <th ><div align="center">当前库存</div></th>
    </tr>
    <%
       int ids=10000001;
       for(int i=0;i<list.size();i++){
    	   Map map=(Map)list.get(i);
    	   String cls=i%2==0?"2":"1";
    	   int tmp=0;
    %>
    <tr class="table_list_row<%=cls %>">
        <input type="hidden" name="materialId" value="<%=map.get("MATERIAL_ID").toString() %>" />
        <td><%=map.get("GROUP_NAME").toString() %></td>
    	<td><%=map.get("MATERIAL_CODE").toString() %></td>
    	<td><%=map.get("COLOR_NAME").toString() %></td>
    	<td><%=map.get("MONTH_AMT").toString() %></td>
    	<%
    	//周日
    	String inputType="hidden";
        if(tmp>=intweek){
        %>
        	<td>
        		<input type="text" name="<%=map.get("MATERIAL_ID").toString()+"ONE" %>" id="<%=ids %>" value="<%=map.get("ONE_AMT").toString() %>" size="5" datatype="1,is_digit,4"/>
        	</td>
        <%
        ids++;
        tmp++;
     	}else{
	    %>
	    	<td>
	    	   <%=map.get("ONE_AMT").toString() %>
	    	   <input type="hidden" name="<%=map.get("MATERIAL_ID").toString()+"ONE" %>" value="<%=map.get("ONE_AMT").toString() %>" />
	    	</td>
	    <%
	    tmp++;
     	}
	    %>
	    
        <%
      //周一
        if(tmp>=intweek){
        %>
        	<td>
        		<input type="text" name="<%=map.get("MATERIAL_ID").toString()+"TWO" %>" id="<%=ids %>" value="<%=map.get("TWO_AMT").toString() %>" size="5" datatype="1,is_digit,4"/>
        	</td>
        <%
        ids++;
        tmp++;
     	}else{
	    %>
	    	<td>
	    	   <%=map.get("TWO_AMT").toString() %>
	    	   <input type="hidden" name="<%=map.get("MATERIAL_ID").toString()+"TWO" %>"  value="<%=map.get("TWO_AMT").toString() %>" />
	    	</td>
	    <%
	    tmp++;
     	}
	    %>
	    
	    <%
	    //周二
        if(tmp>=intweek){
        %>
        	<td>
        		<input type="text" name="<%=map.get("MATERIAL_ID").toString()+"THREE" %>" id="<%=ids %>" value="<%=map.get("THREE_AMT").toString() %>" size="5" datatype="1,is_digit,4"/>
        	</td>
        <%
        ids++;
        tmp++;
     	}else{
	    %>
	    	<td>
	    	   <%=map.get("THREE_AMT").toString() %>
	    	   <input type="hidden" name="<%=map.get("MATERIAL_ID").toString()+"THREE" %>" value="<%=map.get("THREE_AMT").toString() %>" />
	    	</td>
	    <%
	    tmp++;
     	}
	    %>
	    <%
	    //周三
        if(tmp>=intweek){
        %>
        	<td>
        		<input type="text" name="<%=map.get("MATERIAL_ID").toString()+"FOUR" %>" id="<%=ids %>" value="<%=map.get("FOUR_AMT").toString() %>" size="5" datatype="1,is_digit,4"/>
        	</td>
        <%
        ids++;
        tmp++;
     	}else{
	    %>
	    	<td>
	    	   <%=map.get("FOUR_AMT").toString() %>
	    	   <input type="hidden" name="<%=map.get("MATERIAL_ID").toString()+"FOUR" %>" value="<%=map.get("FOUR_AMT").toString() %>" />
	    	</td>
	    <%
	    tmp++;
     	}
	    %>	    
	   	<%
	    //周四
        if(tmp>=intweek){
        %>
        	<td>
        		<input type="text" name="<%=map.get("MATERIAL_ID").toString()+"FIVE" %>" id="<%=ids %>" value="<%=map.get("FIVE_AMT").toString() %>" size="5" datatype="1,is_digit,4"/>
        	</td>
        <%
        ids++;
        tmp++;
     	}else{
	    %>
	    	<td>
	    	   <%=map.get("FIVE_AMT").toString() %>
	    	   <input type="hidden" name="<%=map.get("MATERIAL_ID").toString()+"FIVE" %>" value="<%=map.get("FIVE_AMT").toString() %>" />
	    	</td>
	    <%
	    tmp++;
     	}
	    %>	     
    	<%
	    //周五
        if(tmp>=intweek){
        %>
        	<td>
        		<input type="text" name="<%=map.get("MATERIAL_ID").toString()+"SIX" %>" id="<%=ids %>" value="<%=map.get("SIX_AMT").toString() %>" size="5" datatype="1,is_digit,4"/>
        	</td>
        <%
        ids++;
        tmp++;
     	}else{
	    %>
	    	<td>
	    	   <%=map.get("SIX_AMT").toString() %>
	    	   <input type="hidden" name="<%=map.get("MATERIAL_ID").toString()+"SIX" %>" value="<%=map.get("SIX_AMT").toString() %>" />
	    	</td>
	    <%
	    tmp++;
     	}
	    %>	    
	    <%
	    //周六
        if(tmp>=intweek){
        %>
        	<td>
        		<input type="text" name="<%=map.get("MATERIAL_ID").toString()+"SEVEN" %>" id="<%=ids %>" value="<%=map.get("SEVEN_AMT").toString() %>" size="5" datatype="1,is_digit,4"/>
        	</td>
        <%
        ids++;
        tmp++;
     	}else{
	    %>
	    	<td>
	    	   <%=map.get("SEVEN_AMT").toString() %>
	    	   <input type="hidden" name="<%=map.get("MATERIAL_ID").toString()+"SEVEN" %>" id="<%=ids %>" value="<%=map.get("SEVEN_AMT").toString() %>" />
	    	</td>
	    <%
	    tmp++;
     	}
	    %>
    	<td><%=map.get("WEEK_AMT").toString() %></td>
    	<td><%=map.get("WAR_AMT").toString() %></td>
    	
    </tr>
    <%
       }
    %>
</table>
<table class=table_queryt>
  <tr>
	  <td align="left">
	  		<input type="hidden" name="mateGro" id="mateGro__" value="${mateGro }" />
	     <input type="button" id="btn__" value="保存" class="cssbutton" onclick="isSave();" />
	  </td>
  </tr>
</table>
</form>
<%
}else{
%>
<table class=table_list>
	<tr>
	   <td align="center">
	   		<font color="red">暂无数据</font>
       </td>
	</tr>
</table>
<%
}
%>
</body>
</html>
