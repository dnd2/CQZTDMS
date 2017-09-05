<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%String contextPath = request.getContextPath();%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<TITLE>三包档案抽查管理</TITLE>
<SCRIPT LANGUAGE="JavaScript">
var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/application/DealerNewKp/dealerCheckApplicationQuery1.json";
				
	var title = null;

	var columns = [
					{header: "抽单编号", width:'15%', dataIndex: 'CHECK_NO'},
					{header: "抽单日期", width:'15%', dataIndex: 'CHECK_DATE',renderer:formatDate},
					{header: "抽查数量", width:'7%', dataIndex: 'CHECK_COUNT'},
					{header: "基地", width:'15%', dataIndex: 'YIELDLY',renderer:getItemValue},
					{header: "开票单号", width:'15%', dataIndex: 'BALANCE_NO'},
					{header: "结算时间起", width:'15%', dataIndex: 'START_DATE',renderer:formatDate},
					{header: "结算时间止", width:'15%', dataIndex: 'END_DATE',renderer:formatDate},
					{header: "金额", width:'15%', dataIndex: 'BALANCE_AMOUNT',renderer:amountFormat},
					{header: "状态", width:'15%', dataIndex: 'SB_STATUS',renderer:getItemValue},
					{width:'5%',header: "打印",sortable: false,dataIndex: 'ID',renderer:myLink1,align:'center'}
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
    	var sbStatus = record.data.SB_STATUS;
        if(sbStatus==99911001){
        	return "<a href='#' onclick=\"window.open('<%=contextPath%>/claim/application/DealerNewKp/printForward.do?id="+value+"','','left=0,top=0,width="+screen.availWidth +"- 10,height="+screen.availHeight+"-50,toolbar=no,menubar=no,scrollbars=no,location=no');\">[打印]</a>"+"<a href='#' onclick='isCheck("+record.data.ID+");'>[附件上传]</a>"+"<span id='sbhui'><a href='#' onclick='sb("+record.data.ID+");'>[上报]</a><span>";
        }else{
    		return "<a href='#' onclick=\"window.open('<%=contextPath%>/claim/application/DealerNewKp/printForward.do?id="+value+"','','left=0,top=0,width="+screen.availWidth +"- 10,height="+screen.availHeight+"-50,toolbar=no,menubar=no,scrollbars=no,location=no');\">[打印]</a>";
        }
    }
    function doInit(){
	   loadcalendar();
	}
	function isCheck(value){
		$('ID').value=value;
		makeNomalFormCall("<%=contextPath%>/claim/application/DealerNewKp/check.json",showForwordValue,'fm','queryBtn');
	}
	function showForwordValue(json){
		var ID=json.ID;
		var ok=json.ok;
		if(ok=='ok'){
			window.location.href ="<%=contextPath%>/claim/application/DealerNewKp/upfileDetail.do?id="+ID;
		}else{
			MyAlert("超過開票時間14天的不能上傳附件!");
		}
	}

	function sb(value,sbStatus){
		$('ID').value=value;
		if(confirm('确认上报!')){
			makeNomalFormCall("<%=contextPath%>/claim/application/DealerNewKp/sb.json",showForwordValue1,'fm','queryBtn');
		}
	}
	function showForwordValue1(json){
		var ok=json.ok;
		if(ok=='ok'){
			MyAlert("上报成功!");
			__extQuery__(1);
		}else{
			MyAlert("上报失败!");
			__extQuery__(1);
		}
	}
	
//设置超链接 end
</SCRIPT>
</HEAD>
<BODY>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;三包档案抽查管理</div>
    <form method="post" name ="fm" id="fm">
    <input name="ID" id="ID" value="" type="hidden"/>
    <TABLE  class="table_query">
    	  <tr>
            <td class="table_query_2Col_label_6Letter">开票单号：</td>
            <td><input name="BALANCE_NO" id="BALANCE_NO" value="" type="text" datatype="1,is_digit_letter,25"  class="middle_txt" />
            </td>
            <td class="table_query_2Col_label_6Letter">基地：</td>
            <td>
            	<script type="text/javascript">
            		genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
		    	</script>
            </td> 	
          </tr>
          <tr>
            <td class="table_query_2Col_label_6Letter">结算日期：</td>
            <td>
            	<input type="text" name="balanceStartDate" id="t3" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4"  hasbtn="true" callFunction="showcalendar(event, 't3', false);"/>
            	&nbsp;至&nbsp;
            	<input type="text" name="balanceEndDate" id="t4" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't4', false);"/>
            </td>
            <td class="table_query_2Col_label_6Letter">状态：</td>
            <td>
			<script type="text/javascript">
	              genSelBoxExp("STATUS",<%=Constant.CHECK_APP_STATUS%>,"",true,"short_sel","","true",'');
	        </script>
 		    </td>
          </tr>
          <tr>
            <td  class="table_query_2Col_label_6Letter">抽单号码：</td>
            <td><input name="CHECK_NO" id="CHECK_NO" value="" type="text" datatype="1,is_digit_letter,25"  class="middle_txt" />
            </td>
            <td class="table_query_2Col_label_6Letter">抽单日期：</td>
            <td>
            	<input type="text" name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             	&nbsp;至&nbsp;
 				<input type="text" name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
            </td>
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