package com.example.data.source.realm.entity

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Request(
    @PrimaryKey
    var id: String = "",
    var method: String = "",
    var url: String = "",
    var pathParams: RealmList<RequestPathParam>? = null,
    var body: String? = null,
    var voiceString: String? = null,
    var isLike: Boolean = false
) : RealmObject()