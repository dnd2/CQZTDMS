var menujson = [],
	tabMenu;

// 初始化系统菜单项
function initAccordion() {
	var url = globalContextPath + "/common/MenuShow/getUserSysFun.json";
	makeCall(
		url,
		function (json) {
			var res = json.sysfun;

			for (var i = 0; i < res.length; i++) {
				var row = res[i];
				var red = {};
				red.id = row.funcId;
				red.pid = row.parFuncId;
				red.name = row.funcName;
				red.isfunc = row.isFunc;
				red.icon = row.icon;
				if (row.funcCode && row.isFunc == 1) {
					red.url = globalContextPath + row.funcCode + ".do";
				}
				menujson.push(red);
			}
			var htmlString = '';

			for (var i = 0; i < menujson.length; i++) {
				if (menujson[i].pid == '10') {
					htmlString += '<li class="u-menu-item"' + ' title="' + i + '">';
					htmlString += '<a href="javascript:;" class="u-menu-item-link">';
					htmlString += '<i><img src="' + globalContextPath + '/jmstyle/img/' + menujson[i].icon + '"></i>';
					htmlString += '<span>' + menujson[i].name + '</span><span></span></a>'; // .replace(/new/g,// "")
					htmlString += getTreeNode(menujson, menujson[i].id);
					htmlString += '</li>';
				}
			}
			htmlString += '<li><span class="menu-item-bottom"></span></li>';
			$("#u-menu-ul").html(htmlString);
			initMenu();
		}, null);
}

function getTreeNode(arr, pid) {
	var htmlString = '';
	var isfunc = false;
	for (var i = 0; i < arr.length; i++) {
		if (arr[i].pid == pid) {
			if (arr[i].isfunc == 1) {
				htmlString += '<li class="u-menu-item">';
				htmlString += '<a href="javascript:;" url="' + arr[i].url + '" key="' + arr[i].id + '" class="u-menu-item-link">';
				htmlString += '<i></i>';
				htmlString += '<span class="spanStyle" title=' + arr[i].name + '>' + arr[i].name + '</span>'; // .replace(/new/g,// "")
				htmlString += '</a>';
				htmlString += '</li>';
				isfunc = true;
			} else {
				htmlString += ' <li class="u-menu-item">';
				htmlString += ' <a href="javascript:;" class="u-menu-item-link">';
				htmlString += ' <i></i>';
				htmlString += ' <span>' + arr[i].name + '</span>'; // .replace(/new/g, // "")
				htmlString += ' <span ></span>';
				htmlString += ' </a>';
				var nodeString = getTreeNode(menujson, arr[i].id);
				if (nodeString != null && nodeString != 'undefined') {
					htmlString += nodeString;
				}
				htmlString += ' </li>';
			}
		}
	}
	if (isfunc) {
		htmlString = '<ul class="u-menu-sub unline">' + htmlString + '</ul>';
	} else {
		htmlString = '<ul class="u-menu-sub">' + htmlString + '</ul>';
	}

	return htmlString;
}

