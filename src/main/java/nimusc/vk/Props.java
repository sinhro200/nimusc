package nimusc.vk;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class Props extends Properties{
    private static Props instance;
    public static Props getInstance() throws IOException{ // #3
        if(instance == null){
            instance = Props.build();
        }
        return instance;
    }
    private Props() { super(); }

    private static String filename = "params.conf";

    private static Props build() throws IOException {
        Props p = new Props();
        InputStream path = p.getClass().getResourceAsStream(filename);
        if(path==null) {
            throw new FileNotFoundException("File not found");
        }
        p.load(path);
        return p;
    }

    public String getCurToken(){
        return this.getProperty("access_token");
    }

    public void setCurToken(String token) throws IOException, URISyntaxException {
        this.setProperty("access_token",token);
        URL path = Props.class.getResource(filename);
        if(path==null) {
            throw new FileNotFoundException("File not found");
        }
        File f = new File(path.toURI());
        FileOutputStream fos = new FileOutputStream(f);

        this.store(fos,null);
    }

}
