<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.bean.ConditionBean"%>
<%
	String contextPath = request.getContextPath();
	//ConditionBean bean = (ConditionBean)request.getAttribute("bean");//条件bean
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>预授权状态查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔预授权&gt;索赔预授权工单申请</div>
  <form name='fm' id='fm'>
  <input type="hidden" name="curPage" id="curPage" value="1" />
  <input type="hidden" name="wrgroupid" id="wrgroupid" value="<%=request.getAttribute("wrgroupid")==null?"":request.getAttribute("wrgroupid")%>"/>
  <input type="hidden" name="MODEL_ID" id="MODEL_ID" value="<%=request.getAttribute("MODEL_ID")==null?"":request.getAttribute("MODEL_ID")%>"/><!-- 车型id -->
<%--  <input type="hidden" name="type" id="type" value="<%=bean.getConOne()==null?"":bean.getConOne()%>"/>--%>
  <input type="hidden" name="ids" id="ids" value="<%=request.getAttribute("ids")==null?"":request.getAttribute("ids")%>"/>
  <table class="table_query">
       <tr>            
        <td class="table_query_3Col_label_4Letter">项目类型：</td>            
        <td>
			<script type="text/javascript">
<%--			var type = document.getElementById("type").value;--%>
			genSelBoxExp("PRE_AUTH_ITEM",<%=Constant.PRE_AUTH_ITEM%>,"",true,"short_sel","","true",'');
			</script>
        </td>
        <td class="table_query_3Col_label_4Letter">项目代码：</td>
        <td><input type="text" name="ITEMCODE" id="ITEMCODE" datatype="1,is_null,30" class="middle_txt" /></td>
        <td class="table_query_3Col_label_4Letter">项目名称：</td>
        <td><input type="text" name="ITEMNAME" id="ITEMNAME" datatype="1,is_null,30" class="middle_txt"/></td>             
       </tr>
       <tr>
        <td colspan="6" align="center">
       	    <input  id="queryBtn" class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1);"/>
			<input class="normal_btn" type="button" name="button1" value="添加"  onClick="subChecked();"/>
			<input name="button" id="queryBtn1" type="button" onclick="parent.window._hide()" class="normal_btn"  value="关闭" />
        </td>
       </tr>       
 	</table>
 	<br/>
<%--  <table class="table_list" style="border-bottom:1px solid #DAE0EE" >--%>
<%--  	  <th><input type="checkbox" name="checkAll" onclick="selectAll(this,'businesscodeIds')"/>全选</th>--%>
<%--  	  <th>项目类型</th>--%>
<%--      <th>项目代码</th>--%>
<%--      <th>项目名称</th>--%>
<%--       <c:forEach var="addlist" items="${ADDLIST}">--%>
<%--       <tr class="table_list_row1">--%>
<%--       	  <td>--%>
<%--       	  	<input type="checkbox" id="${addlist.ID}" name="businesscodeIds" value="${addlist.ID}"/>--%>
<%--       	  </td>       	--%>
<%--          <td>--%>
<%--          	<input type="hidden" name="itemtypenames" value="${addlist.CODE_DESC}"/>--%>
<%--          	<input type="hidden" name="itemtypecodes" value="${addlist.CODE_ID}"/>  --%>
<%--			<c:out value="${addlist.CODE_DESC}"></c:out>--%>
<%--          </td>--%>
<%--          <td>--%>
<%--          <input type="hidden" name="codes" value="${addlist.CODE}"/>--%>
<%--          <c:out value="${addlist.CODE}"></c:out>--%>
<%--          </td>--%>
<%--          <td>--%>
<%--          <input type="hidden" name="names" value="${addlist.NAME}"/>--%>
<%--          <c:out value="${addlist.NAME}"></c:out>--%>
<%--          </td>--%>
<%--        </tr>--%>
<%--    </c:forEach>--%>
<%--</table>--%>
</form>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/claim/preAuthorization/PreclaimPreMain/preclaimItemQuery.json?COMMAND=1";
	var title = null;
	var columns = [
				{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"businesscodeIds\")' />全选", width:'8%',sortable: false,dataIndex: 'ID',renderer:myCheckBox, align:'center'},
				{header: "项目类型", dataIndex: 'CODE_DESC', align:'center',renderer:showType},
				{header: "项目代码", dataIndex: 'CODE', align:'center',renderer:showCode},
				{header: "项目名称", dataIndex: 'NAME', align:'center',renderer:showName}
		      ];
//全选checkbox
function myCheckBox(value,metaDate,record){
	return String.format("<input type='checkbox' name='businesscodeIds' value='" + value + "' />");
}
function showType(value,metaDate,record){
	return String.format("<input type='hidden' name='itemtypenames' value='" + value + "' /><input type='hidden' name='itemtypecodes' value='" + record.data.CODE_ID + "' />"+value);
	
}
function showCode(value,metaDate,record){
	return String.format("<input type='hidden' name='codes' value='" + value + "' />"+value);
}
function showName(value,metaDate,record){
	return String.format("<input type='hidden' name='names' value='" + value + "' />"+value);
}			      
function selbyid(obj){
	  $('groupid').value = obj.value;
	_hide();
}
//查询方法：
function query(){
	if(!submitForm('fm')) {
		return false;
	}
	fm.action = '<%=contextPath%>/claim/preAuthorization/PreclaimPreMain/preclaimItemQuery.do';
	fm.submit();	
}
function subChecked() {
	var str = new Array();//id数组
	var itemtypedesc = new Array();
	var itemtypeid = new Array();
	var itemcode = new Array();
	var itemname = new Array();
	var chk = document.getElementsByName("businesscodeIds");//id
	var itd = document.getElementsByName("itemtypenames");//项目类型name
	var iti = document.getElementsByName("itemtypecodes");//项目类型id
	var itc = document.getElementsByName("codes");//项目代码
	var itn = document.getElementsByName("names");//项目名称
	var l = chk.length;
	var cnt = 0;
	for(var i=0;i<l;i++){        
		if(chk[i].checked)
		{            
			//str = chk[i].value+","+str;
			//itemtypedesc = itd[i].value+","+itemtypedesc;
			str.push(chk[i].value);
			itemtypedesc.push(itd[i].value);
			itemtypeid.push(iti[i].value);
			itemcode.push(itc[i].value);
			itemname.push(itn[i].value);
			//itemtypeid = iti[i].value+","+itemtypeid;
			//itemcode = itc[i].value+","+itemcode;
			//itemname = itn[i].value+","+itemname; 
			cnt++;
		}
	}
	if(cnt==0){
        MyDivAlert("请选择！");
        return;
    }else{
    	//MyDivConfirm("确认添加？",add,[str]);
    	add(str,itemtypedesc,itemtypeid,itemcode,itemname);
    }
}
//添加
function add(str,itemtypedesc,itemtypeid,itemcode,itemname){
	parentContainer.setItem(str,itemtypedesc,itemtypeid,itemcode,itemname);
 	//关闭弹出页面
 	parent.window._hide();
}
</script>  
  </body>
</html>