function initMenu() {
	/* menu click */
	var a = $('.u-menu ul li a', App.getSelector());
	a.on('click', function (e) {
		// menu checked
		if (getMenuSwidthChecked()) {
			$('#menu-switch', App.getSelector()).click();
		}
		// menu not checked
		else {

		}
		var that = $(e.target);
		if (that[0].nodeName == 'I' || that[0].nodeName == 'SPAN') {
			that = that.parent();
		}
		if (that[0].nodeName == 'IMG') {
			that = that.parent().parent();
		}
		var isNav = that.parent().parent().hasClass('u-nav'), isOpen = that
			.hasClass('u-menu-item-link u-menu-open');
		// close other opened
		if (isNav) {
			var set = that.parent().parent().children('.u-menu-item');
			for (var i = 0; i < set.length; i++) {
				var ele = $(set[i]).children('.u-menu-item-link');
				ele.removeClass('u-menu-open');
			}
		}
		// sub menu is exits
		var hasSubMenu = that.next().hasClass('u-menu-sub');
		// has sub menu
		if (hasSubMenu) {
			if (isOpen) {
				that.removeClass('u-menu-open');
				$('.active').removeClass('active');
			} else {
				that.addClass('u-menu-open');
			}
			// no has sub menu
		} else {
			var hasActive = that.hasClass('u-menu-item-link active');
			if (hasActive) {
			}
			var set = that.parent().parent().children('.u-menu-item');
			var idx = that.parent().index();
			set.each(function (i, e) {
				var ele = $(e), a = ele.children('a');
				if (ele.index() == idx) {
					a.addClass('active');
					var span = a.children('span');
					App.addPanel(span.html(), a.attr('url'), a.attr('key'));
				} else {
					a.removeClass('active');
				}
			});
		}

		e.preventDefault();
	});
	/* tab click */
	var tab = $('.u-tab', App.getSelector());
	//	tab.on('dblclick', function(e) {
	//		var tit = $(e.target), isTit = tit.hasClass('u-title');
	//		if (isTit) {
	//			var lst = tit.parent().parent().parent().children(), li = tit
	//					.parent().parent();
	//			for ( var i = 0; i < lst.length; i++) {
	//				$(lst[i]).removeClass('u-tab-open');
	//			}
	//			var next = li.next();
	//			var prev = li.prev();
	//			if (next.length) {
	//				next.addClass('u-tab-open');
	//			} else if (prev.length) {
	//				prev.addClass('u-tab-open');
	//			}
	//			li.remove();
	//		}
	//	});

	tab.on('click', function (e) {
		try {
			var span = $(e.target), li = span.parent().parent(), that = li, idx = that.index(), set = that.parent().children('.u-tab-itme'),
				liEls = $('.u-tab li');
			for (var i = 0; i < set.length; i++) {
				var ele = $(set[i]);
				if (ele.index() == idx) {
					ele.addClass('u-tab-open');
				} else {
					ele.removeClass('u-tab-open');
				}
			}

			liEls.each(function () {
				var liEl = $(this),
					iFrame = liEl.find('iframe');

				if (liEl.attr('id') == that.attr('id')) {
					iFrame.attr('id', 'inIframe');
				} else if (liEl[0].tagName == that[0].tagName) {
					iFrame.removeAttr('id');
				}
			});

			var cls = $(e.target);
			var isClose = cls.hasClass('u-close');
			if (isClose) {
				var lst = cls.parent().parent().parent()
					.children(), li = cls.parent().parent();
				for (var i = 0; i < lst.length; i++) {
					$(lst[i]).removeClass('u-tab-open');
				}
				var next = li.next();
				var prev = li.prev();
				if (next.length) {
					next.addClass('u-tab-open');
					next.find('iframe').attr('id', 'inIframe');
				} else if (prev.length) {
					prev.addClass('u-tab-open');
					prev.find('iframe').attr('id', 'inIframe');
				}
				li.remove();

				tabMenu.tabRemoved(li.id);
			}
		} catch (e) {
		}
		e.preventDefault();
	});

	/* menu-switch click */
	var menu_switch = $('#menu-switch', App.getSelector());
	menu_switch.on('change', function (e) {
		var bodyer = $('.u-bodyer', App.getSelector());
		var isMini = bodyer.hasClass('u-bodyer u-mini');
		var checked = this.checked;
		if (isMini) {
			bodyer.removeClass('u-mini');
		} else {
			bodyer.addClass('u-mini');
			var set = $('.u-menu ul li a', App.getSelector());
			set.each(function (idx, ele) {
				$(ele).removeClass('u-menu-open');
			});
		}
		if (checked) {
			bodyer.addClass('u-mini');
		} else {
			bodyer.removeClass('u-mini');
		}
	});
	menu_switch.change();
	// menu switch is checked
	function getMenuSwidthChecked() {
		return $('#menu-switch', App.getSelector()).is(":checked");
	}
	function MenuSwidthOpen() {

	}
	function gunDong() {
		var big = $('.u-selbox');
		var ob = $('.u-selbox .u-sysdate');
		ob.hide();
		ob.prependTo(big);
		ob.slideDown(1100);
	}
	setInterval(gunDong, 5000);
}

