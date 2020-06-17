package main

import (
	"fmt"
	"github.com/gliderlabs/ssh"
	"golang.org/x/crypto/ssh/terminal"
	"io"
	"io/ioutil"
	"log"
	"os"
	"strings"
)

func sessionHandler (s ssh.Session) {
	io.WriteString(s, fmt.Sprintf("Hello %s\n", s.User()))
	term := terminal.NewTerminal(s, "> ")
	aaa:for {
		line, err := term.ReadLine()
		if err != nil {
			break
		}
		ss := strings.Split(line, " ")
		switch ss[0] {
		case "ls":{
			if len(ss) < 2 {
				fmt.Println("Path not entered")
				term.Write(append([]byte("Path not entered"), '\n'))
				continue
			}
			files, err := ioutil.ReadDir(ss[1])
			if err != nil{
				log.Println("error -> ", err)
				term.Write(append([]byte("error -> " + err.Error()), '\n'))
				return
			}
			for i := range files {
				term.Write(append([]byte(files[i].Name()), '\n'))
			}
		}
		case "mkdir":{
			if len(ss) < 2 {
				fmt.Println("Path not entered")
				term.Write(append([]byte("Path not entered"), '\n'))
				continue
			}
			err := os.Mkdir(ss[1], 0777)
			if err != nil{
				log.Println("error -> ", err)
				term.Write(append([]byte("error -> " + err.Error()), '\n'))
				return
			}
		}
		case "rmdir":{
			if len(ss) < 2 {
				fmt.Println("Path not entered")
				term.Write(append([]byte("Path not entered"), '\n'))
				continue
			}
			err := os.Remove(ss[1])
			if err != nil{
				log.Println("error -> ", err)
				term.Write(append([]byte("error -> " + err.Error()), '\n'))
				return
			}
		}
		case "close":{
			term.Write(append([]byte("Closing connection"), '\n'))
			break aaa
		}
		default:{
			term.Write(append([]byte("Not a valid command"), '\n'))
		}
		}
		//response := line
		//log.Println(line)
		//if response != "" {
		//	//term.Write(append([]byte(response), '\n'))
		//}
	}
}

func authHandler (ctx ssh.Context, password string) bool{
	if (ctx.User() == "abc123") && (password == "1001") {
		return true
	}
	return false
}

func main() {
	s := &ssh.Server{
		Addr:             "localhost:2210",
		Handler:          sessionHandler,
		PasswordHandler: authHandler,
	}
	//s.AddHostKey(hostKeySigner)

	//ssh.Handle()

	log.Println("starting ssh server on port 2210...")
	log.Fatal(s.ListenAndServe())

}