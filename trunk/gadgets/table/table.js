var GLOBAL_undefined = undefined,
    GLOBAL_parseInt = parseInt,
    GLOBAL_String = String,
    GLOBAL_Object = Object,
    GLOBAL_document = document,
    GLOBAL_Array = Array;

function SETPROP_className(b, c) {
    return b.className = c
}
function SETPROP_disabled(b, c) {
    return b.disabled = c
}
function SETPROP_currentTarget(b, c) {
    return b.currentTarget = c
}
function SETPROP_keyCode(b, c) {
    return b.keyCode = c
}
function SETPROP_type(b, c) {
    return b.type = c
}
function SETPROP_display(b, c) {
    return b.display = c
}
var $$PROP_appendChild = "appendChild",
    $$PROP_push = "push",
    $$PROP_length = "length",
    $$PROP_propertyIsEnumerable = "propertyIsEnumerable",
    $$PROP_prototype = "prototype",
    $$PROP_selectedIndex = "selectedIndex",
    $$PROP_className = "className",
    $$PROP_replace = "replace",
    $$PROP_split = "split",
    $$PROP_getElementById = "getElementById",
    $$PROP_createTextNode = "createTextNode",
    $$PROP_value = "value",
    $$PROP_insertBefore = "insertBefore",
    $$PROP_indexOf = "indexOf",
    $$PROP_style = "style",
    $$PROP_removeChild = "removeChild",
    $$PROP_call = "call",
    $$PROP_options = "options",
    $$PROP_keyCode = "keyCode",
    $$PROP_firstChild = "firstChild",
    $$PROP_forEach = "forEach",
    $$PROP_handleEvent = "handleEvent",
    $$PROP_type = "type",
    $$PROP_apply = "apply",
    $$PROP_childNodes = "childNodes",
    $$PROP_tagName = "tagName",
    $$PROP_parentNode = "parentNode",
    $$PROP_getValue = "getValue",
    $$PROP_nextSibling = "nextSibling",
    $$PROP_join = "join",
    $$PROP_nodeValue = "nodeValue",
    a, goog$global = this,
    goog$getObjectByName = function (b, c) {
        for (var d = b[$$PROP_split]("."), e = c || goog$global, f; f = d.shift();) if (e[f]) e = e[f];
        else return null;
        return e
    },
    goog$typeOf = function (b) {
        var c = typeof b;
        if (c == "object") if (b) {
            if (b instanceof GLOBAL_Array || !(b instanceof GLOBAL_Object) && GLOBAL_Object[$$PROP_prototype].toString[$$PROP_call](b) == "[object Array]" || typeof b[$$PROP_length] == "number" && typeof b.splice != "undefined" && typeof b[$$PROP_propertyIsEnumerable] != "undefined" && !b[$$PROP_propertyIsEnumerable]("splice")) return "array";
            if (!(b instanceof GLOBAL_Object) && (GLOBAL_Object[$$PROP_prototype].toString[$$PROP_call](b) == "[object Function]" || typeof b[$$PROP_call] != "undefined" && typeof b[$$PROP_propertyIsEnumerable] != "undefined" && !b[$$PROP_propertyIsEnumerable]("call"))) return "function"
        } else return "null";
        else if (c == "function" && typeof b[$$PROP_call] == "undefined") return "object";
        return c
    },
    goog$isArrayLike = function (b) {
        var c = goog$typeOf(b);
        return c == "array" || c == "object" && typeof b[$$PROP_length] == "number"
    },
    goog$isFunction = function (b) {
        return goog$typeOf(b) == "function"
    },
    goog$isObject = function (b) {
        var c = goog$typeOf(b);
        return c == "object" || c == "array" || c == "function"
    },
    goog$getHashCode = function (b) {
        if (b.hasOwnProperty && b.hasOwnProperty(goog$HASH_CODE_PROPERTY_$$constant)) return b[goog$HASH_CODE_PROPERTY_$$constant];
        b[goog$HASH_CODE_PROPERTY_$$constant] || (b[goog$HASH_CODE_PROPERTY_$$constant] = ++goog$hashCodeCounter_);
        return b[goog$HASH_CODE_PROPERTY_$$constant]
    },
    goog$HASH_CODE_PROPERTY_$$constant = "closure_hashCode_" + Math.floor(Math.random() * 2147483648).toString(36),
    goog$hashCodeCounter_ = 0,
    goog$cloneObject = function (b) {
        var c = goog$typeOf(b);
        if (c == "object" || c == "array") {
            if (b.clone) return b.clone[$$PROP_call](b);
            var d = c == "array" ? [] : {};
            for (var e in b) d[e] = goog$cloneObject(b[e]);
            return d
        }
        return b
    },
    goog$bind = function (b, c) {
        var d = b.boundArgs_;
        if (arguments[$$PROP_length] > 2) {
            var e = GLOBAL_Array[$$PROP_prototype].slice[$$PROP_call](arguments, 2);
            d && e.unshift[$$PROP_apply](e, d);
            d = e
        }
        c = b.boundSelf_ || c;
        b = b.boundFn_ || b;
        var f, g = c || goog$global;
        f = d ?
        function () {
            var h = GLOBAL_Array[$$PROP_prototype].slice[$$PROP_call](arguments);
            h.unshift[$$PROP_apply](h, d);
            return b[$$PROP_apply](g, h)
        } : function () {
            return b[$$PROP_apply](g, arguments)
        };
        f.boundArgs_ = d;
        f.boundSelf_ = c;
        f.boundFn_ = b;
        return f
    },
    goog$now = Date.now ||
    function () {
        return (new Date).getTime()
    },
    goog$inherits = function (b, c) {
        function d() {}
        d.prototype = c[$$PROP_prototype];
        b.superClass_ = c[$$PROP_prototype];
        b.prototype = new d;
        b[$$PROP_prototype].constructor = b
    };
var goog$array$forEach = function (b, c, d) {
    if (b[$$PROP_forEach]) b[$$PROP_forEach](c, d);
    else if (GLOBAL_Array[$$PROP_forEach]) GLOBAL_Array[$$PROP_forEach](b, c, d);
    else for (var e = b[$$PROP_length], f = typeof b == "string" ? b[$$PROP_split]("") : b, g = 0; g < e; g++) g in f && c[$$PROP_call](d, f[g], g, b)
},
    goog$array$clone = function (b) {
        if (goog$typeOf(b) == "array") return b.concat();
        else {
            for (var c = [], d = 0, e = b[$$PROP_length]; d < e; d++) c[d] = b[d];
            return c
        }
    };
