package com.networkedassets.condoc.transformer.retrieveDocumentation.delivery.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networkedassets.condoc.transformer.RestResource;
import com.networkedassets.condoc.transformer.common.Documentation;
import com.networkedassets.condoc.transformer.common.docitem.ClassDiagramDocItem;
import com.networkedassets.condoc.transformer.common.docitem.DocItem;
import com.networkedassets.condoc.transformer.common.docitem.OmniDocItem;
import com.networkedassets.condoc.transformer.common.exceptions.DocItemNotFoundException;
import com.networkedassets.condoc.transformer.common.exceptions.DocumentationNotFoundException;
import com.networkedassets.condoc.transformer.retrieveDocumentation.core.boundary.DocItemSearchService;
import com.networkedassets.condoc.transformer.retrieveDocumentation.core.boundary.DocumentationManager;
import com.networkedassets.condoc.transformer.util.functional.Optionals;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Optional;

@Path("documentation")
public class DocumentationResource extends RestResource {

    @Inject
    DocumentationManager documentationManager;

    @Inject
    DocItemSearchService docItemSearchService;

    @Path("sourceUnit/{sourceUnitId:\\d+}/documentationType/{documentationType}")
    @GET
    @JsonView(Documentation.Views.Content.class)
    public Response getDocumentationByType(@PathParam("sourceUnitId") int sourceUnitId,
                                           @PathParam("documentationType") String documentationType) {

        return Response
                .ok(documentationManager.get(sourceUnitId, documentationType)
                        .orElseThrow(DocumentationNotFoundException::new))
                .build();
    }

    @Path("sourceUnit/{sourceUnitId:\\d+}/documentationType/omnidoc/tree")
    @GET
    @JsonView(Documentation.Views.Content.class)
    public Response getDocumentationByTypeAsTopPackageTrees(@PathParam("sourceUnitId") int sourceUnitId) {
        return Response
                .ok(documentationManager.getOmniDocumentationAsTopPackageTrees(sourceUnitId))
                .build();
    }

    @Path("sourceUnit/{sourceUnitId:\\d+}/documentationType/omnidoc/singleRootTree")
    @GET
    @JsonView(Documentation.Views.Content.class)
    public Response getDocumentationByTypeAsSingleRootTree(@PathParam("sourceUnitId") int sourceUnitId) {
        return Response
                .ok(documentationManager.getOmniDocumentationAsSingleRootTree(sourceUnitId))
                .build();
    }

    @Path("sourceUnit/{sourceUnitId:\\d+}/documentationType/{documentationType}/docItem/{name}")
    @GET
    @JsonView(Documentation.Views.Content.class)
    public Response getDocumentationByType(@PathParam("sourceUnitId") int sourceUnitId,
                                           @PathParam("documentationType") String documentationType,
                                           @PathParam("name") String fullyQualifiedName) throws IOException {
        DocItem docItem = documentationManager.getDocItemByName(sourceUnitId, documentationType, fullyQualifiedName)
                .orElseThrow(DocItemNotFoundException::new);

        if (docItem instanceof OmniDocItem) {
            ((OmniDocItem) docItem).getMemberCategories().forEach(cat -> cat.getItems().forEach(member -> {
                if (member.getData() == null) {
                    member.setData((OmniDocItem) documentationManager.getDocItemByName(sourceUnitId, documentationType, member.getFullName()).orElse(null));
                }
            }));
        }

        return Response
                .ok(docItem)
                .build();
    }

    private Response getClassDiagram(int sourceUnitId, String fullyQualifiedName) throws IOException {
        return Optionals.throwingFlatMap(documentationManager.getDocItemByName(sourceUnitId, ClassDiagramDocItem.class, ""), diagram -> {
            for (JsonNode n : OM.readTree(diagram.getContent()).get("pieces")) {
                if (n.get("pieceName").asText().equals(fullyQualifiedName)) {
                    return Optional.of(Response.ok(n.get("content").asText()).build());
                }
            }
            return Optional.empty();
        }).orElse(null);
    }

    private final ObjectMapper OM = new ObjectMapper();

    @Path("sourceUnit/{sourceUnitId:\\d+}/search")
    @GET
    public Response searchDocumentation(@PathParam("sourceUnitId") int sourceUnitId,
                                        @QueryParam("query") String query,
                                        @DefaultValue("20") @QueryParam("max") int maxResults) {
        return Response.ok(docItemSearchService.searchDocItemsInSourceUnit(sourceUnitId, query, maxResults)).build();
    }

}
