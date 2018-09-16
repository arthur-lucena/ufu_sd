package main

import (
	"fmt"
	"bufio"
	"net"
	"os"
	"log"
	"shared/commands"
)

func main() {
	params := []string{"-k", "-88"}
	commands.Execute("rsrs", params)

	// connect to this socket
	conn, err := net.Dial("tcp", "127.0.0.1:10666")

	if err != nil {
		log.Println(err)
		return
	}

	for {
		// read in input from stdin
		reader := bufio.NewReader(os.Stdin)

		fmt.Print("Text to send: ")
		text, err := reader.ReadString('\n')

		if err != nil {
			log.Println(err)
			return
		}

		// send to socket
		fmt.Fprintf(conn, text+"\n")
		// listen for reply
		message, err := bufio.NewReader(conn).ReadString('\n')

		if err != nil {
			log.Println(err)
			return
		}

		fmt.Print("Message from server: " + message)
	}
}
