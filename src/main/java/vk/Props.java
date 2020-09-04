package vk;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class Props extends Properties{
    private static Props instance;
    public static Props getInstance() throws IOException, URISyntaxException { // #3
        if(instance == null){		//если объект еще не создан
            instance = Props.build();	//создать новый объект
        }
        return instance;		// вернуть ранее созданный объект
    }
    private Props() { super(); }

    private static String filename = "params.conf";

    private static Props build() throws URISyntaxException, IOException {
        Props p = new Props();
        URL path = Props.class.getResource(filename);
        if(path==null) {
            throw new FileNotFoundException("File not found");
        }
        File f = new File(path.toURI());
        FileInputStream fis = new FileInputStream(f);
        p.load(fis);
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
