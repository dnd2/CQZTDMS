<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
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
function doInit()
{  
	loadcalendar();   //初始化时间控件
	genLocSel('txt1','txt2','txt3','<c:out value="${map.PROVINCE_ID}"/>','<c:out value="${map.CITY_ID}"/>','<c:out value="${map.AREA_ID}"/>'); // 加载省份城市和县
}

</script>
</head>
<body  onunload='javascript:destoryPrototype();'>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;修改经销商地址</div>
 <form method="post" name = "fm" id="fm" >
 <input id="DEALER_ID" name="DEALER_ID" type="hidden" value="${dealerId}"/>
 <input id="ADDRESS_ID" name="ADDRESS_ID" type="hidden" value="<c:out value="${map.ID}"/>"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		  <tr>
		    <td class="table_query_2Col_label_6Letter">价格有效日期：</td>
		    <td colspan="3">
            		<input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);" />
            		&nbsp;至&nbsp;
            		<input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);" />
            </td>
	      </tr>
	       <tr>
		    <td class="table_query_2Col_label_6Letter">是否默认：</td>
		    <td>
		  <label>
				<script type="text/javascript">
					genSelBoxExp("DEALERSTATUS",<%=Constant.IF_TYPE%>,"10041001",'',"short_sel",'',"false",'');
				</script>
		  </label>
		    </td>
		    <td></td>
		    <td>
		    </td>
	      </tr>
     </table> 
     <table class=table_query>
	 <tr>
	 <td>
	<input type="button" value="修改" name="saveBtn" class="normal_btn" onclick="modifyAddress()"/>	
	<input type="button" value="关闭" name="cancelBtn"  class="normal_btn" onclick="_hide();" /></td>
	</tr>
   </table>
</form>

<script type="text/javascript" >
function modifyAddress()
{
	if(submitForm('fm'))
	{
			 toSubmit();
	} 
}
function toSubmit(){
	var priceId=${priceId};
	var relationId=${relationId};
	var dealerId=${dealerId};
	makeNomalFormCall('<%=contextPath%>/sysmng/dealer/DealerInfo/defaultPrice.json?priceId='+priceId+'&relationId='+relationId+'&dealerId='+dealerId,showResult,'fm');
}
//回调方法
function showResult(json){
	if(json.returnValue == 2){
		_hide();
		MyAlert("必须要有默认价格！");
	}else{
		parentContainer.parentMonth();
		_hide();
	}
}
</script>

</body>
</html>
