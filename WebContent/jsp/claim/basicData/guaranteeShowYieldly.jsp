<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% String contextPath = request.getContextPath(); %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>三包策略新增</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="__extQuery__(1);">
	<form method="post" name="fm" id="fm">
	
	  <script type="text/javascript">
	   var myPage;
	   //查询该策略设定的省份信息
	   var url = "<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/queryYieldly.json";			
	   var title = null;
	   var columns = [
	  				{header: "<input type=\"checkbox\" onclick=\"selectAll(this,'yieldlycb');\" name=\"allcb\"/>",dataIndex: 'AREA_ID',align:'center',renderer:createCB},
	  				{header: "产地名称", dataIndex: 'AREA_NAME', align:'center'}
	  				];

	   function createCB(value,meta,record){
			return String.format("<input type=\"checkbox\" name=\"yieldlycb\" value=\"" + value + "\"/>");
		}

		function saveYieldlyConfim(){
			if(confirm("是否确定设定产地？")){
				saveYieldly();
			}	
		}

		//保存设定省份信息
		function saveYieldly(){
			var turl = "<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/saveGuaranteeStrategyYieldly.json";
			makeNomalFormCall(turl,showResult,'fm');
		}

		function showResult(){
			var first=parent.$('inIframe');
			if(first){
				var second=first.contentWindow.$('yieldlyFrame');
				second.contentWindow.__extQuery__(1);
				_hide();
			}else{
				parent.$('yieldlyFrame').contentWindow.__extQuery__(1);
				_hide();
			}
		}
	 </script> 
	  <!--分页  -->
	  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	  <br/>
	  <table class="table_edit">
	  	  <tr>
	  	  	 <td align="center">
	  	  	     <input type="hidden" name="strategyId" id="strategyId" value="<%=request.getParameter("ID")%>"/>
	  	  	     <input class="normal_btn"  name="submit" type="button" onclick="saveYieldlyConfim();" value ='确定'/>
                    &nbsp;&nbsp;
                 <input class="normal_btn" type="button" name="Submit" onclick="_hide();" value="返回"/>
	  	  	 </td>
	  	  </tr>
	  </table>
	</form>
</body>
</html>