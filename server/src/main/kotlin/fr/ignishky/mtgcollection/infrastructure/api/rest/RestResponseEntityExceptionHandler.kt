package fr.ignishky.mtgcollection.infrastructure.api.rest

import fr.ignishky.mtgcollection.domain.card.exception.NoCardFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [NoCardFoundException::class])
    protected fun handleCardNotFound(ex: NoCardFoundException?, request: WebRequest?) = handleExceptionInternal(ex!!, ex.message, HttpHeaders(), NOT_FOUND, request!!)

}
