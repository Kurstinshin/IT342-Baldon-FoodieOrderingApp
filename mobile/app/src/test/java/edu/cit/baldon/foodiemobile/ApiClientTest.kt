package edu.cit.baldon.foodiemobile

import org.junit.Test
import org.junit.Assert.*
import edu.cit.baldon.foodiemobile.shared.api.ApiClient

/**
 * Basic unit test to verify API Client configuration.
 */
class ApiClientTest {
    @Test
    fun apiClient_isNotNull() {
        val service = ApiClient.service
        assertNotNull("API Service should not be null", service)
    }
}
