<div class="card my-3">
    <#list users as user>
        <div class="m-2">
            <span>
                Username: ${user.username}<br>
                Email: <#if user.email??>${user.email}<#else>¯\_(ツ)_/¯</#if><br>
                First name: <#if user.firstName??>${user.firstName}<#else>¯\_(ツ)_/¯</#if><br>
                Last name: <#if user.lastName??>${user.lastName}<#else>¯\_(ツ)_/¯</#if><br>
                Role: <i>
                    <#if user.role??>
                        <#list user.role as role>
                            ${role}
                        </#list>
                    <#else>¯\_(ツ)_/¯</#if>
                </i>
            </span>
        </div>
    </#list>
</div>