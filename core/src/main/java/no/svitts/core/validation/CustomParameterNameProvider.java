package no.svitts.core.validation;

import javax.validation.ParameterNameProvider;
import javax.validation.Validation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class CustomParameterNameProvider implements ParameterNameProvider {

    private final ParameterNameProvider parameterNameProvider;

    public CustomParameterNameProvider() {
        parameterNameProvider = Validation.byDefaultProvider().configure().getDefaultParameterNameProvider();
    }

    @Override
    public List<String> getParameterNames(final Constructor<?> constructor) {
        return parameterNameProvider.getParameterNames(constructor);
    }

    @Override
    public List<String> getParameterNames(final Method method) {
        return parameterNameProvider.getParameterNames(method);
    }

}
