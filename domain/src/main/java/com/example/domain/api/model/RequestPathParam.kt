package com.example.domain.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestPathParam(
    val name: String,
    val type: String, //value - значение из IntegerSchema.getType()
    val value: String = ""
): Parcelable