package com.example.domain.api.usecase

import com.example.domain.api.converter.toListRequestPathParam
import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi
import com.google.gson.Gson
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.parser.core.models.ParseOptions
import io.swagger.v3.parser.core.models.SwaggerParseResult
import javax.inject.Inject

interface ParserUseCase {
    @Throws(java.lang.IllegalStateException::class)
    fun getListRequestApi(url: String): List<RequestApi>
}

class ParserUseCaseImpl @Inject constructor(): ParserUseCase {
    private val gson = Gson()

    @Throws(IllegalStateException::class)
    override fun getListRequestApi(url: String): List<RequestApi> {
        val resultListRequestApi = mutableListOf<RequestApi>()

        val parseOptions = ParseOptions()
        parseOptions.isResolve = true
        parseOptions.isResolveFully = true

        val result: SwaggerParseResult = OpenAPIParser().readLocation(url, null, parseOptions)
        val openapi: OpenAPI? = result.openAPI

        if (result.messages != null) result.messages.forEach(System.err::println); // validation errors and warnings

        if (openapi != null) {
            val paths: Paths = openapi.paths
            for (path in paths) {
                val url = path.key
                val paramsPath = path.value

                paramsPath.get?.let {
                    resultListRequestApi.add(
                        getRequestApi(
                            url = url,
                            method = MethodRequest.GET,
                            operation = it
                        )
                    )
                }
                paramsPath.post?.let {
                    resultListRequestApi.add(
                        getRequestApi(
                            url = url,
                            method = MethodRequest.POST,
                            operation = it
                        )
                    )
                }
                paramsPath.delete?.let {
                    resultListRequestApi.add(
                        getRequestApi(
                            url = url,
                            method = MethodRequest.DELETE,
                            operation = it
                        )
                    )
                }
                paramsPath.put?.let {
                    resultListRequestApi.add(
                        getRequestApi(
                            url = url,
                            method = MethodRequest.PUT,
                            operation = it
                        )
                    )
                }
                paramsPath.head?.let {
                    resultListRequestApi.add(
                        getRequestApi(
                            url = url,
                            method = MethodRequest.HEAD,
                            operation = it
                        )
                    )
                }
                paramsPath.patch?.let {
                    resultListRequestApi.add(
                        getRequestApi(
                            url = url,
                            method = MethodRequest.PATCH,
                            operation = it
                        )
                    )
                }
            }
        } else {
            throw IllegalStateException("open api is null")
        }

        return resultListRequestApi
    }

    private fun getRequestApi(url: String, method: MethodRequest, operation: Operation): RequestApi {
        val listQueryParams = operation.parameters.toListRequestPathParam() // TODO проверить на Type.PATH
        val exampleJson = generateJsonExample(operation.requestBody?.content?.get(KEY_JSON_BODY)?.schema)
        val jsonString = if (exampleJson != null) gson.toJson(exampleJson) else null

        return RequestApi(
            method = method,
            url = url,
            pathParams = listQueryParams,
            body = jsonString
        )
    }

    private fun generateJsonExample(schema: Schema<Any>?): Map<String, Any?>? {
        if (schema == null) return null

        val jsonExample = mutableMapOf<String, Any?>()
        if (schema.properties != null) {
            schema.properties.forEach { key, value -> jsonExample.put(key, generateJsonValue(value)) }
        }
        return jsonExample
    }

    private fun generateJsonValue(schema: Schema<*>): Any? =
        when {
            schema.type == TypeValue.STRING.title -> TypeValue.STRING.defValue
            schema.type == TypeValue.INTEGER.title -> TypeValue.INTEGER.defValue
            schema.type == TypeValue.BOOLEAN.title -> TypeValue.BOOLEAN.defValue
            schema.type == TypeValue.ARRAY.title && schema.items != null -> {
                val items: MutableList<Any?> = ArrayList()
                items.add(generateJsonValue(schema.items))
                items
            }
            schema.type == TypeValue.OBJECT.title && schema.properties != null -> {
                val obj = mutableMapOf<String, Any?>()
                for (key in schema.properties.keys) {
                    obj.put(key, generateJsonValue(schema.properties[key]!!))
                }
                obj
            }
            else -> null
        }

    companion object {
        private const val KEY_JSON_BODY = "application/json"
    }


    private enum class TypeValue(val title: String, val defValue: Any?) {
        STRING("string", ""),
        INTEGER("integer", 0),
        BOOLEAN("boolean", true),
        ARRAY("array", null),
        OBJECT("object", null)
    }
}