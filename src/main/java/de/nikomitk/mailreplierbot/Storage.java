package de.nikomitk.mailreplierbot;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import yapion.annotations.object.YAPIONData;
import yapion.hierarchy.output.FileOutput;
import yapion.serializing.YAPIONSerializer;

import java.io.File;
import java.io.IOException;

@YAPIONData
@Getter
@Setter
@ToString
public class Storage {

    // Main stuff
    private boolean credsset = false;
    private String lastsubject = null;

    // Settingsgui stuff
    private String yourmail = "Your Mail address";
    private String password = "Your Password";
    private String sendermail = "Who you want to reply to";
    private String yourreply = "Your reply";
    private String trigger = "Text to reply to";
    private boolean minimizetotray = true;

    // MailServerSettings stuff
    private EMailProvider mailProvider = new EMailProvider("OTHER", "Imap server", "Imap port", "Smtp server", "Smtp port");
    private int preselect = 0;

    // ApplicationSettings stuff
    private boolean darktheme = true;
    private long searchdelay = 10;

    public void save() {
        try {
            YAPIONSerializer.serialize(this).toYAPION(new FileOutput(new File("storage.yapion")));
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }

}
