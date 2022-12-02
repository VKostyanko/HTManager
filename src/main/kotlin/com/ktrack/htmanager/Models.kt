package com.ktrack.htmanager

import com.google.gson.annotations.SerializedName

data class UserCredentials(
    val login: String,
    val password: String
)

data class UserToken(
    @SerializedName("ticket") val token: String,
    @SerializedName("expirationTime") val expirationTime: String,
    @SerializedName("expirationUnixTime") val expirationUnixTime: Long,
)

data class Subscriptions(
    @SerializedName("alertTypes") var alertTypes: ArrayList<String> = arrayListOf(),
    @SerializedName("taskIds") var taskIds: ArrayList<String> = arrayListOf(),
    @SerializedName("contactIds") var contactIds: ArrayList<String> = arrayListOf()   //todo: delete
)

data class Task(
    @SerializedName("id") var id: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("checkDnsbl") var checkDnsbl: Boolean? = null,
    @SerializedName("rawUrl") var rawUrl: String? = null,
    @SerializedName("creationTime") var creationTime: String? = null,
    @SerializedName("taskType") var taskType: String? = null,
    @SerializedName("enabled") var enabled: Boolean? = null,
    @SerializedName("interval") var interval: Int? = null,
    @SerializedName("intervalSec") var intervalSec: Int? = null,
    @SerializedName("upFromTime") var upFromTime: String? = null,
    @SerializedName("lastState") var lastState: Boolean? = null,
    @SerializedName("lastStateChangeTime") var lastStateChangeTime: String? = null,
    @SerializedName("openStatEnabled") var openStatEnabled: Boolean? = null,
    @SerializedName("fullLogEnabled") var fullLogEnabled: Boolean? = null,
    @SerializedName("holdResults") var holdResults: Boolean? = null,
    @SerializedName("agentStat") var agentStat: Boolean? = null,
    @SerializedName("billingOverlimits") var billingOverlimits: ArrayList<String> = arrayListOf(),
    @SerializedName("tags") var tags: ArrayList<String> = arrayListOf(),
    @SerializedName("subscriptions") var subscriptions: ArrayList<Subscriptions> = arrayListOf(),
    @SerializedName("agentPools") var agentPools: ArrayList<String> = arrayListOf(),
    @SerializedName("deleted") var deleted: Boolean? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("httpMethod") var httpMethod: String? = null,
    @SerializedName("keywords") var keywords: ArrayList<String> = arrayListOf(),
    @SerializedName("keywordMode") var keywordMode: String? = null,
    @SerializedName("maxResponsePageSize") var maxResponsePageSize: Int? = null,
    @SerializedName("timeout") var timeout: Int? = null,
    @SerializedName("httpHeaders") var httpHeaders: String? = null,
    @SerializedName("followRedirect") var followRedirect: Boolean? = null,
    @SerializedName("treat300AsError") var treat300AsError: Boolean? = null,
    @SerializedName("checkCertificateExpiration") var checkCertificateExpiration: Boolean? = null,
    @SerializedName("checkRussianBlackLists") var checkRussianBlackLists: Boolean? = null,
    @SerializedName("checkDomainExpiration") var checkDomainExpiration: Boolean? = null,
    @SerializedName("ignoredStatuses") var ignoredStatuses: ArrayList<String> = arrayListOf()
)
