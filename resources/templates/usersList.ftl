<div class="card my-3">
    <#list users as user>
        <div class="m-2">
            <span>Username: ${user.name}<br> Email: ${user.email}<br> Date: <i>${user.date}</i></span>
        </div>
    </#list>
</div>