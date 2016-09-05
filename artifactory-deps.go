package main

import
//    "bufio"
(
	"encoding/json"
	"fmt"
	"io/ioutil"
	"reflect"
)

//    "io"
//    "os"

func main() {
	artifacts := getArtifactsByGAV("ggg", "aaa", "vvv")
	fmt.Println(reflect.TypeOf(artifacts))

	// dependencies := getDependencies(artifacts)
	// fmt.Println(artifacts)
	a := artifacts.(map[string]interface{}) // type assertion
	for k := range a {
		fmt.Println(k)
	}
}

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func getArtifactsByGAV(groupID string, artifactID string, version string) interface{} {
	fmt.Println(groupID)
	fmt.Println(artifactID)
	fmt.Println(version)

	dat, err := ioutil.ReadFile("./sampleOutput/output.json")
	check(err)
	// fmt.Print(string(dat))

	var artifacts interface{}
	err = json.Unmarshal(dat, &artifacts)
	check(err)
	return artifacts
}
