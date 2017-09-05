<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>经销商客户关怀活动总结汇总表</TITLE>
<% 
	String contextPath = request.getContextPath();
   List<Map<String, Object>> list = (List<Map<String, Object>>)request.getAttribute("list");
   String serviceactivityType=(String)request.getAttribute("serviceactivityType");
%>
<SCRIPT LANGUAGE="JavaScript">

		      
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;经销商客户关怀活动总结汇总表</div>
  <form method="post" name = "fm" id="fm">
	
    <TABLE align=center width="95%" class="table_query" >
        <tr>
    	     <td class="table_query_2Col_label_6Letter">活动类型：</td>
           <td >
			  <script type="text/javascript">
	              genSelBoxExp("serviceactivity_type",<%=Constant.SERVICEACTIVITY_TYPE%>,"<%=serviceactivityType%>",true,"short_sel","","false",'<%=Constant.SERVICEACTIVITY_TYPE_01%>');
	       </script>
           </td>
             <td  align="right">主题编号：</td>
             <td>  <input name="ButieBh" id="ButieBh" value="${ButieBh}" type="text" class="middle_txt" />
      
			</td>
          </tr>
          <tr>
			<td wclass="table_query_2Col_label_6Letter"idth="15%" align="right">大区：</td>
				<td >
					<select class="short_sel" id="big_org" name="big_org"  >
						<option value="">--请选择--</option>
						<%for(int i=0;i<list.size();i++){%>
							<option <% 
							if(list.get(i).get("ROOT_ORG_ID").toString().equals(request.getAttribute("bigorgId").toString()))
							{

								out.print("value='"+list.get(i).get("ROOT_ORG_ID")+"'");
								out.print(" selected='selected'");
								}else{ 
									out.print("value='"+list.get(i).get("ROOT_ORG_ID")+"'");
									}  %>><%=list.get(i).get("ROOT_ORG_NAME") %></option>
						<%} %>
					</select>
				</td>
             <td  align="right">主题名称：</td>
             <td>  <input name="ButieName" id="ButieName" value="${ButieName}" type="text"  class="middle_txt" />
			</td>
			</tr>
			<tr>
		 <td  class="table_query_3Col_label_5Letter">服务商代码：</td>
			<td align="left">
				<input type="text" name="dealer_code" id="dealer_code" readonly="readonly" value="${dealerCode}" class="long_txt"/>
				<input type="hidden" name="dealer_id" id="dealer_id"/>
				<input type="button" class="mini_btn" value="..." onclick="showOrgDealer('dealer_code','dealer_id',true,'',true,'','10771002');"/>
            	<input type="button" class="normal_btn" value="清除" onclick="clrTxt('dealer_id','dealer_code')"/>
			</td>
			<tr>
            <td colspan="6" align="center" nowrap><input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="查询"  onclick="chaxun(1);" />
            <input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="导出excel"  onclick="goImport();" />
            </td>
          </tr>
  </table>
  <div style="overflow: auto;height: 355px;" >
  <table style="BORDER-BOTTOM: #dae0ee 1px solid; " id="mysehtavlee"    class="table_list"  >
        <tr class="table_list_th" id="mysehtavleeid" >
        <th class="noSort"  rowspan="2" >大区</th>
        <th class="noSort"  rowspan="2">服务商代码</th>
        <th class="noSort"  rowspan="2">服务商名称</th>
        <th class="noSort"  rowspan="2">主题编号</th>
        <th class="noSort"  rowspan="2">活动主题</th>
        <th class="noSort"  rowspan="2">活动总结</th>
        <th class="noSort"  rowspan="2">活动改进及今后建议</th>
        <th class="noSort" colspan="4" >进站量（台）</th>
        <th class="noSort" colspan="4" >老客户返厂量（台）</th>
        <th class="noSort" colspan="4" >客单价（元)</th>
        <th class="noSort" colspan="4" >营业额（元）</th>
        </tr>
        <tr class="table_list_th" id="mysehtavleeid1">

            <th class="noSort"  >提升目标数值</th>
            <th class="noSort"  >活动前平均数据</th>
            <th class="noSort"  >活动期间数据</th>
            <th class="noSort"  >增长%</th>
            <th class="noSort"  >提升目标数值</th>
            <th class="noSort"  >活动前平均数据</th>
            <th class="noSort"  >活动期间数据</th>
            <th class="noSort"  >增长%</th>
            <th class="noSort"  >提升目标数值</th>
            <th class="noSort"  >活动前平均数据</th>
            <th class="noSort"  >活动期间数据</th>
            <th class="noSort"  >增长%</th>
            <th class="noSort"  >提升目标数值</th>
            <th class="noSort"  >活动前平均数据</th>
            <th class="noSort"  >活动期间数据</th>
            <th class="noSort"  >增长%</th>
            </tr>
   <%   
   List<Map<String, Object>> result = (List<Map<String, Object>>)request.getAttribute("result");
   	 if(result.size() ==0){
   		out.println("<div class='pageTips' style='margin-top: 30px'>没有满足条件的数据<script type='text/javascript'>function yingc(){document.getElementById('mysehtavleeid').style.display='none';document.getElementById('mysehtavleeid1').style.display='none';} yingc();</script></div>");
   	 }else{
          for(int i=0;i<result.size();i++){
		   Map<String, Object> map= (Map<String, Object>)result.get(i);
				out.print("<tr style='BACKGROUND-COLOR: #fdfdfd' class='table_list_row1' >");
                out.print(" <td>"+map.get("ROOT_ORG_NAME")+" </td>");  
                out.print(" <td>"+map.get("DEALER_CODE")+" </td>");  
                out.print(" <td>"+map.get("DEALER_NAME")+" </td>");  
                out.print(" <td>"+map.get("SUBJECT_NO")+" </td>");  
                out.print(" <td>"+map.get("SUBJECT_NAME")+" </td>");  
                out.print(" <td>"+map.get("EVALUATE")+" </td>");  
                out.print(" <td>"+map.get("MEASURES")+" </td>");
                
                out.print(" <td>"+map.get("PULL_IN_NUM")+" </td>");  
                out.print(" <td>"+map.get("PULL_IN_MEAN")+" </td>");  
                out.print(" <td>"+map.get("PULL_IN_REGION")+" </td>");  
                out.print(" <td>"+map.get("PULL_IN_INCRE")+" </td>");
                
                out.print(" <td>"+map.get("CUSTOMER_NUM")+" </td>");  
                out.print(" <td>"+map.get("CUSTOMER_MEAN")+" </td>");  
                out.print(" <td>"+map.get("CUSTOMER_REGION")+" </td>");  
                out.print(" <td>"+map.get("CUSTOMER_INCRE")+" </td>");
                
                out.print(" <td>"+map.get("PRICE_NUM")+" </td>");  
                out.print(" <td>"+map.get("PRICE_MEAN")+" </td>");  
                out.print(" <td>"+map.get("PRICE_REGION")+" </td>");  
                out.print(" <td>"+map.get("PRICE_INCRE")+" </td>");
                
                out.print(" <td>"+map.get("OPEN_NUM")+" </td>");  
                out.print(" <td>"+map.get("OPEN_MEAN")+" </td>");  
                out.print(" <td>"+map.get("OPEN_REGION")+" </td>");  
                out.print(" <td>"+map.get("OPEN_INCRE")+" </td>");
                out.print(" </tr>");

			
			
          }
		  
          }
		   
	   

       %>
        <tr>
  </table>
</div>
</form>


<br>

<SCRIPT LANGUAGE="JavaScript">
function clrTxt(value1,value2){
	document.getElementById(value1).value = "";
	document.getElementById(value2).value = "";

}
function chaxun(){
    fm.action = '<%=contextPath%>/report/service/CustomerCareActivity/CustomerCareActivityQuery.do';
    fm.submit();	
	
}

function goImport(){
    fm.action = '<%=contextPath%>/report/service/CustomerCareActivity/CustomerCareActivityExport.do';
    fm.submit();
}
</script>

</BODY>
</html>