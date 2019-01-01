Maven Multi Jar Webapp with Embedded Interceptor
================================================

This is a _minimal working example_, a MWE, showing how to enable
interceptors in a multi jar JavaEE CDI application.

What gave me a big aha! moment was the following tip out of _JBoss
Weld CDI for Java Platform Learn CDI concepts and develop modern web
applications using JBoss Weld_ by Ken Finnigan:

> Activation of an interceptor within beans.xml will activate it for
> all beans within the same archive only.

This is the root of many "why woun't my interceptor work?" questions
on StackOverflow.



This project layout 
-------------------

This project consists of a root project POM (my standard issue POM for
J2EE projects. It's a bit heavy so don't spend any time on it.), and
three child projects/modules: _application_, _beans_ and _intercept_.

_intercept_ contains an interceptor binding, `@ResourceModel`, and
it's interceptor.


	@InterceptorBinding
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ResourceModel {

	}

and 

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


_application_ contains the JAX-RS application ("api") and one REST
endpoint ("test"). The endpoint is marked by the `@ResourceModel`
annotation.

	@ApplicationPath("/api")
	public class TestApplication extends Application {}

and

	@ResourceModel
	@Path("test")
	public class TestEndpoint {

		private String resource = "Hello World!";

		@GET
		@Produces("test/plain")
		public Response simpleGet() {
			return Response.ok(resource)
					.build();

		}
	}



_beans_ lastly, contains another endpoint ("beans"). This is also
annotated with the `@ResourceModel` interceptor binding.


	@ResourceModel
	@Path("beans")
	public class BeansResource {

		private String resource = "Hello Beans!";

		@GET
		@Produces("test/plain")
		public Response simpleGet() {
			return Response.ok(resource)
					.build();

		}
	}




There is a `beans.xml` template file enabling the
`jollobajano.test.ResourceModelInterceptor` from the _intercept_
module. 

	<beans
		xmlns="http://java.sun.com/xml/ns/javaee"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/beans_1_0.xsd">
		<interceptors>
		<class>jollobajano.test.ResourceModelInterceptor</class>
		</interceptors>
	</beans>



This template can be found in the root of this project.



Where to put the `beans.xml`
----------------------------

Taking the hint from the tip from the book.... we _need to enable the
interceptor in all the modules where we want it to intercept_. This
means that for _beans_ we copy the file into the
`src/main/resources/META-INF` directory.

Since _application_ is a `war` type module, we copy the interceptor
enabeling `beans.xml` into the `src/main/webapp/WEB-INF` of that
module.

I first experimented with presence and absence of the `beans.xml` file
in the _application_ and _beans_ modules and was able to control
whether the interceptor would work in each particular module that way.



Conclusion
----------

The way I see it, enabeling an interceptor via the `beans.xml` file
tells the CDI framework, e.g. Weld, to _enable the interceptor for the
archive that contains the enabeling `beans.xml`_.
