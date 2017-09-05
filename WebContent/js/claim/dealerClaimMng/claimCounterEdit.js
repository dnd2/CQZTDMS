autoAlertException();
var btns = new Array();
btns[0]="counterBtn";
btns[1]="backBtn"; 

/**
 * 反索赔按钮事件
 */
function claimCounter() {
	var rmk = $('#counter_remark');
	if (rmk&&$(rmk).val()) {
		if (window.confirm("是否反索赔？")) {
			commitCounter();
		}
	} else {
		alert("请输入反索赔备注!");
	}
}

/**
 * 提交反索赔操作前的操作
 */
function commitCounter(){
	disableButton();
	sendAjax(globalContextPath+ '/report/dmsReport/Accord/accord.json?type=-1&claimid='+document.getElementById("id").value,backdoSummit,'fm');
}

/**
 * 提交反索赔操作
 */
function backdoSummit(json){
	if (json.succ == "1") {
		var submitUrl = globalContextPath	+ '/claim/dealerClaimMng/ClaimBillTrack/claimCounter.json';
		makeNomalFormCall1(submitUrl,doSummitResult,"fm");
	} else {
		alert("提示：插入记录数据失败！");
	}
}

/**
 * 提交结果
 * @param json
 */
function doSummitResult(json){
	if(json){
		if (json.Exception) {
			alert(json.Exception.message);
			enableButton();
		} else {
			var hint = json.hint;
			var _success = json._success;
			alert(hint);
			if(_success||_success=='true'){
				_hide();
			}
		}
	}
}

/**
 * 按钮失效
 */
function disableButton(){
	$(btns).each(function(i){
		document.getElementById(btns[i]).disabled='disabled';
	});
}

/**
 * 按钮生效
 */
function enableButton(){
	$(btns).each(function(i){
		document.getElementById(btns[i]).disabled=null;
	});
}