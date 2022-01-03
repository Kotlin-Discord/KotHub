/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.kotlindiscord.kothub.rest

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

@Suppress("UnnecessaryAbstractClass")
public abstract class GitHubRestClient {
    protected val client: HttpClient = HttpClient {
        install(JsonFeature)
        modifyClient(this)
    }

    internal suspend inline fun <reified T> get(url: String): T =
        client.get(url) {
            modifyRequest(this)
        }

    protected open fun modifyRequest(request: HttpRequestBuilder) {
        // Nothing by default
    }

    protected open fun modifyClient(config: HttpClientConfig<*>) {
        // Nothing by default
    }
}
