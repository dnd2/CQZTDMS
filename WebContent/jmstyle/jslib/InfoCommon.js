/*判断数字*/
function isNumber(str) {
	var digits = "0123456789";
	var i = 0;
	if (str == null) return false;
	var sLength = str.length;
	if (sLength == 0) return false;
	while ((i < sLength)) {
		var c = str.charAt(i);
		if (digits.indexOf(c) == -1) return false;
		i++;
	}
	return true;
}

/*批量隐藏*/
function disabledButton(buttonObj, type) {
	var arr = buttonObj;
	for (var i = 0; i < buttonObj.length; i++) {
		document.getElementById(buttonObj[i]).disabled = type;
	}
}

/*显示错误*/
function showError(ermsg, erdiv, errid, msg, msgw) {
	document.getElementById(ermsg).setText("");
	document.getElementById(ermsg).appendText(msg);
	document.getElementById(erdiv).setStyles({
		display: '',
		width: msgw,
		top: document.getElementById(errid).getTop() - 1,
		left: document.getElementById(errid).getLeft() + document.getElementById(errid).getStyle("width").toInt() + 8
	});
}

/**
 * 判断字符串是否包含相应的字符
 * @param str
 * 		待判断端字符集合
 * @param substr
 * 		需要查询的字符
 * @returns true or false
 */
function iscontains(str, substr) {
	return str.indexOf(substr) >= 0;
}

/**
 * 批量禁用页面上所有的button按钮
 */
function btnDisable() {
	$('input[type="button"]').each(function (button) {
		button.disabled = true;
	});

}

/**
 * 批量启用页面上所有的button按钮
 */
function btnEnable() {
	$('input[type="button"]').each(function (button) {
		button.disabled = "";
	});
}

/**
 * 公用模块代理商选择页面
 */
function showCompany(){ 
	OpenHtmlWindow(g_webAppName+'/common/OrgMng/queryCompany.do',800,450,'经销商公司查询');
}

/**
 * 通用选择经销商界面
 * 
 * @param inputCode
 * 		回填页面经销商Code域id
 * @param inputId
 * 		回填页面经销商Id域id 
 * @param isMulti
 * 		是否多选 true 是 false 否
 * @param orgId
 * 		区域ID TM_ORG 对应ORG_ID
 * @param isAllLevel
 * 		经销商等级限制条件,不传值查询所有等级，否则按照需要限制的经销商等级CODE_ID进行传值 
 * @param isAllArea
 * 		是否查询所有区域 true 是 false 否
 * @param isDealerType
 * 		经销商业务类型 销售11071001 或 服务11071002
 * @param inputName
 * 		回填页面经销商全称
 * @param title 弹框标题
 */
function showOrgDealer(inputCode, inputId, isMulti, orgId, isAllLevel, isAllArea, isDealerType, inputName, title) {
	if (!inputCode) { inputCode = null; }
	if (!inputId) { inputId = null; }
	if (!isMulti) { isMulti = null; } 
	if (!orgId || orgId == 'false' || orgId == 'true' || orgId == 'null' || orgId == '') { orgId = null; }
	if (!isAllLevel) { isAllLevel = null; }
	if (!isAllArea) { isAllArea = null; }
	if (!isDealerType) { isDealerType = null; }
	if (!inputName) { inputName = null; }
	title = title==null ? "选择经销商" : title;
	OpenHtmlWindow(g_webAppName + '/dialog/dealer/showOrgDealer.jsp?INPUTCODE=' + inputCode + "&INPUTID=" + inputId + "&ISMULTI=" + isMulti + "&ORGID=" + orgId + "&ISALLLEVEL=" + isAllLevel + "&ISALLAREA=" + isAllArea + "&isDealerType=" + isDealerType + "&INPUTNAME=" + inputName, 800, 500, title);
}

/**
 * 通用选择物料树界面
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * isAllArea   : true值查全部，否则过滤业务范围
 */
