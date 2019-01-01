package jollobajano.test.beans;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import jollobajano.test.ResourceModel;

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
