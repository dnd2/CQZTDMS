<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>制造商信息添加</title>
    <script type="text/javascript">

        function confirmAdd() {
            if (!submitForm('fm')) {
                return;
            }

            var isAbroad = $("IS_ABROAD").value;
        	if(!isAbroad){
            	MyAlert("请选择国内/国外!");
            	return;
        	}
        	
        	if($("TEL").value!=null&&$("TEL").value!=""){
                var tel = $("TEL").value;
                var pattern =/((^[0-9]{3,4}\-[0-9]{7,8})(-(\d{3,}))?$)|(^[0-9]{7,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^1[0-9]{10}$)/;
    			if(!pattern.exec(tel)){
    				MyAlert("请输入正确的联系电话!");
    				$("TEL").value="";
    				$("TEL").focus();
    				return;
    			}
            }

        	if($("FAX").value!=null&&$("FAX").value!=""){
                var s = $("FAX").value;
            	var pattern =/(^[0-9]{3,4}\-[0-9]{7,8}\-[0-9]{3,4}$)|(^[0-9]{3,4}\-[0-9]{7,8}$)|(^[0-9]{7,8}\-[0-9]{3,4}$)|(^[0-9]{7,15}$)/;
            	 if(!pattern.exec(s)){
            		 MyAlert('请输入正确的传真号码!');
            		 $("FAX").value="";
            		 $("FAX").focus();
            		 return;
            	 }
            }
            
        	var makerType = $("MAKER_TYPE").value;
        	if(!makerType){
            	MyAlert("请选择制造商类型!");
            	return;
        	}

        	if(confirm("确定保存？")){
        		btnDisable();
                var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/addPartMakerInfo.json";
                sendAjax(url, getResult, 'fm');
        	}
        }

        function getResult(jsonObj) {
        	btnEable();
            if (jsonObj != null) {
                var error = jsonObj.error;
                var success = jsonObj.success;
                var exceptions = jsonObj.Exception;
                if (error) {
                    MyAlert(error);
                }
                else if (success) {
                    MyAlert(success);
                    window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/queryPartMakerInit.do';
                }else if(exceptions){
        	    	MyAlert(exceptions.message);
        	    }
            }
        }

        //返回查询页面
        function goback() {
            window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/queryPartMakerInit.do';
        }

    </script>
</head>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：
        基础信息管理 &gt; 配件基础信息维护 &gt; 制造商信息维护 &gt; 新增
    </div>
    <form id="fm" name="fm" method="post">
        <table class="table_edit">
            <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 制造商信息</th>
            <tr>
                <td width="10%" align="right">制造商编码：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" name="MAKER_CODE" id="MAKER_CODE" datatype="0,is_null"/>
                </td>
                
                <td width="10%" align="right">制造商名称：</td>
                <td width="20%"><input type="text" class="middle_txt" name="MAKER_NAME" id="MAKER_NAME" datatype="0,is_null"/></td>
                <!-- 
                <td width="10%" align="right">供应商名称：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME"  name="VENDER_NAME" datatype="0,is_null"/>
                    <input class="mark_btn" type="button" value="&hellip;" onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
                    <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
                </td>
                 -->
                <td width="10%" align="right">国内/国外：</td>
                <td width="20%">
                   <script type="text/javascript">
                       genSelBoxExp("IS_ABROAD", <%=Constant.IS_ABROAD_M%>, "", false, "short_sel", "", "true", '');
                   </script>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">制造商类型：</td>
                <td width="20%">
                    <script type="text/javascript">
                        genSelBoxExp("MAKER_TYPE", <%=Constant.PARTMAKER_TYPE%>, "", false, "short_sel", "", "true", '');
                    </script>
                 </td>
                 
                <td width="10%" align="right">联系人：</td>
                <td width="20%"><input class="middle_txt" type="text" name="LINKMAN" id="linkman"/></td>
                
                <td width="10%" align="right">联系电话：</td>
                <td width="20%">
                    <input type="text" class="normal_txt" name="TEL" id="TEL" maxlength="20"/>
                </td>
            </tr>
            <tr>
            	<td width="10%" align="right">传真：</td>
                <td width="20%">
                    <input type="text" class="normal_txt" name="FAX" id="FAX" maxlength="20"/>
                </td>
                <td width="10%" align="right">地址：</td>
                <td colspan="3"><input class="maxlong_txt" type="text" name="ADDR" id="addr" /></td>
            </tr>
        </table>
        <table class="table_edit">
            <tr>
                <td align="center">
                    <input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="confirmAdd();"
                           class="normal_btn"/>
                    <input type="button" name="saveBtn" id="saveBtn" value="返 回" onclick="javascript:goback();"
                           class="normal_btn"/>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
