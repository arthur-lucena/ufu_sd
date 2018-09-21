package main

import (
	"bytes"
	"strconv"
)

func handlerMessage(in chan string, out chan string, id string) {
	var b bytes.Buffer
	b.WriteString(<-in)
	b.WriteString("olá ")
	b.WriteString(id)
	b.WriteString(" thead\n")
	out <- b.String()
}

func main() {

	// s := "ozuulziqwnvzrtkgbtlmgmwayeigespfvmpxxuwyghnmssrqkiyahpgpijybrmuiwudsltljwyazgmjj"


	var firstChan = make(chan string)
	var lastChan chan string
	var c chan string
	for i := 1; i < 30; i++ {
		c = make(chan string)
		if i == 1 {
			go handlerMessage(firstChan, c, strconv.Itoa(i))
		} else {
			if i == 29 {
				lastChan = make(chan string)
				go handlerMessage(c, lastChan, strconv.Itoa(i))
			} else {
				c = make(chan string)
				go handlerMessage(c, c, strconv.Itoa(i))
			}
		}
	}

	lastChan <- "oi =D"
	go handlerMessage(lastChan, firstChan, "0")

}
