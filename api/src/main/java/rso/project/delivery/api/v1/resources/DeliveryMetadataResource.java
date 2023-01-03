package rso.project.delivery.api.v1.resources;

import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import rso.project.delivery.lib.DeliveryMetadata;
import rso.project.delivery.services.beans.DeliveryMetadataBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@Log
@ApplicationScoped
@Path("/deliveries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeliveryMetadataResource {

    private final Logger log = Logger.getLogger(DeliveryMetadataResource.class.getName());
    @Context
    protected UriInfo uriInfo;
    @Inject
    private DeliveryMetadataBean deliveryMetadataBean;

    @Operation(description = "Get all delivery metadata.", summary = "Get all metadata")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of delivery metadata",
                    content = @Content(schema = @Schema(implementation = DeliveryMetadata.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getDeliveryMetadata() {

        List<DeliveryMetadata> deliveryMetadata = deliveryMetadataBean.getDeliveryMetadataFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(deliveryMetadata).build();
    }


    @Operation(description = "Get metadata for a delivery.", summary = "Get metadata for a delivery")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Delivery metadata",
                    content = @Content(
                            schema = @Schema(implementation = DeliveryMetadata.class))
            )})
    @GET
    @Path("/{deliveryMetadataId}")
    public Response getDeliveryMetadata(@Parameter(description = "Metadata ID.", required = true)
                                        @PathParam("deliveryMetadataId") Integer imageMetadataId) {

        DeliveryMetadata deliveryMetadata = deliveryMetadataBean.getDeliveryMetadata(imageMetadataId);

        if (deliveryMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(deliveryMetadata).build();
    }

    @Operation(description = "Add delivery metadata.", summary = "Add metadata")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Metadata successfully added."
            ),
            @APIResponse(responseCode = "405", description = "Validation error .")
    })
    @POST
    public Response createDeliveryMetadata(@RequestBody(
            description = "DTO object with image metadata.",
            required = true, content = @Content(
            schema = @Schema(implementation = DeliveryMetadata.class))) DeliveryMetadata deliveryMetadata) {

        // || imageMetadata.getUri() == null
        if ((deliveryMetadata.getRestaurantName() == null || deliveryMetadata.getDescription() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            deliveryMetadata = deliveryMetadataBean.createDeliveryMetadata(deliveryMetadata);
        }

        return Response.status(Response.Status.CONFLICT).entity(deliveryMetadata).build();

    }


    @Operation(description = "Update metadata for a delivery.", summary = "Update metadata")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Metadata successfully updated."
            )
    })
    @PUT
    @Path("{deliveryMetadataId}")
    public Response putDeliveryMetadata(@Parameter(description = "Metadata ID.", required = true)
                                        @PathParam("deliveryMetadataId") Integer deliveryMetadataId,
                                        @RequestBody(
                                                description = "DTO object with delivery metadata.",
                                                required = true, content = @Content(
                                                schema = @Schema(implementation = DeliveryMetadata.class)))
                                        DeliveryMetadata deliveryMetadata) {

        deliveryMetadata = deliveryMetadataBean.putDeliveryMetadata(deliveryMetadataId, deliveryMetadata);

        if (deliveryMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @Operation(description = "Delete metadata for a delivery.", summary = "Delete metadata")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Metadata successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{deliveryMetadataId}")
    public Response deleteDeliveryMetadata(@Parameter(description = "Metadata ID.", required = true)
                                           @PathParam("deliveryMetadataId") Integer deliveryMetadataId) {

        boolean deleted = deliveryMetadataBean.deleteDeliveryMetadata(deliveryMetadataId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
