package work;

import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.util.MessageCommand;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ServerTest {

	private String filePath = "src/test/log.txt";

	@Before
	public void setUp() {
		deleteFile();
	}

		@Test
		public void testCrudOK() throws IOException, ClassNotFoundException {
			String IP = "127.0.0.1";
			int PORT = 61666;


			Socket clientSocket = new Socket(IP, PORT);
			ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
			MessageCommand messageCommandInsert = new MessageCommand();
			messageCommandInsert.setTypeCommand(ETypeCommand.INSERT);
			messageCommandInsert.setArgs(new String[] {"mama"});
			outToServer.writeObject(messageCommandInsert);

			Object objectReceiver = inFromServer.readObject();
			Object objectReceiver1 = inFromServer.readObject();
			Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.INSERT);
			Assert.assertEquals("inserção realizada: id: 1, message: mama, createdBy: 0, createdAt: null, updatedBy: 0, updatedAt: null", ((MessageCommand) objectReceiver1).getResponse());

			MessageCommand messageCommandSelect = new MessageCommand();
			messageCommandSelect.setTypeCommand(ETypeCommand.SELECT);
			messageCommandSelect.setObjectId((long) 1);
			messageCommandSelect.setArgs(new String[] {"1"});

			outToServer.writeObject(messageCommandSelect);

			objectReceiver1 = inFromServer.readObject();
			Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.SELECT);
			Assert.assertEquals("object with Id: 1 found: id: 1, message: mama, createdBy: 0, createdAt: null, updatedBy: 0, updatedAt: null",  ((MessageCommand) objectReceiver1).getResponse());

			MessageCommand messageCommandUpdate = new MessageCommand();
			messageCommandUpdate.setTypeCommand(ETypeCommand.UPDATE);
			messageCommandUpdate.setObjectId((long) 1);
			messageCommandUpdate.setArgs(new String[] {"1","meme"});
			outToServer.writeObject(messageCommandUpdate);

			objectReceiver1 = inFromServer.readObject();
			Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.UPDATE);
			Assert.assertEquals("object with Id: 1 updated with information: id: 1, message: meme, createdBy: null, createdAt: null, updatedBy: 0, updatedAt: null", ((MessageCommand) objectReceiver1).getResponse());

			MessageCommand messageCommandSelectTwo = new MessageCommand();
			messageCommandSelectTwo.setTypeCommand(ETypeCommand.SELECT);
			messageCommandSelectTwo.setObjectId((long) 1);
			messageCommandSelectTwo.setArgs(new String[] {"1"});
			outToServer.writeObject(messageCommandSelectTwo);

			objectReceiver1 = inFromServer.readObject();
			Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.SELECT);
			Assert.assertEquals("object with Id: 1 found: id: 1, message: meme, createdBy: null, createdAt: null, updatedBy: 0, updatedAt: null",  ((MessageCommand) objectReceiver1).getResponse());

			MessageCommand messageCommandDelete = new MessageCommand();
			messageCommandDelete.setTypeCommand(ETypeCommand.DELETE);
			messageCommandDelete.setObjectId((long) 1);
			messageCommandDelete.setArgs(new String[] {"1"});
			outToServer.writeObject(messageCommandDelete);

			objectReceiver1 = inFromServer.readObject();
			Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.DELETE);
			Assert.assertEquals("objected with Id: 1 deleted",  ((MessageCommand) objectReceiver1).getResponse());


		}

	@Test
	public void testCrudNOK() throws IOException, ClassNotFoundException {
		String IP = "127.0.0.1";
		int PORT = 61666;


		Socket clientSocket = new Socket(IP, PORT);
		ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
		MessageCommand messageCommandInsert = new MessageCommand();
		messageCommandInsert.setTypeCommand(ETypeCommand.INSERT);
		messageCommandInsert.setArgs(new String[] {"mama"});
		outToServer.writeObject(messageCommandInsert);

		Object objectReceiver = inFromServer.readObject();
		Object objectReceiver1 = inFromServer.readObject();
		Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.INSERT);
		Assert.assertEquals("inserção realizada: id: 1, message: mama, createdBy: 0, createdAt: null, updatedBy: 0, updatedAt: null", ((MessageCommand) objectReceiver1).getResponse());

		MessageCommand messageCommandSelect = new MessageCommand();
		messageCommandSelect.setTypeCommand(ETypeCommand.SELECT);
		messageCommandSelect.setObjectId((long) 2);
		messageCommandSelect.setArgs(new String[] {"2"});
		outToServer.writeObject(messageCommandSelect);

		objectReceiver1 = inFromServer.readObject();
		Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.SELECT);
		Assert.assertEquals("not found",  ((MessageCommand) objectReceiver1).getResponse());

		MessageCommand messageCommandUpdate = new MessageCommand();
		messageCommandUpdate.setTypeCommand(ETypeCommand.UPDATE);
		messageCommandUpdate.setObjectId((long) 2);
		messageCommandUpdate.setArgs(new String[] {"2","meme"});
		outToServer.writeObject(messageCommandUpdate);

		objectReceiver1 = inFromServer.readObject();
		Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.UPDATE);
		Assert.assertEquals("not found", ((MessageCommand) objectReceiver1).getResponse());

		MessageCommand messageCommandInsertTwo = new MessageCommand();
		messageCommandInsertTwo.setTypeCommand(ETypeCommand.INSERT);
		messageCommandInsertTwo.setArgs(new String[] {"meme"});
		outToServer.writeObject(messageCommandInsertTwo);

		objectReceiver1 = inFromServer.readObject();
		Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.INSERT);
		Assert.assertEquals("inserção realizada: id: 2, message: meme, createdBy: 0, createdAt: null, updatedBy: 0, updatedAt: null", ((MessageCommand) objectReceiver1).getResponse());

		MessageCommand messageCommandSelectTwo = new MessageCommand();
		messageCommandSelectTwo.setTypeCommand(ETypeCommand.SELECT);
		messageCommandSelectTwo.setObjectId((long) 2);
		messageCommandSelectTwo.setArgs(new String[] {"2"});
		outToServer.writeObject(messageCommandSelectTwo);

		objectReceiver1 = inFromServer.readObject();
		Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.SELECT);
		Assert.assertEquals("object with Id: 2 found: id: 2, message: meme, createdBy: 0, createdAt: null, updatedBy: 0, updatedAt: null",  ((MessageCommand) objectReceiver1).getResponse());


		MessageCommand messageCommandDelete = new MessageCommand();
		messageCommandDelete.setTypeCommand(ETypeCommand.DELETE);
		messageCommandDelete.setObjectId((long) 2);
		messageCommandDelete.setArgs(new String[] {"2"});
		outToServer.writeObject(messageCommandDelete);

		objectReceiver1 = inFromServer.readObject();
		Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.DELETE);
		Assert.assertEquals("objected with Id: 2 deleted",  ((MessageCommand) objectReceiver1).getResponse());
	}


	private void deleteFile() {
		try {
			if(Files.exists(Paths.get(filePath))) {
				Files.delete(Paths.get(filePath));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	}
