package main

import (
	"fmt"
	"golang.org/x/net/html"
	"log"
	"net/http"
	"time"
)

type Item struct {
	Subject, Ref, HotTime, HotSource, HotBody, Title string
}

func getAttr(node *html.Node, key string) string {
	for _, attr := range node.Attr {
		if attr.Key == key {
			return attr.Val
		}
	}
	return ""
}

func isElem(node *html.Node, tag string) bool {
	return node != nil && node.Type == html.ElementNode && node.Data == tag
}

func isDiv(node *html.Node, class string) bool {
	return (isElem(node, "section") || isElem(node, "div")) && getAttr(node, "class") == class
}

func main(){
	refreshrate, _ := time.ParseDuration("5m")
	fmt.Println(int(refreshrate.Seconds()))
	counter := 0
	startitems := getNews()
	if startitems == nil {
		log.Fatal("VERY BAD")
	}
	for _, x := range startitems {
		if x.HotBody != "" || x.HotTime != "" || x.HotSource != "" {
			fmt.Println("----------------- HOT TOPIC ---------------------")
			fmt.Println(x.Subject)
			fmt.Println(x.HotTime)
			fmt.Println(x.HotSource)
			fmt.Println(x.Title)
			fmt.Println(x.Ref)
			fmt.Println(x.HotBody)
		} else {
			fmt.Println("-------------------------------------------------")
			fmt.Println(x.Subject)
			fmt.Println(x.Title)
			fmt.Println(x.Ref)
		}
	}
	fmt.Println("#################################################")
	time.Sleep(refreshrate)

	for {
		counter++
		items := getNews()
		if items == nil {
			log.Fatal("VERY BAD")
		}
		outputFunc(startitems, items, counter)
		startitems = items
		//fmt.Println("Good")
		time.Sleep(refreshrate)
	}
}

func outputFunc(startitems []Item ,items []Item, counter int){
	fmt.Println("==============================================================")
	fmt.Println("CHANGELOG :", counter)
	for _, x := range items{
		noequ := true
		for _, y := range startitems{
			if x.Ref == y.Ref{
				noequ = false
				break
			}
		}
		if noequ {
			if x.HotBody != "" || x.HotTime != "" || x.HotSource != "" {
				fmt.Println("-------------------------------------------------")
				fmt.Println("---------------- New Hot Topic ------------------")
				fmt.Println("-------------------------------------------------")
				fmt.Println(x.Subject)
				fmt.Println(x.HotTime)
				fmt.Println(x.HotSource)
				fmt.Println(x.Title)
				fmt.Println(x.Ref)
				fmt.Println(x.HotBody)
				fmt.Println("-------------------------------------------------")
			}else {
				fmt.Println("------------------ New Topic --------------------")
				fmt.Println(x.Subject)
				fmt.Println(x.Title)
				fmt.Println(x.Ref)
			}
		}
	}
	fmt.Println("==============================================================")
}

func getNews() []Item{
	response, err := http.Get("https://news.mail.ru/")

	if err != nil{
		log.Fatal(err)
	} else {
		defer response.Body.Close()
		status := response.StatusCode

		if status == http.StatusOK{
			doc, err := html.Parse(response.Body)

			if err != nil {
				fmt.Println("invalid HTML from lenta.ru", "error", err)
			} else {
				//fmt.Println("HTML parsed successfully")
				return search(doc)
			}
		} else {
			log.Fatal(status)
		}
	}
	return nil
}

func parseItem(node *html.Node) []Item{
	if isDiv(node, "cols__inner"){
		subject := parseSubject(node.FirstChild)
		topItem := parseTop(node.FirstChild.NextSibling, subject)
		items := parseOther(node.LastChild, subject)
		items = append(items, topItem)
		return items
	}
	return nil
}

func parseOther(node *html.Node, subject string) []Item{
	var items []Item
	if getAttr(node, "class") == "list list_type_square list_overflow"{
		for c := node.FirstChild; c != nil; c = c.NextSibling{
			var item Item
			z := c.FirstChild.FirstChild
			if isElem(z, "a"){
				item.Ref = "https://news.mail.ru/" + getAttr(z, "href")
				item.Title = z.FirstChild.FirstChild.Data
				item.Subject = subject
				item.HotSource = ""
				item.HotBody = ""
				item.HotTime = ""
				items = append(items, item)
			}
		}
	}
	return items
}

func parseTop(node *html.Node, subject string) Item{
	var item Item
	if isDiv(node, "newsitem newsitem_height_fixed js-ago-wrapper"){
		timeseg := node.FirstChild
		if isDiv(timeseg, "newsitem__params"){
			c := timeseg.FirstChild.FirstChild
			item.HotTime = c.Data
			c = timeseg.LastChild.FirstChild
			item.HotSource = c.Data
		} else{
			item.HotTime = "TIME_NOT_FOUND"
			item.HotSource = "SOURCE_NOT_FOUND"
		}
		textseg := node.LastChild
		item.Title = textseg.FirstChild.FirstChild.FirstChild.Data
		item.HotBody = textseg.LastChild.FirstChild.Data
		item.Ref = "https://news.mail.ru/" + getAttr(textseg.FirstChild, "href")
		item.Subject = subject
	} else {
		item.HotTime = "ERROR"
		item.HotSource = "ERROR"
		item.Title = "ERROR"
		item.HotBody = "ERROR"
		item.Ref = "ERROR"
		item.Subject = "ERROR"
	}
	return item
}

func parseSubject(node *html.Node) string{
	if isDiv(node, "hdr hdr_bold_huge margin_bottom_10"){
		c := node.FirstChild
		if isDiv(c, "hdr__wrapper"){
			c = c.FirstChild
			if isElem(c, "a"){
				return c.FirstChild.FirstChild.Data
			}
		}
	}
	return ""
}

func search(node *html.Node) []Item{
	var items []Item

	if isDiv(node, "block block_separated_top rb_nat"){
		c := node.FirstChild
		if isDiv(c, "wrapper js-module"){
			c = c.FirstChild
			if isDiv(c, "cols cols_margin cols_percent"){
				c = c.FirstChild
				if isDiv(c, "cols__wrapper"){
					for k := c.FirstChild; k != nil; k = k.NextSibling{
						tmp := parseItem(k.FirstChild)
						if tmp != nil {
							items = append(items, tmp...)
						}
					}
					return items
				}
			}
		}
	}

	for c := node.FirstChild; c != nil; c = c.NextSibling {
		items := search(c)
		if items != nil{
			return items
		}
	}
	return nil
}