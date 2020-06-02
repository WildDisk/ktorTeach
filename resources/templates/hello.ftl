<#import "common.ftl" as common>

<@common.page>
    <h2>Hello <#if user??>${user.name}<#else>guest</#if>!</h2>
    Your email address is <#if user??>${user.email}<#else>¯\_(ツ)_/¯</#if>
    <#include "usersList.ftl"/>
</@common.page>