// 声名App对象
(function (wdo, doc) {
	var key = 1;
	wdo.App = {
		version: '1.0',
		init: function () {
			return wdo = (wdo.top == wdo.parent) ? wdo.parent : wdo;
		},
		getSelector: function () {
			return (wdo.top == wdo.parent) ? wdo.parent.document : wdo.document;
		},
		addPanel: function (tit, url, key, typ) {
			try {
				function ck(tit, url, key) {
					var space = $('.u-space', App.getSelector()),
						size = {
							width: (space.width() - 0) + 'px',
							height: (space.height() - 31) + 'px'
						};

					function ifr_load(e) {
						/*
						var ifr_head = $(this.contentWindow.document.head),
							set = App.space_set;
						for(var key in set)
							ifr_head.append(set[key]);
							
						this.height=this.contentWindow.document.documentElement.scrollHeight;
						this.contentWindow.document.documentElement.scrollHeight=0;
						*/
					}
					try {
						var fmt =
							'<li class="u-tab-itme u-tab-open" id="$KEY">' +
							'<label class="u-tab-title">' +
							'<img src="' + globalContextPath + '/jmstyle/img/u-refresh.gif" class="u-refresh" onclick="App.irefresh();">' +
							'<span class="u-title">$TIT</span>' +
							(key == 'welcome' ? '<img src="' + globalContextPath + '/jmstyle/img/u-close.png" class="u-close-hidden">' : '<img src="' + globalContextPath + '/jmstyle/img/u-close.png" class="u-close">') +
							'</label>' +
							'$TYP' +
							'</li>',
							text =
								'<div class="u-tab-panel">' +
								'$TXT' +
								'</iframe>',
							ifrm =
								'<iframe runat="server" id="inIframe" src="$URL" width="$WIDTH" height="$HEIGHT" class="u-tab-panel iPanel-' + key + '" ' +
								'frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="auto" allowtransparency="yes"></iframe>',

							tab = $('.u-tab', App.getSelector());

						var set = $('.u-tab li', App.getSelector()),
							kys = [],
							flag = -1;

						if (typ === "text") {
							fmt = fmt.replace('$TYP', text);
						} else {
							fmt = fmt.replace('$TYP', ifrm);
						}

						for (var i = 0; i < set.length; i++) {
							kys.push(set[i].getAttribute('id'));
							if (key === kys[i]) {
								flag = i;
							}
						}
						/*
						for(var i=0;i<kys.length;i++){
							if(key===kys[i]){
								flag = i;
							}
						}
						*/
						var lst = $('.u-tab li', App.getSelector()), selected;

						//
						if (flag == -1) {

							if((kys.length+1)>11){
								layer.msg('最多只能打开10个窗口', {icon: 15});
								return;
							}

							for (var i = 0; i < lst.length; i++) {
								lst[i].setAttribute('class', 'u-tab-itme');
								//$(lst[i]).attr('class', 'u-tab-itme');
							}

							if (url) {
								/*
								$.ajax({
									url:url,
									global:false,
									type:'post',
									data:null,
									dataType:'html',
									async:false,
									success:function(rs){
										tab.append(fmt.replace('$TIT', tit||'').replace('$KEY', key||'').replace('$TXT', rs));
									}
								});
								
								$.get(url, function(rs){
									tab.append(fmt.replace('$TIT', tit||'').replace('$KEY', key||'').replace('$TXT', rs));
								});
								*/

								var ele = $(
									fmt.replace('$TIT', tit || '')
										.replace('$KEY', key || '')
										.replace('$URL', url)
										.replace('$WIDTH', size.width)
										.replace('$HEIGHT', size.height)
								);

								//ele.children('iframe').on('readystatechange', ifr_load);

								tab.append(ele);
								tabMenu.showTabButton();
							}
						} else {
							for (var i = 0; i < lst.length; i++) {
								var ele = $(lst[i]);
								if (flag == i) {
									ele.addClass('u-tab-open');
									selected = ele;
								} else {
									ele.removeClass('u-tab-open');
								}
							}
						}

						var li = $('.u-tab li');
						if (selected) {
							ele = selected;
						}

						li.each(function () {
							var liv = $(this),
								iBox = liv.find('iframe');

							if (liv.attr('id') == ele.attr('id')) {
								iBox.attr('id', 'inIframe');
							} else {
								iBox.removeAttr('id');
							}
						});
					} catch (e) { }
				}
				setTimeout(function () { ck(tit, url, key); }, 0);
			} catch (e) { throw e; }
		},
		remPanel: function (key) {
			try {
				var ele = $('.u-tab ' + '#' + key, App.getSelector());
				if (ele.length == 0) {
					App.alert('找不到需要关闭的页面！');
					return;
				}
				var next = ele.next();
				var prev = ele.prev();

				var lst = ele.parent().children('li');

				for (var i = 0; i < lst.length; i++) {
					$(lst[i]).removeClass('u-tab-open');
				}
				if (next.length) {
					next.addClass('u-tab-open');
				} else
					if (prev.length) {
						prev.addClass('u-tab-open');
					}
				ele.remove();
			} catch (e) { throw e; }
		},
		irefresh: function () {
			document.getElementById('inIframe').contentWindow.location.reload(true);
		}
	};
})(window, document);

