package main

import (
	"bytes"
	"fmt"
	"strconv"
	"strings"
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

func handlerMessage(init string, in chan string, out chan string, id string) {
	var b bytes.Buffer

	if init != "" {
		b.WriteString(upperNext(init))
	} else {
		b.WriteString(upperNext(<-in))
	}

	fmt.Println(id, ":", b.String())

	out <- b.String()

	if b.String() == strings.ToUpper(b.String()) {
		//fmt.Println("dead")
		//fmt.Println("FIM", <-out)
		//close(out)
	} else {
		//fmt.Println("alive")
	}
}

func endPrint(in chan string) {
	for {
		s := <- in

		if s == strings.ToUpper(s) {
			fmt.Println("dead")
			fmt.Println("FIM", ":", s)
			//close(in)
		} else {
			fmt.Println("alive")
		}
	}
}

func main() {
	// var firstChan = make(chan string)
	var lastChan = make(chan string)
	var nextChan chan string
	var prevChan = make(chan string)

	s := "ozuulziqwnvzrtkgbtlmgmwayeigespfvmpxxuwyghnmssrqkiyahpgpijybrmuiwudsltljwyazgmjj"
	go handlerMessage(s, lastChan, prevChan, "0")

	for i := 1; i < 29; i++ {
		nextChan = make(chan string)
		go handlerMessage("", prevChan, nextChan, strconv.Itoa(i))
		prevChan = nextChan
	}

	go handlerMessage("", prevChan, lastChan, "29")

	go endPrint(lastChan)
}
