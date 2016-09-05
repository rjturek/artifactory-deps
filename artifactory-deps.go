package main

import
//    "bufio"
(
	"encoding/json"
	"fmt"
	"io/ioutil"
)

//    "io"

//    "os"

func main() {
	artifacts := getArtifacts("ggg", "aaa", "vvv")
	dependencies := getDependencies(artifacts)
}

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func getArtifactsByGAV(groupID string, artifactID string, version string) artifacts {
	fmt.Println(groupID)
	fmt.Println(artifactID)
	fmt.Println(version)

	dat, err := ioutil.ReadFile("./sampleOutput/output.json")
	check(err)
	fmt.Print(string(dat))

	var artifacts interface{}
	err = json.Unmarshal(b, &artifacts)
	check(err)
	return artifacts
}
