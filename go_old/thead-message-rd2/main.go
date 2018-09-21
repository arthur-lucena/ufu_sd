package main

import (
	"fmt"
)


func upperNext(s string) string {
	var c rune
	var in int
	var h bool

	for i, cs := range s {
		c = rune(cs)
		if c > 96 && c < 123{
			c = c - 32
			in = i
			h = true
			break
		}
	}

	if h {
		r := []rune(s)
		r[in] = c
		return string(r)
	} else {
		return s
	}
}

func main() {
	s := "qXwe"
	fmt.Println(s)
	s = upperNext(s)
	fmt.Println(s)
	s = upperNext(s)
	fmt.Println(s)
	s = upperNext(s)
	fmt.Println(s)
	s = upperNext(s)
	fmt.Println(s)
	s = upperNext(s)
	fmt.Println(s)
}
