/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.kotlindiscord.kothub.rest

import io.ktor.client.HttpClientConfig
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.BasicAuthCredentials
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers

public class OAuthRestClient(
    private val clientId: String,
    private val clientSecret: String
) : GitHubRestClient() {
    private var userToken: String? = null

    public constructor(clientId: String, clientSecret: String, token: String?) : this(clientId, clientSecret) {
        userToken = token
    }

    public fun setToken(token: String?) {
        userToken = token
    }

    override fun modifyClient(config: HttpClientConfig<*>) {
        config.install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(clientId, clientSecret)
                }
            }
        }
    }

    override fun modifyRequest(request: HttpRequestBuilder) {
        if (userToken != null) {
            request.headers {
                append("Authorization", "token $userToken")
            }
        }
    }
}
