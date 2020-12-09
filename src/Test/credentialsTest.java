package Test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class credentialsTest {
    public static void main(String[] args) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try(FileOutputStream fos=new FileOutputStream(".//credentials.tmp"))
        {
            // перевод строки в байты
            byte[] buffer = "login".getBytes();
            fos.write(buffer, 0, buffer.length);
            buffer = "\n".getBytes();
            fos.write(buffer, 0, buffer.length);
            buffer = "password".getBytes();
            fos.write(buffer, 0, buffer.length);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
