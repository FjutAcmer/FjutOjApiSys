webpackJsonp([6], {
    Obcw: function (e, t) {
    }, pAGS: function (e, t, a) {
        "use strict";
        Object.defineProperty(t, "__esModule", {value: !0});
        var l = {
            data: function () {
                return {form: {name: ""}}
            }, methods: {
                onSubmit: function () {
                    console.log("submit!")
                }
            }
        }, m = {
            render: function () {
                var e = this, t = e.$createElement, a = e._self._c || t;
                return a("div", {staticClass: "register"}, [a("el-card", {staticClass: "box-card"}, [a("h1", [e._v("注册")]), e._v(" "), a("el-form", {
                    ref: "form",
                    attrs: {model: e.form, "label-width": "80px", "label-position": "right"}
                }, [a("el-form-item", {attrs: {label: "用户名*"}}, [a("el-input", {
                    model: {
                        value: e.form.name,
                        callback: function (t) {
                            e.$set(e.form, "name", t)
                        },
                        expression: "form.name"
                    }
                })], 1), e._v(" "), a("el-form-item", {attrs: {label: "密码*"}}, [a("el-input", {
                    model: {
                        value: e.form.name,
                        callback: function (t) {
                            e.$set(e.form, "name", t)
                        },
                        expression: "form.name"
                    }
                })], 1), e._v(" "), a("el-form-item", {attrs: {label: "确认密码*"}}, [a("el-input", {
                    model: {
                        value: e.form.name,
                        callback: function (t) {
                            e.$set(e.form, "name", t)
                        },
                        expression: "form.name"
                    }
                })], 1), e._v(" "), a("el-form-item", {attrs: {label: "呢陈"}}, [a("el-input", {
                    model: {
                        value: e.form.name,
                        callback: function (t) {
                            e.$set(e.form, "name", t)
                        },
                        expression: "form.name"
                    }
                })], 1), e._v(" "), a("el-form-item", {attrs: {label: "学校"}}, [a("el-input", {
                    model: {
                        value: e.form.name,
                        callback: function (t) {
                            e.$set(e.form, "name", t)
                        },
                        expression: "form.name"
                    }
                })], 1), e._v(" "), a("el-form-item", {attrs: {label: "邮箱"}}, [a("el-input", {
                    model: {
                        value: e.form.name,
                        callback: function (t) {
                            e.$set(e.form, "name", t)
                        },
                        expression: "form.name"
                    }
                })], 1), e._v(" "), a("el-form-item", {attrs: {label: "签名"}}, [a("el-input", {
                    model: {
                        value: e.form.name,
                        callback: function (t) {
                            e.$set(e.form, "name", t)
                        },
                        expression: "form.name"
                    }
                })], 1), e._v(" "), a("el-form-item", [a("el-button", {
                    attrs: {type: "primary"},
                    on: {click: e.onSubmit}
                }, [e._v("注册")])], 1)], 1)], 1)], 1)
            }, staticRenderFns: []
        };
        var n = a("VU/8")(l, m, !1, function (e) {
            a("Obcw")
        }, "data-v-4938196b", null);
        t.default = n.exports
    }
});
//# sourceMappingURL=6.87ce0844ca80996dc886.js.map