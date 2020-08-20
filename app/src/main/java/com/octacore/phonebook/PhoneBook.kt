package com.octacore.phonebook

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class PhoneBook(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String
) : Parcelable