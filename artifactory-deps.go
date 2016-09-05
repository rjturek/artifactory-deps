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

var baseURL string
var group string
var artifact string
var version string

var consumerMap = make(map[string][]string, 10)

func main() {
	processCommandLine()
	produceReport()
}

func processCommandLine() {
	// fake for now
	baseURL = "http://pirepo/artifactory/api/"
	group = "ggg"
	artifact = "aaa"
	version = "vvv"
}

func produceReport() {
	artifacts := getArtifactsByGAV(group, artifact, version)
	fmt.Println(reflect.TypeOf(artifacts))

	// dependencies := getDependencies(artifacts)
	// fmt.Println(artifacts)
	jsonMap := artifacts.(map[string]interface{}) // type assertion
	for key, val := range jsonMap {
		fmt.Println(key)
		fmt.Println(reflect.TypeOf(val))
		artifactMap := val.(map[string]interface{})
		for k, v := range artifactMap {
			fmt.Println(k)
			fmt.Println(reflect.TypeOf(v))
		}
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
