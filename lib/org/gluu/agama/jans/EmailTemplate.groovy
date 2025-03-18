package org.gluu.agama.jans;

import java.time.*;
import java.time.format.DateTimeFormatter;
import org.gluu.agama.jans.model.ContextData;

class EmailTemplate {
    
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, YYYY, HH:mma (O)");

    static String get(String magicLink) {

        """
<div style="width: 100%; font-size: 14px; font-family: 'Roboto', sans-serif; font-weight: 300">
    <div style="background-color: #b6f6da; border-bottom: 1px solid #0ca65d">
        <img src="https://gluu.org/wp-content/uploads/elementor/thumbs/Logo-qbe8p4qgmufqni0becxda6fnfib6krzb65uihag270.png" alt="Gluu Inc." />
    </div>
    <div style="padding: 10px; font-size: 12px; border-bottom: 1px solid #ccc;">
        <p>
        <b>Hi,</b>
        <br><br>
        Click on the link to verfy and access to the IDP. ${magicLink}
        </p>
        <p style="font-size: 12px">
        If you did not make this request, you can safely ignore this email.
        </p>
        <p>
        <br>
        Thanks for helping us keep your account secure.<br>
        The Gluu Team
        <br><br>
        </p>
    </div>
</div>
        """
    }

    private static String computeDateTime(String zone) {

        Instant now = Instant.now();
        try {
            return now.atZone(ZoneId.of(zone)).format(formatter);
        } catch (Exception e) {
            return now.atOffset(ZoneOffset.UTC).format(formatter);
        }
        
    }
    
}
