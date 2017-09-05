<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>供应商信息修改</title>
    <script type="text/javascript">
        //修改供应商信息
        function confirmUpdate() {
            if (!submitForm('fm')) {
                return;
            }

            var isAbroad = $("#IS_ABROAD")[0].value;
        	if(!isAbroad){
            	layer.msg("请选择国内/国外!", {icon: 15});
            	return;
        	}
        	
        	if($("#TEL")[0].value!=null&&$("#TEL")[0].value!=""){
                var tel = $("#TEL")[0].value;
                var pattern =/((^[0-9]{3,4}\-[0-9]{7,8})(-(\d{3,}))?$)|(^[0-9]{7,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^1[0-9]{10}$)/;
    			if(!pattern.exec(tel)){
    				layer.msg("请输入正确的联系电话!", {icon: 15});
    				$("#TEL")[0].value="";
    				$("#TEL")[0].focus();
    				return;
    			}
            }

        	if($("#FAX")[0].value!=null&&$("#FAX")[0].value!=""){
                var s = $("#FAX")[0].value;
            	var pattern =/(^[0-9]{3,4}\-[0-9]{7,8}\-[0-9]{3,4}$)|(^[0-9]{3,4}\-[0-9]{7,8}$)|(^[0-9]{7,8}\-[0-9]{3,4}$)|(^[0-9]{7,15}$)/;
            	 if(!pattern.exec(s)){
            		 layer.msg('请输入正确的传真号码!', {icon: 1});
            		 $("#FAX")[0].value="";
            		 $("#FAX")[0].focus();
            		 return;
            	 }
            }
            
        	var venderType = $("#VENDER_TYPE").val();
        	if(!venderType){
            	layer.msg("请选择供应商类型!", {icon: 15});
            	return;
        	}
        	
            editSubmit();
            
        }
        function editSubmit() {
            MyConfirm("是否修改?", editSubmitAction);
        }
        function editSubmitAction() {
        	btnDisable();
            makeNomalFormCall('<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/updatePartVenderInfo.json', showForwordValue, 'fm');
        }
        function showForwordValue(jsonObj) {
        	btnEnable();
            if (jsonObj != null) {
                var error = jsonObj.error;
                var success = jsonObj.success;
                var exceptions = jsonObj.Exception;
                if (error) {
                    MyAlert(error);
                }else if (success) {
                	MyAlert(success, goback);
                }else if(exceptions){
        	    	layer.msg(exceptions.message);
        	    }
            }
//             var parentContainer = parent.frames['inIframe'].contentWindow;
//         	btnDisable();
//             parentContainer.__extQuery__('${curPage}');
//             parent._hide();

        }

        //返回查询页面
        function goback(){
            window.location.href = '<%=contextPath %>/parts/baseManager/partsBaseManager/PartVenderManager/queryPartVenderInit.do';
        }
    </script>
</head>
<body> <!-- onunload='javascript:destoryPrototype();' is not valid -->
<div class="wbox">
    <div class="navigation">
        <img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理 &gt;基础信息管理 &gt; 配件基础信息维护 &gt; 供应商信息维护 &gt; 修改
		</div>
    <form id="fm" name="fm" method="post">
        <input type="hidden" name="VENDER_ID" value="${venderInfo.VENDER_ID}"/>
        <input type="hidden" name="curPage" id="curPage" value="1"/>
        <div class="form-panel">
            <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/> 供应商信息</h2>
            <div class="form-body">
                <table class="table_query">
                    <tr>
                        <td class="right">供应商编码：</td>
                        <td><input class="middle_txt" type="text" name="VENDER_CODE" id="VENDER_CODE" datatype="0,is_null" value="${venderInfo.VENDER_CODE}"/></td>    
                        <td class="right">供应商名称：</td>
                        <td><input type="text" class="middle_txt" name="VENDER_NAME" id="VENDER_NAME" datatype="0,is_null" value="${venderInfo.VENDER_NAME}"/></td>
                        <td class="right">国内/国外：</td>
                        <td>
                            <script type="text/javascript">
                                genSelBox("IS_ABROAD", <%=Constant.IS_ABROAD_V%>, "${venderInfo.IS_ABROAD}", false, "u-select", "");
                            </script>
                        <font color="red">*</font></td>
                    </tr>
                    <tr>
                        <td class="right">联系人：</td>
                        <td><input class="middle_txt" type="text" name="LINKMAN" id="LINKMAN"  value="${venderInfo.LINKMAN}" maxlength="20"/></td>
                        <td class="right">联系电话：</td>
                        <td><input type="text" class="middle_txt" name="TEL" id="TEL" value="${venderInfo.TEL}" maxlength="20"/></td>
                        <td class="right">传真：</td>
                        <td><input type="text" class="middle_txt" name="FAX" id="FAX"  value="${venderInfo.FAX}" maxlength="20"/></td>
                    </tr>
                    <tr>
                        <td class="right">供应商类型：</td>
                        <td>
                            <script type="text/javascript">
                                genSelBox("VENDER_TYPE", <%=Constant.VENDER_TYPE%>, "${venderInfo.VENDER_TYPE}", false, "u-select", "");
                            </script>
                        	<font color="red">*</font>
                        </td>
                        <td class="right">地址：</td>
                        <td colspan=3"><input type="text" name="ADDR" id="addr" class="maxlong_txt middle_txt input-long-txt" value="${venderInfo.ADDR}" datatype="1,is_textarea,100"/>
                        </td>
                    </tr>
                </table>
                <table class="table_edit" width="100%">
                    <tr>
                        <td align="center">
                            <input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="confirmUpdate();" class="u-button"/>
                            <input type="button" name="backBtn" id="backBtn" value="返 回" onclick="goback();" class="u-button"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        
    </form>
</div>
</body>
</html>