var goog$object$forEach = function (b, c, d) {
    for (var e in b) c[$$PROP_call](d, b[e], e, b)
};
var goog$string$trim = function (b) {
    return b[$$PROP_replace](/^[\s\xa0]+|[\s\xa0]+$/g, "")
},
    goog$string$htmlEscape = function (b, c) {
        if (c) return b[$$PROP_replace](goog$string$amperRe_, "&amp;")[$$PROP_replace](goog$string$ltRe_, "&lt;")[$$PROP_replace](goog$string$gtRe_, "&gt;")[$$PROP_replace](goog$string$quotRe_, "&quot;");
        else {
            if (!goog$string$allRe_.test(b)) return b;
            if (b[$$PROP_indexOf]("&") != -1) b = b[$$PROP_replace](goog$string$amperRe_, "&amp;");
            if (b[$$PROP_indexOf]("<") != -1) b = b[$$PROP_replace](goog$string$ltRe_, "&lt;");
            if (b[$$PROP_indexOf](">") != -1) b = b[$$PROP_replace](goog$string$gtRe_, "&gt;");
            if (b[$$PROP_indexOf]('"') != -1) b = b[$$PROP_replace](goog$string$quotRe_, "&quot;");
            return b
        }
    },
    goog$string$amperRe_ = /&/g,
    goog$string$ltRe_ = /</g,
    goog$string$gtRe_ = />/g,
    goog$string$quotRe_ = /\"/g,
    goog$string$allRe_ = /[&<>\"]/,
    goog$string$contains = function (b, c) {
        return b[$$PROP_indexOf](c) != -1
    },
    goog$string$compareVersions = function (b, c) {
        for (var d = 0, e = goog$string$trim(GLOBAL_String(b))[$$PROP_split]("."), f = goog$string$trim(GLOBAL_String(c))[$$PROP_split]("."), g = Math.max(e[$$PROP_length], f[$$PROP_length]), h = 0; d == 0 && h < g; h++) {
            var i = e[h] || "",
                j = f[h] || "",
                k = new RegExp("(\\d*)(\\D*)", "g"),
                l = new RegExp("(\\d*)(\\D*)", "g");
            do {
                var n = k.exec(i) || ["", "", ""],
                    m = l.exec(j) || ["", "", ""];
                if (n[0][$$PROP_length] == 0 && m[0][$$PROP_length] == 0) break;
                var o = n[1][$$PROP_length] == 0 ? 0 : GLOBAL_parseInt(n[1], 10),
                    q = m[1][$$PROP_length] == 0 ? 0 : GLOBAL_parseInt(m[1], 10);
                d = goog$string$compareElements_(o, q) || goog$string$compareElements_(n[2][$$PROP_length] == 0, m[2][$$PROP_length] == 0) || goog$string$compareElements_(n[2], m[2])
            } while (d == 0)
        }
        return d
    },
    goog$string$compareElements_ = function (b, c) {
        if (b < c) return -1;
        else if (b > c) return 1;
        return 0
    },
    goog$string$hashCode = function (b) {
        for (var c = 0, d = 0; d < b[$$PROP_length]; ++d) {
            c = 31 * c + b.charCodeAt(d);
            c %= 4294967296
        }
        return c
    };
goog$now();
var goog$userAgent$detectedOpera_, goog$userAgent$detectedIe_, goog$userAgent$detectedWebkit_, goog$userAgent$detectedMobile_, goog$userAgent$detectedGecko_, goog$userAgent$detectedCamino_, goog$userAgent$detectedMac_, goog$userAgent$detectedWindows_, goog$userAgent$detectedLinux_, goog$userAgent$detectedX11_, goog$userAgent$getUserAgentString = function () {
    return goog$global.navigator ? goog$global.navigator.userAgent : null
},
    goog$userAgent$getNavigator = function () {
        return goog$global.navigator
    };
goog$userAgent$detectedCamino_ = goog$userAgent$detectedGecko_ = goog$userAgent$detectedMobile_ = goog$userAgent$detectedWebkit_ = goog$userAgent$detectedIe_ = goog$userAgent$detectedOpera_ = false;
var JSCompiler_inline_ua_2;
if (JSCompiler_inline_ua_2 = goog$userAgent$getUserAgentString()) {
    var JSCompiler_inline_navigator$$1_3 = goog$userAgent$getNavigator();
    goog$userAgent$detectedOpera_ = JSCompiler_inline_ua_2[$$PROP_indexOf]("Opera") == 0;
    goog$userAgent$detectedIe_ = !goog$userAgent$detectedOpera_ && JSCompiler_inline_ua_2[$$PROP_indexOf]("MSIE") != -1;
    goog$userAgent$detectedMobile_ = (goog$userAgent$detectedWebkit_ = !goog$userAgent$detectedOpera_ && JSCompiler_inline_ua_2[$$PROP_indexOf]("WebKit") != -1) && JSCompiler_inline_ua_2[$$PROP_indexOf]("Mobile") != -1;
    goog$userAgent$detectedCamino_ = (goog$userAgent$detectedGecko_ = !goog$userAgent$detectedOpera_ && !goog$userAgent$detectedWebkit_ && JSCompiler_inline_navigator$$1_3.product == "Gecko") && JSCompiler_inline_navigator$$1_3.vendor == "Camino"
}
var goog$userAgent$OPERA$$constant = goog$userAgent$detectedOpera_,
    goog$userAgent$IE$$constant = goog$userAgent$detectedIe_,
    goog$userAgent$GECKO$$constant = goog$userAgent$detectedGecko_,
    goog$userAgent$WEBKIT$$constant = goog$userAgent$detectedWebkit_,
    goog$userAgent$PLATFORM$$constant, JSCompiler_inline_navigator$$2_6 = goog$userAgent$getNavigator();
goog$userAgent$PLATFORM$$constant = JSCompiler_inline_navigator$$2_6 && JSCompiler_inline_navigator$$2_6.platform || "";
goog$userAgent$detectedMac_ = goog$string$contains(goog$userAgent$PLATFORM$$constant, "Mac");
goog$userAgent$detectedWindows_ = goog$string$contains(goog$userAgent$PLATFORM$$constant, "Win");
goog$userAgent$detectedLinux_ = goog$string$contains(goog$userAgent$PLATFORM$$constant, "Linux");
goog$userAgent$detectedX11_ = !! goog$userAgent$getNavigator() && goog$string$contains(goog$userAgent$getNavigator().appVersion || "", "X11");
var goog$userAgent$VERSION$$constant, JSCompiler_inline_version$$2_13 = "",
    JSCompiler_inline_re$$2_14;
if (goog$userAgent$OPERA$$constant && goog$global.opera) {
    var JSCompiler_inline_operaVersion_15 = goog$global.opera.version;
    JSCompiler_inline_version$$2_13 = typeof JSCompiler_inline_operaVersion_15 == "function" ? JSCompiler_inline_operaVersion_15() : JSCompiler_inline_operaVersion_15
} else {
    if (goog$userAgent$GECKO$$constant) JSCompiler_inline_re$$2_14 = /rv\:([^\);]+)(\)|;)/;
    else if (goog$userAgent$IE$$constant) JSCompiler_inline_re$$2_14 = /MSIE\s+([^\);]+)(\)|;)/;
    else if (goog$userAgent$WEBKIT$$constant) JSCompiler_inline_re$$2_14 = /WebKit\/(\S+)/;
    if (JSCompiler_inline_re$$2_14) {
        var JSCompiler_inline_arr$$32_16 = JSCompiler_inline_re$$2_14.exec(goog$userAgent$getUserAgentString());
        JSCompiler_inline_version$$2_13 = JSCompiler_inline_arr$$32_16 ? JSCompiler_inline_arr$$32_16[1] : ""
    }
}
goog$userAgent$VERSION$$constant = JSCompiler_inline_version$$2_13;
var goog$userAgent$isVersionCache_ = {};
var goog$dom$defaultDomHelper_;
var goog$dom$setProperties = function (b, c) {
    goog$object$forEach(c, function (d, e) {
        if (e == "style") b[$$PROP_style].cssText = d;
        else if (e == "class") SETPROP_className(b, d);
        else if (e == "for") b.htmlFor = d;
        else if (e in goog$dom$DIRECT_ATTRIBUTE_MAP_$$constant) b.setAttribute(goog$dom$DIRECT_ATTRIBUTE_MAP_$$constant[e], d);
        else b[e] = d
    })
},
    goog$dom$DIRECT_ATTRIBUTE_MAP_$$constant = {
        cellpadding: "cellPadding",
        cellspacing: "cellSpacing",
        colspan: "colSpan",
        rowspan: "rowSpan",
        valign: "vAlign",
        height: "height",
        width: "width",
        usemap: "useMap",
        frameborder: "frameBorder",
        type: "type"
    },
    goog$dom$createDom_ = function (b, c) {
        var d = c[0],
            e = c[1];
        if (goog$userAgent$IE$$constant && e && (e.name || e[$$PROP_type])) {
            var f = ["<", d];
            e.name && f[$$PROP_push](' name="', goog$string$htmlEscape(e.name), '"');
            if (e[$$PROP_type]) {
                f[$$PROP_push](' type="', goog$string$htmlEscape(e[$$PROP_type]), '"');
                e = goog$cloneObject(e);
                delete e[$$PROP_type]
            }
            f[$$PROP_push](">");
            d = f[$$PROP_join]("")
        }
        var g = b.createElement(d);
        if (e) if (typeof e == "string") SETPROP_className(g, e);
        else goog$dom$setProperties(g, e);
        if (c[$$PROP_length] > 2) {
            function h(k) {
                if (k) g[$$PROP_appendChild](typeof k == "string" ? b[$$PROP_createTextNode](k) : k)
            }
            for (var i = 2; i < c[$$PROP_length]; i++) {
                var j = c[i];
                goog$isArrayLike(j) && !(goog$isObject(j) && j.nodeType > 0) ? goog$array$forEach(goog$dom$isNodeList(j) ? goog$array$clone(j) : j, h) : h(j)
            }
        }
        return g
    },
    goog$dom$appendChild = function (b, c) {
        b[$$PROP_appendChild](c)
    },
    goog$dom$removeChildren = function (b) {
        for (var c; c = b[$$PROP_firstChild];) b[$$PROP_removeChild](c)
    },
    goog$dom$insertSiblingBefore = function (b, c) {
        c[$$PROP_parentNode] && c[$$PROP_parentNode][$$PROP_insertBefore](b, c)
    },
    goog$dom$insertSiblingAfter = function (b, c) {
        c[$$PROP_parentNode] && c[$$PROP_parentNode][$$PROP_insertBefore](b, c[$$PROP_nextSibling])
    },
    goog$dom$removeNode = function (b) {
        return b && b[$$PROP_parentNode] ? b[$$PROP_parentNode][$$PROP_removeChild](b) : null
    },
    goog$dom$replaceNode = function (b, c) {
        var d = c[$$PROP_parentNode];
        d && d.replaceChild(b, c)
    },
    goog$dom$BAD_CONTAINS_WEBKIT_$$constant = goog$userAgent$WEBKIT$$constant && (goog$userAgent$isVersionCache_["522"] || (goog$userAgent$isVersionCache_["522"] =
    goog$string$compareVersions(goog$userAgent$VERSION$$constant, "522") >= 0)),
    goog$dom$contains = function (b, c) {
        if (typeof b.contains != "undefined" && !goog$dom$BAD_CONTAINS_WEBKIT_$$constant && c.nodeType == 1) return b == c || b.contains(c);
        if (typeof b.compareDocumentPosition != "undefined") return b == c || Boolean(b.compareDocumentPosition(c) & 16);
        for (; c && b != c;) c = c[$$PROP_parentNode];
        return c == b
    },
    goog$dom$isNodeList = function (b) {
        if (b && typeof b[$$PROP_length] == "number") if (goog$isObject(b)) return typeof b.item == "function" || typeof b.item == "string";
        else if (goog$isFunction(b)) return typeof b.item == "function";
        return false
    },
    goog$dom$DomHelper = function (b) {
        this.document_ = b || goog$global.document || GLOBAL_document
    };
