package main

import (
	"fmt"
	"bufio"
	"strings"
	"net"
	"log"
)
// only needed below for sample processing

func main() {

  fmt.Println("Launching server...")

  ln, err := net.Listen("tcp", ":10666")

  if err != nil {
	log.Println(err)
	return
  }

  for {
	  fmt.Println("Waiting connection...")
	  // listen on all interfaces

	  conn, err := ln.Accept()

	  if err != nil {
		  log.Println(err)
		  return
	  }

	  fmt.Println("New connection... ", conn.RemoteAddr().String())
	  go connectionHandler(conn)
  }
}

func connectionHandler (conn net.Conn) {
	for {
		// will listen for message to process ending in newline (\n)
		message, err := bufio.NewReader(conn).ReadString('\n')

		if err != nil {
			log.Println(err)
			return
		}

		fmt.Println("Execution from... ", conn.RemoteAddr().String())
		// output message received
		fmt.Print("Message Received: ", string(message))
		// sample process for string received
		newMessage := strings.ToUpper(message)
		// send new string back to client
		_, err = conn.Write([]byte(newMessage + "\n"))

		if err != nil {
			log.Println(err)
			return
		}
	}
}