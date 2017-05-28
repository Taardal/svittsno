package no.svitts.core.service;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import no.svitts.core.movie.Movie;
import no.svitts.core.serverinfo.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RemoteMovieDiscoveryService implements RemoteDiscoveryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteMovieDiscoveryService.class);
    private static final String SMB_PREFIX = "smb://";

    @Override
    public List<Movie> discover(String path, ServerInfo serverInfo) {
        String url = getUrl(path, serverInfo);
        List<SmbFile> smbFiles = Arrays.asList(getSmbFiles(url, serverInfo));
        LOGGER.debug("Found [{}] SMB files: {}", smbFiles.size(), smbFiles);

        List<String> asd = smbFiles.stream().filter(smbFile -> isReadable(smbFile) && !smbFile.getPath().contains("$")).map(SmbFile::getPath).collect(Collectors.toList());

        smbFiles.forEach(smbFile -> {
            String s;
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(smbFile.getInputStream()))) {
                s = buffer.lines().collect(Collectors.joining("\n"));
                LOGGER.debug(s);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        return null;
    }

    private String getUrl(String path, ServerInfo serverInfo) {
        String url = SMB_PREFIX + serverInfo.getHost();
        if (path != null && !path.equals("")) {
            path = getCleanedPath(path, serverInfo.getHost());
            if (!url.endsWith("/") && !path.startsWith("/")) {
                url += "/";
            }
            url += path;
        }
        LOGGER.info("Remote file url [{}]", url);
        return url;
    }

    private String getCleanedPath(String path, String host) {
        path = path.toLowerCase().replace("\\", "/");
        host = host.toLowerCase();
        if (path.startsWith("//")) {
            path = path.replace("//", "");
        }
        if (path.startsWith(host)) {
            path = path.replace(host, "");
        }
        return path;
    }

    private SmbFile[] getSmbFiles(String url, ServerInfo serverInfo) {
        SmbFile smbFile = getSmbFile(url, serverInfo);
        if (isDirectory(smbFile)) {
            if (!smbFile.getPath().endsWith("/")) {
                smbFile = getSmbFile(url + "/", serverInfo);
            }
            return listFiles(smbFile);
        } else {
            return new SmbFile[]{smbFile};
        }
    }

    private SmbFile getSmbFile(String url, ServerInfo serverInfo) {
        if (serverInfo.getUsername() != null && serverInfo.getPassword() != null) {
            return getSmbFile(url, getNtlmPasswordAuthentication(serverInfo));
        } else {
            return getSmbFile(url);
        }
    }

    private SmbFile getSmbFile(String url, NtlmPasswordAuthentication ntlmPasswordAuthentication) {
        try {
            return new SmbFile(url, ntlmPasswordAuthentication);
        } catch (MalformedURLException e) {
            LOGGER.error("Could not get SMB-file on URL [{}] with authentication [{}]", url, ntlmPasswordAuthentication, e);
            throw new RuntimeException(e);
        }
    }

    private NtlmPasswordAuthentication getNtlmPasswordAuthentication(ServerInfo serverInfo) {
        return new NtlmPasswordAuthentication(serverInfo.getHost(), serverInfo.getUsername(), serverInfo.getPassword());
    }

    private SmbFile getSmbFile(String url) {
        try {
            return new SmbFile(url);
        } catch (MalformedURLException e) {
            LOGGER.error("Could not get SMB-file on URL [{}]", url, e);
            throw new RuntimeException(e);
        }
    }

    private boolean isDirectory(SmbFile smbFile) {
        try {
            return smbFile.isDirectory();
        } catch (SmbException e) {
            LOGGER.error("Could not determine if SMB-file [{}] is a directory.", smbFile.getPath(), e);
            throw new RuntimeException(e);
        }
    }

    private boolean isFile(SmbFile smbFile) {
        try {
            return smbFile.isFile();
        } catch (SmbException e) {
            LOGGER.error("Could not determine if SMB-file [{}] is a file.", smbFile.getPath(), e);
            throw new RuntimeException(e);
        }
    }

    private SmbFile[] listFiles(SmbFile smbFile) {
        try {
            return smbFile.listFiles();
        } catch (SmbException e) {
            LOGGER.error("Could not list files in SMB-file [{}]", smbFile.getPath(), e);
            throw new RuntimeException(e);
        }
    }

    private boolean isReadable(SmbFile smbFile) {
        try {
            return smbFile.canRead();
        } catch (SmbException e) {
            LOGGER.error("Could not determine if SMB-file [{}] is readable.", smbFile.getPath(), e);
            throw new RuntimeException(e);
        }
    }
}
