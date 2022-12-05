package com.ktrack.htmanager

import com.fasterxml.jackson.annotation.JsonProperty

data class UserCredentials(
    val login: String,
    val password: String
)

data class UserToken(
    @JsonProperty("ticket") val token: String,
    @JsonProperty("expirationTime") val expirationTime: String,
    @JsonProperty("expirationUnixTime") val expirationUnixTime: Long,
)

data class Subscriptions(
    @JsonProperty("alertTypes") var alertTypes: ArrayList<String> = arrayListOf(),
    @JsonProperty("taskIds") var taskIds: ArrayList<String> = arrayListOf(),
    @JsonProperty("contactIds") var contactIds: ArrayList<String> = arrayListOf()   //todo: delete
)

data class Task(
    @JsonProperty("id") var id: String? = null,
    @JsonProperty("url") var url: String? = null,
    @JsonProperty("checkDnsbl") var checkDnsbl: Boolean? = null,
    @JsonProperty("rawUrl") var rawUrl: String? = null,
    @JsonProperty("creationTime") var creationTime: String? = null,
    @JsonProperty("taskType") var taskType: String? = null,
    @JsonProperty("enabled") var enabled: Boolean? = null,
    @JsonProperty("interval") var interval: Int? = null,
    @JsonProperty("intervalSec") var intervalSec: Int? = null,
    @JsonProperty("upFromTime") var upFromTime: String? = null,
    @JsonProperty("lastState") var lastState: Boolean? = null,
    @JsonProperty("lastStateChangeTime") var lastStateChangeTime: String? = null,
    @JsonProperty("openStatEnabled") var openStatEnabled: Boolean? = null,
    @JsonProperty("fullLogEnabled") var fullLogEnabled: Boolean? = null,
    @JsonProperty("holdResults") var holdResults: Boolean? = null,
    @JsonProperty("agentStat") var agentStat: Boolean? = null,
    @JsonProperty("billingOverlimits") var billingOverlimits: ArrayList<String> = arrayListOf(),
    @JsonProperty("tags") var tags: ArrayList<String> = arrayListOf(),
    @JsonProperty("subscriptions") var subscriptions: ArrayList<Subscriptions> = arrayListOf(),
    @JsonProperty("agentPools") var agentPools: ArrayList<String> = arrayListOf(),
    @JsonProperty("deleted") var deleted: Boolean? = null,
    @JsonProperty("name") var name: String? = null,
    @JsonProperty("httpMethod") var httpMethod: String? = null,
    @JsonProperty("keywords") var keywords: ArrayList<String> = arrayListOf(),
    @JsonProperty("keywordMode") var keywordMode: String? = null,
    @JsonProperty("maxResponsePageSize") var maxResponsePageSize: Int? = null,
    @JsonProperty("timeout") var timeout: Int? = null,
    @JsonProperty("httpHeaders") var httpHeaders: String? = null,
    @JsonProperty("followRedirect") var followRedirect: Boolean? = null,
    @JsonProperty("treat300AsError") var treat300AsError: Boolean? = null,
    @JsonProperty("checkCertificateExpiration") var checkCertificateExpiration: Boolean? = null,
    @JsonProperty("checkRussianBlackLists") var checkRussianBlackLists: Boolean? = null,
    @JsonProperty("checkDomainExpiration") var checkDomainExpiration: Boolean? = null,
    @JsonProperty("ignoredStatuses") var ignoredStatuses: ArrayList<String> = arrayListOf()
)
