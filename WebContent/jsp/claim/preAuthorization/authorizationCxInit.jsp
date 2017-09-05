<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>索赔单预授权查询</TITLE>
<SCRIPT LANGUAGE="JavaScript">
	var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/preAuthorization/Authorization/authorizationQueryCx.json";
				
	var title = null;

	var columns = [
					{header : '序号', sortable : false, renderer : getIndex},
					{header: "预授权申请单号", width:'15%', dataIndex: 'FO_NO'},
					{header: "维修类型", width:'7%', dataIndex: 'APPROVAL_TYPE',renderer:getItemValue},
					{header: "VIN", width:'15%', dataIndex: 'VIN'},
					{header: "进厂里程数", width:'15%', dataIndex: 'IN_MILEAGE'},
					{header: "预警", width:'15%', dataIndex: 'IS_WARING',renderer:getItemValue},
					{header: "授权状态", width:'15%', dataIndex: 'REPORT_STATUS',renderer:getItemValue},
 					{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink1,align:'center'}
		      ];
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}
	
	//修改的超链接设置
	function myLink1(value,meta,record){
	    return String.format(
         "<a href=\"#\" onclick='sel(\""+value+"\",\""+record.data.RO_NO+"\")'>[撤销]</a>");
		
	}
	function sel(val,RO_NO)
	{
	     var url="<%=contextPath%>/claim/preAuthorization/Authorization/authorizationJudeCx.json?ID="+val+"&RO_NO="+RO_NO;
		 makeNomalFormCall(url,authorizationJudeCx,"fm");
	}
	
	function authorizationJudeCx(json)
	{
	  if(json.jued.length > 0)
	  {
	     MyAlert(json.jued);
	  }else
	  {
	      var url="<%=contextPath%>/claim/preAuthorization/Authorization/authorizationCx.json?ID="+json.ID;
		 makeNomalFormCall(url,authorizationCx,"fm");
	  }
	}
	function authorizationCx()
	{  
	   MyAlert('撤销审核成功');
	   __extQuery__(1);
	   
	}
	
	
	function queryPer(){
	var star = $('RO_CREATE_DATE').value;
	var end = $('DELIVERY_DATE').value;
	  if(star==""||end ==""){
	 
	  }else if(star>end){
	  	MyAlert("开始时间不能大于结束时间");
	  	return false;
	  }else {
	   var s1 = star.replace(/-/g, "/");
		var s2 = end.replace(/-/g, "/");
		var d1 = new Date(s1);
		var d2 = new Date(s2);
		var time= d2.getTime() - d1.getTime();
		var days = parseInt(time / (1000 * 60 * 60 * 24));
		
	 	 __extQuery__(1);
	  }
	}
//设置超链接 end
</script>
</HEAD>
<BODY onload="doInit()">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔预授权&gt;索赔单预授权撤销</div>
  <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_7Letter">预授权申请号：</td>
            <td><input name="FO_NO" id="FO_NO" value="" maxlength="20" type="text"  class="middle_txt" />
            <input name="APPLICTION" id="APPLICTION" value="" type="hidden" class="middle_txt" />
            </td>
            <td  class="table_query_2Col_label_7Letter" >VIN：</td>
 			<td align="left">
 			 <input name="VIN" id="VIN" type="text"  value="" class="middle_txt"/>
<!--  			<textarea name="VIN" cols="18" rows="3" datatype="1,is_digit_letter"></textarea> -->
 			</td>
          </tr>
          <tr>
            <td  class="table_query_2Col_label_7Letter">维修类型：</td>
            <td >
            <script type="text/javascript">
	              genSelBoxExp("REPAIR_TYPE",<%=Constant.REPAIR_TYPE%>,"",true,"short_sel","","false",'<%=Constant.REPAIR_TYPE_04%>,<%=Constant.REPAIR_TYPE_05%>,<%=Constant.REPAIR_TYPE_08%>');
	       	</script>
            
 		    </td> 	
 		    <td class="table_edit_2Col_label_7Letter">
										预警：
			</td>          
         		<td>
				 <script type="text/javascript">
			            genSelBoxExp("IS_WARNING",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
			     </script>
			</td>         
          </tr>    
                         
          <tr>
             <td class="table_query_2Col_label_7Letter">申请日期：</td>
             <td align="left" nowrap="true">
			<input name="RO_CREATE_DATE" type="text" class="short_time_txt" id="RO_CREATE_DATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_CREATE_DATE', false);" />  	
             &nbsp;至&nbsp; <input name="DELIVERY_DATE" type="text" class="short_time_txt" id="DELIVERY_DATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'DELIVERY_DATE', false);" /> 
		</td>	
            <td  class="table_query_2Col_label_7Letter">申请单状态：</td>
  			<td >
  			<script type="text/javascript">
	              genSelBoxExp("REPORT_STATUS",<%=Constant.RO_FORE%>,"<%=Constant.RO_FORE_01%>",true,"short_sel","","false",'');
	        </script>
  			</td>
          </tr>
<!--           <tr> -->
<!-- 				<td class="table_edit_2Col_label_7Letter"> -->
<!-- 										预警： -->
<!-- 				</td>           -->
<!--           		<td> -->
<!-- 					 <script type="text/javascript"> -->
<%-- 				            genSelBoxExp("IS_WARNING",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",''); --%>
<!-- 				     </script> -->
<!-- 				</td> -->
<!--           </tr> -->
<!--           <tr style="display: none"> -->
<!--           	<td class="table_query_2Col_label_7Letter"> -->
<!--           		授权状态： -->
<!--           	</td> -->
<!--           	<td > -->
<!--           	  	<script type="text/javascript"> -->
<%-- 	              genSelBoxExp("RO_FORE",<%=Constant.RO_FORE%>,"",true,"short_sel","","false",''); --%>
<!-- 	        </script> -->
<!--           	</td> -->
<!--           </tr> -->
    	  <tr>
            <td colspan="4" align="center" nowrap><input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="queryPer();" />
<!--             <input id="queryBtn" class="normal_btn" type="button" name="button" value="新增"  onClick="claimPreAdd()" /> -->
			</td>
			</tr>
  </table>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<script type="text/javascript">  
	function claimPreAdd(){
		window.location.href = "<%=contextPath%>/claim/preAuthorization/Authorization/authorizationApplyAddInit.do";
	}

	function   showMonthFirstDay()     
	{     
		  var   Nowdate=new   Date();     
		  var   MonthFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth(),1);     
		  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
	}     
	function   showMonthLastDay()     
	{     
		  var   Nowdate=new   Date();     
		  var   MonthNextFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
		  var   MonthLastDay=new   Date(MonthNextFirstDay-86400000);     
		  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
	}  
  $('RO_CREATE_DATE').value=showMonthFirstDay();
  $('DELIVERY_DATE').value=showMonthLastDay();
</script>
</form>
</BODY>
</html>