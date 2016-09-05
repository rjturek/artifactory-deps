package main

import (
	"fmt"
	"testing"
)

func TestSomething(t *testing.T) {
	a := doIt()
	fmt.Println("Test, got:", a)
	if a != "funkyx" {
		t.Error("Didn't get funky")
	}
}