function showMaterial(inputCode, inputName, isMulti, isAllArea) {
	if (!inputCode) { inputCode = null; }
	if (!inputName) { inputName = null; }
	OpenHtmlWindow(g_webAppName + "/dialog/material/showMaterial.jsp?INPUTID=" + inputCode + "&INPUTNAME=" + inputName + "&ISMULTI=" + isMulti + "&ISALLAREA=" + isAllArea, 850, 480, '物料选择');
}

/**
 * 通用选择物料组树界面
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * groupLevel：输出的物料组等级
 * isAllArea：true值查全部，否则过滤业务范围
 */
function showMaterialGroup(inputCode, inputName, isMulti, groupLevel, isAllArea) {
	if (!inputCode) { inputCode = null; }
	if (!inputName) { inputName = null; }
	if (!groupLevel) { groupLevel = null; }
	OpenHtmlWindow(g_webAppName + "/dialog/material/showMaterialGroup.jsp?INPUTID=" + inputCode + "&INPUTNAME=" + inputName + "&ISMULTI=" + isMulti + "&GROUPLEVEL=" + groupLevel + "&ISALLAREA=" + isAllArea, 850, 480, '物料组选择');
}

/**
 * 通用选择组织界面
 * inputCode : 回填页面经销商Code域id
 * inputId   : 回填页面经销商Id域id 
 * orgId     : 组织id，如有，则表示根据orgId过滤下级所有组织，否则不过滤
 */
function showOrg(inputCode ,inputId ,isMulti ,orgId)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrg.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId,730,390);
}

/**
 * 选择供应商
 * inputId   : 回填页供应商code域id
 * inputName ：回填页供应商id域id
 * isMulti   : true值多选，否则单选
 */
function showPartVender(inputCode ,inputId ,isMulti ){
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/venderSelectSingle.jsp?INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,450);
}

/*获取元素,返回string对象*/
function getElementsByJSet(jset) {
	var json = getElementsBySetTag(jset);
	return JSON.stringify(json);
}

/*获取document的所有节点，返回匹配的json对象*/
function getElementsBySetTag(jset) {
	var item = document.documentElement;
	var json = {};
	for (var i = 0; i < item.length; i++) {
		try {
			if (item[i].getAttribute("jset") == jset) {
				json[item[i].id] = encodeURI(item[i].value.trim(), "UTF-8");
			}
		} catch (e) {
			//alert(e.name + ": " + e.message);
		}
	}
	return json;
}

//清除无用的符号
function clearValue(_str, _sub) {
	if (_str && _sub) {
		var reg = new RegExp("^[" + _sub + "]+|[" + _sub + "]+$", "g");
		_str = _str.replace(reg, '');
		while (_str.indexOf(_sub + _sub) > -1) {
			_str = _str.replace(_sub + _sub, _sub);
		}
	}
	return _str;
}

/**
 * 多选框复选公用方法,使用场景, grid中实现多选
 * 
 * @param obj
 * 		全选文本框DOM对象
 * @param checkInputName
 * 		待选文本框属性 NAME
 */
function selectAll(obj, checkInputName) {
	var checkedflag = obj.checked;

	if (checkedflag) {
		$("input[name='" + checkInputName + "']").each(function () {
			this.checked = true;
		});
	} else {
		$("input[name='" + checkInputName + "']").each(function () {
			this.checked = false;
		});
	}
}

/**
 * Checkbox 选择类，实现全选或取消等功能
 * 
 * @Author Michael
 * @Date 2017.8.8
 */
