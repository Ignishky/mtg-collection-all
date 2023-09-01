package fr.ignishky.mtgcollection.configuration.webmvc

import fr.ignishky.framework.domain.CorrelationIdGenerator
import jakarta.inject.Named
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

@Named
class CorrelationIdInterceptor(private val correlationIdGenerator: CorrelationIdGenerator) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        request.setAttribute("correlationId", correlationIdGenerator.generate())

        return true
    }
}
