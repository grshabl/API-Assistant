package com.example.data.repository.realm

import com.example.data.mapper.realm.mapTo
import com.example.data.source.realm.entity.Request
import com.example.data.source.realm.entity.RequestPathParam
import com.example.domain.api.model.RequestApi
import com.example.domain.common.repository.RealmRepository
import io.realm.Realm
import io.realm.RealmList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RealmRepositoryImpl @Inject constructor() : RealmRepository {

    //... add cache

    override suspend fun getRequestsApi(): List<RequestApi> = withContext(Dispatchers.IO) {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(Request::class.java).findAll().toList()
        realm.close()

        return@withContext result.mapTo()
    }

    override suspend fun getRequestApi(id: String): RequestApi? = withContext(Dispatchers.IO) {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(Request::class.java).equalTo("id", id).findFirst()
        realm.close()

        return@withContext result?.mapTo()
    }

    override fun createRequestApi(requestApi: RequestApi) {
        CoroutineScope(Dispatchers.IO).launch {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()

            val request = realm.createObject(Request::class.java)
            val pathParamsList = RealmList<RequestPathParam>()
            pathParamsList.addAll(requestApi.pathParams?.map { it.mapTo() } ?: listOf())

            request.id = requestApi.id
            request.method = requestApi.method.name
            request.url = requestApi.url
            request.pathParams = pathParamsList
            request.body = requestApi.body
            request.voiceString = requestApi.voiceString
            request.isLike = requestApi.isLike

            realm.commitTransaction()
            realm.close()
        }
    }

    override fun updateRequestApi(requestApi: RequestApi) {
        CoroutineScope(Dispatchers.IO).launch {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()

            val request = realm.where(Request::class.java).equalTo("id", requestApi.id)
                .findFirst()
            val pathParamsList = RealmList<RequestPathParam>()
            pathParamsList.addAll(requestApi.pathParams?.map { it.mapTo() } ?: listOf())

            request?.let {
                it.method = requestApi.method.name
                it.url = requestApi.url
                it.pathParams = pathParamsList
                it.body = requestApi.body
                it.voiceString = requestApi.voiceString
                it.isLike = requestApi.isLike
            }
            // ... add throw exception

            realm.commitTransaction()
            realm.close()
        }
    }

    override fun deleteRequestApi(requestApi: RequestApi) {
        CoroutineScope(Dispatchers.IO).launch {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()

            val request = realm.where(Request::class.java).equalTo("id", requestApi.id)
                .findFirst()
            request?.deleteFromRealm()
            // ... add throw exception

            realm.commitTransaction()
            realm.close()
        }
    }

}