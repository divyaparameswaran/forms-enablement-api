package com.ch.resources;

import static com.ch.service.LoggingService.LoggingLevel.INFO;
import static com.ch.service.LoggingService.tag;

import com.ch.application.FormServiceConstants;
import com.ch.application.FormsServiceApplication;
import com.ch.configuration.CompaniesHouseConfiguration;
import com.ch.conversion.config.TransformConfig;
import com.ch.helpers.ClientHelper;
import com.ch.helpers.QueueHelper;
import com.ch.model.FormStatus;
import com.ch.model.QueueRequest;
import com.ch.service.LoggingService;
import com.codahale.metrics.Timer;
import io.dropwizard.auth.Auth;
import org.json.JSONObject;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Aaron.Witter on 18/07/2016.
 */
@Path("/queue")
public class QueueResource {
  private static final Timer timer = FormsServiceApplication.registry.timer("QueueResource");
  private final ClientHelper client;
  private final CompaniesHouseConfiguration configuration;
  private QueueHelper helper = new QueueHelper(new TransformConfig());


  public QueueResource(ClientHelper client, CompaniesHouseConfiguration configuration) {
    this.configuration = configuration;
    this.client = client;
  }


  /**
   * Resource to request forms from the queue.
   *
   * @return json with response from CHIPS
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response postForms(@Auth
                            QueueRequest request) {
    final Timer.Context context = timer.time();
    try {

      //get params from request
      String requestStatus = request.getFormStatus();
      int count = request.getCount();

      //retrieve forms from db and rebuild
      List<JSONObject> packages = helper.getCompletePackagesByStatus(requestStatus, count);


      for (JSONObject pack : packages) {

        // post to CHIPS
        Response response = client.postJson(configuration.getChipsApiUrl(), pack.toString());

        LoggingService.log(tag, INFO, "Response from CHIPS: " + response.toString(),
          FormSubmissionResource.class);

        helper.processResponse(response, pack.getInt(FormServiceConstants.PACKAGE_IDENTIFIER_KEY));
      }

      return Response.ok("All packages have been processed").build();

    } finally {
      context.stop();
    }
  }
}