var CheckboxHelper = (function () {
	var self;
	var Checkbox = function (params) {
		params = params || { pEl: '#myGrid', chEl: '' };
		this.parentEl = params.pEl ? $(params.pEl) : $('#myGrid');
		this.checkAllEl = params.chEl ? $(params.chEl) : null;
		self = this;

		this.parentEl.on('click', 'input[type="checkbox"]', function () {
			var that = $(this);

			if (that.hasClass('ch-all')) {
				self.doAllClick($(this));
			} else {
				self.checkOrUnCheck($(this));
			}
		});

		if (this.checkAllEl != null) {
			$(this.checkAllEl).click(function () {
				if ($(this).hasClass('ch-all')) {
					self.doAllClick($(this), true);
				} else {
					self.doAllClick($(this), false);
				}
			});
		}
	}

	Checkbox.prototype = {
		doAllClick: function (o, checked) {
			var el = o[0] || null,
				elements = self.parentEl[0],
				len = 0,
				i;

			if (!el || !elements) {
				return;
			}

			if (checked != null && typeof checked == 'boolean') {
				el.checked = checked;
			}

			elements = elements.querySelectorAll('input[type="checkbox"]');
			len = elements.length;

			if (el.checked === true) {
				for (i = 0; i < len; i++) {
					elements[i].checked = true;
				}
			} else {
				for (i = 0; i < len; i++) {
					elements[i].checked = false;
				}
			}
		},

		checkOrUnCheck: function (o) {

		},
		/**
		 * 获取所有选择元素
		 * @return {Array}
		 */
		getCheckedItems: function () {
			var elements = this.parentEl[0];
			len = elements.length,
				result = [],
				i;

			for (i = 0; i < len; i++) {
				result.push(elements[i]);
			}

			return result;
		}
	};

	return Checkbox;
}).call(this);

/**
 * TextBox
 */
function genTextBoxStr(paramTxtId, paramTxtName, paramHidId, paramHidName, paramDiv, paramTextClass, paramDivClass, paramScript, paramTextOther, strTxt, strValue) {
	paramTxtName == "" ? paramTxtId : paramTxtName;
	paramHidName == "" ? paramHidId : paramHidName;

	var str = "";
	str += "<input type=\"text\" name=\"" + paramTxtName + "\" id=\"" + paramTxtId + "\"";

	if (paramTextClass != "") {
		str += " class=\"" + paramTextClass + "\"";
	}

	if (paramTextOther != "") {
		str += "style=\"" + paramTextOther + "\"";
	}

	str += " onclick=\"clickinsertDiv('" + paramDiv + "', '" + paramTxtId + "', '" + paramHidId + "', this.value, '" + strTxt + "', '" + strValue + "');\" onpropertychange=\"insertDiv('" + paramDiv + "', '" + paramTxtId + "', '" + paramHidId + "', this.value, '" + strTxt + "', '" + strValue + "');\" onblur=\"setDivNone('" + paramDiv + "') ;\"";
	str += " />";

	str += "<div id=\"" + paramDiv + "\"";

	if (paramDivClass != "") {
		str += " class=\"" + paramDivClass + "\"";
	}

	str += " style=\"display: none; border: solid #808080 1px; position: absolute;\">";
	str += "</div>";
	str += "<input type=\"hidden\" name=\"" + paramHidName + "\" id=\"" + paramHidId + "\"";

	if (paramScript != "") {
		str += "onpropertychange=\"" + paramScript + "\"";
	}

	str += "/>";
	str += "<input type=\"hidden\" id=\"compareHidden\" />";

	return str;
}

/**
 * Detect the version of a browser
 */
