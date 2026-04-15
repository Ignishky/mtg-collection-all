package fr.ignishky.mtgcollection.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import fr.ignishky.mtgcollection.infrastructure.TestUtils.readFile
import org.apache.http.HttpHeaders.CONTENT_TYPE
import org.apache.http.HttpStatus.SC_OK
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.springframework.http.HttpMethod.GET
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class MockServerBuilder(
    private val mockServer: MockServerClient,
) {

    fun prepareSets(fileName: String) {
        mockServer
            .`when`(
                request()
                    .withMethod(GET.name())
                    .withPath("/sets")
            )
            .respond(
                response()
                    .withStatusCode(SC_OK)
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withBody(readFile("refresh/${fileName}"))
            )
    }

    fun prepareCards(vararg setCode: String) {
        mockServer
            .`when`(
                request()
                    .withMethod(GET.name())
                    .withPath("/bulk-data/default_cards")
            )
            .respond(
                response()
                    .withStatusCode(SC_OK)
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withBody(
                        """{
              "object": "bulk_data",
              "type": "default_cards",
              "download_uri": "http://localhost:${mockServer.port}/bulk-data/default_cards/download"
            }"""
                    )
            )

        val cards = setCode.flatMap {
            val fileContent = readFile("refresh/scryfall_cards_$it.json")
            val jsonNode = ObjectMapper().readTree(fileContent.replace("{baseUrl}", "http://localhost:${mockServer.port}"))
            jsonNode.get("data").asIterable()
        }

        mockServer
            .`when`(
                request()
                    .withMethod(GET.name())
                    .withPath("/bulk-data/default_cards/download")
            )
            .respond(
                response()
                    .withStatusCode(SC_OK)
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withBody(ObjectMapper().writeValueAsString(cards))
            )
    }

}
