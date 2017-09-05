<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>三包预警规则维护</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
//日历控件初始化
function doInit()
	{
	   loadcalendar();
	}
</script>
</head>

<body>
     <div class="navigation">
         <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;三包预警规则维护
     </div>
  <!-- 查询条件 begin -->
  <form method="post" name="fm" id="fm">
    <table  class="table_query">
          <tr>
            <td class="table_query_2Col_label_7Letter">三包预警规则代码：</td>
            <td align="left">
            	<input type="text"  name="vrCode"  id="vrCode"  class="middle_txt"/>
            </td>
            <td class="table_query_2Col_label_7Letter">预警类型：</td>
            <td align="left">
          	   <script type="text/javascript">
	   					    genSelBoxExp("vrType",<%=Constant.VR_TYPE%>,"",true,"short_sel","","false",'');
	  		      </script>
            </td>
          </tr>
          <tr>
             <td class="table_query_2Col_label_7Letter">预警等级：</td>
            <td align="left">
          	    <script type="text/javascript">
	   					    genSelBoxExp("vrLevel",<%=Constant.VR_LEVEL%>,"",true,"short_sel","","false",'');
	  		      </script>
            </td>
            <td class="table_query_2Col_label_7Letter">配件三包类型：</td>
            <td align="left">
	              <select id="partWrType" name="partWrType" class="short_sel" >
						<option value=''>-请选择-</option>
						<c:forEach var="typeList" items="${partWrTypeList}">
								<option value="${typeList.codeId}" title="${typeList.codeId}" >${typeList.codeDesc}</option> 
						</c:forEach>
					</select>
            </td>
          </tr>
		 <tr>
		    <td colspan="6" align="center">
			   <input class="normal_btn" type="button" name="button1" value="查询" onclick="__extQuery__(1);"/>
			   &nbsp;&nbsp;
			   <input class="normal_btn" type="button" name="button2" value="新增"  onclick="claimWrRuleAddInit();"/>	
			   &nbsp;&nbsp;
 				<input class="normal_btn" type="button" onclick="goImport();" value="批量导入"/>
			      
		   </td>
	     </tr>   
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form> 
<!-- 查询条件 end -->
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >

	var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/basicData/ClaimWrRule/claimWrRuleQuery.json";
				
	var title = null;

	var columns = [
				{header: "三包预警规则代码", dataIndex: 'VR_CODE', align:'center'},
				{header: "预警类型",dataIndex: 'VR_TYPE' ,align:'center',renderer:getItemValue},
				{header: "预警等级 ",dataIndex: 'VR_LEVEL' ,align:'center',renderer:getItemValue},
				{header: "配件三包类型",dataIndex: 'WRTYPE' ,align:'center'},
				{header: "配件名称或关注部位",dataIndex: 'PNAME' ,align:'center'},
				{header: "法定",dataIndex: 'VR_LAW' ,align:'center'},
				{header: "预警",dataIndex: 'VR_WARRANTY' ,align:'center'},
				{header: "法规",dataIndex: 'VR_LAW_STANDARD' ,align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'VR_ID',renderer:claimRuleUpdateInit ,align:'center'}
		      ];
	//修改的超链接设置
	function claimRuleUpdateInit(value,meta,record){
	    return String.format(
         "<a href=\"<%=contextPath%>/claim/basicData/ClaimWrRule/claimWrRuleUpdateInit.do?id="+ 
         	value + "\">[修改]</a><a href=\"#\" onclick='sel(\""+value+"\")'>[删除]</a>");
	}
	
	function sel(str){
	MyConfirm("是否确认删除？",del,[str]);
}  
//删除
function del(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimWrRule/claimWrRuleDelete.json?vrId='+str,delBack,'fm','');
}
//删除回调方法：
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlert("删除成功！");
		__extQuery__(1);
	} else {
		MyAlert("删除失败！请联系管理员！");
	}
} 
	//新增三包预警规则页面
	function claimWrRuleAddInit(){
		fm.action = "<%=contextPath%>/claim/basicData/ClaimWrRule/claimWrRuleAddInit.do";
		fm.submit();
	}
	
	function goImport(){
		location = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/inportPerRule.do' ;
	}
	
</script>
</body>
</html>