var BrowserVersion = {
	version: null,
	limitedVer: {'Chrome': 45, 'Firefox': 48, 'Opera' : 40, 'MSIE': 10, 'IE': 10},
	/**
	 * - IE 10
     * ua = 'Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)';
	 * - IE 11
     * ua = 'Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko';
     * - Edge 12 (Spartan)
     * ua = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, 
	 * 		like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0';
     * - Edge 13
     * ua = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, 
	 * 		like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586';
	 * 
	 * @return {Object} Including browser name and version
	 */
	getData: function() {
		var ua = navigator.userAgent, tem, 
			M = ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];

		if (/trident/i.test(M[1])) {
			tem = /\brv[ :]+(\d+)/g.exec(ua) || [];
			return { name: 'IE', version: (tem[1] || '') };
		}
		if (M[1] === 'Chrome') {
			tem = ua.match(/\bOPR|Edge\/(\d+)/)
			if (tem != null) { return { name: 'Opera', version: tem[1] }; }
		}
		M = M[2] ? [M[1], M[2]] : [navigator.appName, navigator.appVersion, '-?'];
		if ((tem = ua.match(/version\/(\d+)/i)) != null) { M.splice(1, 1, tem[1]); }
		return {
			name: M[0],
			version: M[1]
		};
	},
	/**
	 * The browser is satisfied with your application or not.
	 * 
	 * @param {Object} params  {'Chrome': 50, 'Firefox': 50, 'IE': 8}
	 */
	checkVersionIsValid: function( params ) {
		params = params || this.limitedVer;
		var browser = this.getData(), item;

		for ( item in params ) { 
			if ( item == browser.name && parseInt( params[item] ) >= parseInt( browser.version ) ) {
				return false;		
			}
		}

		return true;
	},

	showNoSupportedTips: function( message ) {
		message = message || '很抱歉，由于您的浏览器版本太低，系统功能无法正确使用，请选用更高版本的浏览器！';
		this.version = this.version || this.limitedVer;

		if ( ! this.checkVersionIsValid( this.version ) ) {
			var tip = document.createElement( 'div' ),
				p   = document.createElement( 'p' );
			
			p.innerHTML   = message;
			tip.className = "no-supported";		
			tip.appendChild( p );
			document.body.innerHTML = '';
			document.body.appendChild( tip );		
		}
	}
};	

// Define a namespace for this file
var Common = {
	desc: '通用方法定义',
	version: '1.0.0'
};

/**
 * 查询条件显示隐藏
 * 
 * @author Michael
 * @version 1.0.0
 * @date 2017.8.24
 */
