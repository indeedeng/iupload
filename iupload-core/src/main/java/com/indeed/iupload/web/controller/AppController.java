package com.indeed.iupload.web.controller;

import com.indeed.util.core.io.Closeables2;
import com.indeed.iupload.core.authentification.User;
import com.indeed.iupload.core.authentification.UserPermission;
import com.indeed.iupload.core.authentification.UserPermissionProvider;
import com.indeed.iupload.core.domain.FileInfo;
import com.indeed.iupload.core.domain.FileStatus;
import com.indeed.iupload.core.domain.Index;
import com.indeed.iupload.core.domain.IndexRepository;
import com.indeed.iupload.web.exception.BadRequestException;
import com.indeed.iupload.web.exception.ResourceNotFoundException;
import com.indeed.iupload.web.exception.UnauthorizedOperationException;
import com.indeed.iupload.web.response.BasicResponse;
import com.indeed.iupload.web.KerberosUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author akira
 */
@Controller
public class AppController {
    private final Logger log = Logger.getLogger(AppController.class);

    @Value("${hdfs.base.path.qa}")
    private String qaBasePath;

    @Value("${hdfs.base.path.prod}")
    private String prodBasePath;

    @Value("${use.qa.repository}")
    private String useQaRepo;

    @Autowired
    private UserPermissionProvider permissionProvider;

    @Autowired
    public AppController(Environment env) {
        try {
            KerberosUtils.loginFromKeytab(env.getProperty("kerberos.principal"), env.getProperty("kerberos.keytab"));
        } catch (IOException e) {
            log.warn("Kerberos loging from keytab failed");
        }
    }

    IndexRepository getRepository(String id) throws IOException {
        if (id.equals("prod")) {
            return new IndexRepository("prod", prodBasePath);
        } else if ("true".equals(useQaRepo) && id.equals("qa")) {
            return new IndexRepository("qa", qaBasePath);
        } else {
            throw new ResourceNotFoundException("The specified repository not found.");
        }
    }

    protected UserPermission getUserPermission(HttpServletRequest request) {
        User user = new User(AppController.getUserNameFromRequest(request));
        return permissionProvider.provideUserPermission(user);
    }

    protected boolean checkUserQualification(
            HttpServletRequest request,
            String repositoryName) {
        return getUserPermission(request).isWritable(repositoryName);
    }

