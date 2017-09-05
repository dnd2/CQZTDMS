<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%String contextPath = request.getContextPath();%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<TITLE>经销商抽查审核</TITLE>
<SCRIPT LANGUAGE="JavaScript">
var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/application/DealerNewKp/dealerCheckApplicationQuery.json";
				
	var title = null;

	var columns = [
					{header: "经销商代码", width:'15%', dataIndex: 'DEALER_CODE'},
					{header: "经销商名称", width:'15%', dataIndex: 'DEALER_NAME'},
					{header: "抽单编号", width:'15%', dataIndex: 'CHECK_NO'},
					{header: "抽单日期", width:'15%', dataIndex: 'CHECK_DATE',renderer:formatDate},
					{header: "抽查数量", width:'7%', dataIndex: 'CHECK_COUNT'},
					{header: "基地", width:'7%', dataIndex: 'YIELDLY',renderer:getItemValue},
					{header: "开票单号", width:'15%', dataIndex: 'BALANCE_NO'},
					{header: "结算日期起", width:'15%', dataIndex: 'START_DATE',renderer:formatDate},
					{header: "结算日期止", width:'15%', dataIndex: 'END_DATE',renderer:formatDate},
					{header: "金额", width:'15%', dataIndex: 'BALANCE_AMOUNT'},
					{header: "状态", width:'15%', dataIndex: 'SB_STATUS',renderer:getItemValue},
					{width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink1,align:'center'},
					{width:'5%',header: "签收",sortable: false,dataIndex: 'ID',renderer:myLink2,align:'center'}
		      ];
    
	//格式化时间为YYYY-MM-DD
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	
    function myLink1(value,meta,record) {
    	if(record.data.SB_STATUS == '99911002'){
        	return "<a href='#' onclick='getAuth(\""+record.data.ID+"\")'>[审核]</a>|<a href='#' onclick='ztAudit(\""+record.data.ID+"\")'>[逐条审核]</a>";
    	}else{
    		return "<a href='#' onclick='getDetail(\""+record.data.ID+"\")'>[明细]</a>";
    	}
    }
    function myLink2(value,meta,record) {
    	if(record.data.SB_STATUS == '99911002'){
        	return "<a href='#' onclick='getSign(\""+record.data.ID+"\")'>[完成]</a>";
    	}else{
    		return "--";
    	}
    }
    function getAuth(value){
    	location.href='<%=contextPath%>/claim/application/DealerNewKp/dealerCheckAppAuthInit.do?id='+value;
    }
    
    function getSign(value){
    	if(confirm('确认操作?')){
    		var url = '<%=contextPath%>/claim/application/DealerNewKp/dealerCheckAppAuth.json?id='+value;
    		makeNomalFormCall(url,getCallback,'fm');
    	}
    }
    function getCallback(json){
    	if(json.flag){
    		MyAlert('操作已成功!');
    		if (parent.$('inIframe')){
        		parentContainer.refreshWindow();
    	    }else{
        	    parent.refreshWindow();
        	}
        }
    }
    function refreshWindow(){
        __extQuery__(1);
    }

    function getDetail(value){
    	OpenHtmlWindow('<%=contextPath%>/claim/application/DealerNewKp/queryDetail.do?id='+value,900,500);
    	//location.href='<%=contextPath%>/claim/application/DealerNewKp/queryDetail.do?id='+value;
    }
    function clearInput(){
		$('dealerId').value='';
		$('dealerCode').value='';
	}

    //逐条审核
    function ztAudit(value){
	    var frm = document.getElementById("fm");
	    frm.action = "<%=contextPath%>/claim/application/DealerNewKp/ztAudit.do?id="+value;
	    frm.submit();
    }
    
    function doInit(){
	   loadcalendar();
	}
//设置超链接 end
</SCRIPT>
</HEAD>
<BODY>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;经销商抽查打印</div>
    <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_6Letter">经销商代码：</td>
            <td>
            	<input class="middle_txt" disabled="disabled" id="dealerCode"  name="dealerCode" type="text"/>
				<input class="middle_txt" id="dealerId"  name="dealerId" type="hidden"/>
	            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onClick="showOrgDealer('dealerCode','dealerId','false','',true);" value="..." />        
	            <input name="clrBtn" type="button" class="normal_btn" onClick="clearInput();" value="清除"/>  
            </td>
            <td  class="table_query_2Col_label_6Letter">基地：</td>
            <td>
				<script type="text/javascript">
            		genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
		    	</script>
 		    </td> 	
          </tr>
          <tr>
            <td  class="table_query_2Col_label_6Letter">抽单编号：</td>
            <td><input name="CHECK_NO" id="CHECK_NO" value="" type="text" datatype="1,is_digit_letter,25"  class="middle_txt" />
            </td>
            <td  class="table_query_2Col_label_6Letter">状态：</td>
            <td>
				<script type="text/javascript">
	              genSelBoxExp("STATUS",<%=Constant.SB_STATUS%>,"",false,"short_sel","","true",'<%=Constant.SB_STATUS_1%>');
	       		</script>
 		    </td> 	
          </tr>
          <tr>
            <td  class="table_query_2Col_label_6Letter">开票单号：</td>
            <td><input name="BALANCE_NO" id="BALANCE_NO" value="" type="text" datatype="1,is_digit_letter,25"  class="middle_txt" />
            </td>
            <td  class="table_query_2Col_label_6Letter">抽单日期：</td>
            <td>
            	<input type="text" name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             	&nbsp;至&nbsp;
 				<input type="text" name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
            </td> 	
          </tr>
          <tr>
            <td class="table_query_2Col_label_6Letter">结算日期起：</td>
            <td>
            	<input type="text" name="balanceStartDate" id="t3" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4"  hasbtn="true" callFunction="showcalendar(event, 't3', false);"/>
            	&nbsp;结算日期止：&nbsp;
            	<input type="text" name="balanceEndDate" id="t4" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't4', false);"/>
            </td>
            <td></td>
            <td></td> 	
          </tr>
          <tr>
          	<td colspan="4" align="center" nowrap>
          		<input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="__extQuery__(1);" />
          	</td>
          </tr>
  </table>
  
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</BODY>
</html>