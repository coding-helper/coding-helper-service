<style lang="less">
    @import '../../styles/common.less';
</style>
<template><div>
<#list opts as opt >
<#if opt.type! == "query" >
<Card>
<#include "segments/template-form-query.ftl" />
</Card>
<#break>
</#if>
</#list>
<Card>
    <Row>
        <Col span="24" class="table-top-opt">
<#list opts as opt >
<#if opt.type! == "export" >
            <Button type="success" icon="archive" size="small" <#if opt.permissionTag! != "" >v-if="$store.getters.hasPermission('${opt.permissionTag!}')"</#if> @click="do${opt.name?cap_first}${opt.type?cap_first}">${opt.label!}</Button>
</#if>
</#list>
<#list opts as opt >
<#if opt.type! == "add" >
            <Button type="primary" icon="plus" size="small" <#if opt.permissionTag! != "" >v-if="$store.getters.hasPermission('${opt.permissionTag!}')"</#if> @click="go${opt.name?cap_first}${opt.type?cap_first}()">${opt.label!}</Button>
</#if>
<#if opt.type! == "save" >
            <Button type="primary" icon="plus" size="small" <#if opt.permissionTag! != "" >v-if="$store.getters.hasPermission('${opt.permissionTag!}')"</#if> @click="go${opt.name?cap_first}${opt.type?cap_first}()">添加</Button>
</#if>
</#list>
        </Col>
    </Row>
    <Row>
        <Col span="24">
            <Table :columns="columns" :data="tableData" border></Table>
        </Col>
    </Row>
    <Row>
        <Col span="24" class="table-bottom-pageable">
            <Page :total="page.total" :current="page.current" :page-size="page.size" show-total show-elevator size="small" @on-change="changePage">总计 {{page.total}} 条</Page>
        </Col>
    </Row>
</Card>
<#include "segments/template-modal-edit.ftl" />
<#include "segments/template-modal-detail.ftl" />
</div></template>
<script>
import util from '@/libs/util';
export default {
    data () {
        return {
            tableData: [],
            page: {
                total: 0,
                size: 10,
                current: 1,
                num: 0
            },
            columns: [
<#include "segments/data-columns.ftl" />
            ],
            optForm: {
<#include "segments/data-optform.ftl" />
            },
            optModal: {
<#include "segments/data-optmodal.ftl" />
            },
            optRule: {
<#include "segments/data-optrule.ftl" />
            },
            dict: {
<#include "segments/data-dict.ftl" />
            },
        }
    },
<#include "spec/list-init.ftl" />
    methods: {
        init () {
        	this.setOpts();
        },
        processQueryForm () {
            let queryForm = {
<#list opts as opt >
<#if opt.type! == "query" >
<#list opt.attrs as attr >
                ${attr.name!}: this.optForm.queryForm.${attr.name!},
</#list>
<#break>
</#if>
</#list>
            };
<#include "spec/queryform-page.ftl" />
<#if relaAttr! != "" >
            queryForm.${relaAttr} = this.optForm.${relaAttr};
</#if>
            return queryForm;
        },
        getTableData () {
            let _self = this;
<#list opts as opt >
<#if opt.type! == "query" && opt.exeUrl! != "" >
<#assign getTableDataUrl = opt.exeUrl />
<#break>
</#if>
</#list>
<#if getTableDataUrl! == "" >
<#list opts as opt >
<#if opt.type! == "list" && opt.exeUrl! != "" >
<#assign getTableDataUrl = opt.exeUrl />
<#break>
</#if>
</#list>
</#if>
            util.ajax.get('${getTableDataUrl!}', { params: this.processQueryForm() }).then(res => {
                if (res.status === 200) {
                    if (res.data.<#include "spec/res-success.ftl" />) {
<#include "spec/res-pageable.ftl" />
                    } else {
                        _self.$Message.error(res.data.message);
                    }
                }
            }).catch(err => {
                console.log(err);
            });
        },
        changePage (number) {
            this.page.current = number;
            this.getTableData();
        },
<#list opts as opt >
<#assign optName = opt.name + opt.type?cap_first />
<#if opt.type! == "query">
<#include "segments/method-query.ftl" />
<#elseif opt.type! == "add" >
<#include "segments/method-add.ftl" />
<#elseif opt.type! == "save" >
<#include "segments/method-save.ftl" />
<#elseif opt.type! == "update" >
<#include "segments/method-update.ftl" />
<#elseif opt.type! == "detail" >
<#include "segments/method-detail.ftl" />
<#elseif opt.type! == "delete" >
<#include "segments/method-delete.ftl" />
<#elseif opt.type! == "export" >
<#include "segments/method-export.ftl" />
<#else>
</#if>
</#list>
        setOpts() {
            let opts = [
<#list opts as opt >
<#if opt.type! == "delete" || opt.type! == "detail" || opt.type! == "save" || opt.type! == "update" >
                {
                    widget: 'Button',
                    attrs: params => {
                        return {
                            props: {
                                type: 'primary',
                                size: 'small'
                            },
                            style: {
                                marginRight: '5px',
<#if opt.permissionTag! != "" >
                                display: this.$store.getters.hasPermission('${opt.permissionTag!}') ? 'inline' : 'none',
<#else>
                                display: 'inline',
</#if>
                            },
                            on: {
                                click: () => {
                                    this.go${opt.name?cap_first}${opt.type?cap_first}(params.row.id);
                                }
                            }
                        };
                    },
                    label: '<#if opt.type! == "save" >修改<#else>${opt.label!}</#if>'
                },
</#if>
</#list>
            ];
            let optCount = opts.filter(opt => opt.attrs().style.display !== 'none').length;
            if (optCount > 0) {
                this.columns.push({
                    title: '操作',
                    key: 'action',
                    fixed: 'right',
                    align: 'center',
                    width: 60 * optCount,
                    render: (h, params) => {
                        return h('div', opts.map(optItem => {
                            return h(optItem.widget, optItem.attrs(params), optItem.label);
                        }));
                    }
                });
            }
        },
    }
};
</script>