a = goog$dom$DomHelper[$$PROP_prototype];
a.createDom = function () {
    return goog$dom$createDom_(this.document_, arguments)
};
a.createElement = function (b) {
    return this.document_.createElement(b)
};
a.createTextNode = function (b) {
    return this.document_[$$PROP_createTextNode](b)
};
a.appendChild = goog$dom$appendChild;
a.removeChildren = goog$dom$removeChildren;
a.insertSiblingBefore = goog$dom$insertSiblingBefore;
a.insertSiblingAfter = goog$dom$insertSiblingAfter;
a.removeNode = goog$dom$removeNode;
a.replaceNode = goog$dom$replaceNode;
a.contains = goog$dom$contains;
var goog$Disposable = function () {};
goog$Disposable[$$PROP_prototype].disposed_ = false;
goog$Disposable[$$PROP_prototype].dispose = function () {
    if (!this.disposed_) {
        this.disposed_ = true;
        this.disposeInternal()
    }
};
goog$Disposable[$$PROP_prototype].disposeInternal = function () {};
var goog$structs$SimplePool = function (b, c) {
    goog$Disposable[$$PROP_call](this);
    this.maxCount_ = c;
    this.freeQueue_ = [];
    this.createInitial_(b)
};
goog$inherits(goog$structs$SimplePool, goog$Disposable);
a = goog$structs$SimplePool[$$PROP_prototype];
a.createObjectFn_ = null;
a.disposeObjectFn_ = null;
a.setCreateObjectFn = function (b) {
    this.createObjectFn_ = b
};
a.setDisposeObjectFn = function (b) {
    this.disposeObjectFn_ = b
};
a.getObject = function () {
    if (this.freeQueue_[$$PROP_length]) return this.freeQueue_.pop();
    return this.createObject()
};
a.releaseObject = function (b) {
    this.freeQueue_[$$PROP_length] < this.maxCount_ ? this.freeQueue_[$$PROP_push](b) : this.disposeObject(b)
};
a.createInitial_ = function (b) {
    if (b > this.maxCount_) throw Error("[goog.structs.SimplePool] Initial cannot be greater than max");
    for (var c = 0; c < b; c++) this.freeQueue_[$$PROP_push](this.createObject())
};
a.createObject = function () {
    return this.createObjectFn_ ? this.createObjectFn_() : {}
};
a.disposeObject = function (b) {
    if (this.disposeObjectFn_) this.disposeObjectFn_(b);
    else if (goog$isFunction(b.dispose)) b.dispose();
    else for (var c in b) delete b[c]
};
a.disposeInternal = function () {
    goog$structs$SimplePool.superClass_.disposeInternal[$$PROP_call](this);
    for (var b = this.freeQueue_; b[$$PROP_length];) this.disposeObject(b.pop());
    delete this.freeQueue_
};
var goog$events$Event = function (b, c) {
    SETPROP_type(this, b);
    this.target = c;
    SETPROP_currentTarget(this, this.target)
};
goog$inherits(goog$events$Event, goog$Disposable);
a = goog$events$Event[$$PROP_prototype];
a.disposeInternal = function () {
    delete this[$$PROP_type];
    delete this.target;
    delete this.currentTarget
};
a.propagationStopped_ = false;
a.returnValue_ = true;
a.stopPropagation = function () {
    this.propagationStopped_ = true
};
a.preventDefault = function () {
    this.returnValue_ = false
};
var goog$events$BrowserEvent = function (b, c) {
    b && this.init(b, c)
};
goog$inherits(goog$events$BrowserEvent, goog$events$Event);
a = goog$events$BrowserEvent[$$PROP_prototype];
SETPROP_type(a, null);
a.target = null;
a.relatedTarget = null;
a.offsetX = 0;
a.offsetY = 0;
a.clientX = 0;
a.clientY = 0;
a.screenX = 0;
a.screenY = 0;
a.button = 0;
SETPROP_keyCode(a, 0);
a.charCode = 0;
a.ctrlKey = false;
a.altKey = false;
a.shiftKey = false;
a.metaKey = false;
a.event_ = null;
a.init = function (b, c) {
    SETPROP_type(this, b[$$PROP_type]);
    this.target = b.target || b.srcElement;
    SETPROP_currentTarget(this, c);
    this.relatedTarget = b.relatedTarget ? b.relatedTarget : this[$$PROP_type] == "mouseover" ? b.fromElement : this[$$PROP_type] == "mouseout" ? b.toElement : null;
    this.offsetX = typeof b.layerX == "number" ? b.layerX : b.offsetX;
    this.offsetY = typeof b.layerY == "number" ? b.layerY : b.offsetY;
    this.clientX = typeof b.clientX == "number" ? b.clientX : b.pageX;
    this.clientY = typeof b.clientY == "number" ? b.clientY : b.pageY;
    this.screenX =
    b.screenX || 0;
    this.screenY = b.screenY || 0;
    this.button = b.button;
    SETPROP_keyCode(this, b[$$PROP_keyCode] || 0);
    this.charCode = b.charCode || (this[$$PROP_type] == "keypress" ? b[$$PROP_keyCode] : 0);
    this.ctrlKey = b.ctrlKey;
    this.altKey = b.altKey;
    this.shiftKey = b.shiftKey;
    this.metaKey = b.metaKey;
    this.event_ = b;
    delete this.returnValue_;
    delete this.propagationStopped_
};
a.stopPropagation = function () {
    this.propagationStopped_ = true;
    if (this.event_.stopPropagation) this.event_.stopPropagation();
    else this.event_.cancelBubble = true
};
a.preventDefault = function () {
    this.returnValue_ = false;
    if (this.event_.preventDefault) this.event_.preventDefault();
    else {
        this.event_.returnValue = false;
        try {
            SETPROP_keyCode(this.event_, -1)
        } catch (b) {}
    }
};
a.disposeInternal = function () {
    goog$events$BrowserEvent.superClass_.disposeInternal[$$PROP_call](this);
    this.event_ = null
};
var goog$events$Listener = function () {},
    goog$events$Listener$counter_ = 0;
a = goog$events$Listener[$$PROP_prototype];
a.isFunctionListener_ = null;
a.listener = null;
a.proxy = null;
a.src = null;
SETPROP_type(a, null);
a.capture = null;
a.handler = null;
a.key = 0;
a.removed = false;
a.callOnce = false;
a.init = function (b, c, d, e, f, g) {
    if (goog$isFunction(b)) this.isFunctionListener_ = true;
    else if (b && b[$$PROP_handleEvent] && goog$isFunction(b[$$PROP_handleEvent])) this.isFunctionListener_ = false;
    else throw Error("Invalid listener argument");
    this.listener = b;
    this.proxy = c;
    this.src = d;
    SETPROP_type(this, e);
    this.capture = !! f;
    this.handler = g;
    this.callOnce = false;
    this.key = ++goog$events$Listener$counter_;
    this.removed = false
};
a.handleEvent = function (b) {
    if (this.isFunctionListener_) return this.listener[$$PROP_call](this.handler || this.src, b);
    return this.listener[$$PROP_handleEvent][$$PROP_call](this.listener, b)
};
var goog$events$listeners_ = {},
    goog$events$listenerTree_ = {},
    goog$events$sources_ = {},
    goog$events$objectPool_ = new goog$structs$SimplePool(0, 600);
goog$events$objectPool_.setCreateObjectFn(function () {
    return {
        count_: 0,
        remaining_: 0
    }
});
goog$events$objectPool_.setDisposeObjectFn(function (b) {
    b.count_ = 0
});
var goog$events$arrayPool_ = new goog$structs$SimplePool(0, 600);
goog$events$arrayPool_.setCreateObjectFn(function () {
    return []
});
goog$events$arrayPool_.setDisposeObjectFn(function (b) {
    b.length = 0;
    delete b.locked_;
    delete b.needsCleanup_
});
var goog$events$handleEventProxyPool_ = new goog$structs$SimplePool(0, 600);
goog$events$handleEventProxyPool_.setCreateObjectFn(function () {
    var b = function (c) {
        return goog$events$handleBrowserEvent_[$$PROP_call](b.src, b.key, c)
    };
    return b
});
var goog$events$createListenerFunction_ = function () {
    return new goog$events$Listener
},
    goog$events$listenerPool_ = new goog$structs$SimplePool(0, 600);
goog$events$listenerPool_.setCreateObjectFn(goog$events$createListenerFunction_);
var goog$events$createEventFunction_ = function () {
    return new goog$events$BrowserEvent
},
    goog$events$eventPool_, JSCompiler_inline_eventPool_19 = null;
