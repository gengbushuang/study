import java.io.IOException;

public class ManTest {

    public static void main(String[] args) throws IOException {
        ESClient client = new ESClient();

        client.delete("nvwa-log-20200214","order");

//        client.index();
//        client.ttt();

//        client.b();

//        client.delete();

        client.close();
    }
}
