package org.gluu.agama.jans;

import java.util.Map;

import org.gluu.agama.jans.service.Service;

public abstract class MagicLinkService {
    public abstract Map<String, String> getUserEntity(String email);

    public abstract String sendMail(String to);

    public abstract boolean verifyMagicLink(String token);

    public static MagicLinkService getInstance(String hostName, String secretKey, Integer tokenEpiration){
        return Service.getInstance(hostName, secretKey, tokenEpiration);
    }
}