if (goog$userAgent$IE$$constant) {
    JSCompiler_inline_eventPool_19 = new goog$structs$SimplePool(0, 600);
    JSCompiler_inline_eventPool_19.setCreateObjectFn(goog$events$createEventFunction_)
}
goog$events$eventPool_ = JSCompiler_inline_eventPool_19;
var goog$events$onString_ = "on",
    goog$events$onStringMap_ = {},
    goog$events$listen = function (b, c, d, e, f) {
        if (c) if (goog$typeOf(c) == "array") {
            for (var g = 0; g < c[$$PROP_length]; g++) goog$events$listen(b, c[g], d, e, f);
            return null
        } else {
            var h = !! e,
                i = goog$events$listenerTree_;
            c in i || (i[c] = goog$events$objectPool_.getObject());
            i = i[c];
            if (!(h in i)) {
                i[h] = goog$events$objectPool_.getObject();
                i.count_++
            }
            i = i[h];
            var j = goog$getHashCode(b),
                k, l;
            i.remaining_++;
            if (i[j]) {
                k = i[j];
                for (g = 0; g < k[$$PROP_length]; g++) {
                    l = k[g];
                    if (l.listener == d && l.handler == f) {
                        if (l.removed) break;
                        return k[g].key
                    }
                }
            } else {
                k = i[j] = goog$events$arrayPool_.getObject();
                i.count_++
            }
            var n = goog$events$handleEventProxyPool_.getObject();
            n.src = b;
            l = goog$events$listenerPool_.getObject();
            l.init(d, n, b, c, h, f);
            var m = l.key;
            n.key = m;
            k[$$PROP_push](l);
            goog$events$listeners_[m] = l;
            goog$events$sources_[j] || (goog$events$sources_[j] = goog$events$arrayPool_.getObject());
            goog$events$sources_[j][$$PROP_push](l);
            if (b.addEventListener) {
                if (b == goog$global || !b.customEvent_) b.addEventListener(c, n, h)
            } else b.attachEvent(goog$events$getOnString_(c), n);
            return m
        } else throw Error("Invalid event type");
    },
    goog$events$unlistenByKey = function (b) {
        if (!goog$events$listeners_[b]) return false;
        var c = goog$events$listeners_[b];
        if (c.removed) return false;
        var d = c.src,
            e = c[$$PROP_type],
            f = c.proxy,
            g = c.capture;
        if (d.removeEventListener) {
            if (d == goog$global || !d.customEvent_) d.removeEventListener(e, f, g)
        } else d.detachEvent && d.detachEvent(goog$events$getOnString_(e), f);
        var h = goog$getHashCode(d),
            i = goog$events$listenerTree_[e][g][h];
        if (goog$events$sources_[h]) {
            var j = goog$events$sources_[h],
                k;
            a: if (j[$$PROP_indexOf]) k = j[$$PROP_indexOf](c, GLOBAL_undefined);
            else if (GLOBAL_Array[$$PROP_indexOf]) k = GLOBAL_Array[$$PROP_indexOf](j, c, GLOBAL_undefined);
            else {
                for (var l = 0, n = l; n < j[$$PROP_length]; n++) if (n in j && j[n] === c) {
                    k = n;
                    break a
                }
                k = -1
            }
            k != -1 && GLOBAL_Array[$$PROP_prototype].splice[$$PROP_call](j, k, 1)[$$PROP_length] == 1;j[$$PROP_length] == 0 && delete goog$events$sources_[h]
        }
        c.removed = true;
        i.needsCleanup_ = true;
        goog$events$cleanUp_(e, g, h, i);
        delete goog$events$listeners_[b];
        return true
    },
    goog$events$cleanUp_ = function (b, c, d, e) {
        if (!e.locked_) if (e.needsCleanup_) {
            for (var f = 0, g = 0; f < e[$$PROP_length]; f++) if (e[f].removed) goog$events$listenerPool_.releaseObject(e[f]);
            else {
                if (f != g) e[g] = e[f];
                g++
            }
            e.length = g;
            e.needsCleanup_ = false;
            if (g == 0) {
                goog$events$arrayPool_.releaseObject(e);
                delete goog$events$listenerTree_[b][c][d];
                goog$events$listenerTree_[b][c].count_--;
                if (goog$events$listenerTree_[b][c].count_ == 0) {
                    goog$events$objectPool_.releaseObject(goog$events$listenerTree_[b][c]);
                    delete goog$events$listenerTree_[b][c];
                    goog$events$listenerTree_[b].count_--
                }
                if (goog$events$listenerTree_[b].count_ == 0) {
                    goog$events$objectPool_.releaseObject(goog$events$listenerTree_[b]);
                    delete goog$events$listenerTree_[b]
                }
            }
        }
    },
    goog$events$getOnString_ = function (b) {
        if (b in goog$events$onStringMap_) return goog$events$onStringMap_[b];
        return goog$events$onStringMap_[b] = goog$events$onString_ + b
    },
    goog$events$fireListeners_ = function (b, c, d, e, f) {
        var g = 1,
            h = goog$getHashCode(c);
        if (b[h]) {
            b.remaining_--;
            var i = b[h];
            if (i.locked_) i.locked_++;
            else i.locked_ = 1;
            try {
                for (var j = i[$$PROP_length], k = 0; k < j; k++) {
                    var l = i[k];
                    if (l && !l.removed) g &= goog$events$fireListener(l, f) !== false
                }
            } finally {
                i.locked_--;
                goog$events$cleanUp_(d, e, h, i)
            }
        }
        return Boolean(g)
    },
    goog$events$fireListener = function (b, c) {
        var d = b[$$PROP_handleEvent](c);
        b.callOnce && goog$events$unlistenByKey(b.key);
        return d
    },
    goog$events$handleBrowserEvent_ = function (b, c) {
        if (!goog$events$listeners_[b]) return true;
        var d = goog$events$listeners_[b],
            e = d[$$PROP_type],
            f = goog$events$listenerTree_;
        if (!(e in f)) return true;
        f = f[e];
        var g, h;
        if (goog$userAgent$IE$$constant) {
            var i = c || goog$getObjectByName("window.event"),
                j = true in f,
                k = false in f;
            if (j) {
                if (i[$$PROP_keyCode] < 0 || i.returnValue != GLOBAL_undefined) return true;
                a: {
                    var l = false;
                    if (i[$$PROP_keyCode] == 0) try {
                        SETPROP_keyCode(i, -1);
                        break a
                    } catch (n) {
                        l = true
                    }
                    if (l || i.returnValue == GLOBAL_undefined) i.returnValue = true
                }
            }
            var m = goog$events$eventPool_.getObject();
            m.init(i, this);
            g = true;
            try {
                if (j) {
                    for (var o = goog$events$arrayPool_.getObject(), q = m.currentTarget; q; q = q[$$PROP_parentNode]) o[$$PROP_push](q);
                    h = f[true];
                    h.remaining_ = h.count_;
                    for (var p = o[$$PROP_length] - 1; !m.propagationStopped_ && p >= 0 && h.remaining_; p--) {
                        SETPROP_currentTarget(m, o[p]);
                        g &= goog$events$fireListeners_(h, o[p], e, true, m)
                    }
                    if (k) {
                        h = f[false];
                        h.remaining_ = h.count_;
                        for (p = 0; !m.propagationStopped_ && p < o[$$PROP_length] && h.remaining_; p++) {
                            SETPROP_currentTarget(m, o[p]);
                            g &= goog$events$fireListeners_(h, o[p], e, false, m)
                        }
                    }
                } else g = goog$events$fireListener(d, m)
            } finally {
                if (o) {
                    o.length = 0;
                    goog$events$arrayPool_.releaseObject(o)
                }
                m.dispose();
                goog$events$eventPool_.releaseObject(m)
            }
            return g
        }
        var r =
        new goog$events$BrowserEvent(c, this);
        try {
            g = goog$events$fireListener(d, r)
        } finally {
            r.dispose()
        }
        return g
    };
