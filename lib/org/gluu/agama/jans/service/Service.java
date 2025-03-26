package org.gluu.agama.jans.service;

import io.jans.as.common.model.common.User;
import io.jans.as.common.service.common.EncryptionService;
import io.jans.as.common.service.common.UserService;
import io.jans.model.SmtpConfiguration;
import io.jans.orm.exception.operation.EntryNotFoundException;
import io.jans.service.MailService;
import io.jans.service.cdi.util.CdiUtil;
import io.jans.util.StringHelper;
import io.jans.agama.engine.script.LogUtils;
import java.io.IOException;
import io.jans.as.common.service.common.ConfigurationService;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;

import org.gluu.agama.jans.EmailTemplate;
import org.gluu.agama.jans.MagicLinkService;
import org.gluu.agama.jans.model.ContextData;

public class Service extends MagicLinkService{
    private String HOST;
    private String SECRET_KEY;
    private Integer TOKEN_EXPIRATION;
    private static final String PREFIX = "GJxc7c"; //Add extra string with token for security concern.
    private static final String MAIL = "mail";
    private static final String UID = "uid";
    private static final String DISPLAY_NAME = "displayName";
    private static final String GIVEN_NAME = "givenName";
    private static final String INUM_ATTR = "inum";
    private static final SecureRandom RAND = new SecureRandom();
    private static final String SUBJECT_TEMPLATE = "MagicLink for authentication";
    private static final String MSG_TEMPLATE_TEXT = "%s is the magiclink to complete your verification";

    private static Service INSTANCE = null;
    private Service(){}

    public static synchronized Service getInstance(String hostName, String secretKey, Integer tokenExpiration) {
        if (INSTANCE == null) {
            INSTANCE = new Service();
            INSTANCE.HOST = hostName;
            INSTANCE.SECRET_KEY = secretKey;
            INSTANCE.TOKEN_EXPIRATION = tokenExpiration;
        }
        return INSTANCE;
    }

    public String generateMagicLink(String token) throws Exception {

        return "https://"+ HOST + "/jans-auth/fl/callback?ut=" +PREFIX+token+PREFIX;
    }

    public boolean verifyMagicLink(String token) {
        try {
            token = token.replace(PREFIX, "");
            LogUtils.log("Token after rmoving extra perfex %", token);
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

            if (signedJWT.verify(verifier)) {
                Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
                return expirationTime != null && expirationTime.after(new Date());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }    

    public String generateToken(String email){
        long expirationTime = System.currentTimeMillis() + (this.TOKEN_EXPIRATION * 60 * 1000);

        JWSSigner signer = new MACSigner(SECRET_KEY.getBytes());
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                new JWTClaimsSet.Builder()
                        .subject(email)
                        .expirationTime(new Date(expirationTime))
                        .issueTime(new Date())
                        .build()
        );
        signedJWT.sign(signer);
        String token = signedJWT.serialize();

        return token;
    }

    public Map<String, String> getUserEntity(String email) {
        User user = getUser(MAIL, email);
        boolean local = user != null;
        LogUtils.log("There is % local account for %", local ? "a" : "no", email);
    
        if (local) {

            String uid = getSingleValuedAttr(user, UID);
            String inum = getSingleValuedAttr(user, INUM_ATTR);
            String name = getSingleValuedAttr(user, GIVEN_NAME); 

            if (name == null) {
                name = getSingleValuedAttr(user, DISPLAY_NAME);
                if (name == null && email != null && email.contains("@")) {
                    name = email.substring(0, email.indexOf("@"));
                }
            }
    
            // Creating a truly modifiable map
            Map<String, String> userMap = new HashMap<>();
            userMap.put(UID, uid);
            userMap.put(INUM_ATTR, inum);
            userMap.put("name", name);
            userMap.put("email", email);
    
            return userMap;
        }
    
        return new HashMap<>();     
    }

    public String sendMail(String to) throws Exception {
        SmtpConfiguration smtpConfiguration = getSmtpConfiguration();

        boolean smtp = smtpConfiguration!=null;

        if(smtp){
            String token = generateToken(to);
            String magicLink = generateMagicLink(token);
    
            String from = smtpConfiguration.getFromEmailAddress();
            String subject = String.format(SUBJECT_TEMPLATE);
            String textBody = String.format(MSG_TEMPLATE_TEXT, magicLink);
            String htmlBody = EmailTemplate.get(magicLink);
    
            MailService mailService = CdiUtil.bean(MailService.class);
    
            if (mailService.sendMailSigned(from, from, to, null, subject, textBody, htmlBody)) {
                LogUtils.log("E-mail has been delivered to % with code %", to, token);
                return token;
            }else{
                throw new EntryNotFoundException("Email sending failed. Please re-try");
            }
            LogUtils.log("E-mail delivery failed, check jans-auth logs");
            return null; 
        }

        LogUtils.log("Configuration missing");

     
    }

    private SmtpConfiguration getSmtpConfiguration() {
        ConfigurationService configurationService = CdiUtil.bean(ConfigurationService.class);
        SmtpConfiguration smtpConfiguration = configurationService.getConfiguration().getSmtpConfiguration();
        return smtpConfiguration;

    }    

    private String getSingleValuedAttr(User user, String attribute) {

        Object value = null;
        if (attribute.equals(this.UID)) {
            //user.getAttribute("uid", true, false) always returns null :(
            value = user.getUserId();
        } else {
            value = user.getAttribute(attribute, true, false);
        }
        return value == null ? null : value.toString();

    }    

    private static User getUser(String attributeName, String value) {
        UserService userService = CdiUtil.bean(UserService.class);
        return userService.getUserByAttribute(attributeName, value, true);
    }    

}
