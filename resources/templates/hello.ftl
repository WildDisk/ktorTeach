<#import "common.ftl" as common>

<@common.page>
    <h2>Hello <#if user??>${user.username}<#else>guest</#if>!</h2>
    Your email address is <#if user.email??>${user.email}<#else>¯\_(ツ)_/¯</#if>
    <#include "usersList.ftl"/>
</@common.page>