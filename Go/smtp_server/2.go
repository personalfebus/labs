package main

import (
	"fmt"
	"github.com/sparrc/go-ping"
	"time"
)

func ping_ur(){
	counter := 0
	pinger, err := ping.NewPinger("www.google.com")
	if err != nil {
		fmt.Printf("ERROR: %s\n", err.Error())
		return
	}
	pinger.SetPrivileged(true)
	//pinger.Timeout.Truncate(1)
	pinger.OnRecv = func(pkt *ping.Packet) {
		counter++
		/*fmt.Printf("%d bytes from %s: icmp_seq=%d time=%v\n",
			pkt.Nbytes, pkt.IPAddr, pkt.Seq, pkt.Rtt)*/
		if counter == 30 {
			pinger.Stop()
		}
	}
	pinger.OnFinish = func(stats *ping.Statistics) {
		fmt.Printf("\n--- %s ping statistics ---\n", stats.Addr)
		fmt.Printf("%d packets transmitted, %d packets received, %v%% packet loss\n",
			stats.PacketsSent, stats.PacketsRecv, stats.PacketLoss)
		fmt.Printf("round-trip min/avg/max/stddev = %v/%v/%v/%v\n",
			stats.MinRtt, stats.AvgRtt, stats.MaxRtt, stats.StdDevRtt)
	}
	fmt.Printf("PING %s (%s):\n", pinger.Addr(), pinger.IPAddr())
	pinger.Run()
}

func main(){
	//for i := 0; i < 1000; i++{
	//	fmt.Println("123")
	//
	//}
	//go ping_ur_mom()
	for i := 0; i < 10000; i++{
		go ping_ur()
	}
	<-time.After(10*time.Second)
	fmt.Println("123")
}