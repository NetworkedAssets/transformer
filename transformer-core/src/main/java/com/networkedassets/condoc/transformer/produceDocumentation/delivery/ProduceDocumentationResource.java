package com.networkedassets.condoc.transformer.produceDocumentation.delivery;

import com.networkedassets.condoc.transformer.RestResource;
import com.networkedassets.condoc.transformer.produceDocumentation.core.boundary.DocumentationProducer;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("produce")
public class ProduceDocumentationResource extends RestResource {

    @Inject
    DocumentationProducer documentationProducer;

    @POST
    @Path("{bundleId:\\d+}")
    public Response produceDocumentation(@PathParam("bundleId") int bundleId) {
        new Thread(() -> documentationProducer.produceDocumentationForBundle(bundleId)).start();
        return Response.accepted().build();
    }

}
