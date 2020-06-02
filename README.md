# Ktor
Проект для изучения ktor веб фрэймворка

## Создание embedded сервера

`embeddedServer()` принимает в качестве аргументов: <br>
`factory: ApplicationEngineFactory<TEngine, TConfiguration>`- движок приложения, конкретно `Netty`<br>
`port: Int = 80` - порт <br>
`host: String = "0.0.0.0"` - адрес хоста <br>
`watchPaths: List<String> = emptyList()` - путь для автоматически перезагрузки <br>
`configure: TConfiguration.() -> Unit = {}` - скрипт конфигурации движка <br>
`module: Application.() -> Unit` - функция модуля приложения
```kotlin
fun foo() {
    embeddedServer(Netty, 8080) {}
}
```

## HTML DSL

Что бы не создавать шаблоны, можно использовать html dsl
```kotlin
fun foo() {
    routing {
        get("/") {
            call.respondHtml {
                head {}
                body {}
            }
        }       
    }
}
```

## Шаблонизаторы
Возможность использовать шаблоны, например Freemarker

```groovy
dependency {
    implementation "io.ktor:ktor-freemarker:$ktor_version"
}
```

```kotlin
fun foo() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
    routing {
        get("/") {
            call.respond(FreeMarkerContent("template.ftl", null))
        }
    }
}
```

## JSON
Возвращение json

```groovy
dependency {
    implementation "io.ktor:ktor-gson:$ktor_version"
}
```

```kotlin
fun foo() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    routing {
        get("/") {
            call.respond(listOf<Any>())
        }
    }
}
```