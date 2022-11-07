def call(String name = 'human') {
    echo "Hello, ${name}."
}

def String getNextVersion(String filename){
    // Get tags file
    env.WORKSPACE = pwd()
    def version = readFile "${env.WORKSPACE}/" + filename
    
    // Get greatest number
    def myList = version.readLines()
    mylist = myList.sort().reverse()
    def latestVersion = myList[-1]
    
    // Get MINOR number, casting it to int and then increment it by one
    def MINOR =Integer.parseInt(latestVersion.split('\\.')[2])
    MINOR = MINOR+1
    
    return  "1.0.".concat( MINOR.toString() )
}
