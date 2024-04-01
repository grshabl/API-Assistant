package com.example.data.source.realm.entity

import io.realm.RealmObject

open class RequestPathParam(
    var name: String? = "",
    var type: String? = ""
) : RealmObject()