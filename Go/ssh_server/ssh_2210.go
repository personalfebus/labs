package main

import (
	//"bufio"
	"fmt"
	//ssh2 "github.com/gliderlabs/ssh"
	"golang.org/x/crypto/ssh"
	"golang.org/x/crypto/ssh/agent"
	"io"
	"io/ioutil"
	"net"
	"os"
	"strings"
)

type SSHCommand struct {
	Path   string
	Env    []string
	Stdin  io.Reader
	Stdout io.Writer
	Stderr io.Writer
}

type SSHClient struct {
	Config *ssh.ClientConfig
	Host   string
	Port   int
}

func (client *SSHClient) RunCommand(cmd *SSHCommand) error {
	var (
		session *ssh.Session
		err     error
	)

	if session, err = client.newSession(); err != nil {
		return err
	}
	defer session.Close()

	if err = client.prepareCommand(session, cmd); err != nil {
		return err
	}

	err = session.Run(cmd.Path)
	return err
}

func (client *SSHClient) prepareCommand(session *ssh.Session, cmd *SSHCommand) error {
	for _, env := range cmd.Env {
		variable := strings.Split(env, "=")
		if len(variable) != 2 {
			continue
		}

		if err := session.Setenv(variable[0], variable[1]); err != nil {
			return err
		}
	}

	if cmd.Stdin != nil {
		stdin, err := session.StdinPipe()
		if err != nil {
			return fmt.Errorf("Unable to setup stdin for session: %v", err)
		}
		go io.Copy(stdin, cmd.Stdin)
	}

	if cmd.Stdout != nil {
		stdout, err := session.StdoutPipe()
		if err != nil {
			return fmt.Errorf("Unable to setup stdout for session: %v", err)
		}
		go io.Copy(cmd.Stdout, stdout)
	}

	if cmd.Stderr != nil {
		stderr, err := session.StderrPipe()
		if err != nil {
			return fmt.Errorf("Unable to setup stderr for session: %v", err)
		}
		go io.Copy(cmd.Stderr, stderr)
	}
	return nil
}



func (client *SSHClient) newSession() (*ssh.Session, error) {
	connection, err := ssh.Dial("tcp", fmt.Sprintf("%s:%d", client.Host, client.Port), client.Config)
	if err != nil {
		return nil, fmt.Errorf("Failed to dial: %s", err)
	}
	session, err := connection.NewSession()

	if err != nil {
		return nil, fmt.Errorf("Failed to create session: %s", err)
	}

	modes := ssh.TerminalModes{
		ssh.ECHO:          0,     // disable echoing
		ssh.TTY_OP_ISPEED: 14400, // input speed = 14.4kbaud
		ssh.TTY_OP_OSPEED: 14400, // output speed = 14.4kbaud
	}
	//fmt.Println("123")
	if err := session.RequestPty("xterm", 80, 40, modes); err != nil {
		session.Close()
		return nil, fmt.Errorf("request for pseudo terminal failed: %s", err)
	}

	//if err := session.Shell(); err != nil {
	//	log.Fatal("failed to start shell: ", err)
	//}

	return session, nil
}

func PublicKeyFile(file string) ssh.AuthMethod {
//func PublicKeyFile(file string) ssh.PublicKey {
	buffer, err := ioutil.ReadFile(file)
	if err != nil {
		return nil
	}
	key, err := ssh.ParsePrivateKey(buffer)

	if err != nil {
		return nil
	}

	return ssh.PublicKeys(key)
	//return key.PublicKey()
}

func SSHAgent() ssh.AuthMethod {
	//fmt.Println(os.Getenv("SSH_AUTH_SOCK"))
	if sshAgent, err := net.Dial("tcp", os.Getenv("SSH_AUTH_SOCK")); err == nil {
		return ssh.PublicKeysCallback(agent.NewClient(sshAgent).Signers)
	}
	return nil
}

func main() {
	// ssh.Password("your_password")
	//in := bufio.NewReader(os.Stdin)
	//s, _ := in.ReadString('\n')
	//for {
	//	if (s[len(s) - 1] != 10) && (s[len(s) - 1] != 13){
	//		break
	//	}
	//	s = s[:len(s) - 1]
	//}
	sshConfig := &ssh.ClientConfig{
		User: "iu9_32_10",
		Auth: []ssh.AuthMethod{
			//SSHAgent(),
			ssh.Password("0654"),
		},
		HostKeyCallback: ssh.InsecureIgnoreHostKey(),
		//HostKeyCallback: ssh.FixedHostKey(PublicKeyFile("./id_rsa")),
	}

	client := &SSHClient{
		Config: sshConfig,
		Host:   "185.20.227.83",
		//Host:   "localhost",
		Port:   2210,
	}

	cmd := &SSHCommand{
		//передать строку с mkdir or ls or rn
		//!!
		//!!б
		Path:   "ls /",
		Env:    []string{"/"},
		Stdin:  os.Stdin,
		Stdout: os.Stdout,
		Stderr: os.Stderr,
	}

	fmt.Printf("Running command: %s\n", cmd.Path)
	if err := client.RunCommand(cmd); err != nil {
		fmt.Fprintf(os.Stderr, "command run error: %s\n", err)
		os.Exit(1)
	}
}