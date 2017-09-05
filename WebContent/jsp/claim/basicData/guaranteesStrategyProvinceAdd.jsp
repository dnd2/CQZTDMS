<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% String contextPath = request.getContextPath(); %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>三包策略省份维护</title>
	</head>
<body onload="__extQuery__(1);">
	<form method="post" name="fm" id="fm">
	
	  <script type="text/javascript">
	   var myPage;
	   //查询该策略设定的省份信息
	   var url = "<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/queryStrategyProvince.json";
	   url = url + "?ID=" + <%=request.getParameter("ID")%>;			
	   var title = null;
	   var columns = [
	  				{header: "<input type=\"checkbox\" onclick=\"selectAll(this,'provincecb')\" name=\"allcb\"/>",dataIndex:'ID',align:'center',renderer:createCB},
	  				{header: "省份名称", dataIndex: 'REGION_NAME', align:'center'}
	  				];

	   function createCB(value,meta,record){
			return String.format("<input type=\"checkbox\" name=\"provincecb\" value=\"" +value+ "\"/>");
		}

		function showProvince(){
			OpenHtmlWindow("<%=contextPath%>"+"/jsp/claim/basicData/guaranteeShowProvince.jsp?ID=<%=request.getParameter("ID")%>",770,410);
		}

        function deleteProvinceConfirm(){
        	var selectedModel = document.getElementsByName('provincecb');
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
				MyConfirm("是否确认删除？",deleteProvince,[null]);
			}else{
				MyAlert("请选择省份！");
			}	
		}

		//删除设定省份
		function deleteProvince(){
			var turl = "<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/deleteGuaranteeStrategyProvince.json";
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
	  	  	     <input class="normal_btn"  name="submit" type="button" onclick="showProvince();" value ='新增省份'/>
                    &nbsp;&nbsp;
                 <input class="normal_btn" type="button" name="Submit" onclick="deleteProvinceConfirm();" value="删除省份"/>
	  	  	 </td>
	  	  </tr>
	  </table>
	</form>
</body>
</html>