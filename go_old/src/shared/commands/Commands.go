package commands

import (
	"fmt"
)

func lol(params []string) {
	fmt.Print("essa é a exec do lol.\n")
}

func rsrs(params []string) {
	fmt.Print("essa é a exec do rsrs.\n")
}

var m = map[string]func(params []string) {
	"lol": lol,
	"rsrs": rsrs,
}

func Execute(name string, params []string) {
	m[name](params)
}