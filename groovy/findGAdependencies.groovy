// Main map - keyed by sha1sum
def sha1map = new HashMap()

//def baseApiUrl = "http://app2075:nunya@repo.troweprice.com/artifactory/api"
def baseApiUrl = "http://repo.troweprice.com/artifactory/api"

def mainUrlString = baseApiUrl +  
		"/search/gavc?g=com.trp.jvl.core&a=JVL_Core&repos=release,release-candidate,snapshot"
//		"/search/gavc?g=com.trp.jvl.core&a=JVL_Core&v=3.1.0-rc2&repos=release,release-candidate,snapshot"

///////////////////////////////////////
def getJson = { jsonText ->
	def slurper = new groovy.json.JsonSlurper()
	return slurper.parseText(jsonText)
}

///////////////////////////////////////
def getUrlText =  { url ->
	return new URL(url).text
}

///////////////////////////////////////
def getJsonFromUrl = { url ->
	return getJson(getUrlText(url))
}

/////////////////////////////////////
def getFileSha1sum = { uri -> 
	jsonText = getJsonFromUrl(uri)
	return jsonText.checksums.sha1
}

def getDependencyBuilds = { sha1sum ->
	builds = []
	theJson = getJsonFromUrl( baseApiUrl + "/search/dependency?sha1=" + sha1sum)
	theJson.results.each { depBuild ->
		noSpaces = depBuild.uri.replaceAll("%20", "")
		elems = noSpaces.split("::")
		def mne = elems[0].split("build/")[1]
		def ru  = elems[1]
		def build = elems[2]
		builds << ["mne":mne, "ru":ru, "build":build ]
	}
	return builds
}


///////////////////////////////////////
def handleArtifact = { uri ->
	if (!uri.endsWith(".jar")) {
		return
	}

	def sha1sum = getFileSha1sum(uri)

	if (! sha1map.get(sha1sum)) {
		def consumerGroups = [] as Set
		sha1map.put(sha1sum, [
					"uris":[], 
					"consumerBuilds":[], 
					"consumerGroups":consumerGroups
					])
	}

	def fileSha1Elem = sha1map.get(sha1sum)
	fileSha1Elem['uris'] << uri
	getDependencyBuilds(sha1sum).each { aBuild ->
		fileSha1Elem['consumerBuilds']	<< aBuild
		def buildGroup = "com.trp.${aBuild.mne}.${aBuild.ru}"
		fileSha1Elem['consumerGroups'] << buildGroup
	}
}


/////////////////////////////////////
// MAIN
def theJson = getJsonFromUrl(mainUrlString)

theJson.results.each { uriElem ->
//	println uriElem
	handleArtifact(uriElem.uri)
}

mapJsonString = groovy.json.JsonOutput.toJson(sha1map)

//// Output JSON string
// println mapJsonString

//// Output pretty JSON string
//println groovy.json.JsonOutput.prettyPrint(mapJsonString)

//// Report listing
sha1map.each { elem ->
	//println "sha1sum: $elem.key"
	
	// println "   uris"
	// elem.value['uris'].each { uri ->
	// 	println "      $uri"
	// }

	// Assume only one uri per sha1
	println "   consumedGroup"
	def uri = elem.value['uris'][0]
	def s1 = uri.substring(uri.indexOf("com/trp"))
	pathElems = s1.split("/")
	def consumedGAV = pathElems[0..3].join(".") + ":" + pathElems[4] + ":" + pathElems[5] 
	println "      $consumedGAV"

	// println "   consumerBuilds:"
	// elem.value['consumerBuilds'].each { aBuild ->
	// 	println "      $aBuild"
	// }
	println "   consumerGroups:"
	elem.value['consumerGroups'].each { aGroup ->
		println "       $aGroup"
	}
	
	println "\n"
}
