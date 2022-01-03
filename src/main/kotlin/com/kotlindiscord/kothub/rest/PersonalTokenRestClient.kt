/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.kotlindiscord.kothub.rest

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers

public class PersonalTokenRestClient(private val token: String? = null) : GitHubRestClient() {
    override fun modifyRequest(request: HttpRequestBuilder) {
        if (token != null) {
            request.headers {
                append("Authorization", "token $token")
            }
        }
    }
}
