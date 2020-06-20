package main

import (
	"fmt"
	"golang.org/x/net/html"
	"io/ioutil"
	"net/http"
	"log"
	"strings"
	"encoding/xml"
)

type Urlset struct {
	Url []Url `xml:"url"`
}

type Url struct {
	Loc string `xml:"loc"`
	Changefreq xml.Name `xml:"changefreq"`
}

//type Xmlref struct {
//	Ref xml.Name `xml:"loc"`
//}

var (
	uslugihtmlcounter = 0
	xmluslugicounter = 0
	prefixhtml = "http://lab-sud.ru"
	allrefs []string
	allxmlrefs []string
)

func main(){
	getNews(prefixhtml)
	getXML()
	fmt.Println("xml count =", xmluslugicounter)
	fmt.Println("html count =", uslugihtmlcounter)
}

func getXML()  {
	resp, err := http.Get("http://lab-sud.ru/sitemap.xml")
	if err != nil{
		log.Fatal(err)
	}
	defer resp.Body.Close()

	byteValue, err := ioutil.ReadAll(resp.Body)
	if err != nil{
		log.Fatal(err)

	}
	var xmlrefs Urlset
	err = xml.Unmarshal(byteValue, &xmlrefs)
	if err != nil{
		log.Fatal(err)
	}

	for _, y := range xmlrefs.Url{
		if strings.Contains(y.Loc, "/uslugi/"){
			boo := true
			for _, x := range allrefs{
				if x == y.Loc{
					boo = false
					break
				}
			}
			if boo{
				xmluslugicounter++
				allxmlrefs = append(allxmlrefs, y.Loc)
				fmt.Println("found xml ref:", y.Loc, "; Number:", xmluslugicounter)
			}
		}
	}
}

func getAttr(node *html.Node, key string) string {
	for _, attr := range node.Attr {
		if attr.Key == key {
			return attr.Val
		}
	}
	return ""
}

func getNews(site string) {
	response, err := http.Get(site)

	if err != nil{
		log.Fatal(err)
	} else {
		defer response.Body.Close()
		status := response.StatusCode

		if status == http.StatusOK{
			doc, err := html.Parse(response.Body)
			if err != nil {
				fmt.Println("invalid HTML from", site, "error", err)
			} else {
				//fmt.Println("HTML parsed successfully")
				search(doc)
			}
		} else {
			log.Fatal(status)
		}
	}
	//return nil
}

func search(node *html.Node) {
	ref := getAttr(node, "href")
	iter := counthtml(ref)
	if iter{
		getNews(prefixhtml + ref)
	}
	for c := node.FirstChild; c != nil; c = c.NextSibling {
		search(c)
	}
}

func counthtml(ref string) bool{
	if strings.Contains(ref, "/uslugi/"){
		for _, x := range allrefs{
			if x == ref{
				return false
			}
		}
		uslugihtmlcounter++
		allrefs = append(allrefs, ref)
		fmt.Println("found html ref:", ref, "; Number:", uslugihtmlcounter)
		return true
	}
	return false
}