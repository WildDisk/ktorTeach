<div class="card my-3">
    <#list users as user>
        <div class="m-2">
            <span>
                Username: ${user.name}<br>
                Email: <#if user.email??>${user.email}<#else>¯\_(ツ)_/¯</#if><br>
                Date: <i><#if user.date??>${user.date}<#else>¯\_(ツ)_/¯</#if></i>
            </span>
        </div>
    </#list>
</div>