/**
 * Tab标签页上下文菜单
 */
var TabMenu = (function () {
	var self;
	/**
	 * The constructor initialize all data and events that need to load. 
	 */
	var menu = function (tab) {
		self = this;
		this.defaultPos = 1;
		this.createContextMenu();
		this.menu = $('.l-menu');        // The context menu wrap
		this.menuOver = $('.l-menu-over'); // Which menu item moved to the position where it is on the menu.   
		this.current = null;
		this.tab = tab || $('.u-tab');
		this.tabBar = this.tab.parent();
		this.buttons = { left: 0, right: 0, docLeft: null, docRight: null };
		this.tabFirstEl = this.tab[0].firstElementChild;
		this.tabDefaultVal = 5; // The container has a padding of 5px
		this.frmEvtItems = [];
		this.uniqueId = 0;
		self.menuOver[0].style.top = this.defaultPos + "px";

		// Listening the event of the menu item  
		this.tab.on('contextmenu', 'li', function (e) {
			var elemX = e.target.offsetWidth + e.target.clientLeft,
				elemY = e.target.offsetHeight + e.target.clientTop,
				offset = self.offsetPos(e.target),
				frameEl;

			e.stopPropagation();
			self.menu[0].style.left = e.clientX + "px";
			self.menu[0].style.top = e.clientY + "px";
			self.menu.show();
			self.current = $(this);

			frameEl = self.current.find('iframe')[0];

			if (frameEl && frameEl.contentDocument) {
				var id = frameEl.getAttribute('data-id');

				if (!self.isFrmExisted(id)) {
					$(frameEl.contentDocument).click(function () {
						self.defaultPos = 1;
						self.menuOver[0].style.top = "-24px";
						self.menu.hide();
					});
					self.uniqueId += 1;
					frameEl.setAttribute('data-id', self.uniqueId);
					self.frmEvtItems.unshift({ index: self.uniqueId, val: frameEl });
				}
			}

			return false;
		});

		$(document).on('click', function () {
			self.menu.hide();
		});

		this.menu.on('mouseover', '.l-menu-item', function () {
			var that = $(this)[0],
				height = that.offsetHeight,
				id = that.getAttribute('item-id');

			self.defaultPos = id == 1 ? 1 : height * (id - 1);
			self.menuOver[0].style.top = self.defaultPos + "px";
		});

		$('.l-menu-inner').on('mouseout', function () {
			self.defaultPos = 1;
			self.menuOver[0].style.top = "-24px";
		});

		this.menu.on('click', '.l-menu-item', function () {
			var item = $(this)[0].getAttribute('item-fun');

			if (item != null) {
				self.funs[self.camelCase(item)] && self.funs[self.camelCase(item)]();
			}
		});

		this.tab.parent().on('click', '.tab-button', function () {
			if ($(this).hasClass('tab-link-left')) {
				self.moveToLeftSide();
			} else {
				self.moveToRightSide();
			}
		});
	};

	menu.prototype = {
		createContextMenu: function () {
			var html = '<div class="l-menu" style="display:none;position:absolute;"><div class="l-menu-yline"></div>'
				+ '<div class="l-menu-over" style="top:24px;">'
				+ '<div class="l-menu-over-l"></div><div class="l-menu-over-r"></div></div>'
				+ '<div class="l-menu-inner">'
				+ '<div class="l-menu-item" item-id="1" item-fun="close">'
				+ '<div class="l-menu-item-text">关闭当前页</div></div>'
				+ '<div class="l-menu-item" item-id="2" item-fun="close-others">'
				+ '<div class="l-menu-item-text">关闭其他</div></div>'
				+ '<div class="l-menu-item" item-id="3" item-fun="close-all">'
				+ '<div class="l-menu-item-text">关闭所有</div></div></div>'
				+ '</div>';

			document.body.insertAdjacentHTML('beforeend', html);
		},

		offsetPos: function (el) {
			var left = el.offsetLeft,
				top = el.offsetTop,
				cur = el.offsetParent;

			while (cur != null) {
				left += cur.offsetLeft;
				top += cur.offsetTop;
				cur = cur.offsetParent;
			}

			return {
				x: left,
				y: top
			}
		},

		camelCase: function (name) {
			var index = name.indexOf('-');

			return name[index + 1] ? name.replace("-" + name[index + 1], name[index + 1].toUpperCase())
				: name;
		},

		funs: {
			close: function () {
				if (self.current) {
					var prev = self.current.prev(),
						next = self.current.next(),
						li = self.tab.find('li');

					li.each(function () {
						$(this).removeClass('u-tab-open');
					});

					if (next.attr('id')) {
						self.current.next().addClass('u-tab-open');
						self.current.next().find('iframe').attr('id', 'inIframe');
					} else if (prev.attr('id')) {
						self.current.prev().addClass('u-tab-open');
						self.current.prev().find('iframe').attr('id', 'inIframe');
					}

					if (li.length > 1) {
						self.current.remove();
					} else {
						li[0].classList.add('u-tab-open');
					}
				}
			},

			closeOthers: function () {
				self.removeItems(1);
			},

			closeAll: function () {
				self.removeItems();
			}
		},
		/**
		 * Remove tags
		 * 
		 * @param {Integer} type  1 Remove others except the current element  2 Remove all
		 * @return {void} 
		 */
		removeItems: function (type) {
			var liItems = self.tab[0].querySelectorAll('li'),
				len = liItems.length,
				i;

			type = type || 2;

			if (type == 1) {
				for (i = 0; i < len; i++) {
					if (self.current[0].id == liItems[i].id) {
						liItems[i].classList.add('u-tab-open');
						liItems[i].querySelector('iframe').id = "inIframe";
					} else {
						if (liItems[i].id == 'welcome') {
							continue;
						}

						self.tab[0].removeChild(liItems[i]);
						self.removeFrmEvt(liItems[i]);
					}
				}
			} else {
				for (i = 0; i < len; i++) {
					if (liItems[i].id != 'welcome') {
						self.tab[0].removeChild(liItems[i]);
						self.removeFrmEvt(liItems[i]);
					}
				}

				liItems[0].classList.add('u-tab-open');
				liItems[0].querySelector('iframe').id = "inIframe";
			}

			tabMenu.tabRemoved();
		},
		/**
		 * 删除事件移除元素
		 * 
		 * @param {DOM Object} el
		 * @return {void}
		 */
		removeFrmEvt: function (el) {
			var len = self.frmEvtItems.length,
				i;

			$(el.contentDocument).unbind('click', function () {
				var id = el.getAttribute('data-id');

				for (i = 0; i < len; i++) {
					if (self.frmEvtItems[i].index == id) {
						self.frmEvtItems.splice(i, 1);
						break;
					}
				}
			});
		},
		/**
		 * 检查是否添加过事件元素
		 * 
		 * @param {String} id 元素对应索引值
		 * @return {Boolean}  true 存在 false 不存在
		 */
		isFrmExisted: function (id) {
			var len = self.frmEvtItems.length,
				i;

			for (i = 0; i < len; i++) {
				if (self.frmEvtItems[i].index == id) {
					return true;
				}
			}

			return false;
		},

		/**
         * 计算标签总长大于tab栏总长，显示按钮
         */
		showTabButton: function () {
			var totalWidth = this.tabBar[0].offsetWidth,
				tabs = this.tab[0].children,
				len = tabs.length,
				tabWidth = 0,
				buttons,
				i;

			for (i = 0; i < len; i++) {
				var style = window.getComputedStyle(tabs[i], null),
					left = parseInt(style.getPropertyValue('margin-left'));

				if (i == 0) {
					left = 0;
				}

				tabWidth += tabs[i].offsetWidth + left
					+ parseInt(style.getPropertyValue('margin-right'));
			}

			buttons = this.menuTabButton();
			totalWidth -= buttons.left + buttons.right;

			if (tabWidth > totalWidth) {
				if (buttons.leftEl.classList.contains('hide2')) {
					buttons.leftEl.classList.remove('hide2');
					buttons.rightEl.classList.remove('hide2');
				}

				if ( ! self.tabFirstEl ) {
					self.tabFirstEl = self.tab[0].firstElementChild;
				}

				$(self.tabFirstEl).animate({ marginLeft: (tabWidth - totalWidth) * -1 });
			}
		},
		/**
		 * Create moving buttons and retrieve their width
		 * 
		 * @return {Object}
		 */
		menuTabButton: function () {
			var leftBtn = document.querySelector('.tab-link-left');	

			if (! self.buttons.left && ! leftBtn) {
				var docFrag = document.createDocumentFragment(),
					buttons = ["tab-link-left", "tab-link-right"],
					wrap = document.createElement('div');

				wrap.classList.add('tab-buttons');

				buttons.forEach(function (className) {
					var div = document.createElement('div');
					div.classList.add(className);
					div.classList.add('hide2');
					div.classList.add('tab-button');
					wrap.appendChild(div);
				}, this);
				docFrag.appendChild(wrap);

				document.querySelector('.tabs').appendChild(docFrag);
				self.buttons.docLeft = document.querySelector('.tab-link-left');
				self.buttons.docRight = document.querySelector('.tab-link-right');
				self.buttons.left = self.buttons.docLeft.offsetWidth;
				self.buttons.right = self.buttons.docRight.offsetWidth;
			} else if ( ! self.buttons.left ) {
				var rightBtn = document.querySelector('.tab-link-right');	

				self.buttons.docLeft  = leftBtn;
				self.buttons.docRight = rightBtn;
				self.buttons.left  	  = leftBtn.offsetWidth;
				self.buttons.right 	  = rightBtn.offsetWidth;
			}

			return {
				leftEl: self.buttons.docLeft,
				rightEl: self.buttons.docRight,
				left: self.buttons.left,
				right: self.buttons.right
			};
		},
		/**
		 * When the Left button is clicked, this method will be triggered.
		 * 
		 * @param {Integer} id The specified tab is clicked
		 * @return {Boolean}
		 */
		moveToLeftSide: function (id) {
			var tabs = this.tab[0].children,
				buttons = this.menuTabButton(),
				prevBtnOffset = 0,
				movedToTab,
				tabLen = tabs.length,
				i,
				total = 0,
				startPos = self.tabDefaultVal; // 容器元素有5像素的左边距

			if (!buttons.leftEl || !buttons.rightEl) {
				return false;
			}

			var offsetVal = parseInt(window.getComputedStyle(tabs[0], null).marginLeft),
				offsetRealVal = Math.abs(offsetVal);
			// 去掉边距得到正确的偏移值
			offsetRealVal = offsetRealVal >= startPos ? Math.abs(offsetVal) - startPos : offsetRealVal;
			// The offset position of the button     
			prevBtnOffset = offsetRealVal
				+ parseInt(window.getComputedStyle(buttons.leftEl, null).marginLeft) + buttons.left + total;

			for (i = 0; i < tabLen; i++) {
				var style = window.getComputedStyle(tabs[i], null),
					left = i == 0 ? 0 : parseInt(style.marginLeft),
					right = parseInt(style.marginRight),
					curId = tabs[i].id,
					current,
					start = 0,
					end;

				current = tabs[i].offsetWidth + (left + right);
				start = total;
				end = start + current;

				if (id != null) {
					if (start < prevBtnOffset && curId == id) {
						movedToTab = tabs[i];
						break;
					}
				} else if (start < prevBtnOffset && end >= prevBtnOffset) {
					movedToTab = tabs[i];
					break;
				}

				// The total length of tab
				total += current;
			}

			if (movedToTab == null) {
				return false;
			}

			$(self.tabFirstEl).animate({ marginLeft: (total - buttons.left) * -1 });

			return true;
		},
		/**
		 * Move to Right
		 * 
		 * @param {Integer} id
		 * @return {Boolean}
		 */
		moveToRightSide: function (id) {
			var tabs = this.tab[0].children,
				buttons = this.menuTabButton(),
				tabBox = this.tab[0].parentElement,
				nextBtnOffset = 0,
				movedToTab,
				tabLen = tabs.length,
				total = 0,
				startPos = self.tabDefaultVal,
				pos = 0,
				movedTagVal = 0;

			if (!buttons.leftEl || !buttons.rightEl) {
				return false;
			}

			var offsetVal = parseInt(window.getComputedStyle(tabs[0], null).marginLeft),
				offsetRealVal = Math.abs(offsetVal);
			offsetRealVal = offsetRealVal >= startPos ? Math.abs(offsetVal) - startPos : offsetRealVal;
			// The offset position of the button    
			prevBtnOffset = offsetRealVal
				+ parseInt(window.getComputedStyle(buttons.leftEl, null).marginLeft) + buttons.left + total;
			nextBtnOffset = offsetRealVal
				+ (tabBox.offsetWidth - (parseInt(window.getComputedStyle(buttons.rightEl, null).marginLeft) + buttons.right))

			for (i = 0; i < tabLen; i++) {
				var style = window.getComputedStyle(tabs[i], null),
					left = i == 0 ? 0 : parseInt(style.marginLeft),
					right = parseInt(style.marginRight),
					curId = tabs[i].id,
					current,
					start = 0,
					end;

				current = tabs[i].offsetWidth + (left + right);
				start = total;
				end = start + current;

				if (id != null) {
					if (end > nextBtnOffset && curId == id) {
						movedToTab = tabs[i];
						break;
					}
				} else if (start <= nextBtnOffset && end > nextBtnOffset) {
					movedToTab = tabs[i];
					break;
				}

				// The total length of tab
				total += current;
			}

			if (movedToTab == null) {
				return false;
			}

			movedTagVal = window.getComputedStyle(movedToTab, null);
			movedTagVal = movedToTab.offsetWidth + parseInt(movedTagVal.marginLeft) + parseInt(movedTagVal.marginRight);
			pos = total - (nextBtnOffset - prevBtnOffset) + movedTagVal;
			$(self.tabFirstEl).animate({ marginLeft: (pos * -1) });

			return true;
		},
		/**
		 * When tab is removed, we must indicate its position
		 * 
		 * @param {Integer} id
		 * @return {void}
		 */
		tabRemoved: function (id) {
			var tabs = this.tab[0].children,
				tabBoxWidth = this.tabBar[0].offsetWidth,
				tabWidth = 0,
				len = tabs.length,
				i,
				prevBtnOffset,
				buttons = this.menuTabButton();

			for (i = 0; i < len; i++) {
				var tabStyle = window.getComputedStyle(tabs[i], null),
					left = i == 0 ? 0 : parseInt(tabStyle.marginLeft),
					right = parseInt(tabStyle.marginRight);

				tabWidth += tabs[i].offsetWidth + left + right;
			}

			tabBoxWidth -= buttons.left + buttons.right;

			if (tabWidth < tabBoxWidth) {
				buttons.leftEl.classList.add('hide2');
                buttons.rightEl.classList.add('hide2');
				$(self.tabFirstEl).animate({ marginLeft: 0 });
			}
		}
	};

	return menu;
}).call(this);

$(document).ready(function () {
	initAccordion(); // 初始化用户菜单
	tabMenu = new TabMenu();
});

$(function () {
	App.addPanel('欢迎进入系统', g_webAppName + '/welcome.jsp', 'welcome');
});