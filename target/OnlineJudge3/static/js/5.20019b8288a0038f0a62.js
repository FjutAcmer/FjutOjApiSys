webpackJsonp([5], {
    "db/y": function (t, s, a) {
        "use strict";
        Object.defineProperty(s, "__esModule", {value: !0});
        var e = {
            render: function () {
                var t = this, s = t.$createElement, a = t._self._c || s;
                return a("div", {staticClass: "mallframe"}, t._l(t.listrow, function (s, e) {
                    return a("el-row", {
                        key: s,
                        attrs: {offset: e > 0 ? 2 : 0, type: t.flex, justify: t.center, align: t.middle}
                    }, t._l(t.listcol, function (s, e) {
                        return a("el-col", {
                            key: s,
                            attrs: {span: 4, offset: e > 0 ? 2 : 0}
                        }, [a("el-card", {
                            staticClass: "card",
                            attrs: {"body-style": {padding: "0px"}}
                        }, [t.isloading ? a("div", {
                            staticClass: "column",
                            staticStyle: {height: "200px"}
                        }, [a("div", {staticClass: "container animation-5"}, [a("div", {staticClass: "shape shape1"}), t._v(" "), a("div", {staticClass: "shape shape2"}), t._v(" "), a("div", {staticClass: "shape shape3"}), t._v(" "), a("div", {staticClass: "shape shape4"})])]) : a("img", {
                            staticClass: "image",
                            attrs: {src: ""}
                        }), t._v(" "), a("div", {staticStyle: {"margin-top": "10px"}}, [a("span", [t._v("好吃的汉堡")]), t._v(" "), a("div", {staticClass: "bottom clearfix"}, [a("p", {staticClass: "time"}, [t._v(t._s(t.currentDate) + "ACB")]), t._v(" "), a("p", {staticClass: "button"}, [t._v("剩余：" + t._s(t.residue))])])])])], 1)
                    }), 1)
                }), 1)
            }, staticRenderFns: []
        };
        var i = a("VU/8")({
            data: function () {
                return {isloading: !0, listrow: 4, listcol: 4, currentDate: 100, residue: 30}
            }
        }, e, !1, function (t) {
            a("k7Se")
        }, "data-v-68d3def0", null);
        s.default = i.exports
    }, k7Se: function (t, s) {
    }
});
//# sourceMappingURL=5.20019b8288a0038f0a62.js.map