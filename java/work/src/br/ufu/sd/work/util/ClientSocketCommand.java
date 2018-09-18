package br.ufu.sd.work.util;

import java.net.Socket;

public class ClientSocketCommand {
    private Socket clientSocket;
    private Command command;

    public ClientSocketCommand(Socket clientSocket, Command command) {
        this.clientSocket = clientSocket;
        this.command = command;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public Command getCommand() {
        return command;
    }
}
