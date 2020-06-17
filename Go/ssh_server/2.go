package main

import (
	"fmt"
	//ssh "github.com/gliderlabs/ssh"
	//ssh1 "golang.org/x/crypto/ssh"
	"golang.org/x/crypto/ssh/terminal"
	"log"
)

func (s ssh.Server) handler(sess ssh1.Session) {
	term := terminal.NewTerminal(sess, "> ")
	for {
		line, err := term.ReadLine()
		if err != nil {
			break
		}

		response := router(line)
		log.Println(line)
		if response != "" {
			term.Write(append([]byte(response), '\n'))
		}
	}
	log.Println("terminal closed")
}

func main(){

}