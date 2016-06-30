package no.svitts.core.module;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import no.svitts.core.exception.mapper.ConstraintViolationExceptionMapper;
import no.svitts.core.exception.mapper.WebApplicationExceptionMapper;
import no.svitts.core.json.GsonMessageBodyReader;
import no.svitts.core.json.GsonMessageBodyWriter;
import no.svitts.core.movie.Movie;
import no.svitts.core.service.MovieService;
import no.svitts.core.service.Service;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

public class WebModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeLiteral<Service<Movie>>(){}).to(MovieService.class);
        binder.bind(new TypeLiteral<ExceptionMapper<WebApplicationException>>(){}).to(WebApplicationExceptionMapper.class);
        binder.bind(new TypeLiteral<ExceptionMapper<ConstraintViolationException>>(){}).to(ConstraintViolationExceptionMapper.class);
        binder.bind(new TypeLiteral<MessageBodyReader<Object>>(){}).to(GsonMessageBodyReader.class);
        binder.bind(new TypeLiteral<MessageBodyWriter<Object>>(){}).to(GsonMessageBodyWriter.class);
    }

}
