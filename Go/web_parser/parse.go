package main

import (
	"fmt"
	"golang.org/x/net/html"
	"net/http"
)

func getChildren(node *html.Node) []*html.Node {
	var children []*html.Node
	for c := node.FirstChild; c != nil; c = c.NextSibling {
		children = append(children, c)
	}
	return children
}

func getAttr(node *html.Node, key string) string {
	for _, attr := range node.Attr {
		if attr.Key == key {
			return attr.Val
		}
	}
	return ""
}

func isText(node *html.Node) bool {
	return node != nil && node.Type == html.TextNode
}

func isElem(node *html.Node, tag string) bool {
	return node != nil && node.Type == html.ElementNode && node.Data == tag
}

func isDiv(node *html.Node, class string) bool {
	return (isElem(node, "section") || isElem(node, "div")) && getAttr(node, "class") == class
}

func readItem(item *html.Node) *Item {
	if a := item.FirstChild; isElem(a, "a") {
		// && isText(cs[0])
		cs := getChildren(a)
		//fmt.Println(cs)
		//fmt.Println("===========")
		if /*(len(cs) == 2) && */isElem(cs[0], "time") && isText(cs[1]) {
			return &Item{
				Ref:   getAttr(a, "href"),
				Time:  getAttr(cs[0], "title"),
				Title: cs[1].Data,
			}
		}
	}
	return nil
}

type Item struct {
	Ref, Time, Title string
}


func downloadNews() []*Item {
	fmt.Println("sending request to lenta.ru")
	response, err := http.Get("http://lenta.ru")
	//fmt.Println(response)
	//fmt.Println("=================================")
	if err != nil {
		fmt.Println("request to lenta.ru failed", "error", err)
	} else {
		defer response.Body.Close()
		//fmt.Println(response.Body)
		//fmt.Println("=================================")
		status := response.StatusCode
		fmt.Println("got response from lenta.ru", "status", status)
		if status == http.StatusOK {
			doc, err := html.Parse(response.Body)
			//fmt.Println(doc)
			if err != nil {
				fmt.Println("invalid HTML from lenta.ru", "error", err)
			} else {
				fmt.Println("HTML from lenta.ru parsed successfully")
				return search(doc)
				//return search1(doc)
			}
		}
	}
	return nil
}

func search(node *html.Node) []*Item {
	var items []*Item
	//fmt.Println(node.Type)
	if isDiv(node, "row") {
		//fmt.Println("row here")
		for c := node.FirstChild; c != nil; c = c.NextSibling {
			if isDiv(c, "span8 js-main__content") {
				//fmt.Println("main content here")
				for k := c.FirstChild; k != nil; k = k.NextSibling {
					if isDiv(k, "row b-top7-for-main js-top-seven") {
						//fmt.Println("top 7 here")
						for z := k.FirstChild; z != nil; z = z.NextSibling {
							if isDiv(z , "span4") {
								for d := z.FirstChild; d != nil; d = d.NextSibling {
									if isDiv(d, "item") {
										//fmt.Println("item")
										//fmt.Println(d.FirstChild.Data)
										if item := readItem(d); item != nil {
											items = append(items, item)
										}
									} else if isDiv(d, "first-item"){
										//fmt.Println("first-item")
										//fmt.Println(r.FirstChild.Data)
										if item := readItem(d); item != nil {
											items = append(items, item)
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return items
	}
	for c := node.FirstChild; c != nil; c = c.NextSibling {
		if items := search(c); items != nil {
			return items
		}
	}
	return nil
}

//===================================================================================================

func main() {
	fmt.Println("Downloader started")
	arr := downloadNews()
	fmt.Println(len(arr))
	for _, x := range arr{
		fmt.Println("Title = ", x.Title)
		fmt.Println("Ref = ", x.Ref)
		fmt.Println("Time = ", x.Time)
	}
}