var gviz$ui$TableController = function (b) {
    this.visibilityTable_ = b;
    this.selectedFilterValues_ = [];
    this.hiddenGroupingRows_ = {}
};
a = gviz$ui$TableController[$$PROP_prototype];
a.getSelectedFilterValues = function () {
    return this.selectedFilterValues_
};
a.getHiddenGroupingRows = function () {
    return this.hiddenGroupingRows_
};
a.filterRows = function () {
    var b = GLOBAL_document[$$PROP_getElementById]("dropDownRow"),
        c = this.getAllSelectedValues_(b);
    this.selectedFilterValues_ = c;
    for (var d = b[$$PROP_nextSibling]; d;) {
        if (d[$$PROP_tagName] == "TR") {
            var e = true;
            if (!(d.id[$$PROP_split]("_")[0] == "gtrow")) for (var f = 0; f < c[$$PROP_length]; f++) if (c[f] && c[f] != "__all__") {
                var g = this.getValueInColumn_(d, f);
                if (g != c[f]) {
                    e = false;
                    break
                }
            }
            this.updateRowDisplayVisibility_(d, gviz$ui$TableController$getRowNumber_(d), e, 0)
        }
        d = d[$$PROP_nextSibling]
    }
    this.hideGroupingRowsAfterFilter_(b)
};
a.hideGroupingRowsAfterFilter_ = function (b) {
    var c = [],
        d = 0,
        e = 0,
        f = b[$$PROP_nextSibling];
    if (f && gviz$ui$TableController$getRowDepth_(f) == 0) f = f[$$PROP_nextSibling];
    for (var g; f;) {
        if (f[$$PROP_tagName] == "TR") if (f.id[$$PROP_split]("_")[0] == "gtrow") {
            for (e = gviz$ui$TableController$getRowDepth_(f); g = c.pop();) {
                d = gviz$ui$TableController$getRowDepth_(g);
                if (d >= e) this.updateRowDisplayVisibility_(g, gviz$ui$TableController$getRowNumber_(g), false, 0);
                else break
            }
            g && c[$$PROP_push](g);
            c[$$PROP_push](f);
            gviz$ui$TableController$getRowDepth_(f)
        } else if (f[$$PROP_style].display != "none") {
            for (var h = 0; h < c[$$PROP_length]; h++) {
                g = c[h];
                this.updateRowDisplayVisibility_(g, gviz$ui$TableController$getRowNumber_(g), true, 0)
            }
            c = []
        }
        f = f[$$PROP_nextSibling]
    }
    for (; g = c.pop();) {
        gviz$ui$TableController$getRowDepth_(g);
        this.updateRowDisplayVisibility_(g, gviz$ui$TableController$getRowNumber_(g), false, 0)
    }
};
a.getAllSelectedValues_ = function (b) {
    for (var c = b[$$PROP_firstChild], d, e = []; c;) {
        c[$$PROP_tagName] == "TD" && (d = c[$$PROP_firstChild]) && d[$$PROP_className] == "f-cell-select" && d[$$PROP_options] ? e[$$PROP_push](d[$$PROP_options][d[$$PROP_selectedIndex]][$$PROP_value]) : e[$$PROP_push](false);
        c = c[$$PROP_nextSibling]
    }
    return e
};
a.getValueInColumn_ = function (b, c) {
    var d = null,
        e, f;
    if (b && b[$$PROP_tagName] == "TR" && (e = b[$$PROP_childNodes][c]) && (f = e[$$PROP_firstChild])) if (f[$$PROP_nodeValue]) d = f[$$PROP_nodeValue];
    else if (f[$$PROP_tagName] == "SPAN") d = f[$$PROP_firstChild][$$PROP_nodeValue];
    else if (f[$$PROP_tagName] == "TABLE") d = f[$$PROP_firstChild][$$PROP_firstChild][$$PROP_childNodes][1][$$PROP_firstChild][$$PROP_nodeValue];
    return d
};
a.hideOrShowRows = function (b, c, d, e) {
    var f = GLOBAL_document[$$PROP_getElementById](b),
        g, h = GLOBAL_document[$$PROP_getElementById](c),
        i = d == e,
        j = h[$$PROP_className] == "icon-cell-op",
        k, l = this.getRowHash_(f);
    if (j) {
        SETPROP_className(h, "icon-cell-cl");
        this.hiddenGroupingRows_[l] = true
    } else {
        SETPROP_className(h, "icon-cell-op");
        this.hiddenGroupingRows_[l] = GLOBAL_undefined
    }
    for (; f = gviz$ui$TableController$getNextRow_(f);) {
        var n = gviz$ui$TableController$getRowDepth_(f);
        if (n == -1 || n > d) {
            if (j) k = false;
            else if (i) k = true;
            else if (n == d + 1) {
                k = true;
                var m = f[$$PROP_firstChild][$$PROP_firstChild][$$PROP_firstChild][$$PROP_firstChild][$$PROP_childNodes][2];
                if (m && m[$$PROP_className]) {
                    SETPROP_className(m, "icon-cell-cl");
                    l = this.getRowHash_(f);
                    this.hiddenGroupingRows_[l] = true
                }
            } else k = false;
            k && !i ? this.showRow_(f, gviz$ui$TableController$getRowNumber_(f)) : this.updateRowDisplayVisibility_(f, gviz$ui$TableController$getRowNumber_(f), k, 1)
        } else {
            g = f;
            f = null
        }
    }
    return g
};
a.refreshTableVisibility = function (b, c, d) {
    var e = GLOBAL_document[$$PROP_getElementById]("dropDownRow"),
        f = e[$$PROP_nextSibling],
        g;
    if (b) for (; f;) {
        g = this.getRowHash_(f);
        f = b[g] ? this.hideOrShowRows(f.id, f[$$PROP_firstChild][$$PROP_firstChild][$$PROP_firstChild][$$PROP_firstChild][$$PROP_childNodes][2].id, gviz$ui$TableController$getRowDepth_(f), d) : gviz$ui$TableController$getNextRow_(f)
    }
    if (c) for (var h = e[$$PROP_firstChild] ? e[$$PROP_firstChild] : null, i = 0, j, k, l, n = true; h;) {
        if (h[$$PROP_tagName] == "TD" && (j = h[$$PROP_firstChild]) && j[$$PROP_options]) if ((l = c[i]) && l != "__all__") {
            n = false;
            a: {
                var m = j[$$PROP_options],
                    o = 0;
                if (m) for (o = 0; o < m[$$PROP_length]; o++) if (m[o][$$PROP_value] == l) {
                    k = o;
                    break a
                }
                k = -1
            }
            if (k >= 0) {
                j[$$PROP_options][0].selected = false;
                j[$$PROP_options][k].selected = true;
                j.selectedIndex = k
            }
        }
        h = h[$$PROP_nextSibling];
        i++
    }
    n || this.filterRows()
};
var gviz$ui$TableController$getNextRow_ = function (b) {
    for (; b && b[$$PROP_nextSibling];) {
        b = b[$$PROP_nextSibling];
        if (b[$$PROP_tagName] == "TR") return b
    }
    return null
};
gviz$ui$TableController[$$PROP_prototype].getRowHash_ = function (b) {
    var c = gviz$ui$TableController$getRowNumber_(b);
    return this.visibilityTable_[c][2]
};
gviz$ui$TableController[$$PROP_prototype].updateRowDisplayVisibility_ = function (b, c, d, e) {
    var f;
    this.visibilityTable_[c][e] = d;
    if (this.visibilityTable_[c][1] && this.visibilityTable_[c][0]) {
        SETPROP_display(b[$$PROP_style], "");
        f = true
    } else {
        SETPROP_display(b[$$PROP_style], "none");
        f = false
    }
    return f
};
gviz$ui$TableController[$$PROP_prototype].showRow_ = function (b, c) {
    this.visibilityTable_[c][1] = true;
    this.visibilityTable_[c][0] = true;
    SETPROP_display(b[$$PROP_style], "")
};
var gviz$ui$TableController$getRowNumber_ = function (b) {
    return GLOBAL_parseInt(b.id[$$PROP_split]("_")[1], 10)
},
    gviz$ui$TableController$getRowDepth_ = function (b) {
        return GLOBAL_parseInt(b.id[$$PROP_split]("_")[2], 10)
    };