(function( $, undefined ) {
	'use strict';
	var self;
	var query = function() {
		this.table = document.querySelector('.form-panel .table_query');
		this.panel = null;
		this.event = false;
		this.currentEl = null;
		self = this;

		if ( this.table ) {
			this.init();
		}
	};

	query.prototype = {
		/**
		 * Initialize the status of the related elements
		 * 
		 * @return {Boolean} The quering element is found or not  
		 */
		init: function() {
			var buttons,
				queryEls,
				num = 0;

			this.panel = document.querySelector('.form-panel');		

			if ( ! this.panel ) {
				return false;
			}

			var len = this.table.rows.length;

			for ( var i = 0; i < len; i++ ) {
				if ( this.table.rows[i].style.display != 'none' ) {
					num++;
				}
			}

			if ( this.table.rows && num > 3 ) {
				if ( ! this.addShowButton( this.panel ) ) {
					return false;
				}

				this.showOrHide();
			}

			$( this.panel ).on( 'click', '.switch-list-btn', function() {
				self.setQuery( $(this) );
			});

			return true;
		},

		setQuery: function( o ) {
			var id = o.attr( 'data-id' ),
				queryBox,
				len = 0, i;

			if ( ! id ) {
				return false;
			}

			queryBox = document.getElementById( id );
			len = queryBox.rows ? queryBox.rows.length : 0;

			if ( len > 0 ) {
				var num = 0;

				for ( i = 0; i < len; i++ ) {
					if ( queryBox.rows[i].style.display != 'none' ) {
						num++;
					}
				}

				if ( num > 3 ) {
					if ( ! this.addShowButton( this.panel, queryBox ) ) {
						return false;
					}

					self.table = queryBox;
					this.showOrHide();
				}
			}
		},
		/**
		 * Method to add a button on the title bar
		 */
		addShowButton: function( el, queryEl ) {
			var queryEls = el.querySelector('h2'),
				buttons,
				iconEl,
				bStatus = false,
				len = 0, i;

			buttons = ( queryEl && queryEl.querySelectorAll( 'input[type="button"]' ) ) 
					|| el.querySelectorAll('input[type="button"]' );
			buttons = Array.prototype.slice.call( buttons );
			len = buttons.length;
			
			for ( i = 0; i < len; i++ ) {
				if ( buttons[i].value.replace( /\s+/g, '' ).indexOf( '查询' ) !== -1 ) {
					bStatus = true;
					break;
				}
			}

			if ( ! bStatus ) {
				return false;
			}

			iconEl = queryEls.querySelector( 'i' );

			if ( iconEl == null ) {
				var iEl  = document.createElement( 'i' );

				iEl.className = "icon-eye-close";
				queryEls.appendChild( iEl );
			} else {
				iconEl.classList.remove( 'icon-eye-open' );
				iconEl.classList.add( 'icon-eye-close' );
			}

			if ( ! this.event ) {
				queryEls.addEventListener( 'click', this.showOrHide, false );
				this.event = true;
			}

			this.currentEl = queryEl;
			
			return true;
		},
		/**
		 * Method to show or hide the form elements
		 * 
		 * @param {Object} e  When event has been occured, this value can get the event object.
		 * @return {Boolean}
		 */
		showOrHide: function( e ) {
			var target = e && e.target ? e.target : null,
				el = self.currentEl || self.table;		

			if ( ! el ) {
				return false;
			}

			var rows = el.rows,
				len  = rows.length,
				i;
			
			if ( len > 2 ) {
				for ( i = 0; i < len; i++ ) {
					if ( i == 0 || i == 1 || rows[i].style.display == 'none'
						|| ( i + 1 ) == len ) {
						continue;
					}

					var classItem = rows[i].classList;

					if ( classItem.contains( 'tr-hide' ) ) {
						classItem.remove( 'tr-hide' );
					} else {
						classItem.add( 'tr-hide' );
					}
				}	

				if ( target ) {
					var iEl = target.querySelector( 'i' );

					if ( iEl != null ) {
						if ( iEl.classList.contains( 'icon-eye-close' ) ) {
							iEl.classList.remove( 'icon-eye-close' );
							iEl.classList.add( 'icon-eye-open' );
						} else {
							iEl.classList.remove( 'icon-eye-open' );
							iEl.classList.add( 'icon-eye-close' );
						}
					}
				}
			}	
			
			return true;
		}
	};

	Common.QueryBlockShow = query;
})( jQuery );

/**
 * Method to provide a simple way of print the page
 * 
 * @author Michael
 * @version 1.0.0
 * @date 2017.8.30
 */
