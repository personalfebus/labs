package main

import (
	"fmt"
	"github.com/gliderlabs/ssh"
	"golang.org/x/crypto/ssh/terminal"
	"io"
	"log"
)

func main() {


	ssh.Handle(func(s ssh.Session) {
		log.Println(s.Environ())
		log.Println(s.PublicKey())
		io.WriteString(s, fmt.Sprintf("Hello %s\n", s.User()))
		term := terminal.NewTerminal(s, "> ")
		for {
			line, err := term.ReadLine()
			if err != nil {
				break
			}
			response := line
			log.Println(line)
			if response != "" {
				//term.Write(append([]byte(response), '\n'))
			}
		}
	})

	log.Println("starting ssh server on port 2210...")
	log.Fatal(ssh.ListenAndServe("localhost:2210", nil))

}