var gviz$ui$ConfigurationColumnInfo = function (b, c, d) {
    this.colId_ = b;
    this.colLabel_ = c;
    this.type_ = d
};
gviz$ui$ConfigurationColumnInfo[$$PROP_prototype].getId = function () {
    return this.colId_
};
gviz$ui$ConfigurationColumnInfo[$$PROP_prototype].getDisplayName = function () {
    return this.colLabel_ ? this.colLabel_ : this.colId_
};
gviz$ui$ConfigurationColumnInfo[$$PROP_prototype].getAggregationTypes = function () {
    var b;
    switch (this.type_) {
    case "timeofday":
    case "date":
    case "datetime":
        b = ["none", "min", "max", "count"];
        break;
    case "number":
        b = ["none", "sum", "avg", "min", "max", "count"];
        break;
    case "boolean":
    case "string":
        b = ["none", "count"];
        break
    }
    return b
};
var gviz$ui$ConfigurationDialogController = function (b, c, d, e) {
    this.allColumnsString_ = d;
    this.groupByPref_ = b;
    this.aggregateByPref_ = c;
    this.groupByColumns_ = gviz$ui$ConfigurationDialogController$parseGroupByColumns_(b);
    this.aggregationColumns_ = gviz$ui$ConfigurationDialogController$parseAggregationColumns_(c);
    this.allColumnsInfo_ = gviz$ui$ConfigurationDialogController$parseAllColumnsInfo_(d);
    this.messages_ = e.getMessages();
    this.tableGadgetManager_ = e;
    this.configurationDialogSelectGroupingDiv_ = GLOBAL_document[$$PROP_getElementById]("cfg-first-section-div");
    this.configurationDialogSelectAggregationDiv_ = GLOBAL_document[$$PROP_getElementById]("cfg-second-section-div");
    this.allColumnsTable_ = GLOBAL_document[$$PROP_getElementById](gviz$ui$ConfigurationDialogController$ConfigurationId.ALL_COLUMNS_TABLE);
    this.groupingColumnsTable_ = GLOBAL_document[$$PROP_getElementById](gviz$ui$ConfigurationDialogController$ConfigurationId.GROUPING_COLUMNS_TABLE);
    this.allColumnsTable_ = GLOBAL_document[$$PROP_getElementById](gviz$ui$ConfigurationDialogController$ConfigurationId.AGGREGATION_COLUMNS_TABLE);
    this.dom_ = goog$dom$defaultDomHelper_ || (goog$dom$defaultDomHelper_ = new goog$dom$DomHelper)
};
gviz$ui$ConfigurationDialogController[$$PROP_prototype].storePrefs = function (b, c, d) {
    this.allColumnsString_ = b;
    this.groupByPref_ = c;
    this.aggregateByPref_ = d
};
var gviz$ui$ConfigurationDialogController$parseToTokensBySpaces_ = function (b) {
    var c = [],
        d = [],
        e, f = b == null ? "" : GLOBAL_String(b);
    if (e = /^[\s\xa0]*$/.test(f)) return c;
    for (var g = b[$$PROP_length], h = 0, i, j = 0; h < g;) {
        i = b.charAt(h);
        switch (j) {
        case 1:
            if (i != "`") d[$$PROP_push](i);
            else j = 0;
            break;
        case 2:
            if (i == "`") j = 1;
            else if (i != " ") {
                j = 0;
                d[$$PROP_push](i)
            }
            break;
        case 0:
            if (i == "`") j = 1;
            else if (i == " ") {
                j = 2;
                if (d[$$PROP_length] > 0) {
                    c[$$PROP_push](d[$$PROP_join](""));
                    d = []
                }
            } else d[$$PROP_push](i)
        }
        h++
    }
    d[$$PROP_length] > 0 && c[$$PROP_push](d[$$PROP_join](""));
    return c
},
    gviz$ui$ConfigurationDialogController$parseGroupByColumns_ = function (b) {
        var c = {};
        if (b) for (var d = gviz$ui$ConfigurationDialogController$parseToTokensBySpaces_(b), e = 0; e < d[$$PROP_length]; e++) c[d[e]] = true;
        return c
    },
    gviz$ui$ConfigurationDialogController$parseAggregationColumns_ = function (b) {
        var c = {};
        if (b) {
            var d = gviz$ui$ConfigurationDialogController$parseToTokensBySpaces_(b);
            if (d[$$PROP_length] % 2 == 0) for (var e = 0; e < d[$$PROP_length]; e += 2) c[d[e]] = d[e + 1]
        }
        return c
    },
    gviz$ui$ConfigurationDialogController$parseAllColumnsInfo_ =

    function (b) {
        for (var c = b ? eval("(" + b + ")") : [], d = [], e = 0; e < c[$$PROP_length]; e++) {
            var f = c[e].id,
                g = c[e].label,
                h = c[e][$$PROP_type];
            d[$$PROP_push](new gviz$ui$ConfigurationColumnInfo(f, g, h))
        }
        return d
    };
