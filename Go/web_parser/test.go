package main

import "golang.org/x/net/html"

func search123(node *html.Node) []*Item {
	var items []*Item
	if isDiv(node, "row") {
		//log.Print(node)
		//var items []*Item
		for c := node.FirstChild; c != nil; c = c.NextSibling {
			//log.Print("1231331")
			//log.Print(c)
			if isDiv(c, "span8 js-main__content") {
				//log.Print("333")
				for k := c.FirstChild; k != nil; k = k.NextSibling {
					//log.Print(k)
					//log.Print("this is")
					if isDiv(k, "row b-top7-for-main js-top-seven") {
						//log.Print("span4")
						for z := k.FirstChild; z != nil; z = z.NextSibling {
							if isDiv(z , "span4") {
								//log.Print("test")
								for r := z.FirstChild; r != nil; r = r.NextSibling {
									//log.Print("argv")
									if isDiv(r, "item") {
										//log.Print("take")
										if item := readItem(r); item != nil {
											items = append(items, item)
											//log.Print(item.Title)
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
