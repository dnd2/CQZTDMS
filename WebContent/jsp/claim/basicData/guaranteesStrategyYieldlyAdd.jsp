<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% String contextPath = request.getContextPath(); %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>三包策略产地维护</title>
	</head>
<body onload="__extQuery__(1);">
	<form method="post" name="fm" id="fm">
	
	  <script type="text/javascript">
	   var myPage;
	   //查询该策略设定的省份信息
	   var url = "<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/queryStrategyYieldly.json";
	   url = url + "?ID=" + <%=request.getParameter("ID")%>;			
	   var title = null;
	   var columns = [
	  				{header: "<input type=\"checkbox\" onclick=\"selectAll(this,'yieldlycb')\" name=\"allcb\"/>",dataIndex:'ID',align:'center',renderer:createCB},
	  				{header: "产地名称", dataIndex: 'YIELDLY_NAME', align:'center'}
	  				];

	   function createCB(value,meta,record){
			return String.format("<input type=\"checkbox\" name=\"yieldlycb\" value=\"" +value+ "\"/>");
		}

		function showYieldly(){
			OpenHtmlWindow("<%=contextPath%>"+"/jsp/claim/basicData/guaranteeShowYieldly.jsp?ID=<%=request.getParameter("ID")%>",770,410);
		}

        function deleteYieldlyConfirm(){
        	var selectedModel = document.getElementsByName('yieldlycb');
			var isSelect = false;

			if(selectedModel!=null && selectedModel!='undefined'){
				for(var i=0;i<selectedModel.length;i++){
					if(selectedModel[i].checked){
						isSelect = true;
						break;
					}
				}
			}

			if(isSelect){
				MyConfirm("是否确认删除？",deleteYieldly,[null]);
			}else{
				MyAlert("请选择产地！");
			}	
		}

		//删除设定产地
		function deleteYieldly(){
			var turl = "<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/deleteGuaranteeStrategyYieldly.json";
			makeNomalFormCall(turl,showResult,'fm');
		}

		function showResult(){
			__extQuery__(1);
		}
	 </script> 
	  <!--分页  -->
	  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	  
	  <table class="table_edit">
	  	  <tr>
	  	  	 <td align="center">
	  	  	     <input class="normal_btn"  name="submit" type="button" onclick="showYieldly();" value ='新增产地'/>
                    &nbsp;&nbsp;
                 <input class="normal_btn" type="button" name="Submit" onclick="deleteYieldlyConfirm();" value="删除产地"/>
	  	  	 </td>
	  	  </tr>
	  </table>
	</form>
</body>
</html>