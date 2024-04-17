package com.example.data.repository.realm

import com.example.data.mapper.realm.mapTo
import com.example.data.source.realm.entity.Request
import com.example.data.source.realm.entity.RequestPathParam
import com.example.domain.api.model.RequestApi
import com.example.domain.common.repository.RealmRepository
import io.realm.Realm
import io.realm.RealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RealmRepositoryImpl @Inject constructor() : RealmRepository {

    //... add cache

    override suspend fun getRequestsApi(): List<RequestApi> = withContext(Dispatchers.IO) {
        Realm.getDefaultInstance().use { realm ->
            val result = realm.where(Request::class.java).findAll().toList()
            return@withContext result.mapTo()
        }
    }

    override suspend fun getRequestApiById(id: String): RequestApi? = withContext(Dispatchers.IO) {
        Realm.getDefaultInstance().use { realm ->
            val result = realm.where(Request::class.java).equalTo("id", id).findFirst()
            return@withContext result?.mapTo()
        }
    }

    override suspend fun createRequestApi(requestApi: RequestApi): Boolean =
        withContext(Dispatchers.IO) {
            Realm.getDefaultInstance().use { realm ->
                realm.beginTransaction()

                val request = realm.createObject(Request::class.java, requestApi.id)
                val pathParamsList = RealmList<RequestPathParam>()
                pathParamsList.addAll(requestApi.pathParams?.map {
                    val realmObject = realm.createObject(RequestPathParam::class.java)
                    realmObject.name = it.name
                    realmObject.type = it.type
                    realmObject.value = it.value
                    return@map realmObject
                } ?: listOf())

                request.method = requestApi.method.name
                request.url = requestApi.url
                request.pathParams = pathParamsList
                request.body = requestApi.body
                request.voiceString = requestApi.voiceString
                request.isLike = requestApi.isLike

                realm.commitTransaction()
            }

            return@withContext true
        }

    override suspend fun createRequestApi(listRequestApi: List<RequestApi>): Boolean =
        withContext(Dispatchers.IO) {
            Realm.getDefaultInstance().use { realm ->
                realm.beginTransaction()

                for (requestApi in listRequestApi) {
                    val request = realm.createObject(Request::class.java, requestApi.id)
                    val pathParamsList = RealmList<RequestPathParam>()
                    pathParamsList.addAll(requestApi.pathParams?.map {
                        val realmObject = realm.createObject(RequestPathParam::class.java)
                        realmObject.name = it.name
                        realmObject.type = it.type
                        realmObject.value = it.value
                        return@map realmObject
                    } ?: listOf())

                    request.method = requestApi.method.name
                    request.url = requestApi.url
                    request.pathParams = pathParamsList
                    request.body = requestApi.body
                    request.voiceString = requestApi.voiceString
                    request.isLike = requestApi.isLike
                }

                realm.commitTransaction()
            }

            return@withContext true
        }

    override suspend fun updateRequestApi(requestApi: RequestApi) : Boolean =
        withContext(Dispatchers.IO) {
            Realm.getDefaultInstance().use { realm ->
                realm.beginTransaction()

                val request = realm.where(Request::class.java).equalTo("id", requestApi.id)
                    .findFirst() ?: return@withContext false

                val pathParamsList = RealmList<RequestPathParam>()
                pathParamsList.addAll(requestApi.pathParams?.map {
                    val realmObject = realm.createObject(RequestPathParam::class.java)
                    realmObject.name = it.name
                    realmObject.type = it.type
                    realmObject.value = it.value
                    return@map realmObject
                } ?: listOf())

                request.apply {
                    method = requestApi.method.name
                    url = requestApi.url
                    pathParams = pathParamsList
                    body = requestApi.body
                    voiceString = requestApi.voiceString
                    isLike = requestApi.isLike
                }

                realm.commitTransaction()
            }

            return@withContext true
        }

    override suspend fun deleteRequestApi(requestApi: RequestApi): Boolean =
        withContext(Dispatchers.IO) {
            Realm.getDefaultInstance().use { realm ->
                realm.beginTransaction()

                val request = realm.where(Request::class.java).equalTo("id", requestApi.id)
                    .findFirst() ?: return@withContext false
                request.deleteFromRealm()

                realm.commitTransaction()
            }

            return@withContext true
        }

}