    protected boolean checkUserQualification(
            HttpServletRequest request,
            String repositoryName,
            String indexName) {
        return getUserPermission(request).isWritable(repositoryName, indexName);
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public
    @ResponseBody
    BasicResponse handleResourceNotFoundException(ResourceNotFoundException e) {
        return BasicResponse.error(e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedOperationException.class)
    public
    @ResponseBody
    BasicResponse handleResourceNotFoundException() {
        return BasicResponse.error("You are not allowed to process this operation");
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public
    @ResponseBody
    BasicResponse handleBadRequestException(BadRequestException e) {
        return BasicResponse.error(e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public
    @ResponseBody
    BasicResponse handleGeneralException(IOException e) {
        log.error(e.getMessage());
        return BasicResponse.error(e.getMessage());
    }

    @RequestMapping(value = "/checkAuth/{repositoryId}/{operationName}/", method = RequestMethod.GET)
    public
    @ResponseBody
    BasicResponse isWriteAllowed(
            @PathVariable String repositoryId,
            @PathVariable String operationName,
            HttpServletRequest request
    ) throws Exception {
        if (operationName == null) {
            throw new ResourceNotFoundException("Invalid operation type");
        }
        final boolean isAuthorized = checkUserQualification(request, repositoryId);
        return BasicResponse.success(new HashMap<String, Object>() {{
            put("authorized", isAuthorized);
        }});
    }

    @RequestMapping(value = "/user/permission", method = RequestMethod.GET)
    public
    @ResponseBody
    BasicResponse executeShowUserPermission(
            final HttpServletRequest request
    ) {
        return BasicResponse.success(new HashMap<String, Object>() {{
            put("permission", getUserPermission(request));
        }});
    }

    @RequestMapping(value = "/private/reloadUserPermissions")
    public
    @ResponseBody
    BasicResponse
    executeForceReloadConfiguration() throws Exception {
        permissionProvider.reload();
        return BasicResponse.success(null);
    }


    @RequestMapping(value = "/repository/", method = RequestMethod.GET)
    public
    @ResponseBody
    BasicResponse
    executeRepositoryList() throws Exception {
        final List<IndexRepository> repos = new ArrayList<IndexRepository>();
        if ("true".equals(useQaRepo)) {
            repos.add(getRepository("qa"));
        }
        repos.add(getRepository("prod"));
        return BasicResponse.success(new HashMap<String, Object>() {
            {
                put("repositories", repos);
            }
        });
    }

    @RequestMapping(value = "/repository/{repoId}/index/", method = RequestMethod.GET)
    public
    @ResponseBody
    BasicResponse executeIndexList(
            @PathVariable String repoId) throws Exception {
        IndexRepository repository = getRepository(repoId);
        final List<Index> indexList = repository.getIndexList();
        return BasicResponse.success(new HashMap<String, Object>() {{
            put("indexes", indexList);
        }});
    }

    @RequestMapping(value = "/repository/{repoId}/index/", method = RequestMethod.POST)
    public
    @ResponseBody
    BasicResponse executeCreateIndex(
            @PathVariable String repoId,
            @RequestParam("name") String name,
            HttpServletRequest request) throws Exception {
        if (!checkUserQualification(request, repoId)) {
            throw new UnauthorizedOperationException();
        }
        getRepository(repoId).createIndex(name);
        return BasicResponse.success(null);
    }

    @RequestMapping(value = "/repository/{repoId}/index/{indexName}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    BasicResponse executeDeleteIndex(
            @PathVariable String repoId,
            @PathVariable String indexName,
            HttpServletRequest request) throws Exception {
        if (!checkUserQualification(request, repoId)) {
            throw new UnauthorizedOperationException();
        }
        IndexRepository repository = getRepository(repoId);
        repository.deleteIndex(indexName);
        return BasicResponse.success(null);
    }

    @RequestMapping(value = "/repository/{repoId}/index/{indexName}/file/", method = RequestMethod.GET)
    public
    @ResponseBody
    BasicResponse executeFileList(
            @PathVariable String repoId,
            @PathVariable String indexName) throws Exception {
        final Index index = getRepository(repoId).find(indexName);
        if (index == null) {
            throw new ResourceNotFoundException("No index found.");
        }
        return BasicResponse.success(new HashMap<String, Object>() {{
            put("files", index.getFileList());
        }});
    }

    @RequestMapping(value = "/repository/{repoId}/index/{indexName}/file/", method = RequestMethod.POST)
    public
    @ResponseBody
    BasicResponse executeCreateFile(
            @PathVariable String repoId,
            @PathVariable String indexName,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) throws Exception {
        if (!checkUserQualification(request, repoId, indexName)) {
            throw new UnauthorizedOperationException();
        }
        if (file.isEmpty()) {
            return BasicResponse.error("File is not specified");
        }
        IndexRepository repository = getRepository(repoId);
        Index index = repository.find(indexName);
        InputStream is = file.getInputStream();
        try {
            index.createFileToIndex(file.getOriginalFilename(), is);
        } finally {
            Closeables2.closeQuietly(is, log);
        }
        return BasicResponse.success(null);
    }

    @RequestMapping(value = "/repository/{repoId}/index/{indexName}/file/{fileStatus}/{fileName}", method = RequestMethod.GET)
    public void executeDownloadFile(
            @PathVariable String repoId,
            @PathVariable String indexName,
            @PathVariable String fileStatus,
            @PathVariable String fileName,
            HttpServletResponse response) {
        try {
            IndexRepository repository = getRepository(repoId);
            Index index = repository.find(indexName);
            FileInfo file = index.findFile(FileStatus.fromName(fileStatus), fileName);
            if (file == null) {
                throw new BadRequestException("file not found");
            }
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
            InputStream is = file.getInputStream();
            try {
                org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            } finally {
                Closeables2.closeQuietly(is, log);
            }
            response.flushBuffer();
        } catch (IOException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @RequestMapping(value = "/repository/{repoId}/index/{indexName}/file/{fileStatus}/{fileName}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    BasicResponse executeDeleteFile(
            @PathVariable String repoId,
            @PathVariable String indexName,
            @PathVariable String fileStatus,
            @PathVariable String fileName,
            HttpServletRequest request) throws Exception {
        if (!checkUserQualification(request, repoId, indexName)) {
            throw new UnauthorizedOperationException();
        }
        IndexRepository repository = getRepository(repoId);
        Index index = repository.find(indexName);
        index.deleteFile(FileStatus.fromName(fileStatus), fileName);
        return BasicResponse.success(null);
    }
    
    /**
     * Gets the user name from the HTTP request if it was provided through Basic authentication.
     * 
     * @param request Http request
     * @return User name if Basic auth is used or null otherwise
     */
    private static String getUserNameFromRequest(final HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            // try simple
            final String rawUser = request.getRemoteUser();
            if (rawUser == null) {
                return null;
            } else {
                return rawUser;
            }
        } else {
            final String credStr;
            if (authHeader.startsWith("user ")) {
                credStr = authHeader.substring(5);
            } else {
                // try basic auth
                if (!authHeader.toUpperCase().startsWith("BASIC ")) {
                    // Not basic
                    return null;
                }

                // remove basic
                final String credEncoded = authHeader.substring(6); //length of 'BASIC '

                final byte[] credRaw = Base64.decodeBase64(credEncoded.getBytes());
                if (credRaw == null) {
                    // invalid decoding
                    return null;
                }

                credStr = new String(credRaw);
            }

            // get username part from username:password
            final String[] x = credStr.split(":");
            if (x.length < 1) {
                // bad split
                return null;
            }

            return x[0];
        }
    }

}