gviz$ui$ConfigurationDialogController[$$PROP_prototype].selectedIdAllColumnsTable_ = null;
gviz$ui$ConfigurationDialogController[$$PROP_prototype].selectedIdGroupingColumnsTable_ = null;
gviz$ui$ConfigurationDialogController[$$PROP_prototype].isDialogInitialized_ = false;
var gviz$ui$ConfigurationDialogController$ConfigurationId = {};
gviz$ui$ConfigurationDialogController[$$PROP_prototype].allColumnsTable_ = null;
gviz$ui$ConfigurationDialogController[$$PROP_prototype].groupingColumnsTable_ = null;
gviz$ui$ConfigurationDialogController[$$PROP_prototype].aggregationColumnsTable_ = null;
var gviz$ui$ConfigurationDialogController$ColumnClassName = {};
a = gviz$ui$ConfigurationDialogController[$$PROP_prototype];
a.buttonUp_ = null;
a.buttonDown_ = null;
a.buttonLeft_ = null;
a.buttonRight_ = null;
a.buttonNext_ = null;
a.initButtons_ = function () {
    this.buttonUp_ = GLOBAL_document[$$PROP_getElementById]("up-button");
    this.buttonDown_ = GLOBAL_document[$$PROP_getElementById]("down-button");
    this.buttonLeft_ = GLOBAL_document[$$PROP_getElementById]("left-button");
    this.buttonRight_ = GLOBAL_document[$$PROP_getElementById]("right-button");
    this.buttonNext_ = GLOBAL_document[$$PROP_getElementById]("next-button");
    this.buttonOkSelectGroupingSection_ = GLOBAL_document[$$PROP_getElementById]("ok-button-first-section")
};
a.initConfigDialogSelectGroupingSection_ = function () {
    var b = this.dom_;
    this.allColumnsTable_ = GLOBAL_document[$$PROP_getElementById]("cfg-all-columns-table");
    this.groupingColumnsTable_ = GLOBAL_document[$$PROP_getElementById]("cfg-grouping-columns-table");
    for (var c = this.allColumnsInfo_, d = this.groupByColumns_, e = b.createDom("tbody", null), f = b.createDom("tbody", null), g, h, i = null, j = null, k = 0; k < c[$$PROP_length]; k++) {
        g = c[k].getId();
        h = c[k].getDisplayName();
        if (d[g]) {
            l = b.createDom("tr", {
                "class": "cfg-columns-list-grouping-tr"
            }, b.createDom("td", {
                id: g,
                "class": "cfg-columns-list-non-selected-td"
            }, b[$$PROP_createTextNode](h)));
            goog$events$listen(l, "mousedown", goog$bind(this.handleColumnSelection, this, g));
            f[$$PROP_appendChild](l);
            j || (j = g)
        } else {
            var l = b.createDom("tr", {
                "class": "cfg-columns-list-all-tr"
            }, b.createDom("td", {
                id: g,
                "class": "cfg-columns-list-non-selected-td"
            }, b[$$PROP_createTextNode](h)));
            goog$events$listen(l, "mousedown", goog$bind(this.handleColumnSelection, this, g));
            e[$$PROP_appendChild](l);
            i || (i = g)
        }
    }
    b.removeChildren(this.allColumnsTable_);
    b[$$PROP_appendChild](this.allColumnsTable_, e);
    i && this.handleColumnSelection(i);
    b.removeChildren(this.groupingColumnsTable_);
    b[$$PROP_appendChild](this.groupingColumnsTable_, f);
    if (j) this.handleColumnSelection(j);
    else {
        SETPROP_disabled(this.buttonNext_, true);
        SETPROP_disabled(this.buttonOkSelectGroupingSection_, "")
    }
    this.recreateTable_(1)
};
a.initConfigDialogSelectAggregationValues_ = function () {
    var b = this.dom_;
    this.aggregationColumnsTable_ = GLOBAL_document[$$PROP_getElementById]("cfg-aggregte-columns-table");
    for (var c = gviz$ui$ConfigurationDialogController$ColumnClassName.TR, d = this.allColumnsInfo_, e = this.aggregationColumns_, f = this.groupByColumns_, g = b.createDom("tbody", null), h, i, j, k, l = 0; l < d[$$PROP_length]; l++) {
        k = d[l].getId();
        if (!f[k]) {
            h = d[l].getAggregationTypes();
            i = b.createDom("select", {
                "class": "cfg-select-menu"
            });
            for (var n = 0; n < h[$$PROP_length]; n++) {
                j =
                h[n];
                j == e[k] ? b[$$PROP_appendChild](i, b.createDom("option", {
                    value: j,
                    selected: true
                }, b[$$PROP_createTextNode](this.messages_[j]))) : b[$$PROP_appendChild](i, b.createDom("option", {
                    value: j
                }, b[$$PROP_createTextNode](this.messages_[j])))
            }
            var m = b.createDom("tr", {
                "class": c
            }, b.createDom("td", {
                id: k,
                "class": "cfg-columns-list-td"
            }, b[$$PROP_createTextNode](d[l].getDisplayName())), b.createDom("td", {
                "class": "cfg-columns-list-menu-td"
            }, i));
            g[$$PROP_appendChild](m)
        }
    }
    b.removeChildren(this.aggregationColumnsTable_);
    b[$$PROP_appendChild](this.aggregationColumnsTable_, g)
};
a.handleColumnSelection = function (b) {
    var c = GLOBAL_document[$$PROP_getElementById](b),
        d = c[$$PROP_parentNode],
        e = d[$$PROP_className] == "cfg-columns-list-grouping-tr";
    this.clearColumnSelection_(e);
    SETPROP_className(c, "cfg-columns-list-selected-td");
    if (e) {
        this.selectedIdGroupingColumnsTable_ = b;
        SETPROP_disabled(this.buttonLeft_, "")
    } else {
        this.selectedIdAllColumnsTable_ = b;
        SETPROP_disabled(this.buttonRight_, "")
    }
    var f = d[$$PROP_parentNode];
    if (e && f[$$PROP_childNodes][$$PROP_length] > 1) if (f[$$PROP_firstChild] == d) SETPROP_disabled(this.buttonDown_, "");
    else if (f.lastChild == d) SETPROP_disabled(this.buttonUp_, "");
    else {
        SETPROP_disabled(this.buttonUp_, "");
        SETPROP_disabled(this.buttonDown_, "")
    }
};
a.clearColumnSelection_ = function (b) {
    var c;
    if (b) {
        SETPROP_disabled(this.buttonUp_, true);
        SETPROP_disabled(this.buttonDown_, true);
        SETPROP_disabled(this.buttonLeft_, true);
        c = GLOBAL_document[$$PROP_getElementById](this.selectedIdGroupingColumnsTable_);
        this.selectedIdGroupingColumnsTable_ = null
    } else {
        SETPROP_disabled(this.buttonRight_, true);
        c = GLOBAL_document[$$PROP_getElementById](this.selectedIdAllColumnsTable_);
        this.selectedIdAllColumnsTable_ = null
    }
    if (c) SETPROP_className(c, "cfg-columns-list-non-selected-td")
};
a.moveColumn = function (b) {
    var c = GLOBAL_document[$$PROP_getElementById](this.selectedIdGroupingColumnsTable_);
    if (c) {
        var d = c[$$PROP_parentNode],
            e = d[$$PROP_parentNode];
        if (b) {
            var f = d.previousSibling;
            e[$$PROP_removeChild](d);
            e[$$PROP_insertBefore](d, f)
        } else {
            var g = d[$$PROP_nextSibling][$$PROP_nextSibling];
            e[$$PROP_removeChild](d);
            e[$$PROP_insertBefore](d, g)
        }
        SETPROP_disabled(this.buttonDown_, "");
        SETPROP_disabled(this.buttonUp_, "");
        this.handleColumnSelection(c.id)
    }
    this.recreateTable_(1)
};
a.unGroupColumn = function () {
    var b = GLOBAL_document[$$PROP_getElementById](this.selectedIdGroupingColumnsTable_),
        c = this.groupingColumnsTable_;
    if (b) {
        var d = b[$$PROP_parentNode];
        SETPROP_className(d, "cfg-columns-list-all-tr");
        this.selectedIdGroupingColumnsTable_ = null;
        SETPROP_disabled(this.buttonLeft_, true);
        c[$$PROP_firstChild][$$PROP_removeChild](d);
        this.insertToAllTableInOrder(d);
        this.handleColumnSelection(b.id);
        var e = c[$$PROP_firstChild][$$PROP_firstChild];
        e && this.handleColumnSelection(e[$$PROP_firstChild].id)
    }
    this.recreateTable_(0);
    this.recreateTable_(1);
    if (this.groupingColumnsTable_ && this.groupingColumnsTable_[$$PROP_firstChild] && this.groupingColumnsTable_[$$PROP_firstChild][$$PROP_childNodes][$$PROP_length] == 0) {
        SETPROP_disabled(this.buttonNext_, true);
        SETPROP_disabled(this.buttonOkSelectGroupingSection_, "")
    }
};
a.insertToAllTableInOrder = function (b) {
    if (b) if (this.allColumnsTable_[$$PROP_firstChild][$$PROP_childNodes][$$PROP_length] == 0) this.allColumnsTable_[$$PROP_firstChild][$$PROP_appendChild](b);
    else {
        for (var c = this.allColumnsInfo_, d = this.allColumnsTable_[$$PROP_firstChild][$$PROP_firstChild], e = true, f = 0; d;) {
            var g = c[f].getId();
            f++;
            if (g == b[$$PROP_firstChild].id) {
                e = false;
                break
            }
            if (g == d[$$PROP_firstChild].id) if (d[$$PROP_nextSibling]) d = d[$$PROP_nextSibling];
            else break
        }
        e ? this.dom_.insertSiblingAfter(b, d) : this.dom_.insertSiblingBefore(b, d)
    }
};
a.groupColumn = function () {
    var b = GLOBAL_document[$$PROP_getElementById](this.selectedIdAllColumnsTable_),
        c = this.allColumnsTable_,
        d = this.groupingColumnsTable_;
    if (b) {
        var e = b[$$PROP_parentNode];
        SETPROP_className(e, "cfg-columns-list-grouping-tr");
        this.selectedIdAllColumnsTable_ = null;
        SETPROP_disabled(this.buttonRight_, true);
        c[$$PROP_firstChild][$$PROP_removeChild](e);
        d[$$PROP_firstChild][$$PROP_appendChild](e);
        this.handleColumnSelection(b.id);
        var f = c[$$PROP_firstChild][$$PROP_firstChild];
        f && this.handleColumnSelection(f[$$PROP_firstChild].id)
    }
    this.recreateTable_(0);
    this.recreateTable_(1);
    SETPROP_disabled(this.buttonNext_, "");
    SETPROP_disabled(this.buttonOkSelectGroupingSection_, true)
};
a.recreateTable_ = function (b) {
    var c = this.dom_,
        d, e;
    if (b == 1) {
        d = this.groupingColumnsTable_;
        e = true
    } else if (b == 0) {
        d = this.allColumnsTable_;
        e = false
    } else return;
    for (var f = e ? "cfg-columns-list-grouping-tr" : "cfg-columns-list-all-tr", g = c.createDom("tbody", null), h = d[$$PROP_firstChild][$$PROP_firstChild], i = "\u00a0\u00a0\u00a0\u00a0", j = "\u2514\u00a0", k = "", l = "", n = true; h;) {
        var m = h[$$PROP_firstChild],
            o = m.id,
            q = m[$$PROP_childNodes][$$PROP_length] == 1 ? m[$$PROP_firstChild][$$PROP_nodeValue] : m[$$PROP_childNodes][1][$$PROP_nodeValue];
        if (!n && e) {
            l = k + j;
            k += i
        }
        n = false;
        var p = m[$$PROP_className] == "cfg-columns-list-selected-td" ? "cfg-columns-list-selected-td" : "cfg-columns-list-non-selected-td",
            r = c.createDom("tr", {
                "class": f
            }, c.createDom("td", {
                id: o,
                "class": p
            }, c.createDom("span", {
                "class": "cfg-indent-span"
            }, c[$$PROP_createTextNode](l)), c[$$PROP_createTextNode](q)));
        goog$events$listen(r, "mousedown", goog$bind(this.handleColumnSelection, this, o));
        g[$$PROP_appendChild](r);
        h = h[$$PROP_nextSibling]
    }
    c.removeChildren(d);
    c[$$PROP_appendChild](d, g)
};
a.handleNext = function () {
    SETPROP_display(this.configurationDialogSelectGroupingDiv_[$$PROP_style], "none");
    for (var b = this.groupingColumnsTable_, c = b && b[$$PROP_firstChild] && b[$$PROP_firstChild][$$PROP_firstChild], d = {}; c;) {
        if (c[$$PROP_tagName] == "TR") d[c[$$PROP_firstChild].id] = true;
        c = c[$$PROP_nextSibling]
    }
    this.groupByColumns_ = d;
    this.initConfigDialogSelectAggregationValues_();
    SETPROP_display(this.configurationDialogSelectAggregationDiv_[$$PROP_style], "")
};
a.handleBack = function () {
    SETPROP_display(this.configurationDialogSelectAggregationDiv_[$$PROP_style], "none");
    this.aggregationColumns_ = this.getAggreagtionValues_();
    this.initConfigDialogSelectGroupingSection_();
    SETPROP_display(this.configurationDialogSelectGroupingDiv_[$$PROP_style], "")
};
a.getAggreagtionValues_ = function () {
    for (var b = this.aggregationColumnsTable_, c = b && b[$$PROP_firstChild] && b[$$PROP_firstChild][$$PROP_firstChild], d = {}; c;) {
        if (c[$$PROP_tagName] == "TR") {
            var e = c[$$PROP_childNodes][1][$$PROP_firstChild];
            if (e[$$PROP_selectedIndex] > 0) d[c[$$PROP_firstChild].id] = e[$$PROP_options][e[$$PROP_selectedIndex]][$$PROP_value]
        }
        c = c[$$PROP_nextSibling]
    }
    return d
};
a.handleCancel = function () {
    this.closeDialog_()
};
a.closeDialog_ = function () {
    SETPROP_display(this.configurationDialogSelectGroupingDiv_[$$PROP_style], "none");
    SETPROP_display(this.configurationDialogSelectAggregationDiv_[$$PROP_style], "none")
};
a.handleOk = function () {
    this.closeDialog_();
    this.applyAction_()
};
a.openDialog = function () {
    if (!this.isDialogInitialized_) {
        this.initButtons_();
        this.isDialogInitialized_ = true
    }
    this.allColumnsInfo_ = gviz$ui$ConfigurationDialogController$parseAllColumnsInfo_(this.allColumnsString_);
    this.groupByColumns_ = gviz$ui$ConfigurationDialogController$parseGroupByColumns_(this.groupByPref_);
    this.aggregationColumns_ = gviz$ui$ConfigurationDialogController$parseAggregationColumns_(this.aggregateByPref_);
    this.initConfigDialogSelectGroupingSection_();
    SETPROP_display(this.configurationDialogSelectGroupingDiv_[$$PROP_style], "")
};
a.applyAction_ = function () {
    for (var b = this.tableGadgetManager_.getPrefs(), c = this.groupingColumnsTable_[$$PROP_firstChild][$$PROP_firstChild], d = []; c;) {
        d[$$PROP_push]("`" + c[$$PROP_firstChild].id + "`");
        c = c[$$PROP_nextSibling]
    }
    try {
        b.set("groupbycolumn", d[$$PROP_join](" "))
    } catch (e) {}
    c = this.aggregationColumnsTable_[$$PROP_firstChild][$$PROP_firstChild];
    for (var f = []; c;) {
        var g = c[$$PROP_childNodes][1][$$PROP_firstChild];
        g[$$PROP_selectedIndex] > 0 && f[$$PROP_push]("`" + c[$$PROP_firstChild].id + "` " + g[$$PROP_options][g[$$PROP_selectedIndex]][$$PROP_value]);
        c =
        c[$$PROP_nextSibling]
    }
    try {
        b.set("aggregateby", f[$$PROP_join](" "))
    } catch (h) {}
    this.tableGadgetManager_.sendQuery()
};
gviz$ui$ConfigurationDialogController[$$PROP_prototype].handleOk = gviz$ui$ConfigurationDialogController[$$PROP_prototype].handleOk;
gviz$ui$ConfigurationDialogController[$$PROP_prototype].handleCancel = gviz$ui$ConfigurationDialogController[$$PROP_prototype].handleCancel;
gviz$ui$ConfigurationDialogController[$$PROP_prototype].groupColumn = gviz$ui$ConfigurationDialogController[$$PROP_prototype].groupColumn;
gviz$ui$ConfigurationDialogController[$$PROP_prototype].unGroupColumn = gviz$ui$ConfigurationDialogController[$$PROP_prototype].unGroupColumn;
gviz$ui$ConfigurationDialogController[$$PROP_prototype].moveColumn = gviz$ui$ConfigurationDialogController[$$PROP_prototype].moveColumn;
gviz$ui$ConfigurationDialogController[$$PROP_prototype].handleColumnSelection = gviz$ui$ConfigurationDialogController[$$PROP_prototype].handleColumnSelection;
gviz$ui$ConfigurationDialogController[$$PROP_prototype].openDialog = gviz$ui$ConfigurationDialogController[$$PROP_prototype].openDialog;
gviz$ui$ConfigurationDialogController[$$PROP_prototype].handleNext = gviz$ui$ConfigurationDialogController[$$PROP_prototype].handleNext;
gviz$ui$ConfigurationDialogController[$$PROP_prototype].handleBack = gviz$ui$ConfigurationDialogController[$$PROP_prototype].handleBack;
var gviz$gadget$TableGadgetManager = function (b) {
    this.divTable_ = _gel("table-div");
    var c = this.prefs_ = new _IG_Prefs;
    this.showFilters_ = c.getBool("showfilters");
    this.enableGrouping_ = c.getBool("enablegrouping");
    this.messages_ = b;
    var d = c.getString("title");
    if (d != "") try {
        _IG_SetTitle(d)
    } catch (e) {}
    google.load("visualization", "1");
    google.setOnLoadCallback(goog$bind(this.sendQuery, this));
    if (goog$userAgent$IE$$constant && this.enableGrouping_) this.divTable_[$$PROP_style].top = "-16px"
};
a = gviz$gadget$TableGadgetManager[$$PROP_prototype];
a.groupByPref_ = "";
a.aggregateByPref_ = "";
a.hasGrouping_ = false;
a.hasAggregation_ = false;
a.tableController_ = null;
a.canCreateNewTableController_ = true;
a.configrationController_ = null;
a.allColumnsJson_ = null;
a.getConfigurationController = function () {
    if (this.configrationController_) {
        if (this.canCreateNewTableController_) {
            this.configrationController_.storePrefs(this.allColumnsJson_, this.groupByPref_, this.aggregateByPref_);
            this.canCreateNewTableController_ = false
        }
    } else this.configrationController_ = new gviz$ui$ConfigurationDialogController(this.groupByPref_, this.aggregateByPref_, this.allColumnsJson_, this);
    return this.configrationController_
};
a.filterRows = function (b) {
    SETPROP_display(this.divTable_[$$PROP_style], "none");
    this.tableController_.filterRows(b);
    SETPROP_display(this.divTable_[$$PROP_style], "")
};
a.hideOrShowRows = function (b, c, d, e) {
    SETPROP_display(this.divTable_[$$PROP_style], "none");
    var f = this.tableController_.hideOrShowRows(b, c, d, e);
    SETPROP_display(this.divTable_[$$PROP_style], "");
    return f
};
a.getPrefs = function () {
    return this.prefs_
};
a.getMessages = function () {
    return this.messages_
};
a.gadgetHelper_ = null;
a.sendQuery = function () {
    var b = this.prefs_,
        c = GLOBAL_String(goog$string$hashCode(b.getString("_table_query_url")));
    if (b.getString("last_query_hash") != c) try {
        b.set("last_query_hash", c);
        b.set("groupbycolumn", "");
        b.set("aggregateby", "")
    } catch (d) {}
    this.groupByPref_ = b.getString("groupbycolumn");
    this.aggregateByPref_ = b.getString("aggregateby");
    this.hasGrouping_ = this.groupByPref_ != "";
    this.hasAggregation_ = this.aggregateByPref_ != "";
    this.gadgetHelper_ = new google.visualization.GadgetHelper;
    var e = this.gadgetHelper_.createQueryFromPrefs(b);
    this.showFilters_ || e.setHandlerParameter("hideFilters", "1");
    this.hasGrouping_ && this.enableGrouping_ && e.setHandlerParameter("groupBy", this.groupByPref_);
    this.hasAggregation_ && this.enableGrouping_ && e.setHandlerParameter("aggBy", this.aggregateByPref_);
    e.setHandlerType("table");
    e.send(goog$bind(this.responseHandler, this))
};
a.responseHandler = function (b) {
    SETPROP_display(GLOBAL_document[$$PROP_getElementById]("loading")[$$PROP_style], "none");
    if (this.gadgetHelper_.validateResponse(b)) {
        var c = b.getDataTable(),
            d = null,
            e = null,
            f = 0,
            g = 0,
            h = _gel("scrollpane");
        if (this.tableController_) {
            if (this.hasGrouping_) d = this.tableController_.getHiddenGroupingRows();
            if (this.showFilters_) e = this.tableController_.getSelectedFilterValues();
            f = h.scrollTop;
            g = h.scrollLeft
        }
        SETPROP_display(h[$$PROP_style], "none");
        this.divTable_.innerHTML = "";
        this.divTable_.innerHTML =
        c[$$PROP_getValue](0, 0);
        this.tableController_ = new gviz$ui$TableController(eval(c[$$PROP_getValue](0, 1)));
        var i = GLOBAL_parseInt(c[$$PROP_getValue](0, 2), 10);
        this.tableController_.refreshTableVisibility(d, e, i);
        this.allColumnsJson_ = c[$$PROP_getValue](0, 3);
        this.canCreateNewTableController_ = true;
        if (this.enableGrouping_ && this.allColumnsJson_) {
            var j = GLOBAL_document[$$PROP_getElementById]("open-dialog-link-span");
            SETPROP_display(j[$$PROP_style], "")
        }
        SETPROP_display(h[$$PROP_style], "");
        h[$$PROP_style].width =
        GLOBAL_document.body.clientWidth + "px";
        h[$$PROP_style].height = GLOBAL_document.body.clientHeight + "px";
        h.scrollTop = f;
        h.scrollLeft = g
    }
};
var JSCompiler_inline_parts_165 = "TableGadgetManager" [$$PROP_split]("."),
    JSCompiler_inline_cur_166 = goog$global;
