package jollobajano.test;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Interceptor
@ResourceModel
public class ResourceModelInterceptor {
	private static final Logger log = LoggerFactory.getLogger(ResourceModelInterceptor.class);

	@AroundInvoke
	public Object aroundMethod(InvocationContext ctx) throws Exception {
		log.info("Before");
		return ctx.proceed();
	}
}
