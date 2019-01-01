package jollobajano.test.application;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import jollobajano.test.ResourceModel;

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
