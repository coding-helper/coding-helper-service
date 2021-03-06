<#if opts? size gt 0>
                {
                    title: '操作',
                    key: 'action',
                    fixed: 'right',
                    align: 'center',
                    width: 240,
                    render: (h, params) => {
                        return h('div', [
<#list opts as opt>
<#assign optName = opt.name + opt.type?cap_first />
<#if opt.type! == "update" || opt.type! == "delete" || opt.type! == "detail">
                            h('Button', {
                                props: {
                                    type: 'primary',
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '5px'
                                },
                                on: {
                                    click: () => {
                                        this.go${optName?cap_first}(params.row.id)
                                    }
                                }
                            }, '${opt.label}'),
</#if>
</#list>                            
                        ])
                    }
                }
</#if>