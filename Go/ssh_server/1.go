package main

import (
	"flag"
	"fmt"
	//"io/ioutil"
	//"os"
	//"path"

	//"os"
	//"path"
	"golang.org/x/crypto/ssh"
)

var (
	user = flag.String("u", "", "iu9_32_10")
	pk   = flag.String("pk", defaultKeyPath(), "Private key file")
	//k = flag.String("k", "0654", "Key")
	host = flag.String("h", "185.20.227.83", "Host")
	port = flag.Int("p", 22, "Port")
)

func defaultKeyPath() string {
	//return "0654"
	return "./keyz.txt"
	//home := os.Getenv("HOME")
	//if len(home) > 0 {
	//	//return path.Join(home, ".ssh/id_rsa")
	//}
	//return ""
}

func main() {
	flag.Parse()
	//key, err := ioutil.ReadFile(*pk)
	//if err != nil {
	//	panic(err)
	//}
	//signer, err := ssh.ParsePrivateKey(key)
	//signer, err := ssh.ParsePrivateKey(k)
	//if err != nil {
	//	panic(err)
	//}

	config := &ssh.ClientConfig{
		User: *user,
		Auth: []ssh.AuthMethod{
			ssh.Password("0654"),
			//ssh.PublicKeys(signer),
		},
		HostKeyCallback: ssh.HostKeyCallback(ssh.InsecureIgnoreHostKey()),
	}
	addr := fmt.Sprintf("%s:%d", *host, *port)
	client, err := ssh.Dial("tcp", addr, config)
	if err != nil {
		panic(err)
	}

	session, err := client.NewSession()
	if err != nil {
		panic(err)
	}

	defer session.Close()

	modes := ssh.TerminalModes{
		ssh.ECHO:          0,     // disable echoing
		ssh.TTY_OP_ISPEED: 14400, // input speed = 14.4kbaud
		ssh.TTY_OP_OSPEED: 14400, // output speed = 14.4kbaud
	}

	if err := session.RequestPty("xterm", 80, 40, modes); err != nil {
		session.Close()
		fmt.Errorf("request for pseudo terminal failed: %s", err)
		return
		//return nil
	}

	b, err := session.CombinedOutput("uname -a")
	if err != nil {
		panic(err)
	}
	fmt.Print(string(b))
}