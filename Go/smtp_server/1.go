package main

import (
	"fmt"
	"io/ioutil"

	//"io/ioutil"
	"log"
	"net"
	"net/mail"
	"net/smtp"
	"crypto/tls"
	//"os"
)

// SSL/TLS Email Example
func main() {
	from := mail.Address{"", "zme2001@mail.ru"}
	to   := mail.Address{"", "gleb89206535210@gmail.com"}
	subj := "ИУ9 32Б Лаба 3.1"
	body := "Выполнил Завьялов Глеб (iu9_32_10), теперь с public и private ключами, 123"
	// Setup headers
	headers := make(map[string]string)
	headers["From"] = from.String()
	headers["To"] = to.String()
	headers["Subject"] = subj
	// Setup message
	message := ""
	for k,v := range headers {
		message += fmt.Sprintf("%s: %s\r\n", k, v)
	}
	message += "\r\n" + body

	// Connect to the SMTP Server
	servername := "smtp.mail.ru:465"
	host, _, _ := net.SplitHostPort(servername)
	file, err := ioutil.ReadFile("pass.txt")
	if err != nil {
		log.Println(err)
		return
	}
	auth := smtp.PlainAuth("","zme2001@mail.ru", string(file), host)

	// TLS config
	cer, err := tls.LoadX509KeyPair("server.crt", "server.key")
	if err != nil {
		log.Println(err)
		return
	}
	tlsconfig := &tls.Config {
		Certificates: []tls.Certificate{cer},
		ServerName: host,
	}
	// Here is the key, you need to call tls.Dial instead of smtp.Dial
	// for smtp servers running on 465 that require an ssl connection
	// from the very beginning (no starttls)

	conn, err := tls.Dial("tcp", servername, tlsconfig)
	if err != nil {
		log.Panic(err)
	}
	c, err := smtp.NewClient(conn, host)
	if err != nil {
		log.Panic(err)
	}
	// Auth
	if err = c.Auth(auth); err != nil {
		log.Panic(err)
	}
	// To && From
	if err = c.Mail(from.Address); err != nil {
		log.Panic(err)
	}
	if err = c.Rcpt(to.Address); err != nil {
		log.Panic(err)
	}
	// Data
	w, err := c.Data()
	if err != nil {
		log.Panic(err)
	}
	_, err = w.Write([]byte(message))
	if err != nil {
		log.Panic(err)
	}
	err = w.Close()
	if err != nil {
		log.Panic(err)
	}
	c.Quit()
}