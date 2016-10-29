package com.networkedassets.condoc.githubSourcePlugin.core;


import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.networkedassets.condoc.githubSourcePlugin.core.boundary.GithubClient;
import com.networkedassets.condoc.githubSourcePlugin.core.boundary.GithubHeaderParser;
import com.networkedassets.condoc.githubSourcePlugin.core.boundary.JsonConverter;
import com.networkedassets.condoc.githubSourcePlugin.core.boundary.JsonConverterFactory;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
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

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.networkedassets.condoc.githubSourcePlugin.core.entity.GithubEvent.OWNER_SEPARATOR;
import static com.networkedassets.condoc.githubSourcePlugin.core.entity.GithubEvent.REPOSITORY_SEPARATOR;

public class DefaultGithubClient implements GithubClient {

    private static final int CONNECTION_TIMEOUT = 90000;
    private static final int SOCKET_TIMEOUT = 90000;
    private static final String GITHUB_REST_API_URL = "https://api.github.com/";
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
    @Inject
    private GithubHeaderParser githubHeaderParser;


    public DefaultGithubClient() {

        Unirest.setObjectMapper(getConfiguredObjectMapper());
        Unirest.setHttpClient(getConfiguredHttpClient());
    }

    private static CloseableHttpClient getConfiguredHttpClient() {
        try {

            return HttpClients.custom().setDefaultRequestConfig(
                    RequestConfig.custom()
                            .setSocketTimeout(SOCKET_TIMEOUT)
                            .setConnectTimeout(CONNECTION_TIMEOUT)
                            .build())
                    .setHostnameVerifier(new AllowAllHostnameVerifier())
                    .setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, (_1, _2) -> true).build())
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper getConfiguredObjectMapper() {
        return OBJECT_MAPPER;
    }


    public RawData fetchRawData(SourceNodeIdentifier sourceNodeIdentifier, Source settings) {
        String[] sourceNodeOwnerAndRepository = sourceNodeIdentifier.getUnitIdentifier().split(OWNER_SEPARATOR);
        String[] sourceNodeRepoNameAndRef = sourceNodeOwnerAndRepository[1].split("\\" + REPOSITORY_SEPARATOR);

        Path repoDirectoryPath = cloneRepository(
                settings.getSettingByKeyOrThrow("username").getValue(),
                settings.getSettingByKeyOrThrow("password").getValue(),
                settings.getUrl(),
                sourceNodeRepoNameAndRef[0],
                sourceNodeRepoNameAndRef[1]
        );

        return DefaultRawData.of(repoDirectoryPath);
    }

    /**
     * Clones repository and returns path to this repository
     *
     * @param username         Github username
     * @param password         Github password
     * @param sourceIdentifier Identifier of the source, ie. https://github.com/
     * @param repoName         Name of the repository
     * @param branchName       Name of the branch
     * @return Path to the cloned repository
     * @throws RuntimeException
     */
    private Path cloneRepository(String username, String password, String sourceIdentifier, String
            repoName, String branchName) {
        try {
            Path localRepoDirectory = getLocalRepoDirectory();
            String formattedRepoUrl = String.format("%s/%s.git", sourceIdentifier, repoName);

            Git.cloneRepository().setURI(formattedRepoUrl).setDirectory(localRepoDirectory.toFile()).setBranch
                    (branchName).setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                    .call();

            return localRepoDirectory;
        } catch (GitAPIException e) {
            throw new RuntimeException("There was an error while cloning the repository.. " + e);
        }
    }

    private Path getLocalRepoDirectory() {
        try {
            return Files.createTempDirectory(null);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't create a temp dir!", e);
        }
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
            HttpResponse<JsonNode> response = getJsonFrom(1, source, GITHUB_REST_API_URL + "/user/repos");
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

    private List<Node> getSourceStructureNodes(Source source) {

        List<Node> repos = getSourceUnitsFor(source, GITHUB_REST_API_URL + "/user/repos",
                new SourceNodeIdentifier(source.getId()));

        repos.forEach(repo -> getSourceUnitsFor(
                source,
                String.format(
                        "%s/repos/%s/branches",
                        GITHUB_REST_API_URL,
                        repo.getSourceNodeIdentifier().getLastPartOfUnitIdentifier()
                ),
                repo.getSourceNodeIdentifier()
        ).stream().filter(Objects::nonNull).forEach(repo::addChildren));

        return repos;
    }

    private List<Node> getSourceUnitsFor(final Source source, final String request, final SourceNodeIdentifier
            parentSourceNodeIdentifier) {
        int page = 1;
        ArrayList<Node> sourceUnits = new ArrayList<>();
        JsonConverter jsonConverter = this.jsonConverterFactory.getConverterForUrl(request);
        jsonConverter.setParentNodeIdentifier(parentSourceNodeIdentifier);
        HttpResponse<JsonNode> response;

        do {
            try {
                response = getJsonFrom(page, source, request);
                if (response.getStatus() != 200) throw new InvalidJsonException(response.getBody().toString());
                sourceUnits.addAll(jsonConverter.convert(response.getBody()));
                page += 1;
            } catch (UnirestException e) {
                throw new RuntimeException("Problem with get json data from" + request, e);
            }
        } while (!isLastPage(page, response.getHeaders()));

        return sourceUnits;
    }

    private HttpResponse<JsonNode> getJsonFrom(final int start, final Source source, final String request) throws
            UnirestException {


        return Unirest.get(request).queryString("page", start).basicAuth(source.getSettingByKeyOrThrow("username")
                .getValue(), source.getSettingByKeyOrThrow("password").getValue()).header("accept",
                "application/json").asJson();

    }

    private boolean isLastPage(int page, Headers headers) {

        return !(headers.containsKey("Link") && githubHeaderParser.getLastPage(headers.getFirst("Link")) < page);

    }


}



