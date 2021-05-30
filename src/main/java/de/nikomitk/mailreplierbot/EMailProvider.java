package de.nikomitk.mailreplierbot;

import lombok.Getter;
import lombok.Setter;
import yapion.annotations.object.YAPIONData;


@YAPIONData
public class EMailProvider {
    @Getter
    @Setter
    private String name, imapserver, imapport, smtpserver, smtpport;

    public EMailProvider(String name, String imapserver, String imapport, String smtpserver, String smtpport) {
        this.name = name;
        this.imapserver = imapserver;
        this.imapport = imapport;
        this.smtpserver = smtpserver;
        this.smtpport = smtpport;
    }
}