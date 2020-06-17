package main

import (
	"fmt"
	"github.com/lixiangzhong/traceroute"
)

func main() {
	t := traceroute.New("google.com")
	fmt.Println("1")
	//t.MaxTTL=30
	//t.Timeout=3 * time.Second
	//t.LocalAddr="0.0.0.0"
	result, err := t.Do()
	fmt.Println("2")
	if err != nil {
		fmt.Println(err)
		return
	}
	fmt.Println("3")
	for _, v := range result {
		fmt.Println(v)
	}
	fmt.Println("4")
}