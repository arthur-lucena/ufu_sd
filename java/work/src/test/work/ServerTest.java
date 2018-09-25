package work;

import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.util.MessageCommand;
import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import br.ufu.sd.work.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ServerTest {

	@Test
	public void testStart() throws IOException, ClassNotFoundException {
        String IP = "127.0.0.1";
        int PORT = 61666;


		Socket clientSocket = new Socket(IP, PORT);
		ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
		MessageCommand messageCommandInsert = new MessageCommand();
		messageCommandInsert.setTypeCommand(ETypeCommand.INSERT);
		messageCommandInsert.setArgs(new String[] {"mama"});
		outToServer.writeObject(messageCommandInsert);

		Object objectReceiver = (MessageCommand) inFromServer.readObject();
		Object objectReceiver1 = (MessageCommand) inFromServer.readObject();
		Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.INSERT);
		Assert.assertEquals("Insert realizado!", ((MessageCommand) objectReceiver1).getResponse());

		MessageCommand messageCommandSelect = new MessageCommand();
		messageCommandSelect.setTypeCommand(ETypeCommand.SELECT);
		messageCommandSelect.setArgs(new String[] {"mama"});
		outToServer.writeObject(messageCommandSelect);

		objectReceiver1 = (MessageCommand) inFromServer.readObject();
		Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.SELECT);
		Assert.assertEquals("KEY: 1 VALUE: mama", ((MessageCommand) objectReceiver1).getResponse());

		MessageCommand messageCommandUpdate = new MessageCommand();
		messageCommandUpdate.setTypeCommand(ETypeCommand.UPDATE);
		messageCommandUpdate.setObjectId((long) 1);
		messageCommandUpdate.setArgs(new String[] {"meme"});
		outToServer.writeObject(messageCommandUpdate);

		objectReceiver1 = (MessageCommand) inFromServer.readObject();
		Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.UPDATE);
		Assert.assertEquals("object with Id: 1 updated", ((MessageCommand) objectReceiver1).getResponse());
	}
}