!(JSCompiler_inline_parts_165[0] in JSCompiler_inline_cur_166) && JSCompiler_inline_cur_166.execScript && JSCompiler_inline_cur_166.execScript("var " + JSCompiler_inline_parts_165[0]);
for (var JSCompiler_inline_part_167; JSCompiler_inline_parts_165[$$PROP_length] && (JSCompiler_inline_part_167 = JSCompiler_inline_parts_165.shift());) if (!JSCompiler_inline_parts_165[$$PROP_length] && gviz$gadget$TableGadgetManager !== GLOBAL_undefined) JSCompiler_inline_cur_166[JSCompiler_inline_part_167] = gviz$gadget$TableGadgetManager;
else JSCompiler_inline_cur_166 = JSCompiler_inline_cur_166[JSCompiler_inline_part_167] ? JSCompiler_inline_cur_166[JSCompiler_inline_part_167] : (JSCompiler_inline_cur_166[JSCompiler_inline_part_167] = {});
gviz$gadget$TableGadgetManager[$$PROP_prototype]._fr = gviz$gadget$TableGadgetManager[$$PROP_prototype].filterRows;
gviz$gadget$TableGadgetManager[$$PROP_prototype]._hosr = gviz$gadget$TableGadgetManager[$$PROP_prototype].hideOrShowRows;
gviz$gadget$TableGadgetManager[$$PROP_prototype]._gcc = gviz$gadget$TableGadgetManager[$$PROP_prototype].getConfigurationController;
gviz$gadget$TableGadgetManager[$$PROP_prototype].sendQuery = gviz$gadget$TableGadgetManager[$$PROP_prototype].sendQuery;