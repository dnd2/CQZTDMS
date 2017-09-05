<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<head>
<%@ page import="com.infodms.dms.po.TtIfWrActivityPO"%>
<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<TITLE>服务活动申请表修改</TITLE>
<%
	TtIfWrActivityPO tisp = (TtIfWrActivityPO)request.getAttribute("servicecarBean");
 %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<SCRIPT LANGUAGE="JavaScript">
	function showResult(json){
		if(json.ACTION_RESULT == '1'){
			goBack();
		//	MyConfirm("新增成功！点击确认返回查询界面或者点击左边菜单进入其他功能！","window.location.href = '<%=request.getContextPath()%>/sysmng/orgmng/DlrInfoMng/queryAllDlrInfo.do'");
		}else if(json.ACTION_RESULT == '2'){
			MyAlert("新增失败！请重新载入或者联系系统管理员！");
		}
	}
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/feedbackmng/apply/ServiceActivityApply/serviceactivityapplyQuery.json";
	}
	function confirModify(){
		if(document.getElementById("ACT_TYPE").options[document.getElementById("ACT_TYPE").selectedIndex].value==""){;
		MyAlert("请选择一个活动类型!");
	}else {
		if(submitForm('fm')){
			var content = document.getElementById('CONTENT').value;
			var le = document.getElementById('CONTENT').value.length;
			if(content != null && content != ''){
				if(le > 0 && le <= 200){
				document.getElementById("fm");
				fm.action = '<%=request.getContextPath()%>/feedbackmng/apply/ServiceActivityApply/serviceactivityapplyUpdate.do?ORDER_ID=<%=tisp.getOrderId()%>';
				MyConfirm("确认修改",fm.submit);
				//fm.submit();
				}else{
					MyAlert("申请内容不能超过200个字符！");
				}
				
			}else{
				MyAlert("申请内容是必填项！");
			}
		  }
		}
	}

	//选择厂商
	function selectProducerCodeVendor(){
		window.open("../../MainServlet?action=OEM_APPLICATION_CREATE_PRE_SELECT_PRODUCER_CODE_PRE", "", "width=835px,height=520px,left=100,top=80,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no maximum=yes");
	}//提交
	
</SCRIPT>
</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：  信息反馈管理&gt;信息反馈提报 &gt;服务活动申请表维护</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>

<table width=100% border="0" align="center" cellpadding="1" cellspacing="1"   class="table_edit">
	       <th colspan="6"><img class="nav" src="../../../img/subNav.gif" /> 基本信息</th>
          <tr> 
          <td class="table_edit_3Col_label_7Letter">工单号：</td>
           <td><%=tisp.getOrderId()==null?"":tisp.getOrderId() %>
           <input type="hidden" name="id" value="<%=tisp.getId()%>"/></td>
          </tr>
          <tr >
            <td class="table_edit_3Col_label_7Letter">经销商联系人：</td>
            <td><input type='text'  name='LINK_MAN'  id='LINK_MAN' value="<%=tisp.getLinkMan()==null?"":tisp.getLinkMan() %>"   class="middle_txt" datatype='0,is_digit_letter_cn' /></td>
            <td class="table_edit_3Col_label_7Letter">经销商电话：</td>
            <td><input type='text'  name='TEL'  id='TEL'  class="middle_txt"    value='<%=tisp.getTel()==null?"":tisp.getTel() %>' datatype='0,is_phone,11'/></td>
            <td class="table_edit_3Col_label_7Letter" >经销商传真：</td>
            <td><input type='text'  name='FAX'  id='FAX' class="middle_txt"   datatype='0,is_phone,11' value='<%=tisp.getFax()==null?"":tisp.getFax() %>' /></td>
          </tr>
          <tr >
          <td class="table_edit_3Col_label_7Letter" >服务活动名称：</td>
            <td ><input type='text'  name='ACT_NAME' value="<%=tisp.getActName()==null?"":tisp.getActName() %>" id='ACT_NAME'  class="middle_txt" datatype='0,is_digit_letter_cn' />
            <td class="table_edit_3Col_label_7Letter">活动类型：</td>
              <td nowrap><script type="text/javascript">
	              genSelBoxExp("ACT_TYPE",<%=Constant.ACTIVITY_TYPE%>,"",true,"short_sel","","true",'');
	            </script></td>
            <td class="table_edit_3Col_label_7Letter">金额：</td>
            <td><input type='text'  name='ACT_MONEY'  id='ACT_MONEY'   class="middle_txt"  datatype='0,is_yuan,11'  value='<%=tisp.getActMoney()==null?"":tisp.getActMoney() %>' /></td>
            
          </tr>
          <tr>
            <td class="table_edit_3Col_label_7Letter">申请内容：</td>
            <td height="27" colspan="5" align="left" ><span class="tbwhite">
              <textarea  name='CONTENT'  id='CONTENT'   rows='5' cols='80' ><%=tisp.getActContent()==null?"":tisp.getActContent() %></textarea>
            </span></td>
          </tr>
        </table>
    <!-- 添加附件 开始  -->
	<table class="table_info" border="0" id="file">
		<input type="hidden" id="fjids" name="fjids"/>
	    <tr>
	        <th>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
			     <input type="button" class="normal_btn"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
			</th>
		</tr>
		<tr>
    		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
  		<%for(int i=0;i<fileList.size();i++) { %>
	 	 <script type="text/javascript">
	 	 addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>');
	 	 </script>
	<%}%>
	</table> 
  <!-- 添加附件 结束 -->  
       <table class="table_list">
          <tr > 
            <th height="12" align=center>
								<input type="button" onClick="confirModify();" class="normal_btn"
									style="" value="确定" />
								&nbsp;&nbsp;
			<input type="button" onClick="javascript:history.go(-1);" class="normal_btn" style="width=8%" value="返回"/></th>
		  </tr>
        </table>
  
    <!-- 资料显示区结束 -->

</form>
<script LANGUAGE="JavaScript">
	function assignSelect(name,value) {
		var sel = document.getElementById(name);
		var option = sel.options;
		var optionLength = option.length;
		for (var i = 0;i<optionLength;i++) {
		  	if (value ==option[i].value) {
		  		sel.selectedIndex = i;
		  	}
		}
	}
	assignSelect("ACT_TYPE","<%=tisp.getActType()%>");

  </script>
</BODY>
</html>
