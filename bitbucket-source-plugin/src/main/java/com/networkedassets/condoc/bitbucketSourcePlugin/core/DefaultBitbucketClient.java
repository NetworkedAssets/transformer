package com.networkedassets.condoc.bitbucketSourcePlugin.core;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.networkedassets.condoc.bitbucketSourcePlugin.core.boundary.BitbucketClient;
import com.networkedassets.condoc.bitbucketSourcePlugin.core.boundary.JsonConverter;
import com.networkedassets.condoc.bitbucketSourcePlugin.core.boundary.JsonConverterFactory;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.common.exceptions.TransformerException;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.DefaultRawData;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.Node;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourcePlugin;
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DefaultBitbucketClient implements BitbucketClient {

    private static final int CONNECTION_TIMEOUT = 90000;
    private static final int SOCKET_TIMEOUT = 90000;
    private static final String BITBUCKET_REST_API_URL = "/rest/api/1.0";
    private static final int BITBUCKET_REST_API_PAGE_LIMIT = 1000;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper() {
        private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson
                .databind.ObjectMapper();

        @Override
        public <T> T readValue(String value, Class<T> valueType) {
            try {
                return jacksonObjectMapper.readValue(value, valueType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String writeValue(Object value) {
            try {
                return jacksonObjectMapper.writeValueAsString(value);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };

    @Inject
    private JsonConverterFactory jsonConverterFactory;


    public DefaultBitbucketClient() {

        Unirest.setObjectMapper(getConfiguredObjectMapper());
        Unirest.setHttpClient(getConfiguredHttpClient());
    }

    private static CloseableHttpClient getConfiguredHttpClient() {
        try {

            return HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setSocketTimeout
                    (SOCKET_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).build()).setHostnameVerifier(new
                    AllowAllHostnameVerifier()).setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, (_1,
                                                                                                               _2) ->
                    true).build()).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper getConfiguredObjectMapper() {

        return OBJECT_MAPPER;


    }


    @Override
    public RawData fetchRawData(SourceNodeIdentifier sourceNodeIdentifier, Source settings) {
        Path repoDirectoryPath = cloneRepository(settings.getSettingByKeyOrThrow("username").getValue(), settings
                        .getSettingByKeyOrThrow("password").getValue(), settings.getUrl(),
                sourceNodeIdentifier.getPartOfUnitIdentifier(0), sourceNodeIdentifier.getPartOfUnitIdentifier(1),
                sourceNodeIdentifier.getPartOfUnitIdentifier(2));
        return DefaultRawData.of(repoDirectoryPath);
    }

    @Override
    public SourceStructureRootNode fetchStructure(Source source) {

        SourceStructureRootNode sourceStructureRootNode = new SourceStructureRootNode(source.getId());
        sourceStructureRootNode.setChildren(getSourceStructureNodes(source));
        return sourceStructureRootNode;
    }


    @Override
    public SourcePlugin.VerificationStatus isVerified(Source source) {

        SourcePlugin.VerificationStatus isVerified = SourcePlugin.VerificationStatus.SOURCE_NOT_FOUND;
        try {
            HttpResponse<JsonNode> response = getJsonFrom(source, 0, 25, BITBUCKET_REST_API_URL + "/projects");
            if (response.getStatus() == 200) {
                isVerified = SourcePlugin.VerificationStatus.OK;
            }

            if (response.getStatus() == 401) {
                isVerified = SourcePlugin.VerificationStatus.WRONG_CREDENTIALS;
            }

        } catch (UnirestException ignored) {
        }
        return isVerified;
    }


    public String getRepositoryPath(String sourceIdentifier, String username, String projectName, String repoSlug) {
        return String.format(getUnformattedRepositoryPath(sourceIdentifier), username, projectName, repoSlug);
    }

    /**
     * Clones repository and returns path to this repository
     *
     * @param username   Bitbucket password
     * @param password   Bitbucket password
     * @param repoSlug   Name of the repository
     * @param branchName Name of the branch
     * @return Path to the cloned repository
     * @throws RuntimeException
     */
    private Path cloneRepository(String username, String password, String sourceIdentifier, String projectName, String
            repoSlug, String branchName) {
        try {
            Path localRepoDirectory = getLocalRepoDirectory();
            String formattedRepoUrl = getRepositoryPath(sourceIdentifier, username, projectName, repoSlug);

            Git.cloneRepository().setURI(formattedRepoUrl).setDirectory(localRepoDirectory.toFile()).setBranch
                    (branchName).setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                    .call();

            return localRepoDirectory;
        } catch (GitAPIException e) {
            throw new RuntimeException("There was an error while cloning the repository.. " + e);
        }
    }


    private String getUnformattedRepositoryPath(String sourceIdentifier) {
        try {
            URL baseUrl = new URL(sourceIdentifier);
            StringBuilder uriTemplate = new StringBuilder();
            if (isPortSet(baseUrl)) {
                uriTemplate.append(baseUrl.getProtocol()).append("://%s@").append(baseUrl.getHost()).append(':')
                        .append(baseUrl.getPort()).append(baseUrl.getPath()).append("/scm/%s/%s.git");
            } else {
                uriTemplate.append(baseUrl.getProtocol()).append("://%s@").append(baseUrl.getHost()).append(baseUrl
                        .getPath()).append("/scm/%s/%s.git");
            }

            return uriTemplate.toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Couldn't parse url.. " + e);
        }
    }


    private boolean isPortSet(URL baseUrl) {
        return baseUrl.getPort() != -1;
    }

    private Path getLocalRepoDirectory() {
        try {
            return Files.createTempDirectory(null);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't create a temp dir!", e);
        }
    }

    private List<Node> getSourceStructureNodes(Source source) {
        List<Node> projects = getSourceUnitsFor(source, BITBUCKET_REST_API_URL + "/projects", new
                SourceNodeIdentifier(source.getId()));

        projects.forEach(project -> getSourceUnitsFor(source, String.format("%s/projects/%s/repos",
                BITBUCKET_REST_API_URL, project.getSourceNodeIdentifier().getLastPartOfUnitIdentifier()), project
                .getSourceNodeIdentifier()).stream().filter(Objects::nonNull).forEach(repo -> {
            project.addChildren(repo);
            getSourceUnitsFor(source, String.format("%s/projects/%s/repos/%s/branches", BITBUCKET_REST_API_URL,
                    project.getSourceNodeIdentifier().getLastPartOfUnitIdentifier(), repo.getSourceNodeIdentifier()
                            .getLastPartOfUnitIdentifier()), repo.getSourceNodeIdentifier()).stream().filter
                    (Objects::nonNull).forEach(repo::addChildren);
        }));

        return projects;
    }

    private List<Node> getSourceUnitsFor(
            final Source source,
            final String request,
            final SourceNodeIdentifier parentSourceNodeIdentifier
    ) {
        int start = 0;
        int limit = BITBUCKET_REST_API_PAGE_LIMIT;
        ArrayList<Node> sourceUnits = new ArrayList<>();
        JSONObject jsonObject;

        do {
            try {
                jsonObject = getJsonFrom(source, start, limit, request).getBody().getObject();
                start += limit + 1;
                JsonConverter jsonConverter = this.jsonConverterFactory.getConverterForUrl(request);
                jsonConverter.setParentNodeIdentifier(parentSourceNodeIdentifier);
                sourceUnits.addAll(jsonConverter.convert(jsonObject));
            } catch (UnirestException e) {
                throw new TransformerException("Problem with get json data from " + request, e);
            }
        } while (!isLastPage(jsonObject));

        return sourceUnits;
    }

    private HttpResponse<JsonNode> getJsonFrom(
            final Source source,
            final int start,
            final int limit,
            final String request
    ) throws UnirestException {
        try {
            return Unirest.get(source.getUrl() + request).queryString("start", start).queryString("limit", limit)
                    .basicAuth(source.getSettingByKeyOrThrow("username").getValue(), source.getSettingByKeyOrThrow
                            ("password").getValue()).header("accept", "application/json").asJson();
        } catch (Exception e) {
            if (e instanceof UnirestException) throw e;
            throw new UnirestException(e);
        }
    }

    private boolean isLastPage(JSONObject object) {
        return object.optBoolean("isLastPage", true);
    }
}