(function( window, undefined ) {
	'use strict';
	var self;
	var Printer = function() {
		this.isIeControl = typeof ActiveXObject == 'undefined' ? false : true;
		this.ieControl   = null;
		self = this;
		this.init();
	};

	Printer.prototype = {
		init: function() {
			jQuery(document).ready( function($) {
				if ( this.isIeControl ) {
					if ( this.ieControl == null ) {
						var control  = document.createElement( 'object' ),
						hKeyRoot = 'HKEY_CURRENT_USER',
						hKeyPath = '\\Software\\Microsoft\\Internet Explorer\\PageSetup\\',
						// 设置网页打印的页眉的值
						hKeyHeader = 'header',
						hKeyFooter = 'footer'; 

						control.id  = "webBrower";
						control.setAttribute( 'classid', 'CLSID:8856F961-340A-11D0-A96B-00C04FD705A2' );
						control.style.display = 'none';
						document.body.appendChild( control );

						try {
							var active = new ActiveXObject( "WScript.Shell" );
							// 设置网页打印的页眉页脚为默认值 
							active.RegWrite( hKeyRoot + hKeyPath + hKeyHeader, "" ); 
							active.RegWrite( hKeyRoot + hKeyPath + hKeyFooter, "&b&p/&P" ); 
						} catch ( e ) { console.log( e ); }
					}
				}

				var buttons = $( '.page-print-buttons' );

				if ( buttons && buttons.length > 0 ) {
					buttons.on( 'click', 'input[type="button"]', function( e ) {
						var target    = $(this),
							options   = [
								{name: '打印', callback: self.pagePrint}, 
								{name: '打印设置打印页面设置', callback: self.pagePrintSetting},
								{name: '预览', callback: self.pagePrintPreview}
							], i;
						
						if ( target[0].tagName == 'INPUT' &&　target[0].type == 'button' ) {
							for ( i = 0; i < options.length; i++ ) {
								if ( options[i].name.indexOf( target.val() ) !== -1 || target.val().indexOf(options[i].name) !== -1 ) {
									var before	  = target.attr( 'data-before' ),
										after     = target.attr( 'data-after' ),
										beforeFun = window[before],
										afterFun  = window[after];
									// If a custom function has been defined, so execut it.
									if ( beforeFun ) {
										beforeFun.call( null );
									}
										
									options[i].callback.call( self );

									if ( afterFun ) {
										afterFun.call( null );
									}
									break;
								}
							}
						}	
					});
				}
			});
		},
		/**
		 * Method to retrieve the control dom
		 * 
		 * @return {Element}
		 */
		getControl: function() {
			if ( this.ieControl == null ) {
				this.ieControl = document.getElementById( 'webBrower' );
			}
			
			return this.ieControl;
		},
		/**
		 * 页面设置,因浏览器不同只针对IE,其它浏览器默认为点击打印的效果
		 * 
		 * @return {void}
		 */
		pagePrintSetting: function() {
			if ( this.isIeControl ) {
				this.getControl().execwb( 8, 1 );
				return;
			}

			window.print();
		},
		/**
		 * 页面预览，因浏览器不同只针对IE,其它浏览器默认为点击打印的效果
		 * 
		 * @return {void}
		 */
		pagePrintPreview: function() {
			if ( this.isIeControl ) {
				this.getControl().execwb( 7, 1 );
				return;
			}

			window.print();
		},
		/**
		 * 打印
		 * 
		 * @return {void}
		 */
		pagePrint: function() {
			if ( this.isIeControl ) {
				this.ieControl.execwb( 6, 1 );
				return;
			}

			window.print();
		}
	};

	Common.Printer = new Printer();
})( window );

/**
 * 公共方法
 */
(function( $, undefined ) {
	var general = {
		sizeArgs: [{aEl: '.grid-resize #myGrid', rEl: '.form-panel', aElDom: null, rElDom: null}],
		/**
		 * Method to set the size of the element
		 * 
		 * @param {String} sizeType Method which need to call
		 * @param {Integer} pos		Size offset	
		 * @return {Boolean}
		 */
		setElSize: function( sizeType, pos ) {
			pos      = pos !== void(0) && parseInt( pos ) !== NaN ? pos : 26;
			sizeType = sizeType || 'width';

			try {
				this.sizeArgs.forEach( function( item ) {
					var elPos = item.pos || pos;

					if ( ! item.aElDom ) {
						item.aElDom = $( item.aEl );
						item.rElDom = $( item.rEl );
					} 

					item.aElDom[sizeType]( item.rElDom[sizeType]() - elPos );
				});
			} catch ( e ) {
				console.log(e);
				return false;
			}

			$(window).resize(function() {
				general.sizeArgs.forEach( function( item ) {
					var elPos = item.pos || pos;
					item.aElDom[sizeType]( item.rElDom[sizeType]() - elPos );
				});
			});

			return true;
		}	
	};

	Common.FunHelper = general;
})( jQuery );

$(function() {
	new Common.QueryBlockShow();
	Common.FunHelper.setElSize();
});