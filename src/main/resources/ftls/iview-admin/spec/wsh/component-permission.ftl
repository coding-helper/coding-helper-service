<#if opt.permissionTag! != "" >v-if="$store.getters.hasPermission('${opt.permissionTag!}')" </#if>