package work;

import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.util.MessageCommand;
import org.junit.Assert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConcurrentTest implements Runnable {

	long offset;

	public ConcurrentTest(long offset) {
		this.offset = offset;
	}

	public void run() {
		try {
			String IP = "127.0.0.1";
			int PORT = 61666;

			Socket clientSocket = new Socket(IP, PORT);
			ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
			MessageCommand messageCommandInsert = new MessageCommand();
			messageCommandInsert.setTypeCommand(ETypeCommand.INSERT);
			messageCommandInsert.setObjectId(offset + 1);
			messageCommandInsert.setArgs(new String[]{"mama"});
			outToServer.writeObject(messageCommandInsert);

			Object objectReceiver = inFromServer.readObject();
			Object objectReceiver1 = inFromServer.readObject();
			Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.INSERT);
			Assert.assertEquals("inserção realizada: id: " + (offset + 1) + ", message: mama, createdBy: 0, createdAt: null, updatedBy: 0, updatedAt: null", ((MessageCommand) objectReceiver1).getResponse());

			MessageCommand messageCommandSelect = new MessageCommand();
			messageCommandSelect.setTypeCommand(ETypeCommand.SELECT);
			messageCommandSelect.setObjectId(offset + 1);

			outToServer.writeObject(messageCommandSelect);

			objectReceiver1 = inFromServer.readObject();
			Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.SELECT);
			Assert.assertEquals("object with Id: " + (offset + 1) + " found: id: " + (offset + 1) + ", message: mama, createdBy: 0, createdAt: null, updatedBy: 0, updatedAt: null", ((MessageCommand) objectReceiver1).getResponse());

			MessageCommand messageCommandUpdate = new MessageCommand();
			messageCommandUpdate.setTypeCommand(ETypeCommand.UPDATE);
			messageCommandUpdate.setObjectId(offset + 1);
			messageCommandUpdate.setArgs(new String[]{"meme"});
			outToServer.writeObject(messageCommandUpdate);

			objectReceiver1 = inFromServer.readObject();
			Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.UPDATE);
			Assert.assertEquals("object with Id: " + (offset + 1) + " updated with information: id: " + (offset + 1) + ", message: meme, createdBy: null, createdAt: null, updatedBy: 0, updatedAt: null", ((MessageCommand) objectReceiver1).getResponse());

			MessageCommand messageCommandSelectTwo = new MessageCommand();
			messageCommandSelectTwo.setTypeCommand(ETypeCommand.SELECT);
			messageCommandSelectTwo.setObjectId(offset + 1);
			outToServer.writeObject(messageCommandSelectTwo);

			objectReceiver1 = inFromServer.readObject();
			Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.SELECT);
			Assert.assertEquals("object with Id: " + (offset + 1) + " found: id: " + (offset + 1) + ", message: meme, createdBy: null, createdAt: null, updatedBy: 0, updatedAt: null", ((MessageCommand) objectReceiver1).getResponse());

			MessageCommand messageCommandDelete = new MessageCommand();
			messageCommandDelete.setTypeCommand(ETypeCommand.DELETE);
			messageCommandDelete.setObjectId(offset + 1);
			messageCommandDelete.setArgs(new String[]{String.valueOf(offset + 1)});
			outToServer.writeObject(messageCommandDelete);

			objectReceiver1 = inFromServer.readObject();
			Assert.assertEquals(((MessageCommand) objectReceiver1).getTypeCommand(), ETypeCommand.DELETE);
			Assert.assertEquals("objected with Id: " + (offset + 1) + " deleted", ((MessageCommand) objectReceiver1).getResponse());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
