package work;

import br.ufu.sd.work.Request;
import br.ufu.sd.work.client.request.ExecuteDelete;
import br.ufu.sd.work.client.request.ExecuteInsert;
import br.ufu.sd.work.client.request.ExecuteSelect;
import br.ufu.sd.work.client.request.ExecuteUpdate;
import br.ufu.sd.work.model.Metadata;
import com.google.gson.Gson;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ServerTest {

    private String filePath = "src/test/log.txt";
    private Gson mapper = new Gson();


    @Test
    public void testCrudOKAndLimitMAX() throws IOException, ClassNotFoundException, InterruptedException {
        String IP = "127.0.0.1";
        int PORT = 51666;

        ManagedChannel channel  = ManagedChannelBuilder.forAddress(IP, PORT)
                .usePlaintext().build();
        Request insertRequest = Request.newBuilder().setId(15L)
                .setValue("teste")
                .setClient("1").build();

        ExecuteInsert runnable =  new ExecuteInsert(insertRequest, channel);

        Thread t1 = new Thread(runnable);
        t1.start();
        t1.join();

        Metadata response = mapper.fromJson(runnable.getResponse(), Metadata.class);
        Long id = 15L;
        Assert.assertEquals("teste", response.getMessage());
        Assert.assertEquals(id, response.getId());
        Assert.assertEquals("1", response.getCreatedBy());

        Request selectRequest = Request.newBuilder().setId(15L).build();
        ExecuteSelect runnableSelect = new ExecuteSelect(selectRequest, channel);

        Thread t2= new Thread(runnableSelect);
        t2.start();
        t2.join();

        Metadata responseSelect = mapper.fromJson(runnableSelect.getResponse(), Metadata.class);
        Assert.assertEquals("teste", responseSelect.getMessage());
        Assert.assertEquals(id, responseSelect.getId());
        Assert.assertEquals("1", responseSelect.getCreatedBy());

        Request updateRequest = Request.newBuilder().setId(15L)
                .setClient("1")
                .setValue("teste2")
                .build();
        ExecuteUpdate executeUpdate = new ExecuteUpdate(updateRequest, channel);
        Thread t3 = new Thread(executeUpdate);
        t3.start();
        t3.join();

        Metadata responseUpdate = mapper.fromJson(executeUpdate.getResponse(), Metadata.class);
        Assert.assertEquals("teste2", responseUpdate.getMessage());
        Assert.assertEquals(id, responseUpdate.getId());
        Assert.assertEquals("1", responseUpdate.getCreatedBy());

        Request deleteRequest = Request.newBuilder().setId(15L)
                .setClient("1")
                .build();
        ExecuteDelete executeDelete = new ExecuteDelete(deleteRequest, channel);
        Thread t4 = new Thread(executeDelete);
        t4.start();
        t4.join();

        Assert.assertEquals("objected with Id: 15 deleted", executeDelete.getResponse());

    }

    @Test
    public void testCrudOKAndLimitMIN() throws IOException, ClassNotFoundException, InterruptedException {
        String IP = "127.0.0.1";
        int PORT = 51666;

        ManagedChannel channel  = ManagedChannelBuilder.forAddress(IP, PORT)
                .usePlaintext().build();
        Request insertRequest = Request.newBuilder().setId(0L)
                .setValue("teste")
                .setClient("1").build();
        String stringResponse = "";

        ExecuteInsert runnable =  new ExecuteInsert(insertRequest, channel);

        Thread t1 = new Thread(runnable);
        t1.start();
        t1.join();

        Metadata response = mapper.fromJson(runnable.getResponse(), Metadata.class);
        Long id = 0L;
        Assert.assertEquals("teste", response.getMessage());
        Assert.assertEquals(id, response.getId());
        Assert.assertEquals("1", response.getCreatedBy());

        Request selectRequest = Request.newBuilder().setId(0L).build();
        ExecuteSelect runnableSelect = new ExecuteSelect(selectRequest, channel);

        Thread t2= new Thread(runnableSelect);
        t2.start();
        t2.join();

        Metadata responseSelect = mapper.fromJson(runnableSelect.getResponse(), Metadata.class);
        Assert.assertEquals("teste", responseSelect.getMessage());
        Assert.assertEquals(id, responseSelect.getId());
        Assert.assertEquals("1", responseSelect.getCreatedBy());

        Request updateRequest = Request.newBuilder().setId(0L)
                .setClient("1")
                .setValue("teste2")
                .build();
        ExecuteUpdate executeUpdate = new ExecuteUpdate(updateRequest, channel);
        Thread t3 = new Thread(executeUpdate);
        t3.start();
        t3.join();

        Metadata responseUpdate = mapper.fromJson(executeUpdate.getResponse(), Metadata.class);
        Assert.assertEquals("teste2", responseUpdate.getMessage());
        Assert.assertEquals(id, responseUpdate.getId());
        Assert.assertEquals("1", responseUpdate.getCreatedBy());

        Request deleteRequest = Request.newBuilder().setId(0L)
                .setClient("1")
                .build();
        ExecuteDelete executeDelete = new ExecuteDelete(deleteRequest, channel);
        Thread t4 = new Thread(executeDelete);
        t4.start();
        t4.join();

        Assert.assertEquals("objected with Id: 0 deleted", executeDelete.getResponse());

    }

    @Test
    public void testCrudNOK() throws InterruptedException {

        String IP = "127.0.0.1";
        int PORT = 51666;

        ManagedChannel channel = ManagedChannelBuilder.forAddress(IP, PORT)
                .usePlaintext().build();
        Request insertRequest = Request.newBuilder().setId(13L)
                .setValue("teste")
                .setClient("1").build();
        String stringResponse = "";

        ExecuteInsert runnable = new ExecuteInsert(insertRequest, channel);

        Thread t1 = new Thread(runnable);
        t1.start();
        t1.join();

        Metadata response = mapper.fromJson(runnable.getResponse(), Metadata.class);
        Long id = 13L;
        Assert.assertEquals("teste", response.getMessage());
        Assert.assertEquals(id, response.getId());
        Assert.assertEquals("1", response.getCreatedBy());

        Request insertRequestTwo = Request.newBuilder().setId(13L)
                .setValue("teste")
                .setClient("1").build();

        ExecuteInsert executeInsertTwo = new ExecuteInsert(insertRequestTwo, channel);

        Thread t2 = new Thread(executeInsertTwo);
        t2.start();
        t2.join();

        Assert.assertEquals("Id 13 existing", executeInsertTwo.getResponse());

        Request selectRequest = Request.newBuilder().setId(14L).build();
        ExecuteSelect runnableSelect = new ExecuteSelect(selectRequest, channel);

        Thread t3 = new Thread(runnableSelect);
        t3.start();
        t3.join();

        Assert.assertEquals("not found", runnableSelect.getResponse());

        Request updateRequest = Request.newBuilder().setId(14L)
                .setClient("1")
                .setValue("teste2")
                .build();
        ExecuteUpdate executeUpdate = new ExecuteUpdate(updateRequest, channel);
        Thread t4 = new Thread(executeUpdate);
        t4.start();
        t4.join();

        Assert.assertEquals("Object with id 14 not found", executeUpdate.getResponse());

        Request insertRequestThree = Request.newBuilder().setId(14L)
                .setValue("teste2")
                .setClient("1").build();

        ExecuteInsert executeInsert2 = new ExecuteInsert(insertRequestThree, channel);

        Thread t5 = new Thread(executeInsert2);
        t5.start();
        t5.join();

        Metadata responseTwo = mapper.fromJson(executeInsert2.getResponse(), Metadata.class);
        Long id_2 = 14L;
        Assert.assertEquals("teste2", responseTwo.getMessage());
        Assert.assertEquals(id_2, responseTwo.getId());
        Assert.assertEquals("1", responseTwo.getCreatedBy());

        Thread t6 = new Thread(runnableSelect);
        t6.start();
        t6.join();

        Metadata responseSelect = mapper.fromJson(runnableSelect.getResponse(), Metadata.class);
        Assert.assertEquals("teste2", responseSelect.getMessage());
        Assert.assertEquals(id_2, responseSelect.getId());
        Assert.assertEquals("1", responseSelect.getCreatedBy());

        Request deleteRequest = Request.newBuilder().setId(14L)
                .setClient("1")
                .build();
        ExecuteDelete executeDelete = new ExecuteDelete(deleteRequest, channel);
        Thread t7 = new Thread(executeDelete);
        t7.start();
        t7.join();

        Assert.assertEquals("objected with Id: 14 deleted", executeDelete.getResponse());
    }

    @Test
    public void testCrudWithNegative() throws InterruptedException {

        String IP = "127.0.0.1";
        int PORT = 51666;

        ManagedChannel channel = ManagedChannelBuilder.forAddress(IP, PORT)
                .usePlaintext().build();
        Request insertRequest = Request.newBuilder().setId(-1L)
                .setValue("teste")
                .setClient("1").build();

        ExecuteInsert runnable = new ExecuteInsert(insertRequest, channel);

        Thread t1 = new Thread(runnable);
        t1.start();
        t1.join();

        Assert.assertEquals("Invalid ID, can be below Zero.", runnable.getResponse());

    }

    @Test
    public void testCrudWithKeyBiggerThem() throws InterruptedException {

        String IP = "127.0.0.1";
        int PORT = 51666;

        ManagedChannel channel = ManagedChannelBuilder.forAddress(IP, PORT)
                .usePlaintext().build();
        Request insertRequest = Request.newBuilder().setId(43L)
                .setValue("teste")
                .setClient("1").build();

        ExecuteInsert runnable = new ExecuteInsert(insertRequest, channel);

        Thread t1 = new Thread(runnable);
        t1.start();
        t1.join();

        Assert.assertEquals("Invalid ID, this ID surpass MAX capacity.", runnable.getResponse());

    }

}
