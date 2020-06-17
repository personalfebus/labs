package main

import (
	"fmt"
	"github.com/jlaffaye/ftp"
	"io/ioutil"
	"os"
	"strings"

	"bufio"
	"log"
	"strconv"
	//"strings"
)

func main(){
	c, err := ftp.Dial("students.yss.su:21")
	//c, err := ftp.Dial("localhost:2121")
	//c, err := ftp.Dial("185.20.227.83:3110")
	if err != nil {
		log.Fatal(err)
	}
	err = c.Login("ftpiu8", "3Ru7yOTA")
	//err = c.Login("11", "shutup")
	if err != nil {
		log.Fatal(err)
	}

	a:for ;;{
		in := bufio.NewReader(os.Stdin)
		s, _ := in.ReadString('\n')
		for {
			if (s[len(s) - 1] != 10) && (s[len(s) - 1] != 13){
				break
			}
			s = s[:len(s) - 1]
		}
		//fmt.Println(s)
		ss := strings.Split(s, " ")
		//fmt.Println(ss[0])
		fmt.Printf("%s pop\n", ss[0])
		//fmt.Println(len(ss))
		//fmt.Printf("%s!=get_content\n",ss[0])
		//continue
		//fmt.Println(ss[0])
		//continue
		switch ss[0] {
		case "get_content":{
			if len(ss) < 2 {
				fmt.Println("Path not entered")
				continue
			}
			files, err1 := c.List(ss[1])
			if err1 != nil {
				fmt.Println(err1)
				return
			}
			for i := range files {
				fmt.Println(files[i].Name)
			}
		}
		case "upload":{
			if len(ss) < 2 {
				fmt.Println("Name not entered")
				continue
			}
			t, err1 := os.Open(ss[2])
			if err1 != nil {
				fmt.Println("err1 -> ", err1)
				return
			}
			err2 := c.Stor(t.Name(), t)
			if err2 != nil {
				fmt.Println("err2 -> ", err2)
				return
			}
		}
		case "download":{
			if len(ss) < 2 {
				fmt.Println("Name not entered")
				continue
			}
			resp, err1 := c.Retr(ss[1])
			if err1 != nil {
				fmt.Println("err1 -> ", err1)
				return
			}
			buff, err2 := ioutil.ReadAll(resp)
			if err2 != nil {
				fmt.Println("err2 -> ", err2)
				return
			}
			if len(ss) == 2 {
				fmt.Println(string(buff))
			} else {
				fl, err3 := os.Create(ss[2])
				if err3 != nil {
					fmt.Println("err3 -> ", err3)
					return
				}
				_, err4 := fl.Write(buff)
				if err4 != nil {
					fmt.Println("err4 -> ", err4)
					return
				}
			}
		}
		case "make_dir":{
			if len(ss) < 2 {
				fmt.Println("Name not entered")
				continue
			}
			if len(ss) == 3 {
				n, errlol := strconv.ParseInt(ss[2], 10, 64)
				if errlol != nil {
					fmt.Println("errlol -> ", errlol)
					return
				}
				for i := 1; i < int(n); i++ {
					str := ""
					curr := i
					for curr != 0 {
						str = string(curr%10+48) + str
						curr = curr / 10
					}
					c.MakeDir(os.Args[2] + " " + str)
				}
			}
			err1 := c.MakeDir(ss[1])
			if err1 != nil {
				fmt.Println("err1 -> ", err1)
				return
			}
		}
		case "remove_dir":{
			if len(ss) < 2 {
				fmt.Println("Name not entered")
				continue
			}
			err1 := c.RemoveDir(ss[1])
			if err1 != nil {
				fmt.Println("err1 -> ", err1)
				return
			}
		}
		case "remove_file":{
			if len(ss) < 2 {
				fmt.Println("Name not entered")
				continue
			}
			err1 := c.Delete(ss[1])
			if err1 != nil {
				fmt.Println("err1 -> ", err1)
				return
			}
		}
		case "cm":{
			fmt.Println("get_content remove_file remove_dir make_dir download upload close")
		}
		case "close":{
			break a
		}
		default:{
			fmt.Printf("There is no command %s\nType cm to see all commands\n", s)
		}
		}
	}

	if err := c.Quit(); err != nil {
		log.Fatal(err